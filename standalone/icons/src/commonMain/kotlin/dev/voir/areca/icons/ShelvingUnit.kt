package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shelving unit icon.
 *
 * Converted from Lucide `shelving-unit` (ISC).
 */
public val ArecaIcons.ShelvingUnit: ImageVector
    get() = cachedShelvingUnit ?: ImageVector.Builder(
        name = "ShelvingUnit",
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
            moveTo(12f, 12f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, false, 11f, 8f)
            horizontalLineTo(9f)
            arcTo(1f, 1f, 0f, false, false, 8f, 9f)
            verticalLineTo(12f)
            moveTo(16f, 20f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, false, 15f, 16f)
            horizontalLineTo(13f)
            arcTo(1f, 1f, 0f, false, false, 12f, 17f)
            verticalLineTo(20f)
            moveTo(20f, 22f)
            verticalLineTo(2f)
            moveTo(4f, 12f)
            horizontalLineTo(20f)
            moveTo(4f, 20f)
            horizontalLineTo(20f)
            moveTo(4f, 2f)
            verticalLineTo(22f)
            moveTo(4f, 4f)
            horizontalLineTo(20f)
        }
    }.build().also { cachedShelvingUnit = it }

/** `ShelvingUnit` vector, built on first access and reused afterwards. */
private var cachedShelvingUnit: ImageVector? = null
