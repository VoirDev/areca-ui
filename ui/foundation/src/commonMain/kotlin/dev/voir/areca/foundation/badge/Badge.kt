package dev.voir.areca.foundation.badge

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.foundation.slot.SlotRow
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.foundation.slot.SlotSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.MinimumTouchTarget
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.minimumTouchTarget
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * Colors for a [Badge]'s three states.
 *
 * Every slot comes in three: the filled resting state, a **soft** variant that tints instead of
 * filling, and a disabled one. Soft is the same meaning at lower volume — a row of ten status badges
 * filled at full strength is unreadable, and soft is what lets one of them stand out.
 *
 * The soft and disabled slots default to the filled value, so a style that has no quieter variant is
 * declared by simply not giving one.
 *
 * @property bg Background of the filled state.
 * @property bgDisabled Background while disabled.
 * @property bgSoft Background of the soft variant.
 * @property content Content color of the filled state.
 * @property contentDisabled Content color while disabled.
 * @property contentSoft Content color of the soft variant.
 * @property border Border of the filled state.
 * @property borderDisabled Border while disabled.
 * @property borderSoft Border of the soft variant.
 */
@Immutable
public data class BadgeStyle(
    public val bg: Color,
    public val bgDisabled: Color = bg,
    public val bgSoft: Color = bg,
    public val content: Color,
    public val contentDisabled: Color = content,
    public val contentSoft: Color = content,
    public val border: Color,
    public val borderDisabled: Color = border,
    public val borderSoft: Color = border,
) {
    /** Theme-derived [BadgeStyle] presets, and [default] for building a variation on the default. */
    public companion object {
        /** Accent-filled, softening to a plain surface with a hairline border. The theme default. */
        public val Default: BadgeStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme, starting from [Default].
         *
         * @param bg Background of the filled state.
         * @param bgDisabled Background while disabled.
         * @param bgSoft Background of the soft variant.
         * @param content Content color of the filled state.
         * @param contentDisabled Content color while disabled.
         * @param contentSoft Content color of the soft variant.
         * @param border Border of the filled state.
         * @param borderDisabled Border while disabled.
         * @param borderSoft Border of the soft variant.
         * @return Style for a badge.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.accentBase,
            bgDisabled: Color = ArecaTheme.colors.bgDisabled,
            bgSoft: Color = ArecaTheme.colors.bgSurface,
            content: Color = ArecaTheme.colors.fgOnFill,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
            contentSoft: Color = ArecaTheme.colors.fgSubtle,
            border: Color = ArecaTheme.colors.accentBase,
            borderDisabled: Color = ArecaTheme.colors.borderDisabled,
            borderSoft: Color = ArecaTheme.colors.borderSubtle,
        ): BadgeStyle = BadgeStyle(
            bg = bg,
            bgDisabled = bgDisabled,
            bgSoft = bgSoft,
            content = content,
            contentDisabled = contentDisabled,
            contentSoft = contentSoft,
            border = border,
            borderDisabled = borderDisabled,
            borderSoft = borderSoft,
        )

        /** Product identity, for a label that is not a status. */
        public val Brand: BadgeStyle
            @Composable
            get() = BadgeStyle(
                bgSoft = ArecaTheme.colors.bgBrandMuted,
                bgDisabled = ArecaTheme.colors.bgDisabled,
                bg = ArecaTheme.colors.bgBrandBase,
                contentSoft = ArecaTheme.colors.fgBrandText,
                contentDisabled = ArecaTheme.colors.fgDisabled,
                content = ArecaTheme.colors.fgOnFill,
                border = Color.Transparent,
            )

        /** Errors, failures, and destructive outcomes. */
        public val Danger: BadgeStyle
            @Composable
            get() = BadgeStyle(
                bgSoft = ArecaTheme.colors.bgDangerMuted,
                bgDisabled = ArecaTheme.colors.bgDisabled,
                bg = ArecaTheme.colors.bgDangerBase,
                contentSoft = ArecaTheme.colors.fgDangerText,
                contentDisabled = ArecaTheme.colors.fgDisabled,
                content = ArecaTheme.colors.fgOnFill,
                border = Color.Transparent,
            )

        /** Cautions. Its filled state keeps a dark foreground, because the warning fill is light in both themes. */
        public val Warning: BadgeStyle
            @Composable
            get() = BadgeStyle(
                bgSoft = ArecaTheme.colors.bgWarnMuted,
                bgDisabled = ArecaTheme.colors.bgDisabled,
                bg = ArecaTheme.colors.bgWarnBase,
                contentSoft = ArecaTheme.colors.fgWarnText,
                contentDisabled = ArecaTheme.colors.fgDisabled,
                content = ArecaTheme.colors.fgBase,
                border = Color.Transparent,
            )

        /** Neutral information, such as a version or a category. */
        public val Info: BadgeStyle
            @Composable
            get() = BadgeStyle(
                bgSoft = ArecaTheme.colors.bgInfoMuted,
                bgDisabled = ArecaTheme.colors.bgDisabled,
                bg = ArecaTheme.colors.bgInfoBase,
                contentSoft = ArecaTheme.colors.fgInfoText,
                contentDisabled = ArecaTheme.colors.fgDisabled,
                content = ArecaTheme.colors.fgOnFill,
                border = Color.Transparent,
            )

        /** Confirmations and healthy states. */
        public val Success: BadgeStyle
            @Composable
            get() = BadgeStyle(
                bgSoft = ArecaTheme.colors.bgSuccessMuted,
                bgDisabled = ArecaTheme.colors.bgDisabled,
                bg = ArecaTheme.colors.bgSuccessBase,
                contentSoft = ArecaTheme.colors.fgSuccessText,
                contentDisabled = ArecaTheme.colors.fgDisabled,
                content = ArecaTheme.colors.fgOnFill,
                border = Color.Transparent,
            )

        /** A status with no sentiment, such as "draft" or "archived". */
        public val Neutral: BadgeStyle
            @Composable
            get() = BadgeStyle(
                bgSoft = ArecaTheme.colors.bgNeutralMuted,
                bgDisabled = ArecaTheme.colors.bgDisabled,
                bg = ArecaTheme.colors.bgNeutralIntense,
                contentSoft = ArecaTheme.colors.fgMuted,
                contentDisabled = ArecaTheme.colors.fgDisabled,
                content = ArecaTheme.colors.fgSubtle,
                border = Color.Transparent,
            )

        /** The inverted surface, for a badge that must separate from a busy background. */
        public val NeutralDark: BadgeStyle
            @Composable
            get() = BadgeStyle(
                bgSoft = ArecaTheme.colors.bgNeutralBase,
                bgDisabled = ArecaTheme.colors.bgDisabled,
                bg = ArecaTheme.colors.bgContrast,
                contentSoft = ArecaTheme.colors.fgNeutral,
                contentDisabled = ArecaTheme.colors.fgDisabled,
                content = ArecaTheme.colors.fgOnContrast,
                border = Color.Transparent,
            )

        /**
         * A row of choices, one of them on: the chosen chip is a solid dark pill, and the rest are
         * the bare word with no fill and no outline.
         *
         * An outline around every chip draws a row of boxes and then asks the reader to spot which
         * one is filled, when the only thing worth seeing is the one that is on. Pair with
         * [BadgeSize.Chip]; a chip is chosen when its badge is not soft.
         */
        public val Chip: BadgeStyle
            @Composable
            get() = NeutralDark.copy(
                bgSoft = Color.Transparent,
                contentSoft = ArecaTheme.colors.fgBase,
                borderSoft = Color.Transparent,
            )

        /**
         * [Chip] with every chip on a ground of its own: the chosen chip is the solid dark pill, the
         * rest sit on the subtle fill.
         *
         * For chips that must read as tappable before any is chosen — a row of filters on a white
         * page, tags in a sheet — where bare words would pass for plain text. Pair with
         * [BadgeSize.Chip]; a chip is chosen when its badge is not soft.
         */
        public val FilledChip: BadgeStyle
            @Composable
            get() = Chip.copy(bgSoft = ArecaTheme.colors.bgSubtle)
    }
}

