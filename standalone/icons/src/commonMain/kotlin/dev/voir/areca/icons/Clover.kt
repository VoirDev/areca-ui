package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Clover icon.
 *
 * Converted from Tabler Icons `clover` (MIT).
 */
public val ArecaIcons.Clover: ImageVector
    get() = cachedClover ?: ImageVector.Builder(
        name = "Clover",
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
            moveTo(12f, 10f)
            lineTo(8.603f, 6.56f)
            arcTo(2.104f, 2.104f, 0f, false, true, 8.603f, 3.61f)
            arcTo(2.04f, 2.04f, 0f, false, true, 11.515f, 3.61f)
            lineTo(12f, 4f)
            lineTo(12.485f, 3.61f)
            arcTo(2.04f, 2.04f, 0f, false, true, 15.397f, 3.61f)
            arcTo(2.104f, 2.104f, 0f, false, true, 15.397f, 6.56f)
            lineTo(12f, 10f)
            moveTo(12f, 14f)
            lineTo(8.603f, 17.44f)
            arcTo(2.104f, 2.104f, 0f, false, false, 8.603f, 20.39f)
            arcTo(2.04f, 2.04f, 0f, false, false, 11.515f, 20.39f)
            lineTo(12f, 20f)
            lineTo(12.485f, 20.39f)
            arcTo(2.04f, 2.04f, 0f, false, false, 15.397f, 20.39f)
            arcTo(2.104f, 2.104f, 0f, false, false, 15.397f, 17.44f)
            lineTo(12f, 14f)
            moveTo(14f, 12f)
            lineTo(17.44f, 8.603f)
            arcTo(2.104f, 2.104f, 0f, false, true, 20.39f, 8.603f)
            arcTo(2.04f, 2.04f, 0f, false, true, 20.39f, 11.515f)
            lineTo(20f, 12f)
            lineTo(20.39f, 12.485f)
            arcTo(2.04f, 2.04f, 0f, false, true, 20.39f, 15.397f)
            arcTo(2.104f, 2.104f, 0f, false, true, 17.44f, 15.397f)
            lineTo(14f, 12f)
            moveTo(10f, 12f)
            lineTo(6.56f, 8.603f)
            arcTo(2.104f, 2.104f, 0f, false, false, 3.61f, 8.603f)
            arcTo(2.04f, 2.04f, 0f, false, false, 3.61f, 11.515f)
            lineTo(4f, 12f)
            lineTo(3.61f, 12.485f)
            arcTo(2.04f, 2.04f, 0f, false, false, 3.61f, 15.397f)
            arcTo(2.104f, 2.104f, 0f, false, false, 6.56f, 15.397f)
            lineTo(10f, 12f)
        }
    }.build().also { cachedClover = it }

/** `Clover` vector, built on first access and reused afterwards. */
private var cachedClover: ImageVector? = null
