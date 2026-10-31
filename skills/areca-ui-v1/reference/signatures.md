# Signatures and packages

Open this when calling a foundation component whose parameters, overloads, defaults or package you
are not sure of, or when an import does not resolve, at the latest `dev.voir.areca:foundation`
release. Every top-level foundation composable is listed; members of a scope (`TopBarTitle.*`, `FieldRowScope.*`, `ContentListDefaults.*`) appear with their family. Tokens and the theme provider are in
[theme.md](theme.md).

All functions below are `@Composable public fun` unless they are a `Modifier` extension or a state
factory, and list their parameters in source order. Call with named arguments after the leading
required ones. Comments name the other overloads. `…` stands for parameters left out for length;
they have defaults, and the KDoc of the declaration documents every one.

## Packages

Every foundation symbol is imported from its family's package. Extension composables (`Section`,
`BackAction`, `TextSegment`, …) are imported by name like any top-level function.

| Package | Holds |
| --- | --- |
| `dev.voir.areca.theme` | `ArecaThemeProvider`, `ArecaDarkTheme`, `ArecaTheme`, `ArecaSizing`, `contentInsets`, `horizontalContentInsets`, `verticalContentInsets`, `tween` |
| `dev.voir.areca.foundation` | `LocalContentColor`, `LocalTextStyle`, `AnnounceOnChange` |
| `….foundation.button` | `Button`, `IconButton`, `ButtonStyle`, `ButtonSize`, `IconButtonStyle`, `IconButtonSize`, `ActionColor`, `LocalButtonStyle`, `LocalButtonSize` |
| `….foundation.text` | `Text`, `TextVariant`, `TextColor`, `Heading`, `TextLink`, `TextLinkPrompt`, `SensitiveText` |
| `….foundation.icon` | `Icon`, `IconSize`, `LocalIconSize`, `FeaturedIcon`, `FeaturedIconStyle`, `FeaturedIconSize`, `Chevron`, `ChevronDirection` |
| `….foundation.field` | `TextField`, `PasswordField`, `SearchField`, `AffixTextField`, `OtpField`, `FieldGroup`, `FieldGroupLayout`, `FieldRow`, `TextSegment`, `Field`, `FieldScope`, `FieldStatus`, `FieldStyle`, `FieldSize`, `FieldDefaults`, `LocalFieldStyle`, `LocalFieldSize`, `FieldLabelPlacement` |
| `….foundation.field.select` | `SelectField`, `SelectOption`, `SelectSelectionMode`, `SelectFieldLabels`, `SelectFlowModal`, `rememberSelectFlowModalState`, `SelectFlowModalDefaults`, `SelectSegment` |
| `….foundation.selection` | `Checkbox`, `Radio`, `Toggle`, `ToggleState`, `BinaryToggleCycle`, `TriStateToggleCycle`, `NumberStepper`, `NumberStepperLabels`, `SlidingSelector`, `SelectorItem`, `SelectorItemScope`, `SelectorMode`, `SelectorLayout`, `SelectorOrientation`, `SelectorThumbKind`, `SelectorEdge`, `SlidingSelectorStyle`, `SlidingSelectorSize`, `Swatch`, `SwatchPicker`, `ColorShade`, `ColorItem` |
| `….foundation.list` | `List`, `LazyList`, `ListItem`, `NavigationListItem`, `CheckboxListItem`, `RadioListItem`, `ToggleListItem`, `CheckmarkListItem`, `ExpandableListItem`, `ExpandableListItemContainer`, `ListItemColumn`, `ListItemSkeleton`, `ListItemChevron`, `ListItemCheckmark`, `ListItemStyle`, `ListItemSize` |
| `….foundation.settings` | `Settings`, `Section`, `SettingsRow`, `SettingsToggleRow`, `SettingsSelectRow`, `SettingsTextFieldRow`, `SettingsOptionRow`, `SettingsBlock`, `SettingsRowIcon`, `SettingsRowTile`, `SettingsRowLink`, `SettingsRowStatus`, `SettingsRowTone`, `SettingsRowSkeleton`, `SettingsDefaults` |
| `….foundation.layout` | `BaseLayout`, `Card`, `CardStyle`, `CardSize`, `CardFooterStyle`, `HorizontalDivider`, `VerticalDivider`, `Expandable`, `rememberExpandableState`, `PullToRefreshLayout`, `PullToRefreshIndicator`, `rememberPullToRefreshState`, `CollapsingHeaderLayout`, `CollapsingHeaderState`, `rememberCollapsingHeaderState`, `Overlay`, `RelativePositionLayout`, `RelativePosition`, `VerticalScrollbar`, `HorizontalScrollbar` |
| `….foundation.topbar` | `TopBar`, `LargeTopBar`, `rememberTopBarScrollState`, `TopBarScrollBehavior`, `TopBarStyle`, `TopBarSize`, `TopBarActionStyle`, the `…Action` controls, `TopBarProgress` |
| `….foundation.tabbar`, `….foundation.tabs` | `TabBar`, `FloatingTabBar`, `FloatingTabBarItemContent`, `TabBarItem`, `rememberTabBarState`; `Tabs`, `TabItem`, `TabsVariant`, `TabsStyle`, `TabsSize`, `rememberTabsState` |
| `….foundation.overlay` | `Dialog`, `ModalDefaults`, `ConfirmationDialog`, `ConfirmSheet`, `FlowModal`, `rememberFlowModal`, `FlowModalController`, `FlowModalActionBar`, `FlowModalActionBarLayout`, `FlowModalSecondaryAction`, `DropdownPopup`, `rememberDropdownPopup`, `anchored`, `AnchoredPlacement`, `TooltipBox`, `edgeToEdgeDialogProperties` |
| `….foundation.feedback` | `ToastHost`, `rememberToastState`, `Toast`, `Fallback`, `Skeleton`, `Spinner`, `ProgressBar`, `StepIndicator`, `RequirementList`, `Requirement`, `RequirementState` |
| `….foundation.notice`, `….foundation.status` | `Notice`, `NoticeText`, `NoticeTone`, `NoticeEmphasis`, `AnimatedNotice`; `StatusScene`, `StatusSceneActions`, `StatusMark`, `StatusMarkState` |
| `….foundation.badge`, `….foundation.avatar` | `Badge`, `BadgeStyle`, `BadgeSize`, `NotificationBadge`, `IndicatorDot`, `BadgeSelect`, `LazyBadgeSelect`, `FlowBadgeSelect`, `BadgeOption`, `BadgeSelectionMode`; `Avatar` |
| `….foundation.stat` | `Stat`, `StatGrid`, `StatStyle`, `StatSize`, `LocalStatStyle`, `LocalStatSize` |
| `….foundation.collection`, `….foundation.table` | `ContentList`, `PagedContentList`, `GroupedContentList`, `GroupedPagedContentList`, `LimitedContentListState`, `PagedContentListState`, `ContentListGroup`, `ContentListLabels`, `ContentListDefaults`, `LazyVerticalGroupedGrid`, `LazyHorizontalGroupedGrid`, `GroupedGridData`; `Table`, `LazyTable`, `TableColumn` |
| `….foundation.navigation`, `….foundation.wheel`, `….foundation.slot` | `NavList`, `NavNode`, `NavData`, `NavItem`, `SwipePager`, `PagerControls`, `PageIndicator`, `PageIndicatorVariant`, `PageIndicatorStyle`, `PageIndicatorSize`, `PageCounter`, `PageCounterStyle`, `PageCounterSize`, `rememberPagerController`; `WheelPicker`, `WheelPickerColumn`, `WheelPickerItem`, `WheelPickerHighlight`, `WheelPickerEdgeFade`, `rememberWheelPickerColumnState`; `SlotRow`, `SlotRowLayout` (`Default`, `Auto`, `Center`, `Top`, `FirstLine`, `Centered`, `Fill`, `wrap(…)`), `SlotRowAlignment`, `Modifier.firstLineCenter(textStyle)`, `FirstLineCenter` |
| `dev.voir.areca.indication`, `.modifier`, `.platform`, `.shape` | `TouchIndication`, `FadeIndication`, `StrokeIndication`, `LiquidSweepIndication`, `FocusOutline`; `clickCursor`, `minimumTouchTarget`, `thenIf`; `SystemBars`; `SquircleShape` |
| `….foundation.adaptive` | `AdaptiveNavigation`, `AdaptiveNavigationStyle`, `AdaptiveNavigationLayout`, `AdaptiveNavigationScope`, `AdaptiveToolbar`, `AdaptiveToolbarAction`, `EdgeColumn`, `EdgeColumnSide`, `EdgeColumnStyle`, `EdgeColumnSize`, `EdgeButton`, `EdgeButtonEmphasis`, `VerticalTabBar`, `EdgeStatus`, `FloatingSidebar`, `NavRail`, `NavDestination`, `NavRailStyle`, `NavRailSize`, `ListDetail`, `ListDetailScope`, `SupportingPane`, `SupportingPaneScope`, `ContentColumn`, `AdaptiveGrid`, `AdaptiveSheet`, `AdaptiveMenu`, `AdaptiveMenuItem`, `contextMenu`, `AdaptiveDefaults` |
| `dev.voir.areca.window` | `WindowClass`, `WindowFold`, `FoldOrientation`, `FoldState`, `WidthClass`, `HeightClass`, `InputMode`, `WindowProfile`, `adaptive`, `AdaptiveContent`, `WindowClassBox`, `ProvideWindowClass` |
| `dev.voir.areca` | `ExperimentalArecaApi` |

