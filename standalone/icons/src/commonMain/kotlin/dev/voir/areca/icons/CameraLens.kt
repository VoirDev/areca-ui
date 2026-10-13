package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Aperture icon.
 *
 * Same drawing as `ArecaIcons.Aperture`; both names are kept.
 *
 * Converted from Lucide `aperture` (ISC).
 */
public val ArecaIcons.CameraLens: ImageVector
    get() = cachedCameraLens ?: ImageVector.Builder(
        name = "CameraLens",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(14.31f, 8f)
            lineTo(20.05f, 17.94f)
            moveTo(9.69f, 8f)
            horizontalLineTo(21.17f)
            moveTo(7.38f, 12f)
            lineTo(13.12f, 2.06f)
            moveTo(9.69f, 16f)
            lineTo(3.95f, 6.06f)
            moveTo(14.31f, 16f)
            horizontalLineTo(2.83f)
            moveTo(16.62f, 12f)
            lineTo(10.88f, 21.94f)
        }
    }.build().also { cachedCameraLens = it }

/** `CameraLens` vector, built on first access and reused afterwards. */
private var cachedCameraLens: ImageVector? = null
