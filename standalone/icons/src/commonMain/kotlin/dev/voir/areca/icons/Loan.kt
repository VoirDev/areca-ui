package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hand coins icon.
 *
 * Same drawing as `ArecaIcons.HandCoins`; both names are kept.
 *
 * Converted from Lucide `hand-coins` (ISC).
 */
public val ArecaIcons.Loan: ImageVector
    get() = cachedLoan ?: ImageVector.Builder(
        name = "Loan",
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
            moveTo(11f, 15f)
            horizontalLineTo(13f)
            arcTo(2f, 2f, 0f, true, false, 13f, 11f)
            horizontalLineTo(10f)
            curveTo(9.4f, 11f, 8.9f, 11.2f, 8.6f, 11.6f)
            lineTo(3f, 17f)
            moveTo(7f, 21f)
            lineTo(8.6f, 19.6f)
            curveTo(8.9f, 19.2f, 9.4f, 19f, 10f, 19f)
            horizontalLineTo(14f)
            curveTo(15.1f, 19f, 16.1f, 18.6f, 16.8f, 17.8f)
            lineTo(21.4f, 13.4f)
            arcTo(2f, 2f, 0f, false, false, 18.65f, 10.49f)
            lineTo(14.45f, 14.39f)
            moveTo(2f, 16f)
            lineTo(8f, 22f)
            moveTo(13.1f, 9f)
            arcTo(2.9f, 2.9f, 0f, true, true, 18.9f, 9f)
            arcTo(2.9f, 2.9f, 0f, true, true, 13.1f, 9f)
            close()
            moveTo(3f, 5f)
            arcTo(3f, 3f, 0f, true, true, 9f, 5f)
            arcTo(3f, 3f, 0f, true, true, 3f, 5f)
            close()
        }
    }.build().also { cachedLoan = it }

/** `Loan` vector, built on first access and reused afterwards. */
private var cachedLoan: ImageVector? = null
