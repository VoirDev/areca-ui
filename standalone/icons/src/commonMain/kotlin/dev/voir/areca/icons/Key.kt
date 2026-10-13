package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Key icon.
 *
 * Converted from Lucide `key` (ISC).
 */
public val ArecaIcons.Key: ImageVector
    get() = cachedKey ?: ImageVector.Builder(
        name = "Key",
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
            moveTo(2f, 21f)
            lineTo(11.6f, 11.4f)
            moveTo(7.5f, 15.5f)
            lineTo(9.8f, 17.8f)
            arcTo(1f, 1f, 0f, false, true, 9.8f, 19.2f)
            lineTo(7.7f, 21.3f)
            arcTo(1f, 1f, 0f, false, true, 6.3f, 21.3f)
            lineTo(4f, 19f)
            moveTo(10f, 7.5f)
            arcTo(5.5f, 5.5f, 0f, true, true, 21f, 7.5f)
            arcTo(5.5f, 5.5f, 0f, true, true, 10f, 7.5f)
            close()
        }
    }.build().also { cachedKey = it }

/** `Key` vector, built on first access and reused afterwards. */
private var cachedKey: ImageVector? = null
