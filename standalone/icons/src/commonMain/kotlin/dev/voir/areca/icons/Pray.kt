package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pray icon.
 *
 * Converted from Tabler Icons `pray` (MIT).
 */
public val ArecaIcons.Pray: ImageVector
    get() = cachedPray ?: ImageVector.Builder(
        name = "Pray",
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
            moveTo(11f, 5f)
            arcTo(1f, 1f, 0f, true, false, 13f, 5f)
            arcTo(1f, 1f, 0f, true, false, 11f, 5f)
            moveTo(7f, 20f)
            horizontalLineTo(15f)
            lineTo(11f, 16f)
            verticalLineTo(9f)
            lineTo(15f, 12f)
            lineTo(17f, 10f)
        }
    }.build().also { cachedPray = it }

/** `Pray` vector, built on first access and reused afterwards. */
private var cachedPray: ImageVector? = null
