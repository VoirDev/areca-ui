package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Van icon.
 *
 * Converted from Lucide `van` (ISC).
 */
public val ArecaIcons.Van: ImageVector
    get() = cachedVan ?: ImageVector.Builder(
        name = "Van",
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
            moveTo(13f, 6f)
            verticalLineTo(11f)
            arcTo(1f, 1f, 0f, false, false, 14f, 12f)
            horizontalLineTo(20.102f)
            arcTo(1f, 1f, 0f, false, true, 20.814f, 12.298f)
            lineTo(21.712f, 13.208f)
            arcTo(1f, 1f, 0f, false, true, 22f, 13.91f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 21f, 18f)
            horizontalLineTo(18f)
            moveTo(5f, 18f)
            horizontalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 2f, 17f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 4f, 6f)
            horizontalLineTo(16f)
            curveTo(17.1f, 6f, 18.1f, 6.8f, 18.4f, 7.8f)
            lineTo(19.576f, 12f)
            moveTo(9f, 18f)
            horizontalLineTo(14f)
            moveTo(14f, 18f)
            arcTo(2f, 2f, 0f, true, true, 18f, 18f)
            arcTo(2f, 2f, 0f, true, true, 14f, 18f)
            close()
            moveTo(5f, 18f)
            arcTo(2f, 2f, 0f, true, true, 9f, 18f)
            arcTo(2f, 2f, 0f, true, true, 5f, 18f)
            close()
        }
    }.build().also { cachedVan = it }

/** `Van` vector, built on first access and reused afterwards. */
private var cachedVan: ImageVector? = null
