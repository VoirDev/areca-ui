package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pin icon.
 *
 * Converted from Tabler Icons `pin` (MIT).
 */
public val ArecaIcons.PinAlt: ImageVector
    get() = cachedPinAlt ?: ImageVector.Builder(
        name = "PinAlt",
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
            moveTo(15f, 4.5f)
            lineTo(11f, 8.5f)
            lineTo(7f, 10f)
            lineTo(5.5f, 11.5f)
            lineTo(12.5f, 18.5f)
            lineTo(14f, 17f)
            lineTo(15.5f, 13f)
            lineTo(19.5f, 9f)
            moveTo(9f, 15f)
            lineTo(4.5f, 19.5f)
            moveTo(14.5f, 4f)
            lineTo(20f, 9.5f)
        }
    }.build().also { cachedPinAlt = it }

/** `PinAlt` vector, built on first access and reused afterwards. */
private var cachedPinAlt: ImageVector? = null
