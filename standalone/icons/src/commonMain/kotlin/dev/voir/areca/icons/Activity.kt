package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Activity icon.
 *
 * Converted from Lucide `activity` (ISC).
 */
public val ArecaIcons.Activity: ImageVector
    get() = cachedActivity ?: ImageVector.Builder(
        name = "Activity",
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
            moveTo(22f, 12f)
            horizontalLineTo(19.52f)
            arcTo(2f, 2f, 0f, false, false, 17.59f, 13.46f)
            lineTo(15.24f, 21.82f)
            arcTo(0.25f, 0.25f, 0f, false, true, 14.76f, 21.82f)
            lineTo(9.24f, 2.18f)
            arcTo(0.25f, 0.25f, 0f, false, false, 8.76f, 2.18f)
            lineTo(6.41f, 10.54f)
            arcTo(2f, 2f, 0f, false, true, 4.49f, 12f)
            horizontalLineTo(2f)
        }
    }.build().also { cachedActivity = it }

/** `Activity` vector, built on first access and reused afterwards. */
private var cachedActivity: ImageVector? = null
