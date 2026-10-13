package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Podium icon.
 *
 * Converted from Lucide `podium` (ISC).
 */
public val ArecaIcons.Podium: ImageVector
    get() = cachedPodium ?: ImageVector.Builder(
        name = "Podium",
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
            moveTo(12f, 6f)
            verticalLineTo(2f)
            horizontalLineTo(11f)
            moveTo(9f, 15f)
            arcTo(1f, 1f, 0f, false, false, 8f, 14f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, false, 3f, 15f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, false, 4f, 21f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, false, 21f, 20f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, false, 20f, 16f)
            horizontalLineTo(16f)
            arcTo(1f, 1f, 0f, false, false, 15f, 17f)
            moveTo(9f, 21f)
            verticalLineTo(11f)
            arcTo(1f, 1f, 0f, false, true, 10f, 10f)
            horizontalLineTo(14f)
            arcTo(1f, 1f, 0f, false, true, 15f, 11f)
            verticalLineTo(21f)
        }
    }.build().also { cachedPodium = it }

/** `Podium` vector, built on first access and reused afterwards. */
private var cachedPodium: ImageVector? = null
