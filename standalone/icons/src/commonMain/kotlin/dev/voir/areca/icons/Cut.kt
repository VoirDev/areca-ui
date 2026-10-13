package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cut icon.
 *
 * Converted from Tabler Icons `cut` (MIT).
 */
public val ArecaIcons.Cut: ImageVector
    get() = cachedCut ?: ImageVector.Builder(
        name = "Cut",
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
            moveTo(4f, 17f)
            arcTo(3f, 3f, 0f, true, false, 10f, 17f)
            arcTo(3f, 3f, 0f, true, false, 4f, 17f)
            moveTo(14f, 17f)
            arcTo(3f, 3f, 0f, true, false, 20f, 17f)
            arcTo(3f, 3f, 0f, true, false, 14f, 17f)
            moveTo(9.15f, 14.85f)
            lineTo(18f, 4f)
            moveTo(6f, 4f)
            lineTo(14.85f, 14.85f)
        }
    }.build().also { cachedCut = it }

/** `Cut` vector, built on first access and reused afterwards. */
private var cachedCut: ImageVector? = null
