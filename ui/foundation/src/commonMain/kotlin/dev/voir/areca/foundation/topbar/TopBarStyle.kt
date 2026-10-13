package dev.voir.areca.foundation.topbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of a [TopBar], and the appearance of every control in it.
 *
 * Colors only. A bar that wants artwork, a gradient or a blur behind it takes
 * [bg] as [Color.Transparent] and puts the decoration in
 * [dev.voir.areca.foundation.layout.BaseLayout]'s background slot, which already draws below the
 * bars. Keeping the treatments out here is what stops a foundation bar from growing a catalogue of
 * looks that no two screens use the same way.
 *
 * The presets are named for where the bar sits, and each pairs a fill with the control appearance
 * that reads on it — see [TopBarActionStyle] for why the controls cannot be chosen one by one:
 *
 * | Preset | Fill | Controls | Where |
 * | --- | --- | --- | --- |
 * | [Default] | Canvas | Plain | A screen's own bar |
 * | [Tonal] | Canvas | Tonal discs | A home or hub screen whose controls should look pressable |
 * | [Modal] | None | Tonal discs | A sheet's or dialog's header |
 * | [Floating] | None | Raised discs | Over a file, a map, or scrolling media |
 * | [Scrim] | None | Dark translucent discs, light title | Over a photograph |
 *
 * @property bg Fill behind the whole bar.
 * @property content Color of the title, and of anything in the title slot that does not set its own.
 * @property subtitle Color of the subtitle, a step back from the title.
 * @property actions Appearance of every control in the bar's leading and trailing rows.
 * @property border Color of the rule along the bar's bottom edge, drawn whatever the scroll
 *   position. [Color.Transparent] draws none, which is what a bar over a plain canvas wants.
 * @property scrolledBorder Color the rule fades to once content has scrolled under the bar. Used
 *   only by a bar given a [TopBarScrollState]; see [TopBar].
 */
@Immutable
public data class TopBarStyle(
    public val bg: Color,
    public val content: Color,
    public val subtitle: Color,
    public val actions: TopBarActionStyle,
    public val border: Color,
    public val scrolledBorder: Color,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: the canvas, with bare controls. */
        public val Default: TopBarStyle
            @Composable
            get() = default()

        /**
         * The canvas, with every control in a quiet disc.
         *
         * For a root or hub screen — a home header with search, notifications and an account —
         * where the controls are the screen's main way anywhere and should look like buttons.
         */
        public val Tonal: TopBarStyle
            @Composable
            get() = default(actions = TopBarActionStyle.Tonal)

        /**
         * The bar as a sheet's or dialog's header.
         *
         * Draws no fill of its own, so the modal's surface shows through and the header cannot
         * differ by a shade from the panel it sits on. Its controls are quiet discs, because a bare
         * "×" on a panel reads as decoration rather than as the way out.
         */
        public val Modal: TopBarStyle
            @Composable
            get() = default(
                bg = Color.Transparent,
                actions = TopBarActionStyle.Tonal,
                scrolledBorder = ArecaTheme.colors.borderSubtle,
            )

        /**
         * The bar hovering over content that changes beneath it: a file in a viewer, a map.
         *
         * No fill and no rule, so the content runs up behind it; the controls are raised surface
         * discs so they stay findable whatever passes under them.
         */
        public val Floating: TopBarStyle
            @Composable
            get() = default(
                bg = Color.Transparent,
                actions = TopBarActionStyle.Floating,
                scrolledBorder = Color.Transparent,
            )

        /**
         * The bar laid over a photograph or a dark illustration, such as a cover image.
         *
         * No fill and no rule; the title turns light and the controls sit in translucent dark
         * discs, which read over any picture without covering it.
         */
        public val Scrim: TopBarStyle
            @Composable
            get() = default(
                bg = Color.Transparent,
                content = ArecaTheme.colors.fgOnFill,
                subtitle = ArecaTheme.colors.fgOnFill.copy(alpha = SCRIM_SUBTITLE_ALPHA),
                actions = TopBarActionStyle.Scrim,
                scrolledBorder = Color.Transparent,
            )

        /**
         * Builds a style from the current theme, with no bottom rule.
         *
         * @param bg Fill behind the whole bar.
         * @param content Color of the title.
         * @param subtitle Color of the subtitle.
         * @param actions Appearance of every control in the bar.
         * @param border Color of the always-drawn bottom rule.
         * @param scrolledBorder Color the rule fades to once content scrolls under the bar.
         * @return Style built from the supplied colors.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgCanvas,
            content: Color = ArecaTheme.colors.fgBase,
            subtitle: Color = ArecaTheme.colors.fgSubtle,
            actions: TopBarActionStyle = TopBarActionStyle.Plain,
            border: Color = Color.Transparent,
            scrolledBorder: Color = ArecaTheme.colors.borderBase,
        ): TopBarStyle = TopBarStyle(
            bg = bg,
            content = content,
            subtitle = subtitle,
            actions = actions,
            border = border,
            scrolledBorder = scrolledBorder,
        )
    }
}

/**
 * The same colors with a rule along the bottom edge at every scroll position.
 *
 * Reach for this only when the bar has no [TopBarScrollState]: a bar that knows whether content is
 * under it fades its own rule in and out, and pinning the rule on defeats that.
 *
 * @param border Color of the rule.
 * @return Style with [TopBarStyle.border] set.
 */
@Composable
public fun TopBarStyle.bordered(border: Color = ArecaTheme.colors.borderBase): TopBarStyle =
    copy(border = border)

/**
 * The same colors with every control drawn in another appearance.
 *
 * @param actions Appearance of the controls.
 * @return Style with [TopBarStyle.actions] replaced.
 */
public fun TopBarStyle.withActions(actions: TopBarActionStyle): TopBarStyle =
    copy(actions = actions)

/** Default [TopBarStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalTopBarStyle: ProvidableCompositionLocal<TopBarStyle?> = compositionLocalOf { null }

/** How far a [TopBarStyle.Scrim] subtitle steps back from its title. */
private const val SCRIM_SUBTITLE_ALPHA = 0.8f
