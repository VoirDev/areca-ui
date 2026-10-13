package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Run icon.
 *
 * Converted from Tabler Icons `run` (MIT).
 */
public val ArecaIcons.Run: ImageVector
    get() = cachedRun ?: ImageVector.Builder(
        name = "Run",
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
            moveTo(11.007f, 5f)
            arcTo(2f, 2f, 0f, true, false, 15.007f, 5f)
            arcTo(2f, 2f, 0f, true, false, 11.007f, 5f)
            moveTo(4f, 17f)
            lineTo(9f, 18f)
            lineTo(9.75f, 16.5f)
            moveTo(15f, 21f)
            verticalLineTo(17f)
            lineTo(11f, 14f)
            lineTo(12f, 8f)
            moveTo(7f, 12f)
            verticalLineTo(9f)
            lineTo(12f, 8f)
            lineTo(15f, 11f)
            lineTo(18f, 12f)
        }
    }.build().also { cachedRun = it }

/** `Run` vector, built on first access and reused afterwards. */
private var cachedRun: ImageVector? = null
