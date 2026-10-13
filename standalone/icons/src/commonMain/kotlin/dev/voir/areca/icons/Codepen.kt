package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Codepen logo.
 *
 * Converted from Tabler Icons `brand-codepen` (MIT).
 */
public val ArecaIcons.Codepen: ImageVector
    get() = cachedCodepen ?: ImageVector.Builder(
        name = "Codepen",
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
            moveTo(3f, 15f)
            lineTo(12f, 21f)
            lineTo(21f, 15f)
            lineTo(12f, 9f)
            lineTo(3f, 15f)
            moveTo(3f, 9f)
            lineTo(12f, 15f)
            lineTo(21f, 9f)
            lineTo(12f, 3f)
            lineTo(3f, 9f)
            moveTo(3f, 9f)
            lineTo(3f, 15f)
            moveTo(21f, 9f)
            lineTo(21f, 15f)
            moveTo(12f, 3f)
            lineTo(12f, 9f)
            moveTo(12f, 15f)
            lineTo(12f, 21f)
        }
    }.build().also { cachedCodepen = it }

/** `Codepen` vector, built on first access and reused afterwards. */
private var cachedCodepen: ImageVector? = null
