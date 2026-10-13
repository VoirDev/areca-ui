package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Landmark icon.
 *
 * Converted from Lucide `landmark` (ISC).
 */
public val ArecaIcons.Landmark: ImageVector
    get() = cachedLandmark ?: ImageVector.Builder(
        name = "Landmark",
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
            moveTo(10f, 18f)
            verticalLineTo(11f)
            moveTo(11.119f, 2.205f)
            arcTo(2f, 2f, 0f, false, true, 12.881f, 2.205f)
            lineTo(20.721f, 6.051f)
            arcTo(0.5f, 0.5f, 0f, false, true, 20.5f, 7f)
            horizontalLineTo(3.5f)
            arcTo(0.5f, 0.5f, 0f, false, true, 3.28f, 6.051f)
            close()
            moveTo(14f, 18f)
            verticalLineTo(11f)
            moveTo(18f, 18f)
            verticalLineTo(11f)
            moveTo(3f, 22f)
            horizontalLineTo(21f)
            moveTo(6f, 18f)
            verticalLineTo(11f)
        }
    }.build().also { cachedLandmark = it }

/** `Landmark` vector, built on first access and reused afterwards. */
private var cachedLandmark: ImageVector? = null
