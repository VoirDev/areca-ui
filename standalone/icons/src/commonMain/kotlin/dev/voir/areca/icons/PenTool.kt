package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pen tool icon.
 *
 * Converted from Lucide `pen-tool` (ISC).
 */
public val ArecaIcons.PenTool: ImageVector
    get() = cachedPenTool ?: ImageVector.Builder(
        name = "PenTool",
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
            moveTo(18f, 13f)
            lineTo(17.346f, 8.264f)
            arcTo(4f, 4f, 0f, false, false, 14.59f, 4.998f)
            lineTo(5.427f, 2.098f)
            arcTo(2f, 2f, 0f, false, false, 3.41f, 2.591f)
            lineTo(2.59f, 3.41f)
            arcTo(2f, 2f, 0f, false, false, 2.098f, 5.427f)
            lineTo(4.998f, 14.59f)
            arcTo(4f, 4f, 0f, false, false, 8.264f, 17.346f)
            lineTo(13f, 18f)
            moveTo(18.648f, 12.352f)
            arcTo(1.205f, 1.205f, 0f, false, true, 20.352f, 12.352f)
            lineTo(21.648f, 13.648f)
            arcTo(1.205f, 1.205f, 0f, false, true, 21.648f, 15.352f)
            lineTo(15.352f, 21.648f)
            arcTo(1.205f, 1.205f, 0f, false, true, 13.648f, 21.648f)
            lineTo(12.352f, 20.352f)
            arcTo(1.205f, 1.205f, 0f, false, true, 12.352f, 18.648f)
            close()
            moveTo(3f, 3f)
            lineTo(9.586f, 9.586f)
            moveTo(9f, 11f)
            arcTo(2f, 2f, 0f, true, true, 13f, 11f)
            arcTo(2f, 2f, 0f, true, true, 9f, 11f)
            close()
        }
    }.build().also { cachedPenTool = it }

/** `PenTool` vector, built on first access and reused afterwards. */
private var cachedPenTool: ImageVector? = null
