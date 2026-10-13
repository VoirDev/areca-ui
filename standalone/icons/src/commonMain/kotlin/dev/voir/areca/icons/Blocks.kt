package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Blocks icon.
 *
 * Converted from Lucide `blocks` (ISC).
 */
public val ArecaIcons.Blocks: ImageVector
    get() = cachedBlocks ?: ImageVector.Builder(
        name = "Blocks",
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
            moveTo(10f, 22f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, false, 9f, 6f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 2f, 8f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 4f, 22f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, false, 18f, 20f)
            verticalLineTo(15f)
            arcTo(1f, 1f, 0f, false, false, 17f, 14f)
            horizontalLineTo(2f)
            moveTo(15f, 2f)
            horizontalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 22f, 3f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 21f, 10f)
            horizontalLineTo(15f)
            arcTo(1f, 1f, 0f, false, true, 14f, 9f)
            verticalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 15f, 2f)
            close()
        }
    }.build().also { cachedBlocks = it }

/** `Blocks` vector, built on first access and reused afterwards. */
private var cachedBlocks: ImageVector? = null
