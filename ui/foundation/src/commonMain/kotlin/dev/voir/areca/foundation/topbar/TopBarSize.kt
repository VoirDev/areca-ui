package dev.voir.areca.foundation.topbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics of a [TopBar].
 *
 * Two of the bar's most visible measurements are *derived* from the fields below rather than being
 * fields of their own — [actionEdgeInset] and [actionPadding]. That is the point of this type: a
 * back chevron, a trailing icon and a "Done" label all take their glyph size, their hit target and
 * their distance from the screen gutter from one place, so they cannot drift apart as the bar gains
 * controls. Change [icon] or [action] and every control in the bar moves together.
 *
 * @property padding Space between the bar's edges and its content. The horizontal value is the
 *   screen gutter, so a bar's leading control lines up with the content below it.
 * @property gap Space between the title and the control rows on either side of it.
 * @property actionGap Space between two adjacent controls in the same row.
 * @property titleGap Space between the title and its subtitle.
 * @property icon Size given to every glyph in the bar — in a slot, inside an action, anywhere. One
 *   value, because a back arrow drawn lighter than the icon opposite it is the single most visible
 *   way a bar looks unfinished.
 * @property action Hit target of a control. Held at or above 40dp on the touch steps, because a bar
 *   control is the smallest thing on a phone screen a user is expected to hit. Also the diameter of
 *   a control's disc when the bar's controls are contained; see [TopBarActionStyle].
 * @property containedIcon Size given to a glyph inside a disc. Smaller than [icon], because a glyph
 *   filling a disc to the same extent it fills an empty hit box crowds the disc's edge.
 * @property containedGap Space between two adjacent contained controls. Wider than [actionGap],
 *   because discs spaced as tightly as bare glyphs read as one lumpy shape.
 * @property containedPadding Space either side of the word in a contained worded control — the
 *   capsule a "Done" becomes in a bar of discs.
 * @property minHeight Smallest height of the bar's title row, [padding] included. It is what keeps a
 *   bar holding nothing but a title exactly as tall as one holding icon buttons, so a title does not
 *   change height from screen to screen.
 * @property largeMinHeight Smallest height of a [LargeTopBar]'s title row while it is expanded.
 * @property bottomPadding Space around the bar's [TopBar] bottom band, which holds a tab strip, a
 *   search field or a progress rule.
 * @property borderWidth Thickness of the bar's bottom rule. Drawn only when the rule has a color.
 * @property textStyle Style given to the bar's title.
 * @property subtitleTextStyle Style given to the bar's subtitle.
 * @property actionTextStyle Style given to a worded control, such as "Cancel" or "Done".
 * @property largeTextStyle Style given to a [LargeTopBar]'s headline while it is expanded.
 */
