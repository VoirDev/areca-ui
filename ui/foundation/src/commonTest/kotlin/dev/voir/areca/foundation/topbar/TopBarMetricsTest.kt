package dev.voir.areca.foundation.topbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for the metrics every control in a [TopBar] is measured by.
 *
 * The invariant under test is the one the bar exists to enforce: whatever a control holds — a
 * chevron, an icon, a word, a face — what is drawn inside it begins exactly
 * [TopBarSize.actionEdgeInset] from the control's own edge. The bar pulls each control row outward
 * by that one value, so the moment two controls disagree about it, one of them stops meeting the
 * screen gutter.
 */
class TopBarMetricsTest {
    /**
     * A size with the shape of the theme default, built without a composition.
     *
     * @param icon Glyph size.
     * @param action Hit target of a control.
     * @return Size to measure.
     */
    private fun size(icon: Dp = 24.dp, action: Dp = 40.dp): TopBarSize = TopBarSize(
        padding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        gap = 8.dp,
        actionGap = 4.dp,
        titleGap = 2.dp,
        icon = icon,
        action = action,
        containedIcon = 20.dp,
        containedGap = 8.dp,
        containedPadding = 16.dp,
        minHeight = 56.dp,
        largeMinHeight = 104.dp,
        bottomPadding = PaddingValues(bottom = 8.dp),
        borderWidth = 1.dp,
        textStyle = TextStyle.Default,
        subtitleTextStyle = TextStyle.Default,
        actionTextStyle = TextStyle.Default,
        largeTextStyle = TextStyle.Default,
    )

    @Test
    fun `the edge inset is whatever the hit target has left over around the glyph`() {
        assertEquals(8.dp, size(icon = 24.dp, action = 40.dp).actionEdgeInset)
        assertEquals(6.dp, size(icon = 20.dp, action = 32.dp).actionEdgeInset)
    }

    @Test
    fun `a glyph larger than its hit target insets by nothing rather than by a negative`() {
        // A pull of a negative distance would push the control inward, away from the gutter it is
        // supposed to be meeting.
        assertEquals(0.dp, size(icon = 48.dp, action = 40.dp).actionEdgeInset)
    }

    @Test
    fun `a worded control starts its text where an icon control starts its glyph`() {
        val size = size()

        assertEquals(
            size.actionEdgeInset,
            size.actionPadding.calculateStartPadding(LayoutDirection.Ltr),
        )
        assertEquals(
            size.actionEdgeInset,
            size.actionPadding.calculateEndPadding(LayoutDirection.Ltr),
        )
    }

    @Test
    fun `an icon control draws its glyph at the bar's icon size rather than at a button step's`() {
        val size = size()

        assertEquals(size.icon, size.iconButtonSize.icon)
        assertEquals(size.action, size.iconButtonSize.container)
    }

    @Test
    fun `a worded control is exactly as tall as an icon control`() {
        val size = size()

        assertEquals(size.iconButtonSize.container, size.buttonSize.minHeight)
    }

    @Test
    fun `a worded control hugs its label instead of taking a button's minimum width`() {
        assertEquals(0.dp, size().buttonSize.minWidth)
    }

    @Test
    fun `an avatar control insets its face by the same distance as every other control`() {
        val size = size()
        val button = size.avatarButtonSize

        assertEquals(size.actionEdgeInset, (button.container - button.icon) / 2)
    }

    @Test
    fun `an avatar is drawn larger than a glyph so a face stays recognisable`() {
        val size = size()

        assertTrue(size.avatar > size.icon)
    }

    @Test
    fun `every mark in the bar is drawn at one glyph size`() {
        val size = size()

        // The icon set draws chevrons at the standard stroke weight, so the bar needs no per-icon
        // optical correction: one token, every mark.
        assertEquals(size.icon, size.iconButtonSize.icon)
        assertEquals(size.icon, size.buttonSize.icon)
    }

    @Test
    fun `every size preset keeps the inset invariant`() {
        val presets = listOf(
            size(icon = 20.dp, action = 32.dp),
            size(icon = 24.dp, action = 40.dp),
            size(icon = 24.dp, action = 44.dp),
        )

        presets.forEach { size ->
            val inset = size.actionEdgeInset
            val iconButton = size.iconButtonSize
            val avatarButton = size.avatarButtonSize

            assertEquals(inset, (iconButton.container - iconButton.icon) / 2)
            assertEquals(inset, (avatarButton.container - avatarButton.icon) / 2)
            assertEquals(
                inset,
                size.buttonSize.padding.calculateStartPadding(LayoutDirection.Ltr),
            )
        }
    }

    @Test
    fun `a contained control is a disc as wide as the hit target`() {
        val size = size()

        assertEquals(size.action, size.containedIconButtonSize.container)
        assertEquals(size.containedIcon, size.containedIconButtonSize.icon)
    }

    @Test
    fun `a contained worded control is a capsule as tall and at least as wide as a disc`() {
        val size = size()
        val button = size.containedButtonSize

        assertEquals(size.action, button.minHeight)
        assertEquals(size.action, button.minWidth)
        assertEquals(size.containedPadding, button.padding.calculateStartPadding(LayoutDirection.Ltr))
        assertEquals(size.containedIcon, button.icon)
    }

    @Test
    fun `a contained account control fills its disc with the face`() {
        val button = size().containedAvatarButtonSize

        assertEquals(button.container, button.icon)
    }

    @Test
    fun `an appearance is contained when it draws a fill or an outline`() {
        val bare = TopBarActionStyle(
            bg = Color.Transparent,
            content = Color.Black,
            border = Color.Transparent,
            shape = RectangleShape,
            elevation = 0.dp,
            emphasis = TopBarActionEmphasis.Plain,
        )

        assertTrue(!bare.isContained)
        assertTrue(bare.copy(bg = Color.White, shape = CircleShape).isContained)
        assertTrue(bare.copy(border = Color.Black).isContained)
    }
}
