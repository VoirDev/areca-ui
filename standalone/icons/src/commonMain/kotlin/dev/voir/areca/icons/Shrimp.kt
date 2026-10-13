package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shrimp icon.
 *
 * Converted from Lucide `shrimp` (ISC).
 */
public val ArecaIcons.Shrimp: ImageVector
    get() = cachedShrimp ?: ImageVector.Builder(
        name = "Shrimp",
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
            moveTo(10f, 2f)
            arcTo(3.28f, 3.28f, 0f, false, false, 13.227f, 3.798f)
            lineTo(19.397f, 3.237f)
            arcTo(1f, 1f, 0f, true, true, 19.614f, 8f)
            horizontalLineTo(8.5f)
            arcTo(6.44f, 6.44f, 0f, false, false, 2.87f, 17.75f)
            arcTo(6.5f, 6.5f, 0f, false, false, 8.5f, 21f)
            curveTo(9.88f, 21f, 10.5f, 20.5f, 11f, 20f)
            moveTo(10f, 8f)
            arcTo(8.5f, 8.5f, 0f, false, false, 10f, 16f)
            moveTo(11f, 22f)
            curveTo(10.5f, 21.5f, 9.88f, 21f, 8.5f, 21f)
            arcTo(1f, 1f, 0f, false, true, 8.5f, 16f)
            horizontalLineTo(12f)
            arcTo(7f, 7f, 0f, false, false, 19f, 9f)
            verticalLineTo(8f)
            moveTo(13f, 12f)
            horizontalLineTo(13.01f)
            moveTo(8f, 16f)
            curveTo(6f, 16f, 3.5f, 12f, 4f, 10f)
        }
    }.build().also { cachedShrimp = it }

/** `Shrimp` vector, built on first access and reused afterwards. */
private var cachedShrimp: ImageVector? = null
