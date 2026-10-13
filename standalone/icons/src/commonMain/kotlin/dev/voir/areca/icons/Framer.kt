package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Framer logo.
 *
 * Converted from Tabler Icons `brand-framer` (MIT).
 */
public val ArecaIcons.Framer: ImageVector
    get() = cachedFramer ?: ImageVector.Builder(
        name = "Framer",
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
            moveTo(6f, 15f)
            horizontalLineTo(18f)
            lineTo(6f, 3f)
            horizontalLineTo(18f)
            verticalLineTo(9f)
            horizontalLineTo(6f)
            verticalLineTo(15f)
            lineTo(12f, 21f)
            verticalLineTo(15f)
        }
    }.build().also { cachedFramer = it }

/** `Framer` vector, built on first access and reused afterwards. */
private var cachedFramer: ImageVector? = null
