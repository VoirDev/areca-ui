package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Thumbs up icon.
 *
 * Converted from Lucide `thumbs-up` (ISC).
 */
public val ArecaIcons.ThumbsUp: ImageVector
    get() = cachedThumbsUp ?: ImageVector.Builder(
        name = "ThumbsUp",
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
            moveTo(15f, 5.88f)
            lineTo(14f, 10f)
            horizontalLineTo(19.83f)
            arcTo(2f, 2f, 0f, false, true, 21.75f, 12.56f)
            lineTo(19.42f, 20.56f)
            arcTo(2f, 2f, 0f, false, true, 17.5f, 22f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 20f)
            verticalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 4f, 10f)
            horizontalLineTo(6.76f)
            arcTo(2f, 2f, 0f, false, false, 8.55f, 8.89f)
            lineTo(12f, 2f)
            arcTo(3.13f, 3.13f, 0f, false, true, 15f, 5.88f)
            close()
            moveTo(7f, 10f)
            verticalLineTo(22f)
        }
    }.build().also { cachedThumbsUp = it }

/** `ThumbsUp` vector, built on first access and reused afterwards. */
private var cachedThumbsUp: ImageVector? = null
