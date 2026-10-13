// Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
// Add or remove entries through `bundledIcons` in this module's build script.

package dev.voir.areca.flow.auth.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Google "G" sign-in mark in Google's four brand colours (#4285F4, #34A853, #FBBC04, #EA4335).
 *
 * Draw it with `tint = Color.Unspecified` so the colours survive. Google is a trademark of Google LLC; the
 * mark is maintained by hand, is not covered by the icon licences, and must be used as Google's sign-in
 * branding guidelines require.
 *
 * Copy of `ArecaIcons.SocialGoogle` from `dev.voir.areca:icons`; its licence notice ships as
 * `META-INF/dev.voir.areca.flow.auth.icon/ICONS-NOTICE.md`.
 */
internal val BundledIcons.SocialGoogle: ImageVector
    get() = cachedSocialGoogle ?: ImageVector.Builder(
        name = "SocialGoogle",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        group(
            clipPathData = PathData {
                moveTo(0f, 0f)
                horizontalLineTo(24f)
                verticalLineTo(24f)
                horizontalLineTo(0f)
                close()
            },
        ) {
            path(fill = SolidColor(Color(0xFF4285F4))) {
                moveTo(23.713f, 12.276f)
                curveTo(23.713f, 11.46f, 23.647f, 10.64f, 23.506f, 9.838f)
                horizontalLineTo(12.188f)
                verticalLineTo(14.459f)
                horizontalLineTo(18.669f)
                curveTo(18.4f, 15.949f, 17.536f, 17.268f, 16.271f, 18.105f)
                verticalLineTo(21.104f)
                horizontalLineTo(20.138f)
                curveTo(22.408f, 19.014f, 23.713f, 15.927f, 23.713f, 12.276f)
                close()
            }
            path(fill = SolidColor(Color(0xFF34A853))) {
                moveTo(12.187f, 24.001f)
                curveTo(15.424f, 24.001f, 18.153f, 22.938f, 20.142f, 21.104f)
                lineTo(16.275f, 18.105f)
                curveTo(15.199f, 18.837f, 13.81f, 19.252f, 12.192f, 19.252f)
                curveTo(9.061f, 19.252f, 6.407f, 17.14f, 5.454f, 14.3f)
                horizontalLineTo(1.464f)
                verticalLineTo(17.391f)
                curveTo(3.501f, 21.443f, 7.65f, 24.001f, 12.187f, 24.001f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFBBC04))) {
                moveTo(5.45f, 14.3f)
                curveTo(4.947f, 12.81f, 4.947f, 11.196f, 5.45f, 9.706f)
                verticalLineTo(6.615f)
                horizontalLineTo(1.464f)
                curveTo(-0.238f, 10.006f, -0.238f, 14.001f, 1.464f, 17.391f)
                lineTo(5.45f, 14.3f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEA4335))) {
                moveTo(12.187f, 4.75f)
                curveTo(13.898f, 4.723f, 15.552f, 5.367f, 16.791f, 6.549f)
                lineTo(20.217f, 3.123f)
                curveTo(18.047f, 1.086f, 15.168f, -0.034f, 12.187f, 0.001f)
                curveTo(7.65f, 0.001f, 3.501f, 2.558f, 1.464f, 6.615f)
                lineTo(5.45f, 9.706f)
                curveTo(6.398f, 6.862f, 9.057f, 4.75f, 12.187f, 4.75f)
                close()
            }
        }
    }.build().also { cachedSocialGoogle = it }

/** `SocialGoogle` vector, built on first access and reused afterwards. */
private var cachedSocialGoogle: ImageVector? = null
