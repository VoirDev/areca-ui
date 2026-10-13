package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Contrast icon.
 *
 * Converted from Lucide `contrast` (ISC).
 */
public val ArecaIcons.Contrast: ImageVector
    get() = cachedContrast ?: ImageVector.Builder(
        name = "Contrast",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(12f, 18f)
            arcTo(6f, 6f, 0f, false, false, 12f, 6f)
            verticalLineTo(18f)
            close()
        }
    }.build().also { cachedContrast = it }

/** `Contrast` vector, built on first access and reused afterwards. */
private var cachedContrast: ImageVector? = null
