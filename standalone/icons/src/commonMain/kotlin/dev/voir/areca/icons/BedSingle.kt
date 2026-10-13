package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bed single icon.
 *
 * Converted from Lucide `bed-single` (ISC).
 */
public val ArecaIcons.BedSingle: ImageVector
    get() = cachedBedSingle ?: ImageVector.Builder(
        name = "BedSingle",
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
            moveTo(3f, 20f)
            verticalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 5f, 10f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 12f)
            verticalLineTo(20f)
            moveTo(5f, 10f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 7f, 4f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 19f, 6f)
            verticalLineTo(10f)
            moveTo(3f, 18f)
            horizontalLineTo(21f)
        }
    }.build().also { cachedBedSingle = it }

/** `BedSingle` vector, built on first access and reused afterwards. */
private var cachedBedSingle: ImageVector? = null
