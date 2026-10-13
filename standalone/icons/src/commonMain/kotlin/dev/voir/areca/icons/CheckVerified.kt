package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Badge check icon.
 *
 * Converted from Lucide `badge-check` (ISC).
 */
public val ArecaIcons.CheckVerified: ImageVector
    get() = cachedCheckVerified ?: ImageVector.Builder(
        name = "CheckVerified",
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
            moveTo(3.85f, 8.62f)
            arcTo(4f, 4f, 0f, false, true, 8.63f, 3.85f)
            arcTo(4f, 4f, 0f, false, true, 15.37f, 3.85f)
            arcTo(4f, 4f, 0f, false, true, 20.15f, 8.63f)
            arcTo(4f, 4f, 0f, false, true, 20.15f, 15.37f)
            arcTo(4f, 4f, 0f, false, true, 15.38f, 20.15f)
            arcTo(4f, 4f, 0f, false, true, 8.63f, 20.15f)
            arcTo(4f, 4f, 0f, false, true, 3.85f, 15.38f)
            arcTo(4f, 4f, 0f, false, true, 3.85f, 8.62f)
            close()
            moveTo(16f, 9f)
            lineTo(10.5f, 14.5f)
            lineTo(8f, 12f)
        }
    }.build().also { cachedCheckVerified = it }

/** `CheckVerified` vector, built on first access and reused afterwards. */
private var cachedCheckVerified: ImageVector? = null
