package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Music icon.
 *
 * Converted from Lucide `music` (ISC).
 */
public val ArecaIcons.MusicNote: ImageVector
    get() = cachedMusicNote ?: ImageVector.Builder(
        name = "MusicNote",
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
            moveTo(9f, 18f)
            verticalLineTo(5f)
            lineTo(21f, 3f)
            verticalLineTo(16f)
            moveTo(3f, 18f)
            arcTo(3f, 3f, 0f, true, true, 9f, 18f)
            arcTo(3f, 3f, 0f, true, true, 3f, 18f)
            close()
            moveTo(15f, 16f)
            arcTo(3f, 3f, 0f, true, true, 21f, 16f)
            arcTo(3f, 3f, 0f, true, true, 15f, 16f)
            close()
        }
    }.build().also { cachedMusicNote = it }

/** `MusicNote` vector, built on first access and reused afterwards. */
private var cachedMusicNote: ImageVector? = null
