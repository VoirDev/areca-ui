package dev.voir.areca.foundation.button

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.animation.animateColorFromState
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.feedback.LocalSpinnerSize
import dev.voir.areca.foundation.feedback.LocalSpinnerStyle
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.overlay.AnchoredPlacement
import dev.voir.areca.foundation.overlay.TooltipPopup
import dev.voir.areca.foundation.overlay.rememberPointerTooltip
import dev.voir.areca.foundation.overlay.tooltipTrigger
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.FocusOutline
import dev.voir.areca.indication.StrokeIndication
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.indication.reshapedTo
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.minimumTouchTarget
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors, shape, and interaction feedback for [IconButton].
 *
 * The same four-step emphasis ladder as [ButtonStyle], resolved from the same [ActionColor], so an
 * icon button and a labelled button placed side by side belong to each other.
 *
 * The shape lives here rather than on the composable so that the container, the border, and the
 * indication cannot disagree — a circular icon button reacts in a circle. [IconButton] redraws an
 * Areca indication ([dev.voir.areca.indication.TouchIndication],
 * [dev.voir.areca.indication.StrokeIndication], [dev.voir.areca.indication.FadeIndication],
 * [dev.voir.areca.indication.LiquidSweepIndication]) in [shape] whatever shape it was built with,
 * so `style.copy(shape = …)` changes how the button reacts as well as how it is drawn. An
 * indication of another kind has no shape to change and is used as given.
 *
 * @property bg Container fill.
 * @property bgDisabled Container fill while disabled.
 * @property border Border color. [Color.Transparent] draws no visible border.
 * @property borderDisabled Border color while disabled.
 * @property content Color of the icon, and of the loading spinner that replaces it.
 * @property contentDisabled Color of the icon while disabled.
 * @property shape Shape of the container, the border, and the indication. Content is clipped to it.
 * @property indication Hover, press, and focus feedback. An Areca indication is drawn in [shape],
 *   not in the shape it was built with.
 */
@Immutable
public data class IconButtonStyle(
    public val bg: Color,
    public val bgDisabled: Color = bg,
    public val border: Color,
    public val borderDisabled: Color = border,
    public val content: Color,
    public val contentDisabled: Color = content,
    public val shape: Shape,
    public val indication: Indication,
) {
    /** The four steps of the emphasis ladder, loudest first. */
    public companion object {
        /**
         * Filled with the family's base color. The loudest step.
         *
         * @param color Semantic family.
         * @param shape Container shape.
         * @return The solid style for [color].
         */
        @Composable
        public fun solid(
            color: ActionColor = ActionColor.Brand,
            shape: Shape = ArecaTheme.shapes.md,
        ): IconButtonStyle {
            val colors = ArecaTheme.colors
            return IconButtonStyle(
                bg = color.filledBackground(colors),
                bgDisabled = colors.bgDisabled,
                border = Color.Transparent,
                content = color.onFilled(colors),
                contentDisabled = colors.fgDisabled,
                shape = shape,
                indication = TouchIndication(
                    color = color.filledOverlay(colors),
                    focusOutline = FocusOutline(color = color.focusRing(colors)),
                    shape = shape,
                    hoverAlpha = 0.2f,
                    pressMaxAlpha = 0.24f,
                ),
            )
        }

        /**
         * Tinted with the family's muted step.
         *
         * @param color Semantic family.
         * @param shape Container shape.
         * @return The soft style for [color].
         */
        @Composable
        public fun soft(
            color: ActionColor = ActionColor.Brand,
            shape: Shape = ArecaTheme.shapes.md,
        ): IconButtonStyle {
            val colors = ArecaTheme.colors
            return IconButtonStyle(
                bg = color.mutedBackground(colors),
                bgDisabled = colors.bgDisabled,
                border = Color.Transparent,
                content = color.onMuted(colors),
                contentDisabled = colors.fgDisabled,
                shape = shape,
                indication = TouchIndication(
                    color = color.mutedOverlay(colors),
                    focusOutline = FocusOutline(color = color.focusRing(colors)),
                    shape = shape,
                ),
            )
        }

        /**
         * Bordered, with no fill. Reacts by drawing its own outline.
         *
         * @param color Semantic family.
         * @param shape Container shape.
         * @return The outlined style for [color].
         */
        @Composable
        public fun outlined(
            color: ActionColor = ActionColor.Brand,
            shape: Shape = ArecaTheme.shapes.md,
        ): IconButtonStyle {
            val colors = ArecaTheme.colors
            return IconButtonStyle(
                bg = Color.Transparent,
                border = color.outlineBorder(colors),
                borderDisabled = colors.borderDisabled,
                content = color.onSurface(colors),
                contentDisabled = colors.fgDisabled,
                shape = shape,
                indication = StrokeIndication(
                    strokeColor = color.outlineStroke(colors),
                    focusOutline = FocusOutline(color = color.outlineStroke(colors)),
                    shape = shape,
                ),
            )
        }

        /**
         * No fill and no border. The quietest step, and what a toolbar or field affordance uses.
         *
         * Hover and press tint the shape with the icon's own color, so the feedback reads on a
         * light page and a dark one alike.
         *
         * @param color Semantic family.
         * @param shape Container shape.
         * @return The plain style for [color].
         */
        @Composable
        public fun plain(
            color: ActionColor = ActionColor.Brand,
            shape: Shape = ArecaTheme.shapes.md,
        ): IconButtonStyle {
            val colors = ArecaTheme.colors
            return IconButtonStyle(
                bg = Color.Transparent,
                border = Color.Transparent,
                content = color.onSurface(colors),
                contentDisabled = colors.fgDisabled,
                shape = shape,
                indication = FadeIndication.tint(
                    shape = shape,
                    ink = color.onSurface(colors),
                    focusOutline = FocusOutline(color = color.focusRing(colors)),
                ),
            )
        }
    }
}

