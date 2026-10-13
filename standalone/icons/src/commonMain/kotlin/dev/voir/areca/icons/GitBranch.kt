package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Git branch icon.
 *
 * Converted from Lucide `git-branch` (ISC).
 */
public val ArecaIcons.GitBranch: ImageVector
    get() = cachedGitBranch ?: ImageVector.Builder(
        name = "GitBranch",
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
            moveTo(15f, 6f)
            arcTo(9f, 9f, 0f, false, false, 6f, 15f)
            verticalLineTo(3f)
            moveTo(15f, 6f)
            arcTo(3f, 3f, 0f, true, true, 21f, 6f)
            arcTo(3f, 3f, 0f, true, true, 15f, 6f)
            close()
            moveTo(3f, 18f)
            arcTo(3f, 3f, 0f, true, true, 9f, 18f)
            arcTo(3f, 3f, 0f, true, true, 3f, 18f)
            close()
        }
    }.build().also { cachedGitBranch = it }

/** `GitBranch` vector, built on first access and reused afterwards. */
private var cachedGitBranch: ImageVector? = null
