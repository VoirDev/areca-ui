package dev.voir.areca.crop

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * Masks a crop frame commonly wears that Compose has no shape for.
 *
 * Any [Shape] is a mask: `RectangleShape` for a photo, `RoundedCornerShape(12)` for a card,
 * `SquircleShape` for an app icon, or a product's own outline. These fill the gap the stock shapes
 * leave.
 */
public object CropShapes {

    /**
     * An ellipse touching all four sides of the frame — a circle on a square frame.
     *
     * Prefer it to [CircleShape] for a crop mask: [CircleShape] is a fully rounded rectangle, so on
     * a frame that is not square it draws a pill rather than the ellipse the photo is cut to.
     */
    public val Oval: Shape = OvalShape
}

/** The ellipse inscribed in its bounds. */
private object OvalShape : Shape {

    /**
     * Outlines the ellipse filling [size].
     *
     * @param size Bounds of the ellipse.
     * @param layoutDirection Ignored: an ellipse reads the same both ways.
     * @param density Ignored: the ellipse is defined by its bounds alone.
     * @return A generic outline of the ellipse.
     */
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline =
        Outline.Generic(Path().apply { addOval(Rect(0f, 0f, size.width, size.height)) })

    /**
     * Names the shape in logs and test output.
     *
     * @return `CropShapes.Oval`, the name callers reach the shape by.
     */
    override fun toString(): String = "CropShapes.Oval"
}
