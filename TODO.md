# TODO

Open work before the first release. Paths are relative to
`ui/foundation/src/commonMain/kotlin/dev/voir/areca/` unless they start at the repository root.

## Release

- [ ] GitHub setup for releasing (see README "Releasing"): `RELEASE_BOT_TOKEN` secret,
      `RELEASE_BOT_NAME` / `RELEASE_BOT_EMAIL` variables, a `release` environment with reviewers, and
      a `main` ruleset requiring "Build and check whole repository" and "Build release artifacts".
      After the first release, set each published package's visibility in its package settings.

## Foundation: components

- [ ] `LargeTopBar` announces the title as a heading twice while expanded (the hidden compact title
      stays in the semantics tree). `Tabs` keeps stale `tabBounds` entries when the list shrinks.
- [ ] `Table` column measurements only grow; a column never narrows until the table leaves
      composition. `ContentListDefaults.LoadMoreErrorState` applies `fillMaxSize` inside a lazy item;
      `LimitedContentListState.refreshing` has no visual effect.
- [ ] `FlowModal` on iOS/desktop: CMP's `animateTransition` also scales the full-window content in;
      the Android dim is cleared one frame after the window opens.

## Charts

- [ ] `BarChartStyle.of(color, …)` builds styles where every other style uses `default(...)`.

## Date and time

- [ ] Spoken text in `MonthHeader`, `DateTimeField` and `DateRangeDay` is joined with a hard-coded
      `", "`; `TimePicker` hour and minute digits can't be localised through labels.
- [ ] `flow/profile`'s `BirthdayRow` passes `MONDAY` explicitly; let it follow `DateTimeLocale`.
- [ ] `DateTimeField` sets its placeholders two ways (`labels` and the `datePlaceholder` /
      `timePlaceholder` parameters); `CalendarScope` lacks the `expandMonthPicker` that
      `CalendarState` has.
- [ ] `TimePicker` reads `initialTime` once; a later change doesn't move the wheels (the README
      suggests `key(...)`). `MonthYearPicker` sizes come from `LocalCalendarSize.monthPicker`, not
      `LocalWheelPickerSize`.

## Keyboard

- [ ] `KeyboardScrim.default()` and `KeyboardStyle.default(divider = …)` repeat their constructors'
      defaults, which can drift apart.

## Crop

- [ ] `ImageCropState` has only an internal constructor, so consumers can't build one outside
      composition (for tests); `CropResult.render(outputSize = outputSize())` shadows the method.

## Auth flow

- [ ] Confirm Facebook's button blue against Meta's Brand Resource Center (`#0866FF` is kept; the
      developer Login UX page may still list `#1877F2`, which fails AA with white text).
- [ ] Foundation's `TextLinkPrompt` joins prompt and link with a hard-coded space, which
      `AuthFooterPrompt` and the resend prompt inherit.

## Profile flow

- [ ] The profile sheets (`SignOutSheet` and the others) take no `modifier`, though foundation's
      `ConfirmSheet` does.
- [ ] Compiler warnings: unnecessary safe call at `BirthdayRow.kt:220`, division by zero in
      `UploadPercentTest.kt:28`.

## Attachments flow

- [ ] `DownloadProgress` fill comes from `ArecaTheme.colors.bgContrast` rather than the style.
- [ ] `AttachmentPdfDocument.close()` changes the inherited contract (it may arrive during a
      `render`) without redeclaring the member with its own KDoc.

## Icons

- [ ] Review the drawings that only approximate their names: `Atm`, `BankNoteDollar`,
      `BarChartSquare`, `Box`, `Cheque`, `CoinsSwap`, `CreditCardLock`, `CreditCardShield`,
      `CreditCardRefresh`, `Cryptocurrency`, `Dataflow`, `GlobeSlated`, `Gold`, `Insurance`,
      `LineChartDown`, `Loan`, `OnlineBanking`, `Passport`, `Podcast`, `ReceiptCheck`, `Signal`,
      `Stockmarket`, `FlagWave`, `Tv`.
- [ ] Check the `SocialApple` and `SocialFacebook` geometry against the providers' official assets.
- [ ] Naming: the zero-padded names apps use (`Attachment02`, `Flag01`, `ShoppingBag03`, `Menu01`,
      `MagicWand01`) and the unused `Home01` / `MagicWand02` still break the suffix scheme.

## Documentation

- [ ] Playground pages for `PasswordField`, `SearchField`, `StepIndicator`, `Spinner`, `SwipePager` /
      `PageIndicator` / `PagerControls`, `DropdownPopup`, `BadgeSelect`, `SwatchPicker`, `Heading`,
      and the typography, spacing, radius, elevation and motion tokens.
