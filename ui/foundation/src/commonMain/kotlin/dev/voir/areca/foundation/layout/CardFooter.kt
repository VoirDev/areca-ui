package dev.voir.areca.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.button.LocalButtonSize
import dev.voir.areca.foundation.button.LocalButtonStyle
import dev.voir.areca.foundation.button.filledBackground
import dev.voir.areca.foundation.button.mutedBackground
import dev.voir.areca.foundation.button.onFilled
import dev.voir.areca.foundation.button.onMuted
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.theme.ArecaTheme

/** Opacity of a disabled footer button's label on a footer of the caller's own color. */
private const val DISABLED_CONTENT_ALPHA = 0.5f

/**
 * Colors of a [Card]'s footer: the strip along the bottom of the card that carries its status and
 * its actions — "Renews on 12 November · Manage", "Payment failed · Update card".
 *
 * The footer hands [content] to everything in it through [LocalContentColor], and [button] to every
 * [dev.voir.areca.foundation.button.Button] in it through [LocalButtonStyle], so copy and actions
 * take the footer's ink without being told: a brand footer's actions are white on brand, a danger
 * footer's are red on its tint. A button given a style of its own keeps it.
 *
 * Pick a preset by what the footer says, as with any color family:
 *
 * - [plain] — no fill, a hairline above. Supporting detail that should not compete with the card.
 * - [soft] — the family's muted tint, its text ink. A state worth noticing: a renewal date, a trial
 *   ending ([ActionColor.Warning]), a failed payment ([ActionColor.Danger]).
 * - [solid] — the family's base fill, its on-fill ink. The card's call to action — an upgrade on a
 *   plan card ([ActionColor.Brand]). One per screen, like a solid button.
 *
 * For a color outside the families — an informational blue, a plan's own color — build one with
 * [of], which derives the button style from [content].
 *
 * @property bg Fill of the strip; [Color.Transparent] lets the card show through.
 * @property content Color of the copy and glyphs in the footer.
 * @property divider Hairline along the top of the strip; [Color.Unspecified] draws none.
 * @property button Style of the buttons in the footer, unless a button is given its own.
 */
@Immutable
public data class CardFooterStyle(
    public val bg: Color,
    public val content: Color,
    public val divider: Color,
    public val button: ButtonStyle,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: [plain]. */
        public val Default: CardFooterStyle
            @Composable
            get() = plain()

        /**
         * No fill, with a hairline between the card's body and the footer.
         *
         * @param divider Color of the hairline.
         * @return A footer in the card's own colors.
         */
        @Composable
        public fun plain(
            divider: Color = ArecaTheme.colors.borderBase,
        ): CardFooterStyle = of(
            bg = Color.Transparent,
            content = ArecaTheme.colors.fgSubtle,
            divider = divider,
        )

        /**
         * Tinted with the family's muted step, the copy in the family's text ink.
         *
         * [ActionColor.Neutral] is a grey tint for a footer that only sets itself apart; on a
         * [CardStyle.SoftNeutral] card, whose fill is already grey, prefer [plain] or a colored
         * family.
         *
         * @param color Color family of the footer.
         * @return A tinted footer for [color].
         */
        @Composable
        public fun soft(color: ActionColor = ActionColor.Neutral): CardFooterStyle {
            val colors = ArecaTheme.colors
            return of(bg = color.mutedBackground(colors), content = color.onMuted(colors))
        }

        /**
         * Filled with the family's base step, the copy in the ink that reads on it.
         *
         * @param color Color family of the footer.
         * @return A filled footer for [color].
         */
        @Composable
        public fun solid(color: ActionColor = ActionColor.Brand): CardFooterStyle {
            val colors = ArecaTheme.colors
            return of(bg = color.filledBackground(colors), content = color.onFilled(colors))
        }

        /**
         * A footer of the caller's own colors.
         *
         * The buttons in it are borderless and fill-less, labelled in [content] and tinted with it
         * on hover and press, so they read on any fill [content] reads on. Pass a [button] of your
         * own for a footer whose action should be a filled button.
         *
         * @param bg Fill of the strip.
         * @param content Color of the copy, the glyphs and the button labels.
         * @param divider Hairline along the top of the strip; [Color.Unspecified] draws none.
         * @param button Style of the buttons in the footer.
         * @return A footer in the given colors.
         */
        @Composable
        public fun of(
            bg: Color,
            content: Color,
            divider: Color = Color.Unspecified,
            button: ButtonStyle = footerButton(content),
        ): CardFooterStyle = CardFooterStyle(bg = bg, content = content, divider = divider, button = button)

        /**
         * The quiet button a footer offers its actions: no fill, no border, its label in [content].
         *
         * @param content Ink of the footer.
         * @return A plain button style inked with [content].
         */
        @Composable
        private fun footerButton(content: Color): ButtonStyle {
            val shape = ArecaTheme.shapes.button
            return ButtonStyle(
                bg = Color.Transparent,
                border = Color.Transparent,
                content = content,
                contentDisabled = content.copy(alpha = content.alpha * DISABLED_CONTENT_ALPHA),
                shape = shape,
                indication = FadeIndication.tint(shape = shape, ink = content),
            )
        }
    }
}

