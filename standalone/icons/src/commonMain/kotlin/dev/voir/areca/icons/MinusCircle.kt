package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle minus icon.
 *
 * Converted from Lucide `circle-minus` (ISC).
 */
public val ArecaIcons.MinusCircle: ImageVector
    get() = cachedMinusCircle ?: ImageVector.Builder(
        name = "MinusCircle",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(8f, 12f)
            horizontalLineTo(16f)
        }
    }.build().also { cachedMinusCircle = it }

/** `MinusCircle` vector, built on first access and reused afterwards. */
private var cachedMinusCircle: ImageVector? = null
