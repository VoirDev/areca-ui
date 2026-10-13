package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Balloon icon.
 *
 * Converted from Lucide `balloon` (ISC).
 */
public val ArecaIcons.Balloon: ImageVector
    get() = cachedBalloon ?: ImageVector.Builder(
        name = "Balloon",
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
            moveTo(12f, 16f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 14f, 19f)
            horizontalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 17f, 21f)
            verticalLineTo(22f)
            moveTo(12f, 6f)
            arcTo(2f, 2f, 0f, false, true, 14f, 8f)
            moveTo(18f, 8f)
            curveTo(18f, 12f, 14.5f, 16f, 12f, 16f)
            reflectiveCurveTo(6f, 12f, 6f, 8f)
            arcTo(6f, 6f, 0f, false, true, 18f, 8f)
        }
    }.build().also { cachedBalloon = it }

/** `Balloon` vector, built on first access and reused afterwards. */
private var cachedBalloon: ImageVector? = null
