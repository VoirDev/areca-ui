package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * User x icon.
 *
 * Converted from Lucide `user-x` (ISC).
 */
public val ArecaIcons.UserX: ImageVector
    get() = cachedUserX ?: ImageVector.Builder(
        name = "UserX",
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
            moveTo(16f, 21f)
            verticalLineTo(19f)
            arcTo(4f, 4f, 0f, false, false, 12f, 15f)
            horizontalLineTo(6f)
            arcTo(4f, 4f, 0f, false, false, 2f, 19f)
            verticalLineTo(21f)
            moveTo(5f, 7f)
            arcTo(4f, 4f, 0f, true, true, 13f, 7f)
            arcTo(4f, 4f, 0f, true, true, 5f, 7f)
            close()
            moveTo(17f, 8f)
            lineTo(22f, 13f)
            moveTo(22f, 8f)
            lineTo(17f, 13f)
        }
    }.build().also { cachedUserX = it }

/** `UserX` vector, built on first access and reused afterwards. */
private var cachedUserX: ImageVector? = null
