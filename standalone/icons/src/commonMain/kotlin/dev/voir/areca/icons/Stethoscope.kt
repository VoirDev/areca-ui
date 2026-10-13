package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Stethoscope icon.
 *
 * Converted from Lucide `stethoscope` (ISC).
 */
public val ArecaIcons.Stethoscope: ImageVector
    get() = cachedStethoscope ?: ImageVector.Builder(
        name = "Stethoscope",
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
            moveTo(11f, 2f)
            verticalLineTo(4f)
            moveTo(5f, 2f)
            verticalLineTo(4f)
            moveTo(5f, 3f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 2f, 5f)
            verticalLineTo(9f)
            arcTo(6f, 6f, 0f, false, false, 14f, 9f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 12f, 3f)
            horizontalLineTo(11f)
            moveTo(8f, 15f)
            arcTo(6f, 6f, 0f, false, false, 20f, 15f)
            verticalLineTo(12f)
            moveTo(18f, 10f)
            arcTo(2f, 2f, 0f, true, true, 22f, 10f)
            arcTo(2f, 2f, 0f, true, true, 18f, 10f)
            close()
        }
    }.build().also { cachedStethoscope = it }

/** `Stethoscope` vector, built on first access and reused afterwards. */
private var cachedStethoscope: ImageVector? = null
