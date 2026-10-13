package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Medal icon.
 *
 * Converted from Lucide `medal` (ISC).
 */
public val ArecaIcons.Medal: ImageVector
    get() = cachedMedal ?: ImageVector.Builder(
        name = "Medal",
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
            moveTo(7.21f, 15f)
            lineTo(2.66f, 7.14f)
            arcTo(2f, 2f, 0f, false, true, 2.79f, 4.94f)
            lineTo(4.4f, 2.8f)
            arcTo(2f, 2f, 0f, false, true, 6f, 2f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 19.6f, 2.8f)
            lineTo(21.2f, 4.94f)
            arcTo(2f, 2f, 0f, false, true, 21.34f, 7.14f)
            lineTo(16.79f, 15f)
            moveTo(11f, 12f)
            lineTo(5.12f, 2.2f)
            moveTo(13f, 12f)
            lineTo(18.88f, 2.2f)
            moveTo(8f, 7f)
            horizontalLineTo(16f)
            moveTo(7f, 17f)
            arcTo(5f, 5f, 0f, true, true, 17f, 17f)
            arcTo(5f, 5f, 0f, true, true, 7f, 17f)
            close()
            moveTo(12f, 18f)
            verticalLineTo(16f)
            horizontalLineTo(11.5f)
        }
    }.build().also { cachedMedal = it }

/** `Medal` vector, built on first access and reused afterwards. */
private var cachedMedal: ImageVector? = null
