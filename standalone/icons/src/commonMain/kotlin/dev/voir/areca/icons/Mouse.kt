package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mouse icon.
 *
 * Converted from Lucide `mouse` (ISC).
 */
public val ArecaIcons.Mouse: ImageVector
    get() = cachedMouse ?: ImageVector.Builder(
        name = "Mouse",
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
            moveTo(12f, 2f)
            horizontalLineTo(12f)
            arcTo(7f, 7f, 0f, false, true, 19f, 9f)
            verticalLineTo(15f)
            arcTo(7f, 7f, 0f, false, true, 12f, 22f)
            horizontalLineTo(12f)
            arcTo(7f, 7f, 0f, false, true, 5f, 15f)
            verticalLineTo(9f)
            arcTo(7f, 7f, 0f, false, true, 12f, 2f)
            close()
            moveTo(12f, 6f)
            verticalLineTo(10f)
        }
    }.build().also { cachedMouse = it }

/** `Mouse` vector, built on first access and reused afterwards. */
private var cachedMouse: ImageVector? = null
