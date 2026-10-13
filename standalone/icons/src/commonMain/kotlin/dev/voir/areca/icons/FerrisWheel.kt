package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ferris wheel icon.
 *
 * Converted from Lucide `ferris-wheel` (ISC).
 */
public val ArecaIcons.FerrisWheel: ImageVector
    get() = cachedFerrisWheel ?: ImageVector.Builder(
        name = "FerrisWheel",
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
            moveTo(10f, 12f)
            arcTo(2f, 2f, 0f, true, true, 14f, 12f)
            arcTo(2f, 2f, 0f, true, true, 10f, 12f)
            close()
            moveTo(12f, 2f)
            verticalLineTo(6f)
            moveTo(6.8f, 15f)
            lineTo(3.3f, 17f)
            moveTo(20.7f, 7f)
            lineTo(17.2f, 9f)
            moveTo(6.8f, 9f)
            lineTo(3.3f, 7f)
            moveTo(20.7f, 17f)
            lineTo(17.2f, 15f)
            moveTo(9f, 22f)
            lineTo(12f, 14f)
            lineTo(15f, 22f)
            moveTo(8f, 22f)
            horizontalLineTo(16f)
            moveTo(18f, 18.7f)
            arcTo(9f, 9f, 0f, true, false, 6f, 18.7f)
        }
    }.build().also { cachedFerrisWheel = it }

/** `FerrisWheel` vector, built on first access and reused afterwards. */
private var cachedFerrisWheel: ImageVector? = null