/**
 * Padding, gaps, and type for one step of [Badge].
 *
 * Vertical padding grows far more slowly than horizontal: a badge is a horizontal capsule, and
 * matching the two makes a large badge read as a button. [minWidth] keeps a one-character badge from
 * collapsing into a circle next to a wider neighbour.
 *
 * @property borderWidth Width of the border, drawn for styles that define one.
 * @property padding Space between the border and the content.
 * @property gap Space between the leading, label, and trailing slots.
 * @property icon Size provided to icons through [LocalIconSize].
 * @property textStyle Style provided to the label through [LocalTextStyle].
 * @property minWidth Smallest width the badge will shrink to.
 * @property touchTarget Smallest width and height a pressable badge reserves in layout, centred on
 *   its drawn capsule, so a finger can hit it. The named steps reserve the sizing's touch target
 *   ([dev.voir.areca.theme.ArecaSizing.touchTarget]): 48dp under touch, none under a pointer. A
 *   static badge reserves nothing.
 */
@Immutable
public data class BadgeSize(
    public val borderWidth: Dp,
    public val padding: PaddingValues,
    public val gap: Dp,
    public val icon: Dp,
    public val textStyle: TextStyle,
    public val minWidth: Dp,
    public val touchTarget: Dp = MinimumTouchTarget,
) {
    /** Named [BadgeSize] steps, the theme default, and [default] for building a variation on it. */
    public companion object {
        /** Inline with small text, such as a count beside a list header. */
        public val ExtraSmall: BadgeSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return BadgeSize(
                    borderWidth = 1.dp,
                    padding = PaddingValues(
                        vertical = spacing.xs2,
                        horizontal = spacing.md,
                    ),
                    gap = spacing.xs,
                    icon = IconSize.ExtraSmall,
                    textStyle = ArecaTheme.typography.bodyExtraSmall,
                    minWidth = 36.dp,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** Inside a dense row or a table cell. */
        public val Small: BadgeSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return BadgeSize(
                    borderWidth = 1.dp,
                    padding = PaddingValues(
                        vertical = spacing.xs,
                        horizontal = spacing.lg,
                    ),
                    gap = spacing.sm,
                    icon = IconSize.ExtraSmall,
                    textStyle = ArecaTheme.typography.bodySmall,
                    minWidth = 48.dp,
                    touchTarget = sizing.touchTarget,
                )
            }

        /** The default: beside body copy, or as a standalone status. */
        public val Medium: BadgeSize
            @Composable
            get() = default()

        /** Theme default: [Medium]. */
        public val Default: BadgeSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme, starting from [Medium].
         *
         * @param borderWidth Width of the border, drawn for styles that define one.
         * @param padding Space between the border and the content.
         * @param gap Space between the leading, label, and trailing slots.
         * @param icon Size provided to icons through [LocalIconSize].
         * @param textStyle Style provided to the label through [LocalTextStyle].
         * @param minWidth Smallest width the badge will shrink to.
         * @param touchTarget Smallest width and height a pressable badge reserves in layout.
         * @return Size for a badge.
         */
        @Composable
        public fun default(
            borderWidth: Dp = 1.dp,
            padding: PaddingValues = PaddingValues(
                vertical = ArecaTheme.spacing.sm,
                horizontal = ArecaTheme.spacing.xl,
            ),
            gap: Dp = ArecaTheme.spacing.md,
            icon: Dp = IconSize.Small,
            textStyle: TextStyle = ArecaTheme.typography.bodyMedium,
            minWidth: Dp = 64.dp,
            touchTarget: Dp = ArecaTheme.sizing.touchTarget,
        ): BadgeSize = BadgeSize(
            borderWidth = borderWidth,
            padding = padding,
            gap = gap,
            icon = icon,
            textStyle = textStyle,
            minWidth = minWidth,
            touchTarget = touchTarget,
        )

        /** A badge that is the subject of its own row, such as a filter chip. */
        public val Large: BadgeSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return BadgeSize(
                    borderWidth = 1.dp,
                    padding = PaddingValues(
                        vertical = spacing.md,
                        horizontal = spacing.xl2,
                    ),
                    gap = spacing.lg,
                    icon = IconSize.Medium,
                    textStyle = ArecaTheme.typography.bodyLarge,
                    minWidth = 96.dp,
                    touchTarget = sizing.touchTarget,
                )
            }

        /**
         * One choice in a row of filter chips — the metrics every control on such a row shares, so
         * a chip carrying a glyph and a chip carrying a word read as one strip.
         *
         * A chip is as wide as its word: the other steps' minimum width is sized for a column of
         * status badges lining up, and here it pads "All" out into a box. The glyph is exactly the
         * line box of the label, so both kinds stand the same height on one baseline. Tight padding
         * and a loose gap between chips are what make the row read as a row of choices.
         */
        public val Chip: BadgeSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return BadgeSize(
                    borderWidth = 1.dp,
                    padding = PaddingValues(
                        vertical = spacing.md,
                        horizontal = spacing.lg,
                    ),
                    gap = spacing.xs,
                    icon = IconSize.Medium,
                    textStyle = ArecaTheme.typography.labelMedium,
                    minWidth = Dp.Unspecified,
                    touchTarget = sizing.touchTarget,
                )
            }
    }
}

