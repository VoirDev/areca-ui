package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hearts icon.
 *
 * Converted from Tabler Icons `hearts` (MIT).
 */
public val ArecaIcons.Hearts: ImageVector
    get() = cachedHearts ?: ImageVector.Builder(
        name = "Hearts",
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
            moveTo(14.017f, 18f)
            lineTo(12f, 20f)
            lineTo(4.5f, 12.572f)
            arcTo(5f, 5f, 0f, true, true, 12f, 6.006f)
            arcTo(5f, 5f, 0f, false, true, 20.153f, 11.79f)
            moveTo(15.99f, 20f)
            lineTo(20.187f, 15.777f)
            arcTo(2.81f, 2.81f, 0f, false, false, 20.187f, 11.829f)
            arcTo(2.747f, 2.747f, 0f, false, false, 16.277f, 11.822f)
            lineTo(15.997f, 12.104f)
            lineTo(15.718f, 11.821f)
            arcTo(2.747f, 2.747f, 0f, false, false, 11.808f, 11.814f)
            arcTo(2.81f, 2.81f, 0f, false, false, 11.801f, 15.762f)
            lineTo(15.983f, 20f)
            lineTo(15.99f, 20f)
        }
    }.build().also { cachedHearts = it }

/** `Hearts` vector, built on first access and reused afterwards. */
private var cachedHearts: ImageVector? = null
