package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Banknote icon.
 *
 * Converted from Lucide `banknote` (ISC).
 */
public val ArecaIcons.BankNote: ImageVector
    get() = cachedBankNote ?: ImageVector.Builder(
        name = "BankNote",
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
            moveTo(10f, 12f)
            arcTo(2f, 2f, 0f, true, true, 14f, 12f)
            arcTo(2f, 2f, 0f, true, true, 10f, 12f)
            close()
            moveTo(6f, 12f)
            horizontalLineTo(6.01f)
            moveTo(18f, 12f)
            horizontalLineTo(18.01f)
        }
    }.build().also { cachedBankNote = it }

/** `BankNote` vector, built on first access and reused afterwards. */
private var cachedBankNote: ImageVector? = null
