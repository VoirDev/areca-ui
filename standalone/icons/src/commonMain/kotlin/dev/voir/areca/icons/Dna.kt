package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Dna icon.
 *
 * Converted from Lucide `dna` (ISC).
 */
public val ArecaIcons.Dna: ImageVector
    get() = cachedDna ?: ImageVector.Builder(
        name = "Dna",
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
            moveTo(10f, 16f)
            lineTo(11.5f, 17.5f)
            moveTo(14f, 8f)
            lineTo(12.5f, 6.5f)
            moveTo(15f, 2f)
            curveTo(13.202f, 3.998f, 12.482f, 5.995f, 12.193f, 7.993f)
            moveTo(16.5f, 10.5f)
            lineTo(17.5f, 11.5f)
            moveTo(17f, 6f)
            lineTo(14.109f, 3.109f)
            moveTo(2f, 15f)
            curveTo(8.667f, 9f, 15.333f, 15f, 22f, 9f)
            moveTo(20f, 9f)
            lineTo(20.891f, 9.891f)
            moveTo(3.109f, 14.109f)
            lineTo(4f, 15f)
            moveTo(6.5f, 12.5f)
            lineTo(7.5f, 13.5f)
            moveTo(7f, 18f)
            lineTo(9.891f, 20.891f)
            moveTo(9f, 22f)
            curveTo(10.798f, 20.002f, 11.518f, 18.005f, 11.807f, 16.007f)
        }
    }.build().also { cachedDna = it }

/** `Dna` vector, built on first access and reused afterwards. */
private var cachedDna: ImageVector? = null
