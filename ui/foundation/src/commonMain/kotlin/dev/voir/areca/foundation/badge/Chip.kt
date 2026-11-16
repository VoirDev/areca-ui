package dev.voir.areca.foundation.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.XClose
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.minimumTouchTarget
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors and shape of a [Chip].
 *
 * @property bg Fill at rest. [Color.Unspecified] takes the well of the surface the chip is drawn on.
 * @property bgSelected Fill while chosen.
 * @property content Ink at rest.
 * @property contentSelected Ink while chosen.
 * @property contentDisabled Ink while unavailable.
 * @property shape Shape of the chip and its press feedback.
 */
@Immutable
public data class ChipStyle(
    public val bg: Color,
    public val bgSelected: Color,
    public val content: Color,
    public val contentSelected: Color,
    public val contentDisabled: Color,
    public val shape: Shape,
) {
    /** The theme's default [ChipStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default: the surface's well, filled with the contrast ink while chosen. */
        public val Default: ChipStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg Fill at rest; [Color.Unspecified] takes the surface's well where drawn.
         * @param bgSelected Fill while chosen.
         * @param content Ink at rest.
         * @param contentSelected Ink while chosen.
         * @param contentDisabled Ink while unavailable.
         * @param shape Shape of the chip.
         * @return The style.
         */
        @Composable
        public fun default(
            bg: Color = Color.Unspecified,
            bgSelected: Color = ArecaTheme.colors.bgContrast,
            content: Color = ArecaTheme.colors.fgBase,
            contentSelected: Color = ArecaTheme.colors.fgOnContrast,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
            shape: Shape = ArecaTheme.shapes.full,
        ): ChipStyle = ChipStyle(bg, bgSelected, content, contentSelected, contentDisabled, shape)
    }
}

/**
 * Metrics and type of a [Chip].
 *
 * @property padding Room around the label; the end shrinks beside a remove cross.
 * @property gap Space between the leading mark, the label and the cross.
 * @property crossSize Size of the remove cross's glyph.
 * @property textStyle Type of the label.
 */
@Immutable
public data class ChipSize(
    public val padding: PaddingValues,
    public val gap: Dp,
    public val crossSize: Dp,
    public val textStyle: TextStyle,
) {
    /** The theme's default [ChipSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: ChipSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param padding Room around the label.
         * @param gap Space between the parts.
         * @param crossSize Size of the remove cross's glyph.
         * @param textStyle Type of the label.
         * @return The size.
         */
        @Composable
        public fun default(
            padding: PaddingValues = PaddingValues(
                horizontal = ArecaTheme.spacing.lg,
                vertical = ArecaTheme.spacing.sm,
            ),
            gap: Dp = ArecaTheme.spacing.sm,
            crossSize: Dp = ArecaTheme.spacing.xl,
            textStyle: TextStyle = ArecaTheme.typography.labelMedium,
        ): ChipSize = ChipSize(padding, gap, crossSize, textStyle)
    }
}

/** Default [ChipStyle] in scope; `null` builds [ChipStyle.Default] where the chip is drawn. */
public val LocalChipStyle: ProvidableCompositionLocal<ChipStyle?> = compositionLocalOf { null }

/** Default [ChipSize] in scope; `null` builds [ChipSize.Default] where the chip is drawn. */
public val LocalChipSize: ProvidableCompositionLocal<ChipSize?> = compositionLocalOf { null }

