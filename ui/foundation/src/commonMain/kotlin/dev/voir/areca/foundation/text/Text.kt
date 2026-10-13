package dev.voir.areca.foundation.text

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * The role a piece of text plays, resolved against [dev.voir.areca.theme.ArecaTypography].
 *
 * An entry names what the text *is*, not how big it is: [Display] and [Caption] carry weight and
 * letter spacing as much as a size. Picking by role is what lets a screen survive a retuned type
 * scale, because [dev.voir.areca.theme.ArecaTypography] owns the measurements and this owns only
 * the choice.
 */
public enum class TextVariant {
    /** Subsection title inside a card or list group. */
    HeadingExtraSmall,

    /** Section heading within a screen. */
    HeadingSmall,

    /** Screen heading. */
    HeadingMedium,

    /** Page title. */
    HeadingLarge,

    /** Largest heading, for a screen whose title is its main content. */
    HeadingExtraLarge,

    /** Supporting copy shown alongside body text. */
    BodyExtraSmall,

    /** Compact body copy, as used inside dense components. */
    BodySmall,

    /** Default body copy. */
    BodyMedium,

    /** Lead paragraph, or body copy on a spacious screen. */
    BodyLarge,

    /** Emphasised body copy that is not a heading, such as a summary line. */
    BodyExtraLarge,

    /** Standalone display text, larger than any heading. Use once per screen at most. */
    Display,

    /** Short explanatory text under a field or image. */
    Caption,

    /** Label on a dense control, such as a badge or chip. */
    LabelSmall,

    /** Default control label: buttons, fields, list rows. */
    LabelMedium,

    /** Label on a large control, or a list row's primary line. */
    LabelLarge,
}

/**
 * A named foreground color, resolved against [dev.voir.areca.theme.ArecaColors].
 *
 * The three neutral entries are a hierarchy, not three greys: [Base] is what the reader is meant to
 * read, [Subtle] supports it, and [Muted] is legible but never the only carrier of meaning.
 */
public enum class TextColor {
    /** Primary text. The default, and what body copy should almost always use. */
    Base,

    /** Secondary text: supporting lines, inactive labels. */
    Subtle,

    /** Tertiary text: placeholders, timestamps, metadata. */
    Muted,

    /** Text of a control the user cannot act on. */
    Disabled,

    /** Content on the inverted [dev.voir.areca.theme.ArecaColors.bgContrast] surface. */
    OnContrast,

    /** Content on a solid semantic fill, such as a brand button or a danger badge. */
    OnFill,

    /** Brand-colored text, for emphasis tied to product identity. */
    Brand,

    /** Error text, such as a field's validation message. */
    Danger,

    /** Success text, such as a confirmation line. */
    Success,

    /** Informational text. */
    Info,

    /** Warning text. */
    Warning,
}

/** Text style this role resolves to under the current theme. */
public val TextVariant.style: TextStyle
    @Composable
    get() = when (this) {
        TextVariant.HeadingExtraSmall -> ArecaTheme.typography.headingExtraSmall
        TextVariant.HeadingSmall -> ArecaTheme.typography.headingSmall
        TextVariant.HeadingMedium -> ArecaTheme.typography.headingMedium
        TextVariant.HeadingLarge -> ArecaTheme.typography.headingLarge
        TextVariant.HeadingExtraLarge -> ArecaTheme.typography.headingExtraLarge
        TextVariant.BodyExtraSmall -> ArecaTheme.typography.bodyExtraSmall
        TextVariant.BodySmall -> ArecaTheme.typography.bodySmall
        TextVariant.BodyMedium -> ArecaTheme.typography.bodyMedium
        TextVariant.BodyLarge -> ArecaTheme.typography.bodyLarge
        TextVariant.BodyExtraLarge -> ArecaTheme.typography.bodyExtraLarge
        TextVariant.Display -> ArecaTheme.typography.display
        TextVariant.Caption -> ArecaTheme.typography.caption
        TextVariant.LabelSmall -> ArecaTheme.typography.labelSmall
        TextVariant.LabelMedium -> ArecaTheme.typography.labelMedium
        TextVariant.LabelLarge -> ArecaTheme.typography.labelLarge
    }

