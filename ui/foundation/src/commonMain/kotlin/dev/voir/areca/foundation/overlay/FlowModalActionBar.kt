package dev.voir.areca.foundation.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.layout.HorizontalDivider
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of a [FlowModalActionBar] and of the buttons on it.
 *
 * @property bg Fill under the buttons. Transparent by default, so the bar is the sheet's own ground
 *   whatever colour the sheet was given — a white bar docked to a grey picker sheet reads as a second
 *   panel stacked on the first, rather than as the sheet's foot.
 * @property confirm The commit at the right end.
 * @property secondary The lighter action at the left end — see [FlowModalSecondaryAction].
 */
@Immutable
public data class FlowModalActionBarStyle(
    public val bg: Color,
    public val confirm: ButtonStyle,
    public val secondary: ButtonStyle,
) {
    /**
     * The theme's default [FlowModalActionBarStyle], and [default] for building a variation on it.
     */
    public companion object {
        /** Theme default. */
        public val Default: FlowModalActionBarStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * Both buttons are the dark neutral and both are capsules. The screen behind a sheet usually
         * has a brand-coloured primary action of its own, and a second brand button on top of it
         * makes two different commitments look like the same one. The secondary keeps the plain fill:
         * it is not the sheet's answer, and shows its capsule only under a thumb.
         *
         * @param bg Fill under the buttons.
         * @param confirm The commit.
         * @param secondary The lighter action.
         * @return Style built from the supplied colors.
         */
        @Composable
        public fun default(
            bg: Color = Color.Transparent,
            confirm: ButtonStyle = ButtonStyle.solid(ActionColor.NeutralDark, shape = CircleShape),
            secondary: ButtonStyle = ButtonStyle.plain(ActionColor.NeutralDark, shape = CircleShape),
        ): FlowModalActionBarStyle = FlowModalActionBarStyle(
            bg = bg,
            confirm = confirm,
            secondary = secondary,
        )
    }
}

/**
 * Metrics of a [FlowModalActionBar].
 *
 * @property padding Room between the bar's hairline and edges and the buttons on it.
 * @property gap Least space between the left-hand slot and the commit.
 * @property button Size shared by the commit and every [FlowModalSecondaryAction] on the bar, so the
 *   two ends are always one height.
 */
@Immutable
public data class FlowModalActionBarSize(
    public val padding: PaddingValues,
    public val gap: Dp,
    public val button: ButtonSize,
) {
    /**
     * The theme's default [FlowModalActionBarSize], and [default] for building a variation on it.
     */
    public companion object {
        /** Theme default. */
        public val Default: FlowModalActionBarSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * Small buttons: a sheet's title already says what is being chosen, so the commit says one
         * word, and a full-width or medium button makes that word the loudest thing on the sheet.
         *
         * @param padding Room around the buttons. The page gutter at the sides, `lg` above and below.
         * @param gap Least space between the two ends.
         * @param button Size of every button on the bar.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            padding: PaddingValues = PaddingValues(
                horizontal = ArecaTheme.spacing.horizontalContentInsets,
                vertical = ArecaTheme.spacing.lg,
            ),
            gap: Dp = ArecaTheme.spacing.lg,
            button: ButtonSize = ButtonSize.Small,
        ): FlowModalActionBarSize = FlowModalActionBarSize(
            padding = padding,
            gap = gap,
            button = button,
        )
    }
}

/**
 * How a [FlowModalActionBar] shares its width between its two ends.
 */
@Immutable
public sealed interface FlowModalActionBarLayout {
    /**
     * The commit at its own width against the end edge, the left-hand slot taking the room left
     * over. The default.
     */
    public data object Trailing : FlowModalActionBarLayout

    /**
     * Both ends stretched across the bar, in proportion to their weights — a full-width pair such as
     * "Cancel" beside "Compare with September", where the commit carries a sentence rather than a
     * word. The [FlowModalSecondaryAction] in the left-hand slot stretches with its share.
     *
     * @property secondaryWeight Share of the width given to the left-hand slot. Positive.
     * @property confirmWeight Share of the width given to the commit. Positive.
     * @throws IllegalArgumentException When either weight is not positive.
     */
    public data class Split(
        public val secondaryWeight: Float = 1f,
        public val confirmWeight: Float = 2f,
    ) : FlowModalActionBarLayout {
        init {
            require(secondaryWeight > 0f && confirmWeight > 0f) {
                "Split weights must be positive, were $secondaryWeight and $confirmWeight."
            }
        }
    }
}

/**
 * Whether a [FlowModalSecondaryAction] stretches across its slot, set by a
 * [FlowModalActionBarLayout.Split] bar.
 */
private val LocalFlowModalActionFill = compositionLocalOf { false }

