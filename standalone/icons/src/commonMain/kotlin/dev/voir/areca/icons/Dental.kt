package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Dental icon.
 *
 * Converted from Tabler Icons `dental` (MIT).
 */
public val ArecaIcons.Dental: ImageVector
    get() = cachedDental ?: ImageVector.Builder(
        name = "Dental",
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
            moveTo(12f, 5.5f)
            curveTo(10.926f, 4.914f, 9.417f, 4f, 8f, 4f)
            curveTo(5.9f, 4f, 4f, 5.247f, 4f, 9f)
            curveTo(4f, 13.899f, 5.056f, 17.41f, 6.671f, 19.537f)
            curveTo(7.244f, 20.293f, 8.641f, 20.058f, 9.238f, 19.301f)
            curveTo(9.636f, 18.796f, 10.057f, 17.862f, 10.5f, 16.5f)
            curveTo(10.792f, 15.729f, 11.392f, 14.996f, 12f, 15f)
            curveTo(12.602f, 15f, 13.21f, 15.737f, 13.5f, 16.5f)
            curveTo(13.943f, 17.862f, 14.364f, 18.795f, 14.762f, 19.3f)
            curveTo(15.359f, 20.059f, 16.762f, 20.293f, 17.329f, 19.537f)
            curveTo(18.944f, 17.41f, 20f, 13.9f, 20f, 9f)
            curveTo(20f, 5.26f, 18.092f, 4f, 16f, 4f)
            curveTo(14.577f, 4f, 13.08f, 4.911f, 12f, 5.5f)
            moveTo(12f, 5.5f)
            lineTo(15f, 7f)
        }
    }.build().also { cachedDental = it }

/** `Dental` vector, built on first access and reused afterwards. */
private var cachedDental: ImageVector? = null
