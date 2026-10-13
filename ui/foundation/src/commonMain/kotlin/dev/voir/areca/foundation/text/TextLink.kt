package dev.voir.areca.foundation.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import dev.voir.areca.theme.ArecaTheme

/**
 * Underline treatment applied to a text link across its interaction states.
 */
public enum class TextLinkDecoration {
    /** Never underlines the link. */
    None,

    /** Underlines the link in every state. */
    Always,

    /** Underlines the link only while it is focused, hovered, or pressed. */
    OnInteraction,
}

/**
 * Link styles for this named foreground color, using the default weight and decoration.
 *
 * Mirrors [TextColor.color], so a link can adopt the color of the text it sits in and still pick up
 * hover and press treatments. Reach for [textLinkStyles] when the link also needs a weight or an
 * underline.
 */
public val TextColor.linkStyles: TextLinkStyles
    @Composable
    get() = textLinkStyles(color = this)

/**
 * Creates [TextLinkStyles] whose resting, focused, hovered, and pressed states are resolved from
 * the current theme.
 *
 * @param color Named foreground color of the resting link.
 * @param fontWeight Weight applied in every state; `null` keeps the surrounding text weight.
 * @param decoration Underline treatment across interaction states.
 * @return Link styles ready to pass to [LinkAnnotation] or a text link.
 */
@Composable
public fun textLinkStyles(
    color: TextColor = TextColor.Brand,
    fontWeight: FontWeight? = ArecaTheme.typography.labelMedium.fontWeight,
    decoration: TextLinkDecoration = TextLinkDecoration.None,
): TextLinkStyles {
    val restingDecoration = when (decoration) {
        TextLinkDecoration.None -> TextDecoration.None
        TextLinkDecoration.Always -> TextDecoration.Underline
        TextLinkDecoration.OnInteraction -> TextDecoration.None
    }
    val interactedDecoration = when (decoration) {
        TextLinkDecoration.None -> TextDecoration.None
        TextLinkDecoration.Always, TextLinkDecoration.OnInteraction -> TextDecoration.Underline
    }

    val style = SpanStyle(
        color = color.color,
        fontWeight = fontWeight,
        textDecoration = restingDecoration,
    )

    return TextLinkStyles(
        style = style,
        focusedStyle = style.copy(textDecoration = interactedDecoration),
        hoveredStyle = style.copy(
            color = color.hoveredColor,
            textDecoration = interactedDecoration,
        ),
        pressedStyle = style.copy(
            color = color.pressedColor,
            textDecoration = interactedDecoration,
        ),
    )
}

/**
 * Appends [text] as a clickable link styled with [styles].
 *
 * @param text Link label appended to the builder.
 * @param styles Visual treatment of the link across its interaction states.
 * @param tag Annotation tag carried by the link; defaults to [text].
 * @param onClick Invoked when the link is activated by click, tap, or keyboard.
 */
public fun AnnotatedString.Builder.appendTextLink(
    text: String,
    styles: TextLinkStyles,
    tag: String = text,
    onClick: () -> Unit,
) {
    withLink(
        LinkAnnotation.Clickable(
            tag = tag,
            styles = styles,
            linkInteractionListener = { onClick() },
        ),
    ) {
        append(text)
    }
}

/**
 * A standalone pressable label, such as "Forgot password?" or a row's "Sign out".
 *
 * The label is a link annotation rather than a clickable box, so its resting, hovered, focused and
 * pressed treatments all come from [textLinkStyles] instead of being spelled out per screen. A
 * disabled link renders as plain disabled text: there is nothing to press, so it does not announce
 * itself as a link.
 *
 * @param text Label to show.
 * @param onClick Called when the link is activated by click, tap, or keyboard.
 * @param modifier Modifier applied to the text.
 * @param enabled Whether the link accepts input; a disabled link greys out and stops responding.
 * @param color Named foreground color of the resting link.
 * @param variant Typography token for the label.
 * @param fontWeight Weight applied in every state; `null` keeps the weight [variant] carries.
 * @param decoration Underline treatment across interaction states.
 */
@Composable
public fun TextLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: TextColor = TextColor.Brand,
    variant: TextVariant = TextVariant.LabelMedium,
    fontWeight: FontWeight? = null,
    decoration: TextLinkDecoration = TextLinkDecoration.None,
) {
    if (!enabled) {
        Text(
            text = text,
            modifier = modifier,
            variant = variant,
            color = TextColor.Disabled,
            fontWeight = fontWeight,
        )
        return
    }

    val label = buildAnnotatedString {
        appendTextLink(
            text = text,
            styles = textLinkStyles(
                color = color,
                fontWeight = fontWeight,
                decoration = decoration,
            ),
            onClick = onClick,
        )
    }

    Text(text = label, modifier = modifier, variant = variant)
}

