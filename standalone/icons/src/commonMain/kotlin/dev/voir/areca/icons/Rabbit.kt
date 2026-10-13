package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rabbit icon.
 *
 * Converted from Lucide `rabbit` (ISC).
 */
public val ArecaIcons.Rabbit: ImageVector
    get() = cachedRabbit ?: ImageVector.Builder(
        name = "Rabbit",
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
            moveTo(13f, 16f)
            arcTo(3f, 3f, 0f, false, true, 15.24f, 21f)
            moveTo(18f, 12f)
            horizontalLineTo(18.01f)
            moveTo(18f, 21f)
            horizontalLineTo(10f)
            arcTo(4f, 4f, 0f, false, true, 6f, 17f)
            arcTo(7f, 7f, 0f, false, true, 13f, 10f)
            horizontalLineTo(13.2f)
            lineTo(9.6f, 6.4f)
            arcTo(1f, 1f, 0f, true, true, 12.4f, 3.6f)
            lineTo(15.8f, 7f)
            horizontalLineTo(16f)
            curveTo(19.3f, 7f, 22f, 9.7f, 22f, 13f)
            verticalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 20f, 16f)
            horizontalLineTo(19f)
            arcTo(3f, 3f, 0f, false, false, 16f, 19f)
            moveTo(20f, 8.54f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, true, false, 16f, 4f)
            verticalLineTo(7f)
            moveTo(7.612f, 12.524f)
            arcTo(3f, 3f, 0f, true, false, 6.012f, 16.824f)
        }
    }.build().also { cachedRabbit = it }

/** `Rabbit` vector, built on first access and reused afterwards. */
private var cachedRabbit: ImageVector? = null
