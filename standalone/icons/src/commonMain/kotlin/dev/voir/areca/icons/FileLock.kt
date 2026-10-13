package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * File lock icon.
 *
 * Converted from Lucide `file-lock` (ISC).
 */
public val ArecaIcons.FileLock: ImageVector
    get() = cachedFileLock ?: ImageVector.Builder(
        name = "FileLock",
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
            moveTo(4f, 9.8f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 6f, 2f)
            horizontalLineTo(14f)
            arcTo(2.4f, 2.4f, 0f, false, true, 15.706f, 2.706f)
            lineTo(19.294f, 6.294f)
            arcTo(2.4f, 2.4f, 0f, false, true, 20f, 8f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            horizontalLineTo(15f)
            moveTo(14f, 2f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, false, 15f, 8f)
            horizontalLineTo(20f)
            moveTo(9f, 17f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, false, false, 5f, 15f)
            verticalLineTo(17f)
            moveTo(4f, 17f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 11f, 18f)
            verticalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 10f, 22f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 21f)
            verticalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 4f, 17f)
            close()
        }
    }.build().also { cachedFileLock = it }

/** `FileLock` vector, built on first access and reused afterwards. */
private var cachedFileLock: ImageVector? = null
