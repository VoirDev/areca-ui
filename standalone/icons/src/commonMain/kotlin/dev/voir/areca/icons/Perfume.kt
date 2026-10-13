package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Perfume icon.
 *
 * Converted from Tabler Icons `perfume` (MIT).
 */
public val ArecaIcons.Perfume: ImageVector
    get() = cachedPerfume ?: ImageVector.Builder(
        name = "Perfume",
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
            moveTo(10f, 6f)
            verticalLineTo(9f)
            moveTo(14f, 6f)
            verticalLineTo(9f)
            moveTo(5f, 11f)
            arcTo(2f, 2f, 0f, false, true, 7f, 9f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 19f, 11f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 17f, 21f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 5f, 19f)
            lineTo(5f, 11f)
            moveTo(10f, 15f)
            arcTo(2f, 2f, 0f, true, false, 14f, 15f)
            arcTo(2f, 2f, 0f, true, false, 10f, 15f)
            moveTo(9f, 3f)
            horizontalLineTo(15f)
            verticalLineTo(6f)
            horizontalLineTo(9f)
            lineTo(9f, 3f)
        }
    }.build().also { cachedPerfume = it }

/** `Perfume` vector, built on first access and reused afterwards. */
private var cachedPerfume: ImageVector? = null