/**
 * Container, icon, and spinner sizes for one step of [IconButton].
 *
 * The icon fills a little over half the container across the scale. That ratio is what leaves enough
 * ground for the fill to read as a button rather than as a tinted glyph, and it is why [Tiny] — which
 * cannot afford that ground — is reserved for inline use where no fill is drawn.
 *
 * The container ladder matches [FeaturedIconSize] and [AvatarSize], so the three line up in a row.
 * Steps below [Medium] draw smaller than a finger and reserve the rest of [touchTarget] around
 * themselves.
 *
 * @property container Square container size: what the button draws at.
 * @property icon Size the icon is drawn at.
 * @property borderWidth Border width, used by [IconButtonStyle.outlined].
 * @property spinner Size of the loading spinner, which replaces the icon.
 * @property touchTarget Smallest width and height an interactive button occupies in layout. The
 *   container is centred in the slot, and the band around it still answers a touch. Every named
 *   step reserves [dev.voir.areca.theme.ArecaSizing.touchTarget]: 48dp, the minimum a finger
 *   reliably hits, under touch, and nothing under a pointer. [Dp.Unspecified] — the constructor's
 *   default — reserves nothing beyond the container, for a size built around a layout that already
 *   provides the target, such as a bar or a field.
 */
@Immutable
public data class IconButtonSize(
    public val container: Dp,
    public val icon: Dp,
    public val borderWidth: Dp,
    public val spinner: SpinnerSize,
    public val touchTarget: Dp = Dp.Unspecified,
) {
    /**
     * The named steps, smallest first.
     *
     * Each step from [ExtraSmall] up is a step of [ArecaTheme.sizing], so it follows the input: the dp
     * in each step's summary is touch, then pointer. The icon shrinks with the container so it fills
     * the same share of it, and the touch target is the sizing's — 48dp under touch, none under a
     * pointer.
     */
    public companion object {
        /** 20dp. Inline with text, where a full-size button would break the line. */
        public val Tiny: IconButtonSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return IconButtonSize(
                    container = 20.dp,
                    icon = 12.dp,
                    borderWidth = 1.dp,
                    spinner = SpinnerSize.ExtraSmall,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 32dp under touch, 28dp under a pointer. Trailing affordance of a dense row or a compact field. */
        public val ExtraSmall: IconButtonSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return IconButtonSize(
                    container = sizing.controlXs,
                    icon = sizing.select(touch = 18.dp, pointer = 16.dp),
                    borderWidth = 1.dp,
                    spinner = SpinnerSize.Small,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 40dp under touch, 32dp under a pointer. Toolbar action, or a field's clear button. */
        public val Small: IconButtonSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return IconButtonSize(
                    container = sizing.controlSm,
                    icon = sizing.select(touch = 22.dp, pointer = 18.dp),
                    borderWidth = 1.dp,
                    spinner = sizing.select(touch = SpinnerSize.Medium, pointer = SpinnerSize.Small),
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 48dp under touch, 36dp under a pointer. The default. */
        public val Medium: IconButtonSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return IconButtonSize(
                    container = sizing.controlMd,
                    icon = sizing.select(touch = 26.dp, pointer = 20.dp),
                    borderWidth = 1.dp,
                    spinner = SpinnerSize.Medium,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 56dp under touch, 44dp under a pointer. A standalone action, such as a floating control. */
        public val Large: IconButtonSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return IconButtonSize(
                    container = sizing.controlLg,
                    icon = sizing.select(touch = 30.dp, pointer = 24.dp),
                    borderWidth = 1.dp,
                    spinner = SpinnerSize.Large,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 64dp under touch, 52dp under a pointer. An action that is the subject of its own block. */
        public val ExtraLarge: IconButtonSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                return IconButtonSize(
                    container = sizing.controlXl,
                    icon = sizing.select(touch = 34.dp, pointer = 28.dp),
                    borderWidth = 1.dp,
                    spinner = sizing.select(touch = SpinnerSize.ExtraLarge, pointer = SpinnerSize.Large),
                    touchTarget = sizing.touchTarget,
                )
            }
    }
}

/** Default [IconButtonSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalIconButtonSize: ProvidableCompositionLocal<IconButtonSize?> = compositionLocalOf { null }

/** Default [IconButtonStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalIconButtonStyle: ProvidableCompositionLocal<IconButtonStyle?> = compositionLocalOf { null }

/**
 * An action shown as an icon alone, in a square container.
 *
 * Always square, so that a row of them aligns whatever their icons are. An interactive button
 * smaller than [IconButtonSize.touchTarget] reserves the difference around itself in layout, so
 * under touch a 32dp button still takes a 48dp touch.
 *
 * An icon alone says nothing to assistive technology, so give the [Icon] inside a
 * `contentDescription`. The overloads taking an icon directly make that a required parameter, and
 * under a pointer they show it as a tooltip, as [dev.voir.areca.foundation.overlay.TooltipBox] does,
 * when the pointer rests on the button. Wrap this overload in a `TooltipBox` for the same.
 *
 * [loading] replaces the icon with a spinner, blocks the press, and tells assistive technology the
 * button is busy. `null` for [onClick] makes it non-interactive, for an icon inside an
 * already-clickable row; a non-interactive button reserves no touch target.
 *
 * The content is clipped to [IconButtonStyle.shape]; the container itself is not, so the focus ring
 * the indication draws outside it stays whole.
 *
 * @param onClick Called when the button is pressed. `null` makes it non-interactive.
 * @param modifier Modifier applied to the button, outside its touch-target reservation.
 * @param style Colors, shape, and indication. Defaults to the theme's.
 * @param size Container, icon, and spinner sizes. Defaults to the theme's, or
 *   [IconButtonSize.Medium].
 * @param enabled Whether the action is available.
 * @param loading Whether the action is under way.
 * @param loadingStateDescription What assistive technology says about the button while [loading].
 *   English by default; pass a localized string.
 * @param interactionSource Source of the button's press, hover, and focus interactions. `null`
 *   creates one internally; pass one to observe or emit those interactions from outside.
 * @param content Icon or custom content, centred in the container.
 */
@Composable
public fun IconButton(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    style: IconButtonStyle = LocalIconButtonStyle.current ?: IconButtonStyle.solid(),
    size: IconButtonSize = LocalIconButtonSize.current ?: IconButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingStateDescription: String = "Busy",
    interactionSource: MutableInteractionSource? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    IconButtonLayout(
        onClick = onClick,
        modifier = modifier,
        style = style,
        size = size,
        enabled = enabled,
        loading = loading,
        loadingStateDescription = loadingStateDescription,
        interactionSource = interactionSource,
        tooltip = null,
        content = content,
    )
}

/**
 * The icon button itself, with [tooltip] shown under a pointer: the overloads taking an icon pass
 * its content description, which is what a pointer user cannot otherwise read.
 */
@Composable
private fun IconButtonLayout(
    onClick: (() -> Unit)?,
    modifier: Modifier,
    style: IconButtonStyle,
    size: IconButtonSize,
    enabled: Boolean,
    loading: Boolean,
    loadingStateDescription: String,
    interactionSource: MutableInteractionSource?,
    tooltip: String?,
    content: @Composable BoxScope.() -> Unit,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val interactive = enabled && !loading

    val backgroundColor by animateColorFromState(
        enabled = enabled,
        idleColor = style.bg,
        disabledColor = style.bgDisabled,
        label = "iconButtonBackground",
    )
    val borderColor by animateColorFromState(
        enabled = enabled,
        idleColor = style.border,
        disabledColor = style.borderDisabled,
        label = "iconButtonBorder",
    )
    val contentColor by animateColorFromState(
        enabled = enabled,
        idleColor = style.content,
        disabledColor = style.contentDisabled,
        label = "iconButtonContent",
    )

    val click = onClick
    val pointerTooltip = rememberPointerTooltip(enabled = tooltip != null && click != null)
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalIconSize provides size.icon,
    ) {
        Box(
            modifier = modifier
                .thenIf(click != null) { minimumTouchTarget(size.touchTarget) }
                .size(size.container)
                .tooltipTrigger(pointerTooltip)
                .background(backgroundColor, style.shape)
                .border(size.borderWidth, borderColor, style.shape)
                .then(
                    if (click != null) {
                        Modifier
                            .clickable(
                                enabled = interactive,
                                interactionSource = source,
                                // Read straight off the style: remembering it would pin the first
                                // frame's colors and leave them stale after a theme or style change.
                                // Reshaped so the feedback follows `style.shape` after a `copy`.
                                indication = style.indication.reshapedTo(style.shape),
                                role = Role.Button,
                                onClick = click,
                            )
                            .clickCursor(interactive)
                    } else {
                        Modifier
                    },
                )
                .thenIf(loading) {
                    semantics { stateDescription = loadingStateDescription }
                },
            contentAlignment = Alignment.Center,
        ) {
            // The clip sits on the content alone: on the container it would crop the focus ring.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(style.shape),
                contentAlignment = Alignment.Center,
            ) {
                if (loading) {
                    CompositionLocalProvider(
                        LocalSpinnerStyle provides SpinnerStyle(contentColor),
                        LocalSpinnerSize provides size.spinner,
                    ) {
                        Spinner()
                    }
                } else {
                    content()
                }
            }
            if (pointerTooltip != null && tooltip != null) {
                TooltipPopup(pointerTooltip, tooltip, AnchoredPlacement.BelowCenter)
            }
        }
    }
}

/**
 * An action shown as a vector icon alone.
 *
 * @param imageVector Icon to draw.
 * @param contentDescription What the action does, read by assistive technology. `null` only when an
 *   adjacent label already says it.
 * @param onClick Called when the button is pressed. `null` makes it non-interactive.
 * @param modifier Modifier applied to the button, outside its touch-target reservation.
 * @param style Colors, shape, and indication. Defaults to the theme's.
 * @param size Container, icon, and spinner sizes. Defaults to the theme's.
 * @param enabled Whether the action is available.
 * @param loading Whether the action is under way.
 * @param loadingStateDescription What assistive technology says about the button while [loading].
 *   English by default; pass a localized string.
 * @param interactionSource Source of the button's press, hover, and focus interactions. `null`
 *   creates one internally.
 */
@Composable
public fun IconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    style: IconButtonStyle = LocalIconButtonStyle.current ?: IconButtonStyle.solid(),
    size: IconButtonSize = LocalIconButtonSize.current ?: IconButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingStateDescription: String = "Busy",
    interactionSource: MutableInteractionSource? = null,
) {
    IconButtonLayout(
        onClick = onClick,
        modifier = modifier,
        style = style,
        size = size,
        enabled = enabled,
        loading = loading,
        loadingStateDescription = loadingStateDescription,
        interactionSource = interactionSource,
        tooltip = contentDescription,
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription)
    }
}

