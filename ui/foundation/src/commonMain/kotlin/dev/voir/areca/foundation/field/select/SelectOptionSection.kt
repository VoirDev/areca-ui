package dev.voir.areca.foundation.field.select

import androidx.compose.runtime.Immutable

/**
 * One titled group of options in a sectioned [SelectFlowModal]: "Yours", "Popular", "Similar",
 * "All".
 *
 * The same option may appear in more than one section — a brand that is both one of yours and
 * popular — and is marked as chosen in each. Option keys therefore only have to be unique within a
 * section; the modal keys its rows by section and option together.
 *
 * With [indexed] set, the section's options are broken under letter headers, one per run of options
 * that start with the same letter — the shape of an "All" section. The modal does not sort: pass the
 * options already in the order your language collates them, or a letter will head more than one run.
 *
 * @param Value Type of the values the options carry.
 * @property key Stable identity of the section, unique within the modal. Sections and their options
 *   are told apart by the string form of their keys, so two keys must not print alike.
 * @property title Header drawn above the section, already localized, or `null` for none.
 * @property options Options of the section, in display order. A section with none is not drawn.
 * @property indexed Whether the options are broken under letter headers, taken from the first
 *   character of each label, upper-cased. Labels that start with anything but a letter share
 *   [SelectFlowModalLabels.indexOther].
 */
@Immutable
public data class SelectOptionSection<Value>(
    public val key: Any,
    public val title: String?,
    public val options: List<SelectOption<Value>>,
    public val indexed: Boolean = false,
)

/**
 * One run of an indexed section's options under the letter they share.
 *
 * @param Value Type of the values the options carry.
 * @property letter Header of the run.
 * @property options Options of the run, in the order they were given.
 */
internal data class SelectOptionIndexRun<Value>(
    val letter: String,
    val options: List<SelectOption<Value>>,
)

/**
 * Breaks [options] into consecutive runs that start with the same letter.
 *
 * Consecutive only, so the caller's order is kept: an unsorted list gives a letter more than one run
 * rather than being reordered behind the caller's back.
 *
 * @param Value Type of the values the options carry.
 * @param options Options in display order.
 * @param other Header of labels that do not start with a letter.
 * @return The runs, in order.
 */
internal fun <Value> indexRuns(
    options: List<SelectOption<Value>>,
    other: String,
): List<SelectOptionIndexRun<Value>> {
    val runs = mutableListOf<SelectOptionIndexRun<Value>>()
    var letter: String? = null
    var current = mutableListOf<SelectOption<Value>>()
    for (option in options) {
        val first = option.label.trimStart().firstOrNull()
        val optionLetter = if (first != null && first.isLetter()) first.uppercase() else other
        if (optionLetter != letter) {
            letter?.let { runs += SelectOptionIndexRun(it, current) }
            letter = optionLetter
            current = mutableListOf()
        }
        current += option
    }
    letter?.let { runs += SelectOptionIndexRun(it, current) }
    return runs
}
