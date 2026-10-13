package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cup soda icon.
 *
 * Converted from Lucide `cup-soda` (ISC).
 */
public val ArecaIcons.CupSoda: ImageVector
    get() = cachedCupSoda ?: ImageVector.Builder(
        name = "CupSoda",
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
            moveTo(6f, 8f)
            lineTo(7.75f, 20.28f)
            arcTo(2f, 2f, 0f, false, false, 9.75f, 22f)
            horizontalLineTo(14.29f)
            arcTo(2f, 2f, 0f, false, false, 16.29f, 20.28f)
            lineTo(18f, 8f)
            moveTo(5f, 8f)
            horizontalLineTo(19f)
            moveTo(7f, 15f)
            arcTo(6.47f, 6.47f, 0f, false, true, 12f, 15f)
            arcTo(6.47f, 6.47f, 0f, false, false, 17f, 15f)
            moveTo(12f, 8f)
            lineTo(13f, 2f)
            horizontalLineTo(15f)
        }
    }.build().also { cachedCupSoda = it }

/** `CupSoda` vector, built on first access and reused afterwards. */
private var cachedCupSoda: ImageVector? = null
