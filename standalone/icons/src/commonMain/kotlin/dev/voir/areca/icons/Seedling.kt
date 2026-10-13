package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Seedling icon.
 *
 * Converted from Tabler Icons `seedling` (MIT).
 */
public val ArecaIcons.Seedling: ImageVector
    get() = cachedSeedling ?: ImageVector.Builder(
        name = "Seedling",
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
            moveTo(12f, 10f)
            arcTo(6f, 6f, 0f, false, false, 6f, 4f)
            horizontalLineTo(3f)
            verticalLineTo(6f)
            arcTo(6f, 6f, 0f, false, false, 9f, 12f)
            horizontalLineTo(12f)
            moveTo(12f, 14f)
            arcTo(6f, 6f, 0f, false, true, 18f, 8f)
            horizontalLineTo(21f)
            verticalLineTo(9f)
            arcTo(6f, 6f, 0f, false, true, 15f, 15f)
            horizontalLineTo(12f)
            moveTo(12f, 20f)
            lineTo(12f, 10f)
        }
    }.build().also { cachedSeedling = it }

/** `Seedling` vector, built on first access and reused afterwards. */
private var cachedSeedling: ImageVector? = null
