package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Corner up right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `corner-up-right` (ISC).
 */
public val ArecaIcons.CornerUpRight: ImageVector
    get() = cachedCornerUpRight ?: ImageVector.Builder(
        name = "CornerUpRight",
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
            moveTo(15f, 14f)
            lineTo(20f, 9f)
            lineTo(15f, 4f)
            moveTo(4f, 20f)
            verticalLineTo(13f)
            arcTo(4f, 4f, 0f, false, true, 8f, 9f)
            horizontalLineTo(20f)
        }
    }.build().also { cachedCornerUpRight = it }

/** `CornerUpRight` vector, built on first access and reused afterwards. */
private var cachedCornerUpRight: ImageVector? = null
