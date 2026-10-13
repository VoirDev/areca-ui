package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ham icon.
 *
 * Converted from Lucide `ham` (ISC).
 */
public val ArecaIcons.Ham: ImageVector
    get() = cachedHam ?: ImageVector.Builder(
        name = "Ham",
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
            moveTo(13.144f, 21.144f)
            arcTo(7.274f, 10.445f, 45f, true, false, 2.856f, 10.856f)
            moveTo(13.144f, 21.144f)
            arcTo(7.274f, 4.365f, 45f, false, false, 2.856f, 10.856f)
            arcTo(7.274f, 4.365f, 45f, false, false, 13.144f, 21.144f)
            moveTo(16.565f, 10.435f)
            lineTo(18.6f, 8.4f)
            arcTo(2.501f, 2.501f, 0f, true, false, 20.25f, 3.75f)
            arcTo(2.5f, 2.5f, 0f, true, false, 15.59f, 5.41f)
            lineTo(13.566f, 7.435f)
            moveTo(8.5f, 16.5f)
            lineTo(7.5f, 15.5f)
        }
    }.build().also { cachedHam = it }

/** `Ham` vector, built on first access and reused afterwards. */
private var cachedHam: ImageVector? = null
