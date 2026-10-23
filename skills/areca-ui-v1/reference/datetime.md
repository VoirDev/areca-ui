# Date and time — `dev.voir.areca:datetime`

Add it when a screen picks a day, several days, a range, a month or a time, holds a date-time in a
form, shades a month by activity (heat map), picks a month from tiles, or leads a row with a date
tile. For scroll wheels that are not dates use foundation's `WheelPicker`.

- Artifact `dev.voir.areca:datetime`. Depends on `foundation` as `api`.
- Brings `org.jetbrains.kotlinx:kotlinx-datetime` **0.8.x** as `api`: values are `LocalDate`,
  `LocalTime`, `LocalDateTime`, `YearMonth`, `DayOfWeek`, `Month`, `TimeZone`; "today" comes from a
  `kotlin.time.Clock`. Keep the app on kotlinx-datetime 0.8.x while it uses this module; a newer
  version forced in by another dependency can fail at run time.
- Needs the app's `ArecaThemeProvider` above it.

| Package | Holds |
| --- | --- |
| `dev.voir.areca.datetime` | `TimePicker`, `DateTimeField`, `DateTile`, `DateTimeLocale`, `TimeFormat`, `TimePickerLabels`, `DateTimeFieldLabels`, `DateTimeFieldFormat` |
| `dev.voir.areca.datetime.calendar` | `Calendar` and its parts (`CalendarDay`, `MonthHeader`, `WeekdayHeader`, `TodayButton`), `CalendarHeatDay`, `CalendarHeatLegend`, `calendarHeatLevel`, `MonthYearPicker`, `MonthGrid`, `CalendarState`, `CalendarSelection`, `CalendarBounds`, `CalendarDateRule`, `CalendarDateRange`, `CalendarLabels` |
| `dev.voir.areca.datetime.calendar.range` | `DateRangeCalendar`, `DateRangeDay`, `DateRangeSelection`, `DateRangeConstraints`, `BlockedDayPolicy`, `DateRangeLabels`, `DateRangeStyle` |

## Choose

| Need | Use | Types |
| --- | --- | --- |
| Pick one day, several days, or show a range picked elsewhere | `Calendar(selection, onDateClick)` | `CalendarSelection.Single` / `Multiple` / `Range`, `CalendarState`, `CalendarDateRule`, `CalendarBounds` |
| Pick a run of days in two taps, with length limits | `DateRangeCalendar(selection, onSelectionChange)` | `DateRangeSelection`, `DateRangeConstraints`, `BlockedDayPolicy`, `CalendarDateRange` |
| One date-time in a form, inside a `FieldGroup` | `DateTimeField(value, onValueChange)` | `DateTimeFieldFormat`, `DateTimeFieldLabels` |
| Hour and minute (and AM/PM) wheels | `TimePicker(initialTime, onTimeChange)` | `TimeFormat`, `TimePickerLabels` |
| Month and year wheels on their own | `MonthYearPicker(month, onMonthChange)` | `CalendarBounds` |
| A month shaded by how much each day held | `Calendar` whose `day` slot draws `CalendarHeatDay`, + `CalendarHeatLegend`, levels from `calendarHeatLevel` | `CalendarHeatStyle` |
| Pick a whole month from tiles, a year to a block, with a figure each | `MonthGrid(years, selected, onSelect)` | `CalendarLabels` |
| A date as a month-over-day tile leading a row | `DateTile(date)` | `CalendarLabels` |
| A day cell in a custom `day` slot | `CalendarDay` (single) / `DateRangeDay` (range) | `CalendarDayState`, `DateRangeDayState` |
| The calendar's header band; the weekday row; a "Today" control | `MonthHeader`, `WeekdayHeader`, `TodayButton` (all `CalendarScope` slot parts) | `CalendarScope` |

## Signatures

