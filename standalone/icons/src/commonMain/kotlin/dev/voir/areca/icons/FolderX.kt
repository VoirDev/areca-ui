package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Folder x icon.
 *
 * Converted from Lucide `folder-x` (ISC).
 */
public val ArecaIcons.FolderX: ImageVector
    get() = cachedFolderX ?: ImageVector.Builder(
        name = "FolderX",
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
            moveTo(20f, 20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 18f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, false, 20f, 6f)
            horizontalLineTo(12.1f)
            arcTo(2f, 2f, 0f, false, true, 10.41f, 5.1f)
            lineTo(9.6f, 3.9f)
            arcTo(2f, 2f, 0f, false, false, 7.93f, 3f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 2f, 5f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 4f, 20f)
            close()
            moveTo(9.5f, 10.5f)
            lineTo(14.5f, 15.5f)
            moveTo(14.5f, 10.5f)
            lineTo(9.5f, 15.5f)
        }
    }.build().also { cachedFolderX = it }

/** `FolderX` vector, built on first access and reused afterwards. */
private var cachedFolderX: ImageVector? = null
