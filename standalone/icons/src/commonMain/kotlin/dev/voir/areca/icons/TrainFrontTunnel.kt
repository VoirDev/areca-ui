package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Train front tunnel icon.
 *
 * Converted from Lucide `train-front-tunnel` (ISC).
 */
public val ArecaIcons.TrainFrontTunnel: ImageVector
    get() = cachedTrainFrontTunnel ?: ImageVector.Builder(
        name = "TrainFrontTunnel",
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
            moveTo(2f, 22f)
            verticalLineTo(12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            verticalLineTo(22f)
            moveTo(15f, 6.8f)
            verticalLineTo(8.2f)
            arcTo(3f, 2.8f, 0f, true, true, 9f, 8.2f)
            verticalLineTo(6.8f)
            moveTo(10f, 15f)
            horizontalLineTo(10.01f)
            moveTo(14f, 15f)
            horizontalLineTo(14.01f)
            moveTo(10f, 19f)
            arcTo(4f, 4f, 0f, false, true, 6f, 15f)
            verticalLineTo(12f)
            arcTo(6f, 6f, 0f, true, true, 18f, 12f)
            verticalLineTo(15f)
            arcTo(4f, 4f, 0f, false, true, 14f, 19f)
            close()
            moveTo(9f, 19f)
            lineTo(7f, 22f)
            moveTo(15f, 19f)
            lineTo(17f, 22f)
        }
    }.build().also { cachedTrainFrontTunnel = it }

/** `TrainFrontTunnel` vector, built on first access and reused afterwards. */
private var cachedTrainFrontTunnel: ImageVector? = null
