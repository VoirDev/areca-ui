package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ball baseball icon.
 *
 * Converted from Tabler Icons `ball-baseball` (MIT).
 */
public val ArecaIcons.BallBaseball: ImageVector
    get() = cachedBallBaseball ?: ImageVector.Builder(
        name = "BallBaseball",
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
            moveTo(5.636f, 18.364f)
            arcTo(9f, 9f, 0f, true, false, 18.364f, 5.636f)
            arcTo(9f, 9f, 0f, false, false, 5.636f, 18.364f)
            moveTo(12.495f, 3.02f)
            arcTo(9f, 9f, 0f, false, true, 3.02f, 12.495f)
            moveTo(20.98f, 11.505f)
            arcTo(9f, 9f, 0f, false, false, 11.505f, 20.98f)
            moveTo(9f, 9f)
            lineTo(11f, 11f)
            moveTo(13f, 13f)
            lineTo(15f, 15f)
            moveTo(11f, 7f)
            lineTo(13f, 8f)
            moveTo(7f, 11f)
            lineTo(8f, 13f)
            moveTo(16f, 11f)
            lineTo(17f, 13f)
            moveTo(11f, 16f)
            lineTo(13f, 17f)
        }
    }.build().also { cachedBallBaseball = it }

/** `BallBaseball` vector, built on first access and reused afterwards. */
private var cachedBallBaseball: ImageVector? = null
