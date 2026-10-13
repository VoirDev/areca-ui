package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pc case icon.
 *
 * Converted from Lucide `pc-case` (ISC).
 */
public val ArecaIcons.PcCase: ImageVector
    get() = cachedPcCase ?: ImageVector.Builder(
        name = "PcCase",
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
            moveTo(7f, 2f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 19f, 4f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 17f, 22f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 5f, 20f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 7f, 2f)
            close()
            moveTo(15f, 14f)
            horizontalLineTo(15.01f)
            moveTo(9f, 6f)
            horizontalLineTo(15f)
            moveTo(9f, 10f)
            horizontalLineTo(15f)
        }
    }.build().also { cachedPcCase = it }

/** `PcCase` vector, built on first access and reused afterwards. */
private var cachedPcCase: ImageVector? = null
