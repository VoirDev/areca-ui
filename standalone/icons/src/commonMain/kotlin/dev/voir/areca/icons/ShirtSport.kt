package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shirt sport icon.
 *
 * Converted from Tabler Icons `shirt-sport` (MIT).
 */
public val ArecaIcons.ShirtSport: ImageVector
    get() = cachedShirtSport ?: ImageVector.Builder(
        name = "ShirtSport",
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
            moveTo(15f, 4f)
            lineTo(21f, 6f)
            verticalLineTo(11f)
            horizontalLineTo(18f)
            verticalLineTo(19f)
            arcTo(1f, 1f, 0f, false, true, 17f, 20f)
            horizontalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 6f, 19f)
            verticalLineTo(11f)
            horizontalLineTo(3f)
            verticalLineTo(6f)
            lineTo(9f, 4f)
            arcTo(3f, 3f, 0f, false, false, 15f, 4f)
            moveTo(10.5f, 11f)
            horizontalLineTo(13f)
            lineTo(11.5f, 16f)
        }
    }.build().also { cachedShirtSport = it }

/** `ShirtSport` vector, built on first access and reused afterwards. */
private var cachedShirtSport: ImageVector? = null
