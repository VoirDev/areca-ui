package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Kayak icon.
 *
 * Converted from Lucide `kayak` (ISC).
 */
public val ArecaIcons.Kayak: ImageVector
    get() = cachedKayak ?: ImageVector.Builder(
        name = "Kayak",
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
            moveTo(18f, 17f)
            arcTo(1f, 1f, 0f, false, false, 17f, 18f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, true, false, 19f, 17f)
            close()
            moveTo(20.97f, 3.61f)
            arcTo(0.45f, 0.45f, 0f, false, false, 20.39f, 3.03f)
            curveTo(10.2f, 6.6f, 6.6f, 10.2f, 3.03f, 20.39f)
            arcTo(0.45f, 0.45f, 0f, false, false, 3.61f, 20.97f)
            curveTo(13.8f, 17.4f, 17.4f, 13.8f, 20.97f, 3.61f)
            moveTo(6.707f, 6.707f)
            lineTo(17.293f, 17.293f)
            moveTo(7f, 5f)
            arcTo(2f, 2f, 0f, true, false, 5f, 7f)
            horizontalLineTo(6f)
            arcTo(1f, 1f, 0f, false, false, 7f, 6f)
            close()
        }
    }.build().also { cachedKayak = it }

/** `Kayak` vector, built on first access and reused afterwards. */
private var cachedKayak: ImageVector? = null
