package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Thumbs down icon.
 *
 * Converted from Lucide `thumbs-down` (ISC).
 */
public val ArecaIcons.ThumbsDown: ImageVector
    get() = cachedThumbsDown ?: ImageVector.Builder(
        name = "ThumbsDown",
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
            moveTo(9f, 18.12f)
            lineTo(10f, 14f)
            horizontalLineTo(4.17f)
            arcTo(2f, 2f, 0f, false, true, 2.25f, 11.44f)
            lineTo(4.58f, 3.44f)
            arcTo(2f, 2f, 0f, false, true, 6.5f, 2f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 4f)
            verticalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 20f, 14f)
            horizontalLineTo(17.24f)
            arcTo(2f, 2f, 0f, false, false, 15.45f, 15.11f)
            lineTo(12f, 22f)
            arcTo(3.13f, 3.13f, 0f, false, true, 9f, 18.12f)
            close()
            moveTo(17f, 14f)
            verticalLineTo(2f)
        }
    }.build().also { cachedThumbsDown = it }

/** `ThumbsDown` vector, built on first access and reused afterwards. */
private var cachedThumbsDown: ImageVector? = null
