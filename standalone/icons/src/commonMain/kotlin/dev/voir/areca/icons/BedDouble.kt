package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bed double icon.
 *
 * Converted from Lucide `bed-double` (ISC).
 */
public val ArecaIcons.BedDouble: ImageVector
    get() = cachedBedDouble ?: ImageVector.Builder(
        name = "BedDouble",
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
            verticalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 4f, 10f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 12f)
            verticalLineTo(20f)
            moveTo(4f, 10f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 6f, 4f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 6f)
            verticalLineTo(10f)
            moveTo(12f, 4f)
            verticalLineTo(10f)
            moveTo(2f, 18f)
            horizontalLineTo(22f)
        }
    }.build().also { cachedBedDouble = it }

/** `BedDouble` vector, built on first access and reused afterwards. */
private var cachedBedDouble: ImageVector? = null
