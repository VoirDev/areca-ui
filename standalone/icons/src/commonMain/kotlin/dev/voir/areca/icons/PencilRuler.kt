package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pencil ruler icon.
 *
 * Converted from Lucide `pencil-ruler` (ISC).
 */
public val ArecaIcons.PencilRuler: ImageVector
    get() = cachedPencilRuler ?: ImageVector.Builder(
        name = "PencilRuler",
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
            moveTo(13f, 7f)
            lineTo(8.7f, 2.7f)
            arcTo(2.41f, 2.41f, 0f, false, false, 5.3f, 2.7f)
            lineTo(2.7f, 5.3f)
            arcTo(2.41f, 2.41f, 0f, false, false, 2.7f, 8.7f)
            lineTo(7f, 13f)
            moveTo(8f, 6f)
            lineTo(10f, 4f)
            moveTo(18f, 16f)
            lineTo(20f, 14f)
            moveTo(17f, 11f)
            lineTo(21.3f, 15.3f)
            curveTo(22.24f, 16.24f, 22.24f, 17.76f, 21.3f, 18.7f)
            lineTo(18.7f, 21.3f)
            curveTo(17.76f, 22.24f, 16.24f, 22.24f, 15.3f, 21.3f)
            lineTo(11f, 17f)
            moveTo(21.174f, 6.812f)
            arcTo(1f, 1f, 0f, false, false, 17.188f, 2.825f)
            lineTo(3.842f, 16.174f)
            arcTo(2f, 2f, 0f, false, false, 3.342f, 17.004f)
            lineTo(2.021f, 21.356f)
            arcTo(0.5f, 0.5f, 0f, false, false, 2.644f, 21.978f)
            lineTo(6.997f, 20.658f)
            arcTo(2f, 2f, 0f, false, false, 7.827f, 20.161f)
            close()
            moveTo(15f, 5f)
            lineTo(19f, 9f)
        }
    }.build().also { cachedPencilRuler = it }

/** `PencilRuler` vector, built on first access and reused afterwards. */
private var cachedPencilRuler: ImageVector? = null
