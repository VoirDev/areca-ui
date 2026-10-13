package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hanger icon.
 *
 * Converted from Tabler Icons `hanger` (MIT).
 */
public val ArecaIcons.Hanger: ImageVector
    get() = cachedHanger ?: ImageVector.Builder(
        name = "Hanger",
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
            moveTo(14f, 6f)
            arcTo(2f, 2f, 0f, true, false, 10f, 6f)
            curveTo(10f, 7.667f, 10.67f, 9f, 12f, 10f)
            horizontalLineTo(11.992f)
            lineTo(19.963f, 14.428f)
            arcTo(2f, 2f, 0f, false, true, 20.992f, 16.177f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 18.992f, 19f)
            horizontalLineTo(4.992f)
            arcTo(2f, 2f, 0f, false, true, 2.992f, 17f)
            verticalLineTo(16.177f)
            arcTo(2f, 2f, 0f, false, true, 4.021f, 14.428f)
            lineTo(11.992f, 10f)
        }
    }.build().also { cachedHanger = it }

/** `Hanger` vector, built on first access and reused afterwards. */
private var cachedHanger: ImageVector? = null
