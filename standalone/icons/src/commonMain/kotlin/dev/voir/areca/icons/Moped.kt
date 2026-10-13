package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Moped icon.
 *
 * Converted from Tabler Icons `moped` (MIT).
 */
public val ArecaIcons.Moped: ImageVector
    get() = cachedMoped ?: ImageVector.Builder(
        name = "Moped",
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
            moveTo(16f, 17f)
            arcTo(2f, 2f, 0f, true, false, 20f, 17f)
            arcTo(2f, 2f, 0f, true, false, 16f, 17f)
            moveTo(5f, 16f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 9f, 17f)
            verticalLineTo(12f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, false, 3f, 15f)
            verticalLineTo(16f)
            horizontalLineTo(13f)
            arcTo(6f, 6f, 0f, false, true, 18f, 12f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 16f, 5f)
            horizontalLineTo(15f)
            moveTo(6f, 9f)
            lineTo(9f, 9f)
        }
    }.build().also { cachedMoped = it }

/** `Moped` vector, built on first access and reused afterwards. */
private var cachedMoped: ImageVector? = null
