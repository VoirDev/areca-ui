package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Asterisk icon.
 *
 * Converted from Lucide `asterisk` (ISC).
 */
public val ArecaIcons.Asterisk: ImageVector
    get() = cachedAsterisk ?: ImageVector.Builder(
        name = "Asterisk",
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
            moveTo(12f, 5f)
            verticalLineTo(19f)
            moveTo(18.065f, 8.496f)
            lineTo(5.94f, 15.496f)
            moveTo(5.94f, 8.504f)
            lineTo(18.065f, 15.504f)
        }
    }.build().also { cachedAsterisk = it }

/** `Asterisk` vector, built on first access and reused afterwards. */
private var cachedAsterisk: ImageVector? = null
