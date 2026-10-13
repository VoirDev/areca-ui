package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Key round icon.
 *
 * Converted from Lucide `key-round` (ISC).
 */
public val ArecaIcons.KeyRound: ImageVector
    get() = cachedKeyRound ?: ImageVector.Builder(
        name = "KeyRound",
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
            moveTo(2.586f, 17.414f)
            arcTo(2f, 2f, 0f, false, false, 2f, 18.828f)
            verticalLineTo(21f)
            arcTo(1f, 1f, 0f, false, false, 3f, 22f)
            horizontalLineTo(6f)
            arcTo(1f, 1f, 0f, false, false, 7f, 21f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 8f, 19f)
            horizontalLineTo(9f)
            arcTo(1f, 1f, 0f, false, false, 10f, 18f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 11f, 16f)
            horizontalLineTo(11.172f)
            arcTo(2f, 2f, 0f, false, false, 12.586f, 15.414f)
            lineTo(13.4f, 14.6f)
            arcTo(6.5f, 6.5f, 0f, true, false, 9.4f, 10.6f)
            close()
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(16f, 7.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 17f, 7.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 16f, 7.5f)
            close()
        }
    }.build().also { cachedKeyRound = it }

/** `KeyRound` vector, built on first access and reused afterwards. */
private var cachedKeyRound: ImageVector? = null
