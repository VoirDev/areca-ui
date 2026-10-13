package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Grape icon.
 *
 * Converted from Lucide `grape` (ISC).
 */
public val ArecaIcons.Grape: ImageVector
    get() = cachedGrape ?: ImageVector.Builder(
        name = "Grape",
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
            moveTo(22f, 5f)
            verticalLineTo(2f)
            lineTo(16.11f, 7.89f)
            moveTo(13.6f, 15.89f)
            arcTo(3f, 3f, 0f, true, true, 19.6f, 15.89f)
            arcTo(3f, 3f, 0f, true, true, 13.6f, 15.89f)
            close()
            moveTo(5.11f, 7.4f)
            arcTo(3f, 3f, 0f, true, true, 11.11f, 7.4f)
            arcTo(3f, 3f, 0f, true, true, 5.11f, 7.4f)
            close()
            moveTo(9.35f, 11.65f)
            arcTo(3f, 3f, 0f, true, true, 15.35f, 11.65f)
            arcTo(3f, 3f, 0f, true, true, 9.35f, 11.65f)
            close()
            moveTo(10.91f, 5.85f)
            arcTo(3f, 3f, 0f, true, true, 16.91f, 5.85f)
            arcTo(3f, 3f, 0f, true, true, 10.91f, 5.85f)
            close()
            moveTo(15.15f, 10.09f)
            arcTo(3f, 3f, 0f, true, true, 21.15f, 10.09f)
            arcTo(3f, 3f, 0f, true, true, 15.15f, 10.09f)
            close()
            moveTo(3.56f, 13.2f)
            arcTo(3f, 3f, 0f, true, true, 9.56f, 13.2f)
            arcTo(3f, 3f, 0f, true, true, 3.56f, 13.2f)
            close()
            moveTo(7.8f, 17.44f)
            arcTo(3f, 3f, 0f, true, true, 13.8f, 17.44f)
            arcTo(3f, 3f, 0f, true, true, 7.8f, 17.44f)
            close()
            moveTo(2f, 19f)
            arcTo(3f, 3f, 0f, true, true, 8f, 19f)
            arcTo(3f, 3f, 0f, true, true, 2f, 19f)
            close()
        }
    }.build().also { cachedGrape = it }

/** `Grape` vector, built on first access and reused afterwards. */
private var cachedGrape: ImageVector? = null
