package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Users minus icon.
 *
 * Converted from Tabler Icons `users-minus` (MIT).
 */
public val ArecaIcons.UsersMinus: ImageVector
    get() = cachedUsersMinus ?: ImageVector.Builder(
        name = "UsersMinus",
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
            moveTo(5f, 7f)
            arcTo(4f, 4f, 0f, true, false, 13f, 7f)
            arcTo(4f, 4f, 0f, false, false, 5f, 7f)
            moveTo(3f, 21f)
            verticalLineTo(19f)
            arcTo(4f, 4f, 0f, false, true, 7f, 15f)
            horizontalLineTo(11f)
            curveTo(11.948f, 15f, 12.818f, 15.33f, 13.504f, 15.88f)
            moveTo(16f, 3.13f)
            arcTo(4f, 4f, 0f, false, true, 16f, 10.88f)
            moveTo(16f, 19f)
            horizontalLineTo(22f)
        }
    }.build().also { cachedUsersMinus = it }

/** `UsersMinus` vector, built on first access and reused afterwards. */
private var cachedUsersMinus: ImageVector? = null