```kotlin
@Composable fun Calendar(
    modifier: Modifier = Modifier,
    state: CalendarState = rememberCalendarState(),
    selection: CalendarSelection = CalendarSelection.Empty,
    onDateClick: ((LocalDate) -> Unit)? = null,           // null = read-only
    dateRule: CalendarDateRule = CalendarDateRule.All,
    startDayOfWeek: DayOfWeek = DateTimeLocale.Current.firstDayOfWeek,
    labels: CalendarLabels = CalendarLabels.English,
    showAdjacentMonths: Boolean = false,
    fixedWeekCount: Boolean = true,                      // six rows always
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
    header: (@Composable CalendarScope.() -> Unit)? = { MonthHeader() },
    weekdayHeader: (@Composable CalendarScope.() -> Unit)? = { WeekdayHeader() },
    day: @Composable CalendarScope.(CalendarDayState) -> Unit = { CalendarDay(it, onClick = …) },
)

@Composable fun rememberCalendarState(
    initialMonth: YearMonth? = null,
    bounds: CalendarBounds = CalendarBounds.Default,
    clock: Clock = Clock.System,                         // kotlin.time.Clock
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): CalendarState

@Composable fun DateRangeCalendar(
    selection: DateRangeSelection,
    onSelectionChange: ((DateRangeSelection) -> Unit)?,  // required; null = read-only
    modifier: Modifier = Modifier,
    state: CalendarState = rememberCalendarState(),
    constraints: DateRangeConstraints = DateRangeConstraints(),
    onRangeComplete: ((CalendarDateRange) -> Unit)? = null,
    startDayOfWeek: DayOfWeek = DateTimeLocale.Current.firstDayOfWeek,
    labels: CalendarLabels = CalendarLabels.English,
    rangeLabels: DateRangeLabels = DateRangeLabels.English,
    showAdjacentMonths: Boolean = false,
    fixedWeekCount: Boolean = true,
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    rangeStyle: DateRangeStyle = LocalDateRangeStyle.current ?: DateRangeStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
    header: (@Composable CalendarScope.() -> Unit)? = { MonthHeader() },
    weekdayHeader: (@Composable CalendarScope.() -> Unit)? = { WeekdayHeader() },
    day: @Composable CalendarScope.(DateRangeDayState) -> Unit = { DateRangeDay(it, …) },
)

@Composable fun DateTimeField(
    value: LocalDateTime?,
    onValueChange: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
    style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    timeFormat: TimeFormat = DateTimeLocale.Current.timeFormat,
    timeLabels: TimePickerLabels = TimePickerLabels.English,
    format: DateTimeFieldFormat = DateTimeFieldFormat.numeric(timeFormat, timeLabels),
    labels: DateTimeFieldLabels = DateTimeFieldLabels.English,
    defaultTime: LocalTime = LocalTime(hour = 9, minute = 0),
    datePlaceholder: String = labels.datePlaceholder,
    timePlaceholder: String = labels.timePlaceholder,
    timeWidth: Dp = Dp.Unspecified,                      // fix it to line up a column of fields
    calendarState: CalendarState = rememberCalendarState(initialMonth = value?.date?.yearMonth),
    dateRule: CalendarDateRule = CalendarDateRule.All,
    startDayOfWeek: DayOfWeek = DateTimeLocale.Current.firstDayOfWeek,
    calendarLabels: CalendarLabels = CalendarLabels.English,
    placement: AnchoredPlacement = AnchoredPlacement.BelowStart,
    dropdownStyle: DropdownPopupStyle = DropdownPopupStyle.Default,
    leading: (@Composable RowScope.() -> Unit)? = { /* calendar glyph */ },
)

@Composable fun TimePicker(
    initialTime: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
    format: TimeFormat = DateTimeLocale.Current.timeFormat,   // Hours24 | Hours12
    style: WheelPickerStyle = LocalWheelPickerStyle.current ?: WheelPickerStyle.Default,
    size: WheelPickerSize = LocalWheelPickerSize.current ?: WheelPickerSize.Default,
    scroll: WheelPickerScroll? = LocalWheelPickerScroll.current,
    labels: TimePickerLabels = TimePickerLabels.English,
    centerContent: Boolean = true,
    columnWidth: Dp = /* library default */,
    columnSpacing: Dp = ArecaTheme.spacing.sm,
    itemContent: @Composable WheelPickerItemScope.(text: String) -> Unit = { WheelPickerItem(it) },
)

@Composable fun MonthYearPicker(
    month: YearMonth,
    onMonthChange: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
    bounds: CalendarBounds = CalendarBounds.Default,
    labels: CalendarLabels = CalendarLabels.English,
    style: WheelPickerStyle = LocalWheelPickerStyle.current ?: WheelPickerStyle.Default,
    size: WheelPickerSize = LocalCalendarSize.current?.monthPicker ?: WheelPickerSize.Default,
    monthScroll: WheelPickerScroll? = LocalWheelPickerScroll.current,
    yearScroll: WheelPickerScroll? = LocalWheelPickerScroll.current,
)

@Composable fun MonthGrid(
    years: List<Int>,                                    // in the order given, usually latest first
    selected: YearMonth?,
    onSelect: (YearMonth) -> Unit,
    modifier: Modifier = Modifier,
    enabled: (YearMonth) -> Boolean = { true },
    columns: Int = 4,                                    // < 1 throws
    labels: CalendarLabels = CalendarLabels.English,
    style: MonthGridStyle = LocalMonthGridStyle.current ?: MonthGridStyle.Default,
    size: MonthGridSize = LocalMonthGridSize.current ?: MonthGridSize.Default,
    content: (@Composable (YearMonth) -> Unit)? = null,  // a tile's second line: the figure
)

@Composable fun DateTile(
    date: LocalDate,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,                  // null reads labels.dayDescription(date)
    labels: CalendarLabels = CalendarLabels.English,
    style: DateTileStyle = LocalDateTileStyle.current ?: DateTileStyle.Default,
    size: DateTileSize = LocalDateTileSize.current ?: DateTileSize.Default,
)

// CalendarScope slot parts: call only inside Calendar / DateRangeCalendar slots
@Composable fun CalendarScope.CalendarDay(
    day: CalendarDayState,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,                       // ignored on an unavailable day
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
    interactionSource: MutableInteractionSource? = null,
)
@Composable fun CalendarScope.DateRangeDay(
    day: DateRangeDayState,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    rangeLabels: DateRangeLabels = DateRangeLabels.English,
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    rangeStyle: DateRangeStyle = LocalDateRangeStyle.current ?: DateRangeStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
    interactionSource: MutableInteractionSource? = null,
)
@Composable fun CalendarScope.MonthHeader(
    modifier: Modifier = Modifier,
    showControls: Boolean = true,                        // the back/forward chevrons
    showMonthPicker: Boolean = true,                     // false = month as plain text
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
)
@Composable fun CalendarScope.WeekdayHeader(
    modifier: Modifier = Modifier,
    style: CalendarStyle = LocalCalendarStyle.current ?: CalendarStyle.Default,
    size: CalendarSize = LocalCalendarSize.current ?: CalendarSize.Default,
)
@Composable fun CalendarScope.TodayButton(
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.plain(),
    size: ButtonSize = ButtonSize.Small,
    interactionSource: MutableInteractionSource? = null,
)
@Composable fun CalendarScope.CalendarHeatDay(
    day: CalendarDayState,
    level: Int?,                                         // null = nothing that day (ringed empty tile)
    modifier: Modifier = Modifier,
    amount: String? = null,                              // one clipped line: "€84"
    onClick: (() -> Unit)? = null,
    contentDescription: String? = null,
    style: CalendarHeatStyle = LocalCalendarHeatStyle.current ?: CalendarHeatStyle.Default,
    size: CalendarHeatSize = LocalCalendarHeatSize.current ?: CalendarHeatSize.Default,
)
@Composable fun CalendarHeatLegend(
    lowLabel: String,
    highLabel: String,
    modifier: Modifier = Modifier,
    emptyLabel: String? = null,                          // adds the ringed "nothing" mark
    steps: Int = 4,
    contentDescription: String? = null,
    style: CalendarHeatStyle = LocalCalendarHeatStyle.current ?: CalendarHeatStyle.Default,
    size: CalendarHeatSize = LocalCalendarHeatSize.current ?: CalendarHeatSize.Default,
)
fun calendarHeatLevel(value: Float, max: Float, steps: Int): Int?
```

