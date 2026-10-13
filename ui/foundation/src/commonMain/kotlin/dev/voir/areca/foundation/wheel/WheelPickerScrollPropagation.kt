package dev.voir.areca.foundation.wheel

/**
 * What a wheel does with scrolling it cannot use.
 *
 * A wheel with ends runs out of travel after a few notches. What happens next decides whether the
 * picker behaves like a control or like a hole in the page: by default the leftover goes nowhere, so
 * pointing at a wheel and scrolling moves that wheel and nothing else.
 */
public enum class WheelPickerScrollPropagation {
    /**
     * Keep it. A wheel that has reached its last item simply stops.
     *
     * The default, because the alternative is that scrolling a short wheel silently starts scrolling
     * whatever page the picker is on — which drags every other wheel on screen along with it and
     * reads as the picker having moved them.
     */
    Contain,

    /**
     * Pass it on. A wheel that has reached its last item lets the rest of the gesture scroll an
     * ancestor.
     *
     * For a picker embedded in long scrolling content where a user who overshoots the wheel means to
     * carry on down the page.
     */
    Propagate,
}
