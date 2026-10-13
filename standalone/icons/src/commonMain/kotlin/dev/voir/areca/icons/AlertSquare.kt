package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Alert square rounded icon.
 *
 * Converted from Tabler Icons `alert-square-rounded` (MIT).
 */
public val ArecaIcons.AlertSquare: ImageVector
    get() = cachedAlertSquare ?: ImageVector.Builder(
        name = "AlertSquare",
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
            moveTo(12f, 3f)
            curveTo(19.2f, 3f, 21f, 4.8f, 21f, 12f)
            curveTo(21f, 19.2f, 19.2f, 21f, 12f, 21f)
            curveTo(4.8f, 21f, 3f, 19.2f, 3f, 12f)
            curveTo(3f, 4.8f, 4.8f, 3f, 12f, 3f)
            moveTo(12f, 8f)
            verticalLineTo(12f)
            moveTo(12f, 16f)
            horizontalLineTo(12.01f)
        }
    }.build().also { cachedAlertSquare = it }

/** `AlertSquare` vector, built on first access and reused afterwards. */
private var cachedAlertSquare: ImageVector? = null
