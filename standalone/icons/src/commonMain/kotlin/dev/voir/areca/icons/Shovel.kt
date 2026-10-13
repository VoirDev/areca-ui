package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shovel icon.
 *
 * Converted from Lucide `shovel` (ISC).
 */
public val ArecaIcons.Shovel: ImageVector
    get() = cachedShovel ?: ImageVector.Builder(
        name = "Shovel",
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
            moveTo(21.56f, 4.56f)
            arcTo(1.5f, 1.5f, 0f, false, true, 21.56f, 6.682f)
            lineTo(21.09f, 7.152f)
            arcTo(3f, 3f, 0f, false, true, 16.878f, 7.122f)
            arcTo(3f, 3f, 0f, false, true, 16.878f, 2.879f)
            lineTo(17.318f, 2.439f)
            arcTo(1.5f, 1.5f, 0f, false, true, 19.439f, 2.439f)
            close()
            moveTo(3f, 22f)
            arcTo(1f, 1f, 0f, false, true, 2f, 21f)
            verticalLineTo(17.414f)
            arcTo(1f, 1f, 0f, false, true, 2.293f, 16.707f)
            lineTo(5.648f, 13.352f)
            arcTo(1.205f, 1.205f, 0f, false, true, 7.352f, 13.352f)
            lineTo(10.648f, 16.648f)
            arcTo(1.205f, 1.205f, 0f, false, true, 10.648f, 18.352f)
            lineTo(7.293f, 21.707f)
            arcTo(1f, 1f, 0f, false, true, 6.586f, 22f)
            close()
            moveTo(9f, 15f)
            lineTo(16.879f, 7.122f)
        }
    }.build().also { cachedShovel = it }

/** `Shovel` vector, built on first access and reused afterwards. */
private var cachedShovel: ImageVector? = null
