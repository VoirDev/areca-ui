package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Amphora icon.
 *
 * Converted from Lucide `amphora` (ISC).
 */
public val ArecaIcons.Amphora: ImageVector
    get() = cachedAmphora ?: ImageVector.Builder(
        name = "Amphora",
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
            verticalLineTo(7.632f)
            curveTo(10f, 8.056f, 9.728f, 8.427f, 9.347f, 8.614f)
            arcTo(6f, 6f, 0f, false, false, 6f, 14f)
            curveTo(6.006f, 18f, 9f, 21f, 11f, 22f)
            moveTo(10f, 5f)
            horizontalLineTo(8f)
            arcTo(2f, 2f, 0f, false, false, 8f, 9f)
            horizontalLineTo(8.68f)
            moveTo(14f, 2f)
            verticalLineTo(7.632f)
            curveTo(14f, 8.056f, 14.272f, 8.427f, 14.652f, 8.614f)
            arcTo(6f, 6f, 0f, false, true, 18f, 14f)
            curveTo(18f, 18f, 15f, 21f, 13f, 22f)
            moveTo(14f, 5f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 16f, 9f)
            horizontalLineTo(15.32f)
            moveTo(18f, 22f)
            horizontalLineTo(6f)
            moveTo(9f, 2f)
            horizontalLineTo(15f)
        }
    }.build().also { cachedAmphora = it }

/** `Amphora` vector, built on first access and reused afterwards. */
private var cachedAmphora: ImageVector? = null
