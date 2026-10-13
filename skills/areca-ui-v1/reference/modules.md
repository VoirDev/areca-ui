# Modules beyond foundation

Open this when a screen needs charts, image cropping, dates and times, an on-screen keyboard, the
icon set, or authentication, profile or attachment screens. Validated against the Areca 1.0.0
artifacts. Each module's README in the `VoirDev/areca-ui` repository is the full guide; this file says
when to add the module, what it brings, and the rules that are easy to get wrong.

Every module here except `icons` draws with the theme and needs the app's `ArecaThemeProvider`
above it. All artifacts share one version; keep them aligned.

| Artifact | Package | Passes `foundation` on | README |
| --- | --- | --- | --- |
| `dev.voir.areca:charts` | `dev.voir.areca.charts` | Yes (`api`) | `ui/charts/README.md` |
| `dev.voir.areca:crop` | `dev.voir.areca.crop` | No: add `foundation` yourself | `ui/crop/README.md` |
| `dev.voir.areca:datetime` | `dev.voir.areca.datetime` (`.calendar`, `.calendar.range`) | Yes, plus `kotlinx-datetime` | `ui/datetime/README.md` |
| `dev.voir.areca:keyboard` | `dev.voir.areca.keyboard` (`.layout`, `.key`) | Yes | `ui/keyboard/README.md` |
| `dev.voir.areca:icons` | `dev.voir.areca.icons` | Not theme-bound; depends on nothing in Areca | `standalone/icons/README.md` |
| `dev.voir.areca:flow-auth` | `dev.voir.areca.flow.auth` (`.part`, `.sso`) | Yes | `flow/auth/README.md` |
| `dev.voir.areca:flow-profile` | `dev.voir.areca.flow.profile` (`.part`, `.row`, `.session`, `.sheet`) | Yes, plus `datetime` | `flow/profile/README.md` |
| `dev.voir.areca:flow-attachments` | `dev.voir.areca.flow.attachments` (`.part`, `.row`, `.sheet`, `.viewer`) | Yes | `flow/attachments/README.md` |

## Icons

- No library module depends on `icons`; modules bundle the few glyphs they draw. Add
  `dev.voir.areca:icons` only when the app draws `ArecaIcons.*` itself; otherwise pass the app's own
  `ImageVector` or `Painter`.
- Each icon is an extension property and needs its own import:
  `import dev.voir.areca.icons.ArecaIcons` plus `import dev.voir.areca.icons.Trash`.
- Draw through foundation `Icon`, which tints with `LocalContentColor` and sizes with
  `LocalIconSize`. The full-colour marks `SocialGoogle` and `SocialFacebook` need
  `tint = Color.Unspecified`.
- Directional icons (arrows, `ChevronLeft`/`Right`, `LogIn`/`LogOut`, `Send`, `Share`) mirror in
  right-to-left layouts on their own; media transport, clocks and rotate arrows do not.

## Charts

`BarChart(groups, …)` draws grouped vertical bars against a scale; `StackedBarChart(segments, …)`
draws one horizontal bar split into parts of a whole. Types: `BarChartGroup`, `BarChartBar`,
`BarChartOptions`, `BarChartYAxisLabel`, `StackedBarSegment`.

- The chart draws numbers it is handed. Aggregate, round, choose tick values and format labels in
  the app; the scale is `BarChartOptions(maxValue = …)` or the largest value given.
- A chart is one accessibility node. Its generated description joins the data with English
  punctuation and unlocalised numbers, so pass a `valueFormatter` (bar) or `fractionFormatter`
  (stacked) for real values, and a `contentDescription` when the locale or the message needs a
  sentence ("Revenue rose every quarter").
- Restyle through `BarChartStyle` / `BarChartSize`, `StackedBarChartStyle` /
  `StackedBarChartSize` and their nullable locals, like foundation.

## Crop

The cropper frames a photo for an avatar, a cover or a post. Pieces: `CropSpec` (aspect, mask,
grips, output size), `rememberImageCropState(spec, key)`, the stage `ImageCropper`, the controls
`CropToolbar`, and `CropDialog`, a window that paints the system bars in the editor's black.

