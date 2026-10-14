package dev.voir.areca.foundation.layout

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.StrokeIndication
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.MinimumTouchTarget
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors, shape, and interaction feedback for [Card].
 *
 * Every preset takes `shapes.lg`, the corner of a settings section and a notice, so the surfaces of
 * one screen share one corner. A card nested inside another surface steps down one radius; see the
 * card-corner rule in the foundation guide.
 *
 * Three states rather than two: besides disabled, a card can be **active** — selected, current, or
 * expanded — which is a state the card is *in* rather than one the user is causing. A pressed card
 * is the indication's job; an active one is the style's.
 *
 * @property bg Fill at rest.
 * @property bgDisabled Fill while unavailable.
 * @property bgActive Fill while active.
 * @property border Border at rest. [Color.Unspecified] draws no border.
 * @property borderDisabled Border while unavailable.
 * @property borderActive Border while active.
 * @property content Color handed to children through [LocalContentColor] at rest.
 * @property contentDisabled Color handed to children while unavailable.
 * @property contentActive Color handed to children while active.
 * @property shape Shape of the fill, the border, and the indication alike.
 * @property indication Hover, press, and focus feedback. `null` for a card that is never pressed.
 */
@Immutable
public data class CardStyle(
    public val bg: Color,
    public val bgDisabled: Color = bg,
    public val bgActive: Color = bg,
    public val border: Color = Color.Unspecified,
    public val borderDisabled: Color = border,
    public val borderActive: Color = border,
    public val content: Color = Color.Unspecified,
    public val contentDisabled: Color = content,
    public val contentActive: Color = content,
    public val shape: Shape,
    public val indication: Indication? = FadeIndication(shape = shape),
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: a plain surface with no border, tinted towards its copy when pressed. */
        public val Default: CardStyle
            @Composable
            get() = default()

        /** A bordered card on the canvas. The default for content the user can act on. */
        public val Outlined: CardStyle
            @Composable
            get() = outlined()

        /** A tinted card with no border, for content that groups rather than separates. */
        public val SoftNeutral: CardStyle
            @Composable
            get() = softNeutral()

        /** Invisible at rest, tinting only on hover and press. For a whole-row target. */
        public val GhostNeutral: CardStyle
            @Composable
            get() = ghostNeutral()

        /** A filled brand card, for a single promoted item. */
        public val SolidBrand: CardStyle
            @Composable
            get() = solidBrand()

        /**
         * Builds a style from the current theme.
         *
         * [indication] only runs on a card given an `onClick`, so a static card is unaffected by
         * it. It defaults to [FadeIndication.tint] — the press a neutral list row answers with — so
         * a pressable card beside a list answers the finger the way the rows do rather than not at
         * all. Pass `null` for a pressable card that shows its press some other way.
         *
         * @param bg Fill at rest.
         * @param border Border at rest.
         * @param content Color handed to children.
         * @param shape Shape of the fill, border, and indication.
         * @param indication Hover, press, and focus feedback.
         * @return A card style containing the supplied values.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgSurface,
            border: Color = Color.Unspecified,
            content: Color = ArecaTheme.colors.fgBase,
            shape: Shape = ArecaTheme.shapes.lg,
            indication: Indication? = FadeIndication.tint(shape),
        ): CardStyle = CardStyle(
            bg = bg,
            border = border,
            content = content,
            shape = shape,
            indication = indication
        )

        /**
         * Creates an outlined card style.
         *
         * @param shape Shape used for the card container.
         * @return The preset style with [shape] applied to its fill, border, and indication.
         */
        @Composable
        public fun outlined(shape: Shape = ArecaTheme.shapes.lg): CardStyle =
            CardStyle(
                bg = ArecaTheme.colors.bgCanvas,
                bgDisabled = ArecaTheme.colors.bgDisabled,
                bgActive = ArecaTheme.colors.bgCanvas,
                border = ArecaTheme.colors.borderBase,
                borderDisabled = ArecaTheme.colors.borderDisabled,
                borderActive = ArecaTheme.colors.accentBase,
                content = ArecaTheme.colors.fgBase,
                contentDisabled = ArecaTheme.colors.fgDisabled,
                contentActive = ArecaTheme.colors.fgBase,
                shape = shape,
                indication = StrokeIndication(
                    strokeColor = ArecaTheme.colors.accentSubtle,
                    shape = shape
                )
            )

        /**
         * Creates a soft neutral card style.
         *
         * @param shape Shape used for the card container.
         * @return The preset style with [shape] applied to its fill, border, and indication.
         */
        @Composable
        public fun softNeutral(shape: Shape = ArecaTheme.shapes.lg): CardStyle =
            CardStyle(
                bg = ArecaTheme.colors.bgSubtle,
                bgDisabled = ArecaTheme.colors.bgDisabled,
                bgActive = ArecaTheme.colors.bgMuted,
                border = ArecaTheme.colors.bgSubtle,
                borderDisabled = ArecaTheme.colors.bgDisabled,
                borderActive = ArecaTheme.colors.bgMuted,
                content = ArecaTheme.colors.fgBase,
                contentDisabled = ArecaTheme.colors.fgDisabled,
                contentActive = ArecaTheme.colors.fgBase,
                shape = shape,
                indication = TouchIndication(
                    color = ArecaTheme.colors.bgNeutralIntense,
                    shape = shape
                )
            )

        /**
         * Creates a solid brand card style.
         *
         * @param shape Shape used for the card container.
         * @return The preset style with [shape] applied to its fill, border, and indication.
         */
        @Composable
        public fun solidBrand(shape: Shape = ArecaTheme.shapes.lg): CardStyle =
            CardStyle(
                bg = ArecaTheme.colors.bgBrandBase,
                bgDisabled = ArecaTheme.colors.bgDisabled,
                bgActive = ArecaTheme.colors.bgBrandBase,
                border = ArecaTheme.colors.bgBrandMuted.copy(0f),
                borderDisabled = ArecaTheme.colors.bgBrandMuted.copy(0f),
                borderActive = ArecaTheme.colors.bgBrandMuted,
                content = ArecaTheme.colors.fgOnFill,
                contentDisabled = ArecaTheme.colors.fgDisabled,
                contentActive = ArecaTheme.colors.fgOnFill,
                shape = shape,
                indication = TouchIndication(
                    color = ArecaTheme.colors.bgBrandIntense,
                    shape = shape
                )
            )

        /**
         * Creates a transparent neutral card style with hover and press feedback.
         *
         * @param shape Shape used for the card container.
         * @return The preset style with [shape] applied to its fill, border, and indication.
         */
        @Composable
        public fun ghostNeutral(shape: Shape = ArecaTheme.shapes.lg): CardStyle =
            CardStyle(
                bg = ArecaTheme.colors.bgSubtle.copy(alpha = 0f),
                bgDisabled = ArecaTheme.colors.bgSubtle.copy(alpha = 0f),
                bgActive = ArecaTheme.colors.bgSubtle,
                border = Color.Transparent,
                borderDisabled = Color.Transparent,
                borderActive = Color.Transparent,
                content = ArecaTheme.colors.fgBase,
                contentDisabled = ArecaTheme.colors.fgDisabled,
                contentActive = ArecaTheme.colors.fgBase,
                shape = shape,
                indication = FadeIndication(
                    color = ArecaTheme.colors.bgSubtle.copy(alpha = 0f),
                    hoverColor = ArecaTheme.colors.bgSubtle.copy(alpha = .5f),
                    pressedColor = ArecaTheme.colors.bgSubtle.copy(alpha = 1f),
                    shape = shape,
                )
            )
    }
}

