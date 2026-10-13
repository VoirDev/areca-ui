package dev.voir.areca.shape

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sin

/**
 * Superellipse — a rounded rectangle whose corners blend continuously into its sides.
 *
 * A [androidx.compose.foundation.shape.RoundedCornerShape] joins a straight side to a circular arc,
 * and the curvature jumps at that seam. A superellipse has no seam, which is why icon grids and
 * avatars drawn this way read as softer than their corner radius suggests.
 *
 * The outline is `|x/a|^n + |y/b|^n = 1`, sampled as [steps] line segments. It scales with the
 * element rather than holding a fixed corner radius, so a wide element gets proportionally wider
 * corners — use a rounded shape instead when a constant radius matters.
 *
 * @property exponent Fullness of the curve. `2` is an ellipse, `4` the familiar squircle, and larger
 *   values approach a rectangle. Must be greater than zero.
 * @property steps Segments the outline is sampled into. Higher is smoother and slower; the default
 *   is imperceptible from a true curve at normal component sizes. Must be at least 12.
 */
@Immutable
public data class SquircleShape(
    public val exponent: Float = 4f,
    public val steps: Int = 120,
) : Shape {

    init {
        require(exponent > 0f) { "exponent must be > 0, was $exponent" }
        require(steps >= 12) { "steps must be >= 12, was $steps" }
    }

    /**
     * Samples the superellipse inscribed in [size].
     *
     * @param size Bounds the curve touches at the middle of each side.
     * @param layoutDirection Ignored: the shape is symmetric.
     * @param density Ignored: the shape has no fixed dimensions.
     * @return A generic outline of [steps] segments.
     */
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val path = Path()
        val halfWidth = size.width / 2f
        val halfHeight = size.height / 2f
        // Parametric form of the superellipse, which avoids solving for y at each x:
        //   x = a * sign(cos t) * |cos t|^(2/n)
        //   y = b * sign(sin t) * |sin t|^(2/n)
        val power = 2f / exponent

        for (step in 0..steps) {
            val angle = (step.toFloat() / steps.toFloat()) * PI * 2.0
            val cosAngle = cos(angle).toFloat()
            val sinAngle = sin(angle).toFloat()
            val x = halfWidth + halfWidth * sign(cosAngle) * abs(cosAngle).pow(power)
            val y = halfHeight + halfHeight * sign(sinAngle) * abs(sinAngle).pow(power)

            if (step == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        path.close()
        return Outline.Generic(path)
    }
}
