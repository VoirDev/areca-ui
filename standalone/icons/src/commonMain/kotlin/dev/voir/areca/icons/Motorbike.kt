package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Motorbike icon.
 *
 * Converted from Lucide `motorbike` (ISC).
 */
public val ArecaIcons.Motorbike: ImageVector
    get() = cachedMotorbike ?: ImageVector.Builder(
        name = "Motorbike",
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
            moveTo(18f, 14f)
            lineTo(17f, 11f)
            moveTo(3f, 9f)
            lineTo(9f, 11f)
            arcTo(2f, 2f, 0f, false, true, 11f, 9f)
            horizontalLineTo(13f)
            arcTo(2f, 2f, 0f, false, true, 14.99f, 10.81f)
            moveTo(8f, 17f)
            horizontalLineTo(11f)
            arcTo(1f, 1f, 0f, false, false, 12f, 16f)
            arcTo(6f, 6f, 0f, false, true, 18f, 10f)
            arcTo(1f, 1f, 0f, false, false, 19f, 9f)
            verticalLineTo(8.25f)
            arcTo(5f, 5f, 0f, false, false, 17f, 5f)
            moveTo(16f, 17f)
            arcTo(3f, 3f, 0f, true, true, 22f, 17f)
            arcTo(3f, 3f, 0f, true, true, 16f, 17f)
            close()
            moveTo(2f, 17f)
            arcTo(3f, 3f, 0f, true, true, 8f, 17f)
            arcTo(3f, 3f, 0f, true, true, 2f, 17f)
            close()
        }
    }.build().also { cachedMotorbike = it }

/** `Motorbike` vector, built on first access and reused afterwards. */
private var cachedMotorbike: ImageVector? = null
