package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Temperature minus icon.
 *
 * Converted from Tabler Icons `temperature-minus` (MIT).
 */
public val ArecaIcons.ThermometerLow: ImageVector
    get() = cachedThermometerLow ?: ImageVector.Builder(
        name = "ThermometerLow",
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
            moveTo(8f, 13.5f)
            arcTo(4f, 4f, 0f, true, false, 12f, 13.5f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 8f, 5f)
            verticalLineTo(13.5f)
            moveTo(8f, 9f)
            lineTo(12f, 9f)
            moveTo(16f, 9f)
            lineTo(22f, 9f)
        }
    }.build().also { cachedThermometerLow = it }

/** `ThermometerLow` vector, built on first access and reused afterwards. */
private var cachedThermometerLow: ImageVector? = null
