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
 * Converted from Tabler Icons `lock-open` (MIT).
 */
public val ArecaIcons.Unlock: ImageVector
    get() = cachedUnlock ?: ImageVector.Builder(
        name = "Unlock",
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
            moveTo(5f, 13f)
            arcTo(2f, 2f, 0f, false, true, 7f, 11f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 19f, 13f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 17f, 21f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 5f, 19f)
            lineTo(5f, 13f)
            moveTo(11f, 16f)
            arcTo(1f, 1f, 0f, true, false, 13f, 16f)
            arcTo(1f, 1f, 0f, true, false, 11f, 16f)
            moveTo(8f, 11f)
            verticalLineTo(6f)
            arcTo(4f, 4f, 0f, false, true, 16f, 6f)
        }
    }.build().also { cachedUnlock = it }

/** `Unlock` vector, built on first access and reused afterwards. */
private var cachedUnlock: ImageVector? = null
