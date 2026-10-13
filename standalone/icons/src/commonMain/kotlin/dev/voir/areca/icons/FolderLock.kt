package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Folder lock icon.
 *
 * Converted from Lucide `folder-lock` (ISC).
 */
public val ArecaIcons.FolderLock: ImageVector
    get() = cachedFolderLock ?: ImageVector.Builder(
        name = "FolderLock",
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
            moveTo(15f, 17f)
            horizontalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 22f, 18f)
            verticalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 21f, 22f)
            horizontalLineTo(15f)
            arcTo(1f, 1f, 0f, false, true, 14f, 21f)
            verticalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 15f, 17f)
            close()
            moveTo(10f, 20f)
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
            verticalLineTo(10.5f)
            moveTo(20f, 17f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, true, false, 16f, 15f)
            verticalLineTo(17f)
        }
    }.build().also { cachedFolderLock = it }

/** `FolderLock` vector, built on first access and reused afterwards. */
private var cachedFolderLock: ImageVector? = null
