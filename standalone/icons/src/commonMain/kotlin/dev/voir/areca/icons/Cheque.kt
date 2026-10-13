package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Writing sign icon.
 *
 * Converted from Tabler Icons `writing-sign` (MIT).
 */
public val ArecaIcons.Cheque: ImageVector
    get() = cachedCheque ?: ImageVector.Builder(
        name = "Cheque",
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
            moveTo(3f, 19f)
            curveTo(6.333f, 17f, 8f, 15f, 8f, 13f)
            curveTo(8f, 10f, 7f, 10f, 6f, 10f)
            reflectiveCurveTo(3.968f, 11.085f, 4f, 13f)
            curveTo(4.034f, 15.048f, 5.658f, 15.877f, 6.5f, 17f)
            curveTo(8f, 19f, 9f, 19.5f, 10f, 18f)
            curveTo(10.667f, 17f, 11.167f, 16.167f, 11.5f, 15.5f)
            curveTo(12.5f, 17.833f, 13.833f, 19f, 15.5f, 19f)
            horizontalLineTo(18f)
            moveTo(20f, 17f)
            verticalLineTo(5f)
            curveTo(20f, 3.879f, 19.121f, 3f, 18f, 3f)
            reflectiveCurveTo(16f, 3.879f, 16f, 5f)
            verticalLineTo(17f)
            lineTo(18f, 19f)
            lineTo(20f, 17f)
            moveTo(16f, 7f)
            horizontalLineTo(20f)
        }
    }.build().also { cachedCheque = it }

/** `Cheque` vector, built on first access and reused afterwards. */
private var cachedCheque: ImageVector? = null
