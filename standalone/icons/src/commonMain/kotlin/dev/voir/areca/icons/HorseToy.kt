package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Horse toy icon.
 *
 * Converted from Tabler Icons `horse-toy` (MIT).
 */
public val ArecaIcons.HorseToy: ImageVector
    get() = cachedHorseToy ?: ImageVector.Builder(
        name = "HorseToy",
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
            moveTo(3.5f, 17.5f)
            curveTo(9.167f, 22.167f, 14.833f, 22.167f, 20.5f, 17.5f)
            moveTo(19f, 18.5f)
            lineTo(17f, 10f)
            lineTo(18f, 8f)
            lineTo(20f, 9f)
            lineTo(21.5f, 7.5f)
            lineTo(19f, 3f)
            curveTo(13.948f, 3.218f, 13.01f, 6.133f, 12f, 9f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, false, 3f, 12f)
            moveTo(5f, 18.5f)
            lineTo(7f, 9f)
            moveTo(8f, 20f)
            lineTo(10f, 15f)
            horizontalLineTo(14f)
            lineTo(16f, 20f)
        }
    }.build().also { cachedHorseToy = it }

/** `HorseToy` vector, built on first access and reused afterwards. */
private var cachedHorseToy: ImageVector? = null
