package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow big up icon.
 *
 * Converted from Lucide `arrow-big-up` (ISC).
 */
public val ArecaIcons.ArrowBlockUp: ImageVector
    get() = cachedArrowBlockUp ?: ImageVector.Builder(
        name = "ArrowBlockUp",
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
            moveTo(9f, 19f)
            arcTo(1f, 1f, 0f, false, false, 10f, 20f)
            horizontalLineTo(14f)
            arcTo(1f, 1f, 0f, false, false, 15f, 19f)
            verticalLineTo(13f)
            arcTo(1f, 1f, 0f, false, true, 16f, 12f)
            horizontalLineTo(19.293f)
            arcTo(0.707f, 0.707f, 0f, false, false, 19.793f, 10.793f)
            lineTo(12.707f, 3.707f)
            arcTo(1f, 1f, 0f, false, false, 11.293f, 3.707f)
            lineTo(4.207f, 10.793f)
            arcTo(0.707f, 0.707f, 0f, false, false, 4.707f, 12f)
            horizontalLineTo(8f)
            arcTo(1f, 1f, 0f, false, true, 9f, 13f)
            close()
        }
    }.build().also { cachedArrowBlockUp = it }

/** `ArrowBlockUp` vector, built on first access and reused afterwards. */
private var cachedArrowBlockUp: ImageVector? = null
