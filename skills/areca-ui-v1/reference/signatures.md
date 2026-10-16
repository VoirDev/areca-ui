# Signatures and packages

Open this when calling a foundation component whose parameters you are not sure of, or when an
import does not resolve. Validated against `dev.voir.areca:foundation` 1.0.0.

All functions below are `@Composable public fun` unless they are a `Modifier` extension or a state
factory, and list their parameters in source order. Call with named arguments after the leading
required ones. Comments name the other overloads. Anything not listed here: read the KDoc of the
declaration, which documents every parameter and its default.

## Packages

Every foundation symbol is imported from its family's package. Extension composables (`Section`,
`BackAction`, `TextSegment`, …) are imported by name like any top-level function.

| Package | Holds |
| --- | --- |
| `dev.voir.areca.theme` | `ArecaThemeProvider`, `ArecaDarkTheme`, `ArecaTheme`, `contentInsets`, `horizontalContentInsets`, `verticalContentInsets`, `tween` |
| `dev.voir.areca.foundation` | `LocalContentColor`, `LocalTextStyle`, `AnnounceOnChange` |
| `….foundation.button` | `Button`, `IconButton`, `ButtonStyle`, `ButtonSize`, `IconButtonStyle`, `IconButtonSize`, `ActionColor`, `LocalButtonStyle`, `LocalButtonSize` |
| `….foundation.text` | `Text`, `TextVariant`, `TextColor`, `Heading`, `TextLink`, `TextLinkPrompt`, `SensitiveText` |
| `….foundation.icon` | `Icon`, `IconSize`, `LocalIconSize`, `FeaturedIcon`, `FeaturedIconStyle`, `FeaturedIconSize`, `Chevron` |
| `….foundation.field` | `TextField`, `PasswordField`, `SearchField`, `AffixTextField`, `OtpField`, `FieldGroup`, `FieldRow`, `TextSegment`, `Field`, `FieldStatus`, `FieldStyle`, `FieldSize`, `FieldDefaults`, `LocalFieldStyle`, `FieldLabelPlacement` |
| `….foundation.field.select` | `SelectField`, `SelectOption`, `SelectSelectionMode`, `SelectFieldLabels`, `SelectFlowModal`, `SelectSegment` |
| `….foundation.selection` | `Checkbox`, `Radio`, `Toggle`, `ToggleState`, `BinaryToggleCycle`, `TriStateToggleCycle`, `NumberStepper`, `Swatch`, `SwatchPicker` |
| `….foundation.list` | `List`, `LazyList`, `ListItem`, `NavigationListItem`, `CheckboxListItem`, `RadioListItem`, `ToggleListItem`, `CheckmarkListItem`, `ExpandableListItem`, `ListItemColumn`, `ListItemSkeleton`, `ListItemChevron`, `ListItemStyle`, `ListItemSize` |
| `….foundation.settings` | `Settings`, `Section`, `SettingsRow`, `SettingsToggleRow`, `SettingsSelectRow`, `SettingsTextFieldRow`, `SettingsOptionRow`, `SettingsBlock`, `SettingsRowIcon`, `SettingsRowTile`, `SettingsRowLink`, `SettingsRowStatus`, `SettingsRowTone`, `SettingsRowSkeleton`, `SettingsDefaults` |
| `….foundation.layout` | `BaseLayout`, `Card`, `CardStyle`, `CardSize`, `CardFooterStyle`, `HorizontalDivider`, `VerticalDivider`, `Expandable`, `PullToRefreshLayout`, `CollapsingHeaderLayout`, `Overlay`, `RelativePositionLayout` |
| `….foundation.topbar` | `TopBar`, `LargeTopBar`, `rememberTopBarScrollState`, `TopBarScrollBehavior`, `TopBarStyle`, `TopBarSize`, `TopBarActionStyle`, the `…Action` controls, `TopBarProgress` |
| `….foundation.tabbar`, `….foundation.tabs` | `TabBar`, `FloatingTabBar`, `TabBarItem`, `rememberTabBarState`; `Tabs`, `TabItem`, `TabsVariant`, `rememberTabsState` |
| `….foundation.overlay` | `Dialog`, `ModalDefaults`, `ConfirmationDialog`, `ConfirmSheet`, `FlowModal`, `rememberFlowModal`, `FlowModalController`, `FlowModalActionBar`, `DropdownPopup`, `rememberDropdownPopup`, `anchored`, `edgeToEdgeDialogProperties` |
| `….foundation.feedback` | `ToastHost`, `rememberToastState`, `Toast`, `Fallback`, `Skeleton`, `Spinner`, `ProgressBar`, `StepIndicator`, `RequirementList` |
| `….foundation.notice`, `….foundation.status` | `Notice`, `NoticeText`, `NoticeTone`, `NoticeEmphasis`, `AnimatedNotice`; `StatusScene`, `StatusSceneActions`, `StatusMark`, `StatusMarkState` |
| `….foundation.badge`, `….foundation.avatar` | `Badge`, `BadgeStyle`, `NotificationBadge`, `IndicatorDot`, `BadgeSelect`, `LazyBadgeSelect`, `FlowBadgeSelect`; `Avatar` |
| `….foundation.collection`, `….foundation.table` | `ContentList`, `PagedContentList`, `GroupedContentList`, `GroupedPagedContentList`, `LimitedContentListState`, `PagedContentListState`, `ContentListLabels`, `ContentListDefaults`, `LazyVerticalGroupedGrid`, `LazyHorizontalGroupedGrid`; `Table`, `LazyTable`, `TableColumn` |
| `….foundation.navigation`, `….foundation.wheel`, `….foundation.slot` | `NavList`, `NavItem`, `SwipePager`, `PagerControls`, `PageIndicator`, `rememberPagerController`; `WheelPicker`, `WheelPickerColumn`, `rememberWheelPickerColumnState`; `SlotRow`, `SlotRowLayout` (`Default`, `Auto`, `Center`, `Top`, `FirstLine`, `Centered`, `Fill`, `wrap(…)`), `SlotRowAlignment`, `Modifier.firstLineCenter(textStyle)`, `FirstLineCenter` |
| `dev.voir.areca.indication`, `.modifier`, `.platform`, `.shape` | `TouchIndication`, `FadeIndication`, `StrokeIndication`, `LiquidSweepIndication`, `FocusOutline`; `clickCursor`, `minimumTouchTarget`, `thenIf`; `SystemBars`; `SquircleShape` |
| `dev.voir.areca` | `ExperimentalArecaApi` |

