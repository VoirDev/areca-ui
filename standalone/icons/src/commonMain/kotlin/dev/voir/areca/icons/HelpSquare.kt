package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Help square icon.
 *
 * Converted from Tabler Icons `help-square` (MIT).
 */
public val ArecaIcons.HelpSquare: ImageVector
    get() = cachedHelpSquare ?: ImageVector.Builder(
        name = "HelpSquare",
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
            moveTo(3f, 5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            moveTo(12f, 16f)
            verticalLineTo(16.01f)
            moveTo(12f, 13f)
            arcTo(2f, 2f, 0f, false, false, 12.914f, 9.218f)
            arcTo(1.98f, 1.98f, 0f, false, false, 10.5f, 9.701f)
        }
    }.build().also { cachedHelpSquare = it }

/** `HelpSquare` vector, built on first access and reused afterwards. */
private var cachedHelpSquare: ImageVector? = null
