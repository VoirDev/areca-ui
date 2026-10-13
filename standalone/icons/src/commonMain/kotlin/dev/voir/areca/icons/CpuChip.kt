package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cpu icon.
 *
 * Converted from Lucide `cpu` (ISC).
 */
public val ArecaIcons.CpuChip: ImageVector
    get() = cachedCpuChip ?: ImageVector.Builder(
        name = "CpuChip",
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
            moveTo(12f, 20f)
            verticalLineTo(22f)
            moveTo(12f, 2f)
            verticalLineTo(4f)
            moveTo(17f, 20f)
            verticalLineTo(22f)
            moveTo(17f, 2f)
            verticalLineTo(4f)
            moveTo(2f, 12f)
            horizontalLineTo(4f)
            moveTo(2f, 17f)
            horizontalLineTo(4f)
            moveTo(2f, 7f)
            horizontalLineTo(4f)
            moveTo(20f, 12f)
            horizontalLineTo(22f)
            moveTo(20f, 17f)
            horizontalLineTo(22f)
            moveTo(20f, 7f)
            horizontalLineTo(22f)
            moveTo(7f, 20f)
            verticalLineTo(22f)
            moveTo(7f, 2f)
            verticalLineTo(4f)
            moveTo(6f, 4f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 6f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 18f, 20f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 18f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 6f, 4f)
            close()
            moveTo(9f, 8f)
            horizontalLineTo(15f)
            arcTo(1f, 1f, 0f, false, true, 16f, 9f)
            verticalLineTo(15f)
            arcTo(1f, 1f, 0f, false, true, 15f, 16f)
            horizontalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 8f, 15f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 9f, 8f)
            close()
        }
    }.build().also { cachedCpuChip = it }

/** `CpuChip` vector, built on first access and reused afterwards. */
private var cachedCpuChip: ImageVector? = null
