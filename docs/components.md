# Components

Every component in `dev.voir.areca:foundation`, grouped by family, with what it is for and where it
lives. Find the intent below, or scan a family's table; each component links to its section in the
[foundation README](../ui/foundation/README.md), which has the snippet and the options that matter.

**How to read it.** *Package* is the part after `dev.voir.areca.foundation` — `button` means
`dev.voir.areca.foundation.button`. *Playground* is the component's page in the playground
(`./gradlew :playground:run`), from
`playground/src/commonMain/kotlin/dev/voir/areca/playground/foundation/<package>/<Page>.kt` (the
`field.select` pages are in `field`); a dash means it has none yet. KDoc is the reference for every parameter.

Every component needs the theme around it: wrap the application in `ArecaThemeProvider`, and a
region that is always dark in `ArecaDarkTheme`, both in `dev.voir.areca.theme` — see
[Theme](theme.md). Styles, sizes and defaults work the same way for every component — see
[Customising](customising.md).

## Which component?

| I need to… | Use |
| --- | --- |
| Offer the screen's primary action | `Button` (the default style is `ButtonStyle.solid()`; one per screen) |
| Offer a secondary, cancel or destructive action | `Button` with `ButtonStyle.soft()`, `outlined()` or `plain(ActionColor.Neutral)`; `soft(ActionColor.Danger)` for a destructive one |
| Act with an icon alone | `IconButton`; in a top bar, `IconAction` or a named control such as `SearchAction` |
| Put a link in text | `TextLink`; a prompt and a link ("No account? Sign up") `TextLinkPrompt` |
| Show text; a title with a supporting line | `Text` with a `TextVariant`; `Heading` |
| Hide a value until revealed | `SensitiveText` |
| Show an icon; give a block its subject; show a disclosure arrow | `Icon`; `FeaturedIcon`; `Chevron` |
| Take labelled text input with a hint or an error | `FieldGroup` around `TextField` |
| Take a password; show its rules | `PasswordField`; `RequirementList` |
| Search | `SearchField` |
| Take an amount with a currency or a unit | `AffixTextField` |
| Take a one-time code | `OtpField` |
| Take a compound value — a dialling code and a number | `FieldRow` with `SelectSegment` and `TextSegment` |
| Pick from a short list; from a long or searchable one | `SelectField`; `SelectFlowModal` |
| Pick from a scrolling wheel | `WheelPicker` with `WheelPickerColumn`; dates and times are in [datetime](../ui/datetime/README.md) |
| Filter with chips | `BadgeSelect`, `LazyBadgeSelect` (scrolls) or `FlowBadgeSelect` (wraps) |
| Pick a colour; nudge a small number | `SwatchPicker`; `NumberStepper` |
| Switch a setting on; tick a box in a form; pick one of many | `Toggle`; `Checkbox`; `Radio` |
| Put one of those controls in a row with a title | `ToggleListItem`, `CheckboxListItem`, `RadioListItem`, `CheckmarkListItem` |
| Build a settings screen | `Settings` with `Section`s of `SettingsRow`, `SettingsToggleRow`, `SettingsSelectRow`, `SettingsTextFieldRow`, `SettingsOptionRow` |
| Show grouped rows on one surface | `List` (eager), `LazyList` (lazy), `ListItemColumn` (rows you compose) |
| Show a row that navigates; one that expands | `NavigationListItem`; `ExpandableListItem` |
| Show a remote list with loading, empty and error faces | `ContentList`; paged, `PagedContentList` |
| Show a grid with group headers; tabular data | `LazyVerticalGroupedGrid`; `Table` or `LazyTable` |
| Group content on a surface | `Card`; show and hide a region `Expandable` |
| Separate two things | `HorizontalDivider`, `VerticalDivider` |
| Frame a screen with bars | `BaseLayout` |
| Title a screen | `TopBar` with `BackAction`; a large title that collapses `LargeTopBar` |
| Collapse a hero above content that stays | `CollapsingHeaderLayout` |
| Show the application's destinations | `TabBar` (docked) or `FloatingTabBar` (floating) |
| Switch views within a screen | `Tabs` |
| Choose one of a few values; words with a moving pill; a three-state toggle | `SlidingSelector` (`SlidingSelectorStyle.Segmented`, `Pill`, `Underline`, `Button`; `SelectorMode.Optional` to allow none) |
| Navigate a sidebar or a tree | `NavList` |
| Page through an onboarding tour | `SwipePager` with `PagerControls` and `PageIndicator` |
| Say which picture or page is on screen | `PageIndicator` (`variant`: dots, a shrinking window, a line, story segments); as a count, `PageCounter` |
| Show the steps of a flow | `StepIndicator` |
| Refresh by pulling | `PagedContentList(onRefresh = …)`, or `PullToRefreshLayout` (experimental) |
| Open a bottom sheet | `FlowModal` with `FlowModalActionBar` |
| Confirm something hard to undo | `ConfirmSheet`; centred, `ConfirmationDialog` |
| Open any other dialog | `Dialog` |
| Open a menu from a control | `DropdownPopup` |
| Say something just happened | `ToastHost` |
| Say something that stays on the page | `Notice` with `NoticeText`; one that comes and goes `AnimatedNotice` |
| Fill an empty or failed region | `Fallback` |
| Give a whole screen to progress or a result | `StatusScene` with `StatusMark` |
| Show that something is loading | `Spinner`; `ProgressBar`; `Skeleton`, `ListItemSkeleton`, `SettingsRowSkeleton` |
| Show a status label; a count; a dot | `Badge`; `NotificationBadge`; `IndicatorDot` |
| Show a figure; a block of figures | `Stat`; `StatGrid` |
| Show a chart | [charts](../ui/charts/README.md) |
| Show a person | `Avatar` |
| Build a row of your own that lines up with list rows | `SlotRow` |
| Place content beside other content; veil a box | `RelativePositionLayout`; `Overlay` |
| Tell a screen reader something changed | `AnnounceOnChange` |

