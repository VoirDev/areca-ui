package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Moon icon.
 *
 * Converted from Tabler Icons `moon` (MIT).
 */
public val ArecaIcons.MoonAlt: ImageVector
    get() = cachedMoonAlt ?: ImageVector.Builder(
        name = "MoonAlt",
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
            moveTo(12f, 3f)
            curveTo(12.132f, 3f, 12.263f, 3f, 12.393f, 3f)
            arcTo(7.5f, 7.5f, 0f, false, false, 20.313f, 15.446f)
            arcTo(9f, 9f, 0f, true, true, 12f, 2.992f)
            lineTo(12f, 3f)
        }
    }.build().also { cachedMoonAlt = it }

/** `MoonAlt` vector, built on first access and reused afterwards. */
private var cachedMoonAlt: ImageVector? = null
