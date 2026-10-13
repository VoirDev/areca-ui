package dev.voir.areca.foundation.overlay

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.test.Test
import kotlin.test.assertEquals

class AnchoredPositionProviderTest {

    private val window = IntSize(width = 400, height = 800)
    private val anchor = IntRect(left = 100, top = 200, right = 200, bottom = 240)
    private val popup = IntSize(width = 150, height = 100)

    private fun position(
        placement: AnchoredPlacement,
        layoutDirection: LayoutDirection = LayoutDirection.Ltr,
        anchorBounds: IntRect = anchor,
        popupSize: IntSize = popup,
        offsetX: Int = 0,
        offsetY: Int = 0,
        flipWhenClipped: Boolean = true,
    ): IntOffset = AnchoredPositionProvider(
        anchorBounds = anchorBounds,
        placement = placement,
        offsetX = offsetX,
        offsetY = offsetY,
        flipWhenClipped = flipWhenClipped,
    ).calculatePosition(
        anchorBounds = IntRect.Zero,
        windowSize = window,
        layoutDirection = layoutDirection,
        popupContentSize = popupSize,
    )

    @Test
    fun `below start hangs under the anchor with left edges aligned left to right`() {
        assertEquals(IntOffset(x = 100, y = 240), position(AnchoredPlacement.BelowStart))
    }

    @Test
    fun `below start aligns right edges right to left`() {
        assertEquals(
            IntOffset(x = 50, y = 240),
            position(AnchoredPlacement.BelowStart, LayoutDirection.Rtl),
        )
    }

    @Test
    fun `below end aligns right edges left to right`() {
        assertEquals(IntOffset(x = 50, y = 240), position(AnchoredPlacement.BelowEnd))
    }

    @Test
    fun `below end aligns left edges right to left`() {
        assertEquals(
            IntOffset(x = 100, y = 240),
            position(AnchoredPlacement.BelowEnd, LayoutDirection.Rtl),
        )
    }

    @Test
    fun `center placement is the same in both directions`() {
        val expected = IntOffset(x = 75, y = 240)

        assertEquals(expected, position(AnchoredPlacement.BelowCenter))
        assertEquals(expected, position(AnchoredPlacement.BelowCenter, LayoutDirection.Rtl))
    }

    @Test
    fun `above sits on top of the anchor`() {
        assertEquals(IntOffset(x = 100, y = 100), position(AnchoredPlacement.AboveStart))
    }

    @Test
    fun `offsets move toward the end edge left to right`() {
        assertEquals(
            IntOffset(x = 110, y = 244),
            position(AnchoredPlacement.BelowStart, offsetX = 10, offsetY = 4),
        )
    }

    @Test
    fun `horizontal offset mirrors right to left`() {
        assertEquals(
            IntOffset(x = 40, y = 240),
            position(AnchoredPlacement.BelowStart, LayoutDirection.Rtl, offsetX = 10),
        )
    }

    @Test
    fun `flips above when there is no room below`() {
        val lowAnchor = IntRect(left = 100, top = 740, right = 200, bottom = 780)

        assertEquals(
            IntOffset(x = 100, y = 640),
            position(AnchoredPlacement.BelowStart, anchorBounds = lowAnchor),
        )
    }

    @Test
    fun `flips below when there is no room above`() {
        val highAnchor = IntRect(left = 100, top = 20, right = 200, bottom = 60)

        assertEquals(
            IntOffset(x = 100, y = 60),
            position(AnchoredPlacement.AboveStart, anchorBounds = highAnchor),
        )
    }

    @Test
    fun `keeps the preferred side and clamps when neither side fits`() {
        val tallPopup = IntSize(width = 150, height = 700)

        assertEquals(
            IntOffset(x = 100, y = 100),
            position(AnchoredPlacement.BelowStart, popupSize = tallPopup),
        )
    }

    @Test
    fun `pinned placement clamps instead of flipping`() {
        val lowAnchor = IntRect(left = 100, top = 740, right = 200, bottom = 780)

        assertEquals(
            IntOffset(x = 100, y = 700),
            position(AnchoredPlacement.BelowStart, anchorBounds = lowAnchor, flipWhenClipped = false),
        )
    }

    @Test
    fun `stays inside the window horizontally`() {
        val edgeAnchor = IntRect(left = 350, top = 200, right = 390, bottom = 240)

        assertEquals(
            IntOffset(x = 250, y = 240),
            position(AnchoredPlacement.BelowStart, anchorBounds = edgeAnchor),
        )
        assertEquals(
            IntOffset(x = 0, y = 240),
            position(
                AnchoredPlacement.BelowStart,
                LayoutDirection.Rtl,
                anchorBounds = anchor.copy(left = 0, right = 40),
            ),
        )
    }
}
