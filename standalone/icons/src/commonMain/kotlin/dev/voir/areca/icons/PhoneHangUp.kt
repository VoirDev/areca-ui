package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Phone end icon.
 *
 * Converted from Tabler Icons `phone-end` (MIT).
 */
public val ArecaIcons.PhoneHangUp: ImageVector
    get() = cachedPhoneHangUp ?: ImageVector.Builder(
        name = "PhoneHangUp",
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
            moveTo(5f, 4f)
            horizontalLineTo(9f)
            lineTo(11f, 9f)
            lineTo(8.5f, 10.5f)
            arcTo(11f, 11f, 0f, false, false, 13.5f, 15.5f)
            lineTo(15f, 13f)
            lineTo(20f, 15f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 18f, 21f)
            curveTo(9.928f, 20.51f, 3.49f, 14.072f, 3f, 6f)
            arcTo(2f, 2f, 0f, false, true, 5f, 4f)
            moveTo(17f, 3f)
            lineTo(21f, 7f)
            moveTo(21f, 3f)
            lineTo(17f, 7f)
        }
    }.build().also { cachedPhoneHangUp = it }

/** `PhoneHangUp` vector, built on first access and reused afterwards. */
private var cachedPhoneHangUp: ImageVector? = null
