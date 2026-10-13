package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Earth icon.
 *
 * Converted from Lucide `earth` (ISC).
 */
public val ArecaIcons.Earth: ImageVector
    get() = cachedEarth ?: ImageVector.Builder(
        name = "Earth",
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
            moveTo(21.54f, 15f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 15f, 17f)
            verticalLineTo(21.54f)
            moveTo(7f, 3.34f)
            verticalLineTo(5f)
            arcTo(3f, 3f, 0f, false, false, 10f, 8f)
            arcTo(2f, 2f, 0f, false, true, 12f, 10f)
            curveTo(12f, 11.1f, 12.9f, 12f, 14f, 12f)
            arcTo(2f, 2f, 0f, false, false, 16f, 10f)
            curveTo(16f, 8.9f, 16.9f, 8f, 18f, 8f)
            horizontalLineTo(21.17f)
            moveTo(11f, 21.95f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 9f, 16f)
            arcTo(2f, 2f, 0f, false, true, 7f, 14f)
            verticalLineTo(13f)
            arcTo(2f, 2f, 0f, false, false, 5f, 11f)
            horizontalLineTo(2.05f)
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
        }
    }.build().also { cachedEarth = it }

/** `Earth` vector, built on first access and reused afterwards. */
private var cachedEarth: ImageVector? = null
