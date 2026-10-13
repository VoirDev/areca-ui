package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Plane landing icon.
 *
 * Converted from Lucide `plane-landing` (ISC).
 */
public val ArecaIcons.PlaneLanding: ImageVector
    get() = cachedPlaneLanding ?: ImageVector.Builder(
        name = "PlaneLanding",
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
            moveTo(3.77f, 10.77f)
            lineTo(2f, 9f)
            lineTo(4f, 4.5f)
            lineTo(5.1f, 5.05f)
            curveTo(5.65f, 5.33f, 6f, 5.89f, 6f, 6.5f)
            reflectiveCurveTo(6.35f, 7.67f, 6.9f, 7.95f)
            lineTo(8f, 8.5f)
            lineTo(11f, 2.5f)
            lineTo(12.05f, 3.03f)
            arcTo(2f, 2f, 0f, false, true, 13.14f, 4.55f)
            lineTo(13.86f, 9.95f)
            arcTo(2f, 2f, 0f, false, false, 14.95f, 11.47f)
            lineTo(19.35f, 13.67f)
            curveTo(19.77f, 13.89f, 20.13f, 14.22f, 20.36f, 14.63f)
            lineTo(20.96f, 15.66f)
            curveTo(21.45f, 16.54f, 20.9f, 17.64f, 19.9f, 17.76f)
            lineTo(18.72f, 17.91f)
            curveTo(18.25f, 17.97f, 17.77f, 17.89f, 17.35f, 17.67f)
            lineTo(4.29f, 11.15f)
            arcTo(2f, 2f, 0f, false, true, 3.77f, 10.77f)
            close()
        }
    }.build().also { cachedPlaneLanding = it }

/** `PlaneLanding` vector, built on first access and reused afterwards. */
private var cachedPlaneLanding: ImageVector? = null
