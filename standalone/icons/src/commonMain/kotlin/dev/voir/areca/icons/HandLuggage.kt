package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Luggage icon.
 *
 * Converted from Tabler Icons `luggage` (MIT).
 */
public val ArecaIcons.HandLuggage: ImageVector
    get() = cachedHandLuggage ?: ImageVector.Builder(
        name = "HandLuggage",
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
            arcTo(2f, 2f, 0f, false, true, 8f, 6f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 18f, 8f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 16f, 20f)
            horizontalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 6f, 18f)
            lineTo(6f, 8f)
            moveTo(9f, 6f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 11f, 3f)
            horizontalLineTo(13f)
            arcTo(2f, 2f, 0f, false, true, 15f, 5f)
            verticalLineTo(6f)
            moveTo(6f, 10f)
            horizontalLineTo(18f)
            moveTo(6f, 16f)
            horizontalLineTo(18f)
            moveTo(9f, 20f)
            verticalLineTo(21f)
            moveTo(15f, 20f)
            verticalLineTo(21f)
        }
    }.build().also { cachedHandLuggage = it }

/** `HandLuggage` vector, built on first access and reused afterwards. */
private var cachedHandLuggage: ImageVector? = null
