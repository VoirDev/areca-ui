package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Youtube logo.
 *
 * Converted from Tabler Icons `brand-youtube` (MIT).
 */
public val ArecaIcons.Youtube: ImageVector
    get() = cachedYoutube ?: ImageVector.Builder(
        name = "Youtube",
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
            moveTo(2f, 8f)
            arcTo(4f, 4f, 0f, false, true, 6f, 4f)
            horizontalLineTo(18f)
            arcTo(4f, 4f, 0f, false, true, 22f, 8f)
            verticalLineTo(16f)
            arcTo(4f, 4f, 0f, false, true, 18f, 20f)
            horizontalLineTo(6f)
            arcTo(4f, 4f, 0f, false, true, 2f, 16f)
            verticalLineTo(8f)
            moveTo(10f, 9f)
            lineTo(15f, 12f)
            lineTo(10f, 15f)
            lineTo(10f, 9f)
        }
    }.build().also { cachedYoutube = it }

/** `Youtube` vector, built on first access and reused afterwards. */
private var cachedYoutube: ImageVector? = null
