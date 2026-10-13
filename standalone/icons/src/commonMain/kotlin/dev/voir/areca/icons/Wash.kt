package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wash icon.
 *
 * Converted from Tabler Icons `wash` (MIT).
 */
public val ArecaIcons.Wash: ImageVector
    get() = cachedWash ?: ImageVector.Builder(
        name = "Wash",
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
            moveTo(3.486f, 8.965f)
            curveTo(3.654f, 8.985f, 3.826f, 8.998f, 4f, 9f)
            curveTo(4.79f, 9.009f, 5.539f, 8.822f, 6f, 8.5f)
            curveTo(6.461f, 8.18f, 7.21f, 7.993f, 8f, 8f)
            curveTo(8.79f, 7.993f, 9.539f, 8.18f, 10f, 8.5f)
            curveTo(10.461f, 8.822f, 11.21f, 9.009f, 12f, 9f)
            curveTo(12.79f, 9.009f, 13.539f, 8.822f, 14f, 8.5f)
            curveTo(14.461f, 8.18f, 15.21f, 7.993f, 16f, 8f)
            curveTo(16.79f, 7.993f, 17.539f, 8.18f, 18f, 8.5f)
            curveTo(18.461f, 8.822f, 19.21f, 9.009f, 20f, 9f)
            curveTo(20.17f, 9f, 20.339f, 8.986f, 20.503f, 8.966f)
            moveTo(3f, 6f)
            lineTo(4.721f, 16.329f)
            arcTo(2f, 2f, 0f, false, false, 6.694f, 18f)
            horizontalLineTo(17.306f)
            arcTo(2f, 2f, 0f, false, false, 19.279f, 16.329f)
            lineTo(21f, 6f)
        }
    }.build().also { cachedWash = it }

/** `Wash` vector, built on first access and reused afterwards. */
private var cachedWash: ImageVector? = null
