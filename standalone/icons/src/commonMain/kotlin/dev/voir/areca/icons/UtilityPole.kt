package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Utility pole icon.
 *
 * Converted from Lucide `utility-pole` (ISC).
 */
public val ArecaIcons.UtilityPole: ImageVector
    get() = cachedUtilityPole ?: ImageVector.Builder(
        name = "UtilityPole",
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
            moveTo(12f, 2f)
            verticalLineTo(22f)
            moveTo(2f, 5f)
            horizontalLineTo(22f)
            moveTo(3f, 3f)
            verticalLineTo(5f)
            moveTo(7f, 3f)
            verticalLineTo(5f)
            moveTo(17f, 3f)
            verticalLineTo(5f)
            moveTo(21f, 3f)
            verticalLineTo(5f)
            moveTo(19f, 5f)
            lineTo(12f, 12f)
            lineTo(5f, 5f)
        }
    }.build().also { cachedUtilityPole = it }

/** `UtilityPole` vector, built on first access and reused afterwards. */
private var cachedUtilityPole: ImageVector? = null
