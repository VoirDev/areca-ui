package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Medical cross icon.
 *
 * Converted from Tabler Icons `medical-cross` (MIT).
 */
public val ArecaIcons.MedicalCross: ImageVector
    get() = cachedMedicalCross ?: ImageVector.Builder(
        name = "MedicalCross",
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
            moveTo(13f, 3f)
            arcTo(1f, 1f, 0f, false, true, 14f, 4f)
            verticalLineTo(8.535f)
            lineTo(17.928f, 6.268f)
            arcTo(1f, 1f, 0f, false, true, 19.294f, 6.634f)
            lineTo(20.294f, 8.366f)
            arcTo(1f, 1f, 0f, false, true, 19.928f, 9.732f)
            lineTo(16.001f, 12f)
            lineTo(19.928f, 14.269f)
            arcTo(1f, 1f, 0f, false, true, 20.294f, 15.635f)
            lineTo(19.294f, 17.367f)
            arcTo(1f, 1f, 0f, false, true, 17.928f, 17.733f)
            lineTo(14f, 15.464f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 13f, 21f)
            horizontalLineTo(11f)
            arcTo(1f, 1f, 0f, false, true, 10f, 20f)
            verticalLineTo(15.464f)
            lineTo(6.072f, 17.732f)
            arcTo(1f, 1f, 0f, false, true, 4.706f, 17.366f)
            lineTo(3.706f, 15.634f)
            arcTo(1f, 1f, 0f, false, true, 4.072f, 14.268f)
            lineTo(7.999f, 12f)
            lineTo(4.072f, 9.732f)
            arcTo(1f, 1f, 0f, false, true, 3.706f, 8.366f)
            lineTo(4.706f, 6.634f)
            arcTo(1f, 1f, 0f, false, true, 6.072f, 6.268f)
            lineTo(10f, 8.535f)
            verticalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 11f, 3f)
            horizontalLineTo(13f)
        }
    }.build().also { cachedMedicalCross = it }

/** `MedicalCross` vector, built on first access and reused afterwards. */
private var cachedMedicalCross: ImageVector? = null