- Cropping belongs to the app, not to a flow. Write one crop screen (the README's quick start) and
  open it from every place that crops, each with that place's `CropSpec`, defined once as a
  top-level value or a `copy` of `CropSpec.Avatar` / `CropSpec.Photo`.
- Key the state on the photo: `rememberImageCropState(spec, key = photo)`.
- The module decodes and uploads nothing. Hand it an upright `Painter` with an intrinsic size; on
  Android decode without hardware bitmaps (Coil `allowHardware(false)`) and at no more than about
  4096 px on the long side, and pass the full-resolution painter to `state.render(…)` when Done is
  pressed.
- Use `CropShapes.Oval`, not `CircleShape`, for a round mask.
- `render` allocates the whole bitmap on the calling thread; cap it with `maxOutputDimension` and
  move large renders off the main thread.
- The cropper is always dark. Every word is in `CropToolbarLabels`; pass the app's labels to both
  `CropToolbar` and `ImageCropper`, as a stable value.

## Date and time

`Calendar`, `DateRangeCalendar`, `MonthYearPicker`, `TimePicker` and `DateTimeField` (a
foundation `FieldRow` with a date half and a time half, usable inside a `FieldGroup`).

- `kotlinx-datetime` (0.8.x) is part of the API: values are `LocalDate`, `LocalTime`,
  `LocalDateTime`, `YearMonth`. Keep the app on 0.8.x while it uses this module.
- The calendars and the field are controlled: they draw the selection given and report taps.
  `TimePicker` is the exception: `initialTime` seeds its wheels once, and `onTimeChange` fires when
  the wheels come to rest; key the picker to restart it.
- `DateTimeField`'s `value` is nullable but `onValueChange` always receives a whole
  `LocalDateTime`; a date picked on an empty field arrives at `defaultTime` (09:00 unless given).
- Week start and hour cycle come from `DateTimeLocale.Current`; pin a subtree with
  `LocalDateTimeLocale provides DateTimeLocale.Iso` in screenshot tests. Every word is in
  `CalendarLabels`, `DateRangeLabels`, `TimePickerLabels` and `DateTimeFieldLabels` (English
  defaults); the default field format is ISO digits, so a localised app passes its own
  `DateTimeFieldFormat`.
- Limit days with `CalendarDateRule` and `CalendarBounds`; limit ranges with
  `DateRangeConstraints`. Use `foundation`'s `WheelPicker` for scroll wheels that are not dates.

## Keyboard

An on-screen keyboard docked to the bottom of a screen, for amounts, PINs and codes. Layers:
`KeyboardHost` + `rememberKeyboardController()` + `rememberKeyboardTarget(key)`; the chrome
`KeyboardLayout`; the keys `KeyboardGrids.decimal(onAction, decimalSeparator)` and friends.

- Put one `KeyboardHost` at the root of the screen; its `keyboardContent` receives the open target,
  so one host serves every input. Give targets saveable keys so an open keyboard survives rotation.
- Keys report a `KeyAction`; the app applies it to its own state. The module never edits text.
- Mark a pressable display with `Modifier.keyboardTarget(controller, target, onClickLabel = …)`.
  For a real text field, keep the system IME down with `readOnly = true` and
  `Modifier.keyboardAnchor(…)`, or with `InterceptPlatformTextInput`; `showKeyboardOnFocus = false`
  alone is not enough.
- `compressContent = true` pads the screen by the keyboard's height; use `false` when the screen
  has a docked action bar. Pass the app's `paneTitle` (announced on open) or `null`.

## Flow modules

`flow-auth`, `flow-profile` and `flow-attachments` ship parts, a scaffold and README recipes, not
screens. They draw and report intents; the app owns state, validation, wording of complaints,
networking, navigation, DI and image loading.

- Copy the README recipe for the screen and edit it. Do not wrap the parts in a configurable
  `SignInScreen`-style composable.
- Pass the app's localised words to every word parameter; required words default to English and
  optional ones to `null`.
- Pass complaints as text (`error: String?`); the parts never validate.
- Pass only the options the platform can perform: providers in `SsoGroup`, picture sources in
  `AvatarSourceSheet`, file sources in `AttachmentSourceSheet`.
- Flows never depend on one another, on a navigation library, or on `dev.voir.areca:viewmodel`.
  Sequence steps in the app's navigation and feed the parts from the app's ViewModel state.

| Module | Use it for | Start with |
| --- | --- | --- |
| `flow-auth` | Sign in, sign up, email-first, verify a code, reset links, provider buttons | `AuthScaffold` + `AuthBranding` (layout `Plain`, `HeroTop`, `HeroBottom`, `HeroInline`, `FullBleed`), `AuthHeader`, `AuthEmailField` / `AuthPasswordField` / `AuthNameField`, `AuthCodeField`, `AuthPasswordRequirements`, `AuthResendCode`, `AuthBanner`, `AuthProgress`, `LegalConsent`, `SsoGroup` |
| `flow-profile` | Profile, edit profile, username, account menu and switcher, devices, photo change | `ProfileHeaderCard` / `ProfileCoverCard` / `ProfileSummaryCard`, rows (`ProfileInfoRow`, `EmailRow`, `BirthdayRow`, `LinkedAccountRow`, `SignOutRow`, `DeleteAccountRow`) inside foundation `Settings` sections, `ActiveSessionsSections`, sheets (`AvatarSourceSheet`, `SignOutSheet`, `DeleteAccountSheet`, …) |
| `flow-attachments` | Files on a form or a record, adding a file, acting on one, a viewer | `AttachmentItem` + `AttachmentFormat.of(…)` + `AttachmentState`, `AttachmentStrip` / `AttachmentRow`, `AttachmentSourceSheet`, `PendingAttachmentSheet`, `AttachmentOptionsSheet`, `AttachmentViewer` |

- Images go through a loader interface the app implements once over its own library:
  `LocalAvatarImageLoader` (profile), `LocalAttachmentImageLoader` and `LocalAttachmentPdfRenderer`
  (attachments). Unprovided, pictures fall back to initials or glyphs.
- `LocalAttachmentTileStyle` and `LocalAttachmentViewerStyle` are not seeded by
  `ArecaThemeProvider`; provide them yourself to change the default.
- Cropping before an upload is the app's crop screen, opened from `EditableAvatar`'s
  `onChangePhoto` or `PendingAttachmentSheet`'s `onEdit`.
