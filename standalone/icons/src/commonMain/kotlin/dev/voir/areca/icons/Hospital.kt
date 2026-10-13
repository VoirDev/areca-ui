package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hospital icon.
 *
 * Converted from Lucide `hospital` (ISC).
 */
public val ArecaIcons.Hospital: ImageVector
    get() = cachedHospital ?: ImageVector.Builder(
        name = "Hospital",
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
            moveTo(12f, 7f)
            verticalLineTo(11f)
            moveTo(14f, 21f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 10f, 18f)
            verticalLineTo(21f)
            moveTo(14f, 9f)
            horizontalLineTo(10f)
            moveTo(18f, 11f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 13f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 20f, 21f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 19f)
            verticalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 4f, 8f)
            horizontalLineTo(6f)
            moveTo(18f, 21f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 16f, 3f)
            horizontalLineTo(8f)
            arcTo(2f, 2f, 0f, false, false, 6f, 5f)
            verticalLineTo(21f)
        }
    }.build().also { cachedHospital = it }

/** `Hospital` vector, built on first access and reused afterwards. */
private var cachedHospital: ImageVector? = null
