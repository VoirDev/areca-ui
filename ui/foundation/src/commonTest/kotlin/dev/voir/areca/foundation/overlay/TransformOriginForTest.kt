package dev.voir.areca.foundation.overlay

import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.test.Test
import kotlin.test.assertEquals

class TransformOriginForTest {
    @Test
    fun `a surface below and at the end grows from its top right corner left to right`() {
        assertEquals(TransformOrigin(1f, 0f), transformOriginFor(AnchoredPlacement.BelowEnd, LayoutDirection.Ltr))
    }

    @Test
    fun `a surface below and at the end grows from its top left corner right to left`() {
        assertEquals(TransformOrigin(0f, 0f), transformOriginFor(AnchoredPlacement.BelowEnd, LayoutDirection.Rtl))
    }

    @Test
    fun `a surface above and at the start grows from its bottom left corner left to right`() {
        assertEquals(TransformOrigin(0f, 1f), transformOriginFor(AnchoredPlacement.AboveStart, LayoutDirection.Ltr))
    }

    @Test
    fun `a centred surface grows from the middle of the edge that touches its anchor`() {
        assertEquals(TransformOrigin(0.5f, 1f), transformOriginFor(AnchoredPlacement.AboveCenter, LayoutDirection.Ltr))
    }

    @Test
    fun `the provider reports the flipped placement when the preferred side does not fit`() {
        var reported: AnchoredPlacement? = null
        AnchoredPositionProvider(
            anchorBounds = IntRect(left = 100, top = 700, right = 200, bottom = 740),
            placement = AnchoredPlacement.BelowEnd,
            offsetX = 0,
            offsetY = 0,
            onPlaced = { placement, _ -> reported = placement },
        ).calculatePosition(
            anchorBounds = IntRect.Zero,
            windowSize = IntSize(width = 400, height = 800),
            layoutDirection = LayoutDirection.Ltr,
            popupContentSize = IntSize(width = 150, height = 100),
        )
        assertEquals(AnchoredPlacement.AboveEnd, reported)
    }
}
