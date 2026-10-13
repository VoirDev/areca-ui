package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * News icon.
 *
 * Converted from Tabler Icons `news` (MIT).
 */
public val ArecaIcons.News: ImageVector
    get() = cachedNews ?: ImageVector.Builder(
        name = "News",
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
            moveTo(16f, 6f)
            horizontalLineTo(19f)
            arcTo(1f, 1f, 0f, false, true, 20f, 7f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 16f, 18f)
            verticalLineTo(5f)
            arcTo(1f, 1f, 0f, false, false, 15f, 4f)
            horizontalLineTo(5f)
            arcTo(1f, 1f, 0f, false, false, 4f, 5f)
            verticalLineTo(17f)
            arcTo(3f, 3f, 0f, false, false, 7f, 20f)
            horizontalLineTo(18f)
            moveTo(8f, 8f)
            lineTo(12f, 8f)
            moveTo(8f, 12f)
            lineTo(12f, 12f)
            moveTo(8f, 16f)
            lineTo(12f, 16f)
        }
    }.build().also { cachedNews = it }

/** `News` vector, built on first access and reused afterwards. */
private var cachedNews: ImageVector? = null
