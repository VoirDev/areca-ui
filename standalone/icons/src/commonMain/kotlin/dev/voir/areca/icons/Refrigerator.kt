package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Refrigerator icon.
 *
 * Converted from Lucide `refrigerator` (ISC).
 */
public val ArecaIcons.Refrigerator: ImageVector
    get() = cachedRefrigerator ?: ImageVector.Builder(
        name = "Refrigerator",
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
            moveTo(5f, 6f)
            arcTo(4f, 4f, 0f, false, true, 9f, 2f)
            horizontalLineTo(15f)
            arcTo(4f, 4f, 0f, false, true, 19f, 6f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 17f, 22f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 5f, 20f)
            verticalLineTo(6f)
            close()
            moveTo(5f, 10f)
            horizontalLineTo(19f)
            moveTo(15f, 7f)
            verticalLineTo(13f)
        }
    }.build().also { cachedRefrigerator = it }

/** `Refrigerator` vector, built on first access and reused afterwards. */
private var cachedRefrigerator: ImageVector? = null
