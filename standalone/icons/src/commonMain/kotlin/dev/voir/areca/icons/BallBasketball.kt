package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ball basketball icon.
 *
 * Converted from Tabler Icons `ball-basketball` (MIT).
 */
public val ArecaIcons.BallBasketball: ImageVector
    get() = cachedBallBasketball ?: ImageVector.Builder(
        name = "BallBasketball",
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
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, true, false, 3f, 12f)
            moveTo(5.65f, 5.65f)
            lineTo(18.35f, 18.35f)
            moveTo(5.65f, 18.35f)
            lineTo(18.35f, 5.65f)
            moveTo(12f, 3f)
            arcTo(9f, 9f, 0f, false, false, 21f, 12f)
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, false, true, 12f, 21f)
        }
    }.build().also { cachedBallBasketball = it }

/** `BallBasketball` vector, built on first access and reused afterwards. */
private var cachedBallBasketball: ImageVector? = null