/** Default [BadgeSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalBadgeSize: ProvidableCompositionLocal<BadgeSize?> = compositionLocalOf { null }

/** Default [BadgeStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalBadgeStyle: ProvidableCompositionLocal<BadgeStyle?> = compositionLocalOf { null }

/**
 * A compact capsule holding a label, and optionally an icon on either side.
 *
 * The label is a slot rather than a string so a badge can hold a count, a name and a dot, or an
 * inline link, and so the caller decides what is emphasised. The container animates its own width,
 * so a badge whose label changes — "2 items" to "12 items" — grows rather than jumping.
 *
 * Pass [onClick] to make it a filter or a removable tag. It then gains hover, press, and a keyboard
 * focus ring, announces itself as a button, and reserves [BadgeSize.touchTarget] around itself in
 * layout so a finger can hit it. For a row of badges that select something, use [BadgeSelect],
 * which announces each one as a choice rather than as a button.
 *
 * @param modifier Modifier applied to the badge, outside its touch-target reservation.
 * @param style Colors for the filled, soft, and disabled states. Defaults to the theme's.
 * @param size Padding, gaps, type, and touch target. Defaults to the theme's, or [BadgeSize.Default].
 * @param shape Shape of the fill, the border, and the indication alike.
 * @param soft Whether to draw the quieter tinted variant instead of the filled one.
 * @param onClick Called when the badge is pressed. `null` makes it a static label.
 * @param enabled Whether a pressable badge accepts input, and whether it draws enabled colors.
 * @param interactionSource Source of a pressable badge's press, hover, and focus interactions.
 *   `null` creates one internally.
 * @param leading Content before the label, normally an icon or an [IndicatorDot].
 * @param trailing Content after the label, normally a dismiss icon.
 * @param content The label.
 */
