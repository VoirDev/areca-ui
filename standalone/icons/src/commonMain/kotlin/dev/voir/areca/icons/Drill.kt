package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Drill icon.
 *
 * Converted from Lucide `drill` (ISC).
 */
public val ArecaIcons.Drill: ImageVector
    get() = cachedDrill ?: ImageVector.Builder(
        name = "Drill",
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
            arcTo(1f, 1f, 0f, false, true, 11f, 19f)
            verticalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 10f, 22f)
            horizontalLineTo(5f)
            arcTo(3f, 3f, 0f, false, true, 2f, 19f)
            arcTo(1f, 1f, 0f, false, true, 3f, 18f)
            close()
            moveTo(13f, 10f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 8f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 4f, 2f)
            horizontalLineTo(13f)
            arcTo(1f, 1f, 0f, false, true, 14f, 3f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 13f, 10f)
            lineTo(12.19f, 13.242f)
            arcTo(1f, 1f, 0f, false, true, 11.22f, 14f)
            horizontalLineTo(8f)
            moveTo(14f, 4f)
            horizontalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 18f, 5f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 17f, 8f)
            horizontalLineTo(14f)
            moveTo(18f, 6f)
            horizontalLineTo(22f)
            moveTo(5f, 10f)
            lineTo(3f, 18f)
            moveTo(7f, 18f)
            lineTo(9f, 10f)
        }
    }.build().also { cachedDrill = it }

/** `Drill` vector, built on first access and reused afterwards. */
private var cachedDrill: ImageVector? = null
