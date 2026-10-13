package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cup icon.
 *
 * Converted from Tabler Icons `cup` (MIT).
 */
public val ArecaIcons.Cup: ImageVector
    get() = cachedCup ?: ImageVector.Builder(
        name = "Cup",
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
            moveTo(5f, 11f)
            horizontalLineTo(19f)
            verticalLineTo(8f)
            horizontalLineTo(5f)
            lineTo(5f, 11f)
            moveTo(17.5f, 11f)
            lineTo(16f, 21f)
            horizontalLineTo(8f)
            lineTo(6.5f, 11f)
            moveTo(6f, 8f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 8f, 5f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 18f, 7f)
            verticalLineTo(8f)
            moveTo(15f, 5f)
            verticalLineTo(3f)
        }
    }.build().also { cachedCup = it }

/** `Cup` vector, built on first access and reused afterwards. */
private var cachedCup: ImageVector? = null