## Buttons and actions

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="images/foundation-buttons-dark.png">
  <img alt="Buttons in each style and size, and icon buttons" src="images/foundation-buttons-light.png" width="600">
</picture>

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`Button`](../ui/foundation/README.md#buttons-and-actions) | An action with a label, and optionally an icon either side | `button` | `ButtonShowcase` |
| [`IconButton`](../ui/foundation/README.md#buttons-and-actions) | An action shown by an icon alone | `button` | `IconButtonShowcase` |
| [`BackAction`](../ui/foundation/README.md#buttons-and-actions) | A top bar's back control | `topbar` | `TopBarActionsShowcase` |
| [`CloseAction`](../ui/foundation/README.md#buttons-and-actions) | A top bar's dismiss control, for a sheet or a full-screen task | `topbar` | `TopBarActionsShowcase` |
| [`DoneAction`](../ui/foundation/README.md#buttons-and-actions) | The worded control that finishes a task: "Done", "Save" | `topbar` | `TopBarActionsShowcase` |
| [`CancelAction`](../ui/foundation/README.md#buttons-and-actions) | The worded control that abandons a task: "Cancel", "Discard" | `topbar` | `TopBarActionsShowcase` |
| [`TextAction`](../ui/foundation/README.md#buttons-and-actions) | Any other top bar control written as a word | `topbar` | `TopBarActionsShowcase` |
| [`IconAction`](../ui/foundation/README.md#buttons-and-actions) | Any other top bar control shown as one icon | `topbar` | `TopBarActionsShowcase` |
| [`Action`](../ui/foundation/README.md#buttons-and-actions) | A top bar control holding any mark | `topbar` | `TopBarActionsShowcase` |
| [`SearchAction`](../ui/foundation/README.md#buttons-and-actions) | Open search | `topbar` | `TopBarActionsShowcase` |
| [`ShareAction`](../ui/foundation/README.md#buttons-and-actions) | Share what is on screen | `topbar` | `TopBarActionsShowcase` |
| [`EditAction`](../ui/foundation/README.md#buttons-and-actions) | Edit what is on screen | `topbar` | `TopBarActionsShowcase` |
| [`DeleteAction`](../ui/foundation/README.md#buttons-and-actions) | Delete what is on screen or selected | `topbar` | `TopBarActionsShowcase` |
| [`AddAction`](../ui/foundation/README.md#buttons-and-actions) | Create a new item of what the screen lists | `topbar` | `TopBarActionsShowcase` |
| [`SettingsAction`](../ui/foundation/README.md#buttons-and-actions) | Open settings | `topbar` | `TopBarActionsShowcase` |
| [`DownloadAction`](../ui/foundation/README.md#buttons-and-actions) | Save what is on screen to the device | `topbar` | `TopBarActionsShowcase` |
| [`NotificationsAction`](../ui/foundation/README.md#buttons-and-actions) | Open notifications, with the unread count | `topbar` | `TopBarActionsShowcase` |
| [`FilterAction`](../ui/foundation/README.md#buttons-and-actions) | Open filters, with the number in force | `topbar` | `TopBarActionsShowcase` |
| [`FavoriteAction`](../ui/foundation/README.md#buttons-and-actions) | Mark and unmark a favourite | `topbar` | `TopBarActionsShowcase` |
| [`MenuAction`](../ui/foundation/README.md#buttons-and-actions) | Open a navigation drawer | `topbar` | `TopBarActionsShowcase` |
| [`OverflowAction`](../ui/foundation/README.md#buttons-and-actions) | Open everything the bar had no room for | `topbar` | `TopBarActionsShowcase` |
| [`AvatarAction`](../ui/foundation/README.md#buttons-and-actions) | Who is signed in, opening an account menu | `topbar` | `TopBarActionsShowcase` |

The `…Action` controls are written inside a `TopBar`'s or `LargeTopBar`'s `leading` and `trailing`
slots only.

## Text

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`Text`](../ui/foundation/README.md#text) | All text, by `TextVariant` and `TextColor`, or inheriting a slot's style | `text` | `TextShowcase` |
| [`Heading`](../ui/foundation/README.md#text) | A section's title with a supporting line, announced as a heading | `text` | `TextShowcase` |
| [`TextLink`](../ui/foundation/README.md#text) | A standalone pressable label — "Forgot password?" | `text` | `TextLinkShowcase` |
| [`TextLinkPrompt`](../ui/foundation/README.md#text) | A sentence with one pressable part — "No account? Sign up" | `text` | `TextLinkShowcase` |
| [`SensitiveText`](../ui/foundation/README.md#text) | A value hidden until revealed — a balance, a card number | `text` | `SensitiveTextShowcase` |

## Icons

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`Icon`](../ui/foundation/README.md#icons) | A glyph inline with text | `icon` | `IconsShowcase` |
| [`FeaturedIcon`](../ui/foundation/README.md#icons) | A glyph in a shaped container — an empty state, a dialog header | `icon` | `FeaturedIconShowcase` |
| [`Chevron`](../ui/foundation/README.md#icons) | A chevron that turns with its direction — a disclosure, a dropdown | `icon` | `ChevronShowcase` |

The glyphs themselves are `ArecaIcons.*` in the optional [icons](../standalone/icons/README.md)
artifact.

## Badges and avatars

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`Badge`](../ui/foundation/README.md#badges-and-avatars) | A short label — a status, a tag, a filter | `badge` | `BadgeShowcase` |
| [`NotificationBadge`](../ui/foundation/README.md#badges-and-avatars) | A count, overlaid on something else | `badge` | `BadgeShowcase` |
| [`IndicatorDot`](../ui/foundation/README.md#badges-and-avatars) | A status carried by colour alone: presence, unread | `badge` | `BadgeShowcase` |
| [`BadgeSelect`](../ui/foundation/README.md#badges-and-avatars) | Chips that select something, on one line | `badge` | `BadgeShowcase` |
| [`LazyBadgeSelect`](../ui/foundation/README.md#badges-and-avatars) | The same on one scrolling line | `badge` | `BadgeShowcase` |
| [`FlowBadgeSelect`](../ui/foundation/README.md#badges-and-avatars) | The same on wrapping lines | `badge` | `BadgeShowcase` |
| [`Avatar`](../ui/foundation/README.md#badges-and-avatars) | A person or entity: an image, initials or an empty shape | `avatar` | `AvatarShowcase` |

## Stats

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`Stat`](../ui/foundation/README.md#stats) | One figure with its label and an optional note | `stat` | `StatShowcase` |
| [`StatGrid`](../ui/foundation/README.md#stats) | Stats in equal columns, read as a set | `stat` | `StatShowcase` |

## Selection controls

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="images/foundation-selection-dark.png">
  <img alt="Checkboxes, radios, toggles, sliding selectors, swatches, a number stepper and a badge select" src="images/foundation-selection-light.png" width="720">
</picture>

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`Checkbox`](../ui/foundation/README.md#selection-controls) | A value in a form, saved later; `indeterminate` for mixed children | `selection` | `CheckboxShowcase` |
| [`Radio`](../ui/foundation/README.md#selection-controls) | One option of several | `selection` | `RadioShowcase` |
| [`Toggle`](../ui/foundation/README.md#selection-controls) | A setting that applies the moment it is flipped | `selection` | `ToggleShowcase` |
| [`NumberStepper`](../ui/foundation/README.md#selection-controls) | A whole number nudged by one between bounds | `selection` | `NumberStepperShowcase` |
| [`SlidingSelector`](../ui/foundation/README.md#selection-controls) | A row of options with one thumb sliding to the chosen one: segments, a pill under words, underlines, a three-state toggle. `Tabs` and `FloatingTabBar` are built on it | `selection` | `SlidingSelectorShowcase` |
| [`Swatch`](../ui/foundation/README.md#selection-controls) | One colour sample | `selection` | `SwatchShowcase` |
| [`SwatchPicker`](../ui/foundation/README.md#selection-controls) | A scrolling set of swatches with one selection | `selection` | `SwatchShowcase` |

With a title and a row-wide tap target, use the list rows under
[Lists and settings](#lists-and-settings).

## Fields

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="images/foundation-fields-dark.png">
  <img alt="Text, password, search and select fields, one showing an error" src="images/foundation-fields-light.png" width="720">
</picture>

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`TextField`](../ui/foundation/README.md#fields) | Free text | `field` | `TextFieldShowcase` |
| [`PasswordField`](../ui/foundation/README.md#fields) | A secret, with a reveal control | `field` | `TextFieldShowcase` |
| [`SearchField`](../ui/foundation/README.md#fields) | A query, with a clear control and the search action | `field` | `TextFieldShowcase` |
| [`AffixTextField`](../ui/foundation/README.md#fields) | A value with a fixed prefix or suffix — a currency, a unit | `field` | `TextFieldShowcase` |
| [`OtpField`](../ui/foundation/README.md#fields) | A one-time code, one character to a cell | `field` | `OtpFieldShowcase` |
| [`SelectField`](../ui/foundation/README.md#fields) | A value chosen from a dropdown | `field.select` | `SelectFieldShowcase` |
| [`SelectFlowModal`](../ui/foundation/README.md#fields) | A long or searchable list chosen from in a sheet | `field.select` | `SelectFlowModalShowcase` |
| [`FieldRow`](../ui/foundation/README.md#fields) | Several parts sharing one border and one status | `field` | `FieldRowShowcase` |
| [`TextSegment`](../ui/foundation/README.md#fields) | A typed part of a `FieldRow` | `field` | `FieldRowShowcase` |
| [`SelectSegment`](../ui/foundation/README.md#fields) | A dropdown part of a `FieldRow` | `field.select` | `FieldRowShowcase` |
| [`FieldGroup`](../ui/foundation/README.md#fields) | A field's label and its assistive or error line | `field` | `FieldGroupShowcase` |
| [`FieldGroupLayout`](../ui/foundation/README.md#fields) | `FieldGroup`'s arrangement with the label and message as your own slots | `field` | — |
| [`Field`](../ui/foundation/README.md#fields) | The bare container, for a value no stock field holds | `field` | — |

## Feedback

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="images/foundation-feedback-dark.png">
  <img alt="A notice, a progress bar, skeleton rows, a toast and a status scene" src="images/foundation-feedback-light.png" width="720">
</picture>

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`Spinner`](../ui/foundation/README.md#feedback) | Work of unknown length | `feedback` | `ProgressShowcase` |
| [`ProgressBar`](../ui/foundation/README.md#feedback) | Work with a known total, or indeterminate | `feedback` | `ProgressShowcase` |
| [`StepIndicator`](../ui/foundation/README.md#feedback) | Which of a handful of steps the user is on | `feedback` | — |
| [`Skeleton`](../ui/foundation/README.md#feedback) | Placeholder bars while content loads | `feedback` | `SkeletonShowcase` |
| [`RequirementList`](../ui/foundation/README.md#feedback) | The rules a value must meet, ticked off as met | `feedback` | `RequirementListShowcase` |
| [`Fallback`](../ui/foundation/README.md#feedback) | Part of a screen standing in for missing content: empty, failed, offline | `feedback` | `FallbackShowcase` |
| [`ToastHost`](../ui/foundation/README.md#feedback) | Shows queued toasts one at a time | `feedback` | `ToastShowcase` |
| [`Toast`](../ui/foundation/README.md#feedback) | One toast's surface, drawn by the host | `feedback` | `ToastShowcase` |
| [`AnnounceOnChange`](../ui/foundation/README.md#feedback) | A sentence spoken by the screen reader when it appears or changes | `dev.voir.areca.foundation` | — |

## Notices and status

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`Notice`](../ui/foundation/README.md#notices-and-status) | A block set off from the page to say one thing | `notice` | `NoticeShowcase` |
| [`NoticeText`](../ui/foundation/README.md#notices-and-status) | A line of a notice's body | `notice` | `NoticeShowcase` |
| [`AnimatedNotice`](../ui/foundation/README.md#notices-and-status) | A notice present only while there is something to say | `notice` | `NoticeShowcase` |
| [`StatusScene`](../ui/foundation/README.md#notices-and-status) | A whole screen given to one piece of work: running, done or stopped | `status` | `StatusSceneShowcase` |
| [`StatusMark`](../ui/foundation/README.md#notices-and-status) | The disc a status scene shows | `status` | `StatusMarkShowcase` |
| [`StatusSceneActions`](../ui/foundation/README.md#notices-and-status) | A status scene's buttons | `status` | `StatusSceneShowcase` |

## Overlays

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="images/foundation-overlays-dark.png">
  <img alt="A dialog and a confirm sheet" src="images/foundation-overlays-light.png" width="720">
</picture>

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`ConfirmSheet`](../ui/foundation/README.md#overlays) | A yes-or-no question in a bottom sheet | `overlay` | `ConfirmSheetShowcase` |
| [`ConfirmationDialog`](../ui/foundation/README.md#overlays) | The same question as a centred dialog, with progress | `overlay` | `ConfirmationDialogShowcase` |
| [`Dialog`](../ui/foundation/README.md#overlays) | A centred window for a short task | `overlay` | `DialogShowcase` |
| [`FlowModal`](../ui/foundation/README.md#overlays) | A bottom sheet dragged between heights and away | `overlay` | `FlowModalShowcase` |
| [`FlowModalActionBar`](../ui/foundation/README.md#overlays) | A sheet's footer with its commit button | `overlay` | `FlowModalShowcase` |
| [`FlowModalSecondaryAction`](../ui/foundation/README.md#overlays) | The secondary button in a sheet's footer | `overlay` | `FlowModalShowcase` |
| [`DropdownPopup`](../ui/foundation/README.md#overlays) | A surface hanging off the control that opened it — a menu | `overlay` | — |

## Layout and surfaces

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`BaseLayout`](../ui/foundation/README.md#layout-and-surfaces) | A screen's frame with top and bottom bars | `layout` | `BaseLayoutShowcase` |
| [`CollapsingHeaderLayout`](../ui/foundation/README.md#layout-and-surfaces) | A header that scrolls away above a strip that stays | `layout` | `CollapsingHeaderLayoutShowcase` |
| [`PullToRefreshLayout`](../ui/foundation/README.md#layout-and-surfaces) | Pull-to-refresh around scrolling content (experimental) | `layout` | `PullToRefreshShowcase` |
| [`PullToRefreshIndicator`](../ui/foundation/README.md#layout-and-surfaces) | Its default indicator (experimental) | `layout` | `PullToRefreshShowcase` |
| [`Card`](../ui/foundation/README.md#layout-and-surfaces) | A surface grouping content, optionally pressable, expandable or with a footer | `layout` | `CardShowcase` |
| [`Expandable`](../ui/foundation/README.md#layout-and-surfaces) | A region that opens and closes under a trigger | `layout` | `ExpandableShowcase` |
| [`HorizontalDivider`](../ui/foundation/README.md#layout-and-surfaces) | A horizontal rule, optionally labelled | `layout` | `DividerShowcase` |
| [`VerticalDivider`](../ui/foundation/README.md#layout-and-surfaces) | A vertical rule | `layout` | `DividerShowcase` |
| [`RelativePositionLayout`](../ui/foundation/README.md#layout-and-surfaces) | A companion on one of eight sides of its content | `layout` | — |
| [`Overlay`](../ui/foundation/README.md#layout-and-surfaces) | A tint over a box's content without changing its size | `layout` | `BaseLayoutShowcase` |
| [`SystemBars`](theme.md) | The colour behind the status and navigation bars, for a region that is always dark | `dev.voir.areca.platform` | — |

## Slot rows

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`SlotRow`](../ui/foundation/README.md#slot-rows) | A start, centre and end row that lines up with every list row, button and bar | `slot` | `ThreeSlotRowShowcase` |

## Lists and settings

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="images/foundation-lists-dark.png">
  <img alt="A grouped list and a settings list" src="images/foundation-lists-light.png" width="720">
</picture>

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`List`](../ui/foundation/README.md#lists-and-settings) | A short grouped list, declared with `item(...)` | `list` | `ListShowcase` |
| [`LazyList`](../ui/foundation/README.md#lists-and-settings) | A long grouped list, composed lazily | `list` | `ListShowcase` |
| [`ListItemColumn`](../ui/foundation/README.md#lists-and-settings) | A group of rows you compose, with separators | `list` | `ListItemShowcase` |
| [`ListItem`](../ui/foundation/README.md#lists-and-settings) | A row with title, subtitle, caption, value, leading and trailing slots | `list` | `ListItemShowcase` |
| [`NavigationListItem`](../ui/foundation/README.md#lists-and-settings) | A row that opens something | `list` | `ListItemShowcase` |
| [`CheckboxListItem`](../ui/foundation/README.md#lists-and-settings) | A row that is a checkbox | `list` | `ListItemShowcase` |
| [`RadioListItem`](../ui/foundation/README.md#lists-and-settings) | A row that is a radio | `list` | `ListItemShowcase` |
| [`ToggleListItem`](../ui/foundation/README.md#lists-and-settings) | A row that is a toggle | `list` | `ListItemShowcase` |
| [`CheckmarkListItem`](../ui/foundation/README.md#lists-and-settings) | A row ticked when chosen | `list` | `ListItemShowcase` |
| [`ExpandableListItem`](../ui/foundation/README.md#lists-and-settings) | A row that discloses content under it | `list` | `ListItemShowcase` |
| [`ListItemSkeleton`](../ui/foundation/README.md#lists-and-settings) | A row-shaped placeholder | `list` | `ListItemShowcase` |
| [`ListItemChevron`](../ui/foundation/README.md#lists-and-settings) | The trailing chevron of a row you compose | `list` | `ListItemShowcase` |
| [`ListItemCheckmark`](../ui/foundation/README.md#lists-and-settings) | The trailing check mark of a row you compose | `list` | `ListItemShowcase` |
| [`Settings`](../ui/foundation/README.md#lists-and-settings) | A settings screen of sections | `settings` | `SettingsShowcase` |
| [`Section`](../ui/foundation/README.md#lists-and-settings) | One section of rows on its own card, with header, footer and band | `settings` | `SettingsShowcase` |
| [`SettingsRow`](../ui/foundation/README.md#lists-and-settings) | A settings row: a value, a navigation or an action | `settings` | `SettingsShowcase` |
| [`SettingsToggleRow`](../ui/foundation/README.md#lists-and-settings) | A settings row with a switch | `settings` | `SettingsShowcase` |
| [`SettingsSelectRow`](../ui/foundation/README.md#lists-and-settings) | A settings row choosing from a dropdown | `settings` | `SettingsShowcase` |
| [`SettingsOptionRow`](../ui/foundation/README.md#lists-and-settings) | One option of a set, as a settings row | `settings` | `SettingsShowcase` |
| [`SettingsTextFieldRow`](../ui/foundation/README.md#lists-and-settings) | A field typed in place, as a settings row | `settings` | `SettingsShowcase` |
| [`SettingsRowIcon`](../ui/foundation/README.md#lists-and-settings) | A row's leading glyph on a tile | `settings` | `SettingsShowcase` |
| [`SettingsRowTile`](../ui/foundation/README.md#lists-and-settings) | Any other leading visual, cropped to the tile | `settings` | `SettingsShowcase` |
| [`SettingsRowLink`](../ui/foundation/README.md#lists-and-settings) | A second action at the end of a pressable row | `settings` | `SettingsShowcase` |
| [`SettingsBlock`](../ui/foundation/README.md#lists-and-settings) | Free content on a section card, inset like a row | `settings` | `SettingsShowcase` |
| [`SettingsRowSkeleton`](../ui/foundation/README.md#lists-and-settings) | A settings-row-shaped placeholder | `settings` | `SettingsShowcase` |

## Collections

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`ContentList`](../ui/foundation/README.md#collections) | A set loaded in one go, with loading, empty and error faces | `collection` | `ContentListShowcase` |
| [`GroupedContentList`](../ui/foundation/README.md#collections) | The same, in labelled groups | `collection` | `ContentListShowcase` |
| [`PagedContentList`](../ui/foundation/README.md#collections) | A set loaded a page at a time, with pull-to-refresh | `collection` | `ContentListShowcase` |
| [`GroupedPagedContentList`](../ui/foundation/README.md#collections) | The same, in labelled groups | `collection` | `ContentListShowcase` |
| [`LazyVerticalGroupedGrid`](../ui/foundation/README.md#collections) | A vertical grid broken into labelled groups | `collection` | `GroupedGridShowcase` |
| [`LazyHorizontalGroupedGrid`](../ui/foundation/README.md#collections) | A horizontal grid broken into labelled groups | `collection` | `GroupedGridShowcase` |

## Tables

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`Table`](../ui/foundation/README.md#tables) | Rows and columns of data, composed in one pass | `table` | `TableShowcase` |
| [`LazyTable`](../ui/foundation/README.md#tables) | The same for a long set, composed on demand | `table` | `TableShowcase` |

## Navigation, tabs, tab bar and top bar

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="images/foundation-navigation-dark.png">
  <img alt="A top bar, a strip of tabs and a tab bar" src="images/foundation-navigation-light.png" width="400">
</picture>

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`TopBar`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | A screen's heading bar | `topbar` | `TopBarShowcase` |
| [`LargeTopBar`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | A large title that collapses into a `TopBar` | `topbar` | `LargeTopBarShowcase` |
| [`TopBarProgress`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | A hairline of progress along the bar's bottom edge | `topbar` | `TopBarShowcase` |
| [`Tabs`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | Tabs switching the content below: underlined, buttons or segmented. Built on `SlidingSelector` | `tabs` | `TabsShowcase` |
| [`TabBar`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | The application's destinations, docked to the bottom | `tabbar` | `TabBarShowcase` |
| [`FloatingTabBar`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | The same, floating above the bottom edge | `tabbar` | `FloatingTabBarShowcase` |
| [`FloatingTabBarItemContent`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | A floating tab's icon and optional label | `tabbar` | `FloatingTabBarShowcase` |
| [`NavList`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | A sidebar of destinations, with expandable sections | `navigation` | `NavListShowcase` |
| [`NavItem`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | One destination row, for a sidebar you compose yourself | `navigation` | `NavListShowcase` |
| [`SwipePager`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | Pages swiped sideways | `navigation` | — |
| [`PagerControls`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | A pager's dots and next button | `navigation` | — |
| [`PageIndicator`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | A pager's position as marks: elongated or plain dots, a shrinking window, a line or story segments; on the page or over a picture | `navigation` | `PageIndicatorShowcase` |
| [`PageCounter`](../ui/foundation/README.md#navigation-tabs-tab-bar-and-top-bar) | A pager's position as a count, "3 / 9" | `navigation` | `PageCounterShowcase` |

A top bar's controls — `BackAction` and the rest — are under
[Buttons and actions](#buttons-and-actions).

## Wheel picker

| Component | Use it for | Package | Playground |
| --- | --- | --- | --- |
| [`WheelPicker`](../ui/foundation/README.md#wheel-picker) | Wheels sharing one centre row | `wheel` | `WheelPickerShowcase` |
| [`WheelPickerColumn`](../ui/foundation/README.md#wheel-picker) | One wheel of items | `wheel` | `WheelPickerShowcase` |
| [`WheelPickerItem`](../ui/foundation/README.md#wheel-picker) | A wheel row's default text, for your own `itemContent` | `wheel` | `WheelPickerShowcase` |
| [`WheelPickerHighlight`](../ui/foundation/README.md#wheel-picker) | The band behind the centre row; the picker's default background | `wheel` | `WheelPickerShowcase` |
| [`WheelPickerEdgeFade`](../ui/foundation/README.md#wheel-picker) | The fade over the top and bottom rows; the picker's default foreground | `wheel` | `WheelPickerShowcase` |

## Other modules

- [Charts](../ui/charts/README.md) — `dev.voir.areca:charts`: bar, column, line and stacked bar
  charts, meter bars and legends.
- [Date and time](../ui/datetime/README.md) — `dev.voir.areca:datetime`: calendars, date ranges and
  time pickers.
- [Crop](../ui/crop/README.md) — `dev.voir.areca:crop`: an image cropping screen.
- [Keyboard](../ui/keyboard/README.md) — `dev.voir.areca:keyboard`: an on-screen keyboard.
- Flows — parts and recipes for whole screens: [auth](../flow/auth/README.md),
  [profile](../flow/profile/README.md) and [attachments](../flow/attachments/README.md).
- [Icons](../standalone/icons/README.md) — `dev.voir.areca:icons`: the `ArecaIcons.*` glyph set,
  usable without the theme.
