package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hammer icon.
 *
 * Converted from Lucide `hammer` (ISC).
 */
public val ArecaIcons.Hammer: ImageVector
    get() = cachedHammer ?: ImageVector.Builder(
        name = "Hammer",
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
            moveTo(15f, 12f)
            lineTo(5.627f, 21.373f)
            arcTo(1f, 1f, 0f, false, true, 2.626f, 18.373f)
            lineTo(12f, 9f)
            moveTo(18f, 15f)
            lineTo(22f, 11f)
            moveTo(21.5f, 11.5f)
            lineTo(19.586f, 9.586f)
            arcTo(2f, 2f, 0f, false, true, 19f, 8.172f)
            verticalLineTo(7.828f)
            arcTo(2f, 2f, 0f, false, false, 18.414f, 6.414f)
            lineTo(16.757f, 4.757f)
            arcTo(6f, 6f, 0f, false, false, 12.516f, 3f)
            horizontalLineTo(9f)
            lineTo(10.243f, 4.243f)
            arcTo(6f, 6f, 0f, false, true, 12f, 8.485f)
            verticalLineTo(10f)
            lineTo(14f, 12f)
            horizontalLineTo(15.172f)
            arcTo(2f, 2f, 0f, false, true, 16.586f, 12.586f)
            lineTo(18.5f, 14.5f)
        }
    }.build().also { cachedHammer = it }

/** `Hammer` vector, built on first access and reused afterwards. */
private var cachedHammer: ImageVector? = null
