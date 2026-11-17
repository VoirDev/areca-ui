# TODO

Open work. Paths are relative to `ui/foundation/src/commonMain/kotlin/dev/voir/areca/` unless they
start at the repository root.

## Missing pieces

- [ ] `AmountText` draws the symbol and the figures as separate texts. Check a long amount with an
      RTL symbol, `maxLines = 1` and `autoSize` on an Android and an iOS device.
- [ ] `PanelLayout` cannot hold a lazy list and has no sticky header; `GroupedContentList` (Inset)
      composes every row at once and pins its header; a header and footer that scroll with the
      groups.
- [ ] A nested `BaseLayout` does not inherit the bottom inset of an outer floating bar.
- [ ] `DateTile` without a weekday; datetime `DateTimeField` that opens two sheets instead of a
      dropdown; a full-screen loading face; a field-fill resolver usable by custom field-like controls.
- [ ] `ListScope.item` / `LazyList` rows take no per-item modifier from the lazy item scope, so a
      row cannot `animateItem()` or carry drag-to-reorder; no reorderable list (drag, edge scroll,
      move actions).
- [ ] Selection: a `SelectOption` cannot hold a control of its own (a favourite star); no row with a
      tri-state or ordered mark; `SelectFlowModal` cannot pin the chosen option first.
- [ ] A settings `Section` header on a sheet is louder than the quiet labels above a sheet's other
      controls.
- [ ] `ExpandableListItemContainer.Soft` draws its block only while open; `ListItem`'s overline is
      SemiBold, heavier than a quiet label.
- [ ] Pickers: no always-on "create" row before anything is typed; a section header cannot hold an
      action.
- [ ] `ChoiceCard` has no style options (white with a hairline unselected, brand tint and tick
      selected); its badge text is regular weight.
- [ ] Not yet in Areca: an amount input on the keyboard module, income/expense tokens, `StarRating`,
      `ExpandableText`, a localisable metadata line.
- [ ] Android phones without a predefined key-click effect stay silent on a key tap; a vibrator
      fallback would need `VIBRATE`.

## Foundation: components

- [ ] English defaults that do not read `ArecaLabels` yet: `AddAction`, `MenuAction`,
      `NotificationsAction`, `FilterAction`, `EditAction`, `SettingsAction`, `ShareAction`,
      `DownloadAction`, `DeleteAction`, `FavoriteAction`, `DoneAction`, `AvatarAction`,
      `FloatingSidebar`, `AdaptiveNavigation.sidebarToggleDescription`, `Modifier.contextMenu` and
      `FieldGroup.requiredDescription`.
- [ ] `SettingsRowSkeleton` and `ListItemSkeleton` cannot turn their shimmer off, as
      `SkeletonBlock(animated = false)` can; a settings row's `bottom` slot starts under the leading
      tile while its hairline starts at the text.
- [ ] `EdgeColumnStyle.default(surface = colors.bgSurface)` reads a fixed ground where the other
      docked bars read `surfaces.bar`. `BottomActionBar` builds its button styles on the page, not
      on its bar (soft neutral buttons in it would take the page's well).
- [ ] `autoHeight` on `rememberFlowModal` and the `FlowModalController` constructor is ignored
      without a deprecation warning (only the property is `@Deprecated`); remove it in 2.0.
- [ ] Adaptive shell: the edge column's tooltips open below their anchor (`AnchoredPlacement` has no
      side placement); the shell does not catch the platform's back gesture while the sidebar is
      open (the app routes it to `onSidebarVisibleChange(false)`).

- [ ] `LargeTopBar` announces the title as a heading twice while expanded (the hidden compact title
      stays in the semantics tree).
- [ ] `Table` column measurements only grow; a column never narrows until the table leaves
      composition. `ContentListDefaults.LoadMoreErrorState` applies `fillMaxSize` inside a lazy item;
      `LimitedContentListState.refreshing` has no visual effect.
- [ ] `FlowModal` on iOS/desktop: CMP's `animateTransition` also scales the full-window content in;
      the Android dim is cleared one frame after the window opens.