/**
 * Padding, border width and footer metrics for [Card].
 *
 * [padding] is handed to the content slot rather than applied by the card, so a card can have an
 * edge-to-edge image at the top and padded text below it without fighting a container that has
 * already inset everything. The footer, which the card lays out itself, is padded by
 * [footerPadding].
 *
 * @property padding Inner spacing offered to the content.
 * @property borderWidth Width of the border, drawn for styles that define one.
 * @property footerPadding Inner spacing of the footer strip.
 * @property footerTextStyle Text style handed to the footer's copy.
 * @property footerButton Size handed to the buttons in the footer.
 * @property footerMinHeight Smallest height of the footer's line, inside [footerPadding].
 */
@Immutable
public data class CardSize(
    public val padding: PaddingValues,
    public val borderWidth: Dp,
    public val footerPadding: PaddingValues,
    public val footerTextStyle: TextStyle,
    public val footerButton: ButtonSize,
    public val footerMinHeight: Dp,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default. */
        public val Default: CardSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * The footer's line is one touch target tall, with or without a button in it, so footers
         * of every kind are the same height. A footer that only states — a date on a band, never a
         * button — can drop [footerMinHeight] to `0.dp` and pad itself as tightly as it likes. It is inset like the body at the start and barely at
         * the end, because a footer's button is borderless and carries its own padding: its label
         * then ends about where the body's copy would.
         *
         * @param padding Inner spacing offered to the content.
         * @param borderWidth Width of the border.
         * @param footerPadding Inner spacing of the footer strip.
         * @param footerTextStyle Text style of the footer's copy.
         * @param footerButton Size of the buttons in the footer.
         * @param footerMinHeight Smallest height of the footer's line.
         * @return A card size containing the supplied values.
         */
        @Composable
        public fun default(
            padding: PaddingValues = PaddingValues(ArecaTheme.spacing.xl),
            borderWidth: Dp = 1.dp,
            footerPadding: PaddingValues = PaddingValues(
                start = ArecaTheme.spacing.xl,
                end = ArecaTheme.spacing.xs,
                top = ArecaTheme.spacing.xs,
                bottom = ArecaTheme.spacing.xs,
            ),
            footerTextStyle: TextStyle = ArecaTheme.typography.bodySmall,
            footerButton: ButtonSize = ButtonSize.Small,
            footerMinHeight: Dp = MinimumTouchTarget,
        ): CardSize = CardSize(
            padding = padding,
            borderWidth = borderWidth,
            footerPadding = footerPadding,
            footerTextStyle = footerTextStyle,
            footerButton = footerButton,
            footerMinHeight = footerMinHeight,
        )
    }
}

