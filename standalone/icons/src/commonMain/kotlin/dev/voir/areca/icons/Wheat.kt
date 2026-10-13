package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wheat icon.
 *
 * Converted from Lucide `wheat` (ISC).
 */
public val ArecaIcons.Wheat: ImageVector
    get() = cachedWheat ?: ImageVector.Builder(
        name = "Wheat",
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
            moveTo(2f, 22f)
            lineTo(16f, 8f)
            moveTo(3.47f, 12.53f)
            lineTo(5f, 11f)
            lineTo(6.53f, 12.53f)
            arcTo(3.5f, 3.5f, 0f, false, true, 6.53f, 17.47f)
            lineTo(5f, 19f)
            lineTo(3.47f, 17.47f)
            arcTo(3.5f, 3.5f, 0f, false, true, 3.47f, 12.53f)
            close()
            moveTo(7.47f, 8.53f)
            lineTo(9f, 7f)
            lineTo(10.53f, 8.53f)
            arcTo(3.5f, 3.5f, 0f, false, true, 10.53f, 13.47f)
            lineTo(9f, 15f)
            lineTo(7.47f, 13.47f)
            arcTo(3.5f, 3.5f, 0f, false, true, 7.47f, 8.53f)
            close()
            moveTo(11.47f, 4.53f)
            lineTo(13f, 3f)
            lineTo(14.53f, 4.53f)
            arcTo(3.5f, 3.5f, 0f, false, true, 14.53f, 9.47f)
            lineTo(13f, 11f)
            lineTo(11.47f, 9.47f)
            arcTo(3.5f, 3.5f, 0f, false, true, 11.47f, 4.53f)
            close()
            moveTo(20f, 2f)
            horizontalLineTo(22f)
            verticalLineTo(4f)
            arcTo(4f, 4f, 0f, false, true, 18f, 8f)
            horizontalLineTo(16f)
            verticalLineTo(6f)
            arcTo(4f, 4f, 0f, false, true, 20f, 2f)
            close()
            moveTo(11.47f, 17.47f)
            lineTo(13f, 19f)
            lineTo(11.47f, 20.53f)
            arcTo(3.5f, 3.5f, 0f, false, true, 6.53f, 20.53f)
            lineTo(5f, 19f)
            lineTo(6.53f, 17.47f)
            arcTo(3.5f, 3.5f, 0f, false, true, 11.47f, 17.47f)
            close()
            moveTo(15.47f, 13.47f)
            lineTo(17f, 15f)
            lineTo(15.47f, 16.53f)
            arcTo(3.5f, 3.5f, 0f, false, true, 10.53f, 16.53f)
            lineTo(9f, 15f)
            lineTo(10.53f, 13.47f)
            arcTo(3.5f, 3.5f, 0f, false, true, 15.47f, 13.47f)
            close()
            moveTo(19.47f, 9.47f)
            lineTo(21f, 11f)
            lineTo(19.47f, 12.53f)
            arcTo(3.5f, 3.5f, 0f, false, true, 14.53f, 12.53f)
            lineTo(13f, 11f)
            lineTo(14.53f, 9.47f)
            arcTo(3.5f, 3.5f, 0f, false, true, 19.47f, 9.47f)
            close()
        }
    }.build().also { cachedWheat = it }

/** `Wheat` vector, built on first access and reused afterwards. */
private var cachedWheat: ImageVector? = null
