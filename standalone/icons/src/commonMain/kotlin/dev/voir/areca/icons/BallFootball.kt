package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ball football icon.
 *
 * Converted from Tabler Icons `ball-football` (MIT).
 */
public val ArecaIcons.BallFootball: ImageVector
    get() = cachedBallFootball ?: ImageVector.Builder(
        name = "BallFootball",
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
            moveTo(12f, 7f)
            lineTo(16.76f, 10.45f)
            lineTo(15f, 16f)
            horizontalLineTo(9f)
            lineTo(7.24f, 10.45f)
            lineTo(12f, 7f)
            moveTo(12f, 7f)
            verticalLineTo(3f)
            moveTo(15f, 16f)
            lineTo(17.5f, 19f)
            moveTo(16.76f, 10.45f)
            lineTo(20.5f, 9f)
            moveTo(9.06f, 16.05f)
            lineTo(6.5f, 19f)
            moveTo(7.24f, 10.45f)
            lineTo(3.5f, 9f)
        }
    }.build().also { cachedBallFootball = it }

/** `BallFootball` vector, built on first access and reused afterwards. */
private var cachedBallFootball: ImageVector? = null
