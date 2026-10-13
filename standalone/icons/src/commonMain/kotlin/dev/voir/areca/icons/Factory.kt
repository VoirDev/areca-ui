package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Factory icon.
 *
 * Converted from Lucide `factory` (ISC).
 */
public val ArecaIcons.Factory: ImageVector
    get() = cachedFactory ?: ImageVector.Builder(
        name = "Factory",
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
            moveTo(12f, 16f)
            horizontalLineTo(12.01f)
            moveTo(16f, 16f)
            horizontalLineTo(16.01f)
            moveTo(3f, 19f)
            arcTo(2f, 2f, 0f, false, false, 5f, 21f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 21f, 19f)
            verticalLineTo(8.5f)
            arcTo(0.5f, 0.5f, 0f, false, false, 20.231f, 8.078f)
            lineTo(15.769f, 10.922f)
            arcTo(0.5f, 0.5f, 0f, false, true, 15f, 10.5f)
            verticalLineTo(8.5f)
            arcTo(0.5f, 0.5f, 0f, false, false, 14.231f, 8.078f)
            lineTo(9.77f, 10.922f)
            arcTo(0.5f, 0.5f, 0f, false, true, 9f, 10.5f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 7f, 3f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 3f, 5f)
            close()
            moveTo(8f, 16f)
            horizontalLineTo(8.01f)
        }
    }.build().also { cachedFactory = it }

/** `Factory` vector, built on first access and reused afterwards. */
private var cachedFactory: ImageVector? = null
