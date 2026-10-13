package dev.voir.areca.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

/**
 * Raw type scale: size and line height only, with no weight or role attached.
 *
 * [ArecaTypography] is the meaning assigned to this scale. Keeping the two apart means a consumer
 * can retune sizing once — or swap the font family through [ArecaFonts] — and every named style
 * follows, instead of each style carrying its own copy of the measurements.
 *
 * Build an instance with [from] rather than the constructor, and use `copy` to override one step:
 *
 * ```kotlin
 * ArecaBaseTypography.from(fonts).copy(md = TextStyle(fontSize = 15.sp, lineHeight = 20.sp))
 * ```
 *
 * Every step is a required constructor parameter on purpose, so a step added later cannot silently
 * fall back to a size that was never considered.
 */
@Immutable
public data class ArecaBaseTypography(
    /** 10sp. Smallest legible text: badge counters, dense captions. */
    public val xxs: TextStyle,
    /** 12sp. Secondary and supporting copy. */
    public val xs: TextStyle,
    /** 14sp. Compact body and control labels. */
    public val sm: TextStyle,
    /** 16sp. Default body size. */
    public val md: TextStyle,
    /** 18sp. Lead paragraphs and the smallest heading. */
    public val lg: TextStyle,
    /** 22sp. Section headings. */
    public val xl: TextStyle,
    /** 26sp. Screen headings. */
    public val xl2: TextStyle,
    /** 32sp. Page titles. */
    public val xl3: TextStyle,
    /** 40sp. Prominent page titles. */
    public val xl4: TextStyle,
    /** 50sp. Display text. */
    public val xl5: TextStyle,
    /** 64sp. Hero display text. */
    public val xl6: TextStyle,
    /** 72sp. Largest step, for splash and marketing surfaces. */
    public val xl7: TextStyle,
) {
    /** Factories for a complete type scale. */
    public companion object {
        /**
         * Derives the type scale, applying [fonts] to every step.
         *
         * Line heights are set explicitly rather than left to the font's own metrics, so a change of
         * family does not shift vertical rhythm.
         *
         * @param fonts Families the styles are rendered with.
         * @return Complete type scale.
         */
        public fun from(fonts: ArecaFonts = ArecaFonts()): ArecaBaseTypography = ArecaBaseTypography(
            xxs = TextStyle(fontSize = 10.sp, lineHeight = 12.sp, fontFamily = fonts.primary),
            xs = TextStyle(fontSize = 12.sp, lineHeight = 14.sp, fontFamily = fonts.primary),
            sm = TextStyle(fontSize = 14.sp, lineHeight = 17.sp, fontFamily = fonts.primary),
            md = TextStyle(fontSize = 16.sp, lineHeight = 20.sp, fontFamily = fonts.primary),
            lg = TextStyle(fontSize = 18.sp, lineHeight = 24.sp, fontFamily = fonts.primary),
            xl = TextStyle(fontSize = 22.sp, lineHeight = 26.sp, fontFamily = fonts.primary),
            xl2 = TextStyle(fontSize = 26.sp, lineHeight = 30.sp, fontFamily = fonts.primary),
            xl3 = TextStyle(fontSize = 32.sp, lineHeight = 38.sp, fontFamily = fonts.primary),
            xl4 = TextStyle(fontSize = 40.sp, lineHeight = 48.sp, fontFamily = fonts.primary),
            xl5 = TextStyle(fontSize = 50.sp, lineHeight = 60.sp, fontFamily = fonts.primary),
            xl6 = TextStyle(fontSize = 64.sp, lineHeight = 72.sp, fontFamily = fonts.primary),
            xl7 = TextStyle(fontSize = 72.sp, lineHeight = 90.sp, fontFamily = fonts.primary),
        )
    }
}

/** Raw type scale in scope. Read it through [ArecaTheme.baseTypography]. */
internal val LocalBaseTypography = staticCompositionLocalOf { ArecaBaseTypography.from() }
