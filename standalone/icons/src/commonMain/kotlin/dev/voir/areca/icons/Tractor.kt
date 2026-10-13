package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tractor icon.
 *
 * Converted from Lucide `tractor` (ISC).
 */
public val ArecaIcons.Tractor: ImageVector
    get() = cachedTractor ?: ImageVector.Builder(
        name = "Tractor",
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
            moveTo(10f, 11f)
            lineTo(21f, 11.9f)
            arcTo(1f, 1f, 0f, false, true, 21.8f, 13f)
            lineTo(21.135f, 17.158f)
            arcTo(1f, 1f, 0f, false, true, 20.147f, 18f)
            horizontalLineTo(20f)
            moveTo(16f, 18f)
            horizontalLineTo(11f)
            moveTo(18f, 5f)
            arcTo(1f, 1f, 0f, false, false, 17f, 6f)
            verticalLineTo(11.573f)
            moveTo(3f, 4f)
            horizontalLineTo(11.129f)
            arcTo(1f, 1f, 0f, false, true, 12.119f, 4.863f)
            lineTo(13f, 11.246f)
            moveTo(4f, 11f)
            verticalLineTo(4f)
            moveTo(7f, 15f)
            horizontalLineTo(7.01f)
            moveTo(8f, 10.1f)
            verticalLineTo(4f)
            moveTo(16f, 18f)
            arcTo(2f, 2f, 0f, true, true, 20f, 18f)
            arcTo(2f, 2f, 0f, true, true, 16f, 18f)
            close()
            moveTo(2f, 15f)
            arcTo(5f, 5f, 0f, true, true, 12f, 15f)
            arcTo(5f, 5f, 0f, true, true, 2f, 15f)
            close()
        }
    }.build().also { cachedTractor = it }

/** `Tractor` vector, built on first access and reused afterwards. */
private var cachedTractor: ImageVector? = null
