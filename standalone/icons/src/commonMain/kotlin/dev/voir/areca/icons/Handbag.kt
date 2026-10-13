package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Handbag icon.
 *
 * Converted from Lucide `handbag` (ISC).
 */
public val ArecaIcons.Handbag: ImageVector
    get() = cachedHandbag ?: ImageVector.Builder(
        name = "Handbag",
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
            moveTo(2.048f, 18.566f)
            arcTo(2f, 2f, 0f, false, false, 4f, 21f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 21.952f, 18.566f)
            lineTo(19.952f, 9.566f)
            arcTo(2f, 2f, 0f, false, false, 18f, 8f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 4.048f, 9.566f)
            close()
            moveTo(8f, 11f)
            verticalLineTo(6f)
            arcTo(4f, 4f, 0f, false, true, 16f, 6f)
            verticalLineTo(11f)
        }
    }.build().also { cachedHandbag = it }

/** `Handbag` vector, built on first access and reused afterwards. */
private var cachedHandbag: ImageVector? = null
