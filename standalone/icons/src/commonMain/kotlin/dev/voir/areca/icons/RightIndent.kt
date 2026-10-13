package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * List indent decrease icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `list-indent-decrease` (ISC).
 */
public val ArecaIcons.RightIndent: ImageVector
    get() = cachedRightIndent ?: ImageVector.Builder(
        name = "RightIndent",
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
            moveTo(21f, 5f)
            horizontalLineTo(11f)
            moveTo(21f, 12f)
            horizontalLineTo(11f)
            moveTo(21f, 19f)
            horizontalLineTo(11f)
            moveTo(7f, 8f)
            lineTo(3f, 12f)
            lineTo(7f, 16f)
        }
    }.build().also { cachedRightIndent = it }

/** `RightIndent` vector, built on first access and reused afterwards. */
private var cachedRightIndent: ImageVector? = null
