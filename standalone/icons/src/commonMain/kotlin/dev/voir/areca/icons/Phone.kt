package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Phone icon.
 *
 * Converted from Lucide `phone` (ISC).
 */
public val ArecaIcons.Phone: ImageVector
    get() = cachedPhone ?: ImageVector.Builder(
        name = "Phone",
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
            moveTo(13.832f, 16.568f)
            arcTo(1f, 1f, 0f, false, false, 15.045f, 16.265f)
            lineTo(15.4f, 15.8f)
            arcTo(2f, 2f, 0f, false, true, 17f, 15f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 17f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 20f, 22f)
            arcTo(18f, 18f, 0f, false, true, 2f, 4f)
            arcTo(2f, 2f, 0f, false, true, 4f, 2f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 9f, 4f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 8.2f, 8.6f)
            lineTo(7.732f, 8.951f)
            arcTo(1f, 1f, 0f, false, false, 7.44f, 10.184f)
            arcTo(14f, 14f, 0f, false, false, 13.832f, 16.568f)
        }
    }.build().also { cachedPhone = it }

/** `Phone` vector, built on first access and reused afterwards. */
private var cachedPhone: ImageVector? = null