## Theme and helpers

`ArecaThemeProvider(darkTheme, baseColors, colors, radius, shapes, spacing, elevation, motion,
fonts, baseTypography, inputMode, sizing, typography, content)` and every token, `ArecaSizing`
included: [theme.md](theme.md).

```kotlin
// dev.voir.areca.window — read the one in scope as ArecaTheme.window
class WindowClass(val size: DpSize, val input: InputMode, val fold: WindowFold? = null)
    // + width: WidthClass, height: HeightClass, profile: WindowProfile, actionReach: ActionReach,
    // isLandscape; WindowClass.WideAspectRatio = 1.25f (touch at least this wide = on its side)
enum class ActionReach { Header, Thumb }
    // Header: a pointer, a phone on its side, touch at least WideAspectRatio wide. Thumb: a phone, a
    // tablet held upright, an unfolded foldable. Read it from ArecaTheme.hostWindow, not the pane.
class WindowFold(val orientation: FoldOrientation, val bounds: DpRect,
    val state: FoldState = FoldState.Flat, val isSeparating: Boolean = state == FoldState.HalfOpened)
    // WindowFold.vertical(at: Dp, height: Dp, state = Flat); WindowFold.horizontal(at: Dp, width: Dp,
    // state = HalfOpened). FoldOrientation { Vertical, Horizontal }; FoldState { Flat, HalfOpened }.
    // Reported on Android only. In a WindowClassBox: the box's coordinates, null when it misses the box.
    // Given to ProvideWindowClass: measured from that provider's top-left corner.
enum class WidthClass(val minWidth: Dp) { Compact, Medium, Expanded, Large, ExtraLarge }
    // 0, 600, 840, 1200, 1600dp; WidthClass.of(width); select(compact, medium = compact, …)
enum class HeightClass(val minHeight: Dp) { Compact, Medium, Expanded } // 0, 480, 900dp
enum class InputMode { Touch, Pointer }
enum class WindowProfile { PhonePortrait, PhoneLandscape, TabletPortrait, TabletLandscape, Desktop }
    // Under touch, TabletLandscape vs TabletPortrait by shape (WideAspectRatio), not width; Desktop
    // is expanded and wider under a pointer.

fun <T> adaptive(compact: T, medium: T = compact, expanded: T = medium, large: T = expanded,
    extraLarge: T = large): T
fun AdaptiveContent(compact: @Composable () -> Unit, medium: @Composable () -> Unit = compact,
    expanded: @Composable () -> Unit = medium, large: @Composable () -> Unit = expanded,
    extraLarge: @Composable () -> Unit = large)
fun WindowClassBox(modifier: Modifier = Modifier, contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit)
fun ProvideWindowClass(windowClass: WindowClass, content: @Composable () -> Unit)

// dev.voir.areca.foundation.adaptive
class NavDestination<T : Any>(val value: T, val label: String, val icon: ImageVector,
    val enabled: Boolean = true)
fun <T : Any> AdaptiveNavigation(destinations: List<NavDestination<T>>, selectedValue: T?,
    onValueSelected: (T) -> Unit, modifier: Modifier = Modifier,
    style: AdaptiveNavigationStyle = AdaptiveNavigationStyle.EdgeColumn,
    edgeSide: EdgeColumnSide = EdgeColumnSide.Auto,
    sidebarVisible: Boolean = style == AdaptiveNavigationStyle.Rail,
    onSidebarVisibleChange: ((Boolean) -> Unit)? = null,
    sidebarToggleDescription: String = if (sidebarVisible) "Hide navigation" else "Show navigation",
    onSearch: (() -> Unit)? = null, searchDescription: String = "Search",
    status: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    header: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    sidebarSections: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    bottomBar: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    content: @Composable AdaptiveNavigationScope.() -> Unit)
// AdaptiveNavigationScope.layout: AdaptiveNavigationLayout.BottomBar (compact, not short) |
// Rail (a phone on its side; under style Rail also medium and expanded, or large with the sidebar
// hidden) | Sidebar (large and up under style Rail) | EdgeColumn (medium and up under style
// EdgeColumn). Content sits in a WindowClassBox. contentPadding: the bottomBar's height on a compact
// window, EdgeColumnSize.clearance on the column's side beside the column, else zero.
// Edge column: status and back at its top, header too under ActionReach.Header; a VerticalTabBar,
// then under ActionReach.Thumb header and the screen's actions, then the onSearch button at its foot; the toggle opens a FloatingSidebar (rows, sidebarSections,
// footer) and the tabs hide while it is open. status and footer also sit at the foot of the rail.
// Full guide: adaptive-shell.md.
class AdaptiveToolbarAction(val icon: ImageVector, val label: String, val onClick: () -> Unit,
    val primary: Boolean = false, val enabled: Boolean = true, val destructive: Boolean = false)
fun AdaptiveToolbar(title: String, modifier: Modifier = Modifier, onBack: (() -> Unit)? = null,
    actions: List<AdaptiveToolbarAction> = emptyList(), backDescription: String = "Back",
    moreDescription: String = "More", windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0))
// A TopBar except beside an EdgeColumn, where it draws the title as a heading and hands back to the
// column (the most recently drawn screen's toolbar wins). Its actions: labelled buttons beside the
// title under ActionReach.Header, round buttons at the column's foot under ActionReach.Thumb. Three
// places in each, three icons in the bar, More counted; the rest fold into More. In two
// panes the ListDetail list's and the SupportingPane supporting pane's toolbars stay bars.
fun EdgeColumn(modifier: Modifier = Modifier, side: EdgeColumnSide = EdgeColumnSide.Auto,
    size: EdgeColumnSize = LocalEdgeColumnSize.current ?: EdgeColumnSize.Default,
    windowInsets: WindowInsets = /* safeDrawing, vertical and on its side */,
    top: @Composable ColumnScope.() -> Unit, bottom: @Composable ColumnScope.() -> Unit)
fun EdgeButton(imageVector: ImageVector, contentDescription: String, onClick: () -> Unit,
    modifier: Modifier = Modifier, emphasis: EdgeButtonEmphasis = EdgeButtonEmphasis.Plain,
    enabled: Boolean = true, style: EdgeColumnStyle = …, size: EdgeColumnSize = …)
// EdgeColumnSide: Auto (the camera cutout's side, End without one) | Start | End. Align the column
// to the same edge. At a horizontal fold, top stays above it and bottom below it.
// EdgeButtonEmphasis: Plain | Primary (brand fill) | Toggled (contrast fill: an open sidebar)
fun <T : Any> VerticalTabBar(destinations: List<NavDestination<T>>, selectedValue: T?,
    onValueSelected: (T) -> Unit, modifier: Modifier = Modifier, moreLabel: String = "More",
    style: EdgeColumnStyle = …, size: EdgeColumnSize = …)
// Icons only, labels announced and shown as tooltips; past EdgeColumnSize.maxTabs (5) the last
// tab is More, an AdaptiveMenu of the rest.
fun EdgeStatus(imageVector: ImageVector, contentDescription: String, modifier: Modifier = Modifier,
    attention: Boolean = false, onClick: (() -> Unit)? = null, style: EdgeColumnStyle = …,
    size: EdgeColumnSize = …)
fun FloatingSidebar(visible: Boolean, onDismissRequest: () -> Unit, modifier: Modifier = Modifier,
    side: EdgeColumnSide = EdgeColumnSide.Auto, width: Dp = AdaptiveDefaults.FloatingSidebarWidth,
    edgeOffset: Dp = 0.dp, title: String = "Navigation", dismissLabel: String = "Close navigation",
    style: EdgeColumnStyle = …, size: EdgeColumnSize = …, windowInsets: WindowInsets = …,
    content: @Composable ColumnScope.() -> Unit)
// Draw it in a full-size Box under the EdgeColumn. Scrim tap and Escape call onDismissRequest;
// route the platform back gesture there too.
// EdgeColumnSize.Default: 48dp buttons in a 56dp column under touch (40 in 48 under a pointer), a
// 12dp inset and gap, so clearance = 80dp (72dp). EdgeColumnStyle.Default: bgSurface circles with a
// borderStrong hairline, the selected tab on bgBrandMuted, elevation.lg shadows.
fun <T : Any> NavRail(destinations: List<NavDestination<T>>, selectedValue: T?,
    onValueSelected: (T) -> Unit, modifier: Modifier = Modifier,
    style: NavRailStyle = LocalNavRailStyle.current ?: NavRailStyle.Default,
    size: NavRailSize = LocalNavRailSize.current ?: NavRailSize.Default, showLabels: Boolean = true,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(Vertical + Start),
    header: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null)
fun ListDetail(showDetail: Boolean, modifier: Modifier = Modifier,
    listWidth: Dp = Dp.Unspecified, resizable: Boolean = true,
    detailPlaceholder: (@Composable ListDetailScope.() -> Unit)? = null,
    list: @Composable ListDetailScope.() -> Unit, detail: @Composable ListDetailScope.() -> Unit)
// Two panes from a 600dp container (ListDetailScope.isTwoPane), else the detail replaces the list.
// At a vertical fold that leaves both panes their minimum widths, the panes meet at the fold.
// Under a pointer and resizable, the divider drags (ListPaneMinWidth 240 to leaving
// DetailPaneMinWidth 320); a double-click resets it.
fun SupportingPane(supportingVisible: Boolean, onSupportingDismissRequest: () -> Unit,
    modifier: Modifier = Modifier, supportingHeader: (@Composable () -> Unit)? = null,
    main: @Composable SupportingPaneScope.() -> Unit,
    supporting: @Composable SupportingPaneScope.() -> Unit)
// Inline pane from a 920dp container (SupportingPaneScope.isSupportingInline), else an AdaptiveSheet.
fun ContentColumn(modifier: Modifier = Modifier, maxWidth: Dp = AdaptiveDefaults.ReadableWidth,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit)
fun AdaptiveGrid(modifier: Modifier = Modifier, minCellWidth: Dp = AdaptiveDefaults.GridMinCellWidth,
    maxColumns: Int = Int.MAX_VALUE, maxWidth: Dp = AdaptiveDefaults.ContentWidth,
    state: LazyGridState = rememberLazyGridState(), contentPadding: PaddingValues = PaddingValues(),
    horizontalSpacing: Dp = ArecaTheme.spacing.lg, verticalSpacing: Dp = ArecaTheme.spacing.xl,
    content: LazyGridScope.() -> Unit)
fun AdaptiveSheet(visible: Boolean, onDismissRequest: () -> Unit, modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null, footer: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit)
// FlowModal (capped at 640dp) below an expanded window or on a short one; Dialog otherwise.
class AdaptiveMenuItem(val label: String, val icon: ImageVector? = null, val destructive: Boolean = false,
    val enabled: Boolean = true, val onClick: () -> Unit)
fun AdaptiveMenu(state: DropdownPopupState, actions: List<AdaptiveMenuItem>, modifier: Modifier = Modifier,
    title: String? = null)
// Trigger: Modifier.anchored(state.anchorState) and state.show(). DropdownPopup under a pointer or
// from an expanded window up; a sheet with `title` at its head under touch on narrower windows.
fun Modifier.contextMenu(state: DropdownPopupState, enabled: Boolean = true,
    label: String = "Show actions"): Modifier
// On the row, before its clickable: right-click (menu at the pointer), long press under touch, Menu
// key or Shift+F10, and the accessibility long-click (named `label`) open state's AdaptiveMenu; the
// gesture that opens it never also clicks.
// AdaptiveDefaults: ReadableWidth 640, ContentWidth 1200, TwoPaneMinWidth 600,
// SupportingPaneMinWidth 920, SidebarWidth 256, FloatingSidebarWidth 300, GridMinCellWidth 160, ListPaneMinWidth 240,
// DetailPaneMinWidth 320 (dp); listPaneWidth(container),
// supportingPaneWidth(container); usesDialog(window) — true from an expanded width up unless the
// window is short. A sheet of your own: if (AdaptiveDefaults.usesDialog(ArecaTheme.hostWindow))
// Dialog(…) else FlowModal(…). ConfirmSheet already does this.
```

