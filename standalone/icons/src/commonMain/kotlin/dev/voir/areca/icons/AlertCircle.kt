package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle alert icon.
 *
 * Converted from Lucide `circle-alert` (ISC).
 */
public val ArecaIcons.AlertCircle: ImageVector
    get() = cachedAlertCircle ?: ImageVector.Builder(
        name = "AlertCircle",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(12f, 8f)
            lineTo(12f, 12f)
            moveTo(12f, 16f)
            lineTo(12.01f, 16f)
        }
    }.build().also { cachedAlertCircle = it }

/** `AlertCircle` vector, built on first access and reused afterwards. */
private var cachedAlertCircle: ImageVector? = null
