package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Id card icon.
 *
 * Converted from Lucide `id-card` (ISC).
 */
public val ArecaIcons.IdCard: ImageVector
    get() = cachedIdCard ?: ImageVector.Builder(
        name = "IdCard",
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
            moveTo(13f, 19f)
            arcTo(4f, 4f, 0f, false, false, 5f, 19f)
            moveTo(16f, 10f)
            horizontalLineTo(18f)
            moveTo(16f, 14f)
            horizontalLineTo(18f)
            moveTo(6f, 12f)
            arcTo(3f, 3f, 0f, true, true, 12f, 12f)
            arcTo(3f, 3f, 0f, true, true, 6f, 12f)
            close()
            moveTo(4f, 5f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 7f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 20f, 19f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 17f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 4f, 5f)
            close()
        }
    }.build().also { cachedIdCard = it }

/** `IdCard` vector, built on first access and reused afterwards. */
private var cachedIdCard: ImageVector? = null
