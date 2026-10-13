package dev.voir.areca.foundation.layout

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * Where a divider's label sits along the line.
 *
 * Named along the line's own axis rather than by screen edge, because the same value has to mean
 * something on both a horizontal and a vertical divider: [Leading] is the left of a horizontal rule
 * and the top of a vertical one.
 */
public enum class DividerTextPosition {
    /** Label in the middle, with a line on each side. The default, and the "OR" case. */
    Center,

    /** Label first, line filling the rest. Reads as a section title. */
    Leading,

    /** Line first, label at the far end. Reads as a trailing note. */
    Trailing,
}

/**
 * Dash pattern for a divider's line.
 *
 * Present means dashed, absent means solid — one field rather than a length plus a gap that only
 * matters when the length is set.
 *
 * @property length Length of each dash. Shorter than the line's thickness renders as overlapping
 *   dots, because the line is drawn with round caps.
 * @property gap Space between dashes.
 */
@Immutable
public data class DividerDash(
    public val length: Dp,
    public val gap: Dp,
)

/**
 * Line and label colors for a divider.
 *
 * Shared by [HorizontalDivider] and [VerticalDivider]: nothing about a divider's color depends on
 * which way it runs, so a theme sets it once.
 *
 * @property line Color of the rule.
 * @property text Color of the optional label.
 */
@Immutable
public data class DividerStyle(
    public val line: Color,
    public val text: Color,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: the base border color with a muted label. */
        public val Default: DividerStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param line Color of the rule.
         * @param text Color of the optional label.
         * @return A divider style containing the supplied colors.
         */
        @Composable
        public fun default(
            line: Color = ArecaTheme.colors.borderBase,
            text: Color = ArecaTheme.colors.fgMuted,
        ): DividerStyle = DividerStyle(line = line, text = text)
    }
}

/**
 * Thickness, inset, dashing, and label type for a divider.
 *
 * Shared by [HorizontalDivider] and [VerticalDivider], so a theme's divider metrics cannot drift
 * between the two orientations.
 *
 * @property thickness Width of the rule.
 * @property inset Space left blank at each end of the line, along its own axis. The divider still
 *   occupies the full width or height; only the drawn line is shortened.
 * @property dash Dash pattern, or `null` for a solid line.
 * @property textPadding Space between the label and the line beside it.
 * @property textStyle Style provided to the label through [LocalTextStyle].
 */
@Immutable
public data class DividerSize(
    public val thickness: Dp,
    public val inset: Dp,
    public val dash: DividerDash?,
    public val textPadding: Dp,
    public val textStyle: TextStyle,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: a hairline solid rule, flush to both ends. */
        public val Default: DividerSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param thickness Width of the rule.
         * @param inset Space left blank at each end of the line.
         * @param dash Dash pattern, or `null` for a solid line.
         * @param textPadding Space between the label and the line beside it.
         * @param textStyle Style of the label.
         * @return A divider size containing the supplied metrics.
         */
        @Composable
        public fun default(
            thickness: Dp = 1.dp,
            inset: Dp = 0.dp,
            dash: DividerDash? = null,
            textPadding: Dp = ArecaTheme.spacing.lg,
            textStyle: TextStyle = ArecaTheme.typography.labelSmall,
        ): DividerSize = DividerSize(
            thickness = thickness,
            inset = inset,
            dash = dash,
            textPadding = textPadding,
            textStyle = textStyle,
        )
    }
}

/** Default [DividerSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalDividerSize: ProvidableCompositionLocal<DividerSize?> = compositionLocalOf { null }

/** Default [DividerStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalDividerStyle: ProvidableCompositionLocal<DividerStyle?> =
    compositionLocalOf { null }

/**
 * A horizontal rule separating stacked content, optionally with a label in it.
 *
 * Fills the width it is given. An unlabelled divider is decorative and is hidden from assistive
 * technology; a labelled one announces itself as a heading, because that is what a label in a rule
 * is — "OR" between two sign-in options, "Yesterday" between two groups of messages.
 *
 * @param modifier Modifier applied to the divider.
 * @param style Line and label colors. Defaults to the theme's.
 * @param size Thickness, inset, dashing, and label type. Defaults to the theme's.
 * @param text Label placed in the rule. `null` draws an unbroken line.
 * @param textPosition Where [text] sits along the line.
 */
@Composable
public fun HorizontalDivider(
    modifier: Modifier = Modifier,
    style: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    size: DividerSize = LocalDividerSize.current ?: DividerSize.Default,
    text: String? = null,
    textPosition: DividerTextPosition = DividerTextPosition.Center,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = size.inset)
            .then(dividerSemantics(text)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DividerLabelStyle(style, size) {
            dividerSlots(text, textPosition).forEach { slot ->
                when (slot) {
                    DividerSlot.Line -> DividerLine(
                        modifier = Modifier.weight(1f).height(size.thickness),
                        horizontal = true,
                        color = style.line,
                        thickness = size.thickness,
                        dash = size.dash,
                    )

                    DividerSlot.Label -> Text(
                        text = text.orEmpty(),
                        modifier = when (textPosition) {
                            DividerTextPosition.Center ->
                                Modifier.padding(horizontal = size.textPadding)

                            DividerTextPosition.Leading ->
                                Modifier.padding(end = size.textPadding)

                            DividerTextPosition.Trailing ->
                                Modifier.padding(start = size.textPadding)
                        },
                    )
                }
            }
        }
    }
}