/**
 * The footer strip of a [Card]: [style]'s fill and ink, [size]'s padding, type and button size.
 *
 * Laid out as a row with its items centred on one line and spaced apart, so the usual footer — a
 * line of status that takes `Modifier.weight(1f)`, then an action — needs nothing more.
 *
 * @param style Colors of the footer.
 * @param size Metrics of the card, whose footer values are used.
 * @param content Items of the footer, start to end.
 */
@Composable
internal fun CardFooter(
    style: CardFooterStyle,
    size: CardSize,
    content: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides style.content,
        LocalTextStyle provides size.footerTextStyle,
        LocalButtonStyle provides style.button,
        LocalButtonSize provides size.footerButton,
    ) {
        Row(
            modifier = Modifier
                .background(style.bg)
                .drawBehind {
                    if (style.divider == Color.Unspecified) return@drawBehind
                    // A hairline, floored at one pixel so it never vanishes on a low-density screen.
                    val thickness = FooterDividerThickness.toPx().coerceAtLeast(1f)
                    drawRect(
                        color = style.divider,
                        topLeft = Offset.Zero,
                        size = Size(this.size.width, thickness),
                    )
                }
                .padding(size.footerPadding)
                // One touch target tall by default whether or not it holds a button, so footers
                // line up; a footer that only states can lower it through its size.
                .heightIn(min = size.footerMinHeight),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            content = content,
        )
    }
}

/**
 * Stacks a card's body over its footer, the footer at least as wide as the body.
 *
 * A `Column` would either let the footer shrink to its own items, leaving a strip that stops short
 * of the card's edge, or stretch the card to the whole width it is offered. This measures the body
 * first and gives the footer the body's width as its minimum, so the strip always spans the card
 * and a card that wraps its content still does.
 *
 * @param body The card's content.
 * @param footer The footer strip.
 */
@Composable
internal fun CardBodyWithFooter(
    body: @Composable () -> Unit,
    footer: @Composable () -> Unit,
) {
    Layout(contents = listOf(body, footer)) { (bodyMeasurables, footerMeasurables), constraints ->
        val loose = constraints.copy(minHeight = 0)
        val bodyPlaceables = bodyMeasurables.map { it.measure(loose) }
        val bodyWidth = bodyPlaceables.maxOfOrNull { it.width } ?: 0
        val bodyHeight = bodyPlaceables.maxOfOrNull { it.height } ?: 0

        val footerConstraints = Constraints(
            minWidth = maxOf(bodyWidth, constraints.minWidth).coerceAtMost(constraints.maxWidth),
            maxWidth = constraints.maxWidth,
            minHeight = 0,
            maxHeight = Constraints.Infinity,
        )
        val footerPlaceables = footerMeasurables.map { it.measure(footerConstraints) }
        val footerHeight = footerPlaceables.maxOfOrNull { it.height } ?: 0

        val width = maxOf(bodyWidth, footerPlaceables.maxOfOrNull { it.width } ?: 0)
            .coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = (bodyHeight + footerHeight).coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(width, height) {
            bodyPlaceables.forEach { it.placeRelative(0, 0) }
            footerPlaceables.forEach { it.placeRelative(0, height - footerHeight) }
        }
    }
}

/** Thickness of the hairline above a footer whose style has a divider. */
private val FooterDividerThickness: Dp = 1.dp