## Theme

```kotlin
fun ArecaThemeProvider(darkTheme: Boolean = false, baseColors: ArecaBaseColors = ArecaBaseColors(),
    colors: ArecaColors =
        if (darkTheme) { ArecaColors.dark(baseColors) } else { ArecaColors.light(baseColors) },
    radius: ArecaRadius = ArecaRadius(),
    shapes: ArecaShapes = ArecaShapes.from(radius), spacing: ArecaSpacing = ArecaSpacing(),
    elevation: ArecaElevation = ArecaElevation(), motion: ArecaMotion = ArecaMotion(),
    fonts: ArecaFonts = ArecaFonts(),
    baseTypography: ArecaBaseTypography = ArecaBaseTypography.from(fonts),
    typography: ArecaTypography = ArecaTypography.from(baseTypography),
    content: @Composable () -> Unit)

fun ArecaDarkTheme(content: @Composable () -> Unit)

fun Modifier.horizontalContentInsets(): Modifier   // also verticalContentInsets(), contentInsets()
fun <T> Duration.tween(easing: Easing = FastOutSlowInEasing): TweenSpec<T>
fun AnnounceOnChange(text: String?)
fun SystemBars(color: Color, lightIcons: Boolean = color.luminance() < 0.5f) // Android only acts
fun Modifier.minimumTouchTarget(minimum: Dp = MinimumTouchTarget): Modifier
fun Modifier.clickCursor(enabled: Boolean = true): Modifier
```

## Buttons, text, icons

```kotlin
fun Button(text: String, onClick: (() -> Unit)?, modifier: Modifier = Modifier,
    style: ButtonStyle = LocalButtonStyle.current ?: ButtonStyle.solid(),
    size: ButtonSize = LocalButtonSize.current ?: ButtonSize.Medium,
    textStyle: TextStyle = size.textStyle, textAlign: TextAlign = TextAlign.Center,
    layout: SlotRowLayout = SlotRowLayout.wrap(startGap = size.gap, endGap = size.gap),
    enabled: Boolean = true, loading: Boolean = false, loadingStateDescription: String = "Busy",
    interactionSource: MutableInteractionSource? = null, leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null)
// Overload: no `text`; a trailing `content: @Composable () -> Unit` instead.
// ButtonStyle.solid / soft / outlined / plain(color: ActionColor = Brand, shape = shapes.button)
// ButtonSize.ExtraSmall | Small | Medium | Large | ExtraLarge

fun IconButton(imageVector: ImageVector, contentDescription: String?, onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    style: IconButtonStyle = LocalIconButtonStyle.current ?: IconButtonStyle.solid(),
    size: IconButtonSize = LocalIconButtonSize.current ?: IconButtonSize.Medium,
    enabled: Boolean = true, loading: Boolean = false, loadingStateDescription: String = "Busy",
    interactionSource: MutableInteractionSource? = null)
// Overloads: `imageBitmap: ImageBitmap` or `painter: Painter` in place of `imageVector`; or no
// image and no `contentDescription`, with a trailing `content: @Composable BoxScope.() -> Unit`.
// IconButtonSize.Tiny | ExtraSmall | Small | Medium | Large | ExtraLarge

fun Text(text: CharSequence, variant: TextVariant, modifier: Modifier = Modifier,
    color: TextColor = TextColor.Base, fontSize: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified, fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null, fontStyle: FontStyle? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified, textDecoration: TextDecoration? = null,
    textAlign: TextAlign = TextAlign.Unspecified, overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true, maxLines: Int = Int.MAX_VALUE, minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null)
// Overload for slots: `style: TextStyle = LocalTextStyle.current`,
// `color: Color = LocalContentColor.current` in place of `variant` and `TextColor`.

fun Heading(title: @Composable () -> Unit, modifier: Modifier = Modifier,
    style: HeadingStyle = LocalHeadingStyle.current ?: HeadingStyle.Default,
    size: HeadingSize = LocalHeadingSize.current ?: HeadingSize.Medium,
    supportive: (@Composable () -> Unit)? = null, content: (@Composable () -> Unit)? = null)

fun TextLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    enabled: Boolean = true, color: TextColor = TextColor.Brand,
    variant: TextVariant = TextVariant.LabelMedium, fontWeight: FontWeight? = null,
    decoration: TextLinkDecoration = TextLinkDecoration.None)

fun TextLinkPrompt(prompt: String, linkText: String, onLinkClick: () -> Unit,
    modifier: Modifier = Modifier, color: TextColor = TextColor.Subtle,
    linkColor: TextColor = TextColor.Brand, variant: TextVariant = TextVariant.BodySmall,
    decoration: TextLinkDecoration = TextLinkDecoration.None, textAlign: TextAlign = TextAlign.Center)

fun SensitiveText(text: String, modifier: Modifier = Modifier, revealed: Boolean = false,
    onRevealedChange: ((Boolean) -> Unit)? = null, variant: TextVariant = TextVariant.BodyMedium,
    color: TextColor = TextColor.Base, …, concealedDescription: String = "Hidden", …)

fun Icon(imageVector: ImageVector, contentDescription: String?, modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current, size: Dp = LocalIconSize.current)
// Overloads: `painter: Painter` or `bitmap: ImageBitmap` in place of `imageVector`.

fun FeaturedIcon(imageVector: ImageVector, contentDescription: String?,
    modifier: Modifier = Modifier,
    style: FeaturedIconStyle = LocalFeaturedIconStyle.current ?: FeaturedIconStyle.Default,
    size: FeaturedIconSize = LocalFeaturedIconSize.current ?: FeaturedIconSize.Medium,
    shape: Shape = ArecaTheme.shapes.md)
// Overload: `painter: Painter`. FeaturedIconStyle.solid / soft / outlined(color: ActionColor).
```

