package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Figma logo.
 *
 * Converted from Tabler Icons `brand-figma` (MIT).
 */
public val ArecaIcons.Figma: ImageVector
    get() = cachedFigma ?: ImageVector.Builder(
        name = "Figma",
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
            moveTo(12f, 12f)
            arcTo(3f, 3f, 0f, true, false, 18f, 12f)
            arcTo(3f, 3f, 0f, true, false, 12f, 12f)
            moveTo(6f, 6f)
            arcTo(3f, 3f, 0f, false, true, 9f, 3f)
            horizontalLineTo(15f)
            arcTo(3f, 3f, 0f, false, true, 18f, 6f)
            arcTo(3f, 3f, 0f, false, true, 15f, 9f)
            horizontalLineTo(9f)
            arcTo(3f, 3f, 0f, false, true, 6f, 6f)
            moveTo(9f, 9f)
            arcTo(3f, 3f, 0f, false, false, 9f, 15f)
            horizontalLineTo(12f)
            moveTo(9f, 15f)
            arcTo(3f, 3f, 0f, true, false, 12f, 18f)
            verticalLineTo(3f)
        }
    }.build().also { cachedFigma = it }

/** `Figma` vector, built on first access and reused afterwards. */
private var cachedFigma: ImageVector? = null
