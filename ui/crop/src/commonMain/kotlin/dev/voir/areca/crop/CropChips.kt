package dev.voir.areca.crop

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.minimumTouchTarget
import dev.voir.areca.theme.ArecaTheme

/**
 * One aspect choice in a [CropToolbar]'s Format tool: a glyph of its proportions and its label.
 *
 * A radio button for assistive technology; the row that holds the chips is its selectable group. The
 * chip is drawn [CropToolbarSize.chipHeight] tall in a slot of at least
 * [CropToolbarSize.chipTouchTarget], and answers a press with foundation's tint indication, whose
 * focus ring follows the pill.
 *
 * @param label Name of the aspect, such as "Free" or "4:3".
 * @param glyphRatio Width over height the glyph is drawn at, or `null` for a free aspect, drawn as a
 *   dashed square.
 * @param selected Whether this is the aspect in use.
 * @param onClick Called when the chip is chosen.
 * @param style Colors.
 * @param size Chip and glyph metrics.
 */
@Composable
internal fun AspectChip(
    label: String,
    glyphRatio: Float?,
    selected: Boolean,
    onClick: () -> Unit,
    style: CropToolbarStyle,
    size: CropToolbarSize,
) {
    val content = if (selected) style.chipSelectedContent else style.chipContent
    val shape = ArecaTheme.shapes.full
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .minimumTouchTarget(size.chipTouchTarget)
            .height(size.chipHeight)
            // A shaped fill and border rather than a clip: a clip would crop the focus ring, which is
            // drawn outside the chip.
            .background(if (selected) style.chipSelectedBg else Color.Transparent, shape)
            .border(size.chipBorder, if (selected) style.chipSelectedBg else style.chipBorder, shape)
            .selectable(
                selected = selected,
                interactionSource = interactionSource,
                indication = FadeIndication.tint(shape = shape, ink = ArecaTheme.colors.fgBase),
                role = Role.RadioButton,
                onClick = onClick,
            )
            .clickCursor()
            .padding(horizontal = ArecaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Canvas(Modifier.size(size.chipGlyph)) { drawAspectGlyph(glyphRatio, content, size) }
        Text(text = label, style = chipTextStyle(), color = content)
    }
}

/**
 * Label style of a chip: tabular figures, so "16:9" and "4:5" sit on the same grid.
 *
 * @return The theme's medium label style with tabular figures.
 */
@Composable
private fun chipTextStyle(): TextStyle = ArecaTheme.typography.labelMedium.copy(fontFeatureSettings = "tnum")

/**
 * Draws a rectangle of [ratio] centred in the glyph box, or a dashed square for a free aspect.
 *
 * @param ratio Width over height, or `null` for free.
 * @param color Stroke color.
 * @param metrics Glyph stroke and corner.
 */
private fun DrawScope.drawAspectGlyph(ratio: Float?, color: Color, metrics: CropToolbarSize) {
    val stroke = metrics.chipGlyphStroke.toPx()
    val box = size.width - stroke
    val (width, height) = when {
        ratio == null -> box * FreeGlyphShare to box * FreeGlyphShare
        ratio >= 1f -> box to box / ratio
        else -> box * ratio to box
    }
    drawRoundRect(
        color = color,
        topLeft = Offset((size.width - width) / 2f, (size.height - height) / 2f),
        size = Size(width, height),
        cornerRadius = CornerRadius(metrics.chipGlyphCorner.toPx()),
        style = Stroke(
            width = stroke,
            pathEffect = if (ratio == null) PathEffect.dashPathEffect(floatArrayOf(stroke * DashStrokes, stroke * DashStrokes)) else null,
        ),
    )
}

/** Share of the glyph box the free aspect's dashed square takes. */
private const val FreeGlyphShare = 0.85f

/** Length of a dash, and of the gap after it, in glyph strokes. */
private const val DashStrokes = 1.5f
