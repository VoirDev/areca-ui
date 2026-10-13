package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Camera plus icon.
 *
 * Converted from Tabler Icons `camera-plus` (MIT).
 */
public val ArecaIcons.CameraPlus: ImageVector
    get() = cachedCameraPlus ?: ImageVector.Builder(
        name = "CameraPlus",
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
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 18f)
            verticalLineTo(9f)
            arcTo(2f, 2f, 0f, false, true, 5f, 7f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 8f, 5f)
            arcTo(1f, 1f, 0f, false, true, 9f, 4f)
            horizontalLineTo(15f)
            arcTo(1f, 1f, 0f, false, true, 16f, 5f)
            arcTo(2f, 2f, 0f, false, false, 18f, 7f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 9f)
            verticalLineTo(12.5f)
            moveTo(16f, 19f)
            horizontalLineTo(22f)
            moveTo(19f, 16f)
            verticalLineTo(22f)
            moveTo(9f, 13f)
            arcTo(3f, 3f, 0f, true, false, 15f, 13f)
            arcTo(3f, 3f, 0f, false, false, 9f, 13f)
        }
    }.build().also { cachedCameraPlus = it }

/** `CameraPlus` vector, built on first access and reused afterwards. */
private var cachedCameraPlus: ImageVector? = null
