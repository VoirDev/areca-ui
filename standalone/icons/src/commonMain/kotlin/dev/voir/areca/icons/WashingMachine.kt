package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Washing machine icon.
 *
 * Converted from Lucide `washing-machine` (ISC).
 */
public val ArecaIcons.WashingMachine: ImageVector
    get() = cachedWashingMachine ?: ImageVector.Builder(
        name = "WashingMachine",
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
            moveTo(3f, 6f)
            horizontalLineTo(6f)
            moveTo(17f, 6f)
            horizontalLineTo(17.01f)
            moveTo(5f, 2f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 4f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 19f, 22f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 20f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 5f, 2f)
            close()
            moveTo(7f, 13f)
            arcTo(5f, 5f, 0f, true, true, 17f, 13f)
            arcTo(5f, 5f, 0f, true, true, 7f, 13f)
            close()
            moveTo(12f, 18f)
            arcTo(2.5f, 2.5f, 0f, false, false, 12f, 13f)
            arcTo(2.5f, 2.5f, 0f, false, true, 12f, 8f)
        }
    }.build().also { cachedWashingMachine = it }

/** `WashingMachine` vector, built on first access and reused afterwards. */
private var cachedWashingMachine: ImageVector? = null
