package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Virus icon.
 *
 * Converted from Tabler Icons `virus` (MIT).
 */
public val ArecaIcons.Virus: ImageVector
    get() = cachedVirus ?: ImageVector.Builder(
        name = "Virus",
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
            moveTo(7f, 12f)
            arcTo(5f, 5f, 0f, true, false, 17f, 12f)
            arcTo(5f, 5f, 0f, true, false, 7f, 12f)
            moveTo(12f, 7f)
            verticalLineTo(3f)
            moveTo(11f, 3f)
            horizontalLineTo(13f)
            moveTo(15.536f, 8.464f)
            lineTo(18.364f, 5.636f)
            moveTo(17.657f, 4.929f)
            lineTo(19.071f, 6.343f)
            moveTo(17f, 12f)
            horizontalLineTo(21f)
            moveTo(21f, 11f)
            verticalLineTo(13f)
            moveTo(15.535f, 15.536f)
            lineTo(18.364f, 18.364f)
            moveTo(19.071f, 17.657f)
            lineTo(17.657f, 19.071f)
            moveTo(12f, 17f)
            verticalLineTo(21f)
            moveTo(13f, 21f)
            horizontalLineTo(11f)
            moveTo(8.465f, 15.536f)
            lineTo(5.636f, 18.364f)
            moveTo(6.343f, 19.071f)
            lineTo(4.93f, 17.657f)
            moveTo(7f, 12f)
            horizontalLineTo(3f)
            moveTo(3f, 13f)
            verticalLineTo(11f)
            moveTo(8.464f, 8.464f)
            lineTo(5.636f, 5.636f)
            moveTo(4.929f, 6.343f)
            lineTo(6.343f, 4.93f)
        }
    }.build().also { cachedVirus = it }

/** `Virus` vector, built on first access and reused afterwards. */
private var cachedVirus: ImageVector? = null
