package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Train icon.
 *
 * Converted from Tabler Icons `train` (MIT).
 */
public val ArecaIcons.Train: ImageVector
    get() = cachedTrain ?: ImageVector.Builder(
        name = "Train",
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
            moveTo(21f, 13f)
            curveTo(21f, 9.13f, 17.63f, 6f, 11f, 6f)
            horizontalLineTo(3f)
            moveTo(3f, 15f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 21f, 13f)
            moveTo(3f, 6f)
            verticalLineTo(11f)
            horizontalLineTo(20.5f)
            moveTo(3f, 11f)
            verticalLineTo(15f)
            moveTo(8f, 11f)
            verticalLineTo(6f)
            moveTo(13f, 11f)
            verticalLineTo(6.5f)
            moveTo(3f, 19f)
            horizontalLineTo(21f)
        }
    }.build().also { cachedTrain = it }

/** `Train` vector, built on first access and reused afterwards. */
private var cachedTrain: ImageVector? = null
