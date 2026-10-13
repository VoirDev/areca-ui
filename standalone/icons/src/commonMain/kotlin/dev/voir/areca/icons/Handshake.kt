package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Handshake icon.
 *
 * Converted from Lucide `handshake` (ISC).
 */
public val ArecaIcons.Handshake: ImageVector
    get() = cachedHandshake ?: ImageVector.Builder(
        name = "Handshake",
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
            moveTo(11f, 17f)
            lineTo(13f, 19f)
            arcTo(1f, 1f, 0f, true, false, 16f, 16f)
            moveTo(14f, 14f)
            lineTo(16.5f, 16.5f)
            arcTo(1f, 1f, 0f, true, false, 19.5f, 13.5f)
            lineTo(15.62f, 9.62f)
            arcTo(3f, 3f, 0f, false, false, 11.38f, 9.62f)
            lineTo(10.5f, 10.5f)
            arcTo(1f, 1f, 0f, true, true, 7.5f, 7.5f)
            lineTo(10.31f, 4.69f)
            arcTo(5.79f, 5.79f, 0f, false, true, 17.37f, 3.82f)
            lineTo(17.84f, 4.1f)
            arcTo(2f, 2f, 0f, false, false, 19.26f, 4.35f)
            lineTo(21f, 4f)
            moveTo(21f, 3f)
            lineTo(22f, 14f)
            horizontalLineTo(20f)
            moveTo(3f, 3f)
            lineTo(2f, 14f)
            lineTo(8.5f, 20.5f)
            arcTo(1f, 1f, 0f, true, false, 11.5f, 17.5f)
            moveTo(3f, 4f)
            horizontalLineTo(11f)
        }
    }.build().also { cachedHandshake = it }

/** `Handshake` vector, built on first access and reused afterwards. */
private var cachedHandshake: ImageVector? = null
