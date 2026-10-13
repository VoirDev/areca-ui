package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Movie icon.
 *
 * Converted from Tabler Icons `movie` (MIT).
 */
public val ArecaIcons.Movie: ImageVector
    get() = cachedMovie ?: ImageVector.Builder(
        name = "Movie",
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
            moveTo(4f, 6f)
            arcTo(2f, 2f, 0f, false, true, 6f, 4f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 6f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 18f, 20f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 18f)
            lineTo(4f, 6f)
            moveTo(8f, 4f)
            lineTo(8f, 20f)
            moveTo(16f, 4f)
            lineTo(16f, 20f)
            moveTo(4f, 8f)
            lineTo(8f, 8f)
            moveTo(4f, 16f)
            lineTo(8f, 16f)
            moveTo(4f, 12f)
            lineTo(20f, 12f)
            moveTo(16f, 8f)
            lineTo(20f, 8f)
            moveTo(16f, 16f)
            lineTo(20f, 16f)
        }
    }.build().also { cachedMovie = it }

/** `Movie` vector, built on first access and reused afterwards. */
private var cachedMovie: ImageVector? = null
