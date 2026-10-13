package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Color swatch icon.
 *
 * Converted from Tabler Icons `color-swatch` (MIT).
 */
public val ArecaIcons.Colors: ImageVector
    get() = cachedColors ?: ImageVector.Builder(
        name = "Colors",
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
            moveTo(19f, 3f)
            horizontalLineTo(15f)
            arcTo(2f, 2f, 0f, false, false, 13f, 5f)
            verticalLineTo(17f)
            arcTo(4f, 4f, 0f, false, false, 21f, 17f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 19f, 3f)
            moveTo(13f, 7.35f)
            lineTo(11f, 5.35f)
            arcTo(2f, 2f, 0f, false, false, 8.172f, 5.35f)
            lineTo(5.344f, 8.178f)
            arcTo(2f, 2f, 0f, false, false, 5.344f, 11.006f)
            lineTo(14.344f, 20.006f)
            moveTo(7.3f, 13f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 3f, 15f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 5f, 21f)
            horizontalLineTo(17f)
            moveTo(17f, 17f)
            lineTo(17f, 17.01f)
        }
    }.build().also { cachedColors = it }

/** `Colors` vector, built on first access and reused afterwards. */
private var cachedColors: ImageVector? = null
