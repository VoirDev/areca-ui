package dev.voir.areca.foundation.list

import androidx.compose.animation.core.Easing
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.icon.ChevronSize
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.theme.ArecaTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Timing of the row state transitions.
 *
 * Press lands fast and releases slowly: the asymmetry is what makes a row feel answerable without
 * feeling jumpy, so [pressIn] should stay well below [release].
 *
 * @property pressIn Duration of the fade into the pressed state.
 * @property release Duration of the fade out of press, and of hover transitions.
 * @property selection Duration of the selected-state transition, rail and indent included.
 * @property easing Easing shared by every state transition.
 */
@Immutable
public data class ListItemStateAnimation(
    public val pressIn: Duration,
    public val release: Duration,
    public val selection: Duration,
    public val easing: Easing,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /**
         * Theme default.
         *
         * The three durations sit between the motion scale's steps: a row carries more of the
         * screen than a control does, so its press has to land sooner and settle later than the
         * shared tokens allow. The easing is the theme's, so a retuned curve moves rows with
         * everything else.
         */
        public val Default: ListItemStateAnimation
            @Composable
            get() = ListItemStateAnimation(
                pressIn = 90.milliseconds,
                release = 320.milliseconds,
                selection = 200.milliseconds,
                easing = ArecaTheme.motion.fade,
            )
    }
}

/**
 * Metrics and typography of a list row, including the geometry of its state decorations.
 *
 * One size covers a whole density step: the same tokens set the row height, the text scale and the
 * size given to icons and chevrons in the row slots, so switching from [ListItemSize.Medium] to
 * [ListItemSize.Compact] rescales the row as a whole rather than only its padding. Adjust a step
 * with `copy` — a different title style, say — rather than assembling one from scratch.
 *
 * @property minHeight Minimum height of the main row line.
 * @property contentPadding Padding between the row edges and its content.
 * @property leadingGap Spacing between the leading slot and the text block.
 * @property trailingGap Spacing between the text block and the trailing slot.
 * @property valueGap Spacing between the inline value and the trailing slot.
 * @property textGap Spacing between the overline, title and subtitle lines.
 * @property bottomGap Spacing above the bottom slot.
 * @property nestedLevelOffset Extra start padding added per nesting level.
 * @property selectedRailWidth Width of the selected rail; `0.dp` hides it.
 * @property selectedRailInset Vertical inset of the selected rail within the row.
 * @property selectedRailRadius Corner radius of the selected rail ends.
 * @property selectedContentOffset Extra start padding a selected row gives its content, so the
 * rail never crowds the leading slot.
 * @property icon Size provided to icons in the row slots.
 * @property chevron Size provided to chevrons in the row slots.
 * @property stateAnimation Durations and easing of the row state transitions.
 * @property overlineTextStyle Text style of the overline.
 * @property titleTextStyle Text style of the title.
 * @property subtitleTextStyle Text style of the subtitle.
 * @property valueTextStyle Text style of the inline value.
 * @property captionTextStyle Text style of the caption, the line of detail under the subtitle.
 */
