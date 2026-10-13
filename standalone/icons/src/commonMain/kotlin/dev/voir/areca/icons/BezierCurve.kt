package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Vector bezier icon.
 *
 * Converted from Tabler Icons `vector-bezier` (MIT).
 */
public val ArecaIcons.BezierCurve: ImageVector
    get() = cachedBezierCurve ?: ImageVector.Builder(
        name = "BezierCurve",
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
            moveTo(3f, 15f)
            arcTo(1f, 1f, 0f, false, true, 4f, 14f)
            horizontalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 7f, 15f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 6f, 18f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 17f)
            lineTo(3f, 15f)
            moveTo(17f, 15f)
            arcTo(1f, 1f, 0f, false, true, 18f, 14f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 15f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 20f, 18f)
            horizontalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 17f, 17f)
            lineTo(17f, 15f)
            moveTo(10f, 7f)
            arcTo(1f, 1f, 0f, false, true, 11f, 6f)
            horizontalLineTo(13f)
            arcTo(1f, 1f, 0f, false, true, 14f, 7f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 13f, 10f)
            horizontalLineTo(11f)
            arcTo(1f, 1f, 0f, false, true, 10f, 9f)
            lineTo(10f, 7f)
            moveTo(10f, 8.5f)
            arcTo(6f, 6f, 0f, false, false, 5f, 14f)
            moveTo(14f, 8.5f)
            arcTo(6f, 6f, 0f, false, true, 19f, 14f)
            moveTo(10f, 8f)
            lineTo(4f, 8f)
            moveTo(20f, 8f)
            lineTo(14f, 8f)
            moveTo(2f, 8f)
            arcTo(1f, 1f, 0f, true, false, 4f, 8f)
            arcTo(1f, 1f, 0f, true, false, 2f, 8f)
            moveTo(20f, 8f)
            arcTo(1f, 1f, 0f, true, false, 22f, 8f)
            arcTo(1f, 1f, 0f, true, false, 20f, 8f)
        }
    }.build().also { cachedBezierCurve = it }

/** `BezierCurve` vector, built on first access and reused afterwards. */
private var cachedBezierCurve: ImageVector? = null
