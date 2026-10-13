package dev.voir.areca.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.FontFamily

/**
 * Font families the type scale is built from.
 *
 * Both default to [FontFamily.Default] so an unconfigured app still renders; supplying a brand face
 * here re-types the whole system, because [ArecaBaseTypography.from] reads the families from this
 * one place.
 */
@Immutable
public data class ArecaFonts(
    /** Family for body copy, labels, and headings — everything unless stated otherwise. */
    public val primary: FontFamily = FontFamily.Default,
    /**
     * Family for text that should read as a different voice, such as a display heading or a
     * monospaced value.
     *
     * Nothing in the foundation uses it by default; it exists so a consumer can opt individual
     * styles into a second face without inventing a token.
     */
    public val secondary: FontFamily = FontFamily.Default,
)

/** Font families in scope. Read them through [ArecaTheme.fonts]. */
internal val LocalFonts = staticCompositionLocalOf { ArecaFonts() }
