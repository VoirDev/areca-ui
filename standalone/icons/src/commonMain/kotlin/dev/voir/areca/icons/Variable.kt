package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Variable icon.
 *
 * Converted from Lucide `variable` (ISC).
 */
public val ArecaIcons.Variable: ImageVector
    get() = cachedVariable ?: ImageVector.Builder(
        name = "Variable",
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
            moveTo(8f, 21f)
            reflectiveCurveTo(4f, 18f, 4f, 12f)
            reflectiveCurveTo(8f, 3f, 8f, 3f)
            moveTo(16f, 3f)
            reflectiveCurveTo(20f, 6f, 20f, 12f)
            reflectiveCurveTo(16f, 21f, 16f, 21f)
            moveTo(15f, 9f)
            lineTo(9f, 15f)
            moveTo(9f, 9f)
            lineTo(15f, 15f)
        }
    }.build().also { cachedVariable = it }

/** `Variable` vector, built on first access and reused afterwards. */
private var cachedVariable: ImageVector? = null
