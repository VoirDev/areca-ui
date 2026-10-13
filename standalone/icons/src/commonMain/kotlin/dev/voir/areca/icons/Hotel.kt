package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hotel icon.
 *
 * Converted from Lucide `hotel` (ISC).
 */
public val ArecaIcons.Hotel: ImageVector
    get() = cachedHotel ?: ImageVector.Builder(
        name = "Hotel",
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
            moveTo(10f, 22f)
            verticalLineTo(15.43f)
            moveTo(12f, 11f)
            horizontalLineTo(12.01f)
            moveTo(12f, 7f)
            horizontalLineTo(12.01f)
            moveTo(14f, 15.43f)
            verticalLineTo(22f)
            moveTo(15f, 16f)
            arcTo(5f, 5f, 0f, false, false, 9f, 16f)
            moveTo(16f, 11f)
            horizontalLineTo(16.01f)
            moveTo(16f, 7f)
            horizontalLineTo(16.01f)
            moveTo(8f, 11f)
            horizontalLineTo(8.01f)
            moveTo(8f, 7f)
            horizontalLineTo(8.01f)
            moveTo(6f, 2f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 4f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 20f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 6f, 2f)
            close()
        }
    }.build().also { cachedHotel = it }

/** `Hotel` vector, built on first access and reused afterwards. */
private var cachedHotel: ImageVector? = null
