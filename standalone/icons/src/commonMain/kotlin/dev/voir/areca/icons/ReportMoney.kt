package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Report money icon.
 *
 * Converted from Tabler Icons `report-money` (MIT).
 */
public val ArecaIcons.ReportMoney: ImageVector
    get() = cachedReportMoney ?: ImageVector.Builder(
        name = "ReportMoney",
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
            moveTo(9f, 5f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 5f, 7f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 7f, 21f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 19f, 19f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 17f, 5f)
            horizontalLineTo(15f)
            moveTo(9f, 5f)
            arcTo(2f, 2f, 0f, false, true, 11f, 3f)
            horizontalLineTo(13f)
            arcTo(2f, 2f, 0f, false, true, 15f, 5f)
            arcTo(2f, 2f, 0f, false, true, 13f, 7f)
            horizontalLineTo(11f)
            arcTo(2f, 2f, 0f, false, true, 9f, 5f)
            moveTo(14f, 11f)
            horizontalLineTo(11.5f)
            arcTo(1.5f, 1.5f, 0f, false, false, 11.5f, 14f)
            horizontalLineTo(12.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 12.5f, 17f)
            horizontalLineTo(10f)
            moveTo(12f, 17f)
            verticalLineTo(18f)
            moveTo(12f, 10f)
            verticalLineTo(11f)
        }
    }.build().also { cachedReportMoney = it }

/** `ReportMoney` vector, built on first access and reused afterwards. */
private var cachedReportMoney: ImageVector? = null
