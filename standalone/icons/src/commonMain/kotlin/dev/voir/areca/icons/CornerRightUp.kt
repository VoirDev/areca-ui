package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Corner right up icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `corner-right-up` (ISC).
 */
public val ArecaIcons.CornerRightUp: ImageVector
    get() = cachedCornerRightUp ?: ImageVector.Builder(
        name = "CornerRightUp",
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
            moveTo(10f, 9f)
            lineTo(15f, 4f)
            lineTo(20f, 9f)
            moveTo(4f, 20f)
            horizontalLineTo(11f)
            arcTo(4f, 4f, 0f, false, false, 15f, 16f)
            verticalLineTo(4f)
        }
    }.build().also { cachedCornerRightUp = it }

/** `CornerRightUp` vector, built on first access and reused afterwards. */
private var cachedCornerRightUp: ImageVector? = null
