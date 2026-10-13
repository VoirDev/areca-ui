package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * File text shield icon.
 *
 * Converted from Tabler Icons `file-text-shield` (MIT).
 */
public val ArecaIcons.FileShieldText: ImageVector
    get() = cachedFileShieldText ?: ImageVector.Builder(
        name = "FileShieldText",
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
            verticalLineTo(7f)
            arcTo(0.997f, 0.997f, 0f, false, false, 14f, 8f)
            horizontalLineTo(18f)
            moveTo(11f, 21f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 6f, 3f)
            horizontalLineTo(13f)
            lineTo(18f, 8f)
            verticalLineTo(11.5f)
            moveTo(8f, 9f)
            horizontalLineTo(9f)
            moveTo(8f, 12.994f)
            lineTo(11f, 12.994f)
            moveTo(8f, 16.997f)
            lineTo(10f, 16.997f)
            moveTo(21f, 15.994f)
            curveTo(21f, 19.994f, 18.5f, 21.994f, 17.5f, 21.994f)
            reflectiveCurveTo(14f, 19.994f, 14f, 15.994f)
            curveTo(15f, 15.994f, 16.5f, 15.494f, 17.5f, 14.494f)
            curveTo(18.5f, 15.494f, 20f, 15.994f, 21f, 15.994f)
        }
    }.build().also { cachedFileShieldText = it }

/** `FileShieldText` vector, built on first access and reused afterwards. */
private var cachedFileShieldText: ImageVector? = null
