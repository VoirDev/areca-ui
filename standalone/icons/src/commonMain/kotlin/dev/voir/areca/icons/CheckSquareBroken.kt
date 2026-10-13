package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Square check big icon.
 *
 * Converted from Lucide `square-check-big` (ISC).
 */
public val ArecaIcons.CheckSquareBroken: ImageVector
    get() = cachedCheckSquareBroken ?: ImageVector.Builder(
        name = "CheckSquareBroken",
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
            moveTo(21f, 10.656f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            horizontalLineTo(17.344f)
            moveTo(9f, 11f)
            lineTo(12f, 14f)
            lineTo(22f, 4f)
        }
    }.build().also { cachedCheckSquareBroken = it }

/** `CheckSquareBroken` vector, built on first access and reused afterwards. */
private var cachedCheckSquareBroken: ImageVector? = null
