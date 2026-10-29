package dev.voir.areca.media.image

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * A marker on a point of a picture — the chair in a room, the cap of a bottle — pressable to say
 * more about what is there.
 *
 * Lay it in a frame's `pictureOverlay` with
 * [pinned][MediaPictureScope.pinned], so it stays on its point whatever the frame's shape and as the
 * picture zooms. With no [text] it is a pulsing dot; with a number or a word it is a small capsule.
 * Its pressable area is at least a touch target whatever it draws. Name what it marks in
 * [contentDescription] — "Ridge Chair, 420 dollars".
 *
 * @param onClick Called when the marker is pressed.
 * @param contentDescription What it marks, read out.
 * @param modifier Modifier applied to the marker; put [MediaPictureScope.pinned] here.
 * @param text A number or a short word drawn in a capsule, or `null` for the dot.
 * @param selected Whether its thing is the one picked, ringed in [HotspotStyle.ring].
 * @param style Colors.
 */
@Composable
public fun Hotspot(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    text: String? = null,
    selected: Boolean = false,
    style: HotspotStyle = HotspotStyle.default(),
) {
    Box(
        modifier = modifier
            .sizeIn(minWidth = TOUCH_TARGET, minHeight = TOUCH_TARGET)
            .clickCursor()
            .clickable(interactionSource = null, indication = null, role = Role.Button, onClick = onClick)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        val ring = if (selected) Modifier.border(2.dp, style.ring, CircleShape).padding(3.dp) else Modifier
        if (text == null) {
            PulseDot(style, ring)
        } else {
            Text(
                text = text,
                modifier = ring
                    .shadow(4.dp, ArecaTheme.shapes.full)
                    .background(style.bg, ArecaTheme.shapes.full)
                    .padding(horizontal = ArecaTheme.spacing.md, vertical = ArecaTheme.spacing.xs),
                style = ArecaTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = style.content,
                maxLines = 1,
            )
        }
    }
}

/**
 * The dot: a solid centre in a soft halo, with a ring spreading out of it on a loop.
 *
 * @param style Colors.
 * @param modifier Modifier applied to the halo.
 */
@Composable
private fun PulseDot(style: HotspotStyle, modifier: Modifier) {
    val pulse = rememberInfiniteTransition(label = "hotspot")
    val spread by pulse.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(PULSE_MILLIS, easing = LinearEasing), RepeatMode.Restart),
        label = "hotspotSpread",
    )

    Box(modifier.size(HALO), contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(HALO)
                .graphicsLayer {
                    scaleX = 0.6f + spread
                    scaleY = 0.6f + spread
                    alpha = 1f - spread
                }
                .border(2.dp, style.bg, CircleShape),
        )
        Box(Modifier.size(HALO).background(style.bg.copy(alpha = HALO_ALPHA), CircleShape))
        Box(Modifier.size(DOT).shadow(2.dp, CircleShape).background(style.bg, CircleShape))
    }
}

/**
 * Colors of a [Hotspot].
 *
 * @property bg The dot and the capsule.
 * @property content Words in the capsule.
 * @property ring The ring of a selected marker.
 */
@Immutable
public data class HotspotStyle(
    public val bg: Color,
    public val content: Color,
    public val ring: Color,
) {
    /** The theme's markers. */
    public companion object {
        /**
         * White markers with dark words, readable on any photograph, ringed in the brand colour.
         *
         * @param bg The dot and the capsule.
         * @param content Words in the capsule.
         * @param ring The ring of a selected marker.
         * @return The style.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.baseColors.baseWhite,
            content: Color = ArecaTheme.baseColors.baseBlack,
            ring: Color = ArecaTheme.colors.accentBase,
        ): HotspotStyle = HotspotStyle(bg = bg, content = content, ring = ring)
    }
}

/** Smallest pressable side of a marker. */
private val TOUCH_TARGET = 44.dp

/** Side of the dot's halo. */
private val HALO = 28.dp

/** Side of the dot's solid centre. */
private val DOT = 12.dp

/** How see-through the halo is. */
private const val HALO_ALPHA = 0.35f

/** One pulse of the ring. */
private const val PULSE_MILLIS = 2000
