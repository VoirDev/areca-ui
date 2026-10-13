package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Lock open icon.
 *
 * Converted from Lucide `lock-open` (ISC).
 */
public val ArecaIcons.LockUnlocked: ImageVector
    get() = cachedLockUnlocked ?: ImageVector.Builder(
        name = "LockUnlocked",
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
            moveTo(5f, 11f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 13f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 19f, 22f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 20f)
            verticalLineTo(13f)
            arcTo(2f, 2f, 0f, false, true, 5f, 11f)
            close()
            moveTo(7f, 11f)
            verticalLineTo(7f)
            arcTo(5f, 5f, 0f, false, true, 16.9f, 6f)
        }
    }.build().also { cachedLockUnlocked = it }

/** `LockUnlocked` vector, built on first access and reused afterwards. */
private var cachedLockUnlocked: ImageVector? = null
