package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Apple sign-in mark, filled in black so a tint replaces it (black or white, as Apple requires).
 *
 * Apple is a trademark of Apple Inc.; the mark is maintained by hand, is not covered by the icon licences,
 * and must be used as Apple's Sign in with Apple guidelines require.
 */
public val ArecaIcons.SocialApple: ImageVector
    get() = cachedSocialApple ?: ImageVector.Builder(
        name = "SocialApple",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(16.336f, 1f)
            curveTo(16.336f, 1f, 14.7f, 1.04f, 13.2f, 2.54f)
            curveTo(11.7f, 4.04f, 11.953f, 6.06f, 11.953f, 6.06f)
            curveTo(11.953f, 6.06f, 13.438f, 6.332f, 15.022f, 4.747f)
            curveTo(16.607f, 3.162f, 16.336f, 1f, 16.336f, 1f)
            close()
        }
        path(fill = SolidColor(Color.Black)) {
            moveTo(12.125f, 7.34f)
            curveTo(11.019f, 7.332f, 10.34f, 6.377f, 8.457f, 6.377f)
            curveTo(6.762f, 6.377f, 5.064f, 7.471f, 4.342f, 8.528f)
            curveTo(3.623f, 9.581f, 3f, 10.736f, 3f, 13.079f)
            curveTo(3f, 15.423f, 4.128f, 19.453f, 6.903f, 22.181f)
            curveTo(7.378f, 22.648f, 8.007f, 23f, 8.725f, 23.021f)
            curveTo(10.015f, 23.06f, 10.808f, 22.094f, 12.438f, 22.094f)
            curveTo(14.068f, 22.094f, 14.559f, 23.021f, 16.03f, 23.021f)
            curveTo(16.699f, 23.021f, 17.463f, 22.716f, 18.204f, 21.928f)
            curveTo(19.155f, 20.917f, 20.335f, 18.991f, 20.936f, 17.143f)
            curveTo(20.936f, 17.143f, 17.926f, 15.981f, 17.962f, 12.63f)
            curveTo(17.992f, 9.853f, 20.351f, 8.487f, 20.351f, 8.487f)
            curveTo(20.351f, 8.487f, 18.973f, 6.291f, 16.045f, 6.291f)
            curveTo(14.023f, 6.306f, 13.117f, 7.346f, 12.125f, 7.34f)
            close()
        }
    }.build().also { cachedSocialApple = it }

/** `SocialApple` vector, built on first access and reused afterwards. */
private var cachedSocialApple: ImageVector? = null
