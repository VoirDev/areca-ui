package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Log out icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `log-out` (ISC).
 */
public val ArecaIcons.LogOut: ImageVector
    get() = cachedLogOut ?: ImageVector.Builder(
        name = "LogOut",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(16f, 17f)
            lineTo(21f, 12f)
            lineTo(16f, 7f)
            moveTo(21f, 12f)
            horizontalLineTo(9f)
            moveTo(9f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            horizontalLineTo(9f)
        }
    }.build().also { cachedLogOut = it }

/** `LogOut` vector, built on first access and reused afterwards. */
private var cachedLogOut: ImageVector? = null
