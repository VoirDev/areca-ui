# Date and time

Calendars, a date range, a time picker and a combined date-time field for Compose Multiplatform,
drawn with the Areca theme.

| Piece | What it does | Key types |
| --- | --- | --- |
| **Calendar** | A swipeable month grid. Draws the selection it is handed and reports taps | `Calendar`, `CalendarSelection`, `CalendarState`, `CalendarBounds`, `CalendarDateRule` |
| **Range** | The same grid, picking a run of days in two taps under length limits | `DateRangeCalendar`, `DateRangeSelection`, `DateRangeConstraints` |
| **Month picker** | Month and year wheels for landing on any month, and the header that opens them | `MonthYearPicker`, `MonthHeader` |
| **Time** | Hour, minute and AM/PM wheels | `TimePicker`, `TimeFormat` |
| **Field** | One field with a date half and a time half, each opening its own picker | `DateTimeField`, `DateTimeFieldFormat` |
| **Locale** | Week start and hour cycle, and the text every piece draws or reads aloud | `DateTimeLocale`, `CalendarLabels`, `DateRangeLabels`, `TimePickerLabels`, `DateTimeFieldLabels` |

Packages: the time picker, the field and the locale in `dev.voir.areca.datetime`; the calendar, its
parts and the month picker in `dev.voir.areca.datetime.calendar`; the range in
`dev.voir.areca.datetime.calendar.range`.

The module holds no selection and ships no resources. The calendars, the month picker and the field
are controlled — they draw the value they are given and report what the user did — and every word
the module draws or reads aloud comes from a labels object with an English default, built by the
application from its own localization.

---

## Setup

```kotlin
implementation("dev.voir.areca:datetime:1.0.0")
```