/**
 * One chip: an applied filter, a tag, a choice that toggles, a suggestion to press — with an
 * optional mark before its label and an optional cross that removes it.
 *
 * Unchosen, it sits in the well of the surface it is on; chosen, it fills with the contrast ink, as
 * a chosen [BadgeSelect] chip does. What a press does follows what you pass:
 * - [onSelectedChange]: a toggle, announced with its state;
 * - [onClick]: a button — a suggestion, a recent search;
 * - only [onRemove]: the whole chip removes it — the applied filters above a list — and the cross
 *   only marks it;
 * - [onRemove] with either of the others: the cross is a target of its own.
 *
 * For a whole set of chips that select, use [BadgeSelect], [FlowBadgeSelect] or [LazyBadgeSelect].
 *
 * @param label What the chip says.
 * @param modifier Modifier applied to the chip.
 * @param selected Whether the chip is chosen.
 * @param onSelectedChange Called with the value the user asks for; `null` for a chip that does not
 *   toggle.
 * @param onClick Called when the chip is pressed, for a chip that is a button; ignored when
 *   [onSelectedChange] is given.
 * @param onRemove Called when the chip is removed; `null` draws no cross.
 * @param removeContentDescription What removing does, already localized — "Remove Food".
 * @param enabled Whether the chip accepts input.
 * @param style Colors and shape. Defaults to the theme's.
 * @param size Padding, gaps and type. Defaults to the theme's.
 * @param leading A mark before the label — a colour dot, an order number, a glyph — or `null`.
 */
@Composable
public fun Chip(
    label: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onSelectedChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null,
    removeContentDescription: String = ArecaTheme.labels.remove,
    enabled: Boolean = true,
    style: ChipStyle = LocalChipStyle.current ?: ChipStyle.Default,
    size: ChipSize = LocalChipSize.current ?: ChipSize.Default,
    leading: (@Composable () -> Unit)? = null,
) {
    val bg = if (selected) style.bgSelected else style.bg.takeOrElse { ArecaTheme.surface.well }
    val ink = when {
        !enabled -> style.contentDisabled
        selected -> style.contentSelected
        else -> style.content
    }
    val shape = style.shape
    val toggle = onSelectedChange
    val press = onClick.takeIf { toggle == null }
    // A chip that can only be removed is removed by a press anywhere on it.
    val removeOnPress = onRemove.takeIf { toggle == null && press == null }

    CompositionLocalProvider(
        LocalContentColor provides ink,
        LocalTextStyle provides size.textStyle,
    ) {
        Row(
            modifier = modifier
                // A chip that does nothing when pressed takes no more room than it draws.
                .thenIf(toggle != null || press != null || removeOnPress != null) { minimumTouchTarget() }
                .background(bg, shape)
                .thenIf(toggle != null) {
                    toggleable(
                        value = selected,
                        enabled = enabled,
                        role = Role.Checkbox,
                        interactionSource = null,
                        indication = TouchIndication(shape = shape),
                        onValueChange = { value -> toggle?.invoke(value) },
                    ).clickCursor(enabled)
                }
                .thenIf(press != null || removeOnPress != null) {
                    clickable(
                        enabled = enabled,
                        role = Role.Button,
                        onClickLabel = if (removeOnPress != null) removeContentDescription else null,
                        interactionSource = null,
                        indication = TouchIndication(shape = shape),
                        onClick = { (press ?: removeOnPress)?.invoke() },
                    ).clickCursor(enabled)
                }
                .padding(size.padding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(size.gap),
        ) {
            leading?.invoke()
            Text(label, maxLines = 1)
            if (onRemove != null) {
                val ownTarget = removeOnPress == null
                Box(
                    modifier = Modifier
                        .thenIf(ownTarget) {
                            minimumTouchTarget()
                                .clickable(
                                    enabled = enabled,
                                    role = Role.Button,
                                    interactionSource = null,
                                    indication = TouchIndication(shape = ArecaTheme.shapes.full),
                                    onClick = onRemove,
                                )
                                .clickCursor(enabled)
                                .semantics { contentDescription = removeContentDescription }
                        }
                        .thenIf(!ownTarget) { clearAndSetSemantics { } },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = BundledIcons.XClose,
                        contentDescription = null,
                        modifier = Modifier.size(size.crossSize),
                        size = size.crossSize,
                    )
                }
            }
        }
    }
}