## Values and state

```kotlin
CalendarSelection.Single(date: LocalDate? = null)    // CalendarSelection.Empty == Single()
CalendarSelection.Multiple(dates: Set<LocalDate> = emptySet())
CalendarSelection.Range(range: CalendarDateRange? = null)   // draws only; pick with DateRangeCalendar

DateRangeSelection.Empty | Started(anchor: LocalDate) | Complete(range: CalendarDateRange)
selection.range: CalendarDateRange?                  // non-null once Complete
DateRangeSelection.select(date, constraints, bounds): DateRangeSelection   // the pure two-tap rule

CalendarDateRange(start: LocalDate, endInclusive: LocalDate)   // dayCount (ends included), nights, isSingleDay, asSequence()

CalendarBounds(minDate: LocalDate, maxDate: LocalDate)     // inclusive days
CalendarBounds.Default                               // 1900-01-01 .. 2100-12-31
CalendarBounds.around(today: LocalDate, years: Int)
CalendarBounds.of(minDate: LocalDate?, maxDate: LocalDate?)

fun interface CalendarDateRule { fun isEnabled(date: LocalDate): Boolean }
CalendarDateRule.All | None | allowing(dates) | blocking(dates) | inRange(range) | byDayOfWeek(predicate)
ruleA + ruleB                                        // both must allow

DateRangeConstraints(
    dateRule: CalendarDateRule = CalendarDateRule.All,
    minDays: Int = 1,                                // days, both ends included: two nights = 3
    maxDays: Int? = null,
    blockedDays: BlockedDayPolicy = BlockedDayPolicy.StopAtBlocked,   // or AllowGaps
)
constraints.endWindow(anchor, bounds): CalendarDateRange?

CalendarDayState(date, month: YearMonth, isAdjacent, isToday, isSelected, isEnabled)
DateRangeDayState(day: CalendarDayState, position: DateRangeDayPosition, isPreview)  // + date, isToday, isEnabled, isAdjacent
DateRangeDayPosition.None | Start | InRange | End | Single

DateTimeFieldFormat(date: (LocalDate) -> String, time: (LocalTime) -> String)
DateTimeFieldFormat.numeric(timeFormat: TimeFormat, timeLabels: TimePickerLabels = TimePickerLabels.English)
DateTimeFieldFormat.Iso                              // numeric(Hours24)

DateTimeLocale(firstDayOfWeek: DayOfWeek, timeFormat: TimeFormat)
DateTimeLocale.Current                               // from the platform
DateTimeLocale.Iso                                   // Monday weeks, 24-hour
LocalDateTimeLocale                                  // pin a subtree
```

