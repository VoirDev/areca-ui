package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Typography icon.
 *
 * Converted from Tabler Icons `typography` (MIT).
 */
public val ArecaIcons.TypeAlt: ImageVector
    get() = cachedTypeAlt ?: ImageVector.Builder(
        name = "TypeAlt",
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
            moveTo(4f, 20f)
            lineTo(7f, 20f)
            moveTo(14f, 20f)
            lineTo(21f, 20f)
            moveTo(6.9f, 15f)
            lineTo(13.8f, 15f)
            moveTo(10.2f, 6.3f)
            lineTo(16f, 20f)
            moveTo(5f, 20f)
            lineTo(11f, 4f)
            lineTo(13f, 4f)
            lineTo(20f, 20f)
        }
    }.build().also { cachedTypeAlt = it }

/** `TypeAlt` vector, built on first access and reused afterwards. */
private var cachedTypeAlt: ImageVector? = null
