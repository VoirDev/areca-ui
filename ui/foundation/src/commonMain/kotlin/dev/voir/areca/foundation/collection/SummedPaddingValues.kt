package dev.voir.areca.foundation.collection

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * Padding that is [first] and [second] laid end to end on every side.
 *
 * Each side is resolved for the layout direction at measure time, so a start inset in one and a
 * left inset in the other add up on the correct side in either direction.
 *
 * @property first Padding nearer the edge.
 * @property second Padding added inside [first].
 */
@Immutable
private data class SummedPaddingValues(
    val first: PaddingValues,
    val second: PaddingValues,
) : PaddingValues {
    /**
     * Left inset of both paddings together.
     *
     * @param layoutDirection Direction the start and end sides resolve against.
     * @return Sum of both left insets.
     */
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
        first.calculateLeftPadding(layoutDirection) + second.calculateLeftPadding(layoutDirection)

    /**
     * Top inset of both paddings together.
     *
     * @return Sum of both top insets.
     */
    override fun calculateTopPadding(): Dp =
        first.calculateTopPadding() + second.calculateTopPadding()

    /**
     * Right inset of both paddings together.
     *
     * @param layoutDirection Direction the start and end sides resolve against.
     * @return Sum of both right insets.
     */
    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
        first.calculateRightPadding(layoutDirection) + second.calculateRightPadding(layoutDirection)

    /**
     * Bottom inset of both paddings together.
     *
     * @return Sum of both bottom insets.
     */
    override fun calculateBottomPadding(): Dp =
        first.calculateBottomPadding() + second.calculateBottomPadding()
}

/**
 * Adds [other] inside this padding, side by side.
 *
 * @param other Padding to add.
 * @return Padding whose every side is the sum of the two.
 */
internal fun PaddingValues.plusPadding(other: PaddingValues): PaddingValues =
    SummedPaddingValues(first = this, second = other)