@Composable
public fun Badge(
    modifier: Modifier = Modifier,
    style: BadgeStyle = LocalBadgeStyle.current ?: BadgeStyle.Default,
    size: BadgeSize = LocalBadgeSize.current ?: BadgeSize.Default,
    shape: Shape = ArecaTheme.shapes.full,
    soft: Boolean = false,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val click = onClick
    BadgeFrame(
        modifier = modifier,
        style = style,
        size = size,
        shape = shape,
        soft = soft,
        enabled = enabled,
        interaction = if (click != null) {
            { indication ->
                clickable(
                    enabled = enabled,
                    interactionSource = interactionSource,
                    indication = indication,
                    role = Role.Button,
                    onClick = click,
                )
            }
        } else {
            null
        },
        leading = leading,
        trailing = trailing,
        content = content,
    )
}

/**
 * Background of a badge in the given state. Disabled wins over soft.
 *
 * @param soft Whether the quieter tinted variant is drawn.
 * @param enabled Whether the badge draws enabled colors.
 * @return [BadgeStyle.bgDisabled], [BadgeStyle.bgSoft] or [BadgeStyle.bg].
 */
internal fun BadgeStyle.bgFor(soft: Boolean, enabled: Boolean): Color = when {
    !enabled -> bgDisabled
    soft -> bgSoft
    else -> bg
}

/**
 * Content color of a badge in the given state. Disabled wins over soft.
 *
 * @param soft Whether the quieter tinted variant is drawn.
 * @param enabled Whether the badge draws enabled colors.
 * @return [BadgeStyle.contentDisabled], [BadgeStyle.contentSoft] or [BadgeStyle.content].
 */
internal fun BadgeStyle.contentFor(soft: Boolean, enabled: Boolean): Color = when {
    !enabled -> contentDisabled
    soft -> contentSoft
    else -> content
}

/**
 * Border color of a badge in the given state. Disabled wins over soft.
 *
 * @param soft Whether the quieter tinted variant is drawn.
 * @param enabled Whether the badge draws enabled colors.
 * @return [BadgeStyle.borderDisabled], [BadgeStyle.borderSoft] or [BadgeStyle.border].
 */