/** Default [CardSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalCardSize: ProvidableCompositionLocal<CardSize?> = compositionLocalOf { null }

/** Default [CardStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalCardStyle: ProvidableCompositionLocal<CardStyle?> = compositionLocalOf { null }

/**
 * A surface that groups content, optionally pressable.
 *
 * The content slot is handed the card's [CardSize.padding] rather than having it applied for it.
 * That is deliberate: a card whose padding is already baked in cannot hold a full-bleed image, a
 * divider that reaches both edges, or a list whose rows have their own insets. Apply the padding
 * where it belongs, or ignore it.
 *
 * [active] is a state the card is in — selected, current, expanded — as opposed to a state the user
 * is causing, which is the indication's job. It is not announced, so a card that is meaningfully
 * selected should say so through its own content or a `selectable` modifier.
 *
 * A pressable card is announced as a button, with [onClickLabel] naming what pressing it does.
 *
 * A [footer] is a strip along the bottom of the card, laid out by the card rather than handed
 * padding: its status and its actions, filled and inked by [footerStyle] — a renewal date with a
 * "Manage" action, a failed payment with "Update card". Copy in it takes [CardFooterStyle.content]
 * and buttons in it take [CardFooterStyle.button], so a brand-filled footer reads white without
 * restyling what goes in it. The strip follows the card's bottom corners.
 *
 * ```kotlin
 * Card(
 *     modifier = Modifier.fillMaxWidth(),
 *     style = CardStyle.Outlined,
 *     footerStyle = CardFooterStyle.soft(ActionColor.Brand),
 *     footer = {
 *         Text("Renews on 12 November", modifier = Modifier.weight(1f))
 *         Button(onClick = onManage) { Text("Manage") }
 *     },
 * ) { padding ->
 *     PlanSummary(modifier = Modifier.padding(padding))
 * }
 * ```
 *
 * @param modifier Modifier applied to the card.
 * @param style Colors, shape, and indication. Defaults to the theme's.
 * @param size Padding and border width. Defaults to the theme's, or [CardSize.Default].
 * @param enabled Whether a pressable card accepts input, and whether it draws enabled colors.
 * @param active Whether the card draws its active state.
 * @param onClick Called when the card is pressed. `null` makes it a static surface.
 * @param onClickLabel What pressing the card does, read by assistive technology. Ignored when
 *   [onClick] is `null`.
 * @param interactionSource Source of the card's press, hover, and focus interactions. `null` lets
 *   the card create its own.
 * @param footerStyle Fill and ink of [footer].
 * @param footer Strip along the bottom of the card, its items laid out in a row; `null` for none.
 * @param content The card's body, handed [size]'s padding.
 */
