package dev.voir.areca.datetime.calendar.agenda

import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.yearMonth

/** One item of a [CalendarAgenda]'s list. */
internal sealed interface AgendaEntry {
    /** Stable key of the item, so the list keeps its place when days come and go. */
    val key: String

    /** Day the item belongs to, or `null` for a month separator. */
    val date: LocalDate?
}

/**
 * A month separator, before the first day of [month].
 *
 * @property month Month it names.
 */
internal data class AgendaMonth(val month: YearMonth) : AgendaEntry {
    override val key: String get() = "month:$month"
    override val date: LocalDate? get() = null
}

/**
 * A day's heading.
 *
 * @property date The day.
 * @property isEmpty Whether the day is the placeholder for a picked day with no entries.
 */
internal data class AgendaHeading(override val date: LocalDate, val isEmpty: Boolean) : AgendaEntry {
    override val key: String get() = "heading:$date"
}

/**
 * A day's rows, or the placeholder's.
 *
 * @property date The day.
 * @property isEmpty Whether the day is the placeholder for a picked day with no entries.
 */
internal data class AgendaBody(override val date: LocalDate, val isEmpty: Boolean) : AgendaEntry {
    override val key: String get() = "body:$date"
}

/**
 * Lays the agenda out as list items: per day a heading and its rows, in date order, with a month
 * separator before the first day of each month when asked.
 *
 * @param days Days that have entries, ascending and distinct.
 * @param placeholder A picked day with no entries to give an empty group, or `null`. Ignored when
 *   it is already among [days].
 * @param monthSeparators Whether to put a separator before each month's first day.
 * @return The items in order.
 */
internal fun agendaEntries(
    days: List<LocalDate>,
    placeholder: LocalDate?,
    monthSeparators: Boolean,
): List<AgendaEntry> {
    val extra = placeholder?.takeIf { it !in days }
    val all = if (extra == null) days else (days + extra).sorted()
    val entries = ArrayList<AgendaEntry>(all.size * 2 + 4)
    var month: YearMonth? = null
    all.forEach { day ->
        if (monthSeparators && day.yearMonth != month) entries += AgendaMonth(day.yearMonth)
        month = day.yearMonth
        val isEmpty = day == extra
        entries += AgendaHeading(day, isEmpty)
        entries += AgendaBody(day, isEmpty)
    }
    return entries
}

/**
 * Index of the heading of [date]'s group.
 *
 * @param date Day to look for.
 * @return The heading's index, or `-1` when [date] has no group.
 */
internal fun List<AgendaEntry>.headingIndexOf(date: LocalDate): Int =
    indexOfFirst { it is AgendaHeading && it.date == date }

/**
 * Index of the heading a scroll to [date] lands on: [date]'s own group, else the first group after
 * it, else the last group.
 *
 * @param date Day asked for.
 * @return A heading's index, or `-1` when the agenda has no groups.
 */
internal fun List<AgendaEntry>.scrollTargetFor(date: LocalDate): Int {
    val at = indexOfFirst { it is AgendaHeading && it.date >= date }
    return if (at >= 0) at else indexOfLast { it is AgendaHeading }
}

/** Index of the first group's heading, or `-1` when the agenda has no groups. */
internal fun List<AgendaEntry>.firstHeadingIndex(): Int = indexOfFirst { it is AgendaHeading }

/**
 * For each item, the day of the group it belongs to: the day of the last heading at or before it.
 * A month separator belongs to the group above it, whose heading stays pinned over it. Looked up by
 * the list's first visible index, it names the day whose heading is pinned at the top.
 *
 * @return One day per item; `null` for items before the first heading.
 */
internal fun List<AgendaEntry>.groupDays(): Array<LocalDate?> {
    var day: LocalDate? = null
    return Array(size) { index ->
        (this[index] as? AgendaHeading)?.let { day = it.date }
        day
    }
}
