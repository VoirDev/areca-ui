package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * User down icon.
 *
 * Converted from Tabler Icons `user-down` (MIT).
 */
public val ArecaIcons.UserDown: ImageVector
    get() = cachedUserDown ?: ImageVector.Builder(
        name = "UserDown",
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
            moveTo(8f, 7f)
            arcTo(4f, 4f, 0f, true, false, 16f, 7f)
            arcTo(4f, 4f, 0f, false, false, 8f, 7f)
            moveTo(6f, 21f)
            verticalLineTo(19f)
            arcTo(4f, 4f, 0f, false, true, 10f, 15f)
            horizontalLineTo(14f)
            curveTo(14.342f, 15f, 14.674f, 15.043f, 14.99f, 15.124f)
            moveTo(19f, 16f)
            verticalLineTo(22f)
            moveTo(22f, 19f)
            lineTo(19f, 22f)
            lineTo(16f, 19f)
        }
    }.build().also { cachedUserDown = it }

/** `UserDown` vector, built on first access and reused afterwards. */
private var cachedUserDown: ImageVector? = null
