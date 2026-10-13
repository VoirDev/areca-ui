package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Can soda icon.
 *
 * Converted from Lucide `can-soda` (ISC).
 */
public val ArecaIcons.CanSoda: ImageVector
    get() = cachedCanSoda ?: ImageVector.Builder(
        name = "CanSoda",
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
            moveTo(17f, 22f)
            lineTo(18.664f, 19.504f)
            arcTo(2f, 2f, 0f, false, false, 19f, 18.394f)
            verticalLineTo(5.606f)
            arcTo(2f, 2f, 0f, false, false, 18.664f, 4.496f)
            lineTo(17f, 2f)
            moveTo(18f, 22f)
            horizontalLineTo(6f)
            moveTo(18f, 2f)
            horizontalLineTo(6f)
            moveTo(5f, 17f)
            horizontalLineTo(19f)
            moveTo(5f, 7f)
            horizontalLineTo(19f)
            moveTo(7f, 22f)
            lineTo(5.336f, 19.504f)
            arcTo(2f, 2f, 0f, false, true, 5f, 18.394f)
            verticalLineTo(5.606f)
            arcTo(2f, 2f, 0f, false, true, 5.336f, 4.496f)
            lineTo(7f, 2f)
        }
    }.build().also { cachedCanSoda = it }

/** `CanSoda` vector, built on first access and reused afterwards. */
private var cachedCanSoda: ImageVector? = null
