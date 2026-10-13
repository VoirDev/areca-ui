package dev.voir.areca.foundation.button

import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaColors

/**
 * Semantic color family an action takes its colors from.
 *
 * Shared by [Button], [IconButton] and [dev.voir.areca.foundation.icon.FeaturedIcon]: they differ
 * in shape and content, never in what a danger action or a brand mark means, so the family is named
 * once and all three resolve it the same way.
 *
 * The family says what the element *means*, not how loud it is — that is the style factory's job
 * (`solid`, `soft`, `outlined`, `plain`). `Danger` is destructive whether it is a filled button or a
 * plain icon.
 */
public enum class ActionColor {
    /** Product identity. The default, and what a screen's primary action uses. */
    Brand,

    /** Destructive or irreversible: delete, revoke, leave. */
    Danger,

    /** Confirms or completes: approve, publish, mark done. */
    Success,

    /** Proceeds with a caveat. Its filled step is light, so it takes a dark foreground. */
    Warning,

    /** No sentiment: cancel, dismiss, secondary navigation. */
    Neutral,

    /** The inverted surface, for an action that must separate from a busy background. */
    NeutralDark,
}

/*
 * Color resolution shared by every action style factory.
 *
 * Each function below is one row of the table a factory would otherwise spell out per component. The
 * names say where the color goes, not which token it happens to be, so a retuned palette moves
 * Button, IconButton and FeaturedIcon together and none can drift from the others.
 *
 * They take the palette as a parameter rather than reading the theme, so the contrast test can
 * resolve every pairing against both themes without composing anything.
 */


/**
 * Fill of a solid action.
 *
 * @param colors Palette to resolve against.
 * @return The family's base fill.
 */
internal fun ActionColor.filledBackground(colors: ArecaColors): Color = when (this) {
    ActionColor.Brand -> colors.bgBrandBase
    ActionColor.Danger -> colors.bgDangerBase
    ActionColor.Success -> colors.bgSuccessBase
    ActionColor.Warning -> colors.bgWarnBase
    ActionColor.Neutral -> colors.bgNeutralBase
    ActionColor.NeutralDark -> colors.bgContrast
}

/**
 * Content reversed out of [filledBackground].
 *
 * `Warning`, `Neutral` and `NeutralDark` are the exceptions: the warning fill is light in both
 * themes, the neutral fill is too weak to reverse white out of, and the inverted fill turns light in
 * the dark theme, so its content is the inverted surface's own.
 *
 * @param colors Palette to resolve against.
 * @return Label and icon color on the solid fill. Clears 4.5:1 on it in both themes.
 */
internal fun ActionColor.onFilled(colors: ArecaColors): Color = when (this) {
    ActionColor.Neutral -> colors.fgNeutral
    ActionColor.Warning -> colors.fgOnWarn
    ActionColor.NeutralDark -> colors.fgOnContrast
    ActionColor.Brand, ActionColor.Danger, ActionColor.Success -> colors.fgOnFill
}

/**
 * Tinted fill of a soft action.
 *
 * @param colors Palette to resolve against.
 * @return The family's muted fill.
 */
internal fun ActionColor.mutedBackground(colors: ArecaColors): Color = when (this) {
    ActionColor.Brand -> colors.bgBrandMuted
    ActionColor.Danger -> colors.bgDangerMuted
    ActionColor.Success -> colors.bgSuccessMuted
    ActionColor.Warning -> colors.bgWarnMuted
    ActionColor.Neutral -> colors.bgNeutralMuted
    ActionColor.NeutralDark -> colors.bgContrastMuted
}

/**
 * Primary content on [mutedBackground].
 *
 * A label is type, so each family takes its text foreground — the step that clears 4.5:1 on the
 * very ground it is sitting on. `Brand` included: the design pairs a soft brand label with the
 * accent, which reaches only 4.0–4.4:1 on the brand tint, so the label takes
 * [ArecaColors.fgBrandText], the next brand step past the accent — darker in the light theme,
 * lighter in the dark one. That is a deliberate departure from the design for this one pairing,
 * made so the label passes AA in both themes.
 *
 * @param colors Palette to resolve against.
 * @return Label color on the muted fill. Clears 4.5:1 on it in both themes.
 */
internal fun ActionColor.onMuted(colors: ArecaColors): Color = when (this) {
    ActionColor.Brand -> colors.fgBrandText
    ActionColor.Danger -> colors.fgDangerText
    ActionColor.Success -> colors.fgSuccessText
    ActionColor.Warning -> colors.fgWarnText
    ActionColor.Neutral -> colors.fgNeutral
    ActionColor.NeutralDark -> colors.fgOnContrast
}