| `CalendarState` member | Meaning |
| --- | --- |
| `bounds`, `today` | From `rememberCalendarState`; `today` is read once from `clock` in `timeZone` |
| `visibleMonth` / `settledMonth` | The month the header names (flips mid-swipe) / changes only at rest — load data from `settledMonth` |
| `canScrollBackward`, `canScrollForward` | For controls of your own |
| `isMonthPickerExpanded`; `expandMonthPicker()`, `collapseMonthPicker()`, `toggleMonthPicker()` | Swap the grid for the month wheels |
| `suspend animateTo(month)`, `scrollTo(month)`, `animateToNextMonth()`, `animateToPreviousMonth()`, `animateToToday()` | Launch from `rememberCoroutineScope()`; `scrollTo` (jump) when the move is not the user's |

`CalendarScope` (the receiver of `header`, `weekdayHeader`, `day`): `state`, `labels`,
`startDayOfWeek`, `bounds`, `visibleMonth`, `isMonthPickerExpanded`, `canGoBack`, `canGoForward`, and
fire-and-forget `nextMonth()`, `previousMonth()`, `goTo(month)`, `jumpTo(month)`, `goToToday()`,
`toggleMonthPicker()`, `collapseMonthPicker()` for an `onClick`.

## Rules

- **Controlled.** `Calendar`, `DateRangeCalendar`, `MonthYearPicker`, `MonthGrid` and `DateTimeField`
  draw the value given and report taps; store the result and pass it back. `onDateClick = null` /
  `onSelectionChange = null` is read-only (still pages and announces).
