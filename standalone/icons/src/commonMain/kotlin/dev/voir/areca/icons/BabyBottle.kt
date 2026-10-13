package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Baby bottle icon.
 *
 * Converted from Tabler Icons `baby-bottle` (MIT).
 */
public val ArecaIcons.BabyBottle: ImageVector
    get() = cachedBabyBottle ?: ImageVector.Builder(
        name = "BabyBottle",
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
            moveTo(5f, 10f)
            horizontalLineTo(19f)
            moveTo(12f, 2f)
            verticalLineTo(4f)
            moveTo(12f, 4f)
            arcTo(5f, 5f, 0f, false, true, 17f, 9f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 15f, 22f)
            horizontalLineTo(9f)
            arcTo(2f, 2f, 0f, false, true, 7f, 20f)
            verticalLineTo(9f)
            arcTo(5f, 5f, 0f, false, true, 12f, 4f)
        }
    }.build().also { cachedBabyBottle = it }

/** `BabyBottle` vector, built on first access and reused afterwards. */
private var cachedBabyBottle: ImageVector? = null
