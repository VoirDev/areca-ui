package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ski jumping icon.
 *
 * Converted from Tabler Icons `ski-jumping` (MIT).
 */
public val ArecaIcons.SkiJumping: ImageVector
    get() = cachedSkiJumping ?: ImageVector.Builder(
        name = "SkiJumping",
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
            moveTo(17f, 17.5f)
            lineTo(12f, 13f)
            verticalLineTo(7f)
            lineTo(17f, 11f)
            moveTo(7f, 17.5f)
            lineTo(12f, 13f)
            moveTo(15.103f, 21.58f)
            lineTo(21.865f, 7.078f)
            arcTo(2f, 2f, 0f, false, false, 20.897f, 4.421f)
            moveTo(8.897f, 21.58f)
            lineTo(2.135f, 7.077f)
            arcTo(2f, 2f, 0f, false, true, 3.103f, 4.42f)
            moveTo(7f, 11f)
            lineTo(12f, 7f)
            moveTo(10.007f, 4f)
            arcTo(2f, 2f, 0f, true, false, 14.007f, 4f)
            arcTo(2f, 2f, 0f, true, false, 10.007f, 4f)
        }
    }.build().also { cachedSkiJumping = it }

/** `SkiJumping` vector, built on first access and reused afterwards. */
private var cachedSkiJumping: ImageVector? = null
