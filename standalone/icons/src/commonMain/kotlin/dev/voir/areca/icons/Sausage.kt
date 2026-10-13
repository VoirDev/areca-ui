package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sausage icon.
 *
 * Converted from Tabler Icons `sausage` (MIT).
 */
public val ArecaIcons.Sausage: ImageVector
    get() = cachedSausage ?: ImageVector.Builder(
        name = "Sausage",
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
            moveTo(5.5f, 5.5f)
            arcTo(2.5f, 2.5f, 0f, false, false, 3f, 8f)
            curveTo(3f, 15.18f, 8.82f, 21f, 16f, 21f)
            arcTo(2.5f, 2.5f, 0f, true, false, 16f, 16f)
            arcTo(8f, 8f, 0f, false, true, 8f, 8f)
            arcTo(2.5f, 2.5f, 0f, false, false, 5.5f, 5.5f)
            moveTo(5.195f, 5.519f)
            lineTo(3.952f, 3.53f)
            arcTo(1f, 1f, 0f, false, true, 4.8f, 2f)
            horizontalLineTo(6.192f)
            arcTo(1f, 1f, 0f, false, true, 7.04f, 3.53f)
            lineTo(5.795f, 5.52f)
            moveTo(18.482f, 18.225f)
            lineTo(20.471f, 16.982f)
            arcTo(1f, 1f, 0f, false, true, 22.001f, 17.83f)
            verticalLineTo(19.222f)
            arcTo(1f, 1f, 0f, false, true, 20.471f, 20.07f)
            lineTo(18.48f, 18.825f)
        }
    }.build().also { cachedSausage = it }

/** `Sausage` vector, built on first access and reused afterwards. */
private var cachedSausage: ImageVector? = null
