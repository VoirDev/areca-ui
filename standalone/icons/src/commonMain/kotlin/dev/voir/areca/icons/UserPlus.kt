package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * User plus icon.
 *
 * Converted from Lucide `user-plus` (ISC).
 */
public val ArecaIcons.UserPlus: ImageVector
    get() = cachedUserPlus ?: ImageVector.Builder(
        name = "UserPlus",
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
            moveTo(19f, 8f)
            lineTo(19f, 14f)
            moveTo(22f, 11f)
            lineTo(16f, 11f)
        }
    }.build().also { cachedUserPlus = it }

/** `UserPlus` vector, built on first access and reused afterwards. */
private var cachedUserPlus: ImageVector? = null
