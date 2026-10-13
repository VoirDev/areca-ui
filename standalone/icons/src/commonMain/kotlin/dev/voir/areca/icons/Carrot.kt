package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Carrot icon.
 *
 * Converted from Lucide `carrot` (ISC).
 */
public val ArecaIcons.Carrot: ImageVector
    get() = cachedCarrot ?: ImageVector.Builder(
        name = "Carrot",
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
            moveTo(15f, 16f)
            arcTo(1f, 1f, 0f, false, false, 8f, 9f)
            quadTo(4f, 13f, 2.013f, 21.385f)
            arcTo(0.5f, 0.5f, 0f, false, false, 2.615f, 21.987f)
            quadTo(11f, 20f, 15f, 16f)
            lineTo(12f, 13f)
            moveTo(15f, 9f)
            quadTo(19f, 13f, 22f, 9f)
            quadTo(19f, 5f, 15f, 9f)
            quadTo(19f, 5f, 15f, 2f)
            quadTo(11f, 5f, 15f, 9f)
            moveTo(8f, 15f)
            lineTo(5.42f, 12.42f)
        }
    }.build().also { cachedCarrot = it }

/** `Carrot` vector, built on first access and reused afterwards. */
private var cachedCarrot: ImageVector? = null
