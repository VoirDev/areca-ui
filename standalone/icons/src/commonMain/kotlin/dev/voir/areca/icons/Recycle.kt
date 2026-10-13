package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Recycle icon.
 *
 * Converted from Lucide `recycle` (ISC).
 */
public val ArecaIcons.Recycle: ImageVector
    get() = cachedRecycle ?: ImageVector.Builder(
        name = "Recycle",
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
            moveTo(7f, 19f)
            horizontalLineTo(4.815f)
            arcTo(1.83f, 1.83f, 0f, false, true, 3.245f, 18.119f)
            arcTo(1.785f, 1.785f, 0f, false, true, 3.241f, 16.335f)
            lineTo(7.196f, 9.5f)
            moveTo(11f, 19f)
            horizontalLineTo(19.203f)
            arcTo(1.83f, 1.83f, 0f, false, false, 20.759f, 18.11f)
            arcTo(1.784f, 1.784f, 0f, false, false, 20.759f, 16.335f)
            lineTo(19.533f, 14.215f)
            moveTo(14f, 16f)
            lineTo(11f, 19f)
            lineTo(14f, 22f)
            moveTo(8.293f, 13.596f)
            lineTo(7.196f, 9.5f)
            lineTo(3.1f, 10.598f)
            moveTo(9.344f, 5.811f)
            lineTo(10.437f, 3.919f)
            arcTo(1.83f, 1.83f, 0f, false, true, 11.985f, 3f)
            arcTo(1.784f, 1.784f, 0f, false, true, 13.531f, 3.888f)
            lineTo(17.474f, 10.731f)
            moveTo(13.378f, 9.633f)
            lineTo(17.474f, 10.731f)
            lineTo(18.571f, 6.635f)
        }
    }.build().also { cachedRecycle = it }

/** `Recycle` vector, built on first access and reused afterwards. */
private var cachedRecycle: ImageVector? = null
