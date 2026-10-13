package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Newspaper icon.
 *
 * Converted from Lucide `newspaper` (ISC).
 */
public val ArecaIcons.Newspaper: ImageVector
    get() = cachedNewspaper ?: ImageVector.Builder(
        name = "Newspaper",
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
            moveTo(15f, 18f)
            horizontalLineTo(10f)
            moveTo(18f, 14f)
            horizontalLineTo(10f)
            moveTo(4f, 22f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 20f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 20f, 2f)
            horizontalLineTo(8f)
            arcTo(2f, 2f, 0f, false, false, 6f, 4f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 2f, 20f)
            verticalLineTo(11f)
            arcTo(2f, 2f, 0f, false, true, 4f, 9f)
            horizontalLineTo(6f)
            moveTo(11f, 6f)
            horizontalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 18f, 7f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 17f, 10f)
            horizontalLineTo(11f)
            arcTo(1f, 1f, 0f, false, true, 10f, 9f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 11f, 6f)
            close()
        }
    }.build().also { cachedNewspaper = it }

/** `Newspaper` vector, built on first access and reused afterwards. */
private var cachedNewspaper: ImageVector? = null
