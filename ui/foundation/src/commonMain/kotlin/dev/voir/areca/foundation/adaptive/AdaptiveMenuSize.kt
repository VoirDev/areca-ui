package dev.voir.areca.foundation.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.list.ListItemSize
import dev.voir.areca.theme.ArecaTheme

/** Which end of an [AdaptiveMenu] row an action's glyph sits at. */
public enum class AdaptiveMenuIconPosition {
    /** Before the label, at the start edge. */
    Leading,

    /** At the far end of the row, after the label, so the labels line up at the start edge. */
    Trailing,
}

/**
 * Geometry of an [AdaptiveMenu]: the card's width and gap, and its rows.
 *
 * [width], [offsetY], [maxHeight] and [borderWidth] apply to the dropdown form; the sheet takes the
 * window's width. [iconPosition] and [item] apply to both.
 *
 * @property width Width of the card. A label too long for it wraps rather than widening the card.
 * @property offsetY Gap between the trigger and the card.
 * @property maxHeight Tallest the card grows; past it the actions scroll.
 * @property borderWidth Width of the card's outline.
 * @property iconPosition Which end of a row the glyph sits at.
 * @property item Metrics of a row: its height, padding, gaps, glyph size and type.
 */
@Immutable
public data class AdaptiveMenuSize(
    public val width: Dp,
    public val offsetY: Dp,
    public val maxHeight: Dp,
    public val borderWidth: Dp,
    public val iconPosition: AdaptiveMenuIconPosition,
    public val item: ListItemSize,
) {
    /** The theme's default [AdaptiveMenuSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default: a 248dp card of full-height rows, each glyph at the far end. */
        public val Default: AdaptiveMenuSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param width Width of the card. Defaults to 248dp: room for a two-word label and its glyph.
         * @param offsetY Gap between the trigger and the card. Defaults to `ArecaTheme.spacing.xs`.
         * @param maxHeight Tallest the card grows. Defaults to 360dp, about seven rows.
         * @param borderWidth Width of the card's outline. Defaults to none.
         * @param iconPosition Which end of a row the glyph sits at. Defaults to
         *   [AdaptiveMenuIconPosition.Trailing].
         * @param item Metrics of a row. Defaults to [ListItemSize.Medium] with the whole row, padding
         *   included, as tall as a control: `ArecaTheme.sizing.controlMd`, 48dp under touch — a full
         *   touch target — and 36dp under a pointer.
         * @return Size built from the supplied values.
         */
        @Composable
        public fun default(
            width: Dp = DefaultWidth,
            offsetY: Dp = ArecaTheme.spacing.xs,
            maxHeight: Dp = DefaultMaxHeight,
            borderWidth: Dp = 0.dp,
            iconPosition: AdaptiveMenuIconPosition = AdaptiveMenuIconPosition.Trailing,
            item: ListItemSize = controlHeightRow(),
        ): AdaptiveMenuSize = AdaptiveMenuSize(
            width = width,
            offsetY = offsetY,
            maxHeight = maxHeight,
            borderWidth = borderWidth,
            iconPosition = iconPosition,
            item = item,
        )
    }
}

/**
 * [ListItemSize.Medium] with its main line cut so the row, padding included, is a control's height.
 *
 * @return Row metrics for a default menu.
 */
@Composable
private fun controlHeightRow(): ListItemSize {
    val row = ListItemSize.Medium
    val padding = row.contentPadding.calculateTopPadding() + row.contentPadding.calculateBottomPadding()
    return row.copy(minHeight = ArecaTheme.sizing.controlMd - padding)
}

/** Width of a default card: room for "Cancel subscription" and its glyph, and no wider. */
private val DefaultWidth = 248.dp

/** Tallest a default card grows: about seven rows, so it still reads as a menu. */
private val DefaultMaxHeight = 360.dp

/** The [AdaptiveMenuSize] an [AdaptiveMenu] takes when none is passed; `null` unless provided. */
public val LocalAdaptiveMenuSize: ProvidableCompositionLocal<AdaptiveMenuSize?> =
    compositionLocalOf { null }
