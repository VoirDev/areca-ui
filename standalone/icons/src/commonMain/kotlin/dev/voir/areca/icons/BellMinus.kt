package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bell minus icon.
 *
 * Converted from Lucide `bell-minus` (ISC).
 */
public val ArecaIcons.BellMinus: ImageVector
    get() = cachedBellMinus ?: ImageVector.Builder(
        name = "BellMinus",
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
            moveTo(10.268f, 21f)
            arcTo(2f, 2f, 0f, false, false, 13.732f, 21f)
            moveTo(15f, 8f)
            horizontalLineTo(21f)
            moveTo(16.243f, 3.757f)
            arcTo(6f, 6f, 0f, false, false, 6f, 8f)
            curveTo(6f, 12.499f, 4.589f, 13.956f, 3.262f, 15.326f)
            arcTo(1f, 1f, 0f, false, false, 4f, 17f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, false, 20.74f, 15.327f)
            arcTo(9.4f, 9.4f, 0f, false, true, 18.667f, 12f)
        }
    }.build().also { cachedBellMinus = it }

/** `BellMinus` vector, built on first access and reused afterwards. */
private var cachedBellMinus: ImageVector? = null
