package dev.voir.areca.foundation.badge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.theme.ArecaTheme

/**
 * Diameter of an [IndicatorDot].
 *
 * A token type rather than a bare [Dp] so the dot sits on the same named scale as every other
 * component, and so a future border width or ring can be added without changing the call sites.
 *
 * @property diameter Width and height of the dot.
 */
@Immutable
public data class IndicatorDotSize(
    public val diameter: Dp,
) {
    /** Named [IndicatorDotSize] steps. */
    public companion object {
        /** 6dp. Inline with small text, where a larger dot would push the line apart. */
        public val Small: IndicatorDotSize = IndicatorDotSize(diameter = 6.dp)

        /** 8dp. The default: a list row's leading status, a tab's unread mark. */
        public val Medium: IndicatorDotSize = IndicatorDotSize(diameter = 8.dp)

        /** 12dp. A dot that has to read on its own, such as an avatar's presence badge. */
        public val Large: IndicatorDotSize = IndicatorDotSize(diameter = 12.dp)
    }
}

/** Default [IndicatorDotSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalIndicatorDotSize: ProvidableCompositionLocal<IndicatorDotSize?> =
    compositionLocalOf { null }

/**
 * A filled circle that marks a status.
 *
 * Deliberately the smallest possible status marker: no label, no count, no shape variation, so a row
 * of them reads as one scale of the same thing. For anything with text, use [Badge].
 *
 * A dot carries its meaning entirely in its color, which assistive technology and color-blind users
 * cannot read. Pass [contentDescription] whenever the dot is the only thing saying what the status
 * is; leave it `null` only when the same status is already stated in text beside it.
 *
 * @param modifier Modifier applied to the dot.
 * @param color Fill color. Defaults to the surrounding content color, so a dot inside a themed row
 *   picks up that row's foreground.
 * @param size Diameter. Defaults to the theme's, or [IndicatorDotSize.Medium].
 * @param border Optional ring, for a dot that must separate from a similarly-colored background —
 *   a presence dot on an avatar, say.
 * @param contentDescription What the color means, read by assistive technology. `null` when the
 *   status is already stated in adjacent text.
 */
@Composable
public fun IndicatorDot(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    size: IndicatorDotSize = LocalIndicatorDotSize.current ?: IndicatorDotSize.Medium,
    border: BorderStroke? = null,
    contentDescription: String? = null,
) {
    Box(
        modifier = modifier
            .size(size.diameter)
            .clip(ArecaTheme.shapes.full)
            .background(color)
            .then(
                if (border != null) {
                    Modifier.border(border, ArecaTheme.shapes.full)
                } else {
                    Modifier
                }
            )
            .then(
                if (contentDescription != null) {
                    Modifier.clearAndSetSemantics { this.contentDescription = contentDescription }
                } else {
                    Modifier
                }
            )
    )
}