```kotlin
fun ArecaDarkTheme(content: @Composable () -> Unit)

fun Modifier.horizontalContentInsets(): Modifier   // also verticalContentInsets(), contentInsets()
fun <T> Duration.tween(easing: Easing = FastOutSlowInEasing): TweenSpec<T>
fun <T> ArecaMotion.slide(): SpringSpec<T> // critically damped, stiffness = motion.springStiffness
fun AnnounceOnChange(text: String?)
fun SystemBars(color: Color, lightIcons: Boolean = color.luminance() < 0.5f) // Android only acts
fun Modifier.minimumTouchTarget(minimum: Dp = MinimumTouchTarget): Modifier
fun Modifier.clickCursor(enabled: Boolean = true): Modifier
fun Modifier.thenIf(condition: Boolean, modifier: @Composable Modifier.() -> Modifier): Modifier
// TouchIndication(shape), FadeIndication, StrokeIndication, LiquidSweepIndication: pass one as the
// `indication` of Modifier.clickable on a custom pressable surface (recipes.md).
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
// Under a pointer the icon overloads show contentDescription as a tooltip.
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
// Overload for slots and computed styles: `style: TextStyle = LocalTextStyle.current`,
// `color: Color = LocalContentColor.current` after `modifier`, in place of `variant` and `TextColor`.
// Prefer the variant overload; in a component slot, plain `Text("…")` inherits the slot's style.

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
// Overload: `painter: Painter`. FeaturedIconStyle.solid / soft / outlined(color: ActionColor = Brand).

fun Chevron(direction: ChevronDirection, modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    size: ChevronSize = LocalChevronSize.current ?: ChevronSize.Medium,
    strokeCap: StrokeCap = StrokeCap.Round,
    animationSpec: AnimationSpec<Float> = ArecaTheme.motion.let { it.fast.tween(it.standard) })
// A disclosure arrow that animates between directions. In a list row use ListItemChevron.
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

fun <Value> SelectFlowModal(controller: FlowModalController, values: Set<Value>,
    options: List<SelectOption<Value>>, modifier: Modifier = Modifier,
    onChange: (Set<Value>) -> Unit = {},
    state: SelectFlowModalState = rememberSelectFlowModalState(),
    selectionMode: SelectSelectionMode = SelectSelectionMode.Single,
    style: FlowModalStyle = …, size: FlowModalSize = …, header: (@Composable () -> Unit)? = null,
    footer: (@Composable (draftValues: Set<Value>) -> Unit)? = null,
    onQueryChange: ((String) -> Unit)? = null, searchPlaceholder: String = "Search",
    emptyContent: (@Composable (query: String) -> Unit)? = { … EmptyContent(query) },
    loadingContent: (@Composable (query: String) -> Unit)? = { … LoadingContent(query) },
    content: @Composable (options: List<SelectOption<Value>>, values: Set<Value>,
        onSelect: (SelectOption<Value>) -> Unit) -> Unit = { … SelectFlowModalDefaults.List(…) })
// A picker in a sheet, opened with controller.show(). Search is opt-in and done by the caller:
// pass `onQueryChange` to get a search field, then filter or fetch and pass the matching
// `options`; set `state.isSearching` while results are outstanding. Without it, no search field.
// `footer` receives the draft selection, which an apply button commits.
// rememberSelectFlowModalState(initialQuery: String = "")

fun FieldRow(modifier: Modifier = Modifier, style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size, field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true, readOnly: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable FieldRowScope.() -> Unit)
// One field frame holding segments side by side: a country code and a number.

fun FieldRowScope.TextSegment(value: String, onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier, weight: Float = 1f, enabled: Boolean = true,
    readOnly: Boolean = false, format: ((String) -> String)? = null,
    padding: PaddingValues = contentPadding, loading: Boolean = false,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true, cursorBrush: Brush = SolidColor(ArecaTheme.colors.accentBase),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() })

fun <Value> FieldRowScope.SelectSegment(values: Set<Value>, onChange: (Set<Value>) -> Unit,
    options: List<SelectOption<Value>>, modifier: Modifier = Modifier,
    width: Dp = Dp.Unspecified, weight: Float = 0f,
    selectionMode: SelectSelectionMode = SelectSelectionMode.Single, enabled: Boolean = true,
    loading: Boolean = false, padding: PaddingValues = contentPadding,
    empty: Boolean = values.isEmpty(), placement: AnchoredPlacement = AnchoredPlacement.BelowStart,
    dropdownStyle: DropdownPopupStyle = …, dropdownSize: DropdownPopupSize = DropdownPopupSize.Wide,
    labels: SelectFieldLabels = SelectFieldLabels.English, placeholder: …, leading: …,
    trailing: …, valueContent: …, content: … /* as SelectField */)

fun Field(modifier: Modifier = Modifier, style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size, field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true, readOnly: Boolean = false, loading: Boolean = false,
    empty: Boolean = false, interactionSource: MutableInteractionSource? = null,
    onClick: (() -> Unit)? = null, ownsControl: Boolean = onClick != null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit)
// The bare field frame every field draws: for a custom control that must look like a field (a
// date button that opens a picker). With `onClick` the frame is the pressable control.

fun FieldGroupLayout(modifier: Modifier = Modifier, label: (@Composable () -> Unit)? = null,
    message: (@Composable () -> Unit)? = null, size: FieldSize = FieldDefaults.size,
    labelPlacement: FieldLabelPlacement.External = FieldLabelPlacement.Above,
    messageBehavior: FieldMessageBehavior = FieldMessageBehavior.Animated,
    labelSpace: Dp = size.label.gap, messageSpace: Dp = size.message.gap,
    content: @Composable () -> Unit)
// FieldGroup's layout alone, with your own label and message slots and no FieldScope wiring.
// Prefer FieldGroup, which also announces the error and links the label to the field.
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

```kotlin
fun NumberStepper(value: Int, onValueChange: (Int) -> Unit, modifier: Modifier = Modifier,
    style: NumberStepperStyle = …, size: NumberStepperSize = …, shape: Shape = ArecaTheme.shapes.md,
    minValue: Int = Int.MIN_VALUE, maxValue: Int = Int.MAX_VALUE,
    labels: NumberStepperLabels = NumberStepperLabels.English, enabled: Boolean = true,
    decreaseContent: @Composable () -> Unit = { … "−" }, increaseContent: … = { … "+" },
    valueContent: @Composable BoxScope.(Int) -> Unit = { … })