/**
 * A vertical rule separating side-by-side content, optionally with a label in it.
 *
 * Fills the height it is given, so it needs a parent that constrains it — a `Row` with
 * `IntrinsicSize.Min`, or a fixed height. [DividerTextPosition.Leading] places the label at the top.
 *
 * @param modifier Modifier applied to the divider.
 * @param style Line and label colors. Defaults to the theme's.
 * @param size Thickness, inset, dashing, and label type. Defaults to the theme's.
 * @param text Label placed in the rule. `null` draws an unbroken line.
 * @param textPosition Where [text] sits along the line.
 */
@Composable
public fun VerticalDivider(
    modifier: Modifier = Modifier,
    style: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    size: DividerSize = LocalDividerSize.current ?: DividerSize.Default,
    text: String? = null,
    textPosition: DividerTextPosition = DividerTextPosition.Center,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = size.inset)
            .then(dividerSemantics(text)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DividerLabelStyle(style, size) {
            dividerSlots(text, textPosition).forEach { slot ->
                when (slot) {
                    DividerSlot.Line -> DividerLine(
                        modifier = Modifier.weight(1f).width(size.thickness),
                        horizontal = false,
                        color = style.line,
                        thickness = size.thickness,
                        dash = size.dash,
                    )

                    DividerSlot.Label -> Text(
                        text = text.orEmpty(),
                        modifier = when (textPosition) {
                            DividerTextPosition.Center ->
                                Modifier.padding(vertical = size.textPadding)

                            DividerTextPosition.Leading ->
                                Modifier.padding(bottom = size.textPadding)

                            DividerTextPosition.Trailing ->
                                Modifier.padding(top = size.textPadding)
                        },
                    )
                }
            }
        }
    }
}

/** One piece of a divider, in the order it is laid out. */
private enum class DividerSlot {
    /** A stretch of rule, sharing the remaining space equally with any other line slot. */
    Line,

    /** The label. */
    Label,
}

/**
 * Lays out the pieces a divider is made of.
 *
 * A centred label has a line on each side; an end-positioned one has a single line taking all the
 * remaining space, which is what makes the rule run to the far edge rather than being centred under
 * it.
 *
 * @param text Label, or `null` for an unbroken line.
 * @param position Where the label sits.
 * @return Slots in layout order.
 */
private fun dividerSlots(
    text: String?,
    position: DividerTextPosition,
): List<DividerSlot> = when {
    text == null -> listOf(DividerSlot.Line)
    position == DividerTextPosition.Center ->
        listOf(DividerSlot.Line, DividerSlot.Label, DividerSlot.Line)

    position == DividerTextPosition.Leading -> listOf(DividerSlot.Label, DividerSlot.Line)
    else -> listOf(DividerSlot.Line, DividerSlot.Label)
}

/**
 * Hides an unlabelled divider from assistive technology, and marks a labelled one as a heading.
 *
 * @param text Label, or `null` when the divider is purely decorative.
 * @return Semantics to apply to the divider container.
 */
private fun dividerSemantics(text: String?): Modifier = if (text == null) {
    Modifier.clearAndSetSemantics { }
} else {
    Modifier.semantics { heading() }
}

/**
 * Provides the label's style and color to [content].
 *
 * @param style Colors to provide.
 * @param size Type to provide.
 * @param content Divider slots.
 */
@Composable
private fun DividerLabelStyle(
    style: DividerStyle,
    size: DividerSize,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalTextStyle provides size.textStyle,
        LocalContentColor provides style.text,
        content = content,
    )
}

/**
 * Draws one stretch of rule.
 *
 * Round caps make a dashed line read as a row of lozenges rather than of bricks; on a solid line
 * they are invisible.
 *
 * @param modifier Modifier carrying the slot's weight and cross-axis thickness.
 * @param horizontal Whether the line runs across or down.
 * @param color Color of the rule.
 * @param thickness Width of the rule.
 * @param dash Dash pattern, or `null` for a solid line.
 */
@Composable
private fun DividerLine(
    modifier: Modifier,
    horizontal: Boolean,
    color: Color,
    thickness: Dp,
    dash: DividerDash?,
) {
    Canvas(modifier = modifier) {
        val pathEffect = dash?.let {
            PathEffect.dashPathEffect(floatArrayOf(it.length.toPx(), it.gap.toPx()), 0f)
        }

        val (start, end) = if (horizontal) {
            Offset(0f, size.height / 2f) to Offset(size.width, size.height / 2f)
        } else {
            Offset(size.width / 2f, 0f) to Offset(size.width / 2f, size.height)
        }

        drawLine(
            color = color,
            start = start,
            end = end,
            strokeWidth = thickness.toPx(),
            cap = StrokeCap.Round,
            pathEffect = pathEffect,
        )
    }
}
