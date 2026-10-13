package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mountain icon.
 *
 * Converted from Lucide `mountain` (ISC).
 */
public val ArecaIcons.Mountain: ImageVector
    get() = cachedMountain ?: ImageVector.Builder(
        name = "Mountain",
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
            moveTo(8f, 3f)
            lineTo(12f, 11f)
            lineTo(17f, 6f)
            lineTo(22f, 21f)
            horizontalLineTo(2f)
            lineTo(8f, 3f)
            close()
        }
    }.build().also { cachedMountain = it }

/** `Mountain` vector, built on first access and reused afterwards. */
private var cachedMountain: ImageVector? = null
