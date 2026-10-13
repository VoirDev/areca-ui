package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle check icon.
 *
 * Converted from Lucide `circle-check` (ISC).
 */
public val ArecaIcons.CheckCircle: ImageVector
    get() = cachedCheckCircle ?: ImageVector.Builder(
        name = "CheckCircle",
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
            moveTo(16f, 9f)
            lineTo(10.5f, 14.5f)
            lineTo(8f, 12f)
        }
    }.build().also { cachedCheckCircle = it }

/** `CheckCircle` vector, built on first access and reused afterwards. */
private var cachedCheckCircle: ImageVector? = null
