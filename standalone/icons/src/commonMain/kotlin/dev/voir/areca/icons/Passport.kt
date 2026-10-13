package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * E passport icon.
 *
 * Converted from Tabler Icons `e-passport` (MIT).
 */
public val ArecaIcons.Passport: ImageVector
    get() = cachedPassport ?: ImageVector.Builder(
        name = "Passport",
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
            moveTo(2f, 7f)
            arcTo(2f, 2f, 0f, false, true, 4f, 5f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 7f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 20f, 19f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 17f)
            lineTo(2f, 7f)
            moveTo(9f, 12f)
            arcTo(3f, 3f, 0f, true, false, 15f, 12f)
            arcTo(3f, 3f, 0f, true, false, 9f, 12f)
            moveTo(9f, 12f)
            horizontalLineTo(2f)
            moveTo(15f, 12f)
            horizontalLineTo(22f)
        }
    }.build().also { cachedPassport = it }

/** `Passport` vector, built on first access and reused afterwards. */
private var cachedPassport: ImageVector? = null