- **`TimePicker` is the exception:** `initialTime` seeds the wheels once; later changes do not move
  them — wrap in `key(resetCount) { TimePicker(…) }` to restart. `onTimeChange` fires only when the
  wheels come to rest after the user turns them. Minute precision.
- **`DateTimeField`:** `value` is nullable, `onValueChange` is not — a date picked on an empty field
  arrives at `defaultTime` (09:00). A time picked before any date is held as a draft and not
  reported; it is dropped when `value` changes. The default format is ISO digits (`2026-09-13`); a
  localised app passes its own `DateTimeFieldFormat`. Forward `field = field` from a `FieldGroup`.
  Its dropdown calendar is `CalendarSize.Compact` unless `LocalCalendarSize` is provided.
- **Three limits, not interchangeable:** `CalendarBounds` (on the state) decides which months exist —
  put a booking window here, not in a rule; `CalendarDateRule` (`dateRule`) greys days and refuses
  taps — keep it a set lookup or a comparison, it runs per cell; `DateRangeConstraints` limits a
  range's length — pass the rule inside `constraints`, not to the calendar.
- Changing `bounds` builds a new state; changing `initialMonth` does not. The state saves only the
  visible month.
- Pin "today" in tests with `rememberCalendarState(clock = fixedClock, timeZone = TimeZone.UTC)`.
- A custom `day` slot draws through `CalendarDay` / `DateRangeDay` and decorates around it; a cell
  drawn from scratch loses grid semantics and keyboard navigation.
- **Range taps:** from `Empty` or `Complete` a tap starts a new range; from `Started` a later allowed
  day completes it, an earlier day becomes the new anchor. `onRangeComplete` runs after
  `onSelectionChange` when a tap finishes a range. Under `StopAtBlocked` with a blocking rule, state
  a `maxDays` or narrow the bounds.
- **Heat map:** `level = calendarHeatLevel(value, max, steps = CalendarHeatStyle.Default.ramp.size)`
  — `steps` is the ramp's size (by default `ArecaTheme.colors.accentScale.size`); fewer than two
  throws. Zero, negative or non-finite values give `null` (ringed empty tile). Refuse days still to
  come with `dateRule` — they draw flat and take no tap. Keep `amount` short.
- **`MonthGrid`:** never calls `onSelect` for the selected or a refused month; a refused month draws
  flat.

## Localisation

