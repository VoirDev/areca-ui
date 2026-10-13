package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Candy icon.
 *
 * Converted from Lucide `candy` (ISC).
 */
public val ArecaIcons.Candy: ImageVector
    get() = cachedCandy ?: ImageVector.Builder(
        name = "Candy",
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
            moveTo(10f, 7f)
            verticalLineTo(17.9f)
            moveTo(14f, 6.1f)
            verticalLineTo(17f)
            moveTo(16f, 7f)
            verticalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 17.707f, 2.293f)
            arcTo(2.5f, 2.5f, 0f, false, false, 19.859f, 3.01f)
            arcTo(1f, 1f, 0f, false, true, 20.99f, 4.141f)
            arcTo(2.5f, 2.5f, 0f, false, false, 21.707f, 6.293f)
            arcTo(1f, 1f, 0f, false, true, 21f, 8f)
            horizontalLineTo(17f)
            moveTo(16.536f, 7.465f)
            arcTo(5f, 5f, 0f, false, false, 9.464f, 7.465f)
            lineTo(7.464f, 9.465f)
            arcTo(5f, 5f, 0f, false, false, 7.464f, 16.535f)
            arcTo(5f, 5f, 0f, false, false, 14.536f, 16.535f)
            lineTo(16.536f, 14.535f)
            arcTo(5f, 5f, 0f, false, false, 16.536f, 7.465f)
            moveTo(8f, 17f)
            verticalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 6.293f, 21.707f)
            arcTo(2.5f, 2.5f, 0f, false, false, 4.141f, 20.99f)
            arcTo(1f, 1f, 0f, false, true, 3.01f, 19.859f)
            arcTo(2.5f, 2.5f, 0f, false, false, 2.293f, 17.707f)
            arcTo(1f, 1f, 0f, false, true, 3f, 16f)
            horizontalLineTo(7f)
        }
    }.build().also { cachedCandy = it }

/** `Candy` vector, built on first access and reused afterwards. */
private var cachedCandy: ImageVector? = null