## Fields

```kotlin
fun FieldGroup(modifier: Modifier = Modifier, label: CharSequence? = null,
    required: Boolean = false, requiredText: String = "*", requiredDescription: String = "required",
    assistive: CharSequence? = null, error: CharSequence? = null,
    status: FieldStatus = if (error != null) FieldStatus.Error else FieldStatus.Default,
    style: FieldStyle = FieldDefaults.style, size: FieldSize = FieldDefaults.size,
    labelPlacement: FieldLabelPlacement = FieldLabelPlacement.Above,
    messagePlacement: FieldMessagePlacement = FieldMessagePlacement.Below,
    messageBehavior: FieldMessageBehavior = FieldMessageBehavior.Animated,
    labelSpace: Dp = size.label.gap, messageSpace: Dp = size.message.gap, enabled: Boolean = true,
    content: @Composable (field: FieldScope) -> Unit)

fun TextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    format: ((String) -> String)? = null, style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size, field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true, readOnly: Boolean = false, loading: Boolean = false,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false, maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1, cursorBrush: Brush = SolidColor(ArecaTheme.colors.accentBase),
    interactionSource: MutableInteractionSource? = null)
// Overload: `value: TextFieldValue`, `onValueChange: (TextFieldValue) -> Unit`, no `format`.

fun PasswordField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    format: ((String) -> String)? = null, style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size, field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true, readOnly: Boolean = false, loading: Boolean = false,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    revealContentDescription: String = "Show password",
    hideContentDescription: String = "Hide password",
    cursorBrush: Brush = SolidColor(ArecaTheme.colors.accentBase),
    interactionSource: MutableInteractionSource? = null)

fun SearchField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    style: FieldStyle = FieldDefaults.style, size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null, status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true, readOnly: Boolean = false, loading: Boolean = false,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    onSearch: (KeyboardActionScope.() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions(onSearch = onSearch),
    keyboardOptions: KeyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
    searchContentDescription: String = "Search", clearContentDescription: String = "Clear search")

fun AffixTextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    prefix: (@Composable () -> Unit)? = null, suffix: (@Composable () -> Unit)? = null,
    affixGap: Dp = ArecaTheme.spacing.xs2, format: ((String) -> String)? = null,
    alwaysShowAffixes: Boolean = false, style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size, field: FieldScope? = null, …,
    placeholder: (@Composable () -> Unit)? = null, …)

fun OtpField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    length: Int = 6, onComplete: ((String) -> Unit)? = null,
    style: FieldStyle = FieldDefaults.style,
    size: OtpFieldSize = LocalOtpFieldSize.current ?: OtpFieldSize.Default,
    field: FieldScope? = null, status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true, readOnly: Boolean = false, masked: Boolean = false,
    stretch: Boolean = false, groupSize: Int? = null,
    separator: (@Composable () -> Unit)? = null, placeholder: Char? = null,
    filter: (Char) -> Boolean = Char::isDigit,
    keyboardOptions: KeyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done), …)

fun <Value> SelectField(values: Set<Value>, onChange: (Set<Value>) -> Unit,
    options: List<SelectOption<Value>>, modifier: Modifier = Modifier,
    selectionMode: SelectSelectionMode = SelectSelectionMode.Single,
    style: FieldStyle = FieldDefaults.style, size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null, status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true, readOnly: Boolean = false, loading: Boolean = false,
    empty: Boolean = values.isEmpty(), placement: AnchoredPlacement = AnchoredPlacement.BelowStart,
    dropdownStyle: DropdownPopupStyle =
        LocalDropdownPopupStyle.current ?: DropdownPopupStyle.Default,
    dropdownSize: DropdownPopupSize = LocalDropdownPopupSize.current ?: DropdownPopupSize.Default,
    labels: SelectFieldLabels = SelectFieldLabels.English,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.(expanded: Boolean) -> Unit)? = null,
    valueContent: (@Composable RowScope.(selectedEntries: List<SelectOption<Value>>) -> Unit)? =
        null,
    content: @Composable (options: List<SelectOption<Value>>, values: Set<Value>,
        onSelect: (SelectOption<Value>) -> Unit) -> Unit = { … SelectFlowModalDefaults.List(…) })
// SelectOption(key: Any, value: T, label: String, enabled: Boolean = true)
// SelectSelectionMode.Single | Multiple. In Single, choosing the selected option clears it.
```

Field looks are `FieldStyle.outlined()` (default), `filled()`, `soft()`, `solidNeutral()`,
`underlined()` and `inline()`; provide one for a form with `LocalFieldStyle provides …`.

## Selection controls

```kotlin
fun Checkbox(checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?, modifier: Modifier = Modifier,
    style: CheckboxStyle = LocalCheckboxStyle.current ?: CheckboxStyle.Default,
    size: CheckboxSize = LocalCheckboxSize.current ?: CheckboxSize.Default,
    indeterminate: Boolean = false, enabled: Boolean = true, readOnly: Boolean = false,
    interactionSource: MutableInteractionSource? = null)

fun Radio(checked: Boolean, onClick: (() -> Unit)?, modifier: Modifier = Modifier,
    style: RadioStyle = LocalRadioStyle.current ?: RadioStyle.Default,
    size: RadioSize = LocalRadioSize.current ?: RadioSize.Default, enabled: Boolean = true,
    readOnly: Boolean = false, interactionSource: MutableInteractionSource? = null)

fun Toggle(state: ToggleState, onToggle: ((ToggleState) -> Unit)?, modifier: Modifier = Modifier,
    style: ToggleStyle = LocalToggleStyle.current ?: ToggleStyle.Default,
    size: ToggleSize = LocalToggleSize.current ?: ToggleSize.Default,
    cycleOrder: List<ToggleState> = BinaryToggleCycle, enabled: Boolean = true,
    readOnly: Boolean = false, interactionSource: MutableInteractionSource? = null)
// ToggleState.Off | Indeterminate | On; cycles BinaryToggleCycle, TriStateToggleCycle.
```