- Conventions: `DateTimeLocale.Current` supplies `startDayOfWeek` and `TimeFormat` from the platform
  (including the user's 24-hour setting on Android and iOS). Override per component, or pin a
  subtree: `CompositionLocalProvider(LocalDateTimeLocale provides DateTimeLocale.Iso) { … }` — do this
  in screenshot tests.
- Words: every word comes from a labels object with an English default. Pass the app's own, as
  top-level `val`s (a new lambda each recomposition rebuilds what depends on it).

| Labels | Used by | Fields |
| --- | --- | --- |
| `CalendarLabels` (`English`, `EnglishNarrow`) | `Calendar`, `DateRangeCalendar`, `MonthYearPicker`, `DateTimeField(calendarLabels)`, `MonthGrid`, `DateTile` | `weekday`, `monthHeader`, `pickerMonth`, `dayDescription`, `monthPickerAction`, `monthPickerMonth`, `monthPickerYear`, `previousMonthAction`, `nextMonthAction`, `todayAction`, `monthPickerExpanded`, `monthPickerCollapsed`, `weekdayName`, `dayNumber`, `year`, `shortMonth` |
| `DateRangeLabels` (`English`) | `DateRangeCalendar(rangeLabels)`, `DateRangeDay` | `position: (DateRangeDayPosition) -> String?`, `length: (CalendarDateRange) -> String` ("3 nights") — the calendar never draws `length`; use it in your own footer |
| `TimePickerLabels` (`English`) | `TimePicker`, `DateTimeField(timeLabels)` | `hour`, `minute`, `period`, `am`, `pm`, `hourWithPeriod` |
| `DateTimeFieldLabels` (`English`) | `DateTimeField(labels)` | `date`, `time`, `expanded`, `collapsed`, `datePlaceholder` ("Pick a date"), `timePlaceholder` ("--:--") |

```kotlin
val GermanCalendarLabels = CalendarLabels(
    weekday = { GermanWeekdays[it.ordinal].take(2) },
    monthHeader = { "${GermanMonths[it.month.ordinal]} ${it.year}" },
    pickerMonth = { GermanMonths[it.ordinal] },
    dayDescription = { d -> "${GermanWeekdays[d.dayOfWeek.ordinal]}, ${d.day}. ${GermanMonths[d.month.ordinal]} ${d.year}" },
    monthPickerAction = "Monat und Jahr wählen",
    monthPickerMonth = "Monat",
    monthPickerYear = "Jahr",
    previousMonthAction = "Vorheriger Monat",
    nextMonthAction = "Nächster Monat",
    todayAction = "Heute",
    monthPickerExpanded = "Ausgeklappt",
    monthPickerCollapsed = "Eingeklappt",
    weekdayName = { GermanWeekdays[it.ordinal] },
    shortMonth = { GermanMonths[it.ordinal].take(3) },
)
```

## Accessibility

- The grid is one tab stop; arrows move by day and week, Home/End to the week's ends, Page Up/Down by
  month, Shift + Page Up/Down by year. Right-to-left swaps Left/Right.
- Each day reads `CalendarLabels.dayDescription` (weekday included) with its row and column;
  unavailable days are announced; a range day appends `DateRangeLabels.position`. Weekday headings
  read `weekdayName`. Each month the calendar settles on is announced.
- `MonthGrid` tiles are radio buttons in one group, read as `monthHeader` then `content`; year
  headings are headings.
- `DateTile` reads `contentDescription` or the whole date via `dayDescription`.
- `CalendarHeatLegend` is one node: pass a sentence as `contentDescription` ("Shaded from €0 to
  €280; a ringed day had nothing").
- Each `DateTimeField` half is a button reading its name and value with an expanded state.

## Styles and sizes

| Component | Style · local | Size · local |
| --- | --- | --- |
| `Calendar`, `MonthHeader`, `WeekdayHeader`, `CalendarDay`, `TodayButton` (its own `ButtonStyle`) | `CalendarStyle` · `LocalCalendarStyle` · `default(weekdayLabel, monthLabel, dayBg, dayContent, selectedBg, selectedContent, todayBg, todayContent, todayBorder, disabledBg, disabledContent, …, monthControl, monthPickerToggle, monthPicker)` | `CalendarSize` · `LocalCalendarSize` · `Default`, `Compact` (inside a dropdown or sidebar), `default(…)` |
| `DateRangeCalendar`, `DateRangeDay` | `DateRangeStyle` · `LocalDateRangeStyle` · `default(track, previewTrack, capBg, capContent, …, capShape, trackCorner)`, plus `CalendarStyle` | `CalendarSize` |
| `MonthYearPicker`, `TimePicker` | foundation `WheelPickerStyle` · `LocalWheelPickerStyle` | `WheelPickerSize` (month picker: `CalendarSize.monthPicker`) |
| `DateTimeField` | foundation `FieldStyle` · `LocalFieldStyle` | `FieldSize` · `LocalFieldSize` |
| `CalendarHeatDay`, `CalendarHeatLegend` | `CalendarHeatStyle` · `LocalCalendarHeatStyle` · `default(ramp = accentScale, empty, emptyRing, future, futureContent, content, inkOnLight, inkOnDark, todayRing, selectedRing, legendLabel)`; `inkOn(shade)` | `CalendarHeatSize` · `LocalCalendarHeatSize` · `Default`, `default(…)` |
| `MonthGrid` | `MonthGridStyle` · `LocalMonthGridStyle` · `default(year, bg, content, bgSelected, contentSelected, bgDisabled, contentDisabled)` | `MonthGridSize` · `LocalMonthGridSize` · `Default` (52 dp tiles), `default(…)` |
| `DateTile` | `DateTileStyle` · `LocalDateTileStyle` · `default(bg, month, day)` | `DateTileSize` · `LocalDateTileSize` · `Default` (46 dp), `default(…)` |

The module's locals are `null` unless provided and are **not seeded** by `ArecaThemeProvider`
(the wheel and field locals are foundation's and are seeded):

```kotlin
CompositionLocalProvider(
    LocalCalendarStyle provides CalendarStyle.default(selectedBg = ArecaTheme.colors.bgSuccessBase),
    LocalCalendarSize provides CalendarSize.Compact,
) { BookingScreen() }
```

## Snippets

```kotlin
// One day, today onward, 90-day window, with a Today control
val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
val state = rememberCalendarState(bounds = CalendarBounds(today, today.plus(90, DateTimeUnit.DAY)))
Calendar(
    state = state,
    selection = CalendarSelection.Single(day),
    onDateClick = onPickDay,
    dateRule = CalendarDateRule.byDayOfWeek { it != DayOfWeek.SUNDAY } + CalendarDateRule.blocking(holidays),
    labels = appCalendarLabels,
    header = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MonthHeader(modifier = Modifier.weight(1f))
            TodayButton()
        }
    },
)

// Decorated day: event dot under the number
Calendar(
    selection = selection,
    onDateClick = onPick,
    day = { day ->
        Box(contentAlignment = Alignment.BottomCenter) {
            CalendarDay(day = day, onClick = { onPick(day.date) })
            if (day.date in eventDays && !day.isAdjacent) EventDot()
        }
    },
)

// Stay of 2–14 nights that stops at booked nights
DateRangeCalendar(
    selection = stay,
    onSelectionChange = { stay = it },
    constraints = DateRangeConstraints(
        dateRule = CalendarDateRule.blocking(bookedNights),
        minDays = 3,
        maxDays = 15,
    ),
    rangeLabels = appRangeLabels,
)
stay.range?.let { Text(appRangeLabels.length(it)) }

// Date-time in a form
FieldGroup(label = startsLabel, error = startError) { field ->
    DateTimeField(
        value = start,
        onValueChange = onStartChange,
        field = field,
        format = DateTimeFieldFormat(date = { it.format(appDateFormat) }, time = { it.format(appTimeFormat) }),
        labels = appFieldLabels,
        calendarLabels = appCalendarLabels,
        timeLabels = appTimeLabels,
    )
}

// Time wheels
TimePicker(initialTime = alarm, onTimeChange = onAlarmChange, labels = appTimeLabels)

// Heat-map month
val steps = CalendarHeatStyle.Default.ramp.size
Calendar(
    selection = CalendarSelection.Single(picked),
    onDateClick = onPick,
    dateRule = CalendarDateRule { it <= today },
    day = { day ->
        val spent = spentOn(day.date)
        CalendarHeatDay(
            day = day,
            level = calendarHeatLevel(spent, max = busiestDay, steps = steps),
            amount = if (spent > 0f) formatShort(spent) else null,
            onClick = { onPick(day.date) },
        )
    },
)
CalendarHeatLegend(lowLabel = "€0", highLabel = "€280", emptyLabel = nothingSpentLabel, contentDescription = legendSentence)

// Month tiles with totals
MonthGrid(
    years = listOf(2026, 2025),
    selected = month,
    onSelect = onSelectMonth,
    enabled = { it < thisMonth },
    labels = appCalendarLabels,
) { m -> Text(totalFor(m)) }

// Date tile leading a row
ListItem(
    leading = { DateTile(date = payment.expectedOn, labels = appCalendarLabels) },
    title = { Text(payment.payee) },
    value = { Text(payment.amount) },
)

// Month wheels on their own
MonthYearPicker(month = month, onMonthChange = onMonthChange, bounds = CalendarBounds.around(today, years = 5))
```
