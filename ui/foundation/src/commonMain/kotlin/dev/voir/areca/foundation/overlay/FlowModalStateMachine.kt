package dev.voir.areca.foundation.overlay

import androidx.compose.runtime.Immutable
import kotlin.math.abs

/**
 * Where a [FlowModalController] is and where it is heading, as one value.
 *
 * Invariant kept by [FlowModalStateMachine]: [targetState] is always a resting state
 * ([FlowModalState.Hidden], [FlowModalState.PartiallyOpened] or [FlowModalState.Expanded]), and it
 * equals [state] whenever [state] is itself a resting state.
 *
 * @property state Current state.
 * @property targetState Resting state the modal is moving to, or resting in.
 */
@Immutable
internal data class FlowModalPhase(
    val state: FlowModalState,
    val targetState: FlowModalState,
)

/**
 * Transitions of a flow modal, kept apart from the composable that animates them.
 *
 * Every decision the modal makes about its state — what a call to show or hide does, where a drag
 * comes to rest, what an animation that finished leaves behind — is answered here by functions that
 * take no state and can be tested without a composition. [FlowModalController] holds the current
 * [FlowModalPhase]; [FlowModal] animates the sheet toward it.
 */
internal object FlowModalStateMachine {
    /** States a modal can rest in, and so the only valid initial states and targets. */
    val RestingStates: Set<FlowModalState> = setOf(
        FlowModalState.Hidden,
        FlowModalState.PartiallyOpened,
        FlowModalState.Expanded,
    )

    /** Share of the partial height below which a released drag dismisses the modal. */
    private const val DismissFraction = 0.35f

    /** Share of the only open height above which a released drag keeps the modal open. */
    private const val KeepOpenFraction = 0.5f

    /**
     * Phase of a modal that rests in [state].
     *
     * @param state Initial state.
     * @return Phase resting in [state].
     * @throws IllegalArgumentException When [state] is transitional.
     */
    fun resting(state: FlowModalState): FlowModalPhase {
        require(state in RestingStates) {
            "A flow modal must start in a resting state (one of $RestingStates), was $state"
        }
        return FlowModalPhase(state = state, targetState = state)
    }

    /**
     * Resting state that [phase] is in or heading to.
     *
     * Total over every phase, so a phase that broke the [FlowModalPhase] invariant still resolves to
     * a height instead of recursing.
     *
     * @param phase Phase to resolve.
     * @return [FlowModalState.Hidden], [FlowModalState.PartiallyOpened] or
     *   [FlowModalState.Expanded].
     */
    fun anchorOf(phase: FlowModalPhase): FlowModalState = when (phase.state) {
        FlowModalState.Hidden, FlowModalState.Hiding -> FlowModalState.Hidden
        FlowModalState.PartiallyOpened -> FlowModalState.PartiallyOpened
        FlowModalState.Expanded -> FlowModalState.Expanded
        FlowModalState.Opening -> when (phase.targetState) {
            FlowModalState.Hidden, FlowModalState.Hiding -> FlowModalState.Hidden
            FlowModalState.PartiallyOpened -> FlowModalState.PartiallyOpened
            FlowModalState.Opening, FlowModalState.Expanded -> FlowModalState.Expanded
        }
    }

    /**
     * Height of the sheet when [phase] has come to rest.
     *
     * @param phase Phase to measure.
     * @param partialHeight Height of a partially opened sheet, in pixels.
     * @param expandedHeight Height of an expanded sheet, in pixels.
     * @param autoHeight Whether the sheet sizes to its content, which makes partial mean expanded.
     * @return Target height in pixels.
     */
    fun heightOf(
        phase: FlowModalPhase,
        partialHeight: Float,
        expandedHeight: Float,
        autoHeight: Boolean,
    ): Float = when (anchorOf(phase)) {
        FlowModalState.PartiallyOpened -> if (autoHeight) expandedHeight else partialHeight
        FlowModalState.Expanded -> expandedHeight
        else -> 0f
    }

    /**
     * Opens a hidden or hiding modal; leaves an open one where it is.
     *
     * @param phase Current phase.
     * @param partial Whether to open at the partial height when the modal allows it.
     * @param allowPartial Whether the modal has a partial height.
     * @param autoHeight Whether the sheet sizes to its content.
     * @return Next phase.
     */
    fun show(
        phase: FlowModalPhase,
        partial: Boolean,
        allowPartial: Boolean,
        autoHeight: Boolean,
    ): FlowModalPhase {
        if (phase.state != FlowModalState.Hidden && phase.state != FlowModalState.Hiding) return phase
        val target = when {
            autoHeight -> FlowModalState.Expanded
            partial && allowPartial -> FlowModalState.PartiallyOpened
            else -> FlowModalState.Expanded
        }
        return FlowModalPhase(state = FlowModalState.Opening, targetState = target)
    }

