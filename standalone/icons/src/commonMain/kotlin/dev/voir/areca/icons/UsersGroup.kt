package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Users group icon.
 *
 * Converted from Tabler Icons `users-group` (MIT).
 */
public val ArecaIcons.UsersGroup: ImageVector
    get() = cachedUsersGroup ?: ImageVector.Builder(
        name = "UsersGroup",
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
            moveTo(10f, 13f)
            arcTo(2f, 2f, 0f, true, false, 14f, 13f)
            arcTo(2f, 2f, 0f, false, false, 10f, 13f)
            moveTo(8f, 21f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 10f, 18f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 16f, 20f)
            verticalLineTo(21f)
            moveTo(15f, 5f)
            arcTo(2f, 2f, 0f, true, false, 19f, 5f)
            arcTo(2f, 2f, 0f, false, false, 15f, 5f)
            moveTo(17f, 10f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 12f)
            verticalLineTo(13f)
            moveTo(5f, 5f)
            arcTo(2f, 2f, 0f, true, false, 9f, 5f)
            arcTo(2f, 2f, 0f, false, false, 5f, 5f)
            moveTo(3f, 13f)
            verticalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 5f, 10f)
            horizontalLineTo(7f)
        }
    }.build().also { cachedUsersGroup = it }

/** `UsersGroup` vector, built on first access and reused afterwards. */
private var cachedUsersGroup: ImageVector? = null
