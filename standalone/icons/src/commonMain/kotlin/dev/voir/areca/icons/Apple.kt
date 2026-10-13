package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Apple icon.
 *
 * Converted from Lucide `apple` (ISC).
 */
public val ArecaIcons.Apple: ImageVector
    get() = cachedApple ?: ImageVector.Builder(
        name = "Apple",
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
            moveTo(12f, 6.528f)
            verticalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 13f, 2f)
            horizontalLineTo(13f)
            moveTo(18.237f, 21f)
            arcTo(15f, 15f, 0f, false, false, 22f, 11f)
            arcTo(6f, 6f, 0f, false, false, 12f, 6.528f)
            arcTo(6f, 6f, 0f, false, false, 2f, 11f)
            arcTo(15.1f, 15.1f, 0f, false, false, 5.763f, 21f)
            arcTo(3f, 3f, 0f, false, false, 9.411f, 21.648f)
            arcTo(5.5f, 5.5f, 0f, false, true, 14.589f, 21.648f)
            arcTo(3f, 3f, 0f, false, false, 18.237f, 21f)
        }
    }.build().also { cachedApple = it }

/** `Apple` vector, built on first access and reused afterwards. */
private var cachedApple: ImageVector? = null
