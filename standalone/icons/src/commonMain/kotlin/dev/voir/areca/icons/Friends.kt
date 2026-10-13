package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Friends icon.
 *
 * Converted from Tabler Icons `friends` (MIT).
 */
public val ArecaIcons.Friends: ImageVector
    get() = cachedFriends ?: ImageVector.Builder(
        name = "Friends",
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
            moveTo(5f, 5f)
            arcTo(2f, 2f, 0f, true, false, 9f, 5f)
            arcTo(2f, 2f, 0f, true, false, 5f, 5f)
            moveTo(5f, 22f)
            verticalLineTo(17f)
            lineTo(4f, 16f)
            verticalLineTo(12f)
            arcTo(1f, 1f, 0f, false, true, 5f, 11f)
            horizontalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 10f, 12f)
            verticalLineTo(16f)
            lineTo(9f, 17f)
            verticalLineTo(22f)
            moveTo(15f, 5f)
            arcTo(2f, 2f, 0f, true, false, 19f, 5f)
            arcTo(2f, 2f, 0f, true, false, 15f, 5f)
            moveTo(15f, 22f)
            verticalLineTo(18f)
            horizontalLineTo(13f)
            lineTo(15f, 12f)
            arcTo(1f, 1f, 0f, false, true, 16f, 11f)
            horizontalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 19f, 12f)
            lineTo(21f, 18f)
            horizontalLineTo(19f)
            verticalLineTo(22f)
        }
    }.build().also { cachedFriends = it }

/** `Friends` vector, built on first access and reused afterwards. */
private var cachedFriends: ImageVector? = null
