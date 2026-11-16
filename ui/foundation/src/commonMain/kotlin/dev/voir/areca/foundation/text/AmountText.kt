package dev.voir.areca.foundation.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.em
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.theme.ArecaTheme

/** Which side of the figures a currency symbol or a unit is drawn on. */
public enum class AmountSymbolPlacement {
    /** Before the figures: `$12`, `€12`. */
    Before,

    /** After the figures: `12 ₽`, `12 kg`. */
    After,
}

/**
 * An amount already formatted by the app, in its parts, so [AmountText] can set each part apart
 * without parsing a string.
 *
 * The app formats the figures for the user's locale — grouping, the decimal mark, the minus sign —
 * and passes the pieces; Areca only draws them.
 *
 * @property whole The whole part with its grouping: `"1,284"`, `"1 284"`.
 * @property fraction The minor units without the decimal mark: `"50"`; `null` for none.
 * @property decimalMark The mark between [whole] and [fraction]: `"."` or `","`.
 * @property symbol A currency symbol or a unit: `"€"`, `"د.إ"`, `"kg"`; `null` for none.
 * @property symbolPlacement Which side of the figures [symbol] is drawn on.
 * @property sign A sign before everything: `"−"`, `"+"`; `null` for none.
 * @property spokenText What a screen reader says for the amount — "minus 1,284 euros 50 cents";
 *   `null` reads the parts as drawn.
 */
@Immutable
public data class Amount(
    public val whole: String,
    public val fraction: String? = null,
    public val decimalMark: String = ".",
    public val symbol: String? = null,
    public val symbolPlacement: AmountSymbolPlacement = AmountSymbolPlacement.Before,
    public val sign: String? = null,
    public val spokenText: String? = null,
)

/**
 * A sum of money or a measured quantity: the figures at the style's size, the minor units and the
 * symbol a step smaller, every digit the same width.
 *
 * The symbol and the figures are two texts laid side by side, left to right, in the order
 * [Amount.symbolPlacement] gives: a symbol in another script — `د.إ` — can neither reorder the
 * figures nor be reordered by them, and no invisible control characters are added to the text, so
 * the figures truncate and copy as written. Tabular figures keep a column of amounts aligned and a
 * changing total from jittering. Pass [autoSize] for a hero figure that must stay on one line
 * however long it gets — the figures shrink, the symbol keeps its size; [masked] draws the figures
 * as dots, for a balance hidden until the user reveals it. Read aloud as one phrase: the parts as
 * drawn, or [Amount.spokenText].
 *
 * ```kotlin
 * AmountText(
 *     Amount(whole = "1,284", fraction = "50", symbol = "€", sign = "−"),
 *     style = ArecaTheme.typography.headingLarge,
 *     color = TextColor.Danger.color,
 * )
 * ```
 *
 * @param amount The parts to draw.
 * @param modifier Modifier applied to the row of parts.
 * @param style Style of the figures. Defaults to the one in scope.
 * @param color Colour of the whole amount. Defaults to the one in scope.
 * @param minorScale Size of the minor units and the symbol relative to the figures; `1f` keeps them
 *   level.
 * @param masked Whether the figures are hidden behind dots.
 * @param textAlign Where the amount sits in the width it is given — `TextAlign.End` for a column of
 *   amounts, with a modifier that gives it the column's width.
 * @param maxLines Largest number of lines of the figures; an amount is one line by default.
 * @param autoSize Shrinks the figures to fit what is left beside the symbol, `null` for none.
 */
@Composable
public fun AmountText(
    amount: Amount,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current,
    minorScale: Float = DefaultMinorScale,
    masked: Boolean = false,
    textAlign: TextAlign = TextAlign.Unspecified,
    maxLines: Int = 1,
    autoSize: TextAutoSize? = null,
) {
    val figuresStyle = style.merge(
        fontFeatureSettings = TabularFigures,
        textDirection = TextDirection.Ltr,
    )
    val symbolStyle = style.merge(
        fontSize = if (style.fontSize.isSpecifiedSp()) style.fontSize * minorScale else style.fontSize,
        textDirection = TextDirection.Content,
    )
    val minorSize = minorScale.em
    val symbolFirst = amount.symbol != null && amount.symbolPlacement == AmountSymbolPlacement.Before
    val gap = ArecaTheme.spacing.xs
    val figures = remember(amount, masked, minorSize) {
        buildAnnotatedString {
            // The sign leads the whole amount: before the symbol when the symbol comes first.
            if (!symbolFirst) amount.sign?.let { append(it) }
            append(if (masked) MaskDots else amount.whole)
            if (amount.fraction != null && !masked) {
                withStyle(SpanStyle(fontSize = minorSize)) {
                    append(amount.decimalMark)
                    append(amount.fraction)
                }
            }
        }
    }
    val spoken = amount.spokenText ?: buildString {
        amount.sign?.let { append(it) }
        if (amount.symbol != null && amount.symbolPlacement == AmountSymbolPlacement.Before) {
            append(amount.symbol).append(' ')
        }
        append(if (masked) MaskDots else amount.whole)
        if (amount.fraction != null && !masked) append(amount.decimalMark).append(amount.fraction)
        if (amount.symbol != null && amount.symbolPlacement == AmountSymbolPlacement.After) {
            append(' ').append(amount.symbol)
        }
    }

    // The parts run left to right in every locale, as amounts are written; the row is one node.
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(
            modifier = modifier.clearAndSetSemantics { contentDescription = spoken },
            horizontalArrangement = when (textAlign) {
                TextAlign.End, TextAlign.Right -> Arrangement.End
                TextAlign.Center -> Arrangement.Center
                else -> Arrangement.Start
            },
        ) {
            if (symbolFirst) {
                amount.sign?.let { sign ->
                    Text(text = sign, modifier = Modifier.alignByBaseline(), style = figuresStyle, color = color, maxLines = 1)
                }
                Text(
                    text = amount.symbol.orEmpty(),
                    modifier = Modifier.alignByBaseline().padding(end = gap),
                    style = symbolStyle,
                    color = color,
                    maxLines = 1,
                    softWrap = false,
                )
            }
            Text(
                text = figures,
                modifier = Modifier.weight(1f, fill = false).alignByBaseline(),
                style = figuresStyle,
                color = color,
                maxLines = maxLines,
                // A shrinking amount must never be cut: it shrinks until it fits instead.
                overflow = if (autoSize != null) TextOverflow.Clip else TextOverflow.Ellipsis,
                autoSize = autoSize,
            )
            if (amount.symbol != null && amount.symbolPlacement == AmountSymbolPlacement.After) {
                Text(
                    text = amount.symbol,
                    modifier = Modifier.alignByBaseline().padding(start = gap),
                    style = symbolStyle,
                    color = color,
                    maxLines = 1,
                    softWrap = false,
                )
            }
        }
    }
}

/** Whether this size is a concrete sp value that can be scaled. */
private fun androidx.compose.ui.unit.TextUnit.isSpecifiedSp(): Boolean = isSp

/** Default size of the minor units and the symbol relative to the figures. */
private const val DefaultMinorScale = 0.7f

/** OpenType feature that gives every digit the same advance. */
private const val TabularFigures = "tnum"

/** What a masked amount draws in place of its figures. */
private const val MaskDots = "••••"
