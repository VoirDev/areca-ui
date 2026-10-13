package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Plane takeoff icon.
 *
 * Converted from Lucide `plane-takeoff` (ISC).
 */
public val ArecaIcons.PlaneTakeoff: ImageVector
    get() = cachedPlaneTakeoff ?: ImageVector.Builder(
        name = "PlaneTakeoff",
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
            moveTo(2f, 22f)
            horizontalLineTo(22f)
            moveTo(6.36f, 17.4f)
            lineTo(4f, 17f)
            lineTo(2f, 13f)
            lineTo(3.1f, 12.45f)
            arcTo(2f, 2f, 0f, false, true, 4.9f, 12.45f)
            lineTo(5.07f, 12.55f)
            arcTo(2f, 2f, 0f, false, false, 6.87f, 12.55f)
            lineTo(8f, 12f)
            lineTo(5f, 6f)
            lineTo(5.9f, 5.55f)
            arcTo(2f, 2f, 0f, false, true, 7.99f, 5.75f)
            lineTo(12.01f, 8.75f)
            arcTo(2f, 2f, 0f, false, false, 14.11f, 8.95f)
            lineTo(18.3f, 6.89f)
            arcTo(2.41f, 2.41f, 0f, false, true, 20.03f, 6.72f)
            lineTo(21f, 7f)
            arcTo(1.4f, 1.4f, 0f, false, true, 21.87f, 8.99f)
            lineTo(21.49f, 9.75f)
            curveTo(21.26f, 10.21f, 20.89f, 10.59f, 20.42f, 10.83f)
            lineTo(7.58f, 17.2f)
            arcTo(2f, 2f, 0f, false, true, 6.36f, 17.38f)
            close()
        }
    }.build().also { cachedPlaneTakeoff = it }

/** `PlaneTakeoff` vector, built on first access and reused afterwards. */
private var cachedPlaneTakeoff: ImageVector? = null
