package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle user icon.
 *
 * Converted from Lucide `circle-user` (ISC).
 */
public val ArecaIcons.UserCircle: ImageVector
    get() = cachedUserCircle ?: ImageVector.Builder(
        name = "UserCircle",
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
            moveTo(9f, 10f)
            arcTo(3f, 3f, 0f, true, true, 15f, 10f)
            arcTo(3f, 3f, 0f, true, true, 9f, 10f)
            close()
            moveTo(7f, 20.662f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 9f, 17f)
            horizontalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 17f, 19f)
            verticalLineTo(20.662f)
        }
    }.build().also { cachedUserCircle = it }

/** `UserCircle` vector, built on first access and reused afterwards. */
private var cachedUserCircle: ImageVector? = null