A `null` callback draws a display-only control whose state the enclosing row reports.

## Lists

```kotlin
fun ListItem(modifier: Modifier = Modifier, title: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null, overline: (@Composable () -> Unit)? = null,
    caption: (@Composable () -> Unit)? = null, value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true, selected: Boolean = false,
    highlightSelected: Boolean = true, dragging: Boolean = false, nestingLevel: Int = 0,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    interactionSource: MutableInteractionSource? = null, onClick: (() -> Unit)? = null,
    onClickLabel: String? = null, role: Role? = Role.Button,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    content: (@Composable BoxScope.() -> Unit)? = null)

fun NavigationListItem(onClick: () -> Unit, title: @Composable () -> Unit,
    modifier: Modifier = Modifier, subtitle: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null, value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true, selected: Boolean = false, nestingLevel: Int = 0,
    direction: ChevronDirection = ChevronDirection.Right, style: ListItemStyle = …,
    size: ListItemSize = …, layout: SlotRowLayout = LocalListItemLayout.current,
    interactionSource: MutableInteractionSource? = null, onClickLabel: String? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null)

fun CheckboxListItem(checked: Boolean, onCheckedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit, modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null, overline: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null, enabled: Boolean = true,
    indeterminate: Boolean = false, highlightChecked: Boolean = false, nestingLevel: Int = 0,
    placement: ListItemControlPlacement = ListItemControlPlacement.Leading,
    style: ListItemStyle = …, size: ListItemSize = …, layout: SlotRowLayout = …,
    checkboxStyle: CheckboxStyle = LocalCheckboxStyle.current ?: CheckboxStyle.Default,
    checkboxSize: CheckboxSize = LocalCheckboxSize.current ?: CheckboxSize.Default,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null)

fun RadioListItem(selected: Boolean, onSelect: () -> Unit, title: @Composable () -> Unit,
    modifier: Modifier = Modifier, subtitle: …, overline: …, value: …, enabled: Boolean = true,
    highlightSelected: Boolean = false, nestingLevel: Int = 0,
    placement: ListItemControlPlacement = ListItemControlPlacement.Leading,
    style: …, size: …, layout: …,
    radioStyle: RadioStyle = LocalRadioStyle.current ?: RadioStyle.Default,
    radioSize: RadioSize = LocalRadioSize.current ?: RadioSize.Default,
    interactionSource: …, leading: …, trailing: …, bottom: …)

fun ToggleListItem(state: ToggleState, onToggle: (ToggleState) -> Unit,
    title: @Composable () -> Unit, modifier: Modifier = Modifier, subtitle: …, overline: …,
    enabled: Boolean = true, nestingLevel: Int = 0,
    cycleOrder: List<ToggleState> = BinaryToggleCycle, style: …, size: …, layout: …,
    toggleStyle: ToggleStyle = LocalToggleStyle.current ?: ToggleStyle.Default,
    toggleSize: ToggleSize = LocalToggleSize.current ?: ToggleSize.Default,
    interactionSource: …, leading: …, bottom: …)

fun CheckmarkListItem(selected: Boolean, onClick: () -> Unit, title: @Composable () -> Unit,
    modifier: Modifier = Modifier, subtitle: …, overline: …, value: …, enabled: Boolean = true,
    highlightSelected: Boolean = false, checkmarkColor: Color = Color.Unspecified,
    nestingLevel: Int = 0, style: …, size: …, layout: …, interactionSource: …,
    onClickLabel: String? = null, leading: …, bottom: …)

fun ExpandableListItem(expanded: Boolean, onExpandedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit, modifier: Modifier = Modifier, subtitle: …, overline: …,
    value: …, enabled: Boolean = true, selected: Boolean = false, nestingLevel: Int = 0,
    style: …, size: …, layout: …, interactionSource: …, onClickLabel: String? = null,
    transition: ExpandableTransition = ExpandableTransition.Default, leading: …,
    content: @Composable ColumnScope.() -> Unit)

fun List(modifier: Modifier = Modifier,
    itemStyle: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    itemSize: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    dividerStyle: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    dividerSize: DividerSize = LocalDividerSize.current ?: DividerSize.Default,
    listStyle: ListStyle = LocalListStyle.current ?: ListStyle.Default,
    listSize: ListSize = LocalListSize.current ?: ListSize.Default, separated: Boolean = true,
    enabled: Boolean = true, contentPadding: PaddingValues = PaddingValues(0.dp),
    content: ListScope.() -> Unit)
// LazyList takes the same parameters plus `state: LazyListState = rememberLazyListState()` after
// `modifier`. Both have an overload with a leading `entries: List<ListEntry>` and no `content`.

// ListScope, the receiver of `content`:
fun item(key: Any, title: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null, overline: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null, enabled: Boolean = true, selected: Boolean = false,
    highlightSelected: Boolean = true, nestingLevel: Int = 0, style: ListItemStyle? = null,
    onClick: (() -> Unit)? = null, onClickLabel: String? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    content: (@Composable BoxScope.() -> Unit)? = null)
fun divider(key: Any = "divider:${entries.size}", label: String? = null,
    style: DividerStyle? = null, size: DividerSize? = null)
```

Elided parameters (`…`) have the same names, types and defaults as in `ListItem`.

## Settings

