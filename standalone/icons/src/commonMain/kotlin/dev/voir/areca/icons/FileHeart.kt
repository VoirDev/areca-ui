package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * File heart icon.
 *
 * Converted from Lucide `file-heart` (ISC).
 */
public val ArecaIcons.FileHeart: ImageVector
    get() = cachedFileHeart ?: ImageVector.Builder(
        name = "FileHeart",
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
            moveTo(13f, 22f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 20f, 20f)
            verticalLineTo(8f)
            arcTo(2.4f, 2.4f, 0f, false, false, 19.294f, 6.294f)
            lineTo(15.706f, 2.706f)
            arcTo(2.4f, 2.4f, 0f, false, false, 14f, 2f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 4f, 4f)
            verticalLineTo(11f)
            moveTo(14f, 2f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, false, 15f, 8f)
            horizontalLineTo(20f)
            moveTo(3.62f, 18.8f)
            arcTo(2.25f, 2.25f, 0f, true, true, 7f, 15.836f)
            arcTo(2.25f, 2.25f, 0f, true, true, 10.38f, 18.802f)
            lineTo(7.754f, 21.658f)
            arcTo(1f, 1f, 0f, false, true, 6.247f, 21.658f)
            close()
        }
    }.build().also { cachedFileHeart = it }

/** `FileHeart` vector, built on first access and reused afterwards. */
private var cachedFileHeart: ImageVector? = null
