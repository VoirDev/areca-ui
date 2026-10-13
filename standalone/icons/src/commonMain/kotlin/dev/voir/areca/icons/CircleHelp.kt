package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle question mark icon.
 *
 * Converted from Lucide `circle-question-mark` (ISC).
 */
public val ArecaIcons.CircleHelp: ImageVector
    get() = cachedCircleHelp ?: ImageVector.Builder(
        name = "CircleHelp",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(9.09f, 9f)
            arcTo(3f, 3f, 0f, false, true, 14.92f, 10f)
            curveTo(14.92f, 12f, 11.92f, 13f, 11.92f, 13f)
            moveTo(12f, 17f)
            horizontalLineTo(12.01f)
        }
    }.build().also { cachedCircleHelp = it }

/** `CircleHelp` vector, built on first access and reused afterwards. */
private var cachedCircleHelp: ImageVector? = null
