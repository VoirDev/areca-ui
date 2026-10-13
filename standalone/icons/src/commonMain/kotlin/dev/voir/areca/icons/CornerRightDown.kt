package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Corner right down icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `corner-right-down` (ISC).
 */
public val ArecaIcons.CornerRightDown: ImageVector
    get() = cachedCornerRightDown ?: ImageVector.Builder(
        name = "CornerRightDown",
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
            moveTo(10f, 15f)
            lineTo(15f, 20f)
            lineTo(20f, 15f)
            moveTo(4f, 4f)
            horizontalLineTo(11f)
            arcTo(4f, 4f, 0f, false, true, 15f, 8f)
            verticalLineTo(20f)
        }
    }.build().also { cachedCornerRightDown = it }

/** `CornerRightDown` vector, built on first access and reused afterwards. */
private var cachedCornerRightDown: ImageVector? = null
