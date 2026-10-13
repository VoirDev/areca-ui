package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cable icon.
 *
 * Converted from Lucide `cable` (ISC).
 */
public val ArecaIcons.Cable: ImageVector
    get() = cachedCable ?: ImageVector.Builder(
        name = "Cable",
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
            moveTo(17f, 19f)
            arcTo(1f, 1f, 0f, false, true, 16f, 18f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 18f, 14f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 16f)
            verticalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 21f, 19f)
            close()
            moveTo(17f, 21f)
            verticalLineTo(19f)
            moveTo(19f, 14f)
            verticalLineTo(6.5f)
            arcTo(1f, 1f, 0f, false, false, 12f, 6.5f)
            verticalLineTo(17.5f)
            arcTo(1f, 1f, 0f, false, true, 5f, 17.5f)
            verticalLineTo(10f)
            moveTo(21f, 21f)
            verticalLineTo(19f)
            moveTo(3f, 5f)
            verticalLineTo(3f)
            moveTo(4f, 10f)
            arcTo(2f, 2f, 0f, false, true, 2f, 8f)
            verticalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 3f, 5f)
            horizontalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 8f, 6f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 6f, 10f)
            close()
            moveTo(7f, 5f)
            verticalLineTo(3f)
        }
    }.build().also { cachedCable = it }

/** `Cable` vector, built on first access and reused afterwards. */
private var cachedCable: ImageVector? = null
