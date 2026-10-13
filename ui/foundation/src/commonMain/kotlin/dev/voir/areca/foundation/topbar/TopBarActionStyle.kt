package dev.voir.areca.foundation.topbar

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.theme.ArecaTheme

/**
 * How every control in one [TopBar] is drawn: bare glyphs, or glyphs in a container.
 *
 * Set once per bar, on [TopBarStyle.actions], never per control. That is the rule the whole family
 * rests on: a bar's back chevron, its share icon, its "Done" label and a custom mark wrapped in
 * [Action] all come out in the same treatment, so a screen cannot end up with one circled control
 * beside three bare ones.
 *
 * Pick the appearance from what the bar sits on, not from taste:
 *
 * | Appearance | Bar sits on | Scenario |
 * | --- | --- | --- |
 * | [Plain] | The screen's own canvas | Almost every screen: root, detail, form |
 * | [Tonal] | A panel, or a canvas the controls should stand out from | Sheet and dialog headers, a home header whose controls read as buttons |
 * | [Floating] | Content that varies: a file, a map, a feed | A viewer, a bar hovering over scrolling media |
 * | [Scrim] | A photograph or other dark or busy artwork | A back button over a cover image |
 *
 * A **contained** appearance — any with a [bg] or a [border] — changes the bar's geometry as
 * well as its colors. A bare glyph is aligned to the screen gutter by its ink, so its hit box hangs
 * outside the gutter; a disc is aligned by its edge, because the edge is what the eye sees. A
 * contained bar also spaces its controls further apart and draws a smaller glyph inside each disc;
 * see [TopBarSize.containedIcon].
 *
 * @property bg Fill behind a neutral control. [Color.Transparent] draws none.
 * @property content Color of a neutral control's glyph or label, a step behind the title on a plain
 *   bar and at full strength on a contained one.
 * @property border Outline of a neutral control. [Color.Transparent] draws none.
 * @property shape Shape of every control's container and of its press, hover and focus feedback.
 * @property elevation Shadow under a contained control, lifting it off the content behind the bar.
 *   `0.dp` draws none.
 * @property emphasis How a control in any color other than [ActionColor.Neutral] is drawn — a
 *   "Done", a "Delete". Scales with the appearance, so a colored control is always one step louder
 *   than the neutral ones beside it and never a different kind of object.
 */
@Immutable
public data class TopBarActionStyle(
    public val bg: Color,
    public val content: Color,
    public val border: Color,
    public val shape: Shape,
    public val elevation: Dp,
    public val emphasis: TopBarActionEmphasis,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /**
         * Bare glyphs and words on the bar's own fill. The default, and right for any bar over the
         * screen's canvas.
         */
        public val Plain: TopBarActionStyle
            @Composable
            get() = TopBarActionStyle(
                bg = Color.Transparent,
                content = ArecaTheme.colors.fgSubtle,
                border = Color.Transparent,
                shape = ArecaTheme.shapes.button,
                elevation = 0.dp,
                emphasis = TopBarActionEmphasis.Plain,
            )

        /**
         * Glyphs in quiet neutral discs, and words in quiet capsules.
         *
         * For a header over a panel — a sheet, a dialog — where a bare "×" reads as decoration
         * rather than as a way out, and for a home header whose controls should look pressable.
         */
        public val Tonal: TopBarActionStyle
            @Composable
            get() = TopBarActionStyle(
                bg = ArecaTheme.colors.bgNeutralMuted,
                content = ArecaTheme.colors.fgBase,
                border = Color.Transparent,
                shape = CircleShape,
                elevation = 0.dp,
                emphasis = TopBarActionEmphasis.Soft,
            )

        /**
         * Glyphs in raised surface discs, lifted off whatever is behind the bar.
         *
         * For a bar over content that changes under it — a file in a viewer, a map, a feed of
         * images — where a control has to stay findable whatever colors pass beneath it.
         *
         * In the dark theme the disc is a step lighter than a surface rather than a surface: there
         * a shadow barely shows, so height has to read as lightness instead, and a surface disc
         * would vanish into the surface-colored ground a viewer or a card sits on.
         */
        public val Floating: TopBarActionStyle
            @Composable
            get() = TopBarActionStyle(
                bg = if (ArecaTheme.isDark) ArecaTheme.colors.bgMuted else ArecaTheme.colors.bgSurface,
                content = ArecaTheme.colors.fgBase,
                border = Color.Transparent,
                shape = CircleShape,
                elevation = ArecaTheme.elevation.sm,
                emphasis = TopBarActionEmphasis.Solid,
            )

        /**
         * Light glyphs in translucent dark discs.
         *
         * For a bar laid over a photograph or a dark illustration, where a white surface disc would
         * punch a hole in the picture and a bare glyph would vanish into it.
         */
        public val Scrim: TopBarActionStyle
            @Composable
            get() = TopBarActionStyle(
                bg = ArecaTheme.colors.bgOverlayDark,
                content = ArecaTheme.colors.fgOnFill,
                border = Color.Transparent,
                shape = CircleShape,
                elevation = 0.dp,
                emphasis = TopBarActionEmphasis.Solid,
            )
    }
}

/**
 * Whether the controls of this appearance sit in a container, which moves them onto the gutter by
 * their edge rather than by their glyph.
 */
public val TopBarActionStyle.isContained: Boolean
    get() = bg != Color.Transparent || border != Color.Transparent

/**
 * The step of the [dev.voir.areca.foundation.button.IconButtonStyle] ladder a colored control of a
 * bar is drawn at.
 */
public enum class TopBarActionEmphasis {
    /** Colored glyph or word, no fill. Beside bare neutral controls. */
    Plain,

    /** Colored glyph or word on the color's muted fill. Beside quiet neutral discs. */
    Soft,

    /** Reversed-out glyph or word on the color's full fill. Beside raised or scrim discs. */
    Solid,
}
