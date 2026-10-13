package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Puzzle icon.
 *
 * Same drawing as `ArecaIcons.Puzzle`; both names are kept.
 *
 * Converted from Lucide `puzzle` (ISC).
 */
public val ArecaIcons.PuzzlePiece: ImageVector
    get() = cachedPuzzlePiece ?: ImageVector.Builder(
        name = "PuzzlePiece",
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
            moveTo(15.39f, 4.39f)
            arcTo(1f, 1f, 0f, false, false, 17.07f, 3.916f)
            arcTo(2.5f, 2.5f, 0f, true, true, 20.084f, 6.931f)
            arcTo(1f, 1f, 0f, false, false, 19.61f, 8.611f)
            lineTo(21.293f, 10.293f)
            arcTo(2.414f, 2.414f, 0f, false, true, 21.293f, 13.707f)
            lineTo(19.61f, 15.39f)
            arcTo(1f, 1f, 0f, false, true, 17.93f, 14.916f)
            arcTo(2.5f, 2.5f, 0f, true, false, 14.916f, 17.931f)
            arcTo(1f, 1f, 0f, false, true, 15.39f, 19.611f)
            lineTo(13.707f, 21.293f)
            arcTo(2.414f, 2.414f, 0f, false, true, 10.293f, 21.293f)
            lineTo(8.61f, 19.61f)
            arcTo(1f, 1f, 0f, false, false, 6.93f, 20.084f)
            arcTo(2.5f, 2.5f, 0f, true, true, 3.916f, 17.069f)
            arcTo(1f, 1f, 0f, false, false, 4.39f, 15.389f)
            lineTo(2.707f, 13.707f)
            arcTo(2.414f, 2.414f, 0f, false, true, 2.707f, 10.293f)
            lineTo(4.39f, 8.61f)
            arcTo(1f, 1f, 0f, false, true, 6.07f, 9.084f)
            arcTo(2.5f, 2.5f, 0f, true, false, 9.084f, 6.069f)
            arcTo(1f, 1f, 0f, false, true, 8.61f, 4.389f)
            lineTo(10.293f, 2.707f)
            arcTo(2.414f, 2.414f, 0f, false, true, 13.707f, 2.707f)
            close()
        }
    }.build().also { cachedPuzzlePiece = it }

/** `PuzzlePiece` vector, built on first access and reused afterwards. */
private var cachedPuzzlePiece: ImageVector? = null
