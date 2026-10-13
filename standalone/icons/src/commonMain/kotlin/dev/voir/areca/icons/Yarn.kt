package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Yarn icon.
 *
 * Converted from Tabler Icons `yarn` (MIT).
 */
public val ArecaIcons.Yarn: ImageVector
    get() = cachedYarn ?: ImageVector.Builder(
        name = "Yarn",
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
            moveTo(7.5f, 19.794f)
            arcTo(9f, 9f, 0f, true, false, 16.5f, 4.206f)
            arcTo(9f, 9f, 0f, false, false, 7.5f, 19.794f)
            moveTo(20.997f, 12f)
            arcTo(9f, 9f, 0f, false, false, 12f, 21f)
            moveTo(9.662f, 12.778f)
            arcTo(9f, 9f, 0f, false, false, 3.019f, 12.508f)
            moveTo(19.527f, 7.078f)
            arcTo(14f, 14f, 0f, false, false, 7.077f, 19.536f)
            moveTo(13.503f, 9.18f)
            arcTo(14f, 14f, 0f, false, false, 4.47f, 7.083f)
        }
    }.build().also { cachedYarn = it }

/** `Yarn` vector, built on first access and reused afterwards. */
private var cachedYarn: ImageVector? = null
