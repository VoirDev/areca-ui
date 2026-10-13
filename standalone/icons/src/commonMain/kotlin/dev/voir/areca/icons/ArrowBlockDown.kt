package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow big down icon.
 *
 * Converted from Lucide `arrow-big-down` (ISC).
 */
public val ArecaIcons.ArrowBlockDown: ImageVector
    get() = cachedArrowBlockDown ?: ImageVector.Builder(
        name = "ArrowBlockDown",
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
            moveTo(9f, 5f)
            arcTo(1f, 1f, 0f, false, true, 10f, 4f)
            horizontalLineTo(14f)
            arcTo(1f, 1f, 0f, false, true, 15f, 5f)
            verticalLineTo(11f)
            arcTo(1f, 1f, 0f, false, false, 16f, 12f)
            horizontalLineTo(19.293f)
            arcTo(0.707f, 0.707f, 0f, false, true, 19.793f, 13.207f)
            lineTo(12.707f, 20.293f)
            arcTo(1f, 1f, 0f, false, true, 11.293f, 20.293f)
            lineTo(4.207f, 13.207f)
            arcTo(0.707f, 0.707f, 0f, false, true, 4.707f, 12f)
            horizontalLineTo(8f)
            arcTo(1f, 1f, 0f, false, false, 9f, 11f)
            close()
        }
    }.build().also { cachedArrowBlockDown = it }

/** `ArrowBlockDown` vector, built on first access and reused afterwards. */
private var cachedArrowBlockDown: ImageVector? = null
