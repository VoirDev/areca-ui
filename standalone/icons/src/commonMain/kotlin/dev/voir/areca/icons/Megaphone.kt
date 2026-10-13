package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Megaphone icon.
 *
 * Converted from Lucide `megaphone` (ISC).
 */
public val ArecaIcons.Megaphone: ImageVector
    get() = cachedMegaphone ?: ImageVector.Builder(
        name = "Megaphone",
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
            moveTo(11f, 6f)
            arcTo(13f, 13f, 0f, false, false, 19.4f, 3.2f)
            arcTo(1f, 1f, 0f, false, true, 21f, 4f)
            verticalLineTo(16f)
            arcTo(1f, 1f, 0f, false, true, 19.4f, 16.8f)
            arcTo(13f, 13f, 0f, false, false, 11f, 14f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 12f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 5f, 6f)
            close()
            moveTo(6f, 14f)
            arcTo(12f, 12f, 0f, false, false, 8.4f, 21.2f)
            arcTo(2f, 2f, 0f, false, false, 11.6f, 18.8f)
            arcTo(8f, 8f, 0f, false, true, 10f, 14f)
            moveTo(8f, 6f)
            verticalLineTo(14f)
        }
    }.build().also { cachedMegaphone = it }

/** `Megaphone` vector, built on first access and reused afterwards. */
private var cachedMegaphone: ImageVector? = null
