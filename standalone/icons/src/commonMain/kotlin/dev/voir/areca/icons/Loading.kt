package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Loader icon.
 *
 * Converted from Lucide `loader` (ISC).
 */
public val ArecaIcons.Loading: ImageVector
    get() = cachedLoading ?: ImageVector.Builder(
        name = "Loading",
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
            moveTo(12f, 2f)
            verticalLineTo(6f)
            moveTo(16.2f, 7.8f)
            lineTo(19.1f, 4.9f)
            moveTo(18f, 12f)
            horizontalLineTo(22f)
            moveTo(16.2f, 16.2f)
            lineTo(19.1f, 19.1f)
            moveTo(12f, 18f)
            verticalLineTo(22f)
            moveTo(4.9f, 19.1f)
            lineTo(7.8f, 16.2f)
            moveTo(2f, 12f)
            horizontalLineTo(6f)
            moveTo(4.9f, 4.9f)
            lineTo(7.8f, 7.8f)
        }
    }.build().also { cachedLoading = it }

/** `Loading` vector, built on first access and reused afterwards. */
private var cachedLoading: ImageVector? = null
