package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Paperclip icon.
 *
 * Converted from Tabler Icons `paperclip` (MIT).
 */
public val ArecaIcons.Attachment02: ImageVector
    get() = cachedAttachment02 ?: ImageVector.Builder(
        name = "Attachment02",
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
            moveTo(15f, 7f)
            lineTo(8.5f, 13.5f)
            arcTo(1.5f, 1.5f, 0f, false, false, 11.5f, 16.5f)
            lineTo(18f, 10f)
            arcTo(3f, 3f, 0f, false, false, 12f, 4f)
            lineTo(5.5f, 10.5f)
            arcTo(4.5f, 4.5f, 0f, false, false, 14.5f, 19.5f)
            lineTo(21f, 13f)
        }
    }.build().also { cachedAttachment02 = it }

/** `Attachment02` vector, built on first access and reused afterwards. */
private var cachedAttachment02: ImageVector? = null