// NumberStepperLabels(decrease: String, increase: String); English = "Decrease" / "Increase".

fun Swatch(modifier: Modifier = Modifier, style: SwatchStyle = …, size: SwatchSize = …,
    shape: Shape = ArecaTheme.shapes.full, selected: Boolean? = null,
    contentDescription: String? = null, enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null, onClick: (() -> Unit)? = null,
    content: (@Composable BoxScope.() -> Unit)? = null)
// One colour chip; `selected = null` draws no selection state.

fun SwatchPicker(items: List<ColorShade>, selected: ColorItem?, onChange: (color: ColorItem) -> Unit,
    modifier: Modifier = Modifier, style: SwatchPickerStyle = …, size: SwatchPickerSize = …,
    layout: SwatchPickerLayout = SwatchPickerLayout.GroupedColumns,
    contentPadding: PaddingValues = PaddingValues(0.dp), enabled: Boolean = true)
// ColorShade(id: String, name: String, items: List<ColorItem>)
// ColorItem(id: String, name: String, border: Color, bg: Color); `name` is the swatch's label.
// SwatchPickerLayout.GroupedColumns (a column per shade, scrolling sideways) | FlatRow.
```

A row of options with one thumb sliding to the chosen one — segments, a pill under words,
underlines, a three-state toggle. `Tabs` and `FloatingTabBar` are built on it.

```kotlin
fun <T> SlidingSelector(items: List<SelectorItem<T>>, selected: T?, onSelect: (T?) -> Unit,
    modifier: Modifier = Modifier, mode: SelectorMode = SelectorMode.Required,
    style: SlidingSelectorStyle = LocalSlidingSelectorStyle.current ?: SlidingSelectorStyle.Segmented,
    size: SlidingSelectorSize = LocalSlidingSelectorSize.current ?: SlidingSelectorSize.Default,
    layout: SelectorLayout = SelectorLayout.Fill,
    orientation: SelectorOrientation = SelectorOrientation.Horizontal)
// SelectorItem(value: T, enabled: Boolean = true, thumb: Color = Color.Unspecified,
//     contentSelected: Color = Color.Unspecified, contentDescription: String? = null,
//     content: @Composable SelectorItemScope.() -> Unit); SelectorItemScope has `selected`, `enabled`.
// `thumb` / `contentSelected` override the style's while that option is chosen; the thumb
// cross-fades between colours as it moves. `contentDescription` for an icon, or "Has iris".
// SelectorMode.Required (radio buttons; tapping the chosen option does nothing; onSelect never gets
// null) | Optional (switches; tapping the chosen option calls onSelect(null)).
// SelectorLayout.Fill (equal shares, fills the width) | Hug (each option its content) | Scroll
// (overflows and keeps the chosen option in view). SelectorOrientation.Horizontal | Vertical.
// SlidingSelectorStyle presets: Segmented (Default: track + raised capsule), Pill (no track, a
// capsule under the words, fgOnFill on it), Underline (divider + sliding bar), Button (flat fill,
// no track); default(content, contentSelected, contentDisabled, track, trackBorder, trackShape,
// trackBorderWidth, trackPadding, thumb, thumbKind, thumbShape, thumbElevation, thumbThickness,
// thumbLength, thumbPadding, thumbEdge, divider, dividerThickness, itemShape, indication, motion).
// SelectorThumbKind.Fill | Underline | None; SelectorEdge.Start | End (underline and divider edge).
// `motion: FiniteAnimationSpec<Float>` defaults to ArecaTheme.motion.slide().
// SlidingSelectorSize.Small | Default | Large; default(gap, itemPadding, itemMinWidth,
// itemMinHeight, iconSize, textStyle).
// Opens with the thumb in place; grows in on a first choice, shrinks away when cleared; fades
// between places when ArecaMotion.reduceMotion is set.
```

Filter chips: a group of `Badge` choices.

```kotlin
fun <T> BadgeSelect(options: List<BadgeOption<T>>, onSelect: (option: BadgeOption<T>) -> Unit,
    selected: List<T>, modifier: Modifier = Modifier,
    selectionMode: BadgeSelectionMode = BadgeSelectionMode.Single,
    style: BadgeStyle = LocalBadgeStyle.current ?: BadgeStyle.Default,
    size: BadgeSize = LocalBadgeSize.current ?: BadgeSize.Default,
    shape: Shape = ArecaTheme.shapes.full, gap: Dp = ArecaTheme.spacing.md)
// One row, no wrapping or scrolling.

