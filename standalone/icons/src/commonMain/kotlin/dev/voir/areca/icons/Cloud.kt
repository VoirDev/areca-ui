package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cloud icon.
 *
 * Converted from Lucide `cloud` (ISC).
 */
public val ArecaIcons.Cloud: ImageVector
    get() = cachedCloud ?: ImageVector.Builder(
        name = "Cloud",
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
            moveTo(17.5f, 19f)
            horizontalLineTo(9f)
            arcTo(7f, 7f, 0f, true, true, 15.71f, 10f)
            horizontalLineTo(17.5f)
            arcTo(4.5f, 4.5f, 0f, true, true, 17.5f, 19f)
            close()
        }
    }.build().also { cachedCloud = it }

/** `Cloud` vector, built on first access and reused afterwards. */
private var cachedCloud: ImageVector? = null