- [ ] Segmented `Tabs` are 36dp tall, under the 48dp touch target, like the other `Tabs` variants.
- [ ] `CollapsingHeaderState` is not saved across configuration change.

## Charts

- [ ] `BarChartStyle.of(color, …)` builds styles where every other style uses `default(...)`.
- [ ] `ColumnChart` and `LineChart` draw no negative values (net cash flow, a balance below zero).
- [ ] `ColumnChart` does not keep its scroll position when older columns are prepended.
- [ ] `LineChart` has no point selection (scrubbing) and no horizontal reference line beside its grid.
- [ ] `MeterBar` clamps at the end of its track; an amount past it (an overspend) has no own style.

## Date and time

- [ ] Spoken text in `MonthHeader`, `DateTimeField`, `DateRangeDay` and `CalendarHeatDay` is joined
      with a hard-coded `", "`; `TimePicker` hour and minute digits can't be localised through labels.
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

- [ ] The module sheets (`AttachmentSheetFrame`, `AvatarSourceSheet`, `BirthdayRow`'s sheet) build
      their header from `TopBar(TopBarSize.Modal)` rather than `FlowModalTopBar`; the frame keeps a
      private `rememberLastNonNull` beside foundation's public one. `AttachmentRow` and
      `AttachmentTileStyle` read fixed grounds (`bgMuted`, `bgSurface`) rather than surface roles.
- [ ] `AttachmentImageLoader`, `AttachmentImageState` and `LocalAttachmentImageLoader` are deprecated
      in favour of media's loader and still read as a fallback; remove them in a later release.
- [ ] The viewer's filmstrip (`AttachmentFilmstrip`) and media's `ThumbnailStrip` do the same job
      with different looks — the current file grown rather than ringed.
- [ ] `DownloadProgress` fill comes from `ArecaTheme.colors.bgContrast` rather than the style.
- [ ] `AttachmentPdfDocument.close()` changes the inherited contract (it may arrive during a
      `render`) without redeclaring the member with its own KDoc.

## Media

- [ ] `ThumbnailStrip.centreOn` reads the viewport width in a `LaunchedEffect`; before the strip is
      laid out (width 0) it pushes the chosen thumbnail half off the start edge. Seen in headless
      rendering; check on a device.
- [ ] Pinch, peek, swipe-down-to-close and the open/close transition are covered by JVM UI tests
      only; none has been checked on an Android or iOS device.
- [ ] The viewer's transition assumes its dialog window and the screen's window share coordinates
      (both edge to edge). Unverified on iOS, where a source frame's bounds may be offset.
- [ ] `ZoomBehavior.Peek` lifts the picture into a full-window `Popup`; on Android that popup window
      may take the touches that follow the gesture until it is removed.

## Gallery flow

- [ ] The playground has no desktop device size, so the desktop product page renders in a plain
      window-sized frame rather than `DeviceFrame`.

## Icons

- [ ] Review the drawings that only approximate their names: `Atm`, `BankNoteDollar`,
      `BarChartSquare`, `Box`, `Cheque`, `CoinsSwap`, `CreditCardLock`, `CreditCardShield`,
      `CreditCardRefresh`, `Cryptocurrency`, `Dataflow`, `GlobeSlated`, `Gold`, `Insurance`,
      `LineChartDown`, `Loan`, `OnlineBanking`, `Passport`, `Podcast`, `ReceiptCheck`, `Signal`,
      `Stockmarket`, `FlagWave`, `Tv`.
- [ ] Check the `SocialApple` and `SocialFacebook` geometry against the providers' official assets.
- [ ] Naming: the zero-padded names apps use (`Attachment02`, `Flag01`, `ShoppingBag03`, `Menu01`,
      `MagicWand01`) and the unused `Home01` / `MagicWand02` break the suffix scheme.

## Documentation

- [ ] Playground pages for `PasswordField` and `SearchField` (they appear only as variants on the
      Text field page), `StepIndicator`, `SwipePager` / `PagerControls`,
      `DropdownPopup`, `BadgeSelect`, and Foundations pages for the typography, spacing, radius,
      elevation and motion tokens.
