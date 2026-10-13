package dev.voir.areca.datetime.calendar

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

/**
 * Decides whether one day accepts selection.
 *
 * A single predicate rather than the three overlapping parameters a calendar usually grows — an
 * allow-list, a block-list, and a pair of limits. Those three answer only the questions someone
 * anticipated; "weekdays only", "not bank holidays", "at least 48 hours out" are all ordinary
 * requirements that a set of dates cannot express without enumerating years of them.
 *
 * Rules combine with [plus], which requires both to allow, so the common shape reads as one
 * expression:
 *
 * ```kotlin
 * val rule = CalendarDateRule.inRange(opensOn..closesOn) +
 *     CalendarDateRule.blocking(publicHolidays) +
 *     CalendarDateRule { it.dayOfWeek != DayOfWeek.SUNDAY }
 * ```
 *
 * A rule is called for every cell the calendar draws, so keep it cheap — a set lookup or a
 * comparison. Anything that needs real work should be precomputed into a set and handed to
 * [allowing] or [blocking].
 *
 * This governs *selection*, not visibility. A day the rule refuses is still drawn, greyed and
 * announced as disabled, which is what tells the user it exists but is unavailable. To remove days
 * from the calendar entirely, narrow [CalendarBounds] instead.
 */
public fun interface CalendarDateRule {
    /**
     * Whether a day can be chosen.
     *
     * @param date Day the calendar is about to draw.
     * @return `true` when the day accepts a tap.
     */
    public fun isEnabled(date: LocalDate): Boolean

    /** Ready-made rules and factories for the common restrictions. */
    public companion object {
        /** Every day accepts selection. The default. */
        public val All: CalendarDateRule = CalendarDateRule { true }

        /** No day accepts selection. A whole calendar shown for reference only. */
        public val None: CalendarDateRule = CalendarDateRule { false }

        /**
         * Only the listed days accept selection.
         *
         * @param dates Days to allow. An empty set disables everything.
         * @return Rule allowing exactly [dates].
         */
        public fun allowing(dates: Set<LocalDate>): CalendarDateRule =
            CalendarDateRule { it in dates }

        /**
         * Every day except the listed ones accepts selection.
         *
         * @param dates Days to refuse.
         * @return Rule refusing exactly [dates].
         */
        public fun blocking(dates: Set<LocalDate>): CalendarDateRule =
            CalendarDateRule { it !in dates }

        /**
         * Only days inside a closed range accept selection.
         *
         * @param range Allowed days, both ends inclusive.
         * @return Rule allowing [range].
         */
        public fun inRange(range: ClosedRange<LocalDate>): CalendarDateRule =
            CalendarDateRule { it in range }

        /**
         * Only days matching a day of the week accept selection.
         *
         * @param predicate Decides one day of the week. Called per cell, so keep it a lookup.
         * @return Rule delegating to [predicate].
         */
        public fun byDayOfWeek(predicate: (DayOfWeek) -> Boolean): CalendarDateRule =
            CalendarDateRule { predicate(it.dayOfWeek) }
    }
}

/**
 * Combines two rules so a day must satisfy both.
 *
 * Conjunction rather than disjunction because rules are restrictions: each one added should narrow
 * what is selectable, never widen it. A rule that must widen belongs inside a single lambda.
 *
 * @param other Rule to apply as well as the receiver.
 * @return Rule allowing only days both allow.
 */
public operator fun CalendarDateRule.plus(other: CalendarDateRule): CalendarDateRule =
    CalendarDateRule { isEnabled(it) && other.isEnabled(it) }
