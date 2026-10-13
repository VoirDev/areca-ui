package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Film icon.
 *
 * Converted from Lucide `film` (ISC).
 */
public val ArecaIcons.Film: ImageVector
    get() = cachedFilm ?: ImageVector.Builder(
        name = "Film",
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
            moveTo(5f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            close()
            moveTo(7f, 3f)
            verticalLineTo(21f)
            moveTo(3f, 7.5f)
            horizontalLineTo(7f)
            moveTo(3f, 12f)
            horizontalLineTo(21f)
            moveTo(3f, 16.5f)
            horizontalLineTo(7f)
            moveTo(17f, 3f)
            verticalLineTo(21f)
            moveTo(17f, 7.5f)
            horizontalLineTo(21f)
            moveTo(17f, 16.5f)
            horizontalLineTo(21f)
        }
    }.build().also { cachedFilm = it }

/** `Film` vector, built on first access and reused afterwards. */
private var cachedFilm: ImageVector? = null
