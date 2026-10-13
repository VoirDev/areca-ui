package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Workflow icon.
 *
 * Converted from Lucide `workflow` (ISC).
 */
public val ArecaIcons.Dataflow: ImageVector
    get() = cachedDataflow ?: ImageVector.Builder(
        name = "Dataflow",
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
            moveTo(5f, 3f)
            horizontalLineTo(9f)
            arcTo(2f, 2f, 0f, false, true, 11f, 5f)
            verticalLineTo(9f)
            arcTo(2f, 2f, 0f, false, true, 9f, 11f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 9f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            close()
            moveTo(7f, 11f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, false, false, 9f, 17f)
            horizontalLineTo(13f)
            moveTo(15f, 13f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 15f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 13f, 19f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 15f, 13f)
            close()
        }
    }.build().also { cachedDataflow = it }

/** `Dataflow` vector, built on first access and reused afterwards. */
private var cachedDataflow: ImageVector? = null