Areca is published to GitHub Packages, so the build needs its repository first; see
[Installing](../../README.md#installing).

`datetime` depends on `dev.voir.areca:foundation` as an API dependency, so it comes along: its
signatures carry foundation types (`FieldStyle`, `WheelPickerStyle`, `ButtonStyle`). Everything
draws with the theme, so an `ArecaThemeProvider` must sit above it — usually the one at the
application's root:

```kotlin
ArecaThemeProvider {
    App()
}
```

### kotlinx-datetime is part of the API

The module's parameters and results are `kotlinx-datetime` types — `LocalDate`, `LocalTime`,
`LocalDateTime`, `YearMonth`, `DayOfWeek`, `Month`, `TimeZone` — and "today" comes from a
`kotlin.time.Clock`. So `datetime` depends on `org.jetbrains.kotlinx:kotlinx-datetime` as an API
dependency, and your code uses that library to talk to it. That is why this module is separate from
`foundation`: an application that never shows a calendar does not take the dependency on.

**Supported: kotlinx-datetime 0.8.x, from 0.8.0.** The module is compiled against 0.8.0, the version
in the repository's `gradle/libs.versions.toml`. Gradle resolves one version of a library for the
whole application, the highest any module asks for, so adding `datetime` moves an application on an
older kotlinx-datetime up to 0.8.0.

kotlinx-datetime is not yet 1.0, and its minor releases have changed public API — 0.7.0 moved
`Instant` and `Clock` into the Kotlin standard library. This module's own declarations change only
with Areca's versions, but the kotlinx-datetime types in them carry kotlinx-datetime's guarantees,
not Areca's. A kotlinx-datetime release that changes or removes something this module uses — a
later 0.x, or 1.0 — needs a release of `datetime` compiled against it. Until one ships, keep the
application on 0.8.x: a newer kotlinx-datetime forced into the graph by another dependency can fail
at run time rather than at compile time, since this module's compiled code still refers to 0.8.0's
API.

---

## Quick start

A calendar picking one day:

```kotlin
@Composable
fun DeliveryDay() {
    var day by remember { mutableStateOf<LocalDate?>(null) }

    Calendar(
        selection = CalendarSelection.Single(day),
        onDateClick = { day = it },
    )
}
```

A field holding a date and a time:

```kotlin
@Composable
fun MeetingStart() {
    var start by remember { mutableStateOf<LocalDateTime?>(null) }

    DateTimeField(
        value = start,
        onValueChange = { start = it },
    )
}
```

---

## Calendar

```kotlin
Calendar(
    modifier = Modifier,
    state = rememberCalendarState(),
    selection = CalendarSelection.Empty,
    onDateClick = null,
    dateRule = CalendarDateRule.All,
    startDayOfWeek = DateTimeLocale.Current.firstDayOfWeek,
    labels = CalendarLabels.English,
    showAdjacentMonths = false,
    fixedWeekCount = true,
    style = LocalCalendarStyle.current ?: CalendarStyle.Default,
    size = LocalCalendarSize.current ?: CalendarSize.Default,
    header = { MonthHeader() },
    weekdayHeader = { WeekdayHeader() },
    day = { day -> CalendarDay(day = day) },
)
```

The default `day` draws a `CalendarDay` whose tap calls `onDateClick`.

### Controlled selection

The calendar never changes its selection. It reports the tapped day through `onDateClick` and draws
the `CalendarSelection` it is handed back, so a screen can apply rules the calendar knows nothing
about — a cap on how many days, refusing to clear the last one — without racing internal state.

| `CalendarSelection` | What it holds |
| --- | --- |
| `Single(date)` | One day, or `null` for none. `CalendarSelection.Empty` is `Single()` |
| `Multiple(dates)` | Any set of days |
| `Range(range)` | A `CalendarDateRange`, both ends inclusive, or `null`. For drawing a range picked elsewhere; to pick one, use [`DateRangeCalendar`](#date-range) |

```kotlin
var days by remember { mutableStateOf(emptySet<LocalDate>()) }

Calendar(
    selection = CalendarSelection.Multiple(days),
    onDateClick = { date ->
        days = when {
            date in days -> days - date
            days.size < 5 -> days + date
            else -> days
        }
    },
)
```

**Read-only.** Pass `null` for `onDateClick` — the default. The grid still pages and still
announces its days; nothing is tappable or focusable.

### State, and a testable "today"

```kotlin
rememberCalendarState(
    initialMonth = null,
    bounds = CalendarBounds.Default,
    clock = Clock.System,
    timeZone = TimeZone.currentSystemDefault(),
)
```

A `CalendarState` holds which month is showing, the `bounds` the calendar can move in, and the day
drawn as `today`. Hoist it to drive the calendar from outside: `animateTo(month)` and
`scrollTo(month)` (both clamped into the bounds), `animateToNextMonth()`,
`animateToPreviousMonth()` and `animateToToday()` suspend, like `PagerState`'s, so launch them from
`rememberCoroutineScope()`. Jump rather than animate when the move is not the user's doing —
following a date typed into a field — since animating across years is a long blur:

```kotlin
val state = rememberCalendarState()

LaunchedEffect(typedDate) {
    typedDate?.let { state.scrollTo(it.yearMonth) }
}
Calendar(state = state, selection = CalendarSelection.Single(typedDate), onDateClick = onPick)
```

- **`visibleMonth`** is the month the header names; it flips as a swipe crosses halfway.
  **`settledMonth`** changes only once the pager comes to rest — read it for decisions a reversed
  swipe should not trigger, such as loading a month's events.
- **`canScrollBackward`** and **`canScrollForward`** say whether an earlier or later month exists,
  for controls of your own.
- `expandMonthPicker()`, `collapseMonthPicker()` and `toggleMonthPicker()` swap the grid for the
  month picker; `isMonthPickerExpanded` says which is showing.

The state is saved across configuration changes and process death. Only the visible month is
saved; the bounds and today come from the call site again. Changing `bounds` builds a new state,
since the page count is fixed at construction; changing `initialMonth` does not — it is only the
month the calendar opens on.

**Today is read once, from the clock you pass.** The state reads `clock` in `timeZone` once when it
is created, so every cell agrees on today and a test or screenshot can pin it without touching the
machine's clock:

```kotlin
val fixedClock = object : Clock {
    override fun now(): Instant = Instant.parse("2026-09-15T09:00:00Z")
}

Calendar(
    state = rememberCalendarState(clock = fixedClock, timeZone = TimeZone.UTC),
    selection = CalendarSelection.Single(LocalDate(2026, 9, 18)),
)
```

`Instant` and `Clock` here are `kotlin.time`'s. Today drives the ring on today's cell, the month the
calendar opens on, where Tab lands, and `TodayButton`.

### Bounds, date rules and range constraints

Three things limit which days can be chosen, and they are not interchangeable:

| | Decides | A day it refuses |
| --- | --- | --- |
| **`CalendarBounds`** (on the state) | Which months exist at all | Is never paged to. The edge days of a partial first or last month are drawn unavailable |
| **`CalendarDateRule`** (`dateRule`) | Which of the drawn days accept a tap | Is drawn greyed, announced as unavailable, and refuses the tap |
| **`DateRangeConstraints`** (range only) | How long a range may be, and whether it may cross a refused day | Is greyed once the first end is picked — see [Date range](#date-range) |

**Bounds** are inclusive days, `CalendarBounds(minDate, maxDate)`. Paging works in whole months, so
a `maxDate` of the 15th still shows its whole month, with the 16th onward drawn unavailable. A
booking window of ninety days belongs here rather than in a rule — as a rule, the user could still
swipe through decades of greyed months. `CalendarBounds.Default` runs from 1 January 1900 to
31 December 2100; `CalendarBounds.around(today, years)` gives whole years either side of a day, and
`CalendarBounds.of(minDate, maxDate)` takes nullable ends, falling back to `Default`'s.

```kotlin
val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
val state = rememberCalendarState(
    bounds = CalendarBounds(minDate = today, maxDate = today.plus(90, DateTimeUnit.DAY)),
)
```

**Rules** are a single predicate, `CalendarDateRule { date -> … }`, with ready-made ones —
`All` (the default), `None`, `allowing(dates)`, `blocking(dates)`, `inRange(range)`,
`byDayOfWeek(predicate)` — combined with `+`, which requires both to allow:

```kotlin
val weekend = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
val workingDays = CalendarDateRule.byDayOfWeek { it !in weekend } +
    CalendarDateRule.blocking(publicHolidays)
```

A rule is called for every drawn cell, so keep it to a set lookup or a comparison, and precompute
anything heavier into a set for `allowing` or `blocking`. The bounds always apply on top of the
rule.

### Layout and slots

- **`startDayOfWeek`** is the first column, from the locale by default (see
  [Localization](#localization)).
- **`showAdjacentMonths`** fills the cells before the 1st and after the last with the neighbouring
  months' dates instead of leaving them blank. They are drawn muted, and stay out of the tab order.
- **`fixedWeekCount`** lays every month over six rows, so paging between a five-row and a six-row
  month does not resize the calendar. On by default.
- **`header`** is the band above the grid, `MonthHeader()` by default; **`weekdayHeader`** is the
  Mo/Tu/We row, `WeekdayHeader()`. `null` removes either.
- **`day`** draws one cell from a `CalendarDayState` — its `date`, the page's `month`,
  `isAdjacent`, `isToday`, `isSelected`, `isEnabled`.

The slots run in a `CalendarScope`, which carries the `state`, `labels` and `startDayOfWeek`, and
fire-and-forget navigation for an `onClick`: `nextMonth()`, `previousMonth()`, `goTo(month)`,
`jumpTo(month)`, `goToToday()`, `toggleMonthPicker()`, `collapseMonthPicker()`, with `canGoBack`,
`canGoForward`, `visibleMonth` and `isMonthPickerExpanded` to read. `TodayButton()` is a scope
control that returns to today, disabled while today's month is showing or out of bounds:

```kotlin
Calendar(
    state = state,
    selection = selection,
    onDateClick = onPick,
    header = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MonthHeader(modifier = Modifier.weight(1f))
            TodayButton()
        }
    },
)
```

A custom `day` slot should draw through `CalendarDay` (or `DateRangeDay`): those cells carry the
grid position and keyboard behaviour described [below](#keyboard-and-screen-readers), and a cell
drawn from scratch loses both. Decorate around one instead:

```kotlin
Calendar(
    selection = selection,
    onDateClick = onPick,
    day = { day ->
        Box(contentAlignment = Alignment.BottomCenter) {
            CalendarDay(day = day, onClick = { onPick(day.date) })
            if (day.date in eventDays && !day.isAdjacent) {
                Box(
                    Modifier
                        .padding(bottom = 4.dp)
                        .size(4.dp)
                        .background(ArecaTheme.colors.accentBase, CircleShape),
                )
            }
        }
    },
)
```

`CalendarDay` ignores `onClick` on an unavailable day, so the slot does not have to check.

---

## Date range

```kotlin
var stay by remember { mutableStateOf<DateRangeSelection>(DateRangeSelection.Empty) }

DateRangeCalendar(
    selection = stay,
    onSelectionChange = { stay = it },
    constraints = DateRangeConstraints(
        dateRule = CalendarDateRule.blocking(bookedNights),
        minDays = 3,
        maxDays = 15,
    ),
    showAdjacentMonths = true,
)
stay.range?.let { range -> Text(DateRangeLabels.English.length(range)) }
```

`DateRangeCalendar` is `Calendar` with a selection that has a shape. Paging, the month picker,
bounds, slots and keyboard behave identically; it takes the same `state`, `startDayOfWeek`,
`labels`, `showAdjacentMonths`, `fixedWeekCount`, `style`, `size`, `header` and `weekdayHeader`, and
adds `constraints`, `onRangeComplete`, `rangeLabels` and `rangeStyle`.

**`DateRangeSelection`** says how far through picking the user is: `Empty`, `Started(anchor)` with
one end picked, or `Complete(range)`. Its `range` is the `CalendarDateRange` once complete, and
`null` before. Like `Calendar`, the range calendar is controlled: it calls `onSelectionChange` with
the selection a tap produces — never for a tap that changes nothing — and draws what it is handed
back. Pass `null` for a read-only range: a stay already booked, a reporting period.

**The two taps** are the pure function `DateRangeSelection.select(date, constraints, bounds)`, which
a test can drive without a composition:

- From `Empty` or `Complete`, a tap starts a new range there. A finished range is never edited from
  its nearer end.
- From `Started`, a later day the constraints allow completes the range; an earlier day becomes the
  new anchor rather than filling backwards; a later day the constraints refuse does nothing.
- A day outside the bounds, or one the rule refuses, never changes the selection.

`onRangeComplete` is called after `onSelectionChange` when a tap finishes a range — for a screen
that closes a popup or runs a search next. Under a mouse, hovering paints the range the next tap
would produce; touch shows no preview.

**`DateRangeConstraints`** are what only a range has: a length.

- **`minDays`** and **`maxDays`** count days, both ends included, as `CalendarDateRange.dayCount`
  does: a two-night minimum is `minDays = 3`. `minDays = 1` (the default) allows a single-day range;
  `nights` is `dayCount - 1`. `maxDays = null` means no limit.
- **`dateRule`** is the per-day rule. Pass it here, not to the calendar, so the limits and the rule
  travel together.
- **`blockedDays`** decides whether a range may cross a day the rule refuses.
  `BlockedDayPolicy.StopAtBlocked` (the default) stops a range at the first refused day after its
  anchor — a room taken on Wednesday makes Tuesday-to-Thursday impossible.
  `BlockedDayPolicy.AllowGaps` lets a range span refused days without starting or ending on one — a
  holiday inside a fortnight's leave. The spanned days still count toward the length.

Once an anchor is picked, every day the range could no longer reach is drawn unavailable — past
`maxDays`, short of `minDays`, beyond a blocked day — so the limits are visible before the user
runs into them. Days at or before the anchor stay available, since tapping one starts again.
`constraints.endWindow(anchor, bounds)` returns the window the far end may fall in, for a screen
that wants to say so in words. Under `StopAtBlocked` finding that window walks forward from the
anchor day by day, up to `maxDays` or the end of the bounds, so a calendar that blocks days should
state a `maxDays` or narrow its bounds.

`DateRangeLabels.length` writes a range's extent (`3 nights`, `1 day`). The calendar does not draw
it; it is there for a footer, a field or an announcement of your own, as above.

---

## Month and year picker

**`MonthHeader`** is the default header: the month as a button on the start side, and back and
forward chevrons on the end side. The chevrons disable at the ends of the bounds. Tapping the month
swaps the grid for a `MonthYearPicker` of exactly the grid's height, so opening it does not move
what sits below; the chevrons and weekday row fade out while it shows.
`MonthHeader(showControls = false)` drops the chevrons, and `showMonthPicker = false` leaves the
month as plain text — for a calendar whose range is a few months.

**`MonthYearPicker`** works on its own too. It is controlled: it reports the month the wheels come
to rest on, and follows changes to `month` from outside while the wheels are idle. The month wheel
holds only the months `bounds` allows in the year the year wheel rests on.

```kotlin
var month by remember { mutableStateOf(YearMonth(2026, 9)) }

MonthYearPicker(
    month = month,
    onMonthChange = { month = it },
    bounds = CalendarBounds.around(today, years = 5),
)
```

---

## Time picker

```kotlin
var alarm by remember { mutableStateOf(LocalTime(7, 30)) }

TimePicker(
    initialTime = alarm,
    onTimeChange = { alarm = it },
    format = TimeFormat.Hours12,
)
```

Unlike the calendars, the time picker owns its wheels. `initialTime` seeds them once; a later
change to it does not move them — key the picker on something (`key(resetCount) { TimePicker(…) }`)
to start it again. `onTimeChange` fires when the wheels come to rest after the user turns them, and
for nothing else: not on first composition, and not for the values a wheel spins past.

- **`format`** is `TimeFormat.Hours24` (two wheels, `00`–`23`) or `TimeFormat.Hours12` (three,
  `1`–`12` and AM/PM), from the locale by default. Changing it while the picker shows keeps the
  time.
- The hour and minute wheels wrap, so 23 runs into 00; the period wheel does not.
- The result is minute precision: seconds and nanoseconds of `initialTime` are dropped.
- `itemContent` draws one row from its already-formatted text; `centerContent`, `columnWidth` and
  `columnSpacing` lay the columns out.

Its `style`, `size` and `scroll` are foundation's `WheelPickerStyle`, `WheelPickerSize` and
`WheelPickerScroll`, from foundation's wheel locals.

---

## Date-time field

```kotlin
DateTimeField(
    value = start,
    onValueChange = { start = it },
    format = DateTimeFieldFormat(
        date = { date -> date.format(DayMonthYear) },
        time = { time -> time.format(LocalTime.Formats.ISO) },
    ),
    defaultTime = LocalTime(10, 0),
    dateRule = CalendarDateRule.byDayOfWeek { it != DayOfWeek.SUNDAY },
)

val DayMonthYear = LocalDate.Format {
    day()
    char(' ')
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    year()
}
```

One foundation `FieldRow` with two halves: the date half opens a `Calendar` in a dropdown and the
time half a `TimePicker`. They share one border, one focus and one `status`, and take a `field` from
an enclosing foundation `FieldGroup` like any other field. The pickers are exclusive — opening one
closes the other. Picking a date closes the calendar; the time picker stays open while the wheels
turn, reporting each time they settle. Each time the calendar opens it shows the grid (not the
month picker) on the month of the value the field holds.

**The value is nullable, the callback is not.** `value` is `null` while nothing is chosen.
`onValueChange` always receives a whole `LocalDateTime`: picking a date on an empty field reports
that date at `defaultTime` (09:00 unless given).

**A time picked before any date is a draft.** There is no date to pair it with, and inventing one
could produce a day the rule or the bounds refuse, so the field does not report it. It holds the
time instead: the time half shows it, and the first date picked takes it in place of `defaultTime`.
The draft is dropped whenever `value` changes, so a form that resets the field to `null` resets the
draft too.

- **`format`** turns the value into the two strings shown. The default is
  `DateTimeFieldFormat.numeric(timeFormat, timeLabels)`: ISO-ordered digits (`2026-09-13`) with a
  time counted the way the wheels count (`14:30` or `2:30 PM`). It is a placeholder for the
  application's own formatter — kotlinx-datetime's formats know no locale, as the English month
  names above show, so a translated application passes its own month names
  (`MonthNames(listOf(…))`) or its own formatting.
- **`timeFormat`**, **`startDayOfWeek`** follow the locale by default; **`timeLabels`**,
  **`calendarLabels`** and **`labels`** carry the text.
- **`calendarState`** is the calendar's state, opening on the value's month; pass your own for
  bounds or a fixed clock. **`dateRule`** limits the days.
- **`datePlaceholder`** and **`timePlaceholder`** default to `labels.datePlaceholder`
  (`Pick a date`) and `labels.timePlaceholder` (`--:--`).
- **`timeWidth`** fixes the time half's width so a column of fields lines up; leave room for the
  clock glyph and its padding. **`leading`** is the calendar glyph before the date; `null` removes
  it.
- **`enabled`** `false` disables both halves; **`readOnly`** shows the value and opens nothing.
- **`placement`** and **`dropdownStyle`** position and color the dropdowns.

The calendar in the dropdown is `CalendarSize.Compact` unless a `LocalCalendarSize` is provided.
The field's own `style` and `size` are foundation's `FieldStyle` and `FieldSize`, from foundation's
field locals like every other field.

---

## Localization

Two kinds of thing vary by locale, and the module takes them separately.

**Conventions — `DateTimeLocale`.** The day a week starts on (`firstDayOfWeek`) and whether clocks
count to twelve or twenty-four (`timeFormat`). `kotlinx-datetime` has no locale API, so
**`DateTimeLocale.Current`** reads them from the platform: `java.util.Calendar` and
`DateFormat.is24HourFormat` on Android, `NSCalendar` and `NSDateFormatter` on iOS,
`java.util.Calendar` and `java.time`'s short time pattern on the JVM. On Android and iOS the hour
cycle includes the user's own 24-hour setting, and on Android a locale change while the app runs is
followed.

`Calendar` and `DateRangeCalendar` take their `startDayOfWeek`, `TimePicker` its `format`, and
`DateTimeField` its `timeFormat` and `startDayOfWeek`, from `DateTimeLocale.Current`. Pass the
parameter to override one component, or provide **`LocalDateTimeLocale`** to pin a subtree — a
screenshot test, an app with its own language setting. **`DateTimeLocale.Iso`** is Monday weeks and
a 24-hour clock:

```kotlin
CompositionLocalProvider(LocalDateTimeLocale provides DateTimeLocale.Iso) {
    BookingScreen()
}
```

**Words and digits — the labels objects.** Each has an English default and is built by the
application from its own localization:

| Labels | Used by | Holds |
| --- | --- | --- |
| `CalendarLabels` | `Calendar`, `DateRangeCalendar`, `MonthYearPicker`, `DateTimeField` (`calendarLabels`) | Weekday headings and their spoken names, the month heading, the picker's month names, the spoken day, the control names, and the digits of day numbers and years |
| `DateRangeLabels` | `DateRangeCalendar` (`rangeLabels`) | What a day's place in the range adds to its spoken description, and a range's length |
| `TimePickerLabels` | `TimePicker`, `DateTimeField` (`timeLabels`) | The wheels' spoken names, the AM and PM rows, and how a twelve-hour hour is read with its period |
| `DateTimeFieldLabels` | `DateTimeField` (`labels`) | The halves' spoken names and states, and the placeholders |

`CalendarLabels.English` draws `Mo` over the columns, `September 2026` in the header and reads a day
as `Tuesday 15 September 2026`; `CalendarLabels.EnglishNarrow` draws single-letter weekdays. Labels
are functions where the text is computed, so the order and form follow the language:

```kotlin
val GermanMonths = listOf(
    "Januar", "Februar", "März", "April", "Mai", "Juni",
    "Juli", "August", "September", "Oktober", "November", "Dezember",
)
val GermanWeekdays = listOf(
    "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag",
)

val GermanCalendarLabels = CalendarLabels(
    weekday = { day -> GermanWeekdays[day.ordinal].take(2) },
    monthHeader = { month -> "${GermanMonths[month.month.ordinal]} ${month.year}" },
    pickerMonth = { month -> GermanMonths[month.ordinal] },
    dayDescription = { date ->
        val weekday = GermanWeekdays[date.dayOfWeek.ordinal]
        "$weekday, ${date.day}. ${GermanMonths[date.month.ordinal]} ${date.year}"
    },
    monthPickerAction = "Monat und Jahr wählen",
    monthPickerMonth = "Monat",
    monthPickerYear = "Jahr",
    previousMonthAction = "Vorheriger Monat",
    nextMonthAction = "Nächster Monat",
    todayAction = "Heute",
    monthPickerExpanded = "Ausgeklappt",
    monthPickerCollapsed = "Eingeklappt",
    weekdayName = { day -> GermanWeekdays[day.ordinal] },
)
```

Keep label sets as top-level values: labels that compare unequal on every recomposition — a new
lambda each time — rebuild what depends on them.

Some spoken text is still assembled in English order: the month control, the field's halves and a
range day join their parts with `", "`, and the time picker's hour and minute wheels draw and read
Western digits (`itemContent` can draw other numerals; what is read stays Western).

---

## Keyboard and screen readers

**The grid is one tab stop.** Tab lands on the day the keyboard last rested on in the visible month,
else the first chosen day, else today, else the first day of the month. From there:

| Key | Moves to |
| --- | --- |
| Left / Right | The previous / next day — swapped in a right-to-left layout, so the arrows follow the screen |
| Up / Down | The same weekday a week earlier / later |
| Home / End | The first / last day of the week, as the columns order it |
| Page Up / Page Down | The same day a month earlier / later, or that month's last day when it is shorter |
| Shift + Page Up / Page Down | The same day a year earlier / later |

A move that leaves the month pages the calendar — animated to a neighbouring month, a jump for
anything further — and focus follows once the new page is composed. Moves stop at the bounds.
Unavailable days of the visible month take focus so the arrows can cross them, but refuse Enter;
spill-over days from neighbouring months stay out of the tab order. A read-only calendar has no
focusable days.

**To a screen reader** each page is a table of weeks and weekdays, and each day one selectable cell
reading `CalendarLabels.dayDescription` — weekday included, since a screen reader visiting a cell
does not read the heading above it — with its row and column. Unavailable days are announced as
such. In a range, `DateRangeLabels.position` appends `start of range`, `in range` or `end of range`,
and only committed days are announced as selected, never the hover preview. The weekday headings
read full day names (`CalendarLabels.weekdayName`), not the drawn abbreviations.

Each month the calendar comes to rest on is announced by its heading, except while the month
picker is open. The month control reads the month and `monthPickerAction` with an expanded or
collapsed state; the chevrons read `previousMonthAction` and `nextMonthAction`. The month wheel
reads month names and the year wheel its years. The time picker's wheels read `hour`, `minute` and
`period`, and a twelve-hour hour is always read with its period (`2 PM`). Each half of the date-time
field is a button reading its name and value (`Date, 2026-09-13`) with an expanded or collapsed
state.

---

## Styles and sizes

| Component | Style · local | Size · local |
| --- | --- | --- |
| `Calendar`, `MonthHeader`, `WeekdayHeader`, `CalendarDay` | `CalendarStyle` · `LocalCalendarStyle` | `CalendarSize` · `LocalCalendarSize` |
| `DateRangeCalendar`, `DateRangeDay` | `DateRangeStyle` · `LocalDateRangeStyle`, with `CalendarStyle` | `CalendarSize` · `LocalCalendarSize` |
| `MonthYearPicker` | foundation's `WheelPickerStyle` · `LocalWheelPickerStyle` | `WheelPickerSize`, from `LocalCalendarSize`'s `monthPicker` |
| `TimePicker` | foundation's `WheelPickerStyle` · `LocalWheelPickerStyle` | `WheelPickerSize` · `LocalWheelPickerSize` |
| `DateTimeField` | foundation's `FieldStyle` · `LocalFieldStyle` | `FieldSize` · `LocalFieldSize` |

**`CalendarStyle.default(…)`** builds the calendar's colors from the theme with every color a flat
parameter — the day, chosen, today (an accent ring as well as a color), unavailable and adjacent
fills and numbers, the header labels, and the styles of the month controls and picker.
`CalendarStyle.Default` is `default()`. **`DateRangeStyle.default(…)`** does the same for the band
between the ends, the end caps, their preview and unavailable variants, and the cap shape.

**`CalendarSize.Default`** is the full calendar; **`CalendarSize.Compact`** tightens the insets and
gaps for a calendar inside something else — a dropdown, a sidebar — and keeps the type.
`CalendarSize.default(…)` overrides single values.

The calendar's locals — `LocalCalendarStyle`, `LocalCalendarSize`, `LocalDateRangeStyle` — are
`null` unless provided, and each component then falls back to the type's `Default`;
`LocalDateTimeLocale` falls back to the platform's conventions. `ArecaThemeProvider` does not seed
them: it lives in `foundation`, which does not depend on
`datetime`. Provide them yourself around the screens that want other defaults:

```kotlin
CompositionLocalProvider(
    LocalCalendarStyle provides CalendarStyle.default(selectedBg = ArecaTheme.colors.bgSuccessBase),
    LocalCalendarSize provides CalendarSize.Compact,
) {
    BookingScreen()
}
```

The wheel and field locals are foundation's, and `ArecaThemeProvider` does seed those.

---

## Where to look

Under **Date & time** in the playground: **Calendar**, **Date range calendar**, **Time picker** and
**Date & time field**, each with recipes and edge cases — bounded paging, a week starting on Sunday,
keyboard navigation, combined rules, read-only calendars, a minimum stay, blocked days a range stops
at or steps over, and a time chosen before a date. `flow/profile`'s `BirthdayRow`
([flow/profile/README.md](../../flow/profile/README.md)) opens a `Calendar` in a sheet, bounded to
today, for a date of birth.
