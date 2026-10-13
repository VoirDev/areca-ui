package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * File invoice icon.
 *
 * Converted from Tabler Icons `file-invoice` (MIT).
 */
public val ArecaIcons.Invoice: ImageVector
    get() = cachedInvoice ?: ImageVector.Builder(
        name = "Invoice",
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
            moveTo(14f, 3f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, false, 15f, 8f)
            horizontalLineTo(19f)
            moveTo(17f, 21f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 5f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 7f, 3f)
            horizontalLineTo(14f)
            lineTo(19f, 8f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 17f, 21f)
            moveTo(9f, 7f)
            lineTo(10f, 7f)
            moveTo(9f, 13f)
            lineTo(15f, 13f)
            moveTo(13f, 17f)
            lineTo(15f, 17f)
        }
    }.build().also { cachedInvoice = it }

/** `Invoice` vector, built on first access and reused afterwards. */
private var cachedInvoice: ImageVector? = null
