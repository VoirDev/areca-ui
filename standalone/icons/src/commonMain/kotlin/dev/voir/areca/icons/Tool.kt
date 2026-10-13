package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tool icon.
 *
 * Converted from Tabler Icons `tool` (MIT).
 */
public val ArecaIcons.Tool: ImageVector
    get() = cachedTool ?: ImageVector.Builder(
        name = "Tool",
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
            moveTo(7f, 10f)
            horizontalLineTo(10f)
            verticalLineTo(7f)
            lineTo(6.5f, 3.5f)
            arcTo(6f, 6f, 0f, false, true, 14.5f, 11.5f)
            lineTo(20.5f, 17.5f)
            arcTo(2f, 2f, 0f, false, true, 17.5f, 20.5f)
            lineTo(11.5f, 14.5f)
            arcTo(6f, 6f, 0f, false, true, 3.5f, 6.5f)
            lineTo(7f, 10f)
        }
    }.build().also { cachedTool = it }

/** `Tool` vector, built on first access and reused afterwards. */
private var cachedTool: ImageVector? = null
