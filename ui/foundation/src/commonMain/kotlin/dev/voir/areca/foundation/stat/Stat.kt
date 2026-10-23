package dev.voir.areca.foundation.stat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.minimumTouchTarget
import dev.voir.areca.theme.ArecaTheme
import kotlin.math.max

/**
 * One figure with what it is and, optionally, what it means: "Spent · €2,140 · a month".
 *
 * The three lines carry fixed roles, as a row's slots do: the [label] names the figure in the
 * secondary ink, the [value] is the figure itself in a heading style with tabular digits, and the
 * [note] qualifies it. Pass text, never styled text; the style and size decide the type and ink.
 *
 * A stat with [onClick] opens what the figure is about. It has no visible container — a press
 * shows only the indication — so it reads as a figure, not a button, in a grid of figures. Its hit
 * area is at least the minimum touch target, even when its lines are shorter.
 *
 * Assistive technology reads the stat as one node, its lines in order.
 *
 * @param label What the figure is: "Spent", "Kept", "A day".
 * @param value The figure: "€2,140", "41%".
 * @param modifier Modifier applied to the stat.
 * @param note What qualifies the figure: "a month", "weekends 1.8× weekdays". `null` for none.
 * @param onClick Called when the stat is pressed, or `null` for a figure that only reads.
 * @param onClickLabel What pressing does, for assistive technology: "Open spending".
 * @param enabled Whether a pressable stat takes presses. Ignored without [onClick].
 * @param style Inks of the three lines. Defaults to the theme's.
 * @param size Type of the three lines and the gaps between them. Defaults to the theme's, or
 *   [StatSize.Default].
 * @param interactionSource Source of the press and focus interactions, or `null` for one the stat
 *   creates; pass one to observe them from outside.
 * @param trailing Content after the value on its line, such as a [dev.voir.areca.foundation.badge.Badge]
 *   carrying the change against a reference: "−4%".
 */
@Composable
public fun Stat(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    note: String? = null,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    enabled: Boolean = true,
    style: StatStyle = LocalStatStyle.current ?: StatStyle.Default,
    size: StatSize = LocalStatSize.current ?: StatSize.Default,
    interactionSource: MutableInteractionSource? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier
                        .minimumTouchTarget()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = TouchIndication(shape = size.shape),
                            enabled = enabled,
                            onClickLabel = onClickLabel,
                            role = Role.Button,
                            onClick = onClick,
                        )
                        .clickCursor(enabled)
                } else {
                    Modifier.semantics(mergeDescendants = true) {}
                },
            ),
        verticalArrangement = Arrangement.spacedBy(size.gap),
    ) {
        Text(text = label, style = size.labelTextStyle, color = style.label)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(size.trailingGap),
        ) {
            Text(text = value, style = size.valueTextStyle, color = style.value, maxLines = 1)
            trailing?.invoke()
        }
        if (note != null) {
            Text(text = note, style = size.noteTextStyle, color = style.note)
        }
    }
}

/**
 * [Stat]s laid out in equal columns, filling rows from the start edge.
 *
 * For a block of related figures — "your usual month" — read as a set. Every cell takes an equal
 * share of the width, and every cell of a row the height of the row's tallest, so values line up
 * across and down. Pass the stats in reading order.
 *
 * @param modifier Modifier applied to the grid, which fills the width it is given.
 * @param columns How many stats sit side by side. At least `1`.
 * @param columnGap Space between neighbouring columns.
 * @param rowGap Space between rows.
 * @param content The stats, in reading order.
 * @throws IllegalArgumentException When [columns] is below `1`.
 */
