package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bird icon.
 *
 * Converted from Lucide `bird` (ISC).
 */
public val ArecaIcons.Bird: ImageVector
    get() = cachedBird ?: ImageVector.Builder(
        name = "Bird",
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
            moveTo(16f, 7f)
            horizontalLineTo(16.01f)
            moveTo(3.4f, 18f)
            horizontalLineTo(12f)
            arcTo(8f, 8f, 0f, false, false, 20f, 10f)
            verticalLineTo(7f)
            arcTo(4f, 4f, 0f, false, false, 12.72f, 4.7f)
            lineTo(2f, 20f)
            moveTo(20f, 7f)
            lineTo(22f, 7.5f)
            lineTo(20f, 8f)
            moveTo(10f, 18f)
            verticalLineTo(21f)
            moveTo(14f, 17.75f)
            verticalLineTo(21f)
            moveTo(7f, 18f)
            arcTo(6f, 6f, 0f, false, false, 10.84f, 7.39f)
        }
    }.build().also { cachedBird = it }

/** `Bird` vector, built on first access and reused afterwards. */
private var cachedBird: ImageVector? = null