    /**
     * Moves the modal to its expanded height, opening it if it is hidden.
     *
     * @param phase Current phase.
     * @param allowExpand Whether the modal has an expanded height of its own.
     * @param autoHeight Whether the sheet sizes to its content, which always allows expanding.
     * @return Next phase.
     */
    fun expand(phase: FlowModalPhase, allowExpand: Boolean, autoHeight: Boolean): FlowModalPhase =
        if (allowExpand || autoHeight) {
            FlowModalPhase(state = FlowModalState.Opening, targetState = FlowModalState.Expanded)
        } else {
            phase
        }

    /**
     * Moves the modal to its partial height, opening it if it is hidden.
     *
     * @param phase Current phase.
     * @param allowPartial Whether the modal has a partial height.
     * @param autoHeight Whether the sheet sizes to its content, which has no partial height.
     * @return Next phase.
     */
    fun collapse(phase: FlowModalPhase, allowPartial: Boolean, autoHeight: Boolean): FlowModalPhase =
        if (!autoHeight && allowPartial) {
            FlowModalPhase(state = FlowModalState.Opening, targetState = FlowModalState.PartiallyOpened)
        } else {
            phase
        }

    /**
     * Starts hiding a visible modal; leaves a hidden or hiding one alone.
     *
     * @param phase Current phase.
     * @return Next phase.
     */
    fun hide(phase: FlowModalPhase): FlowModalPhase =
        if (phase.state != FlowModalState.Hidden && phase.state != FlowModalState.Hiding) {
            FlowModalPhase(state = FlowModalState.Hiding, targetState = FlowModalState.Hidden)
        } else {
            phase
        }

    /**
     * Phase after a drag released the sheet toward [anchor].
     *
     * The phase is always transitional (or unchanged), so the sheet animates from wherever the
     * finger left it even when [anchor] is the state the modal was already in. A drag cannot undo a
     * hide that was in progress: the modal keeps hiding, so a caller that asked for it hidden is not
     * left with it open.
     *
     * @param phase Phase when the drag ended.
     * @param anchor Resting state the drag settled on.
     * @return Next phase.
     */
    fun settle(phase: FlowModalPhase, anchor: FlowModalState): FlowModalPhase = when {
        phase.state == FlowModalState.Hidden || phase.state == FlowModalState.Hiding -> phase
        anchor == FlowModalState.Hidden || anchor == FlowModalState.Hiding -> hide(phase)
        anchor == FlowModalState.PartiallyOpened ->
            FlowModalPhase(state = FlowModalState.Opening, targetState = FlowModalState.PartiallyOpened)
        else -> FlowModalPhase(state = FlowModalState.Opening, targetState = FlowModalState.Expanded)
    }

    /**
     * Phase once the animation of [phase] has run to its end.
     *
     * @param phase Phase whose animation finished.
     * @return The resting phase it arrives at; a resting phase is returned unchanged.
     */
    fun completed(phase: FlowModalPhase): FlowModalPhase = when (phase.state) {
        FlowModalState.Opening, FlowModalState.Hiding -> resting(anchorOf(phase))
        else -> phase
    }

    /**
     * Resting state a drag released at [height] settles on.
     *
     * Dragged below a fixed share of the partial height, the sheet is dismissed. A sheet with one
     * open height stays open past half of it; one with both settles on the nearest of hidden,
     * partial and expanded, preferring the lower on a tie.
     *
     * @param height Sheet height when the finger lifted, in pixels.
     * @param partialHeight Height of a partially opened sheet, in pixels.
     * @param expandedHeight Height of an expanded sheet, in pixels.
     * @param allowPartial Whether the sheet can rest at [partialHeight].
     * @param allowExpand Whether the sheet can rest at [expandedHeight].
     * @return [FlowModalState.Hidden], [FlowModalState.PartiallyOpened] or
     *   [FlowModalState.Expanded].
     */
    fun dragTarget(
        height: Float,
        partialHeight: Float,
        expandedHeight: Float,
        allowPartial: Boolean,
        allowExpand: Boolean,
    ): FlowModalState = when {
        height <= DismissFraction * partialHeight -> FlowModalState.Hidden

        !allowPartial ->
            if (allowExpand && height > expandedHeight * KeepOpenFraction) {
                FlowModalState.Expanded
            } else {
                FlowModalState.Hidden
            }

        !allowExpand ->
            if (height > partialHeight * KeepOpenFraction) {
                FlowModalState.PartiallyOpened
            } else {
                FlowModalState.Hidden
            }

        else -> nearestOf(
            value = height,
            anchors = listOf(
                FlowModalState.Hidden to 0f,
                FlowModalState.PartiallyOpened to partialHeight,
                FlowModalState.Expanded to expandedHeight,
            ),
        )
    }

    /**
     * Anchor whose height is closest to [value]; the earliest wins a tie.
     *
     * @param value Height to match, in pixels.
     * @param anchors States paired with their heights, lowest first. Must not be empty.
     * @return State of the closest anchor.
     */
    private fun nearestOf(value: Float, anchors: List<Pair<FlowModalState, Float>>): FlowModalState =
        anchors.minBy { (_, height) -> abs(value - height) }.first
}
