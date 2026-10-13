package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Face grinning icon.
 *
 * Converted from Lucide `face-grinning` (ISC).
 */
public val ArecaIcons.FaceHappy: ImageVector
    get() = cachedFaceHappy ?: ImageVector.Builder(
        name = "FaceHappy",
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
            moveTo(15f, 10f)
            verticalLineTo(9f)
            moveTo(7.084f, 14.302f)
            arcTo(5.12f, 5.12f, 0f, false, false, 16.917f, 14.302f)
            arcTo(0.24f, 0.24f, 0f, false, false, 16.682f, 14f)
            horizontalLineTo(7.32f)
            arcTo(0.24f, 0.24f, 0f, false, false, 7.085f, 14.302f)
            moveTo(9f, 10f)
            verticalLineTo(9f)
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
        }
    }.build().also { cachedFaceHappy = it }

/** `FaceHappy` vector, built on first access and reused afterwards. */
private var cachedFaceHappy: ImageVector? = null
