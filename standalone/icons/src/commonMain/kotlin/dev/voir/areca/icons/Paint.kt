package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Paint icon.
 *
 * Converted from Tabler Icons `paint` (MIT).
 */
public val ArecaIcons.Paint: ImageVector
    get() = cachedPaint ?: ImageVector.Builder(
        name = "Paint",
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
            moveTo(5f, 5f)
            arcTo(2f, 2f, 0f, false, true, 7f, 3f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 19f, 5f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 17f, 9f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 5f, 7f)
            lineTo(5f, 5f)
            moveTo(19f, 6f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 8f)
            arcTo(5f, 5f, 0f, false, true, 17f, 13f)
            lineTo(12f, 13f)
            verticalLineTo(15f)
            moveTo(10f, 16f)
            arcTo(1f, 1f, 0f, false, true, 11f, 15f)
            horizontalLineTo(13f)
            arcTo(1f, 1f, 0f, false, true, 14f, 16f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 13f, 21f)
            horizontalLineTo(11f)
            arcTo(1f, 1f, 0f, false, true, 10f, 20f)
            lineTo(10f, 16f)
        }
    }.build().also { cachedPaint = it }

/** `Paint` vector, built on first access and reused afterwards. */
private var cachedPaint: ImageVector? = null
