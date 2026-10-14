package dev.voir.areca.foundation.slot

/**
 * How the leading and trailing slots of a [SlotRow] line up with the middle one, for a row whose
 * middle is text that may run to several lines.
 *
 * Each value is a scenario rather than a geometry, and rows pick by what they hold:
 *
 * - [Center] — a row of one or two lines: a switch, a menu entry with a description, a person with
 *   a handle, an account with its balance. Everything centres on the row.
 * - [Top] — a row of three lines or more: a long description, a title over a subtitle over a
 *   caption, a field that grows. The leading visual and the text start at the top and the text runs
 *   down from there; the trailing slot centres on the leading visual, so a checkbox or an amount stays
 *   level with the icon however long the text beside them runs.
 * - [FirstLine] — a marker no taller than a line: a legend dot, a bullet, a small status glyph.
 *   The side slots centre on the first line of the text.
 * - [Auto] — [Center] while the middle is about two lines tall, [Top] beyond that. What every list
 *   and settings row does unless it is told otherwise.
 *
 * The first line is the one the middle slot reports with [firstLineCenter]; a middle that reports
 * none is treated as one block, so [FirstLine] and [Top] centre on it and [Auto] centres everything.
 */
public enum class SlotRowAlignment {
    /** Every slot centred on the row. */
    Center,

    /**
     * The leading visual and the text start at the top; the trailing slot centres on the leading
     * visual, or on the first line when there is no visual. A visual shorter than a line centres on
     * the first line instead of sitting above it.
     */
    Top,

    /** The side slots centre on the first line of the middle slot. */
    FirstLine,

    /** [Center] while the middle slot is at most about two lines tall, [Top] beyond. */
    Auto,
}
