package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Road sign icon.
 *
 * Converted from Tabler Icons `road-sign` (MIT).
 */
public val ArecaIcons.RoadSign: ImageVector
    get() = cachedRoadSign ?: ImageVector.Builder(
        name = "RoadSign",
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
            moveTo(13.446f, 2.6f)
            lineTo(21.401f, 10.554f)
            arcTo(2.045f, 2.045f, 0f, false, true, 21.401f, 13.446f)
            lineTo(13.446f, 21.401f)
            arcTo(2.045f, 2.045f, 0f, false, true, 10.554f, 21.401f)
            lineTo(2.599f, 13.446f)
            arcTo(2.045f, 2.045f, 0f, false, true, 2.599f, 10.554f)
            lineTo(10.554f, 2.599f)
            arcTo(2.045f, 2.045f, 0f, false, true, 13.446f, 2.599f)
            moveTo(9f, 14f)
            verticalLineTo(12f)
            curveTo(9f, 11.41f, 9.414f, 11f, 10f, 11f)
            horizontalLineTo(15f)
            moveTo(13f, 9f)
            lineTo(15f, 11f)
            lineTo(13f, 13f)
        }
    }.build().also { cachedRoadSign = it }

/** `RoadSign` vector, built on first access and reused afterwards. */
private var cachedRoadSign: ImageVector? = null