/**
 * The foot a [FlowModal] commits from: a hairline, then the lighter action on the left and the
 * commit on the right.
 *
 * Pass it as the modal's `footer`. **Every sheet's foot is this one bar**, whatever the sheet
 * chooses — a filter, a category, a date, an icon — so the commit is always in the same corner, the
 * same size and the same colour, and the reader stops having to look for it.
 *
 * What goes on the left is whatever is most useful *about this sheet* short of the answer: "Clear",
 * "Random", "Remove", a chip naming the current pick, or "Cancel" when there is nothing better. It
 * sits at the start of the room the commit does not need, so a long chip ellipsises inside that room
 * rather than pushing the commit off the edge.
 *
 * The bar draws no navigation-bar inset: [FlowModal] already keeps its content clear of the system
 * bars, and the sheet's ground runs on under them.
 *
 * @param confirmText Caption of the commit. One word, usually.
 * @param onConfirm Called when the commit is pressed. The sheet is not closed for you.
 * @param modifier Modifier applied to the bar, which fills the width it is given.
 * @param enabled Whether the commit can be taken.
 * @param loading Shows the commit's spinner and refuses presses while the committed work runs.
 * @param style Colors of the bar and its buttons.
 * @param size Metrics of the bar and its buttons.
 * @param layout How the two ends share the bar's width.
 * @param showDivider Whether the hairline above the buttons is drawn. Turn it off on a sheet whose
 *   design separates by space rather than by rules.
 * @param leading What is drawn at the left end — usually one [FlowModalSecondaryAction], which picks
 *   up [style] and [size] from the bar.
 */
@Composable
public fun FlowModalActionBar(
    confirmText: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    style: FlowModalActionBarStyle = LocalFlowModalActionBarStyle.current ?: FlowModalActionBarStyle.Default,
    size: FlowModalActionBarSize = LocalFlowModalActionBarSize.current ?: FlowModalActionBarSize.Default,
    layout: FlowModalActionBarLayout = FlowModalActionBarLayout.Trailing,
    showDivider: Boolean = true,
    leading: (@Composable () -> Unit)? = null,
) {
    val split = layout as? FlowModalActionBarLayout.Split
    Column(modifier = modifier.fillMaxWidth()) {
        if (showDivider) HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(style.bg)
                .padding(size.padding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(size.gap),
        ) {
            // The weight lives on this box rather than on what the caller passes, so the slot cannot
            // stretch a chip across the bar, and the commit stays pinned to the right edge whether or
            // not anything is drawn on the left.
            Box(
                modifier = Modifier.weight(split?.secondaryWeight ?: 1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (leading != null) {
                    // Handed down so a secondary action on a restyled bar matches its commit without
                    // the caller restating the style.
                    CompositionLocalProvider(
                        LocalFlowModalActionBarStyle provides style,
                        LocalFlowModalActionBarSize provides size,
                        LocalFlowModalActionFill provides (split != null),
                        content = leading,
                    )
                }
            }

            Button(
                modifier = if (split != null) Modifier.weight(split.confirmWeight) else Modifier,
                text = confirmText,
                style = style.confirm,
                size = size.button,
                enabled = enabled,
                loading = loading,
                onClick = onConfirm,
            )
        }
    }
}

/**
 * The lighter action at the left end of a [FlowModalActionBar]: a shortcut, a reset, or a way out.
 *
 * Drawn in the bar's secondary style and at the commit's height, read from the bar it sits in. On a
 * [FlowModalActionBarLayout.Split] bar it stretches across its share of the width.
 *
 * @param text What it does. One or two words.
 * @param onClick Called when it is pressed.
 * @param modifier Modifier applied to the button.
 * @param enabled Whether it can be pressed.
 * @param style Colors of the bar it belongs to.
 * @param size Metrics of the bar it belongs to.
 */
@Composable
public fun FlowModalSecondaryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: FlowModalActionBarStyle = LocalFlowModalActionBarStyle.current ?: FlowModalActionBarStyle.Default,
    size: FlowModalActionBarSize = LocalFlowModalActionBarSize.current ?: FlowModalActionBarSize.Default,
) {
    Button(
        text = text,
        modifier = if (LocalFlowModalActionFill.current) modifier.fillMaxWidth() else modifier,
        style = style.secondary,
        size = size.button,
        enabled = enabled,
        onClick = onClick,
    )
}

/** Default [FlowModalActionBarSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalFlowModalActionBarSize: ProvidableCompositionLocal<FlowModalActionBarSize?> =
    compositionLocalOf { null }

/** Default [FlowModalActionBarStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalFlowModalActionBarStyle: ProvidableCompositionLocal<FlowModalActionBarStyle?> =
    compositionLocalOf { null }
