package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Text cursor input icon.
 *
 * Converted from Lucide `text-cursor-input` (ISC).
 */
public val ArecaIcons.TextInput: ImageVector
    get() = cachedTextInput ?: ImageVector.Builder(
        name = "TextInput",
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
            moveTo(12f, 20f)
            horizontalLineTo(11f)
            arcTo(2f, 2f, 0f, false, true, 9f, 18f)
            arcTo(2f, 2f, 0f, false, true, 7f, 20f)
            horizontalLineTo(6f)
            moveTo(13f, 8f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 10f)
            verticalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 20f, 16f)
            horizontalLineTo(13f)
            moveTo(5f, 16f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 14f)
            verticalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 4f, 8f)
            horizontalLineTo(5f)
            moveTo(6f, 4f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 9f, 6f)
            arcTo(2f, 2f, 0f, false, true, 11f, 4f)
            horizontalLineTo(12f)
            moveTo(9f, 6f)
            verticalLineTo(18f)
        }
    }.build().also { cachedTextInput = it }

/** `TextInput` vector, built on first access and reused afterwards. */
private var cachedTextInput: ImageVector? = null
