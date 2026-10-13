package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Folder question icon.
 *
 * Converted from Tabler Icons `folder-question` (MIT).
 */
public val ArecaIcons.FolderQuestion: ImageVector
    get() = cachedFolderQuestion ?: ImageVector.Builder(
        name = "FolderQuestion",
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
            moveTo(15f, 19f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 17f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 5f, 4f)
            horizontalLineTo(9f)
            lineTo(12f, 7f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 9f)
            verticalLineTo(11.5f)
            moveTo(19f, 22f)
            verticalLineTo(22.01f)
            moveTo(19f, 19f)
            arcTo(2.003f, 2.003f, 0f, false, false, 19.914f, 15.218f)
            arcTo(1.98f, 1.98f, 0f, false, false, 17.5f, 15.701f)
        }
    }.build().also { cachedFolderQuestion = it }

/** `FolderQuestion` vector, built on first access and reused afterwards. */
private var cachedFolderQuestion: ImageVector? = null
