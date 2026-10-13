package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Helicopter icon.
 *
 * Converted from Lucide `helicopter` (ISC).
 */
public val ArecaIcons.Helicopter: ImageVector
    get() = cachedHelicopter ?: ImageVector.Builder(
        name = "Helicopter",
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
            moveTo(11f, 17f)
            verticalLineTo(21f)
            moveTo(14f, 3f)
            verticalLineTo(11f)
            arcTo(2f, 2f, 0f, false, false, 16f, 13f)
            horizontalLineTo(21.865f)
            moveTo(17f, 17f)
            verticalLineTo(21f)
            moveTo(18f, 17f)
            arcTo(4f, 4f, 0f, false, false, 22f, 13f)
            arcTo(8f, 6f, 0f, false, false, 14f, 7f)
            arcTo(6f, 5f, 0f, false, false, 8f, 12f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, false, false, 10f, 17f)
            close()
            moveTo(2f, 10f)
            verticalLineTo(15f)
            moveTo(6f, 3f)
            horizontalLineTo(22f)
            moveTo(7f, 21f)
            horizontalLineTo(21f)
            moveTo(8f, 13f)
            horizontalLineTo(2f)
        }
    }.build().also { cachedHelicopter = it }

/** `Helicopter` vector, built on first access and reused afterwards. */
private var cachedHelicopter: ImageVector? = null
