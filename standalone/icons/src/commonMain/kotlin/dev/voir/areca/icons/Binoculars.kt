package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Binoculars icon.
 *
 * Converted from Lucide `binoculars` (ISC).
 */
public val ArecaIcons.Binoculars: ImageVector
    get() = cachedBinoculars ?: ImageVector.Builder(
        name = "Binoculars",
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
            moveTo(10f, 10f)
            horizontalLineTo(14f)
            moveTo(19f, 7f)
            verticalLineTo(4f)
            arcTo(1f, 1f, 0f, false, false, 18f, 3f)
            horizontalLineTo(16f)
            arcTo(1f, 1f, 0f, false, false, 15f, 4f)
            verticalLineTo(7f)
            moveTo(20f, 21f)
            arcTo(2f, 2f, 0f, false, false, 22f, 19f)
            verticalLineTo(15.149f)
            curveTo(22f, 13.759f, 20f, 12.187f, 20f, 10.32f)
            verticalLineTo(8f)
            arcTo(1f, 1f, 0f, false, false, 19f, 7f)
            horizontalLineTo(15f)
            arcTo(1f, 1f, 0f, false, false, 14f, 8f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 16f, 21f)
            close()
            moveTo(22f, 16f)
            lineTo(2f, 16f)
            moveTo(4f, 21f)
            arcTo(2f, 2f, 0f, false, true, 2f, 19f)
            verticalLineTo(15.149f)
            curveTo(2f, 13.759f, 4f, 12.187f, 4f, 10.32f)
            verticalLineTo(8f)
            arcTo(1f, 1f, 0f, false, true, 5f, 7f)
            horizontalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 10f, 8f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 8f, 21f)
            close()
            moveTo(9f, 7f)
            verticalLineTo(4f)
            arcTo(1f, 1f, 0f, false, false, 8f, 3f)
            horizontalLineTo(6f)
            arcTo(1f, 1f, 0f, false, false, 5f, 4f)
            verticalLineTo(7f)
        }
    }.build().also { cachedBinoculars = it }

/** `Binoculars` vector, built on first access and reused afterwards. */
private var cachedBinoculars: ImageVector? = null
