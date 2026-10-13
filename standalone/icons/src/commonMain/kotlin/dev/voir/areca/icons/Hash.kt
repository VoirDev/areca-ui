package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hash icon.
 *
 * Converted from Lucide `hash` (ISC).
 */
public val ArecaIcons.Hash: ImageVector
    get() = cachedHash ?: ImageVector.Builder(
        name = "Hash",
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
            moveTo(4f, 9f)
            lineTo(20f, 9f)
            moveTo(4f, 15f)
            lineTo(20f, 15f)
            moveTo(10f, 3f)
            lineTo(8f, 21f)
            moveTo(16f, 3f)
            lineTo(14f, 21f)
        }
    }.build().also { cachedHash = it }

/** `Hash` vector, built on first access and reused afterwards. */
private var cachedHash: ImageVector? = null