@Immutable
public data class TopBarSize(
    public val padding: PaddingValues,
    public val gap: Dp,
    public val actionGap: Dp,
    public val titleGap: Dp,
    public val icon: Dp,
    public val action: Dp,
    public val containedIcon: Dp,
    public val containedGap: Dp,
    public val containedPadding: Dp,
    public val minHeight: Dp,
    public val largeMinHeight: Dp,
    public val bottomPadding: PaddingValues,
    public val borderWidth: Dp,
    public val textStyle: TextStyle,
    public val subtitleTextStyle: TextStyle,
    public val actionTextStyle: TextStyle,
    public val largeTextStyle: TextStyle,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /**
         * Theme default. A 56dp bar with 24dp glyphs on 40dp targets under touch, a 44dp bar with
         * 20dp glyphs on 32dp targets under a pointer.
         */
        public val Default: TopBarSize
            @Composable
            get() = default()

        /**
         * A tighter bar, 48dp under touch and 36dp under a pointer, for a dense screen or a
         * secondary header inside a panel.
         *
         * Shrinks the glyph as well as the bar, rather than leaving a full-size icon in a short
         * row, because the two together are what read as "compact".
         */
        public val Compact: TopBarSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return default(
                    padding = PaddingValues(
                        horizontal = spacing.horizontalContentInsets,
                        vertical = sizing.select(touch = spacing.sm, pointer = spacing.xs),
                    ),
                    icon = sizing.select(touch = IconSize.Medium, pointer = IconSize.Small),
                    action = sizing.controlXs,
                    containedIcon = IconSize.Small,
                    containedPadding = sizing.select(touch = spacing.md, pointer = spacing.sm),
                    minHeight = sizing.controlMd,
                    largeMinHeight = sizing.select(touch = 84.dp, pointer = 72.dp),
                    textStyle = ArecaTheme.typography.labelLarge,
                    largeTextStyle = ArecaTheme.typography.headingMedium,
                )
            }

        /**
         * A roomier bar, 64dp under touch and 52dp under a pointer, for a screen whose header is the
         * main thing on it.
         */
        public val Comfortable: TopBarSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return default(
                    padding = PaddingValues(
                        horizontal = spacing.horizontalContentInsets,
                        vertical = sizing.select(touch = spacing.lg, pointer = spacing.md),
                    ),
                    action = sizing.select(touch = 44.dp, pointer = 36.dp),
                    minHeight = sizing.controlXl,
                    largeMinHeight = sizing.select(touch = 120.dp, pointer = 108.dp),
                    textStyle = ArecaTheme.typography.headingSmall,
                    largeTextStyle = ArecaTheme.typography.headingExtraLarge,
                )
            }

        /**
         * The bar as a sheet's or dialog's header.
         *
         * Differs from [Default] only in its padding: a modal supplies its own outer insets and a
         * bar inside one that padded itself again would sit a gutter away from the sheet's edge.
         */
        public val Modal: TopBarSize
            @Composable
            get() = default(
                padding = PaddingValues(
                    horizontal = ArecaTheme.spacing.xl,
                    vertical = ArecaTheme.sizing.select(
                        touch = ArecaTheme.spacing.md,
                        pointer = ArecaTheme.spacing.sm,
                    ),
                ),
            )

        /**
         * Builds a size from the current theme.
         *
         * @param padding Space between the bar's edges and its content.
         * @param gap Space between the title and the control rows.
         * @param actionGap Space between two adjacent controls.
         * @param titleGap Space between the title and its subtitle.
         * @param icon Size given to every glyph in the bar.
         * @param action Hit target of a control, and a contained control's diameter.
         * @param containedIcon Size given to a glyph inside a disc.
         * @param containedGap Space between two adjacent contained controls.
         * @param containedPadding Space either side of the word in a contained worded control.
         * @param minHeight Smallest height of the title row.
         * @param largeMinHeight Smallest height of an expanded [LargeTopBar]'s title row.
         * @param bottomPadding Space around the bar's bottom band.
         * @param borderWidth Thickness of the bar's bottom rule.
         * @param textStyle Style given to the title.
         * @param subtitleTextStyle Style given to the subtitle.
         * @param actionTextStyle Style given to a worded control.
         * @param largeTextStyle Style given to an expanded [LargeTopBar]'s headline.
         * @return Size built from the supplied values.
         */
        @Composable
        public fun default(
            padding: PaddingValues = PaddingValues(
                horizontal = ArecaTheme.spacing.horizontalContentInsets,
                vertical = ArecaTheme.sizing.select(
                    touch = ArecaTheme.spacing.md,
                    pointer = ArecaTheme.spacing.sm,
                ),
            ),
            gap: Dp = ArecaTheme.spacing.md,
            actionGap: Dp = ArecaTheme.spacing.xs,
            titleGap: Dp = ArecaTheme.spacing.xs2,
            icon: Dp = ArecaTheme.sizing.select(touch = IconSize.Large, pointer = IconSize.Medium),
            action: Dp = ArecaTheme.sizing.controlSm,
            containedIcon: Dp = ArecaTheme.sizing.select(touch = IconSize.Medium, pointer = IconSize.Small),
            containedGap: Dp = ArecaTheme.spacing.sm,
            containedPadding: Dp = ArecaTheme.sizing.select(
                touch = ArecaTheme.spacing.lg,
                pointer = ArecaTheme.spacing.md,
            ),
            minHeight: Dp = ArecaTheme.sizing.controlLg,
            largeMinHeight: Dp = ArecaTheme.sizing.select(touch = 104.dp, pointer = 92.dp),
            bottomPadding: PaddingValues = PaddingValues(bottom = ArecaTheme.spacing.md),
            borderWidth: Dp = 1.dp,
            textStyle: TextStyle = ArecaTheme.typography.headingExtraSmall,
            subtitleTextStyle: TextStyle = ArecaTheme.typography.bodySmall,
            actionTextStyle: TextStyle = ArecaTheme.typography.labelMedium,
            largeTextStyle: TextStyle = ArecaTheme.typography.headingLarge,
        ): TopBarSize = TopBarSize(
            padding = padding,
            gap = gap,
            actionGap = actionGap,
            titleGap = titleGap,
            icon = icon,
            action = action,
            containedIcon = containedIcon,
            containedGap = containedGap,
            containedPadding = containedPadding,
            minHeight = minHeight,
            largeMinHeight = largeMinHeight,
            bottomPadding = bottomPadding,
            borderWidth = borderWidth,
            textStyle = textStyle,
            subtitleTextStyle = subtitleTextStyle,
            actionTextStyle = actionTextStyle,
            largeTextStyle = largeTextStyle,
        )
    }
}

/**
 * Distance from a control's own edge to the glyph inside it.
 *
 * Half of whatever the hit target has left over once the glyph has taken its share. Every control
 * in the bar is padded by exactly this much and the bar then pulls each control row outward by the
 * same amount, which is what lands a chevron, an icon and a word all on the screen gutter rather
 * than a hit box's width inside it.
 */
internal val TopBarSize.actionEdgeInset: Dp
    get() = ((action - icon) / 2).coerceAtLeast(0.dp)

