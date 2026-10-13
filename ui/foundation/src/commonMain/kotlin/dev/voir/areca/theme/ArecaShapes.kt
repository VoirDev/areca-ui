package dev.voir.areca.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import dev.voir.areca.shape.SquircleShape

/**
 * Corner shapes derived from [ArecaRadius].
 *
 * Components should clip and outline with these rather than building a `RoundedCornerShape` from a
 * radius at each call site: the shape then becomes a single overridable decision, and a consumer who
 * prefers a different corner geometry — a squircle, say — can swap one token set instead of
 * rewriting every component.
 *
 * Build an instance with [from] rather than the constructor, and use `copy` to override one shape —
 * here, squircle inputs and rows, and capsule buttons — then hand it to `ArecaThemeProvider`:
 *
 * ```kotlin
 * val shapes = ArecaShapes.from(ArecaRadius())
 * val custom = shapes.copy(md = SquircleShape(), button = shapes.full)
 * ```
 *
 * Every shape is a required constructor parameter on purpose, so a step added later cannot silently
 * fall back to a value that was never considered.
 */
@Immutable
public data class ArecaShapes(
    /** Square corners. */
    public val none: Shape,
    /** Barely rounded, for hairline elements such as a progress track. */
    public val xs: Shape,
    /** Small controls: badges, chips, swatches. */
    public val sm: Shape,
    /** Default for interactive surfaces: inputs, list rows. */
    public val md: Shape,
    /** Containers: cards, popovers, dropdowns. */
    public val lg: Shape,
    /** Large surfaces: sheets and modals. */
    public val xl: Shape,
    /** Full-bleed surfaces that still need a visible corner. */
    public val xl2: Shape,
    /**
     * Fully rounded ends.
     *
     * On a square element this is a circle, on a wide one a pill. It is the single spelling for what
     * would otherwise appear as `CircleShape`, `RoundedCornerShape(percent = 50)`, and
     * `RoundedCornerShape(50)` in different places.
     */
    public val full: Shape,
    /**
     * A small square that holds a glyph: a settings tile, a list row's leading tile, a category
     * mark.
     *
     * A squircle rather than a rounded square, so a column of tiles reads as a set of app icons. It
     * is a shape of its own rather than one of the radius steps because it scales with the element
     * instead of holding a fixed corner — the same curve on a small tile and a large one.
     */
    public val tile: Shape,
    /**
     * A labelled action: every [dev.voir.areca.foundation.button.ButtonStyle] factory's default, and
     * the press shape of a plain top-bar control.
     *
     * A role rather than a radius step, so a product can draw every action in one shape — capsules,
     * say — with one override instead of restating the shape at each call. Defaults to [md].
     */
    public val button: Shape,
) {
    /** Factories for a complete shape scale. */
    public companion object {
        /**
         * Derives the shape scale from a radius scale. [tile] is a squircle and does not follow it;
         * [button] is [md].
         *
         * @param radius Corner lengths each shape is built from.
         * @return Complete shape scale.
         */
        public fun from(radius: ArecaRadius = ArecaRadius()): ArecaShapes = ArecaShapes(
            none = RectangleShape,
            xs = RoundedCornerShape(radius.xs),
            sm = RoundedCornerShape(radius.sm),
            md = RoundedCornerShape(radius.md),
            button = RoundedCornerShape(radius.md),
            lg = RoundedCornerShape(radius.lg),
            xl = RoundedCornerShape(radius.xl),
            xl2 = RoundedCornerShape(radius.xl2),
            full = CircleShape,
            tile = SquircleShape(exponent = 2.8f),
        )
    }
}

/** Shape scale in scope. Read it through [ArecaTheme.shapes]. */
internal val LocalShapes = staticCompositionLocalOf { ArecaShapes.from() }
