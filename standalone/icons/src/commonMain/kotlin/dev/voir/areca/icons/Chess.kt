package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chess icon.
 *
 * Converted from Tabler Icons `chess` (MIT).
 */
public val ArecaIcons.Chess: ImageVector
    get() = cachedChess ?: ImageVector.Builder(
        name = "Chess",
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
            arcTo(3f, 3f, 0f, false, true, 15f, 6f)
            curveTo(15f, 7.113f, 14.4f, 8.482f, 13.5f, 9f)
            lineTo(15f, 16f)
            horizontalLineTo(9f)
            lineTo(10.5f, 9f)
            curveTo(9.6f, 8.482f, 9f, 7.113f, 9f, 6f)
            arcTo(3f, 3f, 0f, false, true, 12f, 3f)
            moveTo(8f, 9f)
            horizontalLineTo(16f)
            moveTo(6.684f, 16.772f)
            arcTo(1f, 1f, 0f, false, false, 6f, 17.721f)
            verticalLineTo(19f)
            arcTo(1f, 1f, 0f, false, false, 7f, 20f)
            horizontalLineTo(17f)
            arcTo(1f, 1f, 0f, false, false, 18f, 19f)
            verticalLineTo(17.72f)
            arcTo(1f, 1f, 0f, false, false, 17.316f, 16.772f)
            lineTo(15f, 16f)
            horizontalLineTo(9f)
            lineTo(6.684f, 16.772f)
        }
    }.build().also { cachedChess = it }

/** `Chess` vector, built on first access and reused afterwards. */
private var cachedChess: ImageVector? = null
