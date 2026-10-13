package dev.voir.areca.foundation.list

import androidx.compose.foundation.Indication
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.indication.TINT_HOVER_ALPHA
import dev.voir.areca.indication.TINT_PRESSED_ALPHA
import dev.voir.areca.theme.ArecaTheme

/**
 * Semantic tone of a [ListItem], the color half of the style axes.
 *
 * The tone says what the row means; the [ListItemStyle] appearance factories — `plain`, `surface`,
 * `outlined`, `soft`, `ghost` — say how much surface it carries. The two compose, so a destructive
 * row inside a card is `ListItemStyle.plain(ListItemColor.Danger)` and a highlighted success row is
 * `ListItemStyle.soft(ListItemColor.Success)`.
 */
public enum class ListItemColor {
    /** Ordinary content. The tone of almost every row. */
    Neutral,

    /** The product accent, for rows that carry the primary action or the current selection. */
    Brand,

    /** Destructive actions such as deleting, leaving or signing out. */
    Danger,

    /** Completed or healthy states. */
    Success,

    /** States that need attention but are not failures. */
    Warning,

    /** Neutral advisory states. */
    Info,
}

/**
 * Colors of a list row across its interaction states.
 *
 * A row draws its own hover, press and selected layers rather than delegating them to an
 * [Indication], which is what lets a selected row stay visibly selected while the pointer is over
 * it. The background is drawn in two layers: a base layer saying what the row *is* — at rest,
 * selected, disabled or dragging — and a tint laid over it saying what is being *done* to it.
 * [bgHovered] and [bgPressed] are that tint, so they are expected to be translucent; every other
 * background color is a base layer.
 *
 * Styles are built on two axes. The appearance factories — [plain], [surface], [outlined], [soft]
 * and [ghost] — decide how much surface a row carries; [ListItemColor] decides what it means. Every
 * appearance accepts every tone, so a call site names both rather than hand-assembling colors:
 *
 * ```kotlin
 * ListItem(title = { Text("Delete account") }, style = ListItemStyle.plain(ListItemColor.Danger))
 * ```
 *
 * Hover and press are translucent tints of the tone rather than opaque surfaces. A tint tracks the
 * theme on its own — it darkens a light background and lifts a dark one — so one token set stays
 * legible in both directions and over any background the row is placed on.
 *
 * @property bg Base background at rest.
 * @property bgHovered Tint laid over the base while hovered or keyboard-focused.
 * @property bgPressed Tint laid over the base while pressed.
 * @property bgSelected Base background while selected.
 * @property bgSelectedHovered Base background while selected and reached for; the hover or press
 * tint is still drawn on top of it.
 * @property bgDisabled Base background while disabled.
 * @property bgDragging Base background while being dragged.
 * @property title Title color at rest.
 * @property titleSelected Title color while selected.
 * @property titleDisabled Title color while disabled.
 * @property subtitle Subtitle color at rest.
 * @property subtitleSelected Subtitle color while selected.
 * @property subtitleDisabled Subtitle color while disabled.
 * @property overline Overline color at rest.
 * @property overlineSelected Overline color while selected.
 * @property overlineDisabled Overline color while disabled.
 * @property value Inline value color at rest.
 * @property valueActive Inline value color while hovered or pressed.
 * @property valueSelected Inline value color while selected.
 * @property valueDisabled Inline value color while disabled.
 * @property extra Leading and trailing slot color at rest.
 * @property extraActive Leading and trailing slot color while hovered or pressed.
 * @property extraSelected Leading and trailing slot color while selected.
 * @property extraDisabled Leading and trailing slot color while disabled.
 * @property border Border color; [Color.Unspecified] or a zero [borderWidth] draws no border.
 * @property borderSelected Border color while selected.
 * @property borderDisabled Border color while disabled.
 * @property borderWidth Width of the border.
 * @property selectedRail Color of the rail drawn along the leading edge of a selected row;
 * [Color.Unspecified] draws no rail.
 * @property shape Shape the row's background, state tints, rail and border are drawn in. The
 * row's content is not clipped to it, so an [indication] drawn outside the row stays whole.
 * @property indication Optional indication layered on top of the row's own state drawing. `null`
 * by default, because the row already answers hover and press itself.
 */
