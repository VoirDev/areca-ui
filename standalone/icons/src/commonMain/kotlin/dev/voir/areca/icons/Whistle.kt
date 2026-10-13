package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Whistle icon.
 *
 * Converted from Lucide `whistle` (ISC).
 */
public val ArecaIcons.Whistle: ImageVector
    get() = cachedWhistle ?: ImageVector.Builder(
        name = "Whistle",
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
            moveTo(10f, 6f)
            verticalLineTo(10f)
            moveTo(21f, 6f)
            arcTo(1f, 1f, 0f, false, true, 22f, 7f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 21f, 10f)
            horizontalLineTo(15.325f)
            arcTo(7f, 7f, 0f, true, true, 9f, 6f)
            close()
        }
    }.build().also { cachedWhistle = it }

/** `Whistle` vector, built on first access and reused afterwards. */
private var cachedWhistle: ImageVector? = null
