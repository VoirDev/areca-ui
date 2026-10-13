package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cash icon.
 *
 * Converted from Tabler Icons `cash` (MIT).
 */
public val ArecaIcons.Cash: ImageVector
    get() = cachedCash ?: ImageVector.Builder(
        name = "Cash",
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
            moveTo(7f, 15f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 14f)
            verticalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 4f, 5f)
            horizontalLineTo(16f)
            arcTo(1f, 1f, 0f, false, true, 17f, 6f)
            verticalLineTo(9f)
            moveTo(7f, 10f)
            arcTo(1f, 1f, 0f, false, true, 8f, 9f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 10f)
            verticalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 20f, 19f)
            horizontalLineTo(8f)
            arcTo(1f, 1f, 0f, false, true, 7f, 18f)
            lineTo(7f, 10f)
            moveTo(12f, 14f)
            arcTo(2f, 2f, 0f, true, false, 16f, 14f)
            arcTo(2f, 2f, 0f, false, false, 12f, 14f)
        }
    }.build().also { cachedCash = it }

/** `Cash` vector, built on first access and reused afterwards. */
private var cachedCash: ImageVector? = null
