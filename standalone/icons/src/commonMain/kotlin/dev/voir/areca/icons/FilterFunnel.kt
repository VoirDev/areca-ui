package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Funnel icon.
 *
 * Converted from Lucide `funnel` (ISC).
 */
public val ArecaIcons.FilterFunnel: ImageVector
    get() = cachedFilterFunnel ?: ImageVector.Builder(
        name = "FilterFunnel",
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
            moveTo(10f, 20f)
            arcTo(1f, 1f, 0f, false, false, 10.553f, 20.895f)
            lineTo(12.553f, 21.895f)
            arcTo(1f, 1f, 0f, false, false, 14f, 21f)
            verticalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 14.517f, 12.659f)
            lineTo(21.74f, 4.67f)
            arcTo(1f, 1f, 0f, false, false, 21f, 3f)
            horizontalLineTo(3f)
            arcTo(1f, 1f, 0f, false, false, 2.258f, 4.67f)
            lineTo(9.483f, 12.659f)
            arcTo(2f, 2f, 0f, false, true, 10f, 14f)
            close()
        }
    }.build().also { cachedFilterFunnel = it }

/** `FilterFunnel` vector, built on first access and reused afterwards. */
private var cachedFilterFunnel: ImageVector? = null
