package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tools kitchen 2 icon.
 *
 * Converted from Tabler Icons `tools-kitchen-2` (MIT).
 */
public val ArecaIcons.ToolsKitchen2: ImageVector
    get() = cachedToolsKitchen2 ?: ImageVector.Builder(
        name = "ToolsKitchen2",
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
            moveTo(19f, 3f)
            verticalLineTo(15f)
            horizontalLineTo(14f)
            curveTo(13.977f, 11.319f, 14.184f, 7.594f, 19f, 3f)
            moveTo(19f, 15f)
            verticalLineTo(21f)
            horizontalLineTo(18f)
            verticalLineTo(18f)
            moveTo(8f, 4f)
            verticalLineTo(21f)
            moveTo(5f, 4f)
            verticalLineTo(7f)
            arcTo(3f, 3f, 0f, true, false, 11f, 7f)
            verticalLineTo(4f)
        }
    }.build().also { cachedToolsKitchen2 = it }

/** `ToolsKitchen2` vector, built on first access and reused afterwards. */
private var cachedToolsKitchen2: ImageVector? = null
