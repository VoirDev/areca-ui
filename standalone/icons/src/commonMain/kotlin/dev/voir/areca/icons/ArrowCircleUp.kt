package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle arrow up icon.
 *
 * Converted from Tabler Icons `circle-arrow-up` (MIT).
 */
public val ArecaIcons.ArrowCircleUp: ImageVector
    get() = cachedArrowCircleUp ?: ImageVector.Builder(
        name = "ArrowCircleUp",
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
            arcTo(9f, 9f, 0f, false, false, 3f, 12f)
            moveTo(12f, 8f)
            lineTo(8f, 12f)
            moveTo(12f, 8f)
            verticalLineTo(16f)
            moveTo(16f, 12f)
            lineTo(12f, 8f)
        }
    }.build().also { cachedArrowCircleUp = it }

/** `ArrowCircleUp` vector, built on first access and reused afterwards. */
private var cachedArrowCircleUp: ImageVector? = null
