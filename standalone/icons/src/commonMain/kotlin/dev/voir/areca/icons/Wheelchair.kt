package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wheelchair icon.
 *
 * Converted from Tabler Icons `wheelchair` (MIT).
 */
public val ArecaIcons.Wheelchair: ImageVector
    get() = cachedWheelchair ?: ImageVector.Builder(
        name = "Wheelchair",
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
            moveTo(3f, 16f)
            arcTo(5f, 5f, 0f, true, false, 13f, 16f)
            arcTo(5f, 5f, 0f, true, false, 3f, 16f)
            moveTo(17f, 19f)
            arcTo(2f, 2f, 0f, true, false, 21f, 19f)
            arcTo(2f, 2f, 0f, true, false, 17f, 19f)
            moveTo(19f, 17f)
            arcTo(3f, 3f, 0f, false, false, 16f, 14f)
            horizontalLineTo(12.6f)
            moveTo(3f, 3f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 6f, 5f)
            verticalLineTo(11f)
            moveTo(6f, 8f)
            horizontalLineTo(17f)
            moveTo(15f, 8f)
            verticalLineTo(14f)
        }
    }.build().also { cachedWheelchair = it }

/** `Wheelchair` vector, built on first access and reused afterwards. */
private var cachedWheelchair: ImageVector? = null
