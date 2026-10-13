package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Armchair icon.
 *
 * Converted from Lucide `armchair` (ISC).
 */
public val ArecaIcons.Armchair: ImageVector
    get() = cachedArmchair ?: ImageVector.Builder(
        name = "Armchair",
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
            moveTo(19f, 9f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 17f, 4f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 5f, 6f)
            verticalLineTo(9f)
            moveTo(3f, 16f)
            arcTo(2f, 2f, 0f, false, false, 5f, 18f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 21f, 16f)
            verticalLineTo(11f)
            arcTo(2f, 2f, 0f, false, false, 17f, 11f)
            verticalLineTo(12.5f)
            arcTo(0.5f, 0.5f, 0f, false, true, 16.5f, 13f)
            horizontalLineTo(7.5f)
            arcTo(0.5f, 0.5f, 0f, false, true, 7f, 12.5f)
            verticalLineTo(11f)
            arcTo(2f, 2f, 0f, false, false, 3f, 11f)
            close()
            moveTo(5f, 18f)
            verticalLineTo(20f)
            moveTo(19f, 18f)
            verticalLineTo(20f)
        }
    }.build().also { cachedArmchair = it }

/** `Armchair` vector, built on first access and reused afterwards. */
private var cachedArmchair: ImageVector? = null
