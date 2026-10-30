package dev.voir.areca.foundation.selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.minimumTouchTarget
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * What a [Toggle] is set to.
 *
 * The thumb's position is the value: left for [Off], centred for [Indeterminate], right for [On].
 * Availability is *not* a state here — a toggle that cannot be changed is still off or on, and
 * whether it can be changed is [Toggle]'s `enabled` and `readOnly` parameters.
 */
public enum class ToggleState {
    /** Not set. The thumb rests at the start of the track. */
    Off,

    /** Partly set, for a toggle standing in for several others that disagree. Thumb centred. */
    Indeterminate,

    /** Set. The thumb rests at the end of the track. */
    On;

    /**
     * The state a press moves to, given the order the caller cycles through.
     *
     * A state outside [cycleOrder] — as when a tri-state value is shown by a two-state toggle —
     * starts the cycle from its beginning rather than doing nothing, so a press is never ignored.
     *
     * @param cycleOrder States to move through, in order. An empty list leaves the state alone.
     * @return The next state.
     */
    public fun next(cycleOrder: List<ToggleState>): ToggleState {
        if (cycleOrder.isEmpty()) return this
        val index = cycleOrder.indexOf(this)
        return if (index == -1) cycleOrder.first() else cycleOrder[(index + 1) % cycleOrder.size]
    }
}

/** The two states an ordinary on/off toggle moves between. */
public val BinaryToggleCycle: List<ToggleState> = listOf(ToggleState.Off, ToggleState.On)

/** All three states, for a toggle that can report a partial value. */
public val TriStateToggleCycle: List<ToggleState> =
    listOf(ToggleState.Off, ToggleState.Indeterminate, ToggleState.On)

/**
 * Dimensions for one step of [Toggle].
 *
 * The thumb's diameter is the track's height minus twice [padding], so the padding is what decides
 * how much track shows around the thumb — and how far the thumb travels.
 *
 * @property trackWidth Length of the track.
 * @property trackHeight Height of the track as drawn.
 * @property padding Space between the thumb and the track's edge, on every side.
 * @property borderWidth Width of the track's outline, for styles that define one.
 * @property touchTarget Smallest width and height an interactive toggle occupies in layout. The
 *   track is centred in the slot, and the band around it still answers a touch. Every named step
 *   reserves [dev.voir.areca.theme.ArecaSizing.touchTarget]: 48dp, the minimum a finger reliably
 *   hits, under touch, and nothing under a pointer. [Dp.Unspecified] — the constructor's default —
 *   reserves nothing beyond the track. A display-only toggle never reserves the band.
 */
