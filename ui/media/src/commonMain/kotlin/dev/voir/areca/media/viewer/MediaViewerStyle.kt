package dev.voir.areca.media.viewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.foundation.topbar.TopBarActionStyle
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * How a [MediaViewer] is drawn: the ground the pictures sit on, and the bars laid over them.
 *
 * @property bg Ground behind the pictures while the bars show, and the system bars' colour.
 * @property bgChromeHidden Ground once a tap has hidden the bars — black on a light viewer, so the
 *   picture is alone.
 * @property content Colour of the bars' glyphs and words.
 * @property scrim Colour the bars fade from at the screen's edges, so they read on any picture.
 * @property noticeBg Fill of the notice line.
 * @property noticeContent Words of the notice line.
 * @property dark Whether the viewer is dark: its bars, sheets and pages are then drawn in the
 *   application's dark theme whatever the theme outside.
 */
@Immutable
public data class MediaViewerStyle(
    public val bg: Color,
    public val bgChromeHidden: Color,
    public val content: Color,
    public val scrim: Color,
    public val noticeBg: Color,
    public val noticeContent: Color,
    public val dark: Boolean,
) {
    /**
     * A foundation `TopBar` laid over the pictures in this style: no ground of its own, words in
     * [content], and its controls in discs that stay findable over any picture — translucent dark
     * ones on a dark viewer, raised surface ones on a light one. Lay it in the viewer's `topBar`
     * with `TopBarTitleAlignment.Center`, a `CloseAction` and the named actions.
     *
     * @return The bar's style.
     */
    @Composable
    public fun topBar(): TopBarStyle = TopBarStyle.default(
        bg = Color.Transparent,
        content = content,
        subtitle = content.copy(alpha = SUBTITLE_ALPHA),
        actions = if (dark) TopBarActionStyle.Scrim else TopBarActionStyle.Floating,
        scrolledBorder = Color.Transparent,
    )

    /** The two grounds a viewer comes in. */
    public companion object {
        /**
         * Pictures on black — photographs, campaigns, any picture read best against the dark.
         *
         * @param bg Ground behind the pictures.
         * @return The style.
         */
        @Composable
        public fun dark(bg: Color = ArecaTheme.baseColors.baseBlack): MediaViewerStyle = MediaViewerStyle(
            bg = bg,
            bgChromeHidden = bg,
            content = ArecaTheme.baseColors.baseWhite,
            scrim = ArecaTheme.baseColors.baseBlack.copy(alpha = DARK_SCRIM_ALPHA),
            noticeBg = ArecaTheme.baseColors.baseWhite,
            noticeContent = ArecaTheme.baseColors.baseBlack,
            dark = true,
        )

        /**
         * Pictures on white — product shots on their own white ground — turning black when the bars
         * hide.
         *
         * @param bg Ground behind the pictures.
         * @return The style.
         */
        @Composable
        public fun light(bg: Color = ArecaTheme.baseColors.baseWhite): MediaViewerStyle = MediaViewerStyle(
            bg = bg,
            bgChromeHidden = ArecaTheme.baseColors.baseBlack,
            content = ArecaTheme.baseColors.baseBlack,
            scrim = ArecaTheme.baseColors.baseWhite.copy(alpha = LIGHT_SCRIM_ALPHA),
            noticeBg = ArecaTheme.baseColors.baseBlack,
            noticeContent = ArecaTheme.baseColors.baseWhite,
            dark = false,
        )
    }
}

/** Default [MediaViewerStyle] in scope, or `null` for [MediaViewerStyle.dark]. Not seeded by the theme. */
public val LocalMediaViewerStyle: ProvidableCompositionLocal<MediaViewerStyle?> = compositionLocalOf { null }

/** How dark a dark viewer's bars fade from. */
private const val DARK_SCRIM_ALPHA = 0.6f

/** How light a light viewer's bars fade from. */
private const val LIGHT_SCRIM_ALPHA = 0.85f

/** How quiet a bar's second line is against its content colour. */
private const val SUBTITLE_ALPHA = 0.7f
