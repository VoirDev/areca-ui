package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Gavel icon.
 *
 * Converted from Lucide `gavel` (ISC).
 */
public val ArecaIcons.Gavel: ImageVector
    get() = cachedGavel ?: ImageVector.Builder(
        name = "Gavel",
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
            moveTo(14f, 13f)
            lineTo(5.619f, 21.38f)
            arcTo(1f, 1f, 0f, false, true, 2.618f, 18.38f)
            lineTo(11.002f, 9.999f)
            moveTo(16f, 16f)
            lineTo(22f, 10f)
            moveTo(21.5f, 10.5f)
            lineTo(13.5f, 2.5f)
            moveTo(8f, 8f)
            lineTo(14f, 2f)
            moveTo(8.5f, 7.5f)
            lineTo(16.5f, 15.5f)
        }
    }.build().also { cachedGavel = it }

/** `Gavel` vector, built on first access and reused afterwards. */
private var cachedGavel: ImageVector? = null
