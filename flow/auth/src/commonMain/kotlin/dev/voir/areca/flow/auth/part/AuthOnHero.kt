package dev.voir.areca.flow.auth.part

import androidx.compose.runtime.Composable
import dev.voir.areca.foundation.feedback.RequirementListStyle
import dev.voir.areca.foundation.field.FieldDefaults
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.layout.DividerStyle
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.theme.ArecaTheme

/**
 * The foregrounds an authentication screen uses, resolved against whatever it is drawn on.
 *
 * [AuthLayout.FullBleed][dev.voir.areca.flow.auth.AuthLayout.FullBleed] lays the whole screen over
 * artwork, and the theme's ordinary foregrounds are chosen to sit on a page background rather than
 * on a photograph. A subtle grey label, a brand-coloured link and a muted rule all survive on white
 * and all disappear on a saturated image.
 *
 * Every one of them is resolved here rather than at each call site, because the failure mode is
 * silent: a screen that flips four of its five foregrounds looks fine until somebody tries to read
 * the fifth. Anything an authentication screen draws directly on the background belongs in this
 * file.
 *
 * Controls with a surface of their own — the fields' own boxes, the buttons — are not included and
 * do not need to be. They bring their own ground with them.
 */
internal object AuthOnHero {

    /**
     * Colour for a standalone link, such as the password recovery link.
     *
     * @param onHero Whether the link is drawn over artwork.
     * @return Named foreground to pass to a link.
     */
    fun linkColor(onHero: Boolean): TextColor =
        if (onHero) TextColor.OnContrast else TextColor.Brand

    /**
     * Colour for the plain part of a sentence that contains a link, such as the footer prompt.
     *
     * @param onHero Whether the sentence is drawn over artwork.
     * @return Named foreground to pass as the surrounding text colour.
     */
    fun promptColor(onHero: Boolean): TextColor =
        if (onHero) TextColor.OnContrast else TextColor.Subtle

    /**
     * Field colours with the label and message legible over artwork.
     *
     * Only the neutral states are flipped. The error, warning and success states keep their
     * semantic colours, which are saturated enough to read on a dark ground and which would stop
     * meaning anything if they were replaced with white.
     *
     * @param onHero Whether the field is drawn over artwork.
     * @return Style to pass to a field group.
     */
    @Composable
    fun fieldStyle(onHero: Boolean): FieldStyle {
        val style = FieldDefaults.style
        if (!onHero) return style

        val onContrast = ArecaTheme.colors.fgOnContrast

        return style.copy(
            label = style.label.copy(
                content = onContrast,
                contentReadOnly = onContrast,
                contentFocused = onContrast,
            ),
            message = style.message.copy(content = onContrast),
        )
    }

    /**
     * Divider colours for the labelled rule above the provider buttons.
     *
     * @param onHero Whether the rule is drawn over artwork.
     * @return Style to pass to a divider.
     */
    @Composable
    fun dividerStyle(onHero: Boolean): DividerStyle {
        if (!onHero) return DividerStyle.Default

        val onContrast = ArecaTheme.colors.fgOnContrast

        return DividerStyle(
            // The rule is a separator, not content, so it stays quieter than the label it holds.
            line = onContrast.copy(alpha = RuleAlpha),
            text = onContrast,
        )
    }

    /**
     * Requirement colors with the pending and met rules legible over artwork.
     *
     * An unmet rule keeps its danger color for the reason the fields keep theirs: it is saturated
     * enough to read on a dark ground, and it would stop meaning anything in white.
     *
     * @param onHero Whether the list is drawn over artwork.
     * @return Style to pass to a requirement list.
     */
    @Composable
    fun requirementStyle(onHero: Boolean): RequirementListStyle {
        val style = RequirementListStyle.Default
        if (!onHero) return style

        val onContrast = ArecaTheme.colors.fgOnContrast

        return style.copy(
            pendingMark = onContrast,
            pendingText = onContrast,
            metText = onContrast,
        )
    }

    /**
     * Colour for a quiet line of status drawn straight onto the background, such as a progress
     * line or a resend hint.
     *
     * @param onHero Whether the line is drawn over artwork.
     * @return Named foreground to pass to the text.
     */
    fun statusColor(onHero: Boolean): TextColor =
        if (onHero) TextColor.OnContrast else TextColor.Subtle

    /** How far the rule is faded against its label when both are drawn over artwork. */
    private const val RuleAlpha = 0.4f
}
