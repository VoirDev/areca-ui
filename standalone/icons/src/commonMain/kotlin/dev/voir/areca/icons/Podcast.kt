package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mic signal icon.
 *
 * Converted from Lucide `mic-signal` (ISC).
 */
public val ArecaIcons.Podcast: ImageVector
    get() = cachedPodcast ?: ImageVector.Builder(
        name = "Podcast",
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
            moveTo(12f, 17f)
            verticalLineTo(21f)
            moveTo(18f, 11f)
            arcTo(6f, 6f, 0f, false, false, 15f, 5.803f)
            moveTo(2f, 11f)
            arcTo(10f, 10f, 0f, false, true, 7f, 2.338f)
            moveTo(22f, 11f)
            arcTo(10f, 10f, 0f, false, false, 17f, 2.338f)
            moveTo(6f, 11f)
            arcTo(6f, 6f, 0f, false, true, 9f, 5.803f)
            moveTo(9f, 21f)
            horizontalLineTo(15f)
            moveTo(12f, 9f)
            horizontalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 14f, 11f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 12f, 17f)
            horizontalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 10f, 15f)
            verticalLineTo(11f)
            arcTo(2f, 2f, 0f, false, true, 12f, 9f)
            close()
        }
    }.build().also { cachedPodcast = it }

/** `Podcast` vector, built on first access and reused afterwards. */
private var cachedPodcast: ImageVector? = null
