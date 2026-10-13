package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Utensils icon.
 *
 * Converted from Lucide `utensils` (ISC).
 */
public val ArecaIcons.Utensils: ImageVector
    get() = cachedUtensils ?: ImageVector.Builder(
        name = "Utensils",
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
            moveTo(3f, 2f)
            verticalLineTo(9f)
            curveTo(3f, 10.1f, 3.9f, 11f, 5f, 11f)
            horizontalLineTo(9f)
            arcTo(2f, 2f, 0f, false, false, 11f, 9f)
            verticalLineTo(2f)
            moveTo(7f, 2f)
            verticalLineTo(22f)
            moveTo(21f, 15f)
            verticalLineTo(2f)
            arcTo(5f, 5f, 0f, false, false, 16f, 7f)
            verticalLineTo(13f)
            curveTo(16f, 14.1f, 16.9f, 15f, 18f, 15f)
            horizontalLineTo(21f)
            close()
            moveTo(21f, 15f)
            verticalLineTo(22f)
        }
    }.build().also { cachedUtensils = it }

/** `Utensils` vector, built on first access and reused afterwards. */
private var cachedUtensils: ImageVector? = null
