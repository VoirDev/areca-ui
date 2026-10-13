package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Printer icon.
 *
 * Converted from Lucide `printer` (ISC).
 */
public val ArecaIcons.Printer: ImageVector
    get() = cachedPrinter ?: ImageVector.Builder(
        name = "Printer",
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
            moveTo(6f, 18f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 16f)
            verticalLineTo(11f)
            arcTo(2f, 2f, 0f, false, true, 4f, 9f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 11f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 20f, 18f)
            horizontalLineTo(18f)
            moveTo(6f, 9f)
            verticalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 7f, 2f)
            horizontalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 18f, 3f)
            verticalLineTo(9f)
            moveTo(7f, 14f)
            horizontalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 18f, 15f)
            verticalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 17f, 22f)
            horizontalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 6f, 21f)
            verticalLineTo(15f)
            arcTo(1f, 1f, 0f, false, true, 7f, 14f)
            close()
        }
    }.build().also { cachedPrinter = it }

/** `Printer` vector, built on first access and reused afterwards. */
private var cachedPrinter: ImageVector? = null
