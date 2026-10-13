package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle parking icon.
 *
 * Converted from Lucide `circle-parking` (ISC).
 */
public val ArecaIcons.CircleParking: ImageVector
    get() = cachedCircleParking ?: ImageVector.Builder(
        name = "CircleParking",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(9f, 17f)
            verticalLineTo(7f)
            horizontalLineTo(13f)
            arcTo(3f, 3f, 0f, false, true, 13f, 13f)
            horizontalLineTo(9f)
        }
    }.build().also { cachedCircleParking = it }

/** `CircleParking` vector, built on first access and reused afterwards. */
private var cachedCircleParking: ImageVector? = null
