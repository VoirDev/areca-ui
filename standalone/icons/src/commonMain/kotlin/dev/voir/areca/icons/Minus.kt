package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Minus icon.
 *
 * Converted from Lucide `minus` (ISC).
 */
public val ArecaIcons.Minus: ImageVector
    get() = cachedMinus ?: ImageVector.Builder(
        name = "Minus",
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
            moveTo(5f, 12f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedMinus = it }

/** `Minus` vector, built on first access and reused afterwards. */
private var cachedMinus: ImageVector? = null
