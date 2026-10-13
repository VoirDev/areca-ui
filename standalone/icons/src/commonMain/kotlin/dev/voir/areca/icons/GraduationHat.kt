package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Graduation cap icon.
 *
 * Converted from Lucide `graduation-cap` (ISC).
 */
public val ArecaIcons.GraduationHat: ImageVector
    get() = cachedGraduationHat ?: ImageVector.Builder(
        name = "GraduationHat",
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
            moveTo(21.42f, 10.922f)
            arcTo(1f, 1f, 0f, false, false, 21.401f, 9.084f)
            lineTo(12.83f, 5.18f)
            arcTo(2f, 2f, 0f, false, false, 11.17f, 5.18f)
            lineTo(2.6f, 9.08f)
            arcTo(1f, 1f, 0f, false, false, 2.6f, 10.912f)
            lineTo(11.17f, 14.82f)
            arcTo(2f, 2f, 0f, false, false, 12.83f, 14.82f)
            close()
            moveTo(22f, 10f)
            verticalLineTo(16f)
            moveTo(6f, 12.5f)
            verticalLineTo(16f)
            arcTo(6f, 3f, 0f, false, false, 18f, 16f)
            verticalLineTo(12.5f)
        }
    }.build().also { cachedGraduationHat = it }

/** `GraduationHat` vector, built on first access and reused afterwards. */
private var cachedGraduationHat: ImageVector? = null