fun <T> FlowBadgeSelect(options: List<BadgeOption<T>>, onSelect: (BadgeOption<T>) -> Unit,
    selected: List<T>, modifier: Modifier = Modifier,
    selectionMode: BadgeSelectionMode = BadgeSelectionMode.Single,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    itemVerticalAlignment: Alignment.Vertical = Alignment.Top,
    maxItemsInEachRow: Int = Int.MAX_VALUE, maxLines: Int = Int.MAX_VALUE,
    style: BadgeStyle = …, size: BadgeSize = …, shape: Shape = ArecaTheme.shapes.full)
// Wraps onto further lines.

fun <T> LazyBadgeSelect(options: List<BadgeOption<T>>, onSelect: (BadgeOption<T>) -> Unit,
    selected: List<T>, modifier: Modifier = Modifier,
    selectionMode: BadgeSelectionMode = BadgeSelectionMode.Single, style: BadgeStyle = …,
    size: BadgeSize = …, shape: Shape = ArecaTheme.shapes.full,
    containerPadding: PaddingValues = PaddingValues(0.dp), gap: Dp = ArecaTheme.spacing.md,
    key: ((value: T) -> Any)? = null)
// Scrolls sideways; give `containerPadding` the screen gutter so chips scroll to the edge.

// BadgeOption(label: @Composable () -> Unit, value: T, enabled: Boolean = true,
//     leading: (@Composable () -> Unit)? = null, trailing: (@Composable () -> Unit)? = null)
// BadgeSelectionMode.Single (radio semantics) | Multiple (checkboxes). The group holds no
// selection: `onSelect` reports the pressed option and the caller updates `selected`.
// Chips: style = BadgeStyle.Chip (or FilledChip), size = BadgeSize.Chip.
```

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
    bottom: (@Composable ColumnScope.() -> Unit)? = null, showChevron: Boolean = true,
    container: ExpandableListItemContainer = ExpandableListItemContainer.None,
    content: @Composable ColumnScope.() -> Unit)
// ExpandableListItemContainer.None | Soft: a `bgSoft` block of `shapes.lg` behind the row and its
// content while expanded; keep the row's style transparent. `showChevron = false` only when the
// row already says it opens. `bottom` sits under the row's text, inside the row and its hit area —
// a meter under a category's name — unlike `content`, which is revealed under the row.

fun List(modifier: Modifier = Modifier,
    itemStyle: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    itemSize: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    dividerStyle: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    dividerSize: DividerSize = LocalDividerSize.current ?: DividerSize.Default,
    listStyle: ListStyle = LocalListStyle.current ?: ListStyle.Default,
    listSize: ListSize = LocalListSize.current ?: ListSize.Default, separated: Boolean = true,
    enabled: Boolean = true, contentPadding: PaddingValues = PaddingValues(0.dp),
    content: ListScope.() -> Unit)
// Overload: a leading `entries: List<ListEntry>` and no `content`.

fun LazyList(modifier: Modifier = Modifier, state: LazyListState = rememberLazyListState(),
    itemStyle: ListItemStyle = …, itemSize: ListItemSize = …, dividerStyle: DividerStyle = …,
    dividerSize: DividerSize = …, listStyle: ListStyle = …, listSize: ListSize = …,
    separated: Boolean = true, enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp), content: ListScope.() -> Unit)
// Overload: a leading `entries: List<ListEntry>` and no `content`.

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

```kotlin
fun ListItemColumn(modifier: Modifier = Modifier, listStyle: ListStyle = …, listSize: ListSize = …,
    separated: Boolean = true, enabled: Boolean = true, content: @Composable () -> Unit)
// The list surface for rows you compose yourself (ListItem, NavigationListItem, …), with dividers.

fun ListItemSkeleton(modifier: Modifier = Modifier, size: ListItemSize = …,
    showLeading: Boolean = false, showSubtitle: Boolean = true, showTrailing: Boolean = false,
    leadingSize: Dp = size.icon + 12.dp, leadingShape: Shape = CircleShape,
    titleWidthFraction: Float = 0.45f, subtitleWidthFraction: Float = 0.7f,
    valueWidth: Dp? = null, style: ListItemStyle = …)

fun ListItemChevron(modifier: Modifier = Modifier,
    direction: ChevronDirection = ChevronDirection.Right,
    size: ChevronSize = currentListItemSize().chevron, hoverOffset: Dp = 2.dp)
fun ListItemCheckmark(modifier: Modifier = Modifier,
    selected: Boolean = LocalListItemState.current.selected,
    size: Dp = currentListItemSize().icon, color: Color = LocalContentColor.current,
    strokeWidth: Dp = 2.dp)
// Trailing glyphs for a hand-built ListItem: the row's chevron, and its selection tick.
```

Elided list-row parameters (`…`) have the same names, types and defaults as in `ListItem`.

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

fun SettingsBlock(modifier: Modifier = Modifier,
    contentPadding: PaddingValues = SettingsTheme.sizes.row.contentPadding,
    content: @Composable ColumnScope.() -> Unit)
// Free content inside a Section, padded like a row: a chart, a paragraph, a button row.

fun SettingsRowSkeleton(modifier: Modifier = Modifier, showLeading: Boolean = false,
    showSubtitle: Boolean = false, showTrailing: Boolean = false, titleWidthFraction: Float = 0.45f)
// A loading placeholder shaped like a settings row.
```

A row built by hand next to settings rows reads its type and ink from `SettingsTheme.sizes.row` and
`SettingsTheme.styles.row`.

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
// CardFooterStyle presets: Default (= plain()), plain(divider), soft(color: ActionColor = Neutral),
// solid(color: ActionColor = Brand), of(bg, content, divider, button) for a color of your own. The footer
// hands its `content` color and its `button` style to what is inside it.

fun VerticalScrollbar(state: ScrollState, modifier: Modifier = Modifier)
// Overloads: state: LazyListState, state: LazyGridState. HorizontalScrollbar(state: ScrollState |
// LazyListState, modifier). Drawn under a pointer only; place it in a Box beside the scrolling
// element: VerticalScrollbar(scroll, Modifier.align(Alignment.CenterEnd).fillMaxHeight()).
// List, ContentList, AdaptiveGrid, GroupedGrid, NavList, Table and LazyTable draw their own.

fun HorizontalDivider(modifier: Modifier = Modifier,
    style: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    size: DividerSize = LocalDividerSize.current ?: DividerSize.Default, text: String? = null,
    textPosition: DividerTextPosition = DividerTextPosition.Center)

fun VerticalDivider(modifier: Modifier = Modifier, style: DividerStyle = …,
    size: DividerSize = …, text: String? = null,
    textPosition: DividerTextPosition = DividerTextPosition.Center)

fun Expandable(expanded: Boolean, modifier: Modifier = Modifier,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    transition: ExpandableTransition = ExpandableTransition.Default,
    trigger: (@Composable (expanded: Boolean, onToggle: () -> Unit) -> Unit)? = null,
    content: @Composable () -> Unit)
// Overload: `state: ExpandableState` (from rememberExpandableState(initialExpanded = false);
// expand(), collapse(), toggle(), expanded) in place of `expanded` / `onExpandedChange`, with a
// required `trigger`.

fun SlotRow(modifier: Modifier = Modifier, layout: SlotRowLayout = SlotRowLayout.Default,
    start: (@Composable BoxScope.() -> Unit)? = null, center: @Composable BoxScope.() -> Unit,
    end: (@Composable BoxScope.() -> Unit)? = null)
// SlotRowLayout.Default | Auto | Center | Top | FirstLine | Centered | Fill | wrap(startGap, endGap).
// With Auto, put Modifier.firstLineCenter(textStyle) on the text that is the row's first line.

fun BoxScope.Overlay(color: Color, modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center, content: @Composable BoxScope.() -> Unit)
// A tint over the whole parent box, content centred on it, without changing the box's size:
// a spinner over a loading avatar. Declare it last. Dialogs and sheets have their own scrim.

fun RelativePositionLayout(position: RelativePosition, modifier: Modifier = Modifier,
    space: Dp = ArecaTheme.spacing.md, overlay: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit)
// Places `overlay` as a sibling on one side of `content`, taking real space: a percentage above a
// progress bar, a caption under a chart. RelativePosition.TopStart | TopCenter | TopEnd |
// BottomStart | BottomCenter | BottomEnd | Start | End.