```kotlin
fun Settings(modifier: Modifier = Modifier, styles: SettingsStyles = SettingsDefaults.styles(),
    sizes: SettingsSizes = SettingsDefaults.sizes(),
    headerPlacement: SettingsHeaderPlacement = SettingsHeaderPlacement.Inside,
    content: @Composable SettingsScope.() -> Unit)

fun SettingsScope.Section(modifier: Modifier = Modifier, header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null, enabled: Boolean = true, separated: Boolean = true,
    headerPlacement: SettingsHeaderPlacement = LocalSettingsHeaderPlacement.current,
    bandColor: Color = SettingsTheme.styles.section.band, band: (@Composable () -> Unit)? = null,
    content: @Composable SettingsSectionScope.() -> Unit)
// `band`: a strip at the bottom of the card for the section's closing action — one row, usually a
// SettingsRow in the matching tone; `bandColor = ArecaTheme.colors.bgDangerMuted` under a danger row.

fun SettingsRow(title: @Composable () -> Unit, modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null, caption: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null, leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    tone: SettingsRowTone = SettingsRowTone.Default,
    status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true,
    selected: Boolean = false, nestingLevel: Int = 0, onClick: (() -> Unit)? = null,
    onClickLabel: String? = null, interactionSource: MutableInteractionSource? = null,
    style: ListItemStyle = SettingsTheme.styles.rowStyle(tone, interactive = onClick != null),
    size: ListItemSize = SettingsTheme.sizes.row,
    layout: SlotRowLayout = LocalListItemLayout.current)

fun SettingsToggleRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit, modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null, badge: (@Composable () -> Unit)? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true,
    nestingLevel: Int = 0, onClick: (() -> Unit)? = null, onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    style: ListItemStyle = SettingsTheme.styles.row, size: ListItemSize = SettingsTheme.sizes.row,
    layout: SlotRowLayout = LocalListItemLayout.current)
// Overload: `state: ToggleState`, `onToggle: (ToggleState) -> Unit`, and
// `cycleOrder: List<ToggleState> = BinaryToggleCycle` after `onClickLabel`.

fun <T> SettingsSelectRow(title: @Composable () -> Unit, options: List<T>, selected: T,
    onSelect: (T) -> Unit, optionLabel: (T) -> String, modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null, badge: (@Composable () -> Unit)? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true,
    nestingLevel: Int = 0, onClickLabel: String? = null, …)

fun SettingsTextFieldRow(value: String, onValueChange: (String) -> Unit, placeholder: String,
    modifier: Modifier = Modifier, leading: (@Composable BoxScope.() -> Unit)? = null,
    maxLength: Int? = null, showCounter: Boolean = maxLength != null, singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true,
    nestingLevel: Int = 0, keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default, …)

fun SettingsOptionRow(selected: Boolean, onClick: () -> Unit, title: @Composable () -> Unit,
    modifier: Modifier = Modifier, subtitle: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null, leading: (@Composable BoxScope.() -> Unit)? = null,
    enabled: Boolean = true, nestingLevel: Int = 0, onClickLabel: String? = null, …)

fun SettingsRowIcon(imageVector: ImageVector, modifier: Modifier = Modifier,
    color: ActionColor = ActionColor.Neutral, contentDescription: String? = null,
    shape: Shape = ArecaTheme.shapes.tile, size: FeaturedIconSize = SettingsTheme.sizes.iconTile)
// Overload: `style: FeaturedIconStyle` after `imageVector`, in place of `color`.
// SettingsRowStatus.Idle | Loading | Error(message); SettingsRowTone.Default | Brand | Danger.

fun SettingsRowTile(modifier: Modifier = Modifier, shape: Shape = ArecaTheme.shapes.tile,
    size: Dp = SettingsTheme.sizes.iconTile.container, content: @Composable BoxScope.() -> Unit)
// Any visual — a flag, a photo, a brand mark — cropped to the icon tile, so a section's leading
// visuals share one size and shape.

fun SettingsRowLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    enabled: Boolean = true, onClickLabel: String? = null)
// A second action in a pressable row's `trailing` slot ("Verify"): row ink, underlined, a 48dp
// target that overhangs the line instead of making the row taller.
```

## Layout

```kotlin
fun BaseLayout(modifier: Modifier = Modifier, bg: Color = Color.Transparent,
    contentWindowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    topBar: @Composable () -> Unit = {}, bottomBar: @Composable () -> Unit = {},
    background: @Composable BoxScope.() -> Unit = {}, overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit)

fun Card(modifier: Modifier = Modifier,
    style: CardStyle = LocalCardStyle.current ?: CardStyle.Default,
    size: CardSize = LocalCardSize.current ?: CardSize.Default, enabled: Boolean = true,
    active: Boolean = false, onClick: (() -> Unit)? = null, onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    footerStyle: CardFooterStyle = CardFooterStyle.Default,
    footer: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable BoxScope.(padding: PaddingValues) -> Unit)
// Overload: leading `expanded: Boolean`, then the same parameters, `transition`, `footerStyle`,
// `footer`, `expandedContent` and `content`, both
// `@Composable ColumnScope.(padding: PaddingValues) -> Unit`.
// CardStyle presets: Default, Outlined, SoftNeutral, GhostNeutral, SolidBrand; factories
// outlined(shape), softNeutral(shape), …, and default(…).
// CardFooterStyle presets: Default (= plain()), plain(divider), soft(ActionColor = Neutral),
// solid(ActionColor = Brand), of(bg, content, divider, button) for a color of your own. The footer
// hands its `content` color and its `button` style to what is inside it.

fun HorizontalDivider(modifier: Modifier = Modifier,
    style: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    size: DividerSize = LocalDividerSize.current ?: DividerSize.Default, text: String? = null,
    textPosition: DividerTextPosition = DividerTextPosition.Center)
// VerticalDivider takes the same parameters.

@ExperimentalArecaApi
fun PullToRefreshLayout(isRefreshing: Boolean, onRefresh: () -> Unit,
    modifier: Modifier = Modifier, style: PullToRefreshStyle = …, size: PullToRefreshSize = …,
    enabled: Boolean = true, state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.() -> Unit = { PullToRefreshIndicator(…) },
    content: @Composable BoxScope.() -> Unit)
```

