package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Lock keyhole icon.
 *
 * Converted from Lucide `lock-keyhole` (ISC).
 */
public val ArecaIcons.LockKeyholeSquare: ImageVector
    get() = cachedLockKeyholeSquare ?: ImageVector.Builder(
        name = "LockKeyholeSquare",
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
            moveTo(11f, 16f)
            arcTo(1f, 1f, 0f, true, true, 13f, 16f)
            arcTo(1f, 1f, 0f, true, true, 11f, 16f)
            close()
            moveTo(5f, 10f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 12f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 19f, 22f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 20f)
            verticalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 5f, 10f)
            close()
            moveTo(7f, 10f)
            verticalLineTo(7f)
            arcTo(5f, 5f, 0f, false, true, 17f, 7f)
            verticalLineTo(10f)
        }
    }.build().also { cachedLockKeyholeSquare = it }

/** `LockKeyholeSquare` vector, built on first access and reused afterwards. */
private var cachedLockKeyholeSquare: ImageVector? = null
