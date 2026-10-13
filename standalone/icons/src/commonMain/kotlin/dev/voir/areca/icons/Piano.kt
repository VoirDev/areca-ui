package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Piano icon.
 *
 * Converted from Lucide `piano` (ISC).
 */
public val ArecaIcons.Piano: ImageVector
    get() = cachedPiano ?: ImageVector.Builder(
        name = "Piano",
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
            moveTo(10f, 13f)
            verticalLineTo(17f)
            moveTo(14f, 13f)
            verticalLineTo(17f)
            moveTo(18f, 13f)
            verticalLineTo(17f)
            moveTo(2f, 13f)
            horizontalLineTo(22f)
            moveTo(22f, 11.5f)
            arcTo(3.5f, 3.5f, 0f, false, false, 18.5f, 8f)
            arcTo(3.52f, 3.52f, 0f, false, true, 15.327f, 6f)
            arcTo(7f, 7f, 0f, false, false, 2f, 9f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 4f, 21f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 19f)
            close()
            moveTo(6f, 13f)
            verticalLineTo(17f)
        }
    }.build().also { cachedPiano = it }

/** `Piano` vector, built on first access and reused afterwards. */
private var cachedPiano: ImageVector? = null
