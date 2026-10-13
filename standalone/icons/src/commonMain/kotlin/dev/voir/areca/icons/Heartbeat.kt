package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Heartbeat icon.
 *
 * Converted from Tabler Icons `heartbeat` (MIT).
 */
public val ArecaIcons.Heartbeat: ImageVector
    get() = cachedHeartbeat ?: ImageVector.Builder(
        name = "Heartbeat",
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
            moveTo(19.5f, 13.572f)
            lineTo(12f, 21f)
            lineTo(9.104f, 18.132f)
            moveTo(2.987f, 10.028f)
            arcTo(5f, 5f, 0f, false, true, 12f, 7.006f)
            arcTo(5f, 5f, 0f, true, true, 19.5f, 13.578f)
            moveTo(3f, 13f)
            horizontalLineTo(5f)
            lineTo(7f, 16f)
            lineTo(9f, 10f)
            lineTo(10f, 13f)
            horizontalLineTo(13f)
        }
    }.build().also { cachedHeartbeat = it }

/** `Heartbeat` vector, built on first access and reused afterwards. */
private var cachedHeartbeat: ImageVector? = null
