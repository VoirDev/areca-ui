package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Microphone 2 icon.
 *
 * Converted from Tabler Icons `microphone-2` (MIT).
 */
public val ArecaIcons.Microphone2: ImageVector
    get() = cachedMicrophone2 ?: ImageVector.Builder(
        name = "Microphone2",
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
            moveTo(15f, 12.9f)
            arcTo(5f, 5f, 0f, true, false, 11.098f, 9f)
            moveTo(15f, 12.9f)
            lineTo(11.098f, 9.001f)
            lineTo(3.585f, 17.585f)
            arcTo(2f, 2f, 0f, true, false, 6.412f, 20.415f)
            lineTo(15f, 12.9f)
        }
    }.build().also { cachedMicrophone2 = it }

/** `Microphone2` vector, built on first access and reused afterwards. */
private var cachedMicrophone2: ImageVector? = null
