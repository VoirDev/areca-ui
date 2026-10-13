package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import dev.voir.areca.flow.auth.AuthLegalLinks
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.text.appendTextLink
import dev.voir.areca.foundation.text.textLinkStyles

/**
 * The "by continuing you agree to…" line, with the two documents pressable inside the sentence.
 *
 * The links are link annotations inside one string rather than buttons beside it, so the sentence
 * wraps as a sentence and the press targets are the words that name the documents.
 *
 * The sentence is one template rather than fragments around the links, so a translation decides
 * the order of the clause and of the two documents. `%1$s` marks where the terms link goes and
 * `%2$s` where the privacy link goes — the positional placeholders Android string resources and
 * Compose resources already use. Pass the template unformatted, as `stringResource(Res.string.…)`
 * with no arguments returns it.
 *
 * @param links Documents to link to. Each is passed back to [onLinkClick] exactly as given.
 * @param onLinkClick Called with the address of whichever document was pressed. The module never
 *   opens it — a browser, an in-app reader or a screen of the application's own are all valid, and
 *   only the application knows which.
 * @param modifier Modifier applied to the text.
 * @param textAlign Alignment of the sentence within the width it is given.
 * @param sentence Whole sentence, with `%1$s` where [termsLabel] goes and `%2$s` where
 *   [privacyLabel] goes.
 * @param termsLabel Label of the terms link.
 * @param privacyLabel Label of the privacy link.
 * @throws IllegalArgumentException When [sentence] does not hold `%1$s` and `%2$s` exactly once
 *   each.
 */
@Composable
public fun LegalConsent(
    links: AuthLegalLinks,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    sentence: String = "By continuing, you agree to our %1\$s and %2\$s.",
    termsLabel: String = "Terms of Service",
    privacyLabel: String = "Privacy Policy",
) {
    val parts = remember(sentence) { legalSentenceParts(sentence) }

    LegalConsentText(
        parts = parts,
        links = links,
        onLinkClick = onLinkClick,
        modifier = modifier,
        textAlign = textAlign,
        termsLabel = termsLabel,
        privacyLabel = privacyLabel,
    )
}

/**
 * Draws the legal sentence from its parts, with the links annotated in place.
 *
 * @param parts Sentence in order, words and links.
 * @param links Documents to link to.
 * @param onLinkClick Called with the address of whichever document was pressed.
 * @param modifier Modifier applied to the text.
 * @param textAlign Alignment of the sentence within the width it is given.
 * @param termsLabel Label of the terms link.
 * @param privacyLabel Label of the privacy link.
 */
@Composable
private fun LegalConsentText(
    parts: List<LegalSentencePart>,
    links: AuthLegalLinks,
    onLinkClick: (String) -> Unit,
    modifier: Modifier,
    textAlign: TextAlign,
    termsLabel: String,
    privacyLabel: String,
) {
    val onHero = LocalAuthOnHero.current

    val linkColor = if (onHero) TextColor.OnContrast else TextColor.Brand
    val styles = textLinkStyles(color = linkColor)

    val sentence = buildAnnotatedString {
        parts.forEach { part ->
            when (part) {
                is LegalSentencePart.Words -> append(part.text)
                LegalSentencePart.Terms ->
                    appendTextLink(text = termsLabel, styles = styles) { onLinkClick(links.terms) }
                LegalSentencePart.Privacy ->
                    appendTextLink(text = privacyLabel, styles = styles) { onLinkClick(links.privacy) }
            }
        }
    }

    Text(
        text = sentence,
        variant = TextVariant.BodyExtraSmall,
        color = if (onHero) TextColor.OnContrast else TextColor.Muted,
        textAlign = textAlign,
        modifier = modifier.fillMaxWidth(),
    )
}

/** One piece of the legal sentence, in reading order. */
internal sealed interface LegalSentencePart {
    /**
     * Plain words between, before or after the links.
     *
     * @property text Words as written, spaces and punctuation included.
     */
    data class Words(val text: String) : LegalSentencePart

    /** Where the terms link goes. */
    data object Terms : LegalSentencePart

    /** Where the privacy link goes. */
    data object Privacy : LegalSentencePart
}

/**
 * Splits a legal-sentence template into words and the two link positions.
 *
 * Any text other than the two placeholders is kept verbatim, so a sentence whose words contain a
 * percent sign needs no escaping. Empty stretches of words are left out.
 *
 * @param template Sentence with `%1$s` for the terms link and `%2$s` for the privacy link.
 * @return Parts in reading order.
 * @throws IllegalArgumentException When either placeholder is missing or appears more than once —
 *   a legal line that drops a document is not one to draw.
 */
internal fun legalSentenceParts(template: String): List<LegalSentencePart> {
    val parts = mutableListOf<LegalSentencePart>()
    var start = 0
    for (match in LegalPlaceholder.findAll(template)) {
        if (match.range.first > start) {
            parts += LegalSentencePart.Words(template.substring(start, match.range.first))
        }
        parts += if (match.groupValues[1] == "1") LegalSentencePart.Terms else LegalSentencePart.Privacy
        start = match.range.last + 1
    }
    if (start < template.length) parts += LegalSentencePart.Words(template.substring(start))

    val termsCount = parts.count { it == LegalSentencePart.Terms }
    val privacyCount = parts.count { it == LegalSentencePart.Privacy }
    require(termsCount == 1 && privacyCount == 1) {
        "A legal sentence needs %1\$s (terms) and %2\$s (privacy) exactly once each: \"$template\"."
    }
    return parts
}

/** The two positional placeholders a legal sentence holds, capturing the position. */
private val LegalPlaceholder = Regex("%([12])\\\$s")
