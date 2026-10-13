package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hard drive icon.
 *
 * Converted from Lucide `hard-drive` (ISC).
 */
public val ArecaIcons.HardDrive: ImageVector
    get() = cachedHardDrive ?: ImageVector.Builder(
        name = "HardDrive",
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
            moveTo(10f, 16f)
            horizontalLineTo(10.01f)
            moveTo(2.212f, 11.577f)
            arcTo(2f, 2f, 0f, false, false, 2f, 12.473f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 4f, 20f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 18f)
            verticalLineTo(12.473f)
            arcTo(2f, 2f, 0f, false, false, 21.788f, 11.577f)
            lineTo(18.55f, 5.11f)
            arcTo(2f, 2f, 0f, false, false, 16.76f, 4f)
            horizontalLineTo(7.24f)
            arcTo(2f, 2f, 0f, false, false, 5.45f, 5.11f)
            close()
            moveTo(21.946f, 12.013f)
            horizontalLineTo(2.054f)
            moveTo(6f, 16f)
            horizontalLineTo(6.01f)
        }
    }.build().also { cachedHardDrive = it }

/** `HardDrive` vector, built on first access and reused afterwards. */
private var cachedHardDrive: ImageVector? = null
