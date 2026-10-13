package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pool icon.
 *
 * Converted from Tabler Icons `pool` (MIT).
 */
public val ArecaIcons.Pool: ImageVector
    get() = cachedPool ?: ImageVector.Builder(
        name = "Pool",
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
            moveTo(2f, 20f)
            arcTo(2.4f, 2.4f, 0f, false, false, 4f, 21f)
            arcTo(2.4f, 2.4f, 0f, false, false, 6f, 20f)
            arcTo(2.4f, 2.4f, 0f, false, true, 8f, 19f)
            arcTo(2.4f, 2.4f, 0f, false, true, 10f, 20f)
            arcTo(2.4f, 2.4f, 0f, false, false, 12f, 21f)
            arcTo(2.4f, 2.4f, 0f, false, false, 14f, 20f)
            arcTo(2.4f, 2.4f, 0f, false, true, 16f, 19f)
            arcTo(2.4f, 2.4f, 0f, false, true, 18f, 20f)
            arcTo(2.4f, 2.4f, 0f, false, false, 20f, 21f)
            arcTo(2.4f, 2.4f, 0f, false, false, 22f, 20f)
            moveTo(2f, 16f)
            arcTo(2.4f, 2.4f, 0f, false, false, 4f, 17f)
            arcTo(2.4f, 2.4f, 0f, false, false, 6f, 16f)
            arcTo(2.4f, 2.4f, 0f, false, true, 8f, 15f)
            arcTo(2.4f, 2.4f, 0f, false, true, 10f, 16f)
            arcTo(2.4f, 2.4f, 0f, false, false, 12f, 17f)
            arcTo(2.4f, 2.4f, 0f, false, false, 14f, 16f)
            arcTo(2.4f, 2.4f, 0f, false, true, 16f, 15f)
            arcTo(2.4f, 2.4f, 0f, false, true, 18f, 16f)
            arcTo(2.4f, 2.4f, 0f, false, false, 20f, 17f)
            arcTo(2.4f, 2.4f, 0f, false, false, 22f, 16f)
            moveTo(15f, 12f)
            verticalLineTo(4.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 18f, 4.5f)
            moveTo(9f, 12f)
            verticalLineTo(4.5f)
            arcTo(1.5f, 1.5f, 0f, false, false, 6f, 4.5f)
            moveTo(15f, 5f)
            lineTo(9f, 5f)
            moveTo(9f, 10f)
            lineTo(15f, 10f)
        }
    }.build().also { cachedPool = it }

/** `Pool` vector, built on first access and reused afterwards. */
private var cachedPool: ImageVector? = null
