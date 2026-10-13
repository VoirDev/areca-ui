package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Package minus icon.
 *
 * Converted from Lucide `package-minus` (ISC).
 */
public val ArecaIcons.PackageMinus: ImageVector
    get() = cachedPackageMinus ?: ImageVector.Builder(
        name = "PackageMinus",
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
            moveTo(12f, 22f)
            verticalLineTo(12f)
            moveTo(16f, 17f)
            horizontalLineTo(22f)
            moveTo(21f, 13f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, false, 20f, 6.27f)
            lineTo(13f, 2.27f)
            arcTo(2f, 2f, 0f, false, false, 11f, 2.27f)
            lineTo(4f, 6.27f)
            arcTo(2f, 2f, 0f, false, false, 3f, 8f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, false, 4f, 17.729f)
            lineTo(11f, 21.729f)
            arcTo(2f, 2f, 0f, false, false, 13f, 21.73f)
            lineTo(14.675f, 20.775f)
            moveTo(3.29f, 7f)
            lineTo(12f, 12f)
            lineTo(20.71f, 7f)
            moveTo(7.5f, 4.27f)
            lineTo(16.497f, 9.418f)
        }
    }.build().also { cachedPackageMinus = it }

/** `PackageMinus` vector, built on first access and reused afterwards. */
private var cachedPackageMinus: ImageVector? = null
