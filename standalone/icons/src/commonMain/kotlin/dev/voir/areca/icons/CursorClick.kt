package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mouse pointer click icon.
 *
 * Converted from Lucide `mouse-pointer-click` (ISC).
 */
public val ArecaIcons.CursorClick: ImageVector
    get() = cachedCursorClick ?: ImageVector.Builder(
        name = "CursorClick",
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
            moveTo(14f, 4.1f)
            lineTo(12f, 6f)
            moveTo(5.1f, 8f)
            lineTo(2.2f, 7.2f)
            moveTo(6f, 12f)
            lineTo(4.1f, 14f)
            moveTo(7.2f, 2.2f)
            lineTo(8f, 5.1f)
            moveTo(9.037f, 9.69f)
            arcTo(0.498f, 0.498f, 0f, false, true, 9.69f, 9.037f)
            lineTo(20.69f, 13.537f)
            arcTo(0.5f, 0.5f, 0f, false, true, 20.616f, 14.486f)
            lineTo(16.267f, 15.527f)
            arcTo(1f, 1f, 0f, false, false, 15.527f, 16.266f)
            lineTo(14.487f, 20.616f)
            arcTo(0.5f, 0.5f, 0f, false, true, 13.537f, 20.69f)
            close()
        }
    }.build().also { cachedCursorClick = it }

/** `CursorClick` vector, built on first access and reused afterwards. */
private var cachedCursorClick: ImageVector? = null
