package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wand icon.
 *
 * Converted from Lucide `wand` (ISC).
 */
public val ArecaIcons.MagicWand02: ImageVector
    get() = cachedMagicWand02 ?: ImageVector.Builder(
        name = "MagicWand02",
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
            moveTo(15f, 4f)
            verticalLineTo(2f)
            moveTo(15f, 16f)
            verticalLineTo(14f)
            moveTo(8f, 9f)
            horizontalLineTo(10f)
            moveTo(20f, 9f)
            horizontalLineTo(22f)
            moveTo(17.8f, 11.8f)
            lineTo(19f, 13f)
            moveTo(15f, 9f)
            horizontalLineTo(15.01f)
            moveTo(17.8f, 6.2f)
            lineTo(19f, 5f)
            moveTo(3f, 21f)
            lineTo(12f, 12f)
            moveTo(12.2f, 6.2f)
            lineTo(11f, 5f)
        }
    }.build().also { cachedMagicWand02 = it }

/** `MagicWand02` vector, built on first access and reused afterwards. */
private var cachedMagicWand02: ImageVector? = null
