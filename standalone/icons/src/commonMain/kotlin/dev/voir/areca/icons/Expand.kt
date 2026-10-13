package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Maximize 2 icon.
 *
 * Converted from Lucide `maximize-2` (ISC).
 */
public val ArecaIcons.Expand: ImageVector
    get() = cachedExpand ?: ImageVector.Builder(
        name = "Expand",
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
            moveTo(15f, 3f)
            horizontalLineTo(21f)
            verticalLineTo(9f)
            moveTo(21f, 3f)
            lineTo(14f, 10f)
            moveTo(3f, 21f)
            lineTo(10f, 14f)
            moveTo(9f, 21f)
            horizontalLineTo(3f)
            verticalLineTo(15f)
        }
    }.build().also { cachedExpand = it }

/** `Expand` vector, built on first access and reused afterwards. */
private var cachedExpand: ImageVector? = null