## Bars and tabs

```kotlin
fun TopBar(title: String, modifier: Modifier = Modifier, subtitle: String? = null,
    style: TopBarStyle = LocalTopBarStyle.current ?: TopBarStyle.Default,
    size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
    titleAlignment: TopBarTitleAlignment = TopBarTitleAlignment.Start,
    layout: SlotRowLayout = titleAlignment.slotRowLayout(size.gap),
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0), scrollState: TopBarScrollState? = null,
    leading: (@Composable TopBarActionScope.() -> Unit)? = null,
    trailing: (@Composable TopBarActionScope.() -> Unit)? = null,
    bottom: (@Composable () -> Unit)? = null)
// Overload: no `title` / `subtitle`; a trailing `content: @Composable () -> Unit` instead.
// TopBarStyle presets: Default, Tonal, Floating, Modal, Scrim.

fun LargeTopBar(title: String, scrollState: TopBarScrollState, modifier: Modifier = Modifier,
    subtitle: String? = null, style: TopBarStyle = …, size: TopBarSize = …,
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    leading: (@Composable TopBarActionScope.() -> Unit)? = null,
    trailing: (@Composable TopBarActionScope.() -> Unit)? = null,
    bottom: (@Composable () -> Unit)? = null)

fun rememberTopBarScrollState(
    behavior: TopBarScrollBehavior = TopBarScrollBehavior.Pinned): TopBarScrollState
// TopBarScrollBehavior.Pinned | EnterAlways | ExitUntilCollapsed. Attach
// `scrollState.nestedScrollConnection` to the scroller with `Modifier.nestedScroll`.

fun TopBarProgress(progress: Float?, modifier: Modifier = Modifier,
    contentDescription: String? = null,
    style: ProgressBarStyle = ProgressBarStyle.Default.copy(track = Color.Transparent))

fun <T : Any> TabBar(items: List<TabBarItem<T>>, selectedValue: T?, onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    style: TabBarStyle = LocalTabBarStyle.current ?: TabBarStyle.Default,
    size: TabBarSize = LocalTabBarSize.current ?: TabBarSize.Default,
    windowInsets: WindowInsets =
        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))
// FloatingTabBar adds `indicatorStyle: FloatingTabBarIndicatorStyle =
// FloatingTabBarIndicatorStyle.underline()` before `windowInsets` and `animationSpec` after it.
// Both: overload with `state: TabBarState<T>` in place of `selectedValue` / `onValueSelected`.
// TabBarItem(value: T, enabled: Boolean = true, content: @Composable TabBarItemScope.() -> Unit)

fun <T> Tabs(tabs: List<TabItem<T>>, selectedValue: T, onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier, style: TabsStyle = LocalTabsStyle.current ?: TabsStyle.Default,
    size: TabsSize = LocalTabsSize.current ?: TabsSize.Default,
    variant: TabsVariant = TabsVariant.Underline,
    orientation: TabsOrientation = TabsOrientation.Horizontal,
    horizontalPosition: TabsHorizontalPosition = TabsHorizontalPosition.Bottom,
    verticalPosition: TabsVerticalPosition = TabsVerticalPosition.Start)
// Overload: `state: TabsState<T>` (from `rememberTabsState(initialValue)`, read
// `state.selectedValue`) in place of `selectedValue` / `onValueSelected`.
// TabItem(value: T, enabled: Boolean = true, content: @Composable TabItemScope.() -> Unit)
```

Bar controls are `TopBarActionScope` extensions for `leading` and `trailing`. Each takes `onClick`
first (`FavoriteAction` takes `selected` before it) and a localisable `contentDescription` or `text`:

| Control | Extra parameters and English default |
| --- | --- |
| `BackAction` | `label: String? = null`, `contentDescription = "Back"`, `color` |
| `CloseAction`, `OverflowAction`, `AddAction` | `contentDescription = "Close"` / `"More"` / `"Add"`, `color` |
| `MenuAction` | `contentDescription = "Menu"`, `color`, `badge: String?` |
| `NotificationsAction`, `FilterAction` | `badge: String?`, `contentDescription = "Notifications"` / `"Filters"` |
| `SearchAction`, `EditAction`, `SettingsAction` | `contentDescription = "Search"` / `"Edit"` / `"Settings"` |
| `ShareAction`, `DownloadAction` | `contentDescription = "Share"` / `"Download"`, `loading` |
| `DeleteAction` | `contentDescription = "Delete"`, `color = ActionColor.Danger`, `loading` |
| `FavoriteAction(selected, onClick, …)` | `contentDescription = "Favorite"`, `selectedColor = ActionColor.Danger` |
| `DoneAction`, `CancelAction` | `text = "Done"` / `"Cancel"`; `DoneAction` also `loading` |
| `IconAction(icon, contentDescription, onClick, …)` | `color`, `loading`, `badge: String?` |
| `TextAction(text, onClick, …)` | `color = ActionColor.Brand`, `loading`, `leading`, `trailing` |
| `AvatarAction` | `letters: String?`, `contentDescription = "Account"`, `loading`, `content` |

## Overlays

```kotlin
fun Dialog(onDismissRequest: () -> Unit, modifier: Modifier = Modifier,
    style: ModalStyle = LocalModalStyle.current ?: ModalStyle.Default,
    size: ModalSize = LocalModalSize.current ?: ModalSize.Default,
    dismissOnOverlayClick: Boolean = true, dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true, overlayContentDescription: String = "Dismiss",
    header: (@Composable () -> Unit)? = null, footer: (@Composable () -> Unit)? = null,
    overlay: @Composable BoxScope.() -> Unit = {}, content: @Composable () -> Unit)

