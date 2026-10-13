package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Crown icon.
 *
 * Converted from Lucide `crown` (ISC).
 */
public val ArecaIcons.Crown: ImageVector
    get() = cachedCrown ?: ImageVector.Builder(
        name = "Crown",
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
            moveTo(11.562f, 3.266f)
            arcTo(0.5f, 0.5f, 0f, false, true, 12.438f, 3.266f)
            lineTo(15.39f, 8.87f)
            arcTo(1f, 1f, 0f, false, false, 16.906f, 9.164f)
            lineTo(21.183f, 5.5f)
            arcTo(0.5f, 0.5f, 0f, false, true, 21.981f, 6.019f)
            lineTo(19.147f, 16.265f)
            arcTo(1f, 1f, 0f, false, true, 18.191f, 16.999f)
            horizontalLineTo(5.81f)
            arcTo(1f, 1f, 0f, false, true, 4.853f, 16.265f)
            lineTo(2.02f, 6.02f)
            arcTo(0.5f, 0.5f, 0f, false, true, 2.818f, 5.501f)
            lineTo(7.094f, 9.165f)
            arcTo(1f, 1f, 0f, false, false, 8.61f, 8.871f)
            close()
            moveTo(5f, 21f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedCrown = it }

/** `Crown` vector, built on first access and reused afterwards. */
private var cachedCrown: ImageVector? = null
