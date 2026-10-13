package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * File x icon.
 *
 * Converted from Lucide `file-x` (ISC).
 */
public val ArecaIcons.FileX: ImageVector
    get() = cachedFileX ?: ImageVector.Builder(
        name = "FileX",
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
            moveTo(6f, 22f)
            arcTo(2f, 2f, 0f, false, true, 4f, 20f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 6f, 2f)
            horizontalLineTo(14f)
            arcTo(2.4f, 2.4f, 0f, false, true, 15.704f, 2.706f)
            lineTo(19.292f, 6.294f)
            arcTo(2.4f, 2.4f, 0f, false, true, 20f, 8f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            close()
            moveTo(14f, 2f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, false, 15f, 8f)
            horizontalLineTo(20f)
            moveTo(14.5f, 12.5f)
            lineTo(9.5f, 17.5f)
            moveTo(9.5f, 12.5f)
            lineTo(14.5f, 17.5f)
        }
    }.build().also { cachedFileX = it }

/** `FileX` vector, built on first access and reused afterwards. */
private var cachedFileX: ImageVector? = null
