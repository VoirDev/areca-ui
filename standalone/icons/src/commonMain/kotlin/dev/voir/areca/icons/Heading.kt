package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Heading icon.
 *
 * Converted from Lucide `heading` (ISC).
 */
public val ArecaIcons.Heading: ImageVector
    get() = cachedHeading ?: ImageVector.Builder(
        name = "Heading",
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
            moveTo(6f, 12f)
            horizontalLineTo(18f)
            moveTo(6f, 20f)
            verticalLineTo(4f)
            moveTo(18f, 20f)
            verticalLineTo(4f)
        }
    }.build().also { cachedHeading = it }

/** `Heading` vector, built on first access and reused afterwards. */
private var cachedHeading: ImageVector? = null
