package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Heart handshake icon.
 *
 * Converted from Tabler Icons `heart-handshake` (MIT).
 */
public val ArecaIcons.HeartHandshake: ImageVector
    get() = cachedHeartHandshake ?: ImageVector.Builder(
        name = "HeartHandshake",
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
            moveTo(19.5f, 12.572f)
            lineTo(12f, 20f)
            lineTo(4.5f, 12.572f)
            arcTo(5f, 5f, 0f, true, true, 12f, 6.006f)
            arcTo(5f, 5f, 0f, true, true, 19.5f, 12.578f)
            moveTo(12f, 6f)
            lineTo(8.707f, 9.293f)
            arcTo(1f, 1f, 0f, false, false, 8.707f, 10.707f)
            lineTo(9.25f, 11.25f)
            curveTo(9.94f, 11.94f, 11.06f, 11.94f, 11.75f, 11.25f)
            lineTo(12.75f, 10.25f)
            arcTo(3.182f, 3.182f, 0f, false, true, 17.25f, 10.25f)
            lineTo(19.5f, 12.5f)
            moveTo(12.5f, 15.5f)
            lineTo(14.5f, 17.5f)
            moveTo(15f, 13f)
            lineTo(17f, 15f)
        }
    }.build().also { cachedHeartHandshake = it }

/** `HeartHandshake` vector, built on first access and reused afterwards. */
private var cachedHeartHandshake: ImageVector? = null
