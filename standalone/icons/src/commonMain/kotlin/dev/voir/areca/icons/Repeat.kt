package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Repeat icon.
 *
 * Converted from Lucide `repeat` (ISC).
 */
public val ArecaIcons.Repeat: ImageVector
    get() = cachedRepeat ?: ImageVector.Builder(
        name = "Repeat",
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
            moveTo(17f, 2f)
            lineTo(21f, 6f)
            lineTo(17f, 10f)
            moveTo(3f, 11f)
            verticalLineTo(10f)
            arcTo(4f, 4f, 0f, false, true, 7f, 6f)
            horizontalLineTo(21f)
            moveTo(7f, 22f)
            lineTo(3f, 18f)
            lineTo(7f, 14f)
            moveTo(21f, 13f)
            verticalLineTo(14f)
            arcTo(4f, 4f, 0f, false, true, 17f, 18f)
            horizontalLineTo(3f)
        }
    }.build().also { cachedRepeat = it }

/** `Repeat` vector, built on first access and reused afterwards. */
private var cachedRepeat: ImageVector? = null
