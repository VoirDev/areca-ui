package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Toilet icon.
 *
 * Converted from Lucide `toilet` (ISC).
 */
public val ArecaIcons.Toilet: ImageVector
    get() = cachedToilet ?: ImageVector.Builder(
        name = "Toilet",
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
            moveTo(7f, 12f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 13f)
            arcTo(5f, 5f, 0f, false, true, 16f, 18f)
            horizontalLineTo(15.402f)
            arcTo(0.5f, 0.5f, 0f, false, false, 14.978f, 18.765f)
            lineTo(16.522f, 21.235f)
            arcTo(0.5f, 0.5f, 0f, false, true, 16.098f, 22f)
            horizontalLineTo(5.402f)
            arcTo(0.5f, 0.5f, 0f, false, true, 4.978f, 21.235f)
            lineTo(7f, 18f)
            moveTo(8f, 18f)
            arcTo(5f, 5f, 0f, false, true, 3f, 13f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 5f, 2f)
            horizontalLineTo(13f)
            arcTo(2f, 2f, 0f, false, true, 15f, 4f)
            verticalLineTo(12f)
        }
    }.build().also { cachedToilet = it }

/** `Toilet` vector, built on first access and reused afterwards. */
private var cachedToilet: ImageVector? = null
