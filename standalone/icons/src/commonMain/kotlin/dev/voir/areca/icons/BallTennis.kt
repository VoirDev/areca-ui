package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ball tennis icon.
 *
 * Converted from Tabler Icons `ball-tennis` (MIT).
 */
public val ArecaIcons.BallTennis: ImageVector
    get() = cachedBallTennis ?: ImageVector.Builder(
        name = "BallTennis",
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
            moveTo(6f, 5.3f)
            arcTo(9f, 9f, 0f, false, true, 6f, 18.7f)
            moveTo(18f, 5.3f)
            arcTo(9f, 9f, 0f, false, false, 18f, 18.7f)
        }
    }.build().also { cachedBallTennis = it }

/** `BallTennis` vector, built on first access and reused afterwards. */
private var cachedBallTennis: ImageVector? = null
