package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * School icon.
 *
 * Converted from Lucide `school` (ISC).
 */
public val ArecaIcons.School: ImageVector
    get() = cachedSchool ?: ImageVector.Builder(
        name = "School",
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
            moveTo(14f, 21f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 10f, 18f)
            verticalLineTo(21f)
            moveTo(18f, 4.933f)
            verticalLineTo(21f)
            moveTo(4f, 6f)
            lineTo(11.106f, 2.21f)
            arcTo(2f, 2f, 0f, false, true, 12.894f, 2.21f)
            lineTo(20f, 6f)
            moveTo(6f, 11f)
            lineTo(2.48f, 13.147f)
            arcTo(1f, 1f, 0f, false, false, 2f, 14.001f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 4f, 21f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 19f)
            verticalLineTo(14f)
            arcTo(1f, 1f, 0f, false, false, 21.52f, 13.147f)
            lineTo(18f, 11f)
            moveTo(6f, 4.933f)
            verticalLineTo(21f)
            moveTo(10f, 9f)
            arcTo(2f, 2f, 0f, true, true, 14f, 9f)
            arcTo(2f, 2f, 0f, true, true, 10f, 9f)
            close()
        }
    }.build().also { cachedSchool = it }

/** `School` vector, built on first access and reused afterwards. */
private var cachedSchool: ImageVector? = null