@Immutable
public data class ListItemStyle(
    public val bg: Color,
    public val bgHovered: Color,
    public val bgPressed: Color,
    public val bgSelected: Color,
    public val bgSelectedHovered: Color = bgSelected,
    public val bgDisabled: Color,
    public val bgDragging: Color = bgHovered,

    public val title: Color,
    public val titleSelected: Color = title,
    public val titleDisabled: Color,

    public val subtitle: Color,
    public val subtitleSelected: Color = subtitle,
    public val subtitleDisabled: Color,

    public val overline: Color,
    public val overlineSelected: Color = overline,
    public val overlineDisabled: Color,

    public val value: Color,
    public val valueActive: Color = value,
    public val valueSelected: Color = value,
    public val valueDisabled: Color,

    public val extra: Color,
    public val extraActive: Color = extra,
    public val extraSelected: Color = extra,
    public val extraDisabled: Color,

    public val border: Color = Color.Unspecified,
    public val borderSelected: Color = border,
    public val borderDisabled: Color = border,
    public val borderWidth: Dp = 0.dp,

    public val selectedRail: Color = Color.Unspecified,

    public val shape: Shape = RectangleShape,

    public val indication: Indication? = null,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** The default row style: a transparent, neutral row that tints on hover. */
        public val Default: ListItemStyle
            @Composable
            get() = plain()

        /**
         * A row with no surface of its own: transparent at rest, tinted while reached for, filled
         * while selected.
         *
         * This is the default appearance, and the one to use for rows inside a card, a sheet or a
         * [ListItemColumn] — the container owns the background and the corners, which is why the
         * row is square by default. It is also the answer to needing a different background per
         * row: pick a tone instead of passing a color.
         *
         * @param color Tone of the row.
         * @param shape Shape the row draws its states in. Square by default so rows stack
         * seamlessly inside a container that rounds them.
         * @return Colors of a plain row.
         */
        @Composable
        public fun plain(
            color: ListItemColor = ListItemColor.Neutral,
            shape: Shape = RectangleShape,
        ): ListItemStyle {
            val tone = toneOf(color)
            return toneStyle(
                tone = tone,
                bg = Color.Transparent,
                bgSelected = tone.softBg,
                bgDisabled = Color.Transparent,
                shape = shape,
            )
        }

        /**
         * A row that carries its own opaque surface, for rows placed directly on the canvas.
         *
         * @param color Tone of the row.
         * @param shape Shape the row draws its states in.
         * @return Colors of a surface row.
         */
        @Composable
        public fun surface(
            color: ListItemColor = ListItemColor.Neutral,
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.md),
        ): ListItemStyle {
            val tone = toneOf(color)
            return toneStyle(
                tone = tone,
                bg = ArecaTheme.colors.bgSurface,
                bgSelected = tone.softBg,
                bgDisabled = ArecaTheme.colors.bgSurface,
                shape = shape,
            )
        }

        /**
         * A surface row with a hairline border, for standalone rows that need to read as separate
         * objects rather than as a stack.
         *
         * @param color Tone of the row.
         * @param shape Shape the row and its border follow.
         * @param borderWidth Width of the border.
         * @return Colors of an outlined row.
         */
        @Composable
        public fun outlined(
            color: ListItemColor = ListItemColor.Neutral,
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.md),
            borderWidth: Dp = 1.dp,
        ): ListItemStyle {
            val tone = toneOf(color)
            return toneStyle(
                tone = tone,
                bg = ArecaTheme.colors.bgSurface,
                bgSelected = tone.softBg,
                bgDisabled = ArecaTheme.colors.bgSurface,
                shape = shape,
                border = tone.border,
                borderSelected = tone.rail,
                borderWidth = borderWidth,
            )
        }

        /**
         * A row filled with a muted wash of its tone, for a row that is called out among its
         * neighbours — a recommended plan, a failed upload, an unread thread.
         *
         * Selection is carried by the rail and the title rather than by a second fill, so a soft
         * row that is also selected does not turn into a third background.
         *
         * @param color Tone of the row.
         * @param shape Shape the row draws its states in.
         * @return Colors of a soft row.
         */
        @Composable
        public fun soft(
            color: ListItemColor = ListItemColor.Brand,
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.md),
        ): ListItemStyle {
            val tone = toneOf(color)
            return toneStyle(
                tone = tone,
                bg = tone.softBg,
                bgSelected = tone.softBg,
                bgDisabled = ArecaTheme.colors.bgDisabled,
                shape = shape,
            )
        }

        /**
         * A tight, rounded, transparent row for menus, popovers and command palettes.
         *
         * It has no selected rail: a menu marks its current entry with a check mark, and a rail
         * inside a floating surface reads as a scrollbar. Pair it with [ListItemSize.Compact] and a
         * [CheckmarkListItem].
         *
         * @param color Tone of the row.
         * @param shape Shape the row draws its states in.
         * @return Colors of a ghost row.
         */
        @Composable
        public fun ghost(
            color: ListItemColor = ListItemColor.Neutral,
            shape: Shape = RoundedCornerShape(ArecaTheme.radius.sm),
        ): ListItemStyle {
            val tone = toneOf(color)
            return toneStyle(
                tone = tone,
                bg = Color.Transparent,
                bgSelected = tone.softBg,
                bgDisabled = Color.Transparent,
                shape = shape,
                selectedRail = Color.Unspecified,
            )
        }
    }
}

