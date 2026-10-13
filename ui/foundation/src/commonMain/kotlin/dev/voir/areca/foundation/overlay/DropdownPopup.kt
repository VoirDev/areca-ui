package dev.voir.areca.foundation.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import dev.voir.areca.theme.ArecaTheme

/**
 * How wide a dropdown makes itself relative to the control that opened it.
 *
 * [MatchAnchorWithMin] is the default for a reason: matching the anchor is what makes a dropdown
 * read as belonging to its trigger, but a narrow trigger — an icon button — would give a useless
 * strip, so the minimum rescues it.
 */
@Immutable
public sealed interface DropdownWidth {
    /** Exactly as wide as its anchor. */
    public data object MatchAnchor : DropdownWidth

    /**
     * At least [min] wide, growing with its content and bounded only by the window.
     *
     * Only for content that has a width of its own. Anything that fills the width it is offered —
     * a list of [dev.voir.areca.foundation.list.ListItem]s, a row with a weight — takes the whole
     * window under this mode, because nothing above it says to stop. Use [MatchAnchorWithMin] or
     * [Fixed] for that content.
     *
     * @property min Narrowest the dropdown will be.
     */
    public data class Min(public val min: Dp) : DropdownWidth

    /**
     * Always [width], whatever the anchor and the content.
     *
     * @property width Width of the dropdown.
     */
    public data class Fixed(public val width: Dp) : DropdownWidth

    /**
     * As wide as its anchor, but never narrower than [min].
     *
     * @property min Narrowest the dropdown will be.
     */
    public data class MatchAnchorWithMin(public val min: Dp) : DropdownWidth
}

/**
 * Colors, shape and lift of a dropdown surface.
 *
 * @property bg Fill of the surface.
 * @property border Border of the surface.
 * @property shape Shape of the fill, the border and the shadow.
 * @property shadowElevation How far the surface floats above what it covers.
 */
