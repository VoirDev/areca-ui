package dev.voir.areca.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Entry point for every Areca design token.
 *
 * This is the only supported way to read tokens. Each accessor resolves the value
 * [ArecaThemeProvider] put in scope, so a component reads the theme it is composed under rather than
 * a global.
 *
 * The accessors come in two kinds. [baseColors], [baseTypography], and [fonts] are the raw material
 * a theme is assembled from; [colors], [typography], [radius], [shapes], [spacing], [elevation], and
 * [motion] are the meaning assigned to it. Component code should read the semantic ones — they are
 * what a consumer retunes — and drop to the raw ones only when no semantic token expresses the
 * intent.
 *
 * Every accessor has a usable value without a provider, so previews and tests compose without
 * wrapping. That default is the light theme; only [ArecaThemeProvider] can make it dark.
 */
public object ArecaTheme {

    /**
     * Whether the tokens in scope were resolved for the dark theme.
     *
     * Read it only for a decision the tokens genuinely cannot carry, such as choosing between a
     * light and a dark scrim over arbitrary imagery. Branching on it to pick a colour is a sign the
     * colour should be a token instead.
     */
    public val isDark: Boolean
        @Composable @ReadOnlyComposable get() = LocalIsDarkTheme.current

    /** Raw color palette the semantic [colors] are derived from. */
    public val baseColors: ArecaBaseColors
        @Composable @ReadOnlyComposable get() = LocalBaseColors.current

    /** Semantic color tokens. */
    public val colors: ArecaColors
        @Composable @ReadOnlyComposable get() = LocalColors.current

    /** Corner radius lengths. Prefer [shapes] unless a raw length is needed. */
    public val radius: ArecaRadius
        @Composable @ReadOnlyComposable get() = LocalRadius.current

    /** Corner shapes derived from [radius]. */
    public val shapes: ArecaShapes
        @Composable @ReadOnlyComposable get() = LocalShapes.current

    /** Spacing scale and screen gutters. */
    public val spacing: ArecaSpacing
        @Composable @ReadOnlyComposable get() = LocalSpacing.current

    /** Shadow elevation scale. */
    public val elevation: ArecaElevation
        @Composable @ReadOnlyComposable get() = LocalElevation.current

    /** Animation duration and easing tokens. */
    public val motion: ArecaMotion
        @Composable @ReadOnlyComposable get() = LocalMotion.current

    /** Font families the type scale is rendered with. */
    public val fonts: ArecaFonts
        @Composable @ReadOnlyComposable get() = LocalFonts.current

    /** Raw type scale the named [typography] styles are derived from. */
    public val baseTypography: ArecaBaseTypography
        @Composable @ReadOnlyComposable get() = LocalBaseTypography.current

    /** Named text styles. */
    public val typography: ArecaTypography
        @Composable @ReadOnlyComposable get() = LocalTypography.current

    /**
     * The [colors] this theme has in the dark: [colors] themselves when [isDark], otherwise
     * [baseColors] mapped by [ArecaColors.dark].
     *
     * For a part that is dark whatever the theme — a photo editor, a media viewer — and has to name
     * a dark colour before its [ArecaDarkTheme] scope begins, such as a style parameter's default,
     * which is resolved where the part is called. Inside the scope, [colors] is the same value.
     *
     * A light theme given its own `colors` rather than [baseColors] has no dark counterpart to
     * consult, so this maps its [baseColors].
     */
    public val darkColors: ArecaColors
        @Composable get() {
            if (isDark) return colors
            val base = baseColors
            return remember(base) { ArecaColors.dark(base) }
        }
}

/** Theme direction in scope. Read it through [ArecaTheme.isDark]. */
internal val LocalIsDarkTheme = staticCompositionLocalOf { false }