/** Colors of the rows of the enclosing list, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalListItemStyle: ProvidableCompositionLocal<ListItemStyle?> =
    compositionLocalOf { null }

/**
 * Opacity of the hover tint laid over a toned row. Heavier than the neutral tint so the hue of the
 * tone survives against a neutral background instead of reading as grey.
 */
private const val TONED_HOVER_TINT_ALPHA = 0.10f

/** Opacity of the press tint laid over a toned row. */
private const val TONED_PRESSED_TINT_ALPHA = 0.18f

/**
 * Assembles a full style from a tone and the few colors an appearance actually varies.
 *
 * Text, slot and state colors follow from the tone, which is what keeps the appearances consistent
 * with each other: only the base layers and the border differ between them.
 *
 * @param tone Resolved colors of the row tone.
 * @param bg Base row background at rest.
 * @param bgSelected Base row background while selected.
 * @param bgDisabled Base row background while disabled.
 * @param shape Shape the row draws its states in.
 * @param border Border color at rest.
 * @param borderSelected Border color while selected.
 * @param borderWidth Width of the border.
 * @param selectedRail Color of the selected rail.
 * @return Colors of the row.
 */
@Composable
private fun toneStyle(
    tone: ListItemTone,
    bg: Color,
    bgSelected: Color,
    bgDisabled: Color,
    shape: Shape,
    border: Color = Color.Unspecified,
    borderSelected: Color = border,
    borderWidth: Dp = 0.dp,
    selectedRail: Color = tone.rail,
): ListItemStyle = ListItemStyle(
    bg = bg,
    bgHovered = tone.tint.copy(alpha = tone.hoverAlpha),
    bgPressed = tone.tint.copy(alpha = tone.pressedAlpha),
    bgSelected = bgSelected,
    bgSelectedHovered = bgSelected,
    bgDisabled = bgDisabled,
    bgDragging = tone.tint.copy(alpha = tone.pressedAlpha),
    title = tone.title,
    titleSelected = tone.title,
    titleDisabled = ArecaTheme.colors.fgDisabled,
    subtitle = ArecaTheme.colors.fgMuted,
    subtitleSelected = ArecaTheme.colors.fgSubtle,
    subtitleDisabled = ArecaTheme.colors.fgDisabled,
    overline = ArecaTheme.colors.fgMuted,
    overlineSelected = ArecaTheme.colors.fgSubtle,
    overlineDisabled = ArecaTheme.colors.fgDisabled,
    value = ArecaTheme.colors.fgSubtle,
    valueActive = ArecaTheme.colors.fgBase,
    valueSelected = ArecaTheme.colors.fgBase,
    valueDisabled = ArecaTheme.colors.fgDisabled,
    extra = tone.extra,
    extraActive = tone.extraActive,
    extraSelected = tone.extraActive,
    extraDisabled = ArecaTheme.colors.fgDisabled,
    border = border,
    borderSelected = borderSelected,
    borderDisabled = ArecaTheme.colors.borderDisabled,
    borderWidth = borderWidth,
    selectedRail = selectedRail,
    shape = shape,
)

