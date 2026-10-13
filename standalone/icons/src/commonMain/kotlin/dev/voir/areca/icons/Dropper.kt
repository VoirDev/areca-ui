package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pipette icon.
 *
 * Converted from Lucide `pipette` (ISC).
 */
public val ArecaIcons.Dropper: ImageVector
    get() = cachedDropper ?: ImageVector.Builder(
        name = "Dropper",
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
            moveTo(12f, 9f)
            lineTo(3.586f, 17.414f)
            arcTo(2f, 2f, 0f, false, false, 3f, 18.828f)
            verticalLineTo(20.172f)
            arcTo(2f, 2f, 0f, false, true, 2.414f, 21.586f)
            arcTo(2f, 2f, 0f, false, true, 3.828f, 21f)
            horizontalLineTo(5.172f)
            arcTo(2f, 2f, 0f, false, false, 6.586f, 20.414f)
            lineTo(15f, 12f)
            moveTo(18f, 9f)
            lineTo(18.4f, 9.4f)
            arcTo(1f, 1f, 0f, true, true, 15.4f, 12.4f)
            lineTo(11.6f, 8.6f)
            arcTo(1f, 1f, 0f, true, true, 14.6f, 5.6f)
            lineTo(15f, 6f)
            lineTo(18.4f, 2.6f)
            arcTo(1f, 1f, 0f, true, true, 21.4f, 5.6f)
            close()
            moveTo(2f, 22f)
            lineTo(2.414f, 21.586f)
        }
    }.build().also { cachedDropper = it }

/** `Dropper` vector, built on first access and reused afterwards. */
private var cachedDropper: ImageVector? = null