/**
 * Padding inside a worded control, so its first character starts where an icon's glyph would.
 *
 * Derived rather than stored: a separate field could be set to something other than
 * [actionEdgeInset], and a bar whose "Done" sits two pixels off its trailing icon is exactly the
 * kind of drift this type exists to prevent.
 */
internal val TopBarSize.actionPadding: PaddingValues
    get() = PaddingValues(horizontal = actionEdgeInset)

/** Spinner that stands in for a glyph of this bar's icon size while an action is working. */
internal val TopBarSize.spinner: SpinnerSize
    get() = SpinnerSize(container = icon)

/**
 * Icon-button metrics for a control in this bar.
 *
 * Built from the bar's own tokens instead of a named [IconButtonSize] step, because
 * [dev.voir.areca.foundation.button.IconButton] re-provides [dev.voir.areca.foundation.icon.LocalIconSize] from
 * whatever size it is given. Handing it a step of its own would stop the bar's icon token at the
 * slot boundary and leave the glyphs inside its buttons a different size from the ones outside
 * them.
 */
internal val TopBarSize.iconButtonSize: IconButtonSize
    get() = IconButtonSize(
        container = action,
        icon = icon,
        borderWidth = 0.dp,
        spinner = spinner,
    )

/**
 * Diameter of an avatar standing in for a glyph, such as an account control.
 *
 * Larger than [icon], because a face shrunk to a glyph's size stops being recognisable — and paired
 * below with a hit target widened by the same amount, so [actionEdgeInset] still describes the gap
 * between the control's edge and what is drawn inside it.
 */
internal val TopBarSize.avatar: Dp
    get() = action - actionEdgeInset

/**
 * Hit target of a control whose content is an [avatar] rather than a glyph.
 *
 * Sized so the avatar sits [actionEdgeInset] inside it, exactly as a glyph sits inside [action].
 * That is what lets the bar pull every control row out by one value and still land an avatar, an
 * icon and a word on the same gutter.
 */
internal val TopBarSize.avatarAction: Dp
    get() = avatar + actionEdgeInset * 2

/**
 * Icon-button metrics for a control whose content is an [avatar].
 *
 * A separate step rather than [iconButtonSize] with a wider modifier, because
 * [dev.voir.areca.foundation.button.IconButton] sizes itself *after* the modifier it is given: an outer
 * width is silently ignored, and the avatar would end up in an ordinary control's box at an
 * ordinary control's inset — which is the misalignment this whole type exists to rule out.
 */
internal val TopBarSize.avatarButtonSize: IconButtonSize
    get() = IconButtonSize(
        container = avatarAction,
        icon = avatar,
        borderWidth = 0.dp,
        spinner = spinner,
    )

/**
 * Button metrics for a worded control in this bar.
 *
 * Hugs its label rather than taking a button's minimum width, because a bar control is measured
 * against the title beside it and not against the other buttons on the screen. Its padding is
 * [actionPadding], so "Done" starts where a trailing icon's glyph would.
 */
internal val TopBarSize.buttonSize: ButtonSize
    get() = ButtonSize(
        borderWidth = 0.dp,
        padding = actionPadding,
        gap = actionGap,
        icon = icon,
        textStyle = actionTextStyle,
        minWidth = 0.dp,
        minHeight = action,
        spinner = spinner,
    )

/** Spinner that stands in for a glyph inside a disc while a contained action is working. */
internal val TopBarSize.containedSpinner: SpinnerSize
    get() = SpinnerSize(container = containedIcon)

/**
 * Icon-button metrics for a contained control: an [action]-wide disc with a [containedIcon] glyph.
 *
 * A contained control is aligned by its edge, so unlike [iconButtonSize] there is no inset to
 * describe — the disc *is* the thing that meets the gutter.
 */
internal val TopBarSize.containedIconButtonSize: IconButtonSize
    get() = IconButtonSize(
        container = action,
        icon = containedIcon,
        borderWidth = 1.dp,
        spinner = containedSpinner,
    )

/**
 * Icon-button metrics for a contained account control: the avatar fills the disc.
 *
 * The disc's own fill would only show as a ring around the face, so the face takes the whole of it.
 */
internal val TopBarSize.containedAvatarButtonSize: IconButtonSize
    get() = IconButtonSize(
        container = action,
        icon = action,
        borderWidth = 0.dp,
        spinner = spinner,
    )

/**
 * Button metrics for a contained worded control: a capsule as tall as a disc, padded by
 * [containedPadding].
 */
internal val TopBarSize.containedButtonSize: ButtonSize
    get() = ButtonSize(
        borderWidth = 1.dp,
        padding = PaddingValues(horizontal = containedPadding),
        gap = actionGap,
        icon = containedIcon,
        textStyle = actionTextStyle,
        minWidth = action,
        minHeight = action,
        spinner = containedSpinner,
    )

/** Default [TopBarSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalTopBarSize: ProvidableCompositionLocal<TopBarSize?> = compositionLocalOf { null }
