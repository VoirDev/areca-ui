package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Send icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `send` (ISC).
 */
public val ArecaIcons.Send: ImageVector
    get() = cachedSend ?: ImageVector.Builder(
        name = "Send",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(14.536f, 21.686f)
            arcTo(0.5f, 0.5f, 0f, false, false, 15.473f, 21.662f)
            lineTo(21.973f, 2.662f)
            arcTo(0.496f, 0.496f, 0f, false, false, 21.338f, 2.027f)
            lineTo(2.338f, 8.527f)
            arcTo(0.5f, 0.5f, 0f, false, false, 2.314f, 9.464f)
            lineTo(10.244f, 12.644f)
            arcTo(2f, 2f, 0f, false, true, 11.356f, 13.754f)
            close()
            moveTo(21.854f, 2.147f)
            lineTo(10.914f, 13.086f)
        }
    }.build().also { cachedSend = it }

/** `Send` vector, built on first access and reused afterwards. */
private var cachedSend: ImageVector? = null
