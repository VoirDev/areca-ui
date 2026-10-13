package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mood smile icon.
 *
 * Converted from Tabler Icons `mood-smile` (MIT).
 */
public val ArecaIcons.FaceSmile: ImageVector
    get() = cachedFaceSmile ?: ImageVector.Builder(
        name = "FaceSmile",
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
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, true, false, 3f, 12f)
            moveTo(9f, 10f)
            lineTo(9.01f, 10f)
            moveTo(15f, 10f)
            lineTo(15.01f, 10f)
            moveTo(9.5f, 15f)
            arcTo(3.5f, 3.5f, 0f, false, false, 14.5f, 15f)
        }
    }.build().also { cachedFaceSmile = it }

/** `FaceSmile` vector, built on first access and reused afterwards. */
private var cachedFaceSmile: ImageVector? = null
