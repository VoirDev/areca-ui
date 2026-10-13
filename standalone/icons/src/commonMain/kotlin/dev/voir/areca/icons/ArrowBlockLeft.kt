package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow big left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `arrow-big-left` (ISC).
 */
public val ArecaIcons.ArrowBlockLeft: ImageVector
    get() = cachedArrowBlockLeft ?: ImageVector.Builder(
        name = "ArrowBlockLeft",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(10.793f, 19.793f)
            arcTo(0.707f, 0.707f, 0f, false, false, 12f, 19.293f)
            verticalLineTo(16f)
            arcTo(1f, 1f, 0f, false, true, 13f, 15f)
            horizontalLineTo(19f)
            arcTo(1f, 1f, 0f, false, false, 20f, 14f)
            verticalLineTo(10f)
            arcTo(1f, 1f, 0f, false, false, 19f, 9f)
            horizontalLineTo(13f)
            arcTo(1f, 1f, 0f, false, true, 12f, 8f)
            verticalLineTo(4.707f)
            arcTo(0.707f, 0.707f, 0f, false, false, 10.793f, 4.207f)
            lineTo(3.853f, 11.147f)
            arcTo(1.207f, 1.207f, 0f, false, false, 3.853f, 12.854f)
            close()
        }
    }.build().also { cachedArrowBlockLeft = it }

/** `ArrowBlockLeft` vector, built on first access and reused afterwards. */
private var cachedArrowBlockLeft: ImageVector? = null
