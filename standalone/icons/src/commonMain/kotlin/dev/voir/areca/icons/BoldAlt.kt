package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bold icon.
 *
 * Converted from Tabler Icons `bold` (MIT).
 */
public val ArecaIcons.BoldAlt: ImageVector
    get() = cachedBoldAlt ?: ImageVector.Builder(
        name = "BoldAlt",
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
            moveTo(7f, 5f)
            horizontalLineTo(13f)
            arcTo(3.5f, 3.5f, 0f, false, true, 13f, 12f)
            horizontalLineTo(7f)
            lineTo(7f, 5f)
            moveTo(13f, 12f)
            horizontalLineTo(14f)
            arcTo(3.5f, 3.5f, 0f, false, true, 14f, 19f)
            horizontalLineTo(7f)
            verticalLineTo(12f)
        }
    }.build().also { cachedBoldAlt = it }

/** `BoldAlt` vector, built on first access and reused afterwards. */
private var cachedBoldAlt: ImageVector? = null
