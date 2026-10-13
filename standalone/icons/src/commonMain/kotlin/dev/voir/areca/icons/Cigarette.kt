package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cigarette icon.
 *
 * Converted from Lucide `cigarette` (ISC).
 */
public val ArecaIcons.Cigarette: ImageVector
    get() = cachedCigarette ?: ImageVector.Builder(
        name = "Cigarette",
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
            moveTo(17f, 12f)
            horizontalLineTo(3f)
            arcTo(1f, 1f, 0f, false, false, 2f, 13f)
            verticalLineTo(15f)
            arcTo(1f, 1f, 0f, false, false, 3f, 16f)
            horizontalLineTo(17f)
            moveTo(18f, 8f)
            curveTo(18f, 5.5f, 16f, 5.5f, 16f, 3f)
            moveTo(21f, 16f)
            arcTo(1f, 1f, 0f, false, false, 22f, 15f)
            verticalLineTo(13f)
            arcTo(1f, 1f, 0f, false, false, 21f, 12f)
            moveTo(22f, 8f)
            curveTo(22f, 5.5f, 20f, 5.5f, 20f, 3f)
            moveTo(7f, 12f)
            verticalLineTo(16f)
        }
    }.build().also { cachedCigarette = it }

/** `Cigarette` vector, built on first access and reused afterwards. */
private var cachedCigarette: ImageVector? = null
