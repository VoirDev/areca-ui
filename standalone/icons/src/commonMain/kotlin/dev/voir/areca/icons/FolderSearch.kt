package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Folder search icon.
 *
 * Converted from Lucide `folder-search` (ISC).
 */
public val ArecaIcons.FolderSearch: ImageVector
    get() = cachedFolderSearch ?: ImageVector.Builder(
        name = "FolderSearch",
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
            moveTo(10.7f, 20f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 18f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 4f, 3f)
            horizontalLineTo(7.9f)
            arcTo(2f, 2f, 0f, false, true, 9.59f, 3.9f)
            lineTo(10.4f, 5.1f)
            arcTo(2f, 2f, 0f, false, false, 12.07f, 6f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 8f)
            verticalLineTo(12.1f)
            moveTo(21f, 21f)
            lineTo(19.1f, 19.1f)
            moveTo(14f, 17f)
            arcTo(3f, 3f, 0f, true, true, 20f, 17f)
            arcTo(3f, 3f, 0f, true, true, 14f, 17f)
            close()
        }
    }.build().also { cachedFolderSearch = it }

/** `FolderSearch` vector, built on first access and reused afterwards. */
private var cachedFolderSearch: ImageVector? = null
