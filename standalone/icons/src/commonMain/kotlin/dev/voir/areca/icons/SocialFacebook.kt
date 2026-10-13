package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Facebook "f" sign-in mark in Facebook blue (#0866FF).
 *
 * Draw it with `tint = Color.Unspecified` so the colour survives. Facebook is a trademark of Meta Platforms,
 * Inc.; the mark is maintained by hand, is not covered by the icon licences, and must be used as Meta's brand
 * guidelines for Facebook Login require.
 */
public val ArecaIcons.SocialFacebook: ImageVector
    get() = cachedSocialFacebook ?: ImageVector.Builder(
        name = "SocialFacebook",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            fill = SolidColor(Color(0xFF0866FF)),
            pathFillType = PathFillType.EvenOdd,
        ) {
            moveTo(24f, 12.073f)
            curveTo(24f, 5.406f, 18.627f, 0f, 12f, 0f)
            curveTo(5.373f, 0f, 0f, 5.406f, 0f, 12.073f)
            curveTo(0f, 18.098f, 4.388f, 23.093f, 10.125f, 24f)
            verticalLineTo(15.564f)
            horizontalLineTo(7.077f)
            verticalLineTo(12.073f)
            horizontalLineTo(10.125f)
            verticalLineTo(9.413f)
            curveTo(10.125f, 6.387f, 11.917f, 4.715f, 14.658f, 4.715f)
            curveTo(15.971f, 4.715f, 17.344f, 4.951f, 17.344f, 4.951f)
            verticalLineTo(7.922f)
            horizontalLineTo(15.831f)
            curveTo(14.341f, 7.922f, 13.875f, 8.852f, 13.875f, 9.808f)
            verticalLineTo(12.073f)
            horizontalLineTo(17.203f)
            lineTo(16.671f, 15.564f)
            horizontalLineTo(13.875f)
            verticalLineTo(24f)
            curveTo(19.612f, 23.095f, 24f, 18.1f, 24f, 12.073f)
            close()
        }
    }.build().also { cachedSocialFacebook = it }

/** `SocialFacebook` vector, built on first access and reused afterwards. */
private var cachedSocialFacebook: ImageVector? = null
