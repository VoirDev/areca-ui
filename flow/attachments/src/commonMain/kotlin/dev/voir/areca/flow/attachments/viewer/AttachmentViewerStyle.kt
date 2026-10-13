package dev.voir.areca.flow.attachments.viewer

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.topbar.TopBarActionStyle
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.theme.ArecaBaseColors
import dev.voir.areca.theme.ArecaColors
import dev.voir.areca.theme.ArecaTheme

/**
 * How the viewer is drawn: the ground a file sits on, and the surfaces laid on it.
 *
 * Dark whatever the application's theme, like the application's crop screen: the file sits on black,
 * and its surfaces — the filmstrip's squares, a secondary button, a notice, a page still drawing —
 * are the dark theme's raised surface. A photo or a page is read fairly against a dark surround, and
 * the viewer paints the system bars with [bg] so it reads as one surface to the screen's edges.
 *
 * The heading's controls are not among them: they are bare, as on any screen's own bar; see
 * [topBar].
 *
 * Make one with [default], or take [Default], which read the theme in scope. Provide one through
 * [LocalAttachmentViewerStyle] to restyle every viewer below that point. Every property also has a
 * default here, so a style built directly keeps working as properties are added; those defaults
 * are the stock Areca dark palette and do not follow the application's theme.
 *
 * @property bg Ground behind the file, and behind the whole viewer.
 * @property controlBg Fill of a filmstrip square, a secondary button, a notice and a page still
 *   drawing.
 * @property controlContent Glyphs and captions on those.
 * @property controlContentDisabled Caption of a secondary button that cannot be pressed.
 * @property title Colour of the heading over the file.
 * @property detail Colour of the quieter lines: the position, the line under the file.
 * @property pillBg Fill of the transient line over the file and the page counter of a PDF.
 * @property pillContent Text on that fill.
 */
@Immutable
public data class AttachmentViewerStyle(
    public val bg: Color = StockBaseColors.baseBlack,
    public val controlBg: Color = StockDarkColors.bgSurface,
    public val controlContent: Color = StockDarkColors.fgBase,
    public val controlContentDisabled: Color = StockDarkColors.fgDisabled,
    public val title: Color = StockDarkColors.fgBase,
    public val detail: Color = StockDarkColors.fgMuted,
    public val pillBg: Color = StockDarkColors.bgContrast,
    public val pillContent: Color = StockDarkColors.fgOnContrast,
) {
    /**
     * The viewer's heading bar in this style: the foundation's plain bar on the viewer's ground,
     * with its title and position line taken from this style's colors.
     *
     * Plain rather than floating. The heading sits above the pager, not over the file, so nothing
     * passes under its controls and there is nothing to raise them off; bare, they are the same
     * glyphs, size and distance from the edge as the bar of any other screen in the application.
     *
     * @return The bar's style.
     */
    @Composable
    public fun topBar(): TopBarStyle = TopBarStyle.default(
        bg = Color.Transparent,
        content = title,
        subtitle = detail,
        actions = TopBarActionStyle.Plain,
        scrolledBorder = Color.Transparent,
    )

    /**
     * A secondary capsule of this style, beside the solid primary one on a page that cannot be drawn.
     *
     * @return The style.
     */
    @Composable
    public fun secondaryButton(): ButtonStyle = ButtonStyle(
        bg = controlBg,
        border = Color.Transparent,
        content = controlContent,
        contentDisabled = controlContentDisabled,
        shape = CircleShape,
        indication = FadeIndication.tint(CircleShape),
    )

    /** Makes a style from the theme in scope. */
    public companion object {
        /** The viewer in the theme's dark colours: [default] with nothing replaced. */
        public val Default: AttachmentViewerStyle
            @Composable
            get() = default()

        /**
         * The viewer in the theme's dark colours, with any colour replaced.
         *
         * @param bg Ground behind the file.
         * @param controlBg Fill of the surfaces laid on the ground.
         * @param controlContent Content on them.
         * @param controlContentDisabled Caption of a secondary button that cannot be pressed.
         * @param title Colour of the heading.
         * @param detail Colour of the quieter lines.
         * @param pillBg Fill of the transient line and the page counter.
         * @param pillContent Text on that fill.
         * @return The style.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.baseColors.baseBlack,
            controlBg: Color = ArecaTheme.darkColors.bgSurface,
            controlContent: Color = ArecaTheme.darkColors.fgBase,
            controlContentDisabled: Color = ArecaTheme.darkColors.fgDisabled,
            title: Color = ArecaTheme.darkColors.fgBase,
            detail: Color = ArecaTheme.darkColors.fgMuted,
            pillBg: Color = ArecaTheme.darkColors.bgContrast,
            pillContent: Color = ArecaTheme.darkColors.fgOnContrast,
        ): AttachmentViewerStyle = AttachmentViewerStyle(
            bg = bg,
            controlBg = controlBg,
            controlContent = controlContent,
            controlContentDisabled = controlContentDisabled,
            title = title,
            detail = detail,
            pillBg = pillBg,
            pillContent = pillContent,
        )
    }
}

/**
 * Default [AttachmentViewerStyle] in scope, or `null` to use [AttachmentViewerStyle.Default].
 *
 * Read by [AttachmentViewer] and [AttachmentViewerPage] when they are not given a style.
 *
 * Unlike the foundation's component defaults this is **not** seeded by
 * [dev.voir.areca.theme.ArecaThemeProvider]: that provider lives in `:foundation`, and `:foundation`
 * does not depend on `:flow:attachments`. Wire it yourself around a subtree that wants a different
 * default:
 *
 * ```kotlin
 * CompositionLocalProvider(
 *     LocalAttachmentViewerStyle provides AttachmentViewerStyle.default(
 *         bg = ArecaTheme.darkColors.bgCanvas,
 *     ),
 * ) { … }
 * ```
 */
public val LocalAttachmentViewerStyle: ProvidableCompositionLocal<AttachmentViewerStyle?> =
    compositionLocalOf { null }

/** The stock Areca palette, which a style built without [AttachmentViewerStyle.default] falls back to. */
private val StockBaseColors = ArecaBaseColors()

/** The stock Areca dark colours, which a style built without [AttachmentViewerStyle.default] falls back to. */
private val StockDarkColors = ArecaColors.dark(StockBaseColors)
