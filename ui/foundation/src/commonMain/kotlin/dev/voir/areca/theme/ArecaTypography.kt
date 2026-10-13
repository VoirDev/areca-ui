package dev.voir.areca.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Named text styles, each a role rather than a size.
 *
 * [ArecaBaseTypography] supplies the measurements; this type adds weight and letter spacing and
 * gives the result a name a screen can be written against. Call sites should pick by role —
 * [bodyMedium] for paragraph copy, [labelMedium] for a control's label — so retuning a role changes
 * every screen at once.
 *
 * The four families are: headings for section structure, body for reading, labels for controls, and
 * [display] plus [caption] for the two roles that fit neither.
 *
 * Build an instance with [from] rather than the constructor, and use `copy` to override one style:
 *
 * ```kotlin
 * ArecaTypography.from(baseTypography).copy(display = brandDisplayStyle)
 * ```
 *
 * Every style is a required constructor parameter on purpose, so a role added later cannot silently
 * fall back to a style that was never considered.
 */
@Immutable
public data class ArecaTypography(
    /** Smallest heading. Subsection titles inside a card or list group. */
    public val headingExtraSmall: TextStyle,
    /** Section heading within a screen. */
    public val headingSmall: TextStyle,
    /** Screen heading. */
    public val headingMedium: TextStyle,
    /** Page title. */
    public val headingLarge: TextStyle,
    /** Largest heading, for a screen whose title is its main content. */
    public val headingExtraLarge: TextStyle,
    /** Supporting copy shown alongside body text. */
    public val bodyExtraSmall: TextStyle,
    /** Compact body copy, as used inside dense components. */
    public val bodySmall: TextStyle,
    /** Default body copy. */
    public val bodyMedium: TextStyle,
    /** Lead paragraph, or body copy on a spacious screen. */
    public val bodyLarge: TextStyle,
    /** Emphasised body copy that is not a heading, such as a summary line. */
    public val bodyExtraLarge: TextStyle,
    /** Standalone display text, larger than any heading. Use once per screen at most. */
    public val display: TextStyle,
    /** Short explanatory text under a field or image. Letter-spaced for legibility at small sizes. */
    public val caption: TextStyle,
    /** Label on a dense control, such as a badge or chip. */
    public val labelSmall: TextStyle,
    /** Default control label: buttons, fields, list rows. */
    public val labelMedium: TextStyle,
    /** Label on a large control, or a list row's primary line. */
    public val labelLarge: TextStyle,
) {
    /** Factories for a complete set of named styles. */
    public companion object {
        /**
         * Derives the named styles from a raw type scale.
         *
         * Headings take `SemiBold` up to [headingMedium] and `Bold` above it: at large sizes
         * `SemiBold` stops reading as emphasis, and at small sizes `Bold` reads as shouting. Labels
         * take `Medium` so a control's text separates from the body copy around it without
         * competing with a heading.
         *
         * @param baseTypography Measurements the styles are built on.
         * @return Complete set of named styles.
         */
        public fun from(
            baseTypography: ArecaBaseTypography = ArecaBaseTypography.from(),
        ): ArecaTypography = ArecaTypography(
            headingExtraSmall = baseTypography.lg.merge(fontWeight = FontWeight.SemiBold),
            headingSmall = baseTypography.xl.merge(fontWeight = FontWeight.SemiBold),
            headingMedium = baseTypography.xl2.merge(fontWeight = FontWeight.SemiBold),
            headingLarge = baseTypography.xl3.merge(fontWeight = FontWeight.Bold),
            headingExtraLarge = baseTypography.xl4.merge(fontWeight = FontWeight.Bold),

            bodyExtraSmall = baseTypography.xs,
            bodySmall = baseTypography.sm,
            bodyMedium = baseTypography.md,
            bodyLarge = baseTypography.lg,
            bodyExtraLarge = baseTypography.xl.merge(fontWeight = FontWeight.Medium),

            display = baseTypography.xl5.merge(fontWeight = FontWeight.Bold),

            caption = baseTypography.sm.merge(
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.15.sp,
            ),

            labelSmall = baseTypography.xs.merge(fontWeight = FontWeight.Medium),
            labelMedium = baseTypography.sm.merge(fontWeight = FontWeight.Medium),
            labelLarge = baseTypography.md.merge(fontWeight = FontWeight.Medium),
        )
    }
}

/** Named text styles in scope. Read them through [ArecaTheme.typography]. */
internal val LocalTypography = staticCompositionLocalOf { ArecaTypography.from() }
