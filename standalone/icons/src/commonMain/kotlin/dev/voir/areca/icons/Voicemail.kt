package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Voicemail icon.
 *
 * Converted from Lucide `voicemail` (ISC).
 */
public val ArecaIcons.Voicemail: ImageVector
    get() = cachedVoicemail ?: ImageVector.Builder(
        name = "Voicemail",
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
            arcTo(4f, 4f, 0f, true, true, 10f, 12f)
            arcTo(4f, 4f, 0f, true, true, 2f, 12f)
            close()
            moveTo(14f, 12f)
            arcTo(4f, 4f, 0f, true, true, 22f, 12f)
            arcTo(4f, 4f, 0f, true, true, 14f, 12f)
            close()
            moveTo(6f, 16f)
            lineTo(18f, 16f)
        }
    }.build().also { cachedVoicemail = it }

/** `Voicemail` vector, built on first access and reused afterwards. */
private var cachedVoicemail: ImageVector? = null
