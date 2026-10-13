package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Users plus icon.
 *
 * Converted from Tabler Icons `users-plus` (MIT).
 */
public val ArecaIcons.UsersPlus: ImageVector
    get() = cachedUsersPlus ?: ImageVector.Builder(
        name = "UsersPlus",
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
            moveTo(5f, 7f)
            arcTo(4f, 4f, 0f, true, false, 13f, 7f)
            arcTo(4f, 4f, 0f, false, false, 5f, 7f)
            moveTo(3f, 21f)
            verticalLineTo(19f)
            arcTo(4f, 4f, 0f, false, true, 7f, 15f)
            horizontalLineTo(11f)
            curveTo(11.96f, 15f, 12.84f, 15.338f, 13.53f, 15.901f)
            moveTo(16f, 3.13f)
            arcTo(4f, 4f, 0f, false, true, 16f, 10.88f)
            moveTo(16f, 19f)
            horizontalLineTo(22f)
            moveTo(19f, 16f)
            verticalLineTo(22f)
        }
    }.build().also { cachedUsersPlus = it }

/** `UsersPlus` vector, built on first access and reused afterwards. */
private var cachedUsersPlus: ImageVector? = null
