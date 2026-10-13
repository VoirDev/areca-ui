package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * House icon.
 *
 * Same drawing as `ArecaIcons.House`; both names are kept.
 *
 * Converted from Lucide `house` (ISC).
 */
public val ArecaIcons.Home: ImageVector
    get() = cachedHome ?: ImageVector.Builder(
        name = "Home",
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
            moveTo(15f, 21f)
            verticalLineTo(13f)
            arcTo(1f, 1f, 0f, false, false, 14f, 12f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, false, 9f, 13f)
            verticalLineTo(21f)
            moveTo(3f, 10f)
            arcTo(2f, 2f, 0f, false, true, 3.709f, 8.472f)
            lineTo(10.709f, 2.472f)
            arcTo(2f, 2f, 0f, false, true, 13.291f, 2.472f)
            lineTo(20.291f, 8.472f)
            arcTo(2f, 2f, 0f, false, true, 21f, 10f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            close()
        }
    }.build().also { cachedHome = it }

/** `Home` vector, built on first access and reused afterwards. */
private var cachedHome: ImageVector? = null