@Immutable
public data class DropdownPopupStyle(
    public val bg: Color,
    public val border: Color,
    public val shape: Shape,
    public val shadowElevation: Dp,
) {
    /** The theme's default [DropdownPopupStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: DropdownPopupStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg Fill of the surface.
         * @param border Border of the surface.
         * @param shape Shape of the fill, border and shadow.
         * @param shadowElevation How far the surface floats above what it covers.
         * @return Style built from the supplied colors, shape and lift.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgSurface,
            border: Color = ArecaTheme.colors.borderBase,
            shape: Shape = ArecaTheme.shapes.md,
            shadowElevation: Dp = ArecaTheme.elevation.xs,
        ): DropdownPopupStyle = DropdownPopupStyle(
            bg = bg,
            border = border,
            shape = shape,
            shadowElevation = shadowElevation,
        )
    }
}

/**
 * Geometry of a dropdown surface.
 *
 * [maxHeight] caps the surface rather than the window: a dropdown that filled the screen would read
 * as a sheet, and the cap is what keeps it reading as a menu hanging off its trigger.
 *
 * The surface does not scroll by itself: content taller than [maxHeight] is clipped unless it
 * scrolls on its own, with `verticalScroll` or a lazy list. The surface leaves that to the content
 * because a lazy list inside a scrolling container cannot measure.
 *
 * @property offsetX Horizontal nudge from the placement the anchor implies, toward the end edge.
 * @property offsetY Gap between the anchor and the surface.
 * @property width How wide the surface makes itself.
 * @property maxHeight Tallest the surface grows; the content must scroll to reach anything past it.
 * @property borderWidth Width of the surface's border.
 */
@Immutable
public data class DropdownPopupSize(
    public val offsetX: Dp,
    public val offsetY: Dp,
    public val width: DropdownWidth,
    public val maxHeight: Dp,
    public val borderWidth: Dp,
) {
    /** Named [DropdownPopupSize] steps, and [default] for building a variation on them. */
    public companion object {
        /** Theme default: as wide as the trigger, but never narrower than a short menu. */
        public val Default: DropdownPopupSize
            @Composable
            get() = default()

        /**
         * A wider floor, for a dropdown opened from a narrow trigger — a currency or unit segment
         * beside an amount — whose options are far wider than the trigger itself.
         */
        public val Wide: DropdownPopupSize
            @Composable
            get() = default(width = DropdownWidth.MatchAnchorWithMin(min = WideMinWidth))

        /**
         * Builds a size from the current theme.
         *
         * @param offsetX Horizontal nudge from the placement the anchor implies, toward the end
         *   edge.
         * @param offsetY Gap between the anchor and the surface.
         * @param width How wide the surface makes itself.
         * @param maxHeight Tallest the surface grows.
         * @param borderWidth Width of the surface's border.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            offsetX: Dp = ArecaTheme.spacing.none,
            offsetY: Dp = ArecaTheme.spacing.xs,
            width: DropdownWidth = DropdownWidth.MatchAnchorWithMin(min = DefaultMinWidth),
            maxHeight: Dp = DefaultMaxHeight,
            borderWidth: Dp = DefaultBorderWidth,
        ): DropdownPopupSize = DropdownPopupSize(
            offsetX = offsetX,
            offsetY = offsetY,
            width = width,
            maxHeight = maxHeight,
            borderWidth = borderWidth,
        )
    }
}

/** Narrowest a [DropdownPopupSize.Default] dropdown gets: room for a short label and its check. */
private val DefaultMinWidth = 180.dp

/** Narrowest a [DropdownPopupSize.Wide] dropdown gets. */
private val WideMinWidth = 220.dp

/** Tallest a default dropdown grows: about seven compact rows, so it still reads as a menu. */
private val DefaultMaxHeight = 320.dp

/** Border of a default dropdown: a single hairline. */
private val DefaultBorderWidth = 1.dp

/**
 * Whether a dropdown is open, and where its anchor is.
 *
 * Created with [rememberDropdownPopup], which ties it to the anchor's measured position.
 *
 * @property anchorState Where the control that opens this dropdown sits.
 * @property placement Preferred side and alignment relative to the anchor.
 * @param initialOpen Whether the dropdown starts open.
 */
@Stable
public class DropdownPopupState internal constructor(
    public val anchorState: AnchoredState,
    public val placement: AnchoredPlacement,
    initialOpen: Boolean,
) {
    /** Whether the dropdown is showing. */
    public var isOpen: Boolean by mutableStateOf(initialOpen)
        private set

    /** Opens the dropdown. Does nothing if it is already open. */
    public fun show() {
        isOpen = true
    }

    /** Closes the dropdown. Does nothing if it is already closed. */
    public fun dismiss() {
        isOpen = false
    }

    /** Opens a closed dropdown, or closes an open one. */
    public fun toggle() {
        isOpen = !isOpen
    }
}

/**
 * Remembers a dropdown's open state, tied to an anchor.
 *
 * @param initialOpen Whether the dropdown starts open.
 * @param placement Preferred side and alignment relative to the anchor.
 * @param anchorState Where the opening control sits. Apply [Modifier.anchored] to that control with
 *   the same state.
 * @return State to hand to [DropdownPopup].
 */
@Composable
public fun rememberDropdownPopup(
    initialOpen: Boolean = false,
    placement: AnchoredPlacement = AnchoredPlacement.BelowStart,
    anchorState: AnchoredState = rememberAnchoredState(),
): DropdownPopupState {
    return remember(anchorState, placement) {
        DropdownPopupState(
            anchorState = anchorState,
            placement = placement,
            initialOpen = initialOpen,
        )
    }
}

/**
 * A surface hanging off the control that opened it.
 *
 * Shown in a popup window, so it escapes any clipping or scrolling ancestor and floats above the
 * rest of the screen. It positions itself against the anchor's *window* bounds and flips to the
 * other side of the anchor when the preferred side would run off screen.
 *
 * Composes nothing while closed.
 *
 * @param state Whether the dropdown is open, and where its anchor is.
 * @param onDismissRequest Called when the user asks to close it, by back gesture or by pressing
 *   outside.
 * @param modifier Modifier applied to the dropdown surface.
 * @param style Colors, shape and lift. Defaults to the theme's.
 * @param size Offsets, width rule and height cap. Defaults to the theme's.
 * @param dismissOnBackPress Whether the back gesture closes it.
 * @param dismissOnClickOutside Whether pressing outside closes it.
 * @param clippingEnabled Whether the popup is kept inside the window. Turn it off only for a surface
 *   that is meant to overhang, such as one with its own shadow gutter.
 * @param content The dropdown's body, normally a list of rows. It must scroll itself if it can grow
 *   past [DropdownPopupSize.maxHeight]; the surface clips it there.
 */
@Composable
public fun DropdownPopup(
    state: DropdownPopupState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: DropdownPopupStyle = LocalDropdownPopupStyle.current ?: DropdownPopupStyle.Default,
    size: DropdownPopupSize = LocalDropdownPopupSize.current ?: DropdownPopupSize.Default,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    clippingEnabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    if (!state.isOpen) return

    val anchorBounds = state.anchorState.anchorBoundsInWindow ?: return
    val density = LocalDensity.current
    val offsetXPx = with(density) { size.offsetX.roundToPx() }
    val offsetYPx = with(density) { size.offsetY.roundToPx() }

    val positionProvider = remember(anchorBounds, state.placement, offsetXPx, offsetYPx) {
        AnchoredPositionProvider(
            anchorBounds = anchorBounds,
            placement = state.placement,
            offsetX = offsetXPx,
            offsetY = offsetYPx,
        )
    }

    // anchor width in dp (for sizing)
    val anchorWidthDp = with(density) { state.anchorState.anchorSize.width.toDp() }

    Popup(
        popupPositionProvider = positionProvider,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            clippingEnabled = clippingEnabled,
        ),
    ) {
        val widthModifier = when (val w = size.width) {
            is DropdownWidth.MatchAnchor -> Modifier.width(anchorWidthDp)
            is DropdownWidth.Fixed -> Modifier.width(w.width)
            is DropdownWidth.Min -> Modifier.widthIn(min = w.min)
            is DropdownWidth.MatchAnchorWithMin -> Modifier
                .widthIn(min = w.min)
                .width(anchorWidthDp)
        }

        Box(
            modifier = modifier
                .heightIn(max = size.maxHeight)
                // shadow first (not clipped), then clip content
                .shadow(elevation = style.shadowElevation, shape = style.shape, clip = false)
                .clip(style.shape)
                .background(style.bg)
                .border(width = size.borderWidth, color = style.border, shape = style.shape)
                .then(widthModifier),
            content = content
        )
    }
}

/** Default [DropdownPopupStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalDropdownPopupStyle: ProvidableCompositionLocal<DropdownPopupStyle?> =
    compositionLocalOf { null }

/** Default [DropdownPopupSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalDropdownPopupSize: ProvidableCompositionLocal<DropdownPopupSize?> =
    compositionLocalOf { null }
