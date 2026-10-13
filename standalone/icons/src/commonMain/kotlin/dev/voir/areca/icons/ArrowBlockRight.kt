package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow big right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `arrow-big-right` (ISC).
 */
public val ArecaIcons.ArrowBlockRight: ImageVector
    get() = cachedArrowBlockRight ?: ImageVector.Builder(
        name = "ArrowBlockRight",
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
            moveTo(13.207f, 19.793f)
            arcTo(0.707f, 0.707f, 0f, false, true, 12f, 19.293f)
            verticalLineTo(16f)
            arcTo(1f, 1f, 0f, false, false, 11f, 15f)
            horizontalLineTo(5f)
            arcTo(1f, 1f, 0f, false, true, 4f, 14f)
            verticalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 5f, 9f)
            horizontalLineTo(11f)
            arcTo(1f, 1f, 0f, false, false, 12f, 8f)
            verticalLineTo(4.707f)
            arcTo(0.707f, 0.707f, 0f, false, true, 13.207f, 4.207f)
            lineTo(20.147f, 11.147f)
            arcTo(1.207f, 1.207f, 0f, false, true, 20.147f, 12.854f)
            close()
        }
    }.build().also { cachedArrowBlockRight = it }

/** `ArrowBlockRight` vector, built on first access and reused afterwards. */
private var cachedArrowBlockRight: ImageVector? = null
