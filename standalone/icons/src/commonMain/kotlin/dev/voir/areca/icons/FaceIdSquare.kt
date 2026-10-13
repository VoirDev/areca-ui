package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Scan face icon.
 *
 * Converted from Lucide `scan-face` (ISC).
 */
public val ArecaIcons.FaceIdSquare: ImageVector
    get() = cachedFaceIdSquare ?: ImageVector.Builder(
        name = "FaceIdSquare",
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
            moveTo(3f, 7f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            horizontalLineTo(7f)
            moveTo(17f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(7f)
            moveTo(21f, 17f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(17f)
            moveTo(7f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(17f)
            moveTo(8f, 14f)
            reflectiveCurveTo(9.5f, 16f, 12f, 16f)
            reflectiveCurveTo(16f, 14f, 16f, 14f)
            moveTo(9f, 9f)
            horizontalLineTo(9.01f)
            moveTo(15f, 9f)
            horizontalLineTo(15.01f)
        }
    }.build().also { cachedFaceIdSquare = it }

/** `FaceIdSquare` vector, built on first access and reused afterwards. */
private var cachedFaceIdSquare: ImageVector? = null