@Composable
public fun Card(
    modifier: Modifier = Modifier,
    style: CardStyle = LocalCardStyle.current ?: CardStyle.Default,
    size: CardSize = LocalCardSize.current ?: CardSize.Default,
    enabled: Boolean = true,
    active: Boolean = false,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    footerStyle: CardFooterStyle = CardFooterStyle.Default,
    footer: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable BoxScope.(padding: PaddingValues) -> Unit,
) {
    val bgColor = when {
        !enabled -> style.bgDisabled
        active -> style.bgActive
        else -> style.bg
    }
    val borderColor = when {
        !enabled -> style.borderDisabled
        active -> style.borderActive
        else -> style.border
    }
    val contentColor = when {
        !enabled -> style.contentDisabled
        active -> style.contentActive
        else -> style.content
    }

    val click = onClick
    val pressModifier = if (click != null) {
        Modifier
            .clickable(
                enabled = enabled,
                onClickLabel = onClickLabel,
                role = Role.Button,
                onClick = click,
                // Read straight off the style: remembering it would pin the first frame's colors
                // and leave them stale after a theme or style change.
                indication = style.indication,
                interactionSource = interactionSource,
            )
            .clickCursor(enabled = enabled)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .background(bgColor, style.shape)
            .border(width = size.borderWidth, color = borderColor, shape = style.shape)
            .then(pressModifier)
            // Only a footer needs the content clipped, so its fill follows the bottom corners; the
            // border is drawn over it, and the press indication sits outside the clip.
            .thenIf(footer != null) { clip(style.shape) }
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            if (footer == null) {
                content(size.padding)
            } else {
                CardBodyWithFooter(
                    body = { Box { content(size.padding) } },
                    footer = { CardFooter(style = footerStyle, size = size, content = footer) },
                )
            }
        }
    }
}

/**
 * A card whose lower half opens and closes.
 *
 * Expansion goes through [Expandable], so a card and a list row open the same way — the content
 * fades in over a vertical expand rather than appearing at full opacity.
 *
 * [expandedContent] is composed only while open, so an expensive detail section costs nothing until
 * it is opened.
 *
 * @param expanded Whether the lower half is open.
 * @param modifier Modifier applied to the card.
 * @param style Colors, shape, and indication. Defaults to the theme's.
 * @param size Padding and border width. Defaults to the theme's, or [CardSize.Default].
 * @param enabled Whether a pressable card accepts input, and whether it draws enabled colors.
 * @param active Whether the card draws its active state.
 * @param onClick Called when the card is pressed. `null` makes it a static surface.
 * @param onClickLabel What pressing the card does, read by assistive technology. Ignored when
 *   [onClick] is `null`.
 * @param interactionSource Source of the card's press, hover, and focus interactions. `null` lets
 *   the card create its own.
 * @param transition How the lower half opens and closes.
 * @param footerStyle Fill and ink of [footer].
 * @param footer Strip along the bottom of the card, below the part that opens; `null` for none.
 * @param expandedContent The part that opens, handed [size]'s padding.
 * @param content The part that is always shown, handed [size]'s padding.
 */
@Composable
public fun Card(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    style: CardStyle = LocalCardStyle.current ?: CardStyle.Default,
    size: CardSize = LocalCardSize.current ?: CardSize.Default,
    enabled: Boolean = true,
    active: Boolean = false,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    transition: ExpandableTransition = ExpandableTransition.Default,
    footerStyle: CardFooterStyle = CardFooterStyle.Default,
    footer: (@Composable RowScope.() -> Unit)? = null,
    expandedContent: @Composable ColumnScope.(padding: PaddingValues) -> Unit,
    content: @Composable ColumnScope.(padding: PaddingValues) -> Unit,
) {
    Card(
        modifier = modifier,
        style = style,
        size = size,
        enabled = enabled,
        active = active,
        onClick = onClick,
        onClickLabel = onClickLabel,
        interactionSource = interactionSource,
        footerStyle = footerStyle,
        footer = footer,
    ) { padding ->
        Column(modifier = Modifier.fillMaxWidth()) {
            content(padding)

            Expandable(
                expanded = expanded,
                modifier = Modifier.fillMaxWidth(),
                transition = transition,
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    expandedContent(padding)
                }
            }
        }
    }
}
