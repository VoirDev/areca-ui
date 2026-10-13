package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Users round icon.
 *
 * Converted from Lucide `users-round` (ISC).
 */
public val ArecaIcons.UsersRound: ImageVector
    get() = cachedUsersRound ?: ImageVector.Builder(
        name = "UsersRound",
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
            moveTo(18f, 21f)
            arcTo(8f, 8f, 0f, false, false, 2f, 21f)
            moveTo(5f, 8f)
            arcTo(5f, 5f, 0f, true, true, 15f, 8f)
            arcTo(5f, 5f, 0f, true, true, 5f, 8f)
            close()
            moveTo(22f, 20f)
            curveTo(22f, 16.63f, 20f, 13.5f, 18f, 12f)
            arcTo(5f, 5f, 0f, false, false, 17.55f, 3.7f)
        }
    }.build().also { cachedUsersRound = it }

/** `UsersRound` vector, built on first access and reused afterwards. */
private var cachedUsersRound: ImageVector? = null
