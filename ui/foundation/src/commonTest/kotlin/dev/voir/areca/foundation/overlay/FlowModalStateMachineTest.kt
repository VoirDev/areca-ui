package dev.voir.areca.foundation.overlay

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** Transitions of a flow modal: initial states, drags that interrupt animations and drag targets. */
class FlowModalStateMachineTest {

    @Test
    fun `a transitional initial state is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            FlowModalController(initialState = FlowModalState.Opening)
        }
        assertFailsWith<IllegalArgumentException> {
            FlowModalController(initialState = FlowModalState.Hiding)
        }
    }

    @Test
    fun `a resting initial state is its own target`() {
        val controller = FlowModalController(initialState = FlowModalState.Expanded)

        assertEquals(FlowModalState.Expanded, controller.state)
        assertEquals(FlowModalState.Expanded, controller.targetState)
    }

    @Test
    fun `an opening phase with no resting target still resolves to a height`() {
        val phase = FlowModalPhase(state = FlowModalState.Opening, targetState = FlowModalState.Opening)

        val height = FlowModalStateMachine.heightOf(
            phase = phase,
            partialHeight = 200f,
            expandedHeight = 400f,
            autoHeight = false,
        )

        assertEquals(400f, height)
    }

    @Test
    fun `a drag released during a hide finishes hiding`() {
        val controller = FlowModalController(initialState = FlowModalState.Expanded)
        controller.hide()
        val hiding = controller.phase

        controller.settle(FlowModalState.Hidden)
        controller.finish(controller.phase)

        assertEquals(FlowModalState.Hiding, hiding.state)
        assertEquals(FlowModalState.Hidden, controller.state)
    }

    @Test
    fun `a drag released high during a hide does not reopen the modal`() {
        val controller = FlowModalController(initialState = FlowModalState.Expanded)
        controller.hide()

        controller.settle(FlowModalState.Expanded)

        assertEquals(FlowModalState.Hiding, controller.state)
        assertEquals(FlowModalState.Hidden, controller.targetState)
    }

    @Test
    fun `a drag released on the target of an interrupted opening still completes it`() {
        val controller = FlowModalController()
        controller.show(partial = true)

        controller.settle(FlowModalState.PartiallyOpened)
        controller.finish(controller.phase)

        assertEquals(FlowModalState.PartiallyOpened, controller.state)
        assertEquals(FlowModalState.PartiallyOpened, controller.targetState)
    }

    @Test
    fun `a drag released at rest animates back to the anchor`() {
        val phase = FlowModalStateMachine.resting(FlowModalState.Expanded)

        val next = FlowModalStateMachine.settle(phase, FlowModalState.Expanded)

        assertEquals(FlowModalPhase(FlowModalState.Opening, FlowModalState.Expanded), next)
    }

    @Test
    fun `a hide is possible after a drag interrupted the opening`() {
        val controller = FlowModalController()
        controller.show(partial = true)
        controller.settle(FlowModalState.PartiallyOpened)

        controller.hide()

        assertEquals(FlowModalState.Hiding, controller.state)
    }

    @Test
    fun `a completion that arrives after a newer request is ignored`() {
        val controller = FlowModalController()
        controller.show(partial = true)
        val opening = controller.phase

        controller.hide()
        controller.finish(opening)

        assertEquals(FlowModalState.Hiding, controller.state)
    }

    @Test
    fun `retargeting an opening modal changes its observable target`() {
        val controller = FlowModalController()
        controller.show(partial = true)

        controller.expand()

        assertEquals(FlowModalState.Opening, controller.state)
        assertEquals(FlowModalState.Expanded, controller.targetState)
    }

    @Test
    fun `a sheet dragged below the dismiss threshold is dismissed`() {
        val target = FlowModalStateMachine.dragTarget(
            height = 60f,
            partialHeight = 200f,
            expandedHeight = 400f,
            allowPartial = true,
            allowExpand = true,
        )

        assertEquals(FlowModalState.Hidden, target)
    }

    @Test
    fun `a sheet with both heights settles on the nearest`() {
        fun targetAt(height: Float) = FlowModalStateMachine.dragTarget(
            height = height,
            partialHeight = 200f,
            expandedHeight = 400f,
            allowPartial = true,
            allowExpand = true,
        )

        assertEquals(FlowModalState.PartiallyOpened, targetAt(250f))
        assertEquals(FlowModalState.Expanded, targetAt(350f))
        assertEquals(FlowModalState.PartiallyOpened, targetAt(300f))
    }

    @Test
    fun `a sheet with only an expanded height stays open past half of it`() {
        fun targetAt(height: Float) = FlowModalStateMachine.dragTarget(
            height = height,
            partialHeight = 200f,
            expandedHeight = 400f,
            allowPartial = false,
            allowExpand = true,
        )

        assertEquals(FlowModalState.Expanded, targetAt(201f))
        assertEquals(FlowModalState.Hidden, targetAt(199f))
    }

    @Test
    fun `a sheet with only a partial height stays open past half of it`() {
        fun targetAt(height: Float) = FlowModalStateMachine.dragTarget(
            height = height,
            partialHeight = 200f,
            expandedHeight = 400f,
            allowPartial = true,
            allowExpand = false,
        )

        assertEquals(FlowModalState.PartiallyOpened, targetAt(380f))
        assertEquals(FlowModalState.Hidden, targetAt(90f))
    }
}
