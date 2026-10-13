package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Church icon.
 *
 * Converted from Lucide `church` (ISC).
 */
public val ArecaIcons.Church: ImageVector
    get() = cachedChurch ?: ImageVector.Builder(
        name = "Church",
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
            moveTo(10f, 9f)
            horizontalLineTo(14f)
            moveTo(12f, 7f)
            verticalLineTo(12f)
            moveTo(14f, 21f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 10f, 18f)
            verticalLineTo(21f)
            moveTo(18f, 9f)
            lineTo(21.52f, 11.147f)
            arcTo(1f, 1f, 0f, false, true, 22f, 12.001f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 20f, 21f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 19f)
            verticalLineTo(12.001f)
            arcTo(1f, 1f, 0f, false, true, 2.48f, 11.147f)
            lineTo(6f, 9f)
            moveTo(6f, 21f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 6.376f, 6.218f)
            lineTo(11.376f, 2.219f)
            arcTo(1f, 1f, 0f, false, true, 12.625f, 2.22f)
            lineTo(17.625f, 6.22f)
            arcTo(1f, 1f, 0f, false, true, 18f, 7f)
            verticalLineTo(21f)
        }
    }.build().also { cachedChurch = it }

/** `Church` vector, built on first access and reused afterwards. */
private var cachedChurch: ImageVector? = null