fun ConfirmationDialog(confirmText: @Composable () -> Unit, onConfirm: suspend () -> Unit,
    modifier: Modifier = Modifier, onDecline: (suspend () -> Unit)? = null,
    onDismissRequest: () -> Unit = {}, onFailure: (Exception) -> Unit = {},
    style: ConfirmationDialogStyle = …, size: ConfirmationDialogSize = …,
    leading: (@Composable ColumnScope.() -> Unit)? = null, title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    extraContent: (@Composable ColumnScope.() -> Unit)? = null,
    declineText: (@Composable () -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null)

fun rememberFlowModal(allowPartial: Boolean = true, allowExpand: Boolean = true,
    allowDrag: Boolean = true, partialMultiplier: Float = 0.5f,
    @FloatRange(from = 0.0, to = 1.0) maxHeightFraction: Float = 0.9f, autoHeight: Boolean = false,
    dialogProperties: DialogProperties = flowModalDialogProperties(),
    animationSpec: AnimationSpec<Float>? = null): FlowModalController

fun FlowModal(controller: FlowModalController, modifier: Modifier = Modifier,
    style: FlowModalStyle = LocalFlowModalStyle.current ?: FlowModalStyle.Default,
    size: FlowModalSize = LocalFlowModalSize.current ?: FlowModalSize.Default,
    onDismissRequest: () -> Unit = { controller.hide() },
    overlayContentDescription: String = "Dismiss", header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null, content: @Composable () -> Unit)
// FlowModalController: show(partial: Boolean = true), expand(), collapse(), hide(), isVisible.
// FlowModal composes nothing while hidden, so call it unconditionally.

fun FlowModalActionBar(confirmText: String, onConfirm: () -> Unit, modifier: Modifier = Modifier,
    enabled: Boolean = true, loading: Boolean = false, style: FlowModalActionBarStyle = …,
    size: FlowModalActionBarSize = …, leading: (@Composable () -> Unit)? = null)

fun ConfirmSheet(visible: Boolean, icon: ImageVector?, title: String, description: String?,
    confirmText: String, onConfirm: () -> Unit, onDismiss: () -> Unit,
    modifier: Modifier = Modifier, note: String? = null,
    noteColor: Color = ArecaTheme.colors.fgSubtle, confirming: Boolean = false,
    confirmEnabled: Boolean = true, cancelText: String = "Cancel",
    style: ConfirmSheetStyle = LocalConfirmSheetStyle.current ?: ConfirmSheetStyle.Default,
    size: ConfirmSheetSize = LocalConfirmSheetSize.current ?: ConfirmSheetSize.Default,
    extra: (@Composable ColumnScope.() -> Unit)? = null)

fun rememberDropdownPopup(initialOpen: Boolean = false,
    placement: AnchoredPlacement = AnchoredPlacement.BelowStart,
    anchorState: AnchoredState = rememberAnchoredState()): DropdownPopupState

fun DropdownPopup(state: DropdownPopupState, onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: DropdownPopupStyle = LocalDropdownPopupStyle.current ?: DropdownPopupStyle.Default,
    size: DropdownPopupSize = LocalDropdownPopupSize.current ?: DropdownPopupSize.Default,
    dismissOnBackPress: Boolean = true, dismissOnClickOutside: Boolean = true,
    clippingEnabled: Boolean = true, content: @Composable BoxScope.() -> Unit)
// DropdownPopupState: isOpen, show(), dismiss(), toggle(), anchorState.
fun Modifier.anchored(state: AnchoredState): Modifier // on the trigger: state.anchorState
```

## Feedback, status, display

```kotlin
fun ToastHost(state: ToastState, modifier: Modifier = Modifier,
    animation: ToastAnimation = ToastAnimation.Slide,
    style: ToastStyle = LocalToastStyle.current ?: ToastStyle.Default,
    windowInsets: WindowInsets = WindowInsets.safeDrawing)
// rememberToastState(): ToastState. ToastHost fills its parent and keeps clear of the insets:
// put it in BaseLayout(overlay = { … }).
// ToastState: show(...): Long, dismiss(id: Long), dismiss(), dismissAll().
fun show(label: CharSequence, caption: CharSequence? = null, color: ToastColor? = null,
    shape: Shape? = null, duration: Duration? = 2.seconds,
    alignment: Alignment.Vertical = Alignment.Top,
    leading: (@Composable RowScope.(id: Long) -> Unit)? = null,
    trailing: (@Composable RowScope.(id: Long) -> Unit)? = null,
    onClick: ((id: Long) -> Unit)? = null): Long

fun Notice(title: String?, modifier: Modifier = Modifier, icon: ImageVector? = null,
    tone: NoticeTone = NoticeTone.Neutral,
    emphasis: NoticeEmphasis =
        if (tone == NoticeTone.Danger) { NoticeEmphasis.Filled } else { NoticeEmphasis.Subtle },
    busy: Boolean = false,
    style: NoticeStyle = LocalNoticeStyle.current ?: NoticeStyle.of(tone, emphasis),
    size: NoticeSize = LocalNoticeSize.current ?: NoticeSize.Default,
    content: @Composable ColumnScope.() -> Unit = {})
fun NoticeText(text: String, modifier: Modifier = Modifier, quiet: Boolean = false)
fun <T : Any> AnimatedNotice(value: T?, modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit)
// NoticeTone.Neutral | Info | Success | Warning | Danger; NoticeEmphasis.Subtle | Filled.

fun Fallback(modifier: Modifier = Modifier,
    style: FallbackStyle = LocalFallbackStyle.current ?: FallbackStyle.Default,
    size: FallbackSize = LocalFallbackSize.current ?: FallbackSize.Default,
    top: (@Composable () -> Unit)? = null, heading: (@Composable () -> Unit)? = null,
    body: (@Composable () -> Unit)? = null, content: (@Composable () -> Unit)? = null)

fun StatusScene(title: String, modifier: Modifier = Modifier,
    mark: (@Composable () -> Unit)? = null, message: String? = null, footnote: String? = null,
    actions: (@Composable () -> Unit)? = null, note: String? = null, stagedEntrance: Boolean = true,
    style: StatusSceneStyle = …, size: StatusSceneSize = …)
fun StatusMark(state: StatusMarkState, contentDescription: String?, modifier: Modifier = Modifier,
    style: StatusMarkStyle = …, size: StatusMarkSize = …)
// StatusMarkState.Working(icon: ImageVector? = null) | Success | Stopped(icon: ImageVector)
fun StatusSceneActions(primary: String, onPrimary: () -> Unit, modifier: Modifier = Modifier,
    style: StatusSceneActionsStyle = …, size: StatusSceneActionsSize = …,
    loading: Boolean = false, secondary: String? = null, onSecondary: (() -> Unit)? = null,
    secondaryLeading: (@Composable () -> Unit)? = null)

fun Skeleton(modifier: Modifier = Modifier, count: Int = 1,
    arrangement: SkeletonArrangement = SkeletonArrangement.Lines, width: Dp? = null,
    height: Dp? = null, textStyle: TextStyle? = null, contentDescription: String? = null,
    animated: Boolean = true, reversed: Boolean = false, style: SkeletonStyle = …,
    size: SkeletonSize = …)

fun Spinner(modifier: Modifier = Modifier, style: SpinnerStyle = …, size: SpinnerSize = …,
    contentDescription: String? = null, period: Duration = 900.milliseconds)

fun ProgressBar(progress: Float?, modifier: Modifier = Modifier, label: CharSequence? = null,
    labelPosition: RelativePosition = RelativePosition.BottomCenter,
    contentDescription: String? = null, style: ProgressBarStyle = …, size: ProgressBarSize = …)
// progress = null is indeterminate.

fun Badge(modifier: Modifier = Modifier,
    style: BadgeStyle = LocalBadgeStyle.current ?: BadgeStyle.Default,
    size: BadgeSize = LocalBadgeSize.current ?: BadgeSize.Default,
    shape: Shape = ArecaTheme.shapes.full, soft: Boolean = false, onClick: (() -> Unit)? = null,
    enabled: Boolean = true, interactionSource: MutableInteractionSource? = null,
    leading: (@Composable () -> Unit)? = null, trailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit)
// BadgeStyle presets: Default, Brand, Neutral, NeutralDark, Success, Warning, Danger, Info, Chip.

fun Avatar(modifier: Modifier = Modifier,
    style: AvatarStyle = LocalAvatarStyle.current ?: AvatarStyle.Default,
    size: AvatarSize = LocalAvatarSize.current ?: AvatarSize.Default,
    shape: Shape = ArecaTheme.shapes.full, letters: String? = null,
    contentDescription: String? = null, loading: Boolean = false,
    content: (@Composable BoxScope.() -> Unit)? = null)
```

## Collections and tables

```kotlin
fun <T> ContentList(state: LimitedContentListState<T>, modifier: Modifier = Modifier,
    style: ContentListStyle = …, size: ContentListSize = …,
    labels: ContentListLabels = ContentListLabels.English, fillAvailableHeight: Boolean = false,
    onRetryInitial: () -> Unit = {},
    loadingContent: @Composable () -> Unit = { ContentListDefaults.LoadingState() },
    emptyContent: @Composable () -> Unit = { ContentListDefaults.EmptyState(…) },
    initialErrorContent: @Composable (String?, () -> Unit) -> Unit = { … ErrorState(…) },
    header: (@Composable ColumnScope.() -> Unit)? = null, itemContent: @Composable (T) -> Unit)

fun <T> PagedContentList(state: PagedContentListState<T>, modifier: Modifier = Modifier,
    style: ContentListStyle = …, size: ContentListSize = …,
    labels: ContentListLabels = ContentListLabels.English,
    contentPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.none),
    fillAvailableHeight: Boolean = true, listState: LazyListState = rememberLazyListState(),
    onRefresh: (() -> Unit)? = null,
    onLoadNextPage: () -> Unit = {}, onRetryInitial: () -> Unit = onRefresh ?: {},
    onRetryLoadMore: () -> Unit = onLoadNextPage, itemKey: ((T) -> Any)? = null,
    loadingContent: …, emptyContent: …, initialErrorContent: …,
    loadMoreContent: @Composable () -> Unit = { ContentListDefaults.LoadMoreState() },
    loadMoreErrorContent: @Composable (String, () -> Unit) -> Unit = { … },
    endReachedContent: @Composable () -> Unit = { … },
    header: (@Composable ColumnScope.() -> Unit)? = null, itemContent: @Composable (T) -> Unit)

// LimitedContentListState(items: List<T>, loadingInitial: Boolean = false,
//     refreshing: Boolean = false, initialError: String? = null)
// PagedContentListState(items: List<T>, hasNextPage: Boolean, loadingNextPage: Boolean = false,
//     loadMoreError: String? = null, loadingInitial: Boolean = false, refreshing: Boolean = false,
//     initialError: String? = null)
// ContentListLabels(emptyTitle, errorTitle, retry, loadMoreErrorTitle, endReached: String)

fun <T> Table(items: List<T>, columns: List<TableColumn<T>>, modifier: Modifier = Modifier,
    style: TableStyle = LocalTableStyle.current ?: TableStyle.Default,
    size: TableSize = LocalTableSize.current ?: TableSize.Default, separated: Boolean = true)
// LazyTable adds `key: ((index: Int, item: T) -> Any)? = null`.
// TableColumn(id: String, title: String, width: Dp? = null, minWidth: Dp = 80.dp,
//     maxWidth: Dp? = null, expandWeight: Float = 1f, value: (T) -> Any? = { "" },
//     cell: (@Composable (T) -> Unit)? = null, header: (@Composable () -> Unit)? = null)
```
