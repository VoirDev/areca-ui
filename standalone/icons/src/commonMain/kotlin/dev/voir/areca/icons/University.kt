package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * University icon.
 *
 * Converted from Lucide `university` (ISC).
 */
public val ArecaIcons.University: ImageVector
    get() = cachedUniversity ?: ImageVector.Builder(
        name = "University",
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
            moveTo(18f, 12f)
            horizontalLineTo(18.01f)
            moveTo(18f, 16f)
            horizontalLineTo(18.01f)
            moveTo(22f, 7f)
            arcTo(1f, 1f, 0f, false, false, 21f, 6f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 17.857f, 5.641f)
            lineTo(13.143f, 2.36f)
            arcTo(2f, 2f, 0f, false, false, 10.857f, 2.359f)
            lineTo(6.143f, 5.64f)
            arcTo(2f, 2f, 0f, false, true, 5f, 6f)
            horizontalLineTo(3f)
            arcTo(1f, 1f, 0f, false, false, 2f, 7f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 4f, 21f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 19f)
            close()
            moveTo(6f, 12f)
            horizontalLineTo(6.01f)
            moveTo(6f, 16f)
            horizontalLineTo(6.01f)
            moveTo(10f, 10f)
            arcTo(2f, 2f, 0f, true, true, 14f, 10f)
            arcTo(2f, 2f, 0f, true, true, 10f, 10f)
            close()
        }
    }.build().also { cachedUniversity = it }

/** `University` vector, built on first access and reused afterwards. */
private var cachedUniversity: ImageVector? = null
