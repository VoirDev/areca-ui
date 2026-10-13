package dev.voir.areca.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Corner radius scale.
 *
 * These are raw lengths. Components should reach for [ArecaShapes] instead, which turns this scale
 * into ready [androidx.compose.ui.graphics.Shape] values; read a radius directly only when a length
 * is genuinely what is needed, such as a hand-drawn corner in a `Canvas`.
 *
 * The scale is intentionally short. A design system with many near-identical radii reads as sloppy
 * rather than expressive, so each step is a visibly different corner.
 */
@Immutable
public data class ArecaRadius(
    /** Square corners. */
    public val none: Dp = 0.dp,
    /** Barely rounded, for hairline elements such as a progress track. */
    public val xs: Dp = 2.dp,
    /** Small controls: badges, chips, swatches. */
    public val sm: Dp = 4.dp,
    /** Default for interactive surfaces: buttons, inputs, list rows. */
    public val md: Dp = 8.dp,
    /** Containers: cards, popovers, dropdowns. */
    public val lg: Dp = 16.dp,
    /** Large surfaces: sheets and modals. */
    public val xl: Dp = 24.dp,
    /** Full-bleed surfaces that still need a visible corner. */
    public val xl2: Dp = 32.dp,
)

/** Radius scale in scope. Read it through [ArecaTheme.radius]. */
internal val LocalRadius = staticCompositionLocalOf { ArecaRadius() }