/**
 * An action shown as a bitmap icon alone.
 *
 * @param imageBitmap Icon to draw.
 * @param contentDescription What the action does, read by assistive technology.
 * @param onClick Called when the button is pressed. `null` makes it non-interactive.
 * @param modifier Modifier applied to the button, outside its touch-target reservation.
 * @param style Colors, shape, and indication. Defaults to the theme's.
 * @param size Container, icon, and spinner sizes. Defaults to the theme's.
 * @param enabled Whether the action is available.
 * @param loading Whether the action is under way.
 * @param loadingStateDescription What assistive technology says about the button while [loading].
 *   English by default; pass a localized string.
 * @param interactionSource Source of the button's press, hover, and focus interactions. `null`
 *   creates one internally.
 */
@Composable
public fun IconButton(
    imageBitmap: ImageBitmap,
    contentDescription: String?,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    style: IconButtonStyle = LocalIconButtonStyle.current ?: IconButtonStyle.solid(),
    size: IconButtonSize = LocalIconButtonSize.current ?: IconButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingStateDescription: String = "Busy",
    interactionSource: MutableInteractionSource? = null,
) {
    IconButtonLayout(
        onClick = onClick,
        modifier = modifier,
        style = style,
        size = size,
        enabled = enabled,
        loading = loading,
        loadingStateDescription = loadingStateDescription,
        interactionSource = interactionSource,
        tooltip = contentDescription,
    ) {
        Icon(bitmap = imageBitmap, contentDescription = contentDescription)
    }
}

