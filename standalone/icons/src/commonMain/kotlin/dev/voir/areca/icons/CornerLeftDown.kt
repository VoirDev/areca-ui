package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Corner left down icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `corner-left-down` (ISC).
 */
public val ArecaIcons.CornerLeftDown: ImageVector
    get() = cachedCornerLeftDown ?: ImageVector.Builder(
        name = "CornerLeftDown",
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
            moveTo(14f, 15f)
            lineTo(9f, 20f)
            lineTo(4f, 15f)
            moveTo(20f, 4f)
            horizontalLineTo(13f)
            arcTo(4f, 4f, 0f, false, false, 9f, 8f)
            verticalLineTo(20f)
        }
    }.build().also { cachedCornerLeftDown = it }

/** `CornerLeftDown` vector, built on first access and reused afterwards. */
private var cachedCornerLeftDown: ImageVector? = null
