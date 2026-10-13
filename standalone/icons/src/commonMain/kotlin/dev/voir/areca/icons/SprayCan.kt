package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Spray can icon.
 *
 * Converted from Lucide `spray-can` (ISC).
 */
public val ArecaIcons.SprayCan: ImageVector
    get() = cachedSprayCan ?: ImageVector.Builder(
        name = "SprayCan",
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
            moveTo(3f, 3f)
            horizontalLineTo(3.01f)
            moveTo(7f, 5f)
            horizontalLineTo(7.01f)
            moveTo(11f, 7f)
            horizontalLineTo(11.01f)
            moveTo(3f, 7f)
            horizontalLineTo(3.01f)
            moveTo(7f, 9f)
            horizontalLineTo(7.01f)
            moveTo(3f, 11f)
            horizontalLineTo(3.01f)
            moveTo(15f, 5f)
            horizontalLineTo(19f)
            verticalLineTo(9f)
            horizontalLineTo(15f)
            close()
            moveTo(19f, 9f)
            lineTo(21f, 11f)
            verticalLineTo(21f)
            curveTo(21f, 21.6f, 20.6f, 22f, 20f, 22f)
            horizontalLineTo(14f)
            curveTo(13.4f, 22f, 13f, 21.6f, 13f, 21f)
            verticalLineTo(11f)
            lineTo(15f, 9f)
            moveTo(13f, 14f)
            lineTo(21f, 12f)
            moveTo(13f, 19f)
            lineTo(21f, 17f)
        }
    }.build().also { cachedSprayCan = it }

/** `SprayCan` vector, built on first access and reused afterwards. */
private var cachedSprayCan: ImageVector? = null