/**
 * An action shown as a painted icon alone.
 *
 * @param painter Icon to draw.
 * @param contentDescription What the action does, read by assistive technology.
 * @param onClick Called when the button is pressed. `null` makes it non-interactive.
 * @param modifier Modifier applied to the button, outside its touch-target reservation.
 * @param style Colors, shape, and indication. Defaults to the theme's.
 * @param size Container, icon, and spinner sizes. Defaults to the theme's.
 * @param enabled Whether the action is available.
 * @param loading Whether the action is under way.
 * @param loadingStateDescription What assistive technology says about the button while [loading].
 *   English by default; pass a localized string.
 * @param interactionSource Source of the button's press, hover, and focus interactions. `null`
 *   creates one internally.
 */
@Composable
public fun IconButton(
    painter: Painter,
    contentDescription: String?,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    style: IconButtonStyle = LocalIconButtonStyle.current ?: IconButtonStyle.solid(),
    size: IconButtonSize = LocalIconButtonSize.current ?: IconButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingStateDescription: String = "Busy",
    interactionSource: MutableInteractionSource? = null,
) {
    IconButtonLayout(
        onClick = onClick,
        modifier = modifier,
        style = style,
        size = size,
        enabled = enabled,
        loading = loading,
        loadingStateDescription = loadingStateDescription,
        interactionSource = interactionSource,
        tooltip = contentDescription,
    ) {
        Icon(painter = painter, contentDescription = contentDescription)
    }
}
