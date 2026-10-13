package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Presentation icon.
 *
 * Converted from Lucide `presentation` (ISC).
 */
public val ArecaIcons.Presentation: ImageVector
    get() = cachedPresentation ?: ImageVector.Builder(
        name = "Presentation",
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
            moveTo(2f, 3f)
            horizontalLineTo(22f)
            moveTo(21f, 3f)
            verticalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 19f, 16f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 14f)
            verticalLineTo(3f)
            moveTo(7f, 21f)
            lineTo(12f, 16f)
            lineTo(17f, 21f)
        }
    }.build().also { cachedPresentation = it }

/** `Presentation` vector, built on first access and reused afterwards. */
private var cachedPresentation: ImageVector? = null
