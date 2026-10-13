package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Notebook icon.
 *
 * Converted from Lucide `notebook` (ISC).
 */
public val ArecaIcons.Notebook: ImageVector
    get() = cachedNotebook ?: ImageVector.Builder(
        name = "Notebook",
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
            moveTo(2f, 6f)
            horizontalLineTo(6f)
            moveTo(2f, 10f)
            horizontalLineTo(6f)
            moveTo(2f, 14f)
            horizontalLineTo(6f)
            moveTo(2f, 18f)
            horizontalLineTo(6f)
            moveTo(6f, 2f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 4f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 20f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 6f, 2f)
            close()
            moveTo(16f, 2f)
            verticalLineTo(22f)
        }
    }.build().also { cachedNotebook = it }

/** `Notebook` vector, built on first access and reused afterwards. */
private var cachedNotebook: ImageVector? = null
