package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Drum icon.
 *
 * Converted from Lucide `drum` (ISC).
 */
public val ArecaIcons.Drum: ImageVector
    get() = cachedDrum ?: ImageVector.Builder(
        name = "Drum",
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
            moveTo(2f, 2f)
            lineTo(10f, 10f)
            moveTo(22f, 2f)
            lineTo(14f, 10f)
            moveTo(2f, 9f)
            arcTo(10f, 5f, 0f, true, true, 22f, 9f)
            arcTo(10f, 5f, 0f, true, true, 2f, 9f)
            close()
            moveTo(7f, 13.4f)
            verticalLineTo(21.3f)
            moveTo(12f, 14f)
            verticalLineTo(22f)
            moveTo(17f, 13.4f)
            verticalLineTo(21.3f)
            moveTo(2f, 9f)
            verticalLineTo(17f)
            arcTo(10f, 5f, 0f, false, false, 22f, 17f)
            verticalLineTo(9f)
        }
    }.build().also { cachedDrum = it }

/** `Drum` vector, built on first access and reused afterwards. */
private var cachedDrum: ImageVector? = null
