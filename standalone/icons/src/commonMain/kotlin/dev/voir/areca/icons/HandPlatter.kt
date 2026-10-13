package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hand platter icon.
 *
 * Converted from Lucide `hand-platter` (ISC).
 */
public val ArecaIcons.HandPlatter: ImageVector
    get() = cachedHandPlatter ?: ImageVector.Builder(
        name = "HandPlatter",
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
            moveTo(12f, 3f)
            verticalLineTo(2f)
            moveTo(15.4f, 17.4f)
            lineTo(18.6f, 14.6f)
            arcTo(2f, 2f, 0f, true, true, 21.4f, 17.5f)
            lineTo(17.8f, 20.8f)
            curveTo(17.1f, 21.6f, 16.1f, 22f, 15f, 22f)
            horizontalLineTo(11f)
            curveTo(9.9f, 22f, 8.9f, 21.6f, 8.2f, 20.8f)
            lineTo(6.898f, 19.336f)
            arcTo(1f, 1f, 0f, false, false, 6.151f, 19f)
            horizontalLineTo(5f)
            moveTo(2f, 14f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 14f, 18f)
            horizontalLineTo(12f)
            moveTo(4f, 10f)
            horizontalLineTo(20f)
            moveTo(5f, 10f)
            arcTo(7f, 7f, 0f, false, true, 19f, 10f)
            moveTo(5f, 14f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 4f, 21f)
            horizontalLineTo(2f)
        }
    }.build().also { cachedHandPlatter = it }

/** `HandPlatter` vector, built on first access and reused afterwards. */
private var cachedHandPlatter: ImageVector? = null
