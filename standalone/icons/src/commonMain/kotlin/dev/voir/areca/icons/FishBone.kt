package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Fish bone icon.
 *
 * Converted from Tabler Icons `fish-bone` (MIT).
 */
public val ArecaIcons.FishBone: ImageVector
    get() = cachedFishBone ?: ImageVector.Builder(
        name = "FishBone",
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
            moveTo(16.69f, 7.44f)
            arcTo(6.973f, 6.973f, 0f, false, false, 15f, 12f)
            arcTo(6.97f, 6.97f, 0f, false, false, 16.699f, 16.571f)
            curveTo(18.613f, 15.887f, 20.39f, 14.388f, 22f, 12.006f)
            curveTo(20.387f, 9.622f, 18.606f, 8.123f, 16.688f, 7.441f)
            moveTo(2f, 9.504f)
            arcTo(40.73f, 40.73f, 0f, false, false, 4.422f, 12.008f)
            arcTo(39.679f, 39.679f, 0f, false, false, 2f, 14.506f)
            moveTo(18f, 11f)
            verticalLineTo(11.01f)
            moveTo(4.422f, 12f)
            horizontalLineTo(15f)
            moveTo(7f, 10f)
            verticalLineTo(14f)
            moveTo(11f, 8f)
            verticalLineTo(16f)
        }
    }.build().also { cachedFishBone = it }

/** `FishBone` vector, built on first access and reused afterwards. */
private var cachedFishBone: ImageVector? = null
