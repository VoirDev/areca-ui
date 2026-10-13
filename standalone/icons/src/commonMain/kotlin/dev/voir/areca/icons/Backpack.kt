package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Backpack icon.
 *
 * Converted from Lucide `backpack` (ISC).
 */
public val ArecaIcons.Backpack: ImageVector
    get() = cachedBackpack ?: ImageVector.Builder(
        name = "Backpack",
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
            moveTo(4f, 10f)
            arcTo(4f, 4f, 0f, false, true, 8f, 6f)
            horizontalLineTo(16f)
            arcTo(4f, 4f, 0f, false, true, 20f, 10f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 20f)
            close()
            moveTo(8f, 10f)
            horizontalLineTo(16f)
            moveTo(8f, 18f)
            horizontalLineTo(16f)
            moveTo(8f, 22f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 10f, 14f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 16f, 16f)
            verticalLineTo(22f)
            moveTo(9f, 6f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 11f, 2f)
            horizontalLineTo(13f)
            arcTo(2f, 2f, 0f, false, true, 15f, 4f)
            verticalLineTo(6f)
        }
    }.build().also { cachedBackpack = it }

/** `Backpack` vector, built on first access and reused afterwards. */
private var cachedBackpack: ImageVector? = null
