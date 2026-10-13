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
 * Converted from Tabler Icons `hash` (MIT).
 */
public val ArecaIcons.HashAlt: ImageVector
    get() = cachedHashAlt ?: ImageVector.Builder(
        name = "HashAlt",
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
            moveTo(5f, 9f)
            lineTo(19f, 9f)
            moveTo(5f, 15f)
            lineTo(19f, 15f)
            moveTo(11f, 4f)
            lineTo(7f, 20f)
            moveTo(17f, 4f)
            lineTo(13f, 20f)
        }
    }.build().also { cachedHashAlt = it }

/** `HashAlt` vector, built on first access and reused afterwards. */
private var cachedHashAlt: ImageVector? = null
