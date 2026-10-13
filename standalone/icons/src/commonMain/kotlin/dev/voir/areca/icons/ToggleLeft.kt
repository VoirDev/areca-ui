package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Toggle left icon.
 *
 * Converted from Lucide `toggle-left` (ISC).
 */
public val ArecaIcons.ToggleLeft: ImageVector
    get() = cachedToggleLeft ?: ImageVector.Builder(
        name = "ToggleLeft",
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
            moveTo(6f, 12f)
            arcTo(3f, 3f, 0f, true, true, 12f, 12f)
            arcTo(3f, 3f, 0f, true, true, 6f, 12f)
            close()
            moveTo(9f, 5f)
            horizontalLineTo(15f)
            arcTo(7f, 7f, 0f, false, true, 22f, 12f)
            verticalLineTo(12f)
            arcTo(7f, 7f, 0f, false, true, 15f, 19f)
            horizontalLineTo(9f)
            arcTo(7f, 7f, 0f, false, true, 2f, 12f)
            verticalLineTo(12f)
            arcTo(7f, 7f, 0f, false, true, 9f, 5f)
            close()
        }
    }.build().also { cachedToggleLeft = it }

/** `ToggleLeft` vector, built on first access and reused afterwards. */
private var cachedToggleLeft: ImageVector? = null
