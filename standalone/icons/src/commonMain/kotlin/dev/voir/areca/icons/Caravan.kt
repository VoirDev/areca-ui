package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Caravan icon.
 *
 * Converted from Lucide `caravan` (ISC).
 */
public val ArecaIcons.Caravan: ImageVector
    get() = cachedCaravan ?: ImageVector.Builder(
        name = "Caravan",
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
            moveTo(18f, 19f)
            verticalLineTo(9f)
            arcTo(4f, 4f, 0f, false, false, 14f, 5f)
            horizontalLineTo(6f)
            arcTo(4f, 4f, 0f, false, false, 2f, 9f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 4f, 19f)
            horizontalLineTo(6f)
            moveTo(2f, 9f)
            horizontalLineTo(5f)
            arcTo(1f, 1f, 0f, false, true, 6f, 10f)
            verticalLineTo(12f)
            arcTo(1f, 1f, 0f, false, true, 5f, 13f)
            horizontalLineTo(2f)
            moveTo(22f, 17f)
            verticalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 21f, 19f)
            horizontalLineTo(10f)
            verticalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 11f, 9f)
            horizontalLineTo(13f)
            arcTo(1f, 1f, 0f, false, true, 14f, 10f)
            verticalLineTo(19f)
            moveTo(6f, 19f)
            arcTo(2f, 2f, 0f, true, true, 10f, 19f)
            arcTo(2f, 2f, 0f, true, true, 6f, 19f)
            close()
        }
    }.build().also { cachedCaravan = it }

/** `Caravan` vector, built on first access and reused afterwards. */
private var cachedCaravan: ImageVector? = null
