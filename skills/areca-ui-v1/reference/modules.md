# Modules beyond foundation

Open this when a screen needs charts, dates and times, cropping, an on-screen keyboard, the icon set,
or authentication, profile or attachment screens. It says which artifact to add and which reference
to read next; each reference lists every public composable of its module with exact signatures,
rules and recipes.

## Artifacts

| Artifact | Package(s) | Passes `foundation` on | Reference |
| --- | --- | --- | --- |
| `dev.voir.areca:charts` | `dev.voir.areca.charts` | Yes (`api`) | [charts.md](charts.md) |
| `dev.voir.areca:datetime` | `dev.voir.areca.datetime`, `.calendar`, `.calendar.range` | Yes (`api`), plus `kotlinx-datetime` 0.8.x (`api`) | [datetime.md](datetime.md) |
| `dev.voir.areca:crop` | `dev.voir.areca.crop` | **No**: declare `dev.voir.areca:foundation` yourself | [crop.md](crop.md) |
| `dev.voir.areca:keyboard` | `dev.voir.areca.keyboard`, `.layout`, `.key` | Yes (`api`) | [keyboard.md](keyboard.md) |
| `dev.voir.areca:icons` | `dev.voir.areca.icons` | Not theme-bound; depends on nothing in Areca | [icons.md](icons.md) |
| `dev.voir.areca:flow-auth` | `dev.voir.areca.flow.auth`, `.part`, `.sso` | Yes (`api`) | [flow-auth.md](flow-auth.md) |
| `dev.voir.areca:flow-profile` | `dev.voir.areca.flow.profile`, `.part`, `.row`, `.session`, `.sheet` | Yes (`api`), plus `datetime` (`api`) | [flow-profile.md](flow-profile.md) |
| `dev.voir.areca:flow-attachments` | `dev.voir.areca.flow.attachments`, `.part`, `.row`, `.sheet`, `.viewer` | Yes (`api`) | [flow-attachments.md](flow-attachments.md) |

```kotlin
commonMain.dependencies {
    implementation("dev.voir.areca:foundation:<version>")
    implementation("dev.voir.areca:charts:<version>")   // one line per module the app uses, all at one version
}
```

## When to add each

- **charts** — grouped bars, a split of a whole, a period picker made of columns, a running total,
  an amount against its usual or a target, and the legend for any of them.
- **datetime** — a calendar (one day, many, a range), a heat-map month, month tiles, a date tile, a
  time picker, or a date-time field in a form.
- **crop** — framing a photo before it is saved or uploaded: an avatar, a cover, a post.
- **keyboard** — an on-screen keyboard docked to the screen's bottom for amounts, PINs, codes or a
  calculator, instead of the platform keyboard.
- **icons** — only when the app itself draws `ArecaIcons.*`; otherwise pass the app's own
  `ImageVector` or `Painter`.
- **flow-auth** — sign in, sign up, email-first, verify a code, pages an emailed link opens, provider
  buttons.
- **flow-profile** — profile, edit profile, username, account menu and switcher, sign-in methods, data
  export, devices, change photo, sign out, delete account.
- **flow-attachments** — files on a form or a record, adding a file, acting on one, a full-screen
  viewer.

## Cross-module rules

- **One version.** Every Areca artifact is released together; keep them all at the same version.
- **One theme.** Every module except `icons` reads `ArecaTheme`; the app's single
  `ArecaThemeProvider` at the root covers them all.
- **Module locals are not seeded.** `ArecaThemeProvider` lives in `foundation` and seeds only
  foundation's locals. The modules' own `LocalXStyle` / `LocalXSize` (`LocalBarChartStyle`,
  `LocalCalendarStyle`, `LocalCalendarHeatStyle`, `LocalMonthGridSize`, `LocalImageCropperStyle`,
  `LocalKeyboardStyle`, `LocalAttachmentTileStyle`, `LocalAttachmentViewerStyle`, …) are `null` until
  the app provides them, and each component then falls back to its type's `Default`. Provide them
  with `CompositionLocalProvider` just inside the theme provider for an app-wide default.
- **Same customisation model as foundation.** Each component takes `style` and `size` defaulting to
  `LocalX.current ?: X.Default`; styles have `default(…)` factories whose parameters are theme tokens,
  built inside composition; change one value with `copy(…)`.
- **Icons are opt-in.** No module depends on `icons`; each bundles the few glyphs it draws. An icon
  needs two imports: `dev.voir.areca.icons.ArecaIcons` and the icon itself
  (`dev.voir.areca.icons.Trash`).
- **Words are parameters.** Modules ship English defaults for required words (in labels objects —
  `CalendarLabels`, `CropToolbarLabels`, `KeyboardGridLabels`, … — or per-part parameters in flows)
  and `null` for optional ones. A production app passes its own strings for every one, as top-level
  values where they are objects or lambdas.
- **Always-dark regions.** The cropper and the attachment viewer are dark whatever the theme and paint
  the system bars themselves (`CropDialog`, the viewer's window); do not wrap them in a dark provider.
  For a navigation destination of your own, use foundation `SystemBars(color)`.
- **No module loads images, decodes, uploads or renders PDFs.** Flows read pictures through a loader
  the app implements once over its image library — `LocalAvatarImageLoader` (profile),
  `LocalAttachmentImageLoader` and `LocalAttachmentPdfRenderer` (attachments). Unprovided, pictures
  fall back to initials or file glyphs.
- **Cropping belongs to the app.** Write one crop screen with `crop` and open it from every place
  that crops, each with its own `CropSpec`: `EditableAvatar`'s `onChangePhoto` (profile) and
  `PendingAttachmentSheet`'s `onEdit` (attachments). Flows never depend on `crop`.

### Flow modules

- They ship **parts, a scaffold and recipes, not screens**. Copy the recipe from the flow's reference
  and edit it; never wrap the parts in a configurable `SignInScreen` / `ProfileScreen` /
  `AttachmentsScreen`.
- They draw and report intents (`onSubmit`, `onProviderClick(provider)`, `onOpen(item)`). The app owns
  form state, validation, the wording of complaints, networking, storage, sessions, navigation and DI.
- Complaints arrive as worded text (`error: String?`, `failure`, `problem`, `complaint`); parts never
  validate.
- Offer only what the platform can perform: providers in `SsoGroup`, picture sources in
  `AvatarSourceSheet`, file sources in `AttachmentSourceSheet`.
- Flows never depend on each other, on a navigation library, on DI or on `dev.voir.areca:viewmodel`.
  Sequence steps in the app's navigation and feed the parts from the app's presentation state.
- Their sheets are state-driven (`visible`, or the item they are about), report `onDismiss`, and are
  composed unconditionally so they animate out.
