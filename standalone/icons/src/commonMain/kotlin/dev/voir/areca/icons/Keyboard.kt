package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Keyboard icon.
 *
 * Converted from Lucide `keyboard` (ISC).
 */
public val ArecaIcons.Keyboard: ImageVector
    get() = cachedKeyboard ?: ImageVector.Builder(
        name = "Keyboard",
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
            moveTo(10f, 8f)
            horizontalLineTo(10.01f)
            moveTo(12f, 12f)
            horizontalLineTo(12.01f)
            moveTo(14f, 8f)
            horizontalLineTo(14.01f)
            moveTo(16f, 12f)
            horizontalLineTo(16.01f)
            moveTo(18f, 8f)
            horizontalLineTo(18.01f)
            moveTo(6f, 8f)
            horizontalLineTo(6.01f)
            moveTo(7f, 16f)
            horizontalLineTo(17f)
            moveTo(8f, 12f)
            horizontalLineTo(8.01f)
            moveTo(4f, 4f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 6f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 20f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 18f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 4f)
            close()
        }
    }.build().also { cachedKeyboard = it }

/** `Keyboard` vector, built on first access and reused afterwards. */
private var cachedKeyboard: ImageVector? = null
