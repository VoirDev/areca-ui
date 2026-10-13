package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Phone x icon.
 *
 * Converted from Tabler Icons `phone-x` (MIT).
 */
public val ArecaIcons.PhoneX: ImageVector
    get() = cachedPhoneX ?: ImageVector.Builder(
        name = "PhoneX",
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
            arcTo(16f, 16f, 0f, false, true, 3f, 6f)
            arcTo(2f, 2f, 0f, false, true, 5f, 4f)
            moveTo(16f, 4f)
            lineTo(20f, 8f)
            moveTo(20f, 4f)
            lineTo(16f, 8f)
        }
    }.build().also { cachedPhoneX = it }

/** `PhoneX` vector, built on first access and reused afterwards. */
private var cachedPhoneX: ImageVector? = null