/** Foreground color this role resolves to under the current theme. */
public val TextColor.color: Color
    @Composable
    get() = when (this) {
        TextColor.Base -> ArecaTheme.colors.fgBase
        TextColor.Subtle -> ArecaTheme.colors.fgSubtle
        TextColor.Muted -> ArecaTheme.colors.fgMuted
        TextColor.Disabled -> ArecaTheme.colors.fgDisabled
        TextColor.OnContrast -> ArecaTheme.colors.fgOnContrast
        TextColor.OnFill -> ArecaTheme.colors.fgOnFill
        TextColor.Brand -> ArecaTheme.colors.fgBrandText
        TextColor.Danger -> ArecaTheme.colors.fgDangerText
        TextColor.Success -> ArecaTheme.colors.fgSuccessText
        TextColor.Info -> ArecaTheme.colors.fgInfoText
        TextColor.Warning -> ArecaTheme.colors.fgWarnText
    }

/**
 * Renders [text] with an explicit style and color.
 *
 * This is the low-level overload. Prefer the one taking a [TextVariant], which names a role instead
 * of spelling out a style, and only reach for this when inheriting from the surrounding container —
 * inside a [Heading] slot, say — or when a style is computed rather than chosen.
 *
 * Every optional parameter below [color] is merged over [style], so passing one overrides that
 * single attribute and leaves the rest of the style intact.
 *
 * @param text Text to render. A plain [String] takes a faster path than an [AnnotatedString].
 * @param modifier Modifier applied to the text node.
 * @param style Base style. Defaults to whatever the container provides through [LocalTextStyle].
 * @param color Foreground color. Defaults to what the container provides through [LocalContentColor].
 * @param fontSize Font size override.
 * @param lineHeight Line height override.
 * @param fontWeight Weight override.
 * @param fontFamily Family override.
 * @param fontStyle Slant override.
 * @param letterSpacing Letter spacing override.
 * @param textDecoration Underline or strikethrough override.
 * @param textAlign Alignment within the node's width.
 * @param overflow How text that does not fit is handled.
 * @param softWrap Whether text wraps onto further lines.
 * @param maxLines Largest number of lines to render.
 * @param minLines Smallest number of lines to reserve space for.
 * @param onTextLayout Called with the measured layout, for callers that need glyph positions.
 */
@Composable
public fun Text(
    text: CharSequence,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalContentColor.current,
    fontSize: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    fontStyle: FontStyle? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
) {
    val mergedStyle = style.merge(
        color = color,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
    )

    // A String goes to BasicText's own String overload rather than being wrapped. Wrapping would
    // allocate an AnnotatedString on every recomposition, and plain strings are the common case.
    if (text is String) {
        BasicText(
            text = text,
            modifier = modifier,
            style = mergedStyle,
            onTextLayout = onTextLayout,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
        )
    } else {
        BasicText(
            text = text.toAnnotatedString(),
            modifier = modifier,
            style = mergedStyle,
            onTextLayout = onTextLayout,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
        )
    }
}

/**
 * Renders [text] in a named role and color.
 *
 * The default way to write text in Areca. [variant] and [color] resolve against the current theme,
 * so a screen written this way follows a retuned type scale or palette without being touched.
 *
 * Every optional parameter below [color] is merged over the role's style, so passing one overrides
 * that single attribute and leaves the rest intact.
 *
 * @param text Text to render. A plain [String] takes a faster path than an [AnnotatedString].
 * @param variant Role the text plays, which decides its style.
 * @param modifier Modifier applied to the text node.
 * @param color Named foreground color.
 * @param fontSize Font size override.
 * @param lineHeight Line height override.
 * @param fontWeight Weight override.
 * @param fontFamily Family override.
 * @param fontStyle Slant override.
 * @param letterSpacing Letter spacing override.
 * @param textDecoration Underline or strikethrough override.
 * @param textAlign Alignment within the node's width.
 * @param overflow How text that does not fit is handled.
 * @param softWrap Whether text wraps onto further lines.
 * @param maxLines Largest number of lines to render.
 * @param minLines Smallest number of lines to reserve space for.
 * @param onTextLayout Called with the measured layout, for callers that need glyph positions.
 */
@Composable
public fun Text(
    text: CharSequence,
    variant: TextVariant,
    modifier: Modifier = Modifier,
    color: TextColor = TextColor.Base,
    fontSize: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    fontStyle: FontStyle? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        style = variant.style,
        color = color.color,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        fontStyle = fontStyle,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
    )
}

/**
 * Views this text as an [AnnotatedString], without copying when it already is one.
 *
 * @return The receiver itself when annotated, otherwise its characters with no spans.
 */
internal fun CharSequence.toAnnotatedString(): AnnotatedString = when (this) {
    is AnnotatedString -> this
    is String -> AnnotatedString(this)
    else -> AnnotatedString(toString())
}
