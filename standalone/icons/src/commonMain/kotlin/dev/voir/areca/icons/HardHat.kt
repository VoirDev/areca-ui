package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hard hat icon.
 *
 * Converted from Lucide `hard-hat` (ISC).
 */
public val ArecaIcons.HardHat: ImageVector
    get() = cachedHardHat ?: ImageVector.Builder(
        name = "HardHat",
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
            moveTo(10f, 10f)
            verticalLineTo(5f)
            arcTo(1f, 1f, 0f, false, true, 11f, 4f)
            horizontalLineTo(13f)
            arcTo(1f, 1f, 0f, false, true, 14f, 5f)
            verticalLineTo(10f)
            moveTo(14f, 6f)
            arcTo(6f, 6f, 0f, false, true, 20f, 12f)
            verticalLineTo(15f)
            moveTo(4f, 15f)
            verticalLineTo(12f)
            arcTo(6f, 6f, 0f, false, true, 10f, 6f)
            moveTo(3f, 15f)
            horizontalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 22f, 16f)
            verticalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 21f, 19f)
            horizontalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 2f, 18f)
            verticalLineTo(16f)
            arcTo(1f, 1f, 0f, false, true, 3f, 15f)
            close()
        }
    }.build().also { cachedHardHat = it }

/** `HardHat` vector, built on first access and reused afterwards. */
private var cachedHardHat: ImageVector? = null
