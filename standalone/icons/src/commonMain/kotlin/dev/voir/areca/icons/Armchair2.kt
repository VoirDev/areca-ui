package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Armchair 2 icon.
 *
 * Converted from Tabler Icons `armchair-2` (MIT).
 */
public val ArecaIcons.Armchair2: ImageVector
    get() = cachedArmchair2 ?: ImageVector.Builder(
        name = "Armchair2",
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
            verticalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 8f, 3f)
            horizontalLineTo(16f)
            arcTo(3f, 3f, 0f, false, true, 19f, 6f)
            verticalLineTo(10f)
            moveTo(16f, 15f)
            verticalLineTo(13f)
            arcTo(3f, 3f, 0f, true, true, 19f, 16f)
            verticalLineTo(19f)
            horizontalLineTo(5f)
            verticalLineTo(16f)
            arcTo(3f, 3f, 0f, true, true, 8f, 13f)
            verticalLineTo(15f)
            moveTo(8f, 12f)
            horizontalLineTo(16f)
            moveTo(7f, 19f)
            verticalLineTo(21f)
            moveTo(17f, 19f)
            verticalLineTo(21f)
        }
    }.build().also { cachedArmchair2 = it }

/** `Armchair2` vector, built on first access and reused afterwards. */
private var cachedArmchair2: ImageVector? = null
