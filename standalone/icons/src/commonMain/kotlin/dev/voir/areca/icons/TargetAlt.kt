package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Target icon.
 *
 * Converted from Tabler Icons `target` (MIT).
 */
public val ArecaIcons.TargetAlt: ImageVector
    get() = cachedTargetAlt ?: ImageVector.Builder(
        name = "TargetAlt",
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
            moveTo(11f, 12f)
            arcTo(1f, 1f, 0f, true, false, 13f, 12f)
            arcTo(1f, 1f, 0f, true, false, 11f, 12f)
            moveTo(7f, 12f)
            arcTo(5f, 5f, 0f, true, false, 17f, 12f)
            arcTo(5f, 5f, 0f, true, false, 7f, 12f)
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, true, false, 3f, 12f)
        }
    }.build().also { cachedTargetAlt = it }

/** `TargetAlt` vector, built on first access and reused afterwards. */
private var cachedTargetAlt: ImageVector? = null
