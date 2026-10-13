package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pig money icon.
 *
 * Converted from Tabler Icons `pig-money` (MIT).
 */
public val ArecaIcons.PigMoney: ImageVector
    get() = cachedPigMoney ?: ImageVector.Builder(
        name = "PigMoney",
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
            moveTo(15f, 11f)
            verticalLineTo(11.01f)
            moveTo(5.173f, 8.378f)
            arcTo(3f, 3f, 0f, true, true, 9.829f, 7.001f)
            moveTo(16f, 4f)
            verticalLineTo(7.803f)
            arcTo(6.019f, 6.019f, 0f, false, true, 18.658f, 11f)
            horizontalLineTo(19.999f)
            arcTo(1f, 1f, 0f, false, true, 20.999f, 12f)
            verticalLineTo(14f)
            arcTo(1f, 1f, 0f, false, true, 19.999f, 15f)
            horizontalLineTo(18.657f)
            curveTo(18.321f, 15.95f, 17.75f, 16.8f, 16.999f, 17.473f)
            verticalLineTo(19.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 13.999f, 19.5f)
            verticalLineTo(18.917f)
            arcTo(6.04f, 6.04f, 0f, false, true, 12.999f, 19f)
            horizontalLineTo(8.999f)
            arcTo(6.04f, 6.04f, 0f, false, true, 7.999f, 18.917f)
            verticalLineTo(19.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 4.999f, 19.5f)
            verticalLineTo(17.5f)
            lineTo(4.999f, 17.473f)
            arcTo(6f, 6f, 0f, false, true, 8.999f, 7f)
            horizontalLineTo(11.499f)
            lineTo(15.999f, 4f)
        }
    }.build().also { cachedPigMoney = it }

/** `PigMoney` vector, built on first access and reused afterwards. */
private var cachedPigMoney: ImageVector? = null
