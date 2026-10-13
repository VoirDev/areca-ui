package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shoe icon.
 *
 * Converted from Tabler Icons `shoe` (MIT).
 */
public val ArecaIcons.Shoe: ImageVector
    get() = cachedShoe ?: ImageVector.Builder(
        name = "Shoe",
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
            moveTo(4f, 6f)
            horizontalLineTo(9.426f)
            arcTo(1f, 1f, 0f, false, true, 10.289f, 6.496f)
            lineTo(11.353f, 8.319f)
            arcTo(3f, 3f, 0f, false, false, 13.249f, 9.726f)
            lineTo(17.926f, 10.84f)
            arcTo(4f, 4f, 0f, false, true, 21f, 14.73f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 20f, 18f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 17f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 4f, 6f)
            moveTo(14f, 13f)
            lineTo(15f, 11f)
            moveTo(8f, 18f)
            verticalLineTo(17f)
            arcTo(4f, 4f, 0f, false, false, 4f, 13f)
            horizontalLineTo(3f)
            moveTo(10f, 12f)
            lineTo(11.5f, 9f)
        }
    }.build().also { cachedShoe = it }

/** `Shoe` vector, built on first access and reused afterwards. */
private var cachedShoe: ImageVector? = null
