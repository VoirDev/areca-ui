package dev.voir.areca.indication

/**
 * Where a stroke sits relative to the edge it follows.
 *
 * A stroke is centred on its path, so the three entries differ only in how far the path is offset
 * from the element's edge before it is stroked.
 */
public enum class StrokeAlignment {
    /**
     * Entirely inside the element, outer edge flush with the element's edge.
     *
     * The CSS `border` model. Costs a stroke's width of visible padding.
     */
    Inside,

    /**
     * Straddling the element's edge, half in and half out.
     *
     * A compromise that rarely looks deliberate; prefer [Inside] or [Outside].
     */
    Center,

    /**
     * Entirely outside the element, inner edge flush with the element's edge.
     *
     * The CSS `outline` model, and the default: it never intrudes on the element's content. Requires
     * that no ancestor clips the element.
     */
    Outside,
}