@Composable
public fun StatGrid(
    modifier: Modifier = Modifier,
    columns: Int = 2,
    columnGap: Dp = ArecaTheme.spacing.xl2,
    rowGap: Dp = ArecaTheme.spacing.xl,
    content: @Composable () -> Unit,
) {
    require(columns >= 1) { "A stat grid needs at least one column, was $columns." }
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val gapX = columnGap.roundToPx()
        val gapY = rowGap.roundToPx()
        val width = constraints.maxWidth
        val cell = ((width - gapX * (columns - 1)) / columns).coerceAtLeast(0)
        // Row heights come from the cells' intrinsic heights, so every cell can then be measured at
        // its row's height: a pressable stat's indication covers the whole cell.
        val rowHeights = measurables.chunked(columns).map { row -> row.maxOf { it.minIntrinsicHeight(cell) } }
        val placeables = measurables.mapIndexed { index, measurable ->
            val height = rowHeights[index / columns]
            measurable.measure(Constraints(minWidth = cell, maxWidth = cell, minHeight = height))
        }
        val rows = placeables.chunked(columns)
        val height = rowHeights.sum() + gapY * max(0, rows.size - 1)
        layout(width, height) {
            var y = 0
            rows.forEachIndexed { r, row ->
                row.forEachIndexed { c, placeable -> placeable.placeRelative(c * (cell + gapX), y) }
                y += rowHeights[r] + gapY
            }
        }
    }
}

/**
 * Inks of a [Stat].
 *
 * @property label The line naming the figure.
 * @property value The figure.
 * @property note The line qualifying it.
 */
@Immutable
public data class StatStyle(
    public val label: Color,
    public val value: Color,
    public val note: Color,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: the figure in the primary ink, its label and note in the secondary one. */
        public val Default: StatStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param label The line naming the figure.
         * @param value The figure.
         * @param note The line qualifying it.
         * @return Style built from the supplied inks.
         */
        @Composable
        public fun default(
            label: Color = ArecaTheme.colors.fgSubtle,
            value: Color = ArecaTheme.colors.fgBase,
            note: Color = ArecaTheme.colors.fgSubtle,
        ): StatStyle = StatStyle(label = label, value = value, note = note)
    }
}

/**
 * Type and gaps of a [Stat].
 *
 * Take a step — [Default] for a grid of figures, [Large] for the figure a section is about — and
 * change one value with `copy`.
 *
 * @property labelTextStyle Type of the line naming the figure.
 * @property valueTextStyle Type of the figure. Tabular digits, so figures of one width align.
 * @property noteTextStyle Type of the line qualifying it.
 * @property gap Space between the lines.
 * @property trailingGap Space between the figure and its trailing content.
 * @property shape Shape of the press feedback of a pressable stat.
 */
@Immutable
public data class StatSize(
    public val labelTextStyle: TextStyle,
    public val valueTextStyle: TextStyle,
    public val noteTextStyle: TextStyle,
    public val gap: Dp,
    public val trailingGap: Dp,
    public val shape: Shape,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** A figure in a grid of figures. */
        public val Default: StatSize
            @Composable
            get() = default()

        /** The figure a section is about, with its change beside it. */
        public val Large: StatSize
            @Composable
            get() = default(
                labelTextStyle = ArecaTheme.typography.bodySmall,
                valueTextStyle = ArecaTheme.typography.headingExtraLarge.tabular(),
                noteTextStyle = ArecaTheme.typography.bodySmall,
                trailingGap = ArecaTheme.spacing.lg,
            )

        /**
         * Builds a size from the current theme.
         *
         * @param labelTextStyle Type of the line naming the figure.
         * @param valueTextStyle Type of the figure.
         * @param noteTextStyle Type of the line qualifying it.
         * @param gap Space between the lines.
         * @param trailingGap Space between the figure and its trailing content.
         * @param shape Shape of the press feedback.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            labelTextStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
            valueTextStyle: TextStyle = ArecaTheme.typography.headingSmall.tabular(),
            noteTextStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
            gap: Dp = ArecaTheme.spacing.xs2,
            trailingGap: Dp = ArecaTheme.spacing.md,
            shape: Shape = ArecaTheme.shapes.md,
        ): StatSize = StatSize(
            labelTextStyle = labelTextStyle,
            valueTextStyle = valueTextStyle,
            noteTextStyle = noteTextStyle,
            gap = gap,
            trailingGap = trailingGap,
            shape = shape,
        )
    }
}

/** This style with tabular digits, so figures of one width line up. */
private fun TextStyle.tabular(): TextStyle = copy(fontFeatureSettings = "tnum")

/** Default [StatStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalStatStyle: ProvidableCompositionLocal<StatStyle?> = compositionLocalOf { null }

/** Default [StatSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalStatSize: ProvidableCompositionLocal<StatSize?> = compositionLocalOf { null }
