package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Broom icon.
 *
 * Converted from Lucide `broom` (ISC).
 */
public val ArecaIcons.Broom: ImageVector
    get() = cachedBroom ?: ImageVector.Builder(
        name = "Broom",
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
            moveTo(13.5f, 10.5f)
            lineTo(22f, 2f)
            moveTo(14.734f, 13.841f)
            arcTo(2f, 2f, 0f, false, false, 14.42f, 11.421f)
            lineTo(12.58f, 9.58f)
            arcTo(2f, 2f, 0f, false, false, 10.159f, 9.266f)
            lineTo(2.502f, 13.727f)
            arcTo(1f, 1f, 0f, false, false, 2.3f, 15.3f)
            lineTo(8.703f, 21.703f)
            arcTo(1f, 1f, 0f, false, false, 10.274f, 21.499f)
            close()
            moveTo(5f, 18f)
            lineTo(7f, 16f)
            moveTo(7.699f, 10.7f)
            lineTo(13.301f, 16.301f)
        }
    }.build().also { cachedBroom = it }

/** `Broom` vector, built on first access and reused afterwards. */
private var cachedBroom: ImageVector? = null