@ExperimentalArecaApi
fun PullToRefreshLayout(isRefreshing: Boolean, onRefresh: () -> Unit,
    modifier: Modifier = Modifier, style: PullToRefreshStyle = …, size: PullToRefreshSize = …,
    enabled: Boolean = true, state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.() -> Unit = { PullToRefreshIndicator(…) },
    content: @Composable BoxScope.() -> Unit)

@ExperimentalArecaApi
fun PullToRefreshIndicator(state: PullToRefreshState, isRefreshing: Boolean,
    modifier: Modifier = Modifier, style: PullToRefreshStyle = …, size: PullToRefreshSize = …)
// rememberPullToRefreshState() (experimental too). PagedContentList(onRefresh = …) needs no opt-in.

fun CollapsingHeaderLayout(header: @Composable () -> Unit, pinned: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    state: CollapsingHeaderState = rememberCollapsingHeaderState(),
    collapsed: (@Composable () -> Unit)? = null, expandOnlyWhenScrolledToTop: Boolean = true,
    expandDuration: Duration = ArecaTheme.motion.medium, content: @Composable (Modifier) -> Unit)
// `content` brings its own scrolling container. `collapsed`: a compact band (opaque ground) the
// header stops at and fades in over it, holding a title and a figure. Once the header has fully
// collapsed, assistive technology reads the band instead of the header, never both. Read
// `state.collapsedFraction` (0f shown … 1f collapsed as far as it goes) to drive your own fade.
```

`BaseLayout` lays `content` out at full size under the bars; `content` must apply the padding it is
given (a lazy list's `contentPadding`, or `Modifier.padding`). The top bar's height and
`contentWindowInsets` add up, so give the status-bar inset to the top bar's `windowInsets` and the
rest to `contentWindowInsets`. A `Card`'s content is handed its padding rather than padded for it.

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

fun <T : Any> FloatingTabBar(items: List<TabBarItem<T>>, selectedValue: T?,
    onValueSelected: (T) -> Unit, modifier: Modifier = Modifier,
    style: FloatingTabBarStyle = LocalFloatingTabBarStyle.current ?: FloatingTabBarStyle.Default,
    size: FloatingTabBarSize = LocalFloatingTabBarSize.current ?: FloatingTabBarSize.Default,
    indicatorStyle: FloatingTabBarIndicatorStyle = FloatingTabBarIndicatorStyle.underline(),
    windowInsets: WindowInsets =
        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom),
    animationSpec: FiniteAnimationSpec<Dp> = ArecaTheme.motion.slide())
// Built on SlidingSelector: the indicator grows in when a value is selected after none and shrinks
// away when the selection is cleared. Selecting the selected tab again is reported.

fun FloatingTabBarItemContent(modifier: Modifier = Modifier, label: String? = null,
    icon: @Composable () -> Unit)
// The usual body of a FloatingTabBar item: an icon over an optional one-line label.

// TabBar and FloatingTabBar: overload with `state: TabBarState<T>` (from
// rememberTabBarState(items, initialSelectedValue = null)) in place of `selectedValue` /
// `onValueSelected`. Both apply the bottom inset themselves.
// TabBarItem(value: T, enabled: Boolean = true, content: @Composable TabBarItemScope.() -> Unit);
// TabBarItemScope has `selected` and `enabled`.

fun <T> Tabs(tabs: List<TabItem<T>>, selectedValue: T, onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier, style: TabsStyle = LocalTabsStyle.current ?: TabsStyle.Default,
    size: TabsSize = LocalTabsSize.current ?: TabsSize.Default,
    variant: TabsVariant = TabsVariant.Underline,
    orientation: TabsOrientation = TabsOrientation.Horizontal,
    horizontalPosition: TabsHorizontalPosition = TabsHorizontalPosition.Bottom,
    verticalPosition: TabsVerticalPosition = TabsVerticalPosition.Start, fillWidth: Boolean = false)
// Overload: `state: TabsState<T>` (from `rememberTabsState(initialValue)`, read
// `state.selectedValue`) in place of `selectedValue` / `onValueSelected`; it takes `fillWidth` too.
// TabsVariant.Underline (views within a screen) | Button | Segmented (a setting). Segmented: tabs
// in a `TabsStyle.track` capsule with a raised `thumb` sliding to the selection; pair it with
// `size = TabsSize.Segmented` — the size does not follow the variant. Button: a flat
// `bgSelected` fill that slides. `fillWidth = true`: equal shares of the width, no scrolling.
// Built on SlidingSelector; the indicator moves on ArecaTheme.motion.slide() unless
// `TabsSize.indicatorDuration` differs from `motion.medium` (then a tween of that length). For a
// setting rather than a view, or one that may be unset, prefer SlidingSelector.
// TabItem(value: T, enabled: Boolean = true, content: @Composable TabItemScope.() -> Unit)
```

Bar controls are `TopBarActionScope` extensions for a bar's `leading` and `trailing` slots:
`BackAction`, `CloseAction`, `OverflowAction`, `AddAction`, `MenuAction`, `NotificationsAction`,
`FilterAction`, `SearchAction`, `EditAction`, `SettingsAction`, `ShareAction`, `DownloadAction`,
`DeleteAction`, `FavoriteAction`, `DoneAction`, `CancelAction`, `IconAction`, `TextAction`,
`AvatarAction` and the base `Action`. Every `contentDescription` or `text` is an English default
to replace with the app's string.

```kotlin
fun TopBarActionScope.BackAction(onClick: () -> Unit, modifier: Modifier = Modifier,
    label: String? = null, contentDescription: String? = "Back",
    color: ActionColor = ActionColor.Neutral, enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null)
fun TopBarActionScope.CloseAction(onClick: () -> Unit, modifier: Modifier = Modifier,
    contentDescription: String? = "Close", color: ActionColor = ActionColor.Neutral,
    enabled: Boolean = true, interactionSource: MutableInteractionSource? = null)
// OverflowAction ("More") and AddAction ("Add") take the same parameters as CloseAction.
fun TopBarActionScope.MenuAction(onClick: () -> Unit, modifier: Modifier = Modifier,
    contentDescription: String? = "Menu", color: ActionColor = ActionColor.Neutral,
    enabled: Boolean = true, badge: String? = null,
    interactionSource: MutableInteractionSource? = null)
fun TopBarActionScope.NotificationsAction(onClick: () -> Unit, modifier: Modifier = Modifier,
    badge: String? = null, contentDescription: String? = "Notifications", enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null)
// FilterAction: the same, with contentDescription = "Filters".
fun TopBarActionScope.SearchAction(onClick: () -> Unit, modifier: Modifier = Modifier,
    contentDescription: String? = "Search", enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null)
// EditAction ("Edit") and SettingsAction ("Settings") take the same parameters as SearchAction.
fun TopBarActionScope.ShareAction(onClick: () -> Unit, modifier: Modifier = Modifier,
    contentDescription: String? = "Share", enabled: Boolean = true, loading: Boolean = false,
    interactionSource: MutableInteractionSource? = null)
// DownloadAction: the same, with contentDescription = "Download".
fun TopBarActionScope.DeleteAction(onClick: () -> Unit, modifier: Modifier = Modifier,
    contentDescription: String? = "Delete", color: ActionColor = ActionColor.Danger,
    enabled: Boolean = true, loading: Boolean = false,
    interactionSource: MutableInteractionSource? = null)
fun TopBarActionScope.FavoriteAction(selected: Boolean, onClick: () -> Unit,
    modifier: Modifier = Modifier, contentDescription: String? = "Favorite",
    selectedColor: ActionColor = ActionColor.Danger, enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null)
fun TopBarActionScope.DoneAction(onClick: () -> Unit, modifier: Modifier = Modifier,
    text: String = "Done", enabled: Boolean = true, loading: Boolean = false,
    interactionSource: MutableInteractionSource? = null)
fun TopBarActionScope.CancelAction(onClick: () -> Unit, modifier: Modifier = Modifier,
    text: String = "Cancel", enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null)
fun TopBarActionScope.IconAction(icon: ImageVector, contentDescription: String?,
    onClick: () -> Unit, modifier: Modifier = Modifier, color: ActionColor = ActionColor.Neutral,
    enabled: Boolean = true, loading: Boolean = false, badge: String? = null,
    interactionSource: MutableInteractionSource? = null)
fun TopBarActionScope.TextAction(text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    color: ActionColor = ActionColor.Brand, enabled: Boolean = true, loading: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable () -> Unit)? = null, trailing: (@Composable () -> Unit)? = null)
fun TopBarActionScope.AvatarAction(onClick: () -> Unit, modifier: Modifier = Modifier,
    letters: String? = null, contentDescription: String? = "Account", enabled: Boolean = true,
    loading: Boolean = false, interactionSource: MutableInteractionSource? = null,
    content: (@Composable BoxScope.() -> Unit)? = null)
fun TopBarActionScope.Action(onClick: (() -> Unit)?, modifier: Modifier = Modifier,
    color: ActionColor = ActionColor.Neutral, enabled: Boolean = true, loading: Boolean = false,
    badge: String? = null, interactionSource: MutableInteractionSource? = null,
    content: @Composable BoxScope.() -> Unit)
// The base control the others are built on: any content in the bar's control frame, labelled by
// what you put in it. Prefer a named control or IconAction.
```

