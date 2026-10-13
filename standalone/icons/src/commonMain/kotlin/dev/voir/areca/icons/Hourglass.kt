package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hourglass icon.
 *
 * Converted from Lucide `hourglass` (ISC).
 */
public val ArecaIcons.Hourglass: ImageVector
    get() = cachedHourglass ?: ImageVector.Builder(
        name = "Hourglass",
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
            moveTo(5f, 22f)
            horizontalLineTo(19f)
            moveTo(5f, 2f)
            horizontalLineTo(19f)
            moveTo(17f, 22f)
            verticalLineTo(17.828f)
            arcTo(2f, 2f, 0f, false, false, 16.414f, 16.414f)
            lineTo(12f, 12f)
            lineTo(7.586f, 16.414f)
            arcTo(2f, 2f, 0f, false, false, 7f, 17.828f)
            verticalLineTo(22f)
            moveTo(7f, 2f)
            verticalLineTo(6.172f)
            arcTo(2f, 2f, 0f, false, false, 7.586f, 7.586f)
            lineTo(12f, 12f)
            lineTo(16.414f, 7.586f)
            arcTo(2f, 2f, 0f, false, false, 17f, 6.172f)
            verticalLineTo(2f)
        }
    }.build().also { cachedHourglass = it }

/** `Hourglass` vector, built on first access and reused afterwards. */
private var cachedHourglass: ImageVector? = null
