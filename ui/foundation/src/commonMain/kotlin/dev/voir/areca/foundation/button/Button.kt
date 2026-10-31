package dev.voir.areca.foundation.button

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.animation.animateColorFromState
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.feedback.LocalSpinnerSize
import dev.voir.areca.foundation.feedback.LocalSpinnerStyle
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.foundation.slot.SlotRow
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.FocusOutline
import dev.voir.areca.indication.StrokeIndication
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.indication.reshapedTo
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.minimumTouchTarget
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * Colors, shape, and interaction feedback for [Button].
 *
 * Four factories build one from an [ActionColor], and they are an emphasis ladder rather than four
 * unrelated looks: [solid] claims the page's one primary action, [soft] and [outlined] sit beside
 * it, and [plain] recedes into the layout. A screen with two solid buttons has no primary action.
 *
 * [shape] is the one source of the outline: the container, the border, and the indication all
 * follow it. The factories build the indication in the shape they are given, and [Button] redraws
 * an Areca indication ([dev.voir.areca.indication.TouchIndication],
 * [dev.voir.areca.indication.StrokeIndication], [dev.voir.areca.indication.FadeIndication],
 * [dev.voir.areca.indication.LiquidSweepIndication]) in [shape] whatever shape it was built with,
 * so `style.copy(shape = …)` changes how the button reacts as well as how it is drawn. An
 * indication of another kind has no shape to change and is used as given.
 *
 * @property bg Container fill.
 * @property bgDisabled Container fill while disabled.
 * @property border Border color. [Color.Transparent] draws no visible border.
 * @property borderDisabled Border color while disabled.
 * @property content Color of the label.
 * @property contentDisabled Color of the label while disabled.
 * @property extra Color of the leading and trailing slots, and of the loading spinner. Separate from
 *   [content] so an icon can step back from the label it accompanies.
 * @property extraDisabled Color of those slots while disabled.
 * @property shape Shape of the container, the border, and the indication.
 * @property indication Hover, press, and focus feedback. An Areca indication is drawn in [shape],
 *   not in the shape it was built with.
 */