Controls without a `color` parameter draw in `ActionColor.Neutral`, except `DoneAction`
(`ActionColor.Brand`) and a selected `FavoriteAction` (`selectedColor`).

## Navigation and paging

```kotlin
fun NavList(nodes: List<NavNode>, selectedId: String?, onItemClick: (NavData) -> Unit,
    modifier: Modifier = Modifier, size: NavListSize = LocalNavListSize.current ?: NavListSize.Default)
// NavNode.Item(item: NavData) | NavNode.Section(trigger: NavData, children: List<NavData>,
//     expandable: Boolean = true, initiallyExpanded: Boolean = false)
// NavData(id: String, title: String, enabled: Boolean = true)

fun NavItem(modifier: Modifier = Modifier, style: NavItemStyle = …, size: NavItemSize = …,
    selected: Boolean = false, indent: Dp = 0.dp, enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null, onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null, trailing: (@Composable () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit)
// One sidebar row, for a navigation list NavList's nodes cannot express.

fun rememberPagerController(pageCount: Int, initialPage: Int = 0): PagerController
// PagerController: pagerState, pageCount, currentPage, isFirstPage, isLastPage, next(),
// previous(), goTo(page, animated = true).

fun SwipePager(controller: PagerController, modifier: Modifier = Modifier,
    userScrollEnabled: Boolean = true, beyondViewportPageCount: Int = 0,
    controls: (@Composable () -> Unit)? = null, page: @Composable (index: Int) -> Unit)

fun PagerControls(controller: PagerController, actionText: String?, modifier: Modifier = Modifier,
    onAction: () -> Unit = controller::next, indicatorDescription: String? = null,
    buttonStyle: ButtonStyle = LocalButtonStyle.current ?: ButtonStyle.solid(),
    indicatorStyle: PageIndicatorStyle = …, indicatorSize: PageIndicatorSize = …,
    spacing: Dp = ArecaTheme.spacing.xl2,
    contentPadding: PaddingValues = …, windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0))
// `contentPadding` defaults to spacing.xl3 at the sides and top and spacing.xl below.
// A page indicator over the action button ("Next", then "Get started" on the last page).

fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier = Modifier,
    currentPageOffset: Float = 0f, contentDescription: String? = null,
    variant: PageIndicatorVariant = PageIndicatorVariant.Elongated, progress: () -> Float = { 0f },
    onPageClick: ((page: Int) -> Unit)? = null,
    style: PageIndicatorStyle = …, size: PageIndicatorSize = …)
// Overload: `pagerState: PagerState` in place of the page numbers and offset.
// PageIndicatorVariant: Elongated (default), Dots, Shrinking(window = 5), Line, Segments.
// `progress` fills the current segment (autoplay); `onPageClick` is pointer-only, the marks stay
// one element named by `contentDescription`. Segments fill the width the modifier gives them.
// PageIndicatorStyle(current, other, capsule = null): default(…), onImage() — white marks on a
// translucent capsule. PageIndicatorSize adds lineThickness and lineLength.

fun PageCounter(currentPage: Int, pageCount: Int, modifier: Modifier = Modifier,
    contentDescription: String? = null,
    text: (index: Int, count: Int) -> String = { i, n -> "${i + 1} / $n" },
    style: PageCounterStyle = …, size: PageCounterSize = …)
// Overload: `pagerState: PagerState`. "3 / 9" in a capsule, tabular figures.
// PageCounterStyle(bg, content): default(), onImage(). PageCounterSize(padding, textStyle, shape).

fun StepIndicator(stepCount: Int, currentStep: Int, modifier: Modifier = Modifier,
    contentDescription: String? = null, style: StepIndicatorStyle = …,
    size: StepIndicatorSize = LocalStepIndicatorSize.current ?: StepIndicatorSize.Compact)
// Progress through the steps of a flow ("Step 2 of 4" as its description).
```

## Wheel picker

```kotlin
fun WheelPicker(modifier: Modifier = Modifier, style: WheelPickerStyle = …,
    size: WheelPickerSize = …, scroll: WheelPickerScroll? = LocalWheelPickerScroll.current,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    backgroundDecoration: @Composable BoxScope.() -> Unit = { WheelPickerHighlight() },
    foregroundDecoration: @Composable BoxScope.() -> Unit = { WheelPickerEdgeFade() },
    columns: @Composable RowScope.() -> Unit)

fun <T> WheelPickerColumn(items: List<T>, state: WheelPickerColumnState,
    modifier: Modifier = Modifier, style: WheelPickerStyle = …, size: WheelPickerSize = …,
    scroll: WheelPickerScroll? = LocalWheelPickerScroll.current, enabled: Boolean = true,
    scrollPropagation: WheelPickerScrollPropagation = WheelPickerScrollPropagation.Contain,
    hapticFeedback: Boolean = true, contentDescription: String? = null,
    valueDescription: (item: T) -> String = { item -> item.toString() },
    onSettledIndexChange: ((index: Int) -> Unit)? = null,
    itemContent: @Composable WheelPickerItemScope.(item: T) -> Unit =
        { item -> WheelPickerItem(item.toString()) })

fun rememberWheelPickerColumnState(initialIndex: Int = 0, infinite: Boolean = false):
    WheelPickerColumnState
// WheelPickerColumnState: currentIndex, settledIndex, itemCount, isScrollInProgress,
// suspend animateTo(index), suspend scrollTo(index). Read the choice from `settledIndex` or
// `onSettledIndexChange`.

fun WheelPickerItemScope.WheelPickerItem(text: String, modifier: Modifier = Modifier,
    style: WheelPickerStyle = …)
// The default row text; use it in a custom `itemContent` to keep the selected-row styling.

fun BoxScope.WheelPickerHighlight(style: WheelPickerStyle = …, size: WheelPickerSize = …)
fun BoxScope.WheelPickerEdgeFade(style: WheelPickerStyle = …, size: WheelPickerSize = …)
// The default decorations: the band behind the selected row, and the fade at the top and bottom
// edges. Pass your own `backgroundDecoration` / `foregroundDecoration` to replace or drop them.
```

Dates and times are `dev.voir.areca:datetime`, built on this picker; see
[datetime.md](datetime.md).

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
// The drag handle floats over the top of the sheet: `header` starts at the sheet's top edge, so its
// fill runs under the handle, and clears it with `FlowModalHeaderInsets` (a WindowInsets, zero when
// the sheet cannot be dragged): `TopBar(…, windowInsets = FlowModalHeaderInsets)`. Without a header
// the body starts below the handle.
// FlowModalSize.default(dragPadding, dragWidth = 44.dp, dragHeight = 4.dp,
//     maxWidth: Dp = Dp.Unspecified): `maxWidth` caps the sheet, centred along the bottom edge; the
// scrim still covers the window. A sheet opens in its own window, so an app that keeps a phone
// layout on a tablet caps its sheets here — `LocalFlowModalSize provides
// FlowModalSize.default(maxWidth = …)` — as well as its screens.

fun FlowModalActionBar(confirmText: String, onConfirm: () -> Unit, modifier: Modifier = Modifier,
    enabled: Boolean = true, loading: Boolean = false, style: FlowModalActionBarStyle = …,
    size: FlowModalActionBarSize = …,
    layout: FlowModalActionBarLayout = FlowModalActionBarLayout.Trailing,
    showDivider: Boolean = true, leading: (@Composable () -> Unit)? = null)
