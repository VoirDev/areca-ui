package dev.voir.areca.foundation.wheel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import kotlin.math.abs

/**
 * Where one row sits on the wheel, handed to that row's content.
 *
 * [distanceFromCenter] is continuous rather than a selected flag, which is the point: a row halfway
 * between two positions has a distance of `0.5`, so content can shrink, fade or tip *with* the
 * scroll instead of switching appearance at the moment the selection changes.
 *
 * That also makes it the one value here that changes on every frame of a scroll. Read it inside a
 * deferred lambda — [Modifier.graphicsLayer], a draw scope, [falloff] — and the row re-draws without
 * recomposing. Read it directly in the body of an item and every visible row recomposes sixty times
 * a second for nothing.
 */
@Stable
public interface WheelPickerItemScope {
    /** Index of this row's item in the list the column was given. */
    public val index: Int

    /**
     * Whether this row is the one the wheel would come to rest on.
     *
     * Exactly one visible row reports `true` at any moment, including on an endless wheel short
     * enough to show the same item twice. Changes only as a row crosses the centre, so it is safe to
     * read directly in an item's body.
     */
    public val isSelected: Boolean

    /**
     * Signed rows between this row and the centre. `0f` is centred, `+1f` is one row below, `-1.5f`
     * is halfway between one and two rows above.
     */
    public val distanceFromCenter: Float

    /** Rows from the centre, ignoring direction. */
    public val absDistance: Float get() = abs(distanceFromCenter)

    /**
     * Applies a [WheelPickerFalloff] to a row.
     *
     * Reads [distanceFromCenter] inside a graphics layer, so the row is re-drawn as the wheel turns
     * rather than recomposed.
     *
     * @param falloff How appearance should fall away from the centre.
     * @return Modifier that scales, fades and optionally tips the row.
     */
    public fun Modifier.falloff(falloff: WheelPickerFalloff): Modifier = graphicsLayer {
        val signed = distanceFromCenter.coerceIn(-falloff.maxRows, falloff.maxRows)
        val distance = abs(signed)
        val scale = (1f - distance * falloff.scalePerRow).coerceAtLeast(0f)
        scaleX = scale
        scaleY = scale
        alpha = (1f - distance * falloff.alphaPerRow).coerceIn(0f, 1f)
        if (falloff.rotationPerRow != 0f) {
            cameraDistance = falloff.cameraDistance * this.density
            rotationX = -signed * falloff.rotationPerRow
        }
    }
}

/**
 * [WheelPickerItemScope] backed by a column's live position.
 *
 * @property state Column whose offset this row is measured against.
 * @property row Row position this item is drawn at. Differs from [index] on an endless wheel, where
 *   the same item is drawn at every row congruent to its index.
 * @property index Index of the item in the caller's list.
 */
internal class WheelPickerItemScopeImpl(
    private val state: WheelPickerColumnState,
    private val row: Int,
    override val index: Int,
) : WheelPickerItemScope {
    override val distanceFromCenter: Float get() = row - state.offset

    /**
     * Whether this row is nearest the centre, derived rather than compared against the offset on
     * the spot.
     *
     * `isSelected` is the one value on this scope a row is meant to read while composing, and the
     * offset it would be compared against changes on every frame of a scroll — so a plain
     * comparison invalidates every visible row sixty times a second, re-laying out their text, to
     * answer a question whose answer changes twice per row crossing. Derived, a row recomposes only
     * when its own answer flips.
     */
    private val selectedState = derivedStateOf { nearestRow(state.offset) == row }

    override val isSelected: Boolean by selectedState
}

/**
 * Default rendering of one row.
 *
 * Reads the wheel's style rather than taking colours, so every wheel in a product shrinks and fades
 * the same way. Wrap it, or replace it entirely, when a column needs something other than a line of
 * text.
 *
 * @param text Already-formatted content of the row.
 * @param modifier Modifier applied to the text.
 * @param style Style the colour, weights and falloff come from.
 */
@Composable
public fun WheelPickerItemScope.WheelPickerItem(
    text: String,
    modifier: Modifier = Modifier,
    style: WheelPickerStyle = LocalWheelPickerStyle.current ?: WheelPickerStyle.Default,
) {
    Text(
        text = text,
        style = ArecaTheme.baseTypography.md,
        color = style.content,
        fontWeight = if (isSelected) style.selectedFontWeight else style.fontWeight,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.falloff(style.falloff),
    )
}
