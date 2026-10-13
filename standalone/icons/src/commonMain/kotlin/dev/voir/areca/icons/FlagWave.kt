package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Flag icon.
 *
 * Converted from Tabler Icons `flag` (MIT).
 */
public val ArecaIcons.FlagWave: ImageVector
    get() = cachedFlagWave ?: ImageVector.Builder(
        name = "FlagWave",
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
            moveTo(5f, 5f)
            arcTo(5f, 5f, 0f, false, true, 12f, 5f)
            arcTo(5f, 5f, 0f, false, false, 19f, 5f)
            verticalLineTo(14f)
            arcTo(5f, 5f, 0f, false, true, 12f, 14f)
            arcTo(5f, 5f, 0f, false, false, 5f, 14f)
            verticalLineTo(5f)
            moveTo(5f, 21f)
            verticalLineTo(14f)
        }
    }.build().also { cachedFlagWave = it }

/** `FlagWave` vector, built on first access and reused afterwards. */
private var cachedFlagWave: ImageVector? = null