internal fun BadgeStyle.borderFor(soft: Boolean, enabled: Boolean): Color = when {
    !enabled -> borderDisabled
    soft -> borderSoft
    else -> border
}

/**
 * A [Badge]'s capsule, with whatever input handling its caller attaches.
 *
 * Shared by [Badge], which attaches a click, and the [BadgeSelect] family, which attaches a
 * selection, so the two draw identically. The shape is drawn by the fill and the border rather than
 * by a clip, which would crop the focus ring the indication draws outside the bounds.
 *
 * @param modifier Modifier applied to the badge, outside its touch-target reservation.
 * @param style Colors for the filled, soft, and disabled states.
 * @param size Padding, gaps, type, and touch target.
 * @param shape Shape of the fill, the border, and the indication.
 * @param soft Whether to draw the quieter tinted variant instead of the filled one.
 * @param enabled Whether the badge draws enabled colors, and whether its pointer cursor shows.
 * @param interaction Input handling to attach, given the indication to draw with, or `null` for a
 *   static label. An interactive badge reserves [BadgeSize.touchTarget].
 * @param leading Content before the label.
 * @param trailing Content after the label.
 * @param content The label.
 */
@Composable
internal fun BadgeFrame(
    modifier: Modifier,
    style: BadgeStyle,
    size: BadgeSize,
    shape: Shape,
    soft: Boolean,
    enabled: Boolean,
    interaction: (Modifier.(indication: Indication) -> Modifier)?,
    leading: (@Composable () -> Unit)?,
    trailing: (@Composable () -> Unit)?,
    content: @Composable () -> Unit,
) {
    val motion = ArecaTheme.motion

    val backgroundColor by animateColorAsState(
        targetValue = style.bgFor(soft = soft, enabled = enabled),
        animationSpec = motion.fast.tween(),
        label = "badgeBackground",
    )
    val contentColor by animateColorAsState(
        targetValue = style.contentFor(soft = soft, enabled = enabled),
        animationSpec = motion.fast.tween(),
        label = "badgeContent",
    )
    val borderColor by animateColorAsState(
        targetValue = style.borderFor(soft = soft, enabled = enabled),
        animationSpec = motion.fast.tween(),
        label = "badgeBorder",
    )

    Box(
        modifier = modifier
            .thenIf(interaction != null) { minimumTouchTarget(size.touchTarget) }
            .background(backgroundColor, shape)
            .border(width = size.borderWidth, color = borderColor, shape = shape)
            .then(
                if (interaction != null) {
                    Modifier
                        .interaction(TouchIndication(shape = shape, color = contentColor))
                        .clickCursor(enabled)
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides size.textStyle.merge(
                color = contentColor,
                fontWeight = FontWeight.Medium,
            ),
            LocalIconSize provides size.icon,
        ) {
            SlotRow(
                modifier = Modifier
                    .widthIn(min = size.minWidth)
                    .padding(size.padding)
                    .animateContentSize(animationSpec = motion.medium.tween(motion.standard)),
                layout = SlotRowLayout(
                    start = SlotSize.Wrap,
                    center = SlotSize.Wrap,
                    end = SlotSize.Wrap,
                    startGap = size.gap,
                    endGap = size.gap,
                    verticalAlignment = Alignment.CenterVertically,
                    centerAlignment = Alignment.Center,
                ),
                start = leading?.let { { it() } },
                center = { content() },
                end = trailing?.let { { it() } },
            )
        }
    }
}

/**
 * Diameter, padding, and type for [NotificationBadge].
 *
 * [minDiameter] is a minimum rather than a fixed size: a single digit should be a circle, and a
 * three-character count such as "99+" has to widen into a capsule rather than clipping. [padding] is
 * what does the widening.
 *
 * @property minDiameter Smallest width and the exact height, so a one-character count is circular.
 * @property padding Horizontal space added around a count too wide to fit [minDiameter].
 * @property textStyle Style provided to the count through [LocalTextStyle].
 */
@Immutable
public data class NotificationBadgeSize(
    public val minDiameter: Dp,
    public val padding: Dp,
    public val textStyle: TextStyle,
) {
    /** Named [NotificationBadgeSize] steps. */
    public companion object {
        /** 16dp. A dot-sized count, for overlaying a small icon. */
        public val Small: NotificationBadgeSize
            @Composable
            get() = NotificationBadgeSize(
                minDiameter = 16.dp,
                padding = ArecaTheme.spacing.xs,
                textStyle = ArecaTheme.typography.labelSmall,
            )

        /** 24dp. The default: overlaying a tab bar item or a toolbar icon. */
        public val Medium: NotificationBadgeSize
            @Composable
            get() = NotificationBadgeSize(
                minDiameter = 24.dp,
                padding = ArecaTheme.spacing.sm,
                textStyle = ArecaTheme.typography.labelMedium,
            )

        /** 32dp. A standalone count that is read rather than glanced at. */
        public val Large: NotificationBadgeSize
            @Composable
            get() = NotificationBadgeSize(
                minDiameter = 32.dp,
                padding = ArecaTheme.spacing.md,
                textStyle = ArecaTheme.typography.labelLarge,
            )
    }
}

/** Default [NotificationBadgeSize] in scope, or `null` to use [NotificationBadgeSize.Medium]. */
public val LocalNotificationBadgeSize: ProvidableCompositionLocal<NotificationBadgeSize?> =
    compositionLocalOf { null }

/**
 * Default [BadgeStyle] of a [NotificationBadge] in scope, or `null` to use [BadgeStyle.Brand].
 *
 * Separate from [LocalBadgeStyle]: a count is filled with the brand by default, where a label badge
 * takes the accent.
 */
public val LocalNotificationBadgeStyle: ProvidableCompositionLocal<BadgeStyle?> =
    compositionLocalOf { null }

/**
 * A count, shown as a filled circle or capsule.
 *
 * Distinct from [Badge] in that it holds a number rather than a label, and is normally overlaid on
 * something else rather than sitting in the flow. It draws its filled state unless [soft] asks for
 * the quieter tint, for a count that informs rather than calls to act, and its disabled colors while
 * the control it counts for is unavailable. The border slots of [style] are not drawn.
 *
 * A bare number tells assistive technology nothing about what it counts, so pass
 * [contentDescription] — "3 unread messages", not "3".
 *
 * @param text Count to show. Cap it yourself ("99+") rather than relying on the badge to truncate.
 * @param modifier Modifier applied to the badge container.
 * @param contentDescription What the count refers to, read by assistive technology.
 * @param style Fill and content colors for the filled, soft, and disabled states. Defaults to the
 *   theme's, or [BadgeStyle.Brand].
 * @param size Diameter, padding, and type. Defaults to the theme's, or
 *   [NotificationBadgeSize.Medium].
 * @param shape Shape of the fill.
 * @param soft Whether to draw the quieter tinted variant instead of the filled one.
 * @param enabled Whether to draw enabled colors. Pass the enabled state of the control the badge
 *   counts for.
 */
@Composable
public fun NotificationBadge(
    text: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    style: BadgeStyle = LocalNotificationBadgeStyle.current ?: BadgeStyle.Brand,
    size: NotificationBadgeSize = LocalNotificationBadgeSize.current ?: NotificationBadgeSize.Medium,
    shape: Shape = ArecaTheme.shapes.full,
    soft: Boolean = false,
    enabled: Boolean = true,
) {
    val motion = ArecaTheme.motion
    val backgroundColor by animateColorAsState(
        targetValue = style.bgFor(soft = soft, enabled = enabled),
        animationSpec = motion.fast.tween(),
        label = "notificationBadgeBackground",
    )
    val contentColor by animateColorAsState(
        targetValue = style.contentFor(soft = soft, enabled = enabled),
        animationSpec = motion.fast.tween(),
        label = "notificationBadgeContent",
    )

    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides size.textStyle.merge(
            color = contentColor,
            textAlign = TextAlign.Center,
        ),
    ) {
        Box(
            modifier = modifier
                .then(
                    if (contentDescription != null) {
                        Modifier.clearAndSetSemantics {
                            this.contentDescription = contentDescription
                        }
                    } else {
                        Modifier
                    }
                )
                .background(backgroundColor, shape = shape)
                // Square at the minimum, then widened by padding once the count outgrows it.
                .defaultMinSize(minWidth = size.minDiameter, minHeight = size.minDiameter)
                .height(size.minDiameter)
                .padding(horizontal = size.padding),
            contentAlignment = Alignment.Center,
        ) {
            Text(text, maxLines = 1)
        }
    }
}
