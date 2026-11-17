package dev.voir.areca.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of an [BottomActionBar].
 *
 * @property bg Fill of the bar, run on under the navigation bar. Opaque by default: in a
 *   [BaseLayout] the screen scrolls under its bottom bar, and a transparent bar would let rows show
 *   through the button.
 * @property button The action.
 * @property leading Color of the copy in the leading slot — a count of what the action applies to.
 * @property hint Color of the line under the button that says why the action is not available yet.
 * @property buttonNotReady The action while the form behind it is not complete yet: the same family,
 *   softened, still pressable so a press can run validation and say what is missing.
 * @property caption Color of the line above the button.
 */
@Immutable
public data class BottomActionBarStyle(
    public val bg: Color,
    public val button: ButtonStyle,
    public val leading: Color,
    public val hint: Color,
    public val buttonNotReady: ButtonStyle = button,
    public val caption: Color = hint,
) {
    /** The theme's default [BottomActionBarStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: BottomActionBarStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * The action is the screen's primary one, so it is the solid brand button — unlike a
         * [dev.voir.areca.foundation.overlay.FlowModalActionBar], whose commit sits over a screen
         * that already has one.
         *
         * @param bg Fill of the bar. Defaults to the theme's bar surface.
         * @param button The action.
         * @param leading Color of the leading copy.
         * @param hint Color of the hint line.
         * @param buttonNotReady The action while the form is not complete: the soft brand button.
         * @param caption Color of the line above the button.
         * @return Style built from the supplied colors.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.surfaces.bar,
            button: ButtonStyle = ButtonStyle.solid(),
            leading: Color = ArecaTheme.colors.fgBase,
            hint: Color = ArecaTheme.colors.fgMuted,
            buttonNotReady: ButtonStyle = ButtonStyle.soft(),
            caption: Color = ArecaTheme.colors.fgSubtle,
        ): BottomActionBarStyle = BottomActionBarStyle(
            bg = bg,
            button = button,
            leading = leading,
            hint = hint,
            buttonNotReady = buttonNotReady,
            caption = caption,
        )
    }
}

/**
 * Metrics of an [BottomActionBar].
 *
 * @property padding Room between the bar's hairline and edges and what is on it.
 * @property gap Least space between the leading slot and the button.
 * @property hintGap Space between the button and the hint under it.
 * @property button Size of the action.
 * @property leadingTextStyle Type of the copy in the leading slot.
 * @property hintTextStyle Type of the hint line.
 */
@Immutable
public data class BottomActionBarSize(
    public val padding: PaddingValues,
    public val gap: Dp,
    public val hintGap: Dp,
    public val button: ButtonSize,
    public val leadingTextStyle: TextStyle,
    public val hintTextStyle: TextStyle,
) {
    /** The theme's default [BottomActionBarSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: BottomActionBarSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param padding Room around the content. The page gutter at the sides, `md` above and below.
         * @param gap Least space between the leading slot and the button.
         * @param hintGap Space between the button and the hint.
         * @param button Size of the action. Medium: a screen's primary action, within a thumb's reach.
         * @param leadingTextStyle Type of the leading copy.
         * @param hintTextStyle Type of the hint line.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            padding: PaddingValues = PaddingValues(
                horizontal = ArecaTheme.spacing.horizontalContentInsets,
                vertical = ArecaTheme.spacing.md,
            ),
            gap: Dp = ArecaTheme.spacing.lg,
            hintGap: Dp = ArecaTheme.spacing.sm,
            button: ButtonSize = ButtonSize.Medium,
            leadingTextStyle: TextStyle = ArecaTheme.typography.labelMedium,
            hintTextStyle: TextStyle = ArecaTheme.typography.caption,
        ): BottomActionBarSize = BottomActionBarSize(
            padding = padding,
            gap = gap,
            hintGap = hintGap,
            button = button,
            leadingTextStyle = leadingTextStyle,
            hintTextStyle = hintTextStyle,
        )
    }
}

/** Default [BottomActionBarStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalBottomActionBarStyle: ProvidableCompositionLocal<BottomActionBarStyle?> = compositionLocalOf { null }

/** Default [BottomActionBarSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalBottomActionBarSize: ProvidableCompositionLocal<BottomActionBarSize?> = compositionLocalOf { null }

/**
 * The bar a screen commits from, pinned under its content: pass it as [BaseLayout]'s `bottomBar`.
 *
 * Three shapes, by what is passed:
 *
 * - **One action** — just [actionText]: the button runs across the bar. "Save", "Add item".
 * - **A count and an action** — a [leading] slot: what the action applies to on the left, the button
 *   at its own width on the right. "3 selected · Move". The slot is given [BottomActionBarStyle.leading]
 *   and [BottomActionBarSize.leadingTextStyle], so a plain `Text` is enough.
 * - **Not yet** — `enabled = false` with a [hint]: the button stays where it will be, and the line
 *   under it says what is missing. "Add a name to save". A disabled button with no reason is a
 *   dead end; the hint is the reason.
 *
 * The bar draws a hairline above itself and, like [dev.voir.areca.foundation.tabbar.TabBar], pads
 * its content by the horizontal and bottom safe-drawing insets: its fill runs on under the
 * navigation bar while the button clears it — and the keyboard, so a form's "Save" rides above it.
 * [BaseLayout] measures the bar, insets included, and hands its height to the content as bottom
 * padding, so the last row scrolls clear of the button. Keep the bottom insets out of
 * `BaseLayout`'s `contentWindowInsets` then, or they are counted twice.
 *
 * Inside a [dev.voir.areca.foundation.overlay.FlowModal], use
 * [dev.voir.areca.foundation.overlay.FlowModalActionBar] instead: a sheet keeps its own insets and
 * has a screen behind it with a primary action of its own.
 *
 * @param actionText Caption of the action.
 * @param onAction Called when the action is pressed.
 * @param modifier Modifier applied to the bar, which fills the width it is given.
 * @param enabled Whether the action can be taken. Say why not with [hint].
 * @param loading Shows the button's spinner and refuses presses while the action runs.
 * @param hint Line under the button, already localized, or `null` for none. Usually passed only
 *   while the action is not [enabled]; it is announced when it appears.
 * @param caption Line above the button, already localized, or `null` for none: something worth
 *   knowing before committing — "The file uploads when the import starts".
 * @param ready Whether the form behind the action is complete. `false` softens the button but keeps
 *   it pressable, so a press can run validation and put each complaint under its field; use
 *   [enabled] for an action that must not happen at all.
 * @param style Colors of the bar and its button.
 * @param size Metrics of the bar and its button.
 * @param showDivider Whether the hairline above the bar is drawn.
 * @param windowInsets Platform insets the content is padded by, inside the bar's fill. The
 *   horizontal and bottom safe-drawing insets by default, as for a tab bar; pass
 *   `WindowInsets(0, 0, 0, 0)` when something below the bar already clears them.
 * @param leading What goes on the left of the button — usually a count of what the action applies
 *   to. Null runs the button across the bar.
 */
@Composable
public fun BottomActionBar(
    actionText: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    hint: String? = null,
    caption: String? = null,
    ready: Boolean = true,
    style: BottomActionBarStyle = LocalBottomActionBarStyle.current ?: BottomActionBarStyle.Default,
    size: BottomActionBarSize = LocalBottomActionBarSize.current ?: BottomActionBarSize.Default,
    showDivider: Boolean = true,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
    ),
    leading: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(style.bg),
    ) {
        if (showDivider) HorizontalDivider()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .padding(size.padding),
        ) {
            val buttonStyle = if (ready) style.button else style.buttonNotReady
            if (caption != null) {
                Text(
                    text = caption,
                    modifier = Modifier.fillMaxWidth(),
                    style = size.hintTextStyle,
                    color = style.caption,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(size.hintGap))
            }
            if (leading == null) {
                Button(
                    text = actionText,
                    onClick = onAction,
                    modifier = Modifier.fillMaxWidth(),
                    style = buttonStyle,
                    size = size.button,
                    enabled = enabled,
                    loading = loading,
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(size.gap),
                ) {
                    // Weighted so a long count ellipsises in the room the button leaves rather than
                    // pushing the button off the edge.
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        CompositionLocalProvider(
                            LocalContentColor provides style.leading,
                            LocalTextStyle provides size.leadingTextStyle,
                            content = leading,
                        )
                    }
                    Button(
                        text = actionText,
                        onClick = onAction,
                        style = buttonStyle,
                        size = size.button,
                        enabled = enabled,
                        loading = loading,
                    )
                }
            }

            if (hint != null) {
                Spacer(Modifier.height(size.hintGap))
                Text(
                    text = hint,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { liveRegion = LiveRegionMode.Polite },
                    style = size.hintTextStyle,
                    color = style.hint,
                    textAlign = if (leading == null) TextAlign.Center else TextAlign.End,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
