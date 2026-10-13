package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sticker icon.
 *
 * Converted from Lucide `sticker` (ISC).
 */
public val ArecaIcons.StickerSquare: ImageVector
    get() = cachedStickerSquare ?: ImageVector.Builder(
        name = "StickerSquare",
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
            moveTo(21f, 9f)
            arcTo(2.4f, 2.4f, 0f, false, false, 20.294f, 7.294f)
            lineTo(16.706f, 3.706f)
            arcTo(2.4f, 2.4f, 0f, false, false, 15f, 3f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 3f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 5f, 21f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 21f, 19f)
            close()
            moveTo(15f, 3f)
            verticalLineTo(8f)
            arcTo(1f, 1f, 0f, false, false, 16f, 9f)
            horizontalLineTo(21f)
            moveTo(8f, 13f)
            horizontalLineTo(8.01f)
            moveTo(16f, 13f)
            horizontalLineTo(16.01f)
            moveTo(10f, 16f)
            reflectiveCurveTo(10.8f, 17f, 12f, 17f)
            curveTo(13.3f, 17f, 14f, 16f, 14f, 16f)
        }
    }.build().also { cachedStickerSquare = it }

/** `StickerSquare` vector, built on first access and reused afterwards. */
private var cachedStickerSquare: ImageVector? = null