@Immutable
public data class ListItemSize(
    public val minHeight: Dp,
    public val contentPadding: PaddingValues,
    public val leadingGap: Dp,
    public val trailingGap: Dp,
    public val valueGap: Dp,
    public val textGap: Dp,
    public val bottomGap: Dp,
    public val nestedLevelOffset: Dp,
    public val selectedRailWidth: Dp,
    public val selectedRailInset: Dp,
    public val selectedRailRadius: Dp,
    public val selectedContentOffset: Dp,
    public val icon: Dp,
    public val chevron: ChevronSize,
    public val stateAnimation: ListItemStateAnimation,
    public val overlineTextStyle: TextStyle,
    public val titleTextStyle: TextStyle,
    public val subtitleTextStyle: TextStyle,
    public val valueTextStyle: TextStyle,
    public val captionTextStyle: TextStyle,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** The default row size: [Medium]. */
        public val Default: ListItemSize
            @Composable
            get() = Medium

        /** Dense rows for menus, pickers and secondary lists. */
        public val Compact: ListItemSize
            @Composable
            get() = ListItemSize(
                minHeight = 32.dp,
                contentPadding = PaddingValues(
                    horizontal = ArecaTheme.spacing.lg,
                    vertical = ArecaTheme.spacing.sm,
                ),
                leadingGap = ArecaTheme.spacing.md,
                trailingGap = ArecaTheme.spacing.md,
                valueGap = ArecaTheme.spacing.sm,
                textGap = ArecaTheme.spacing.xs2,
                bottomGap = ArecaTheme.spacing.sm,
                nestedLevelOffset = ArecaTheme.spacing.lg,
                selectedRailWidth = 2.5.dp,
                selectedRailInset = 4.dp,
                selectedRailRadius = 2.dp,
                selectedContentOffset = 3.dp,
                icon = 18.dp,
                chevron = ChevronSize.Small,
                stateAnimation = ListItemStateAnimation.Default,
                overlineTextStyle = ArecaTheme.typography.labelSmall,
                titleTextStyle = ArecaTheme.typography.bodySmall,
                subtitleTextStyle = ArecaTheme.typography.bodyExtraSmall,
                valueTextStyle = ArecaTheme.typography.bodyExtraSmall,
                captionTextStyle = ArecaTheme.typography.bodyExtraSmall,
            )

        /** The default row density, tall enough to be a comfortable touch target. */
        public val Medium: ListItemSize
            @Composable
            get() = ListItemSize(
                minHeight = 44.dp,
                contentPadding = PaddingValues(
                    horizontal = ArecaTheme.spacing.xl,
                    vertical = ArecaTheme.spacing.md,
                ),
                leadingGap = ArecaTheme.spacing.lg,
                trailingGap = ArecaTheme.spacing.lg,
                valueGap = ArecaTheme.spacing.md,
                textGap = ArecaTheme.spacing.xs2,
                bottomGap = ArecaTheme.spacing.md,
                nestedLevelOffset = ArecaTheme.spacing.xl,
                selectedRailWidth = 3.dp,
                selectedRailInset = 6.dp,
                selectedRailRadius = 2.dp,
                selectedContentOffset = 4.dp,
                icon = 20.dp,
                chevron = ChevronSize.Medium,
                stateAnimation = ListItemStateAnimation.Default,
                overlineTextStyle = ArecaTheme.typography.labelSmall,
                titleTextStyle = ArecaTheme.typography.bodyMedium,
                subtitleTextStyle = ArecaTheme.typography.bodySmall,
                valueTextStyle = ArecaTheme.typography.bodySmall,
                captionTextStyle = ArecaTheme.typography.bodyExtraSmall,
            )

        /** Roomy rows for primary content — contacts, conversations, media. */
        public val Comfortable: ListItemSize
            @Composable
            get() = ListItemSize(
                minHeight = 56.dp,
                contentPadding = PaddingValues(
                    horizontal = ArecaTheme.spacing.xl,
                    vertical = ArecaTheme.spacing.lg,
                ),
                leadingGap = ArecaTheme.spacing.xl,
                trailingGap = ArecaTheme.spacing.lg,
                valueGap = ArecaTheme.spacing.lg,
                textGap = ArecaTheme.spacing.xs,
                bottomGap = ArecaTheme.spacing.lg,
                nestedLevelOffset = ArecaTheme.spacing.xl2,
                selectedRailWidth = 3.dp,
                selectedRailInset = 8.dp,
                selectedRailRadius = 2.dp,
                selectedContentOffset = 6.dp,
                icon = 24.dp,
                chevron = ChevronSize.Large,
                stateAnimation = ListItemStateAnimation.Default,
                overlineTextStyle = ArecaTheme.typography.labelMedium,
                titleTextStyle = ArecaTheme.typography.bodyLarge,
                subtitleTextStyle = ArecaTheme.typography.bodyMedium,
                valueTextStyle = ArecaTheme.typography.bodySmall,
                captionTextStyle = ArecaTheme.typography.bodySmall,
            )
    }
}

/** Metrics of the rows of the enclosing list, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalListItemSize: ProvidableCompositionLocal<ListItemSize?> =
    compositionLocalOf { null }

/**
 * Size of the [ListItem] currently being composed, or `null` outside of a row.
 *
 * Differs from [LocalListItemSize] when a row is given its own `size`: row slots such as
 * [ListItemChevron] and [ListItemCheckmark] follow the row they sit in, not the list around it.
 */
internal val LocalListItemRowSize = compositionLocalOf<ListItemSize?> { null }

/**
 * Size of the enclosing row, else the list's, else the default.
 *
 * @return Metrics and timing the row slots should follow.
 */
@Composable
internal fun currentListItemSize(): ListItemSize =
    LocalListItemRowSize.current ?: LocalListItemSize.current ?: ListItemSize.Default

/**
 * Slot layout shared by the rows of the enclosing list.
 *
 * A row overrides the gaps from its own [ListItemSize], so this carries the slot widths and
 * alignments only. [SlotRowLayout.Auto] by default: centred for a row of one or two lines, from the
 * top for a longer one.
 */
public val LocalListItemLayout: ProvidableCompositionLocal<SlotRowLayout> =
    compositionLocalOf { SlotRowLayout.Auto }
