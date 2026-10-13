package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Lamp ceiling icon.
 *
 * Converted from Lucide `lamp-ceiling` (ISC).
 */
public val ArecaIcons.LampCeiling: ImageVector
    get() = cachedLampCeiling ?: ImageVector.Builder(
        name = "LampCeiling",
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
            moveTo(12f, 2f)
            verticalLineTo(7f)
            moveTo(14.829f, 15.998f)
            arcTo(3f, 3f, 0f, true, true, 9.171f, 15.998f)
            moveTo(20.92f, 14.606f)
            arcTo(1f, 1f, 0f, false, true, 20f, 16f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3.08f, 14.606f)
            lineTo(6.08f, 7.606f)
            arcTo(1f, 1f, 0f, false, true, 7f, 7f)
            horizontalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 17.92f, 7.606f)
            close()
        }
    }.build().also { cachedLampCeiling = it }

/** `LampCeiling` vector, built on first access and reused afterwards. */
private var cachedLampCeiling: ImageVector? = null