@Immutable
public data class ButtonStyle(
    public val bg: Color,
    public val bgDisabled: Color = bg,
    public val border: Color,
    public val borderDisabled: Color = border,
    public val content: Color,
    public val contentDisabled: Color = content,
    public val extra: Color = content,
    public val extraDisabled: Color = contentDisabled,
    public val shape: Shape,
    public val indication: Indication,
) {
    /** The four steps of the emphasis ladder, loudest first. */
    public companion object {
        /**
         * Filled with the family's base color. The loudest step — one per screen.
         *
         * @param color Semantic family.
         * @param shape Container shape. Defaults to the theme's button shape.
         * @return The style for [color] at this step of the ladder.
         */
        @Composable
        public fun solid(
            color: ActionColor = ActionColor.Brand,
            shape: Shape = ArecaTheme.shapes.button,
        ): ButtonStyle {
            val colors = ArecaTheme.colors
            return ButtonStyle(
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
                    // Stronger than the defaults: the overlay has to register against a
                    // saturated fill, not against a page background.
                    hoverAlpha = 0.2f,
                    pressMaxAlpha = 0.24f,
                ),
            )
        }

        /**
         * Tinted with the family's muted step. A secondary action that still reads as its family.
         *
         * The label clears 4.5:1 on the tint in both themes. For `Brand` that means a label one ramp
         * step past the design's accent pairing — darker in the light theme, lighter in the dark —
         * which is a deliberate departure from the design for this one pairing.
         *
         * @param color Semantic family.
         * @param shape Container shape. Defaults to the theme's button shape.
         * @return The style for [color] at this step of the ladder.
         */
        @Composable
        public fun soft(
            color: ActionColor = ActionColor.Brand,
            shape: Shape = ArecaTheme.shapes.button,
        ): ButtonStyle {
            val colors = ArecaTheme.colors
            return ButtonStyle(
                bg = color.mutedBackground(colors),
                bgDisabled = colors.bgDisabled,
                border = Color.Transparent,
                content = color.onMuted(colors),
                contentDisabled = colors.fgDisabled,
                extra = color.onMutedSecondary(colors),
                extraDisabled = colors.fgDisabled,
                shape = shape,
                indication = TouchIndication(
                    color = color.mutedOverlay(colors),
                    focusOutline = FocusOutline(color = color.focusRing(colors)),
                    shape = shape,
                ),
            )
        }

        /**
         * Bordered, with no fill. Reacts by drawing its own outline rather than by tinting.
         *
         * @param color Semantic family.
         * @param shape Container shape. Defaults to the theme's button shape.
         * @return The style for [color] at this step of the ladder.
         */
        @Composable
        public fun outlined(
            color: ActionColor = ActionColor.Brand,
            shape: Shape = ArecaTheme.shapes.button,
        ): ButtonStyle {
            val colors = ArecaTheme.colors
            return ButtonStyle(
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
         * No fill and no border. The quietest step, for an action that must not pull the eye.
         *
         * Hover and press tint the shape with the label's own color, so the feedback reads on a
         * light page and a dark one alike.
         *
         * @param color Semantic family.
         * @param shape Container shape. Defaults to the theme's button shape.
         * @return The style for [color] at this step of the ladder.
         */
        @Composable
        public fun plain(
            color: ActionColor = ActionColor.Brand,
            shape: Shape = ArecaTheme.shapes.button,
        ): ButtonStyle {
            val colors = ArecaTheme.colors
            return ButtonStyle(
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
 * Padding, type, and minimum bounds for one step of [Button].
 *
 * [minWidth] and [minHeight] are what make a row of buttons line up regardless of label length, and
 * what keeps a short label ("OK") from shrinking the button to nothing. [touchTarget] is separate
 * from them: the smaller steps draw below a finger's reach, and reserve the rest around themselves.
 *
 * @property borderWidth Width of the border, drawn for styles that define one.
 * @property padding Space between the border and the content.
 * @property gap Space between the leading, label, and trailing slots.
 * @property icon Size provided to icons through [LocalIconSize].
 * @property textStyle Style of the label.
 * @property minWidth Smallest width the button will shrink to.
 * @property minHeight Smallest height the button draws at.
 * @property spinner Size of the loading spinner, which replaces the trailing slot.
 * @property touchTarget Smallest width and height an interactive button occupies in layout. The
 *   button draws at its own size, centred in the slot, and the band around it still answers a touch.
 *   Every named step reserves [dev.voir.areca.theme.ArecaSizing.touchTarget]: 48dp, the minimum a
 *   finger reliably hits, under touch, and nothing under a pointer. [Dp.Unspecified] — the
 *   constructor's default — reserves nothing beyond the button's own bounds, for a size built
 *   around a layout that already provides the target, such as a bar or a field.
 */
@Immutable
public data class ButtonSize(
    public val borderWidth: Dp,
    public val padding: PaddingValues,
    public val gap: Dp,
    public val icon: Dp,
    public val textStyle: TextStyle,
    public val minWidth: Dp,
    public val minHeight: Dp,
    public val spinner: SpinnerSize,
    public val touchTarget: Dp = Dp.Unspecified,
) {
    /**
     * The named steps, smallest first.
     *
     * Each step's height is a step of [ArecaTheme.sizing], so it follows the input: the dp in each
     * step's summary is touch, then pointer. Paddings shrink with the height so the label still sits
     * centred, and the touch target is the sizing's — 48dp under touch, none under a pointer.
     */
    public companion object {
        /** 32dp tall under touch, 28dp under a pointer. Inside a dense row or a table cell. */
        public val ExtraSmall: ButtonSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return ButtonSize(
                    borderWidth = 1.dp,
                    padding = PaddingValues(
                        horizontal = sizing.select(touch = spacing.lg, pointer = spacing.md),
                        vertical = sizing.select(touch = spacing.sm, pointer = spacing.xs),
                    ),
                    gap = sizing.select(touch = spacing.sm, pointer = spacing.xs),
                    icon = IconSize.ExtraSmall,
                    textStyle = ArecaTheme.typography.labelSmall,
                    minWidth = sizing.select(touch = 64.dp, pointer = 56.dp),
                    minHeight = sizing.controlXs,
                    spinner = SpinnerSize.ExtraSmall,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 40dp tall under touch, 32dp under a pointer. Beside a field, or in a toolbar. */
        public val Small: ButtonSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return ButtonSize(
                    borderWidth = 1.dp,
                    padding = PaddingValues(
                        horizontal = sizing.select(touch = spacing.xl, pointer = spacing.lg),
                        vertical = sizing.select(touch = spacing.md, pointer = spacing.sm),
                    ),
                    gap = sizing.select(touch = spacing.md, pointer = spacing.sm),
                    icon = sizing.select(touch = IconSize.Small, pointer = IconSize.ExtraSmall),
                    textStyle = ArecaTheme.typography.labelMedium,
                    minWidth = sizing.select(touch = 72.dp, pointer = 64.dp),
                    minHeight = sizing.controlSm,
                    spinner = sizing.select(touch = SpinnerSize.Small, pointer = SpinnerSize.ExtraSmall),
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 48dp tall under touch, 36dp under a pointer. The default. */
        public val Medium: ButtonSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return ButtonSize(
                    borderWidth = 1.dp,
                    padding = PaddingValues(
                        horizontal = sizing.select(touch = spacing.xl2, pointer = spacing.xl),
                        vertical = sizing.select(touch = spacing.lg, pointer = spacing.md),
                    ),
                    gap = sizing.select(touch = spacing.lg, pointer = spacing.md),
                    icon = sizing.select(touch = IconSize.Medium, pointer = IconSize.Small),
                    textStyle = ArecaTheme.typography.labelLarge,
                    minWidth = sizing.select(touch = 96.dp, pointer = 80.dp),
                    minHeight = sizing.controlMd,
                    spinner = SpinnerSize.Small,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 56dp tall under touch, 44dp under a pointer. A screen's primary action, alone at the bottom. */
        public val Large: ButtonSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return ButtonSize(
                    borderWidth = 1.dp,
                    padding = PaddingValues(
                        horizontal = sizing.select(touch = spacing.xl4, pointer = spacing.xl3),
                        vertical = sizing.select(touch = spacing.xl, pointer = spacing.lg),
                    ),
                    gap = sizing.select(touch = spacing.lg, pointer = spacing.md),
                    icon = IconSize.Medium,
                    textStyle = ArecaTheme.typography.labelLarge,
                    minWidth = sizing.select(touch = 112.dp, pointer = 96.dp),
                    minHeight = sizing.controlLg,
                    spinner = sizing.select(touch = SpinnerSize.Medium, pointer = SpinnerSize.Small),
                    touchTarget = sizing.touchTarget,
                )
            }

        /** 64dp tall under touch, 52dp under a pointer. An onboarding or landing screen's single call to action. */
        public val ExtraLarge: ButtonSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return ButtonSize(
                    borderWidth = 1.dp,
                    padding = PaddingValues(
                        horizontal = sizing.select(touch = spacing.xl5, pointer = spacing.xl4),
                        vertical = sizing.select(touch = spacing.xl2, pointer = spacing.lg),
                    ),
                    gap = sizing.select(touch = spacing.xl, pointer = spacing.lg),
                    icon = sizing.select(touch = IconSize.Large, pointer = IconSize.Medium),
                    textStyle = sizing.select(
                        touch = ArecaTheme.typography.bodyExtraLarge,
                        pointer = ArecaTheme.typography.bodyLarge.merge(fontWeight = FontWeight.Medium),
                    ),
                    minWidth = sizing.select(touch = 118.dp, pointer = 104.dp),
                    minHeight = sizing.controlXl,
                    spinner = SpinnerSize.Medium,
                    touchTarget = sizing.touchTarget,
                )
            }
    }
}

/** Default [ButtonSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalButtonSize: ProvidableCompositionLocal<ButtonSize?> = compositionLocalOf { null }

/** Default [ButtonStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalButtonStyle: ProvidableCompositionLocal<ButtonStyle?> = compositionLocalOf { null }

/**
 * An action the user can take, with a label and optional icons on either side.
 *
 * The label is a slot rather than a string so a button can hold a count, a shortcut hint, or styled
 * text — see the [String] overload for the common case.
 *
 * Three states, and they are not interchangeable. `enabled = false` means the action is unavailable
 * and says so by greying out. [loading] means the action is *under way*: the click is blocked, the
 * trailing slot becomes a spinner, and assistive technology is told the button is busy. Passing
 * `null` for [onClick] means there is no action at all — for a button inside an already-clickable
 * row, where a nested click target would swallow the row's own press.
 *
 * The container animates its own width, so a label that changes while loading does not jump.
 *
 * An interactive button smaller than [ButtonSize.touchTarget] reserves the difference around itself
 * in layout, so its touch target is never smaller than a finger even where it draws smaller.
 *
 * @param onClick Called when the button is pressed. `null` makes it non-interactive.
 * @param modifier Modifier applied to the button, outside its touch-target reservation.
 * @param style Colors, shape, and indication. Defaults to the theme's.
 * @param size Padding, type, and minimum bounds. Defaults to the theme's, or [ButtonSize.Medium].
 * @param textStyle Style of the label. Defaults to the one [size] carries.
 * @param textAlign Alignment of the label within its slot.
 * @param layout How the leading, label, and trailing slots share the width.
 * @param enabled Whether the action is available.
 * @param loading Whether the action is under way.
 * @param loadingStateDescription What assistive technology says about the button while [loading].
 *   English by default; pass a localized string.
 * @param interactionSource Source of the button's press, hover, and focus interactions. `null`
 *   creates one internally; pass one to observe or emit those interactions from outside.
 * @param leading Content before the label, normally an icon.
 * @param trailing Content after the label, normally an icon. Replaced by the spinner while [loading].
 * @param content The label.
 */
@Composable
public fun Button(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    style: ButtonStyle = LocalButtonStyle.current ?: ButtonStyle.solid(),
    size: ButtonSize = LocalButtonSize.current ?: ButtonSize.Medium,
    textStyle: TextStyle = size.textStyle,
    textAlign: TextAlign = TextAlign.Center,
    layout: SlotRowLayout = SlotRowLayout.wrap(startGap = size.gap, endGap = size.gap),
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingStateDescription: String = "Busy",
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val motion = ArecaTheme.motion
    val interactive = enabled && !loading

    val backgroundColor by animateColorFromState(
        enabled = enabled,
        idleColor = style.bg,
        disabledColor = style.bgDisabled,
        label = "buttonBackground",
    )
    val borderColor by animateColorFromState(
        enabled = enabled,
        idleColor = style.border,
        disabledColor = style.borderDisabled,
        label = "buttonBorder",
    )
    val contentColor by animateColorFromState(
        enabled = enabled,
        idleColor = style.content,
        disabledColor = style.contentDisabled,
        label = "buttonContent",
    )
    val extraColor by animateColorFromState(
        enabled = enabled,
        idleColor = style.extra,
        disabledColor = style.extraDisabled,
        label = "buttonExtra",
    )

    val click = onClick
    Box(
        modifier = modifier
            .thenIf(click != null) { minimumTouchTarget(size.touchTarget) }
            .widthIn(min = size.minWidth)
            .heightIn(min = size.minHeight)
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
        CompositionLocalProvider(LocalIconSize provides size.icon) {
            SlotRow(
                modifier = Modifier
                    .clipToBounds()
                    .padding(size.padding)
                    .animateContentSize(animationSpec = motion.medium.tween(motion.standard)),
                layout = layout,
                start = leading?.let {
                    { ExtraSlot(extraColor) { it() } }
                },
                center = {
                    CompositionLocalProvider(
                        LocalTextStyle provides textStyle.merge(textAlign = textAlign),
                        LocalContentColor provides contentColor,
                    ) {
                        content()
                    }
                },
                end = when {
                    loading -> {
                        {
                            CompositionLocalProvider(
                                LocalSpinnerStyle provides SpinnerStyle(extraColor),
                                LocalSpinnerSize provides size.spinner,
                            ) {
                                Spinner()
                            }
                        }
                    }

                    trailing != null -> {
                        { ExtraSlot(extraColor) { trailing() } }
                    }

                    else -> null
                },
            )
        }
    }
}

/**
 * An action the user can take, labelled with [text].
 *
 * The overload to reach for. See the slot-based [Button] for a label that is not plain text.
 *
 * @param text The label.
 * @param onClick Called when the button is pressed. `null` makes it non-interactive.
 * @param modifier Modifier applied to the button, outside its touch-target reservation.
 * @param style Colors, shape, and indication. Defaults to the theme's.
 * @param size Padding, type, and minimum bounds. Defaults to the theme's, or [ButtonSize.Medium].
 * @param textStyle Style of the label. Defaults to the one [size] carries.
 * @param textAlign Alignment of the label within its slot.
 * @param layout How the leading, label, and trailing slots share the width.
 * @param enabled Whether the action is available.
 * @param loading Whether the action is under way.
 * @param loadingStateDescription What assistive technology says about the button while [loading].
 *   English by default; pass a localized string.
 * @param interactionSource Source of the button's press, hover, and focus interactions. `null`
 *   creates one internally.
 * @param leading Content before the label, normally an icon.
 * @param trailing Content after the label, normally an icon. Replaced by the spinner while [loading].
 */
@Composable
public fun Button(
    text: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    style: ButtonStyle = LocalButtonStyle.current ?: ButtonStyle.solid(),
    size: ButtonSize = LocalButtonSize.current ?: ButtonSize.Medium,
    textStyle: TextStyle = size.textStyle,
    textAlign: TextAlign = TextAlign.Center,
    layout: SlotRowLayout = SlotRowLayout.wrap(startGap = size.gap, endGap = size.gap),
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingStateDescription: String = "Busy",
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        style = style,
        size = size,
        textStyle = textStyle,
        textAlign = textAlign,
        layout = layout,
        enabled = enabled,
        loading = loading,
        loadingStateDescription = loadingStateDescription,
        interactionSource = interactionSource,
        leading = leading,
        trailing = trailing,
    ) {
        Text(text)
    }
}

/**
 * Provides the leading or trailing slot's color to [content].
 *
 * @param color Color to provide.
 * @param content Slot content.
 */
@Composable
private fun ExtraSlot(color: Color, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentColor provides color, content = content)
}
