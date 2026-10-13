package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Git pull request icon.
 *
 * Converted from Lucide `git-pull-request` (ISC).
 */
public val ArecaIcons.GitPullRequest: ImageVector
    get() = cachedGitPullRequest ?: ImageVector.Builder(
        name = "GitPullRequest",
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
            moveTo(15f, 18f)
            arcTo(3f, 3f, 0f, true, true, 21f, 18f)
            arcTo(3f, 3f, 0f, true, true, 15f, 18f)
            close()
            moveTo(3f, 6f)
            arcTo(3f, 3f, 0f, true, true, 9f, 6f)
            arcTo(3f, 3f, 0f, true, true, 3f, 6f)
            close()
            moveTo(13f, 6f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 18f, 8f)
            verticalLineTo(15f)
            moveTo(6f, 9f)
            lineTo(6f, 21f)
        }
    }.build().also { cachedGitPullRequest = it }

/** `GitPullRequest` vector, built on first access and reused afterwards. */
private var cachedGitPullRequest: ImageVector? = null
