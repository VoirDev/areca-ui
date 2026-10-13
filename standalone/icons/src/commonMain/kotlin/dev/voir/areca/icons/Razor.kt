package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Razor icon.
 *
 * Converted from Tabler Icons `razor` (MIT).
 */
public val ArecaIcons.Razor: ImageVector
    get() = cachedRazor ?: ImageVector.Builder(
        name = "Razor",
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
            moveTo(7f, 3f)
            horizontalLineTo(17f)
            verticalLineTo(7f)
            horizontalLineTo(7f)
            lineTo(7f, 3f)
            moveTo(12f, 7f)
            verticalLineTo(11f)
            moveTo(12f, 11f)
            arcTo(2f, 2f, 0f, false, true, 14f, 13f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, true, true, 10f, 19f)
            verticalLineTo(13f)
            arcTo(2f, 2f, 0f, false, true, 12f, 11f)
        }
    }.build().also { cachedRazor = it }

/** `Razor` vector, built on first access and reused afterwards. */
private var cachedRazor: ImageVector? = null
