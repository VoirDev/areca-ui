package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Glasses icon.
 *
 * Converted from Lucide `glasses` (ISC).
 */
public val ArecaIcons.Glasses: ImageVector
    get() = cachedGlasses ?: ImageVector.Builder(
        name = "Glasses",
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
            moveTo(2f, 15f)
            arcTo(4f, 4f, 0f, true, true, 10f, 15f)
            arcTo(4f, 4f, 0f, true, true, 2f, 15f)
            close()
            moveTo(14f, 15f)
            arcTo(4f, 4f, 0f, true, true, 22f, 15f)
            arcTo(4f, 4f, 0f, true, true, 14f, 15f)
            close()
            moveTo(14f, 15f)
            arcTo(2f, 2f, 0f, false, false, 12f, 13f)
            arcTo(2f, 2f, 0f, false, false, 10f, 15f)
            moveTo(2.5f, 13f)
            lineTo(5f, 7f)
            curveTo(5.7f, 5.7f, 6.4f, 5f, 8f, 5f)
            moveTo(21.5f, 13f)
            lineTo(19f, 7f)
            curveTo(18.3f, 5.7f, 17.5f, 5f, 16f, 5f)
        }
    }.build().also { cachedGlasses = it }

/** `Glasses` vector, built on first access and reused afterwards. */
private var cachedGlasses: ImageVector? = null