fun FlowModalSecondaryAction(text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    enabled: Boolean = true, style: FlowModalActionBarStyle = …, size: FlowModalActionBarSize = …)
// FlowModalActionBarLayout.Trailing (commit at its own width) |
// Split(secondaryWeight: Float = 1f, confirmWeight: Float = 2f): both ends stretched by weight —
// "Cancel" beside "Compare with September". The secondary action in `leading` stretches with it.

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
// AnchoredPlacement.BelowStart (default) | BelowCenter | BelowEnd | AboveStart | AboveCenter |
// AboveEnd; it flips to the other side when it would run off screen.

fun TooltipBox(text: String, modifier: Modifier = Modifier,
    placement: AnchoredPlacement = AnchoredPlacement.BelowCenter, enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit)
// Under a pointer only: shows after the pointer rests 500ms, or at once on keyboard focus; hides on
// exit or press. The label is visual only — give the content its own description too.
```

`LocalConfirmSheetStyle` and `LocalConfirmSheetSize` stay `null` unless provided: a confirm sheet
builds its default from the `LocalFlowModalStyle` / `LocalFlowModalSize` in scope where it is drawn,
so restyle sheets there.

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

fun Toast(id: Long, label: CharSequence, modifier: Modifier = Modifier,
    caption: CharSequence? = null, style: ToastStyle = …, shape: Shape = ArecaTheme.shapes.md,
    alignment: Alignment.Vertical = Alignment.Top, onClick: ((id: Long) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable RowScope.(id: Long) -> Unit)? = null,
    trailing: (@Composable RowScope.(id: Long) -> Unit)? = null)
// The toast body ToastHost draws; compose it yourself only for a preview or a custom host.

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
// LocalNoticeStyle is null by default, so each notice follows its tone; providing a style gives
// every notice in the subtree that one look.

fun RequirementList(requirements: List<Requirement>, modifier: Modifier = Modifier,
    columns: Int = 1, labels: RequirementListLabels = RequirementListLabels.English,
    contentDescription: String? = null, style: RequirementListStyle = …,
    size: RequirementListSize = …)
// Requirement(text: String, state: RequirementState); RequirementState.Pending | Met | Unmet.
// RequirementListLabels(met, notMet): read after each rule ("A number, met").

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
// BadgeStyle presets: Default, Brand, Neutral, NeutralDark, Success, Warning, Danger, Info, Chip,
// FilledChip (Chip with the unchosen chips on the subtle fill).

fun NotificationBadge(text: String, modifier: Modifier = Modifier,
    contentDescription: String? = null,
    style: BadgeStyle = LocalNotificationBadgeStyle.current ?: BadgeStyle.Brand,
    size: NotificationBadgeSize = LocalNotificationBadgeSize.current ?: NotificationBadgeSize.Medium,
    shape: Shape = ArecaTheme.shapes.full, soft: Boolean = false, enabled: Boolean = true)
// A count on an icon ("3"); its contentDescription says what it counts ("3 unread messages").

fun IndicatorDot(modifier: Modifier = Modifier, color: Color = LocalContentColor.current,
    size: IndicatorDotSize = LocalIndicatorDotSize.current ?: IndicatorDotSize.Medium,
    border: BorderStroke? = null, contentDescription: String? = null)
// A small status dot (online, unread); label it when it carries meaning.

fun Avatar(modifier: Modifier = Modifier,
    style: AvatarStyle = LocalAvatarStyle.current ?: AvatarStyle.Default,
    size: AvatarSize = LocalAvatarSize.current ?: AvatarSize.Default,
    shape: Shape = ArecaTheme.shapes.full, letters: String? = null,
    contentDescription: String? = null, loading: Boolean = false,
    content: (@Composable BoxScope.() -> Unit)? = null)

fun Stat(label: String, value: String, modifier: Modifier = Modifier, note: String? = null,
    onClick: (() -> Unit)? = null, onClickLabel: String? = null, enabled: Boolean = true,
    style: StatStyle = LocalStatStyle.current ?: StatStyle.Default,
    size: StatSize = LocalStatSize.current ?: StatSize.Default,
    interactionSource: MutableInteractionSource? = null,
    trailing: (@Composable () -> Unit)? = null)
// StatSize.Default (a figure in a grid) | Large (the figure a section is about). Plain strings:
// the label in the secondary ink, the value in a heading style with tabular digits, the note.
// `trailing` follows the value — a Badge with the change ("−4%"). One accessibility node. With
// `onClick`, the hit area is at least the minimum touch target; `enabled` is ignored without it.

fun StatGrid(modifier: Modifier = Modifier, columns: Int = 2,
    columnGap: Dp = ArecaTheme.spacing.xl2, rowGap: Dp = ArecaTheme.spacing.xl,
    content: @Composable () -> Unit)
// Equal columns, every cell given its row's height — its tallest's — so a pressable stat's
// indication covers the cell; stats in reading order.
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

fun <G, T> GroupedContentList(state: LimitedContentListState<ContentListGroup<G, T>>,
    modifier: Modifier = Modifier, …same as ContentList…,
    header: (@Composable ColumnScope.() -> Unit)? = null,
    groupHeaderContent: @Composable (G) -> Unit, itemContent: @Composable (T) -> Unit)
fun <G, T> GroupedPagedContentList(state: PagedContentListState<ContentListGroup<G, T>>,
    modifier: Modifier = Modifier, …same as PagedContentList…,
    header: (@Composable ColumnScope.() -> Unit)? = null,
    groupHeaderContent: @Composable (G) -> Unit, itemContent: @Composable (T) -> Unit)
// ContentListGroup(key: Any, group: G, items: List<T>): a header ("Today") over its items.

fun <Group, Item> LazyVerticalGroupedGrid(data: GroupedGridData<Group, Item>, columns: GridCells,
    modifier: Modifier = Modifier, state: LazyGridState = rememberLazyGridState(), …,
    contentPadding: PaddingValues =
        PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(ArecaTheme.spacing.xl),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(ArecaTheme.spacing.xl),
    header: (LazyGridScope.() -> Unit)? = null, footer: (LazyGridScope.() -> Unit)? = null,
    key: ((GroupedGridItem<Group, Item>) -> Any)? = null,
    group: @Composable (Group) -> Unit, item: @Composable (Item) -> Unit)
fun <Group, Item> LazyHorizontalGroupedGrid(data: GroupedGridData<Group, Item>, rows: Int = 5,
    modifier: Modifier = Modifier, state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = …, horizontalArrangement: Arrangement.Horizontal = …,
    verticalArrangement: Arrangement.Vertical = …,
    itemVerticalSpacing: Dp = ArecaTheme.spacing.xl, itemHorizontalSpacing: Dp = ArecaTheme.spacing.xl,
    …, header: (LazyListScope.() -> Unit)? = null, footer: (LazyListScope.() -> Unit)? = null,
    key: ((Group) -> Any)? = null, group: @Composable (Group) -> Unit,
    item: @Composable (Item) -> Unit)
// GroupedGridData<Group, Item> = Map<Group, List<Item>>, in display order. A photo library by
// month: headers span the width, items take a cell each.

fun <T> Table(items: List<T>, columns: List<TableColumn<T>>, modifier: Modifier = Modifier,
    style: TableStyle = LocalTableStyle.current ?: TableStyle.Default,
    size: TableSize = LocalTableSize.current ?: TableSize.Default, separated: Boolean = true)

fun <T> LazyTable(items: List<T>, columns: List<TableColumn<T>>, modifier: Modifier = Modifier,
    style: TableStyle = …, size: TableSize = …, separated: Boolean = true,
    key: ((index: Int, item: T) -> Any)? = null)
// Table composes every row; LazyTable composes rows as they scroll in.
// TableColumn(id: String, title: String, width: Dp? = null, minWidth: Dp = 80.dp,
//     maxWidth: Dp? = null, expandWeight: Float = 1f, value: (T) -> Any? = { "" },
//     cell: (@Composable (T) -> Unit)? = null, header: (@Composable () -> Unit)? = null)
```
