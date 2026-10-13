package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Corner down left icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `corner-down-left` (ISC).
 */
public val ArecaIcons.CornerDownLeft: ImageVector
    get() = cachedCornerDownLeft ?: ImageVector.Builder(
        name = "CornerDownLeft",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(20f, 4f)
            verticalLineTo(11f)
            arcTo(4f, 4f, 0f, false, true, 16f, 15f)
            horizontalLineTo(4f)
            moveTo(9f, 10f)
            lineTo(4f, 15f)
            lineTo(9f, 20f)
        }
    }.build().also { cachedCornerDownLeft = it }

/** `CornerDownLeft` vector, built on first access and reused afterwards. */
private var cachedCornerDownLeft: ImageVector? = null
