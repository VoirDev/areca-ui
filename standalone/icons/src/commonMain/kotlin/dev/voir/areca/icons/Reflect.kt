package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Triangles centerline dashed vertical icon.
 *
 * Converted from Lucide `triangles-centerline-dashed-vertical` (ISC).
 */
public val ArecaIcons.Reflect: ImageVector
    get() = cachedReflect ?: ImageVector.Builder(
        name = "Reflect",
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
            moveTo(12f, 14f)
            verticalLineTo(16f)
            moveTo(12f, 20f)
            verticalLineTo(22f)
            moveTo(12f, 2f)
            verticalLineTo(4f)
            moveTo(12f, 8f)
            verticalLineTo(10f)
            moveTo(20.288f, 16.703f)
            arcTo(1f, 1f, 0f, false, false, 22f, 16f)
            verticalLineTo(8f)
            arcTo(1f, 1f, 0f, false, false, 20.288f, 7.297f)
            lineTo(16.298f, 11.288f)
            arcTo(1f, 1f, 0f, false, false, 16.297f, 12.712f)
            close()
            moveTo(3.712f, 16.703f)
            arcTo(1f, 1f, 0f, false, true, 2f, 16f)
            verticalLineTo(8f)
            arcTo(1f, 1f, 0f, false, true, 3.712f, 7.297f)
            lineTo(7.702f, 11.288f)
            arcTo(1f, 1f, 0f, false, true, 7.703f, 12.712f)
            close()
        }
    }.build().also { cachedReflect = it }

/** `Reflect` vector, built on first access and reused afterwards. */
private var cachedReflect: ImageVector? = null