@Immutable
public data class ToggleSize(
    public val trackWidth: Dp,
    public val trackHeight: Dp,
    public val padding: Dp,
    public val borderWidth: Dp = 0.dp,
    public val touchTarget: Dp = Dp.Unspecified,
) {
    /** The steps of the toggle ramp, smallest first, and the theme default. */
    public companion object {
        /** 36×20dp. Inside a dense row, or beside compact text. */
        public val Small: ToggleSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return ToggleSize(
                    trackWidth = 36.dp,
                    trackHeight = 20.dp,
                    padding = 2.dp,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 40×24dp. The default. */
        public val Medium: ToggleSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return ToggleSize(
                    trackWidth = 40.dp,
                    trackHeight = 24.dp,
                    padding = 2.dp,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** Theme default: [Medium]. */
        public val Default: ToggleSize
            @Composable
            get() = Medium

        /** 56×32dp. A standalone switch, or a settings screen read at arm's length. */
        public val Large: ToggleSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return ToggleSize(
                    trackWidth = 56.dp,
                    trackHeight = 32.dp,
                    padding = 3.dp,
                    touchTarget = sizing.touchTarget,
                )
            }
    }
}

/**
 * Colors for a [Toggle] across its states.
 *
 * [SelectionColors.bg] is the track, [SelectionColors.mark] the thumb, and
 * [SelectionColors.border] the track's outline — so a toggle, a checkbox, and a radio describe
 * themselves the same way.
 *
 * [disabled] and [readOnly] are single slots rather than one per state: an unavailable toggle reads
 * as unavailable whichever way it is set, and the thumb's position still says which way that is.
 *
 * @property off Not set, and editable.
 * @property indeterminate Partly set, and editable.
 * @property on Set, and editable.
 * @property disabled Unavailable, whatever the state.
 * @property readOnly Shown but not editable, whatever the state.
 */
@Immutable
public data class ToggleStyle(
    public val off: SelectionColors,
    public val indeterminate: SelectionColors,
    public val on: SelectionColors,
    public val disabled: SelectionColors,
    public val readOnly: SelectionColors,
) {
    /** The theme default and the factory for variations on it. */
    public companion object {
        /** Theme default: a muted track that fills with the accent when on. */
        public val Default: ToggleStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * Parameters are flat colors rather than [SelectionColors] triples, so overriding one color
         * stays a one-line change.
         *
         * @param trackOff Track while off.
         * @param trackIndeterminate Track while partly set.
         * @param trackOn Track while on.
         * @param trackDisabled Track while unavailable.
         * @param trackReadOnly Track while uneditable.
         * @param thumbOff Thumb while off.
         * @param thumbIndeterminate Thumb while partly set.
         * @param thumbOn Thumb while on.
         * @param thumbDisabled Thumb while unavailable.
         * @param thumbReadOnly Thumb while uneditable.
         * @param border Track outline in every state. Transparent by default.
         * @return Style covering every state, plus the disabled and read-only slots.
         */
        @Composable
        public fun default(
            trackOff: Color = ArecaTheme.colors.bgMuted,
            trackIndeterminate: Color = ArecaTheme.colors.accentSubtle,
            trackOn: Color = ArecaTheme.colors.accentBase,
            trackDisabled: Color = ArecaTheme.colors.bgDisabled,
            trackReadOnly: Color = ArecaTheme.colors.bgMuted,
            thumbOff: Color = ArecaTheme.colors.fgMuted,
            thumbIndeterminate: Color = ArecaTheme.colors.fgOnFill,
            thumbOn: Color = ArecaTheme.colors.fgOnFill,
            thumbDisabled: Color = ArecaTheme.colors.bgNeutralIntense,
            thumbReadOnly: Color = ArecaTheme.colors.fgMuted,
            border: Color = Color.Transparent,
        ): ToggleStyle = ToggleStyle(
            off = SelectionColors(trackOff, border, thumbOff),
            indeterminate = SelectionColors(trackIndeterminate, border, thumbIndeterminate),
            on = SelectionColors(trackOn, border, thumbOn),
            disabled = SelectionColors(trackDisabled, border, thumbDisabled),
            readOnly = SelectionColors(trackReadOnly, border, thumbReadOnly),
        )
    }

    /**
     * Picks the colors for one combination of state and editability.
     *
     * Disabled wins over read-only, matching [SelectionStyle.resolve].
     *
     * @param state What the toggle is set to.
     * @param enabled Whether the toggle is available at all.
     * @param readOnly Whether the toggle shows its state but rejects changes.
     * @return Colors to draw with.
     */
    public fun resolve(
        state: ToggleState,
        enabled: Boolean,
        readOnly: Boolean,
    ): SelectionColors = when {
        !enabled -> this.disabled
        readOnly -> this.readOnly
        state == ToggleState.On -> on
        state == ToggleState.Indeterminate -> indeterminate
        else -> off
    }
}

/** Default [ToggleSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalToggleSize: ProvidableCompositionLocal<ToggleSize?> =
    compositionLocalOf { null }

/** Default [ToggleStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalToggleStyle: ProvidableCompositionLocal<ToggleStyle?> =
    compositionLocalOf { null }

/**
 * A switch the user slides between off and on, and optionally through a partial state.
 *
 * Unlike a checkbox, a toggle takes effect immediately — it is the control for a setting that
 * applies as soon as it is flipped, not for one that is saved later. Use [Checkbox] when the value
 * is part of a form.
 *
 * [cycleOrder] decides what a press does. The default is [BinaryToggleCycle]; pass
 * [TriStateToggleCycle] for a toggle that can also report "some, but not all".
 *
 * The thumb lifts on release and settles on press, which is the whole of the physical metaphor —
 * enough to feel like an object, not enough to be a bounce.
 *
 * [enabled] and [readOnly] are different refusals. Disabled means the setting is unavailable and the
 * control greys out; read-only means the value is real and current but not the user's to change, so
 * it keeps its shape. Assistive technology reads a disabled toggle as unavailable and a read-only
 * one as a switch offering no action; "on", "off" and the partial state are the platform's own
 * words.
 *
 * A `null` [onToggle] makes the toggle display-only: it draws its state and nothing else — no press,
 * no focus, no semantics, no reserved touch band — for an enclosing element, such as a row that is
 * itself the switch, to report the state once.
 *
 * @param state What the toggle is set to.
 * @param onToggle Called with the state a press asks for; never called when the toggle is
 *   unavailable or uneditable. `null` makes the toggle display-only.
 * @param modifier Modifier applied to the track.
 * @param style Colors. Defaults to the theme's.
 * @param size Dimensions. Defaults to the theme's, or [ToggleSize.Default].
 * @param cycleOrder States a press moves through, in order.
 * @param enabled Whether the control is available.
 * @param readOnly Whether the control shows its value but rejects changes.
 * @param interactionSource Source of the toggle's interactions; pass one to observe them.
 */
@Composable
public fun Toggle(
    state: ToggleState,
    onToggle: ((ToggleState) -> Unit)?,
    modifier: Modifier = Modifier,
    style: ToggleStyle = LocalToggleStyle.current ?: ToggleStyle.Default,
    size: ToggleSize = LocalToggleSize.current ?: ToggleSize.Default,
    cycleOrder: List<ToggleState> = BinaryToggleCycle,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
) {
    val toggle = onToggle.takeUnless { LocalSelectionControlInert.current }
    val interactive = toggle != null && enabled && !readOnly
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val colors = style.resolve(state = state, enabled = enabled, readOnly = readOnly)
    val motion = ArecaTheme.motion
    val trackShape: Shape = ArecaTheme.shapes.full

    val thumbFraction by animateFloatAsState(
        targetValue = when (state) {
            ToggleState.Off -> 0f
            ToggleState.Indeterminate -> 0.5f
            ToggleState.On -> 1f
        },
        animationSpec = motion.medium.tween(motion.standard),
        label = "toggleThumbFraction",
    )
    val trackColor by animateColorAsState(
        targetValue = colors.bg,
        animationSpec = motion.medium.tween(motion.standard),
        label = "toggleTrack",
    )
    val thumbColor by animateColorAsState(
        targetValue = colors.mark,
        animationSpec = motion.medium.tween(motion.standard),
        label = "toggleThumb",
    )
    val thumbElevation by animateDpAsState(
        targetValue = if (pressed && interactive) {
            ArecaTheme.elevation.xs
        } else {
            ArecaTheme.elevation.sm
        },
        animationSpec = motion.instant.tween(motion.standard),
        label = "toggleThumbElevation",
    )

    val thumbDiameter = size.trackHeight - size.padding * 2
    val travel = size.trackWidth - size.padding * 2 - thumbDiameter

    Box(
        modifier = modifier
            .thenIf(toggle != null) { minimumTouchTarget(size.touchTarget) }
            .size(width = size.trackWidth, height = size.trackHeight)
            .background(color = trackColor, shape = trackShape)
            .border(width = size.borderWidth, color = colors.border, shape = trackShape)
            .toggleControl(
                state = state.toToggleableState(),
                role = Role.Switch,
                onClick = toggle?.let { onChange -> { onChange(state.next(cycleOrder)) } },
                enabled = enabled,
                readOnly = readOnly,
                interactionSource = source,
                // Clipped to the track, so the press reads as the switch reacting rather than as a
                // square overlay behind a pill.
                indication = TouchIndication(shape = trackShape),
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = size.padding + travel * thumbFraction)
                .size(thumbDiameter)
                // Shadow first and unclipped, so the thumb casts onto the track rather than being
                // cut off at its own edge.
                .shadow(elevation = thumbElevation, shape = ArecaTheme.shapes.full, clip = false)
                .background(color = thumbColor, shape = ArecaTheme.shapes.full)
        )
    }
}

/**
 * Accessibility state of a switch in this state.
 *
 * @return The matching [ToggleableState]; the middle position of a three-state switch is
 * [ToggleableState.Indeterminate].
 */
internal fun ToggleState.toToggleableState(): ToggleableState = when (this) {
    ToggleState.Off -> ToggleableState.Off
    ToggleState.Indeterminate -> ToggleableState.Indeterminate
    ToggleState.On -> ToggleableState.On
}
