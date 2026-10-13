package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Id card lanyard icon.
 *
 * Converted from Lucide `id-card-lanyard` (ISC).
 */
public val ArecaIcons.IdCardLanyard: ImageVector
    get() = cachedIdCardLanyard ?: ImageVector.Builder(
        name = "IdCardLanyard",
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
            moveTo(13.5f, 8f)
            horizontalLineTo(10.5f)
            moveTo(15f, 2f)
            lineTo(14f, 4f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 19f, 6f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 17f, 22f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 5f, 20f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 7f, 4f)
            horizontalLineTo(10f)
            moveTo(16f, 22f)
            arcTo(4f, 4f, 0f, false, false, 8f, 22f)
            moveTo(9f, 2f)
            lineTo(12f, 8f)
            moveTo(9f, 15f)
            arcTo(3f, 3f, 0f, true, true, 15f, 15f)
            arcTo(3f, 3f, 0f, true, true, 9f, 15f)
            close()
        }
    }.build().also { cachedIdCardLanyard = it }

/** `IdCardLanyard` vector, built on first access and reused afterwards. */
private var cachedIdCardLanyard: ImageVector? = null
