package dev.voir.areca.foundation.field

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp

/**
 * Where a label sits relative to the container it describes.
 *
 * The distinction that matters is [External] against [Internal]: an external label is drawn by
 * [FieldGroup] around the control, while an internal one is drawn by [Field] inside the box and
 * so has to be handed down through a [FieldScope]. Everything else — which side, how wide — is
 * arrangement.
 */
@Immutable
public sealed interface FieldLabelPlacement {
    /**
     * A placement [FieldGroup] can honor on its own, because the label is outside the container.
     *
     * [FieldGroupLayout] accepts only these: it wraps a control it cannot see into, so it has
     * nowhere to put an internal label.
     */
    @Immutable
    public sealed interface External : FieldLabelPlacement

    /**
     * A placement only [Field] can honor, because the label is inside the container.
     *
     * Reaches the container through [FieldScope], which [FieldGroup] builds and the control
     * forwards.
     */
    @Immutable
    public sealed interface Internal : FieldLabelPlacement

    /** No label at all. */
    public data object None : External

    /**
     * Label above the control. The default, and the only sensible choice on narrow screens
     * where a side label would starve the control of width.
     */
    public data object Above : External

    /**
     * Label beside the control, message underneath it. Suits settings-style rows on wide
     * layouts, where a column of labels reads as a table.
     *
     * @property width Fixed label column width, so labels line up down a form. Unspecified lets
     *   each label size to its own text, which is what you want for a single row and not what
     *   you want for a form.
     * @property alignment Vertical alignment of the label against the control.
     */
    public data class Beside(
        public val width: Dp = Dp.Unspecified,
        public val alignment: Alignment.Vertical = Alignment.CenterVertically,
    ) : External

    /**
     * Label stacked above the value *inside* the container, in smaller type.
     *
     * The container grows past [FieldSize.minHeight] to hold both lines, and the leading and
     * trailing slots stay centered against the taller stack. This is the dense form pattern: it
     * costs no vertical space between fields, never truncates the way a notched label does, and
     * needs no animation.
     *
     * A placeholder still works alongside it — the label says what the field is, the placeholder
     * shows what a value looks like.
     */
    public data object Inside : Internal

    /**
     * Label at the start edge *inside* the container, with the value after it on the same line.
     *
     * The settings-row pattern: a column of labels on the left, their values on the right. Give
     * [width] a value so the values line up, and [valueAlignment] `Alignment.End` for the look
     * iOS uses.
     *
     * @property width Fixed label width inside the container. Unspecified sizes the label to its
     *   text, which leaves the values ragged down a list.
     * @property valueAlignment Which edge of the remaining space the value sits against. Only
     *   meaningful for a value that is narrower than the space it is given — a text editor fills
     *   that space and so ignores it.
     */
    public data class InsideLeading(
        public val width: Dp = Dp.Unspecified,
        public val valueAlignment: Alignment.Horizontal = Alignment.Start,
    ) : Internal
}

/**
 * Where the assistive or validation line sits.
 */
public enum class FieldMessagePlacement {
    /** Under the control, in the group's own column. */
    Below,

    /** Nowhere. The group resolves a status but says nothing in writing. */
    None,
}

/**
 * The text of a label, before anything has decided where to draw it.
 *
 * Carried by [FieldScope] rather than a composable slot because an internal label is colored by
 * the container, which is the only thing that knows whether the field is focused, disabled or in
 * error. A label that needs more than text and a marker belongs outside the container, where
 * [FieldGroupLayout] takes a slot.
 *
 * @property text Label content.
 * @property required Whether the value cannot be left out, and the marker should be shown.
 * @property requiredText Marker drawn after [text] when [required] is true.
 */
@Immutable
public data class FieldLabel(
    public val text: CharSequence,
    public val required: Boolean = false,
    public val requiredText: String = "*",
)
