package dev.voir.areca.indication

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.node.DelegatableNode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

/** Tests for [reshapedTo], which keeps a style's indication on the style's own shape. */
class IndicationShapeTest {
    @Test
    fun `every Areca indication takes the new shape and keeps the rest`() {
        val touch = TouchIndication(shape = RectangleShape, color = Color.Red)
        val stroke = StrokeIndication(shape = RectangleShape, strokeColor = Color.Red)
        val fade = FadeIndication(shape = RectangleShape)
        val sweep = LiquidSweepIndication(shape = RectangleShape)

        assertEquals(
            listOf(
                touch.copy(shape = CircleShape),
                stroke.copy(shape = CircleShape),
                fade.copy(shape = CircleShape),
                sweep.copy(shape = CircleShape),
            ),
            listOf(touch, stroke, fade, sweep).map { it.reshapedTo(CircleShape) },
        )
    }

    @Test
    fun `an indication already on the shape is returned as it is`() {
        val touch = TouchIndication(shape = CircleShape)

        assertSame(touch, touch.reshapedTo(CircleShape))
    }

    @Test
    fun `an indication without a shape is returned as it is`() {
        assertSame(ShapelessIndication, ShapelessIndication.reshapedTo(CircleShape))
    }

    /** A third-party indication with no shape to change. */
    private data object ShapelessIndication : IndicationNodeFactory {
        override fun create(interactionSource: InteractionSource): DelegatableNode =
            object : Modifier.Node() {}
    }
}