/**
 * A sentence with one part of it you can press — "Don't have an account? Sign up".
 *
 * The link is part of the sentence rather than a button beside it, so that the two read as one
 * phrase and the press target is the words that name the destination.
 *
 * @param prompt The part that is only text.
 * @param linkText The part that can be pressed.
 * @param onLinkClick Called when the link is activated by click, tap, or keyboard.
 * @param modifier Modifier applied to the text.
 * @param color Named foreground color of the surrounding sentence.
 * @param linkColor Named foreground color of the resting link.
 * @param variant Typography token for the whole sentence.
 * @param decoration Underline treatment of the link across interaction states.
 * @param textAlign Alignment of the sentence within [modifier]'s width.
 */
@Composable
public fun TextLinkPrompt(
    prompt: String,
    linkText: String,
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: TextColor = TextColor.Subtle,
    linkColor: TextColor = TextColor.Brand,
    variant: TextVariant = TextVariant.BodySmall,
    decoration: TextLinkDecoration = TextLinkDecoration.None,
    textAlign: TextAlign = TextAlign.Center,
) {
    val sentence = buildAnnotatedString {
        append(prompt)
        append(' ')
        appendTextLink(
            text = linkText,
            styles = textLinkStyles(color = linkColor, decoration = decoration),
            onClick = onLinkClick,
        )
    }

    Text(
        text = sentence,
        modifier = modifier,
        variant = variant,
        color = color,
        textAlign = textAlign,
    )
}

/**
 * Foreground color of a link of this color while hovered.
 *
 * Every entry moves, because a link that does not respond to the pointer reads as plain text. The
 * neutral entries move *towards* the reader's attention — [TextColor.Muted] brightens to subtle,
 * [TextColor.Base] softens so the change is visible without shouting — while the semantic entries
 * step to their intense fill, which is the palette's "one step further" for that meaning.
 *
 * [TextColor.OnContrast] and [TextColor.OnFill] do not move: they sit on a surface whose color is
 * not the theme's to vary, and any shift risks failing contrast there. [TextColor.Disabled] does not
 * move because a disabled link is not hoverable in the first place.
 */
private val TextColor.hoveredColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        TextColor.Base -> ArecaTheme.colors.fgSubtle
        TextColor.Subtle -> ArecaTheme.colors.fgBase
        TextColor.Muted -> ArecaTheme.colors.fgSubtle
        TextColor.Disabled -> ArecaTheme.colors.fgDisabled
        TextColor.OnContrast -> ArecaTheme.colors.fgOnContrast
        TextColor.OnFill -> ArecaTheme.colors.fgOnFill
        TextColor.Brand -> ArecaTheme.colors.accentBase
        TextColor.Danger -> ArecaTheme.colors.bgDangerIntense
        TextColor.Success -> ArecaTheme.colors.bgSuccessIntense
        TextColor.Info -> ArecaTheme.colors.bgInfoIntense
        TextColor.Warning -> ArecaTheme.colors.bgWarnIntense
    }

/**
 * Foreground color of a link of this color while pressed.
 *
 * One step beyond [hoveredColor] wherever the palette has one, so press reads as more committed than
 * hover rather than as a second, unrelated color. The entries that do not move on hover do not move
 * here either.
 */
private val TextColor.pressedColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        TextColor.Base -> ArecaTheme.colors.fgMuted
        TextColor.Subtle -> ArecaTheme.colors.fgBase
        TextColor.Muted -> ArecaTheme.colors.fgBase
        TextColor.Disabled -> ArecaTheme.colors.fgDisabled
        TextColor.OnContrast -> ArecaTheme.colors.fgOnContrast
        TextColor.OnFill -> ArecaTheme.colors.fgOnFill
        TextColor.Brand -> ArecaTheme.colors.accentStrong
        TextColor.Danger -> ArecaTheme.colors.bgDangerBase
        TextColor.Success -> ArecaTheme.colors.bgSuccessBase
        TextColor.Info -> ArecaTheme.colors.bgInfoBase
        TextColor.Warning -> ArecaTheme.colors.bgWarnBase
    }
