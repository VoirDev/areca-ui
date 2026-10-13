package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Baby carriage icon.
 *
 * Converted from Tabler Icons `baby-carriage` (MIT).
 */
public val ArecaIcons.BabyCarriage: ImageVector
    get() = cachedBabyCarriage ?: ImageVector.Builder(
        name = "BabyCarriage",
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
            moveTo(6f, 19f)
            arcTo(2f, 2f, 0f, true, false, 10f, 19f)
            arcTo(2f, 2f, 0f, true, false, 6f, 19f)
            moveTo(16f, 19f)
            arcTo(2f, 2f, 0f, true, false, 20f, 19f)
            arcTo(2f, 2f, 0f, true, false, 16f, 19f)
            moveTo(2f, 5f)
            horizontalLineTo(4.5f)
            lineTo(6.132f, 9.897f)
            arcTo(6f, 6f, 0f, false, false, 11.825f, 14f)
            horizontalLineTo(14.5f)
            arcTo(5.5f, 5.5f, 0f, false, false, 14.5f, 3f)
            horizontalLineTo(14f)
            verticalLineTo(9f)
            moveTo(6f, 9f)
            horizontalLineTo(20f)
            moveTo(9f, 17f)
            lineTo(10f, 14f)
            moveTo(16f, 14f)
            lineTo(17f, 17f)
        }
    }.build().also { cachedBabyCarriage = it }

/** `BabyCarriage` vector, built on first access and reused afterwards. */
private var cachedBabyCarriage: ImageVector? = null
