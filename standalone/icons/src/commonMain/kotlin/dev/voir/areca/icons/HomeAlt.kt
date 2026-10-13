package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Home 2 icon.
 *
 * Converted from Tabler Icons `home-2` (MIT).
 */
public val ArecaIcons.HomeAlt: ImageVector
    get() = cachedHomeAlt ?: ImageVector.Builder(
        name = "HomeAlt",
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
            moveTo(5f, 12f)
            lineTo(3f, 12f)
            lineTo(12f, 3f)
            lineTo(21f, 12f)
            lineTo(19f, 12f)
            moveTo(5f, 12f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 7f, 21f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 19f, 19f)
            verticalLineTo(12f)
            moveTo(10f, 12f)
            horizontalLineTo(14f)
            verticalLineTo(16f)
            horizontalLineTo(10f)
            lineTo(10f, 12f)
        }
    }.build().also { cachedHomeAlt = it }

/** `HomeAlt` vector, built on first access and reused afterwards. */
private var cachedHomeAlt: ImageVector? = null
