package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Horse icon.
 *
 * Converted from Tabler Icons `horse` (MIT).
 */
public val ArecaIcons.Horse: ImageVector
    get() = cachedHorse ?: ImageVector.Builder(
        name = "Horse",
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
            moveTo(7f, 10f)
            lineTo(6.15f, 18.507f)
            arcTo(1.357f, 1.357f, 0f, false, false, 7.5f, 20f)
            horizontalLineTo(7.646f)
            arcTo(2f, 2f, 0f, false, false, 9.503f, 18.743f)
            lineTo(10.497f, 16.257f)
            arcTo(2f, 2f, 0f, false, true, 12.354f, 15f)
            horizontalLineTo(13.646f)
            arcTo(2f, 2f, 0f, false, true, 15.503f, 16.257f)
            lineTo(16.497f, 18.743f)
            arcTo(2f, 2f, 0f, false, false, 18.354f, 20f)
            horizontalLineTo(18.5f)
            arcTo(1.37f, 1.37f, 0f, false, false, 19.864f, 18.506f)
            lineTo(19f, 9f)
            horizontalLineTo(11f)
            curveTo(11f, 6f, 8f, 4f, 5f, 4f)
            lineTo(2f, 10f)
            lineTo(4f, 12f)
            lineTo(7f, 10f)
            moveTo(22f, 14f)
            verticalLineTo(12f)
            arcTo(3f, 3f, 0f, false, false, 19f, 9f)
        }
    }.build().also { cachedHorse = it }

/** `Horse` vector, built on first access and reused afterwards. */
private var cachedHorse: ImageVector? = null
