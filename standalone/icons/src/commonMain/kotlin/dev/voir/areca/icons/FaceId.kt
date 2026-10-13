package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Face id icon.
 *
 * Converted from Tabler Icons `face-id` (MIT).
 */
public val ArecaIcons.FaceId: ImageVector
    get() = cachedFaceId ?: ImageVector.Builder(
        name = "FaceId",
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
            moveTo(4f, 8f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 6f, 4f)
            horizontalLineTo(8f)
            moveTo(4f, 16f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 6f, 20f)
            horizontalLineTo(8f)
            moveTo(16f, 4f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 6f)
            verticalLineTo(8f)
            moveTo(16f, 20f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 20f, 18f)
            verticalLineTo(16f)
            moveTo(9f, 10f)
            lineTo(9.01f, 10f)
            moveTo(15f, 10f)
            lineTo(15.01f, 10f)
            moveTo(9.5f, 15f)
            arcTo(3.5f, 3.5f, 0f, false, false, 14.5f, 15f)
        }
    }.build().also { cachedFaceId = it }

/** `FaceId` vector, built on first access and reused afterwards. */
private var cachedFaceId: ImageVector? = null
