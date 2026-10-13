package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Crutches icon.
 *
 * Converted from Tabler Icons `crutches` (MIT).
 */
public val ArecaIcons.Crutches: ImageVector
    get() = cachedCrutches ?: ImageVector.Builder(
        name = "Crutches",
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
            moveTo(8f, 5f)
            arcTo(2f, 2f, 0f, false, true, 10f, 3f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 16f, 5f)
            arcTo(2f, 2f, 0f, false, true, 14f, 7f)
            horizontalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 8f, 5f)
            moveTo(11f, 21f)
            horizontalLineTo(13f)
            moveTo(12f, 21f)
            verticalLineTo(16.908f)
            arcTo(3f, 3f, 0f, false, true, 12.504f, 15.244f)
            lineTo(13.496f, 13.756f)
            arcTo(3f, 3f, 0f, false, false, 14f, 12.092f)
            verticalLineTo(7f)
            moveTo(12f, 21f)
            verticalLineTo(16.908f)
            arcTo(3f, 3f, 0f, false, false, 11.496f, 15.244f)
            lineTo(10.504f, 13.756f)
            arcTo(3f, 3f, 0f, false, true, 10f, 12.092f)
            verticalLineTo(7f)
            moveTo(10f, 11f)
            horizontalLineTo(14f)
        }
    }.build().also { cachedCrutches = it }

/** `Crutches` vector, built on first access and reused afterwards. */
private var cachedCrutches: ImageVector? = null
