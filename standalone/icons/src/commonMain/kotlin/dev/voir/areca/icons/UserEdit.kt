package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * User edit icon.
 *
 * Converted from Tabler Icons `user-edit` (MIT).
 */
public val ArecaIcons.UserEdit: ImageVector
    get() = cachedUserEdit ?: ImageVector.Builder(
        name = "UserEdit",
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
            moveTo(8f, 7f)
            arcTo(4f, 4f, 0f, true, false, 16f, 7f)
            arcTo(4f, 4f, 0f, false, false, 8f, 7f)
            moveTo(6f, 21f)
            verticalLineTo(19f)
            arcTo(4f, 4f, 0f, false, true, 10f, 15f)
            horizontalLineTo(13.5f)
            moveTo(18.42f, 15.61f)
            arcTo(2.1f, 2.1f, 0f, false, true, 21.39f, 18.58f)
            lineTo(18f, 22f)
            horizontalLineTo(15f)
            verticalLineTo(19f)
            lineTo(18.42f, 15.61f)
        }
    }.build().also { cachedUserEdit = it }

/** `UserEdit` vector, built on first access and reused afterwards. */
private var cachedUserEdit: ImageVector? = null
