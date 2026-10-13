package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Play football icon.
 *
 * Converted from Tabler Icons `play-football` (MIT).
 */
public val ArecaIcons.PlayFootball: ImageVector
    get() = cachedPlayFootball ?: ImageVector.Builder(
        name = "PlayFootball",
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
            moveTo(3f, 17f)
            lineTo(8f, 18f)
            lineTo(8.75f, 16.5f)
            moveTo(14f, 21f)
            verticalLineTo(17f)
            lineTo(10f, 14f)
            lineTo(11f, 8f)
            moveTo(6f, 12f)
            verticalLineTo(9f)
            lineTo(11f, 8f)
            lineTo(14f, 11f)
            lineTo(17f, 12f)
            moveTo(18.007f, 19.5f)
            arcTo(1.5f, 1.5f, 0f, true, false, 21.007f, 19.5f)
            arcTo(1.5f, 1.5f, 0f, true, false, 18.007f, 19.5f)
            moveTo(10.007f, 5f)
            arcTo(2f, 2f, 0f, true, false, 14.007f, 5f)
            arcTo(2f, 2f, 0f, true, false, 10.007f, 5f)
        }
    }.build().also { cachedPlayFootball = it }

/** `PlayFootball` vector, built on first access and reused afterwards. */
private var cachedPlayFootball: ImageVector? = null
