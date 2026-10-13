package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Chrome logo.
 *
 * Converted from Tabler Icons `brand-chrome` (MIT).
 */
public val ArecaIcons.GoogleChrome: ImageVector
    get() = cachedGoogleChrome ?: ImageVector.Builder(
        name = "GoogleChrome",
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
            moveTo(9f, 12f)
            arcTo(3f, 3f, 0f, true, false, 15f, 12f)
            arcTo(3f, 3f, 0f, true, false, 9f, 12f)
            moveTo(12f, 9f)
            horizontalLineTo(20.4f)
            moveTo(14.598f, 13.5f)
            lineTo(10.398f, 20.775f)
            moveTo(9.402f, 13.5f)
            lineTo(5.202f, 6.225f)
        }
    }.build().also { cachedGoogleChrome = it }

/** `GoogleChrome` vector, built on first access and reused afterwards. */
private var cachedGoogleChrome: ImageVector? = null
