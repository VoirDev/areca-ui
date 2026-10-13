package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mirror round icon.
 *
 * Converted from Lucide `mirror-round` (ISC).
 */
public val ArecaIcons.MirrorRound: ImageVector
    get() = cachedMirrorRound ?: ImageVector.Builder(
        name = "MirrorRound",
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
            moveTo(10f, 6.6f)
            lineTo(8.6f, 8f)
            moveTo(12f, 18f)
            verticalLineTo(22f)
            moveTo(15f, 7.5f)
            lineTo(9.5f, 13f)
            moveTo(7f, 22f)
            horizontalLineTo(17f)
            moveTo(4f, 10f)
            arcTo(8f, 8f, 0f, true, true, 20f, 10f)
            arcTo(8f, 8f, 0f, true, true, 4f, 10f)
            close()
        }
    }.build().also { cachedMirrorRound = it }

/** `MirrorRound` vector, built on first access and reused afterwards. */
private var cachedMirrorRound: ImageVector? = null
