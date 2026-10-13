package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Square pen icon.
 *
 * Converted from Lucide `square-pen` (ISC).
 */
public val ArecaIcons.EditSquare: ImageVector
    get() = cachedEditSquare ?: ImageVector.Builder(
        name = "EditSquare",
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
            moveTo(12f, 3f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 3f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 5f, 21f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 21f, 19f)
            verticalLineTo(12f)
            moveTo(18.375f, 2.625f)
            arcTo(1f, 1f, 0f, false, true, 21.375f, 5.625f)
            lineTo(12.362f, 14.639f)
            arcTo(2f, 2f, 0f, false, true, 11.509f, 15.144f)
            lineTo(8.636f, 15.984f)
            arcTo(0.5f, 0.5f, 0f, false, true, 8.016f, 15.364f)
            lineTo(8.856f, 12.491f)
            arcTo(2f, 2f, 0f, false, true, 9.362f, 11.639f)
            close()
        }
    }.build().also { cachedEditSquare = it }

/** `EditSquare` vector, built on first access and reused afterwards. */
private var cachedEditSquare: ImageVector? = null
