package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Walk icon.
 *
 * Converted from Tabler Icons `walk` (MIT).
 */
public val ArecaIcons.Walk: ImageVector
    get() = cachedWalk ?: ImageVector.Builder(
        name = "Walk",
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
            moveTo(12f, 4f)
            arcTo(1f, 1f, 0f, true, false, 14f, 4f)
            arcTo(1f, 1f, 0f, true, false, 12f, 4f)
            moveTo(7f, 21f)
            lineTo(10f, 17f)
            moveTo(16f, 21f)
            lineTo(14f, 17f)
            lineTo(11f, 14f)
            lineTo(12f, 8f)
            moveTo(6f, 12f)
            lineTo(8f, 9f)
            lineTo(12f, 8f)
            lineTo(15f, 11f)
            lineTo(18f, 12f)
        }
    }.build().also { cachedWalk = it }

/** `Walk` vector, built on first access and reused afterwards. */
private var cachedWalk: ImageVector? = null
