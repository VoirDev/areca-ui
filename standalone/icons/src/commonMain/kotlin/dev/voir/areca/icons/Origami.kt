package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Origami icon.
 *
 * Converted from Lucide `origami` (ISC).
 */
public val ArecaIcons.Origami: ImageVector
    get() = cachedOrigami ?: ImageVector.Builder(
        name = "Origami",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(12f, 12f)
            verticalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 13f, 3f)
            horizontalLineTo(19.297f)
            arcTo(1f, 1f, 0f, false, true, 19.948f, 4.759f)
            lineTo(15.252f, 8.784f)
            moveTo(12f, 21f)
            lineTo(4.586f, 13.586f)
            arcTo(2f, 2f, 0f, false, true, 4f, 12.172f)
            verticalLineTo(6.415f)
            arcTo(1.002f, 1.002f, 0f, false, true, 5.707f, 5.708f)
            lineTo(20f, 20.009f)
            moveTo(12.214f, 3.381f)
            lineTo(20.628f, 18.347f)
            arcTo(1f, 1f, 0f, false, true, 20.461f, 19.546f)
            lineTo(19.293f, 20.709f)
            arcTo(1f, 1f, 0f, false, true, 18.587f, 21f)
            horizontalLineTo(6.351f)
            arcTo(1f, 1f, 0f, false, true, 5.726f, 20.781f)
            lineTo(3.25f, 18.8f)
            arcTo(1f, 1f, 0f, false, true, 3.881f, 17.019f)
            lineTo(8.046f, 17.046f)
        }
    }.build().also { cachedOrigami = it }

/** `Origami` vector, built on first access and reused afterwards. */
private var cachedOrigami: ImageVector? = null