/**
 * Resolves the theme colors that stand for a tone.
 *
 * @param color Tone to resolve.
 * @return Colors the appearances build their styles from.
 */
@Composable
private fun toneOf(color: ListItemColor): ListItemTone = when (color) {
    // The only tone whose rail is not its copy color: a neutral row is still selected *into* the
    // product accent, while its title stays plain body copy.
    ListItemColor.Neutral -> ListItemTone(
        title = ArecaTheme.colors.fgBase,
        extra = ArecaTheme.colors.fgMuted,
        extraActive = ArecaTheme.colors.fgBase,
        rail = ArecaTheme.colors.accentBase,
        tint = ArecaTheme.colors.fgBase,
        softBg = ArecaTheme.colors.bgMuted,
        border = ArecaTheme.colors.borderBase,
        // The same tints as a pressable card's, so neutral surfaces answer a hover alike.
        hoverAlpha = TINT_HOVER_ALPHA,
        pressedAlpha = TINT_PRESSED_ALPHA,
    )

    // Brand and Danger take the icon foreground rather than the text one: a tone's color runs the
    // slots and the selected rail as well as the title, and at the text step the red reads as
    // brown. Success, Warning and Info stay on their text step.
    ListItemColor.Brand -> tonedTone(
        title = ArecaTheme.colors.fgBrand,
        softBg = ArecaTheme.colors.bgBrandMuted,
        border = ArecaTheme.colors.accentSubtle,
    )

    ListItemColor.Danger -> tonedTone(
        title = ArecaTheme.colors.fgDanger,
        softBg = ArecaTheme.colors.bgDangerMuted,
        border = ArecaTheme.colors.borderDanger,
    )

    ListItemColor.Success -> tonedTone(
        title = ArecaTheme.colors.fgSuccessText,
        softBg = ArecaTheme.colors.bgSuccessMuted,
        border = ArecaTheme.colors.borderSuccess,
    )

    ListItemColor.Warning -> tonedTone(
        title = ArecaTheme.colors.fgWarnText,
        softBg = ArecaTheme.colors.bgWarnMuted,
        border = ArecaTheme.colors.borderWarn,
    )

    ListItemColor.Info -> tonedTone(
        title = ArecaTheme.colors.fgInfoText,
        softBg = ArecaTheme.colors.bgInfoMuted,
        border = ArecaTheme.colors.borderInfo,
    )
}

/**
 * Builds a non-neutral tone, whose one color carries the title, the slots and the rail.
 *
 * @param title Copy color of the tone, which also carries its slots and its selected rail.
 * @param softBg Muted background of the tone.
 * @param border Border color of the tone.
 * @return Resolved colors of the tone.
 */
private fun tonedTone(
    title: Color,
    softBg: Color,
    border: Color,
): ListItemTone = ListItemTone(
    title = title,
    extra = title,
    extraActive = title,
    rail = title,
    tint = title,
    softBg = softBg,
    border = border,
    hoverAlpha = TONED_HOVER_TINT_ALPHA,
    pressedAlpha = TONED_PRESSED_TINT_ALPHA,
)

/**
 * Theme colors that stand for one [ListItemColor], shared by every appearance.
 *
 * @property title Copy color of the title.
 * @property extra Color of the leading and trailing slots at rest.
 * @property extraActive Color of those slots while the row is reached for or selected.
 * @property rail Color of the selected rail, and of the border of a selected `outlined` row.
 * @property tint Color the hover and press tints are made of.
 * @property softBg Muted background used while selected, and as the fill of `soft`.
 * @property border Border color used by `outlined`.
 * @property hoverAlpha Opacity of the hover tint.
 * @property pressedAlpha Opacity of the press tint.
 */
@Immutable
private data class ListItemTone(
    val title: Color,
    val extra: Color,
    val extraActive: Color,
    val rail: Color,
    val tint: Color,
    val softBg: Color,
    val border: Color,
    val hoverAlpha: Float,
    val pressedAlpha: Float,
)
