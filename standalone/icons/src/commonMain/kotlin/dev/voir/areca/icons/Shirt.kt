package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shirt icon.
 *
 * Converted from Lucide `shirt` (ISC).
 */
public val ArecaIcons.Shirt: ImageVector
    get() = cachedShirt ?: ImageVector.Builder(
        name = "Shirt",
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
            moveTo(20.38f, 3.46f)
            lineTo(16f, 2f)
            arcTo(4f, 4f, 0f, false, true, 8f, 2f)
            lineTo(3.62f, 3.46f)
            arcTo(2f, 2f, 0f, false, false, 2.28f, 5.69f)
            lineTo(2.86f, 9.16f)
            arcTo(1f, 1f, 0f, false, false, 3.85f, 10f)
            horizontalLineTo(6f)
            verticalLineTo(20f)
            curveTo(6f, 21.1f, 6.9f, 22f, 8f, 22f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, false, 18f, 20f)
            verticalLineTo(10f)
            horizontalLineTo(20.15f)
            arcTo(1f, 1f, 0f, false, false, 21.14f, 9.16f)
            lineTo(21.72f, 5.69f)
            arcTo(2f, 2f, 0f, false, false, 20.38f, 3.46f)
            close()
        }
    }.build().also { cachedShirt = it }

/** `Shirt` vector, built on first access and reused afterwards. */
private var cachedShirt: ImageVector? = null
