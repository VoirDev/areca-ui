# Changelog

All notable changes to the Areca libraries. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and the libraries use
[semantic versioning](https://semver.org/).

## Unreleased

### Added

- `foundation`: `AdaptiveMenu(state, groups = …)` draws actions in groups, one full-width hairline
  between groups and none inside one, with destructive actions moved to a last group in the danger
  ink. `presentation` picks its form on every window: `AdaptiveMenuPresentation.Dropdown`, the
  default, a card hanging from its trigger, or `AdaptiveMenuPresentation.Sheet`, a sheet from the
  bottom edge. `LocalAdaptiveMenuPresentation` sets it for an app. `AdaptiveMenuStyle`
  and `AdaptiveMenuSize`, with `LocalAdaptiveMenuStyle` and `LocalAdaptiveMenuSize` (`null` unless
  provided), give the card's shape (the surface shape), lift (`elevation.md`), outline (none),
  width (248dp) and its rows — control-height `ListItem`s with the glyph at the far end or, with
  `AdaptiveMenuIconPosition.Leading`, before the label.

- `foundation`: `AdaptiveNavigation(navigationVisible = false)` hides the rail, sidebar, edge column
  or bottom bar and gives the content the whole window. The navigation slides away and back on the
  theme's motion tokens, and the content keeps its place in the composition, so a navigation host
  inside it keeps its state.
- `foundation`: `AdaptiveNavigation(compactNavigation = CompactNavigation.None)` draws no navigation
  on a compact width or a compact height (a phone on its side), for an app whose phone screens reach
  each other themselves. `CompactNavigation.Bar`, the default, keeps the `TabBar` or `bottomBar` and
  the label-less rail.
- `foundation`: `AdaptiveNavigationLayout.None`, what the content of `AdaptiveNavigation` reads when
  no navigation is drawn.
- `keyboard`: `KeyboardKeys(maxKeyHeight = …)` fills the height the keys are given, from
  `KeyboardSize.keyHeight` up to `maxKeyHeight`, and places the grid at the bottom of a taller space.
  The default, `Dp.Unspecified`, keeps every key at `KeyboardSize.keyHeight`.
- `datetime`: the calendar title steps out from the days to the twelve months of the year
  (`CalendarLevel.Months`) and to a page of twelve years (`CalendarLevel.Years`). A tap on a tile is
  the choice; Escape or the tile already shown returns unchanged. `CalendarState` gains `level`,
  `zoomOut()`, `showDays()`, `selectYear()`, `selectMonth()`, `browsedYear`, `browsedYears`,
  `browseBackward()`, `browseForward()` and their `can…` flags; `CalendarScope` gains `level`,
  `goBack()`, `goForward()`, `zoomOut()` and `showDays()`.
- `datetime`: `Calendar(markers = …)` and `DateRangeCalendar(markers = …)` draw a `CalendarMarker`
  under each day — `Dots`, `Count`, `Icon`, `Label`, `Load` or `Custom` — in a row every day keeps,
  so markers never sit on the selection. On a filled tile they take the selected day's ink.
  `monthMarkers` does the same for month tiles; `CalendarDay(marker = …)` overrides one day.
- `datetime`: `Calendar(fixedWeekCount = false)` animates its height between months, following a
  swipe as well as the chevrons. The `MonthHeader` title slides with the month it names.
- `datetime`: `CalendarState.fold(anchor, animationSpec)` folds a calendar's days to one week;
  `unfold(animationSpec)`, `snapFoldTo(fraction)` (a fold under the finger), `animateFoldTo(anchor)`,
  `isFolded`, `foldFraction` and `foldAnchor` go with it. The fold moves in layout, without
  recomposing the grid.
  Folded, the chevrons and `CalendarScope.goBack()` / `goForward()` step a week, announced with the
  new `CalendarLabels.previousWeekAction` and `nextWeekAction`.
- `datetime`: `CalendarAgenda`, a compact month joined to a long, scrolling day-by-day list the app
  draws (`dev.voir.areca.datetime.calendar.agenda`). Tapping a day scrolls its group up and flashes
  its heading; scrolling picks the day whose heading is pinned at the top; on a compact window the
  calendar collapses to the selected week with the list's scroll, like a collapsing top bar; from a
  medium window the calendar is a card beside the list. With `CalendarAgendaState`, `rememberCalendarAgendaState`,
  `CalendarAgendaDayScope`, `CalendarAgendaStyle`, `CalendarAgendaSize`, `CalendarAgendaLabels` and
  their locals.
- `datetime`: `MonthHeader(actions = …)` draws controls of your own before the chevrons.
- `datetime`: `CalendarLegend(items)` with `CalendarLegendItem(color, label)`, the key to a calendar's
  dot markers; styled by the new `CalendarStyle.legendLabel` and `CalendarSize.legendDotSize`,
  `legendTextStyle`, `legendSpacing`, `legendItemSpacing` and `legendPadding`.
- `datetime`: `CalendarSize.maxDayHeight` caps a day's square; past it the cell is wider than tall
  and its circle stays round. `CalendarSize.Compact` caps days at 40dp.
- `datetime`: `CalendarStyle.dayShape` — `CalendarDayShape.Circle` (the default) or `Tile`, a fill
  over the whole cell for days that carry a price or a count.

### Fixed

- `foundation`: a `Section` takes the screen gutter of the place it is drawn. Section sizes built
  once at the theme's root — an app's `LocalSettingsSizes` in its component defaults — kept the
  window's gutter, so a section in a narrower pane (a `ContentColumn` on a tablet) sat 8dp further in
  than the content beside it. An `outerPadding` the app sets is kept as it is.
- `foundation`: a `FlowModal` measured without a height limit — as an iOS dialog's layer can be for a
  frame while the window turns — no longer crashes with "Can't represent a width of … and height of …
  in Constraints"; its body is left unlimited instead of taking the header and footer from the
  unlimited height.
- `foundation`: `AdaptiveNavigation` no longer recreates its content when the form changes — a
  resize, a turned tablet, an unfolded foldable, the sidebar toggled, the navigation hidden. The
  content is moved, so `remember` and `rememberSaveable` state inside it, a navigation host's
  included, survives.

### Changed

- `foundation`: every `FlowModal` stops at `AdaptiveDefaults.ReadableWidth` (640dp), centred on the
  bottom edge of a wider window: `FlowModalSize.maxWidth` and `FlowModalSize.default(maxWidth = …)`
  default to it instead of `Dp.Unspecified`. `ConfirmSheet`, `SelectFlowModal` and `AdaptiveMenu`'s
  sheet follow. A phone is narrower than the cap and is unchanged; `maxWidth = Dp.Unspecified` spans
  the window as before.
- `foundation`, `profile`, `attachments`: no component turns into a dialog by window any more.
  `ConfirmSheet`, `BirthdayRow`'s calendar, `AvatarSourceSheet`, every `attachments` sheet and
  `SupportingPane`'s sheet below 920dp are a `FlowModal` on every window, where from an expanded
  window up they were a centred `Dialog`. Whether a question is a sheet or a dialog is the app's
  choice: `ConfirmationDialog` and `Dialog` are the centred forms.
- `foundation`: `AdaptiveMenu` keeps one form on every window. `AdaptiveMenu(state, actions = …)`,
  `AdaptiveToolbar`'s overflow and `EdgeColumn`'s More follow `LocalAdaptiveMenuPresentation` —
  a card at the trigger unless the app provides `AdaptiveMenuPresentation.Sheet` — where they were a
  sheet under touch below an expanded window and a dropdown otherwise.
- `media`: a picture peeked with `ZoomBehavior.Peek` moves toward the window's centre as it grows,
  reaching it at twice its size, instead of growing around its frame; a picture at the top of the
  screen no longer spills off the top. It still follows the fingers and springs back onto its frame.
- `foundation`: a `DropdownPopup` grows in with a short fade and scale from the corner that touches
  its anchor — on the side it opened on, after a flip — and shrinks back into it when it closes,
  staying composed until that exit has run (`motion.fast`). It ignores presses while it leaves.
  Under `ArecaMotion.reduceMotion` it appears and goes at once, as before.
- `foundation`: an exhaustive `when` over `AdaptiveNavigationLayout` needs a `None` branch.
- `foundation`, `keyboard`: `AdaptiveNavigation` and `KeyboardKeys` take new trailing parameters
  with defaults. Calls compile unchanged; code compiled against an earlier version must be rebuilt.
- `datetime`: `CalendarSize`, `CalendarStyle` and `CalendarLabels` gain properties (the day height
  cap, the legend tokens, the week actions) and `MonthHeader` an `actions` parameter, all with
  defaults. Calls compile unchanged; code compiled against an earlier version must be rebuilt.
- `datetime`: `CalendarSize.Compact` caps a day at 40dp tall, so a compact calendar wider than about
  300dp has shorter rows than before.

### Removed

- `foundation`: `AdaptiveSheet` and `AdaptiveDefaults.usesDialog`, without deprecation; see
  Migrating.
- `datetime`: the month and year wheels inside `Calendar`, replaced by the month and year levels.
  This release removes, without deprecation:
  - `CalendarState.isMonthPickerExpanded`, `expandMonthPicker()`, `collapseMonthPicker()`,
    `toggleMonthPicker()` — use `level`, `zoomOut()` and `showDays()`;
  - `CalendarScope.isMonthPickerExpanded`, `toggleMonthPicker()`, `collapseMonthPicker()` — use
    `level`, `zoomOut()` and `showDays()`;
  - `MonthHeader(showMonthPicker = …)` — now `zoomable`;
  - `CalendarSize.monthPicker` and `CalendarStyle.monthPicker`; `monthPickerToggle` on both is now
    `levelToggle`. `MonthYearPicker` takes its size from `LocalWheelPickerSize`;
  - `CalendarLabels.monthPickerAction`, `monthPickerExpanded`, `monthPickerCollapsed` — use
    `chooseMonthAction`, plus the new `chooseYearAction`, `pickMonthHint`, `pickYearHint`,
    `yearSpan`, `previousYearAction`, `nextYearAction`, `previousYearsAction`, `nextYearsAction`.
  - An app that wants wheels places `MonthYearPicker` in its own sheet with Cancel and Done.

### Migrating

- `AdaptiveSheet(visible, onDismissRequest, header = …, footer = …) { … }` becomes a `FlowModal`
  driven from the same state, or a `Dialog` where the app wants a centred window:

  ```kotlin
  val sheet = rememberFlowModal()
  LaunchedEffect(visible) { if (visible) sheet.show(partial = false) else sheet.hide() }
  FlowModal(controller = sheet, onDismissRequest = onDismissRequest, header = …, footer = …) { … }
  ```

- `AdaptiveDefaults.usesDialog(window)` has no replacement: choose the sheet or the dialog where it
  opens. An app that wants its own rule reads `ArecaTheme.hostWindow`.
- `FlowModalSize.default(maxWidth = AdaptiveDefaults.ReadableWidth)` is the default now; drop it.
  A sheet that should span a wide window passes `maxWidth = Dp.Unspecified`.
- An app that wants its menus as sheets provides `LocalAdaptiveMenuPresentation provides
  AdaptiveMenuPresentation.Sheet` in `componentDefaults`.