/**
 * Secondary content on [mutedBackground] — a leading icon beside a label, rather than the label.
 *
 * Matches [onMuted] except for `Neutral`, which steps back so an icon does not compete with the text
 * it accompanies.
 *
 * @param colors Palette to resolve against.
 * @return Icon color beside a label on the muted fill.
 */
internal fun ActionColor.onMutedSecondary(colors: ArecaColors): Color = when (this) {
    ActionColor.Neutral -> colors.fgMuted
    ActionColor.Brand,
    ActionColor.Danger,
    ActionColor.Success,
    ActionColor.Warning,
    ActionColor.NeutralDark -> onMuted(colors)
}

/**
 * Content on an unfilled surface: an outlined or plain action. Type, so the text foregrounds.
 *
 * @param colors Palette to resolve against.
 * @return Label color on the page.
 */
internal fun ActionColor.onSurface(colors: ArecaColors): Color = when (this) {
    ActionColor.Brand -> colors.accentBase
    ActionColor.Danger -> colors.fgDangerText
    ActionColor.Success -> colors.fgSuccessText
    ActionColor.Warning -> colors.fgWarnText
    ActionColor.Neutral -> colors.fgMuted
    ActionColor.NeutralDark -> colors.fgBase
}

/**
 * Border of an outlined action at rest.
 *
 * @param colors Palette to resolve against.
 * @return The family's border color.
 */
internal fun ActionColor.outlineBorder(colors: ArecaColors): Color = when (this) {
    ActionColor.Brand -> colors.accentBase
    ActionColor.Danger -> colors.borderDanger
    ActionColor.Success -> colors.borderSuccess
    ActionColor.Warning -> colors.borderWarn
    ActionColor.Neutral -> colors.borderIntense
    ActionColor.NeutralDark -> colors.bgContrastMuted
}

/**
 * Stroke an outlined action draws on hover and press.
 *
 * Matches [outlineBorder] except for `Neutral`, whose resting border is too faint to read as a
 * reaction when it is merely redrawn.
 *
 * @param colors Palette to resolve against.
 * @return Stroke color for the outline indication.
 */
internal fun ActionColor.outlineStroke(colors: ArecaColors): Color = when (this) {
    ActionColor.Neutral -> colors.bgNeutralIntense
    ActionColor.Brand,
    ActionColor.Danger,
    ActionColor.Success,
    ActionColor.Warning,
    ActionColor.NeutralDark -> outlineBorder(colors)
}

/**
 * Overlay a solid action tints itself with on hover and press.
 *
 * One step further from the background than [filledBackground], so pressing reads as more of the
 * same color rather than as a different one.
 *
 * @param colors Palette to resolve against.
 * @return Overlay color for the solid indication.
 */
internal fun ActionColor.filledOverlay(colors: ArecaColors): Color = when (this) {
    ActionColor.Brand -> colors.bgBrandIntense
    ActionColor.Danger -> colors.bgDangerIntense
    ActionColor.Success -> colors.bgSuccessIntense
    ActionColor.Warning -> colors.bgWarnIntense
    ActionColor.Neutral -> colors.bgNeutralIntense
    ActionColor.NeutralDark -> colors.fgOnContrast
}

/**
 * Overlay a soft action tints itself with on hover and press.
 *
 * The family's base step, which is a large jump from its muted fill — the overlay is drawn at low
 * alpha, so it needs the contrast to register at all.
 *
 * @param colors Palette to resolve against.
 * @return Overlay color for the soft indication.
 */
internal fun ActionColor.mutedOverlay(colors: ArecaColors): Color = when (this) {
    ActionColor.Brand -> colors.bgBrandBase
    ActionColor.Danger -> colors.bgDangerBase
    ActionColor.Success -> colors.bgSuccessBase
    ActionColor.Warning -> colors.bgWarnBase
    ActionColor.Neutral, ActionColor.NeutralDark -> colors.bgNeutralIntense
}

/**
 * Keyboard focus ring for an action of this family.
 *
 * The family's base step in every case, so the ring reads as "this control" regardless of how loud
 * the control itself is.
 *
 * @param colors Palette to resolve against.
 * @return Focus ring color.
 */
internal fun ActionColor.focusRing(colors: ArecaColors): Color = filledBackground(colors)
