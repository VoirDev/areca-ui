package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Music plus icon.
 *
 * Converted from Tabler Icons `music-plus` (MIT).
 */
public val ArecaIcons.MusicNotePlus: ImageVector
    get() = cachedMusicNotePlus ?: ImageVector.Builder(
        name = "MusicNotePlus",
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
            moveTo(3f, 17f)
            arcTo(3f, 3f, 0f, true, false, 9f, 17f)
            arcTo(3f, 3f, 0f, false, false, 3f, 17f)
            moveTo(9f, 17f)
            verticalLineTo(4f)
            horizontalLineTo(19f)
            verticalLineTo(12f)
            moveTo(9f, 8f)
            horizontalLineTo(19f)
            moveTo(16f, 19f)
            horizontalLineTo(22f)
            moveTo(19f, 16f)
            verticalLineTo(22f)
        }
    }.build().also { cachedMusicNotePlus = it }

/** `MusicNotePlus` vector, built on first access and reused afterwards. */
private var cachedMusicNotePlus: ImageVector? = null
