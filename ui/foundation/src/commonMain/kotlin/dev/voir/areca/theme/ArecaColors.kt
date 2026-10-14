package dev.voir.areca.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Semantic color tokens for a single theme direction.
 *
 * [ArecaBaseColors] is the raw palette; this type is the meaning assigned to it. A light and a dark
 * theme share one palette and differ only in how the ramps are mapped, so a consumer who swaps in a brand
 * palette gets both directions from that single change.
 *
 * Build an instance with [light] or [dark] rather than the constructor, and use `copy` to override
 * individual tokens:
 *
 * ```kotlin
 * ArecaColors.dark(brandPalette).copy(accentBase = brandPalette.brand40)
 * ```
 *
 * Every token is a required constructor parameter on purpose. A new token added later must be given
 * a value in both [light] and [dark], so it cannot silently fall back to a light value in dark mode.
 */
@Immutable
public data class ArecaColors(
    // Background colors
    /** The page itself. Nothing sits behind it. */
    public val bgCanvas: Color,
    /** A region set off from the canvas without a border, such as a settings card or a grouped list. */
    public val bgSubtle: Color,
    /** A region set further back than [bgSubtle], such as an inset well or a disabled track. */
    public val bgMuted: Color,
    /** A raised container on the canvas: cards, sheets, popovers. */
    public val bgSurface: Color,
    /**
     * The inverted surface — dark in the light theme, light in the dark one.
     *
     * Pair with [fgOnContrast].
     */
    public val bgContrast: Color,
    /** A weaker inverted surface, for content layered on [bgContrast]. */
    public val bgContrastMuted: Color,
    /** Scrim darkening whatever is behind a modal surface. */
    public val bgOverlayDark: Color,
    /** Scrim lightening whatever is behind it, for use over dark imagery. */
    public val bgOverlayLight: Color,
    /** Fill of a control the user cannot act on. */
    public val bgDisabled: Color,

    /** Tinted brand background for a chip, banner, or selected row. */
    public val bgBrandMuted: Color,
    /** Solid brand fill, as on a primary button. Pair with [fgOnFill]. */
    public val bgBrandBase: Color,
    /** Brand fill under press or hover, one step further from the background. */
    public val bgBrandIntense: Color,

    /** Tinted danger background for an inline error region. */
    public val bgDangerMuted: Color,
    /** Solid danger fill, as on a destructive button. Pair with [fgOnFill]. */
    public val bgDangerBase: Color,
    /** Danger fill under press or hover. */
    public val bgDangerIntense: Color,

    /** Tinted success background for a confirmation region. */
    public val bgSuccessMuted: Color,
    /** Solid success fill. Pair with [fgOnFill]. */
    public val bgSuccessBase: Color,
    /** Success fill under press or hover. */
    public val bgSuccessIntense: Color,

    /** Tinted warning background for a caution region. */
    public val bgWarnMuted: Color,
    /**
     * Solid warning fill.
     *
     * Light in both themes, so pair with [fgOnWarn] rather than [fgOnFill].
     */
    public val bgWarnBase: Color,
    /** Warning fill under press or hover. */
    public val bgWarnIntense: Color,

    /** Tinted informational background. */
    public val bgInfoMuted: Color,
    /** Solid informational fill. Pair with [fgOnFill]. */
    public val bgInfoBase: Color,
    /** Informational fill under press or hover. */
    public val bgInfoIntense: Color,

    /** Tinted neutral background, for a status that carries no sentiment. */
    public val bgNeutralMuted: Color,
    /** Solid neutral fill. */
    public val bgNeutralBase: Color,
    /** Neutral fill under press or hover. */
    public val bgNeutralIntense: Color,

    // Foreground colors
    /** Primary text and icons. Clears 7:1 on every resting background. */
    public val fgBase: Color,
    /** Secondary text: supporting lines, inactive labels. Clears 4.5:1 on canvas and surface. */
    public val fgSubtle: Color,
    /**
     * Tertiary text: placeholders, timestamps, decorative icons.
     *
     * Clears only 3:1, so it must never be the sole carrier of meaning.
     */
    public val fgMuted: Color,

    /** Content on [bgContrast]. */
    public val fgOnContrast: Color,
    /** Content on any solid semantic fill except [bgWarnBase]. White in both themes. */
    public val fgOnFill: Color,
    /** Content on [bgWarnBase], which is light in both themes and so needs a dark foreground. */
    public val fgOnWarn: Color,

    /** Text and icons of a control the user cannot act on. */
    public val fgDisabled: Color,

    /**
     * Brand-coloured icons, graphics, and rules on a neutral or [bgBrandMuted] background.
     *
     * Each semantic family carries two foregrounds because one step cannot serve both roles. This
     * one keeps the colour's identity: it sits a step nearer the base fill, so a 20dp icon still
     * reads as the brand rather than as a dark smudge of it. It clears 3:1 on every resting
     * background — the floor for a graphic that carries meaning — but not the 4.5:1 body text
     * needs. Set type in [fgBrandText] instead.
     */
    public val fgBrand: Color,
    /**
     * Brand-coloured text.
     *
     * Clears 4.5:1 on the canvas, on a surface, and on [bgBrandMuted] — the chip pairing is what
     * fixes the step, because it is stricter than the canvas.
     */
    public val fgBrandText: Color,
    /** Danger-coloured icons and graphics. Clears 3:1; use [fgDangerText] for type. */
    public val fgDanger: Color,
    /** Danger-coloured text, as in a field error message. Clears 4.5:1 on its own muted ground. */
    public val fgDangerText: Color,
    /** Success-coloured icons and graphics. Clears 3:1; use [fgSuccessText] for type. */
    public val fgSuccess: Color,
    /** Success-coloured text. Clears 4.5:1 on its own muted ground. */
    public val fgSuccessText: Color,
    /** Informational icons and graphics. Clears 3:1; use [fgInfoText] for type. */
    public val fgInfo: Color,
    /** Informational text. Clears 4.5:1 on its own muted ground. */
    public val fgInfoText: Color,
    /** Warning-coloured icons and graphics. Clears 3:1; use [fgWarnText] for type. */
    public val fgWarn: Color,
    /** Warning-coloured text. Clears 4.5:1 on its own muted ground. */
    public val fgWarnText: Color,
    /**
     * Neutral status text and icons.
     *
     * The one semantic family with a single foreground. Its vivid candidate would land on the same
     * step as [fgMuted] in both themes, so the split would name a distinction that does not exist;
     * and unlike the chromatic families this token also has to carry content on [bgNeutralBase],
     * where a lighter step drops below AA.
     */
    public val fgNeutral: Color,

    // Border colors
    /** Default border, as on a card or input at rest. */
    public val borderBase: Color,
    /** Border that must separate more clearly than [borderBase], such as a hovered control. */
    public val borderStrong: Color,
    /** Strongest neutral border, for a focused or pressed control. */
    public val borderIntense: Color,
    /** Barely visible divider between rows of the same group. */
    public val borderSubtle: Color,
    /**
     * Divider drawn on a [bgSubtle] region, such as the hairline between the rows of a settings
     * card: one step past that fill in each theme, where [borderBase] sits level with it.
     */
    public val borderOnSubtle: Color,

    /** Border of a control the user cannot act on. */
    public val borderDisabled: Color,

    /** Border of a control in an error state. */
    public val borderDanger: Color,
    /** Border of a control in a success state. */
    public val borderSuccess: Color,
    /** Border of a control carrying information. */
    public val borderInfo: Color,
    /** Border of a control in a warning state. */
    public val borderWarn: Color,

    // Accent colors
    /** Default accent for selection and focus: a checked control, a focus ring, an active tab. */
    public val accentBase: Color,
    /** Accent under press or hover. */
    public val accentStrong: Color,
    /** Strongest accent, for a held or dragged state. */
    public val accentIntense: Color,
    /** Weak accent, for a selection indicator that should not compete with content. */
    public val accentSubtle: Color,
    /** Accent tint behind selected content. */
    public val accentMuted: Color,
) {
    /** Factories that map a palette to one theme direction. */
    public companion object {
        /**
         * Maps [baseColors] to the light theme: light surfaces, dark foregrounds, and semantic
         * ramps read in their natural direction where a higher step is a stronger color.
         *
         * Each semantic family splits its foreground across two steps. The text token sits at step
         * 70: body text has to clear 4.5:1 on the canvas *and* on its own muted chip, and step 50
         * cleared neither while step 60 cleared only the canvas. The icon token sits at step 60,
         * one nearer the base fill, where the colour still reads as itself — 4.5:1 on the canvas
         * and 3.8:1 on a muted ground, past the 3:1 a meaningful graphic owes.
         *
         * @param baseColors Palette the tokens are drawn from.
         * @return Complete token set for the light theme.
         */
        public fun light(baseColors: ArecaBaseColors = ArecaBaseColors()): ArecaColors = ArecaColors(
            bgCanvas = baseColors.baseWhite,
            bgSubtle = baseColors.neutral15,
            // A full step past the subtle region, so a track or a well still reads on a settings
            // card: neutral20 sits level with neutral15.
            bgMuted = baseColors.neutral30,
            bgSurface = baseColors.baseWhite,
            bgContrast = baseColors.neutral100,
            bgContrastMuted = baseColors.neutral60,
            bgOverlayDark = baseColors.neutral100.copy(alpha = 0.4f),
            bgOverlayLight = baseColors.baseWhite.copy(alpha = 0.4f),
            bgDisabled = baseColors.neutral20,

            bgBrandMuted = baseColors.brand20,
            bgBrandBase = baseColors.brand60,
            bgBrandIntense = baseColors.brand80,

            bgDangerMuted = baseColors.red20,
            bgDangerBase = baseColors.red60,
            bgDangerIntense = baseColors.red80,

            bgSuccessMuted = baseColors.green20,
            bgSuccessBase = baseColors.green60,
            bgSuccessIntense = baseColors.green80,

            bgWarnMuted = baseColors.yellow10,
            bgWarnBase = baseColors.yellow30,
            bgWarnIntense = baseColors.yellow50,

            bgInfoMuted = baseColors.blue20,
            bgInfoBase = baseColors.blue60,
            bgInfoIntense = baseColors.blue80,

            bgNeutralMuted = baseColors.neutral20,
            bgNeutralBase = baseColors.neutral30,
            bgNeutralIntense = baseColors.neutral40,

            fgBase = baseColors.neutral100,
            fgSubtle = baseColors.neutral80,
            fgMuted = baseColors.neutral60,

            fgOnContrast = baseColors.baseWhite,
            fgOnFill = baseColors.baseWhite,
            fgOnWarn = baseColors.neutral100,

            fgDisabled = baseColors.neutral50,

            fgBrand = baseColors.brand60,
            fgBrandText = baseColors.brand70,
            fgDanger = baseColors.red60,
            fgDangerText = baseColors.red70,
            fgSuccess = baseColors.green60,
            fgSuccessText = baseColors.green70,
            fgInfo = baseColors.blue60,
            fgInfoText = baseColors.blue70,
            fgWarn = baseColors.yellow60,
            fgWarnText = baseColors.yellow70,
            fgNeutral = baseColors.neutral80,

            borderBase = baseColors.neutral20,
            borderStrong = baseColors.neutral30,
            borderIntense = baseColors.neutral40,
            borderSubtle = baseColors.neutral10,
            borderOnSubtle = baseColors.neutral30,

            borderDisabled = baseColors.neutral30,

            borderDanger = baseColors.red60,
            borderSuccess = baseColors.green50,
            borderInfo = baseColors.blue50,
            borderWarn = baseColors.yellow50,

            accentBase = baseColors.brand60,
            accentStrong = baseColors.brand80,
            accentIntense = baseColors.brand100,
            accentSubtle = baseColors.brand40,
            accentMuted = baseColors.brand20,
        )

        /**
         * Maps [baseColors] to the dark theme.
         *
         * The neutral ramp is read from the dark end: `neutral100` becomes the canvas and
         * `neutral10` the primary foreground. Semantic ramps keep their base step, because that step
         * is the color's identity, while the muted step moves to a dark tint and the intense step
         * moves lighter so emphasis still reads as "further from the background".
         *
         * The semantic foreground split runs the other way here than it does in light. Text takes
         * step 40, the pale end, because on a near-black canvas legibility comes from lightness.
         * Icons take step 50 — *darker* than the text step, but more saturated, which is what makes
         * a small graphic read as the colour instead of as a tinted grey. "Vivid" and "lighter"
         * point in opposite directions on this end of the ramp; the tokens follow vivid.
         *
         * [bgContrast] is the inverted surface, so in dark it is light and [fgOnContrast] is dark.
         * [fgOnFill] is the separate token for content on a solid semantic fill — a brand button, a
         * danger badge, a checkbox mark — and stays white in both themes, because those fills keep
         * their base step in both.
         *
         * @param baseColors Palette the tokens are drawn from.
         * @return Complete token set for the dark theme.
         */
        public fun dark(baseColors: ArecaBaseColors = ArecaBaseColors()): ArecaColors = ArecaColors(
            bgCanvas = baseColors.neutral100,
            bgSubtle = baseColors.neutral90,
            bgMuted = baseColors.neutral80,
            bgSurface = baseColors.neutral90,
            bgContrast = baseColors.neutral10,
            bgContrastMuted = baseColors.neutral40,
            bgOverlayDark = baseColors.neutral100.copy(alpha = 0.6f),
            bgOverlayLight = baseColors.baseWhite.copy(alpha = 0.2f),
            bgDisabled = baseColors.neutral80,

            bgBrandMuted = baseColors.brand90,
            bgBrandBase = baseColors.brand60,
            bgBrandIntense = baseColors.brand50,

            bgDangerMuted = baseColors.red90,
            bgDangerBase = baseColors.red60,
            bgDangerIntense = baseColors.red50,

            bgSuccessMuted = baseColors.green90,
            bgSuccessBase = baseColors.green60,
            bgSuccessIntense = baseColors.green50,

            bgWarnMuted = baseColors.yellow90,
            bgWarnBase = baseColors.yellow40,
            bgWarnIntense = baseColors.yellow30,

            bgInfoMuted = baseColors.blue90,
            bgInfoBase = baseColors.blue60,
            bgInfoIntense = baseColors.blue50,

            bgNeutralMuted = baseColors.neutral80,
            bgNeutralBase = baseColors.neutral70,
            bgNeutralIntense = baseColors.neutral60,

            fgBase = baseColors.neutral10,
            fgSubtle = baseColors.neutral30,
            fgMuted = baseColors.neutral50,

            fgOnContrast = baseColors.neutral100,
            fgOnFill = baseColors.baseWhite,
            fgOnWarn = baseColors.neutral100,

            fgDisabled = baseColors.neutral60,

            fgBrand = baseColors.brand50,
            fgBrandText = baseColors.brand40,
            fgDanger = baseColors.red50,
            fgDangerText = baseColors.red40,
            fgSuccess = baseColors.green50,
            fgSuccessText = baseColors.green40,
            fgInfo = baseColors.blue50,
            fgInfoText = baseColors.blue40,
            fgWarn = baseColors.yellow50,
            fgWarnText = baseColors.yellow40,
            fgNeutral = baseColors.neutral30,

            borderBase = baseColors.neutral80,
            borderStrong = baseColors.neutral70,
            borderIntense = baseColors.neutral60,
            borderSubtle = baseColors.neutral90,
            borderOnSubtle = baseColors.neutral80,

            borderDisabled = baseColors.neutral70,

            borderDanger = baseColors.red50,
            borderSuccess = baseColors.green40,
            borderInfo = baseColors.blue40,
            borderWarn = baseColors.yellow40,

            accentBase = baseColors.brand50,
            accentStrong = baseColors.brand40,
            accentIntense = baseColors.brand30,
            accentSubtle = baseColors.brand70,
            accentMuted = baseColors.brand90,
        )
    }
}

/** Semantic colors in scope. Read them through [ArecaTheme.colors]. */
internal val LocalColors = staticCompositionLocalOf { ArecaColors.light() }
