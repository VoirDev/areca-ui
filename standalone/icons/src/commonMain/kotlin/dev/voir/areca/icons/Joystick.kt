package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Joystick icon.
 *
 * Converted from Lucide `joystick` (ISC).
 */
public val ArecaIcons.Joystick: ImageVector
    get() = cachedJoystick ?: ImageVector.Builder(
        name = "Joystick",
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
            moveTo(21f, 17f)
            arcTo(2f, 2f, 0f, false, false, 19f, 15f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 3f, 17f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 5f, 21f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 21f, 19f)
            verticalLineTo(17f)
            close()
            moveTo(6f, 15f)
            verticalLineTo(13f)
            moveTo(12f, 15f)
            verticalLineTo(9f)
            moveTo(9f, 6f)
            arcTo(3f, 3f, 0f, true, true, 15f, 6f)
            arcTo(3f, 3f, 0f, true, true, 9f, 6f)
            close()
        }
    }.build().also { cachedJoystick = it }

/** `Joystick` vector, built on first access and reused afterwards. */
private var cachedJoystick: ImageVector? = null
