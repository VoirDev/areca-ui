package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rectangle ellipsis icon.
 *
 * Converted from Lucide `rectangle-ellipsis` (ISC).
 */
public val ArecaIcons.Passcode: ImageVector
    get() = cachedPasscode ?: ImageVector.Builder(
        name = "Passcode",
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
            moveTo(4f, 6f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 8f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 20f, 18f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 16f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 4f, 6f)
            close()
            moveTo(12f, 12f)
            horizontalLineTo(12.01f)
            moveTo(17f, 12f)
            horizontalLineTo(17.01f)
            moveTo(7f, 12f)
            horizontalLineTo(7.01f)
        }
    }.build().also { cachedPasscode = it }

/** `Passcode` vector, built on first access and reused afterwards. */
private var cachedPasscode: ImageVector? = null
