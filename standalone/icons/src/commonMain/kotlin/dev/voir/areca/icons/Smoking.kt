package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Smoking icon.
 *
 * Converted from Tabler Icons `smoking` (MIT).
 */
public val ArecaIcons.Smoking: ImageVector
    get() = cachedSmoking ?: ImageVector.Builder(
        name = "Smoking",
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
            moveTo(3f, 14f)
            arcTo(1f, 1f, 0f, false, true, 4f, 13f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 14f)
            verticalLineTo(16f)
            arcTo(1f, 1f, 0f, false, true, 20f, 17f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 16f)
            lineTo(3f, 14f)
            moveTo(8f, 13f)
            lineTo(8f, 17f)
            moveTo(16f, 5f)
            verticalLineTo(5.5f)
            arcTo(2f, 2f, 0f, false, false, 18f, 7.5f)
            arcTo(2f, 2f, 0f, false, true, 20f, 9.5f)
            verticalLineTo(10f)
        }
    }.build().also { cachedSmoking = it }

/** `Smoking` vector, built on first access and reused afterwards. */
private var cachedSmoking: ImageVector? = null
