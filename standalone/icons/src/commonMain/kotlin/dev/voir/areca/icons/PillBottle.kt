package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pill bottle icon.
 *
 * Converted from Lucide `pill-bottle` (ISC).
 */
public val ArecaIcons.PillBottle: ImageVector
    get() = cachedPillBottle ?: ImageVector.Builder(
        name = "PillBottle",
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
            moveTo(18f, 11f)
            horizontalLineTo(14f)
            arcTo(1f, 1f, 0f, false, false, 13f, 12f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, false, 14f, 18f)
            horizontalLineTo(18f)
            moveTo(6f, 7f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 8f, 22f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, false, 18f, 20f)
            verticalLineTo(7f)
            moveTo(5f, 2f)
            horizontalLineTo(19f)
            arcTo(1f, 1f, 0f, false, true, 20f, 3f)
            verticalLineTo(6f)
            arcTo(1f, 1f, 0f, false, true, 19f, 7f)
            horizontalLineTo(5f)
            arcTo(1f, 1f, 0f, false, true, 4f, 6f)
            verticalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 5f, 2f)
            close()
        }
    }.build().also { cachedPillBottle = it }

/** `PillBottle` vector, built on first access and reused afterwards. */
private var cachedPillBottle: ImageVector? = null
