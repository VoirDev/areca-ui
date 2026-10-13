package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * List icon.
 *
 * Converted from Lucide `list` (ISC).
 */
public val ArecaIcons.Dotpoints: ImageVector
    get() = cachedDotpoints ?: ImageVector.Builder(
        name = "Dotpoints",
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
            moveTo(3f, 5f)
            horizontalLineTo(3.01f)
            moveTo(3f, 12f)
            horizontalLineTo(3.01f)
            moveTo(3f, 19f)
            horizontalLineTo(3.01f)
            moveTo(8f, 5f)
            horizontalLineTo(21f)
            moveTo(8f, 12f)
            horizontalLineTo(21f)
            moveTo(8f, 19f)
            horizontalLineTo(21f)
        }
    }.build().also { cachedDotpoints = it }

/** `Dotpoints` vector, built on first access and reused afterwards. */
private var cachedDotpoints: ImageVector? = null
