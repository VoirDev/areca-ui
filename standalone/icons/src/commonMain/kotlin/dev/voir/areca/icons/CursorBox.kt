package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Square mouse pointer icon.
 *
 * Converted from Lucide `square-mouse-pointer` (ISC).
 */
public val ArecaIcons.CursorBox: ImageVector
    get() = cachedCursorBox ?: ImageVector.Builder(
        name = "CursorBox",
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
            moveTo(12.034f, 12.681f)
            arcTo(0.498f, 0.498f, 0f, false, true, 12.681f, 12.034f)
            lineTo(21.681f, 15.534f)
            arcTo(0.5f, 0.5f, 0f, false, true, 21.648f, 16.477f)
            lineTo(18.204f, 17.545f)
            arcTo(1f, 1f, 0f, false, false, 17.544f, 18.205f)
            lineTo(16.477f, 21.648f)
            arcTo(0.5f, 0.5f, 0f, false, true, 15.534f, 21.681f)
            close()
            moveTo(21f, 11f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 19f, 3f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 3f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 5f, 21f)
            horizontalLineTo(11f)
        }
    }.build().also { cachedCursorBox = it }

/** `CursorBox` vector, built on first access and reused afterwards. */
private var cachedCursorBox: ImageVector? = null
