package dev.voir.areca.flow.attachments.part

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaColors
import dev.voir.areca.theme.ArecaRadius
import dev.voir.areca.theme.ArecaTheme

/**
 * How a file's square is drawn, wherever it appears: in a strip, in a row, on its own.
 *
 * Neutral by default. The fill is the subtle page grey, which reads as a square on the white a strip
 * usually sits on; an application whose sheets are grey passes a white [bg] instead.
 *
 * Make one with [default], or take [Default], which read the theme in scope. Provide one through
 * [LocalAttachmentTileStyle] to restyle every square below that point. Every property also has a
 * default here, so a style built directly keeps working as properties are added; those defaults
 * are the stock Areca light palette and do not follow the application's theme.
 *
 * @property bg Fill behind a file that cannot be drawn as itself, and of the add square.
 * @property shape Outline of the square, which the press tint and the focus ring follow too.
 * @property glyph Colour of the glyph that says what kind of file it is.
 * @property extension Colour of the extension printed under that glyph.
 * @property content Colour of the plus on the add square.
 * @property contentDisabled Colour of the plus on an add square that cannot be pressed.
 * @property markBg Fill of the disc in a square's corner that says the bytes are elsewhere or
 *   moving.
 * @property markContent Colour of the glyph or the progress ring on that disc.
 * @property ringThickness Width of the progress ring's stroke, on the disc and at a row's end.
 * @property veil Wash over a file still being added; the picture stays visible under it.
 * @property removeBg Fill of the remove mark.
 * @property removeContent Colour of the cross on the remove mark.
 * @property caption Colour of the line under a square.
 * @property captionDisabled Colour of the line under an add square that cannot be pressed.
 * @property detail Colour of the quieter second line under a square.
 */
@Immutable
public data class AttachmentTileStyle(
    public val bg: Color = StockColors.bgSubtle,
    public val shape: Shape = RoundedCornerShape(StockRadius.lg),
    public val glyph: Color = StockColors.fgSubtle,
    public val extension: Color = StockColors.fgMuted,
    public val content: Color = StockColors.fgBase,
    public val contentDisabled: Color = StockColors.fgDisabled,
    public val markBg: Color = StockColors.bgSurface,
    public val markContent: Color = StockColors.fgBase,
    public val ringThickness: Dp = DefaultRingThickness,
    public val veil: Color = StockColors.bgSurface.copy(alpha = VEIL_ALPHA),
    public val removeBg: Color = StockColors.bgContrast,
    public val removeContent: Color = StockColors.fgOnContrast,
    public val caption: Color = StockColors.fgBase,
    public val captionDisabled: Color = StockColors.fgDisabled,
    public val detail: Color = StockColors.fgMuted,
) {
    /** Makes a style from the theme in scope. */
    public companion object {
        /** The theme's neutral square: [default] with nothing replaced. */
        public val Default: AttachmentTileStyle
            @Composable
            get() = default()

        /**
         * The theme's neutral square, with any colour or the shape replaced.
         *
         * @param bg Fill behind an undrawable file and the add square.
         * @param shape Outline of the square.
         * @param glyph Colour of the kind-of-file glyph.
         * @param extension Colour of the extension.
         * @param content Colour of the add square's plus.
         * @param contentDisabled Colour of the plus while the add square cannot be pressed.
         * @param markBg Fill of the corner disc.
         * @param markContent Colour on the corner disc.
         * @param ringThickness Width of the progress ring's stroke.
         * @param veil Wash over a file still being added.
         * @param removeBg Fill of the remove mark.
         * @param removeContent Colour of the remove mark's cross.
         * @param caption Colour of the line under a square.
         * @param captionDisabled Colour of the add square's line while it cannot be pressed.
         * @param detail Colour of the second line under a square.
         * @return The style.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgSubtle,
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.lg),
            glyph: Color = ArecaTheme.colors.fgSubtle,
            extension: Color = ArecaTheme.colors.fgMuted,
            content: Color = ArecaTheme.colors.fgBase,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
            markBg: Color = ArecaTheme.colors.bgSurface,
            markContent: Color = ArecaTheme.colors.fgBase,
            ringThickness: Dp = DefaultRingThickness,
            veil: Color = ArecaTheme.colors.bgSurface.copy(alpha = VEIL_ALPHA),
            removeBg: Color = ArecaTheme.colors.bgContrast,
            removeContent: Color = ArecaTheme.colors.fgOnContrast,
            caption: Color = ArecaTheme.colors.fgBase,
            captionDisabled: Color = ArecaTheme.colors.fgDisabled,
            detail: Color = ArecaTheme.colors.fgMuted,
        ): AttachmentTileStyle = AttachmentTileStyle(
            bg = bg,
            shape = shape,
            glyph = glyph,
            extension = extension,
            content = content,
            contentDisabled = contentDisabled,
            markBg = markBg,
            markContent = markContent,
            ringThickness = ringThickness,
            veil = veil,
            removeBg = removeBg,
            removeContent = removeContent,
            caption = caption,
            captionDisabled = captionDisabled,
            detail = detail,
        )

        /** How opaque the veil over a file still being added is: enough to dim, not to hide. */
        private const val VEIL_ALPHA = 0.6f
    }
}

/**
 * Default [AttachmentTileStyle] in scope, or `null` to use [AttachmentTileStyle.Default].
 *
 * Read by every square of this module — [AttachmentTile], [AttachmentAddTile],
 * [AttachmentThumbnail], [AttachmentStrip], the thumbnail of
 * [dev.voir.areca.flow.attachments.sheet.PendingAttachmentSheet] and the square of
 * [dev.voir.areca.flow.attachments.row.AttachmentRow] — when it is not given a style.
 *
 * Unlike the foundation's component defaults this is **not** seeded by
 * [dev.voir.areca.theme.ArecaThemeProvider]: that provider lives in `:foundation`, and `:foundation`
 * does not depend on `:flow:attachments`. Wire it yourself around a subtree that wants a different
 * default:
 *
 * ```kotlin
 * CompositionLocalProvider(
 *     LocalAttachmentTileStyle provides AttachmentTileStyle.default(
 *         bg = ArecaTheme.colors.bgSurface,
 *     ),
 * ) { … }
 * ```
 */
public val LocalAttachmentTileStyle: ProvidableCompositionLocal<AttachmentTileStyle?> =
    compositionLocalOf { null }

/** The stock Areca light colours, which a style built without [AttachmentTileStyle.default] falls back to. */
private val StockColors = ArecaColors.light()

/** The stock Areca radii, which a style built without [AttachmentTileStyle.default] falls back to. */
private val StockRadius = ArecaRadius()

/** Width of the progress ring's stroke unless a style says otherwise. */
private val DefaultRingThickness = 2.dp
