package dev.voir.areca.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Shadow elevation scale.
 *
 * Elevation here means one thing only: how far a surface floats above the one behind it, expressed
 * as the `elevation` a [androidx.compose.ui.draw.shadow] modifier takes. It does not tint the
 * surface — Areca separates the two, so a raised surface picks its own background from
 * [ArecaColors].
 *
 * The steps are a stacking order, not a decoration. A surface should sit one step above whatever it
 * overlaps, which keeps depth readable when several layers are on screen at once.
 */
@Immutable
public data class ArecaElevation(
    /** Flush with the surface behind it. No shadow is drawn. */
    public val none: Dp = 0.dp,
    /** Resting lift for a surface that must separate from an adjacent one, such as a sticky bar. */
    public val xs: Dp = 1.dp,
    /** A surface lifted by interaction, such as a dragged row or a toggle thumb. */
    public val sm: Dp = 2.dp,
    /** Anchored transient surfaces: dropdowns, popovers, tooltips. */
    public val md: Dp = 4.dp,
    /** Floating surfaces that own the screen's focus, such as a toast or a floating tab bar. */
    public val lg: Dp = 8.dp,
    /** Modal surfaces presented over a scrim. */
    public val xl: Dp = 16.dp,
)

/** Elevation scale in scope. Read it through [ArecaTheme.elevation]. */
internal val LocalElevation = staticCompositionLocalOf { ArecaElevation() }
