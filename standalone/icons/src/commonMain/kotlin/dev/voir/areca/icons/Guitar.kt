package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Guitar icon.
 *
 * Converted from Lucide `guitar` (ISC).
 */
public val ArecaIcons.Guitar: ImageVector
    get() = cachedGuitar ?: ImageVector.Builder(
        name = "Guitar",
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
            moveTo(11.9f, 12.1f)
            lineTo(16.414f, 7.586f)
            moveTo(20.1f, 2.3f)
            arcTo(1f, 1f, 0f, false, false, 18.7f, 2.3f)
            lineTo(17.586f, 3.414f)
            arcTo(2f, 2f, 0f, false, false, 17f, 4.828f)
            verticalLineTo(6.172f)
            arcTo(2f, 2f, 0f, false, true, 16.414f, 7.586f)
            arcTo(2f, 2f, 0f, false, true, 17.828f, 7f)
            horizontalLineTo(19.172f)
            arcTo(2f, 2f, 0f, false, false, 20.586f, 6.414f)
            lineTo(21.7f, 5.3f)
            arcTo(1f, 1f, 0f, false, false, 21.7f, 3.9f)
            close()
            moveTo(6f, 16f)
            lineTo(8f, 18f)
            moveTo(8.23f, 9.85f)
            arcTo(3f, 3f, 0f, false, true, 11f, 8f)
            arcTo(5f, 5f, 0f, false, true, 16f, 13f)
            arcTo(3f, 3f, 0f, false, true, 14.15f, 15.77f)
            lineTo(13.23f, 16.15f)
            arcTo(2f, 2f, 0f, false, false, 12f, 18f)
            arcTo(4f, 4f, 0f, false, true, 8f, 22f)
            arcTo(6f, 6f, 0f, false, true, 2f, 16f)
            arcTo(4f, 4f, 0f, false, true, 6f, 12f)
            arcTo(2f, 2f, 0f, false, false, 7.85f, 10.77f)
            close()
        }
    }.build().also { cachedGuitar = it }

/** `Guitar` vector, built on first access and reused afterwards. */
private var cachedGuitar: ImageVector? = null
