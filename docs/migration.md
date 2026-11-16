# Migrating to Areca 1.1

This guide moves an app from the first Areca artifacts, released as `1.0.0`, to the current 1.x
line, which ships as `1.1.0`. It also lists what changes for an app already on `1.0.4`.

## Who this is for

| You are on | Read |
| --- | --- |
| `1.0.0`, with the `components`, `fields`, `table` or `viewModel` artifacts | Every section, in order |
| `1.0.4` | [From 1.0.4 to 1.1.0](#from-104-to-110) only |
| `1.0.1` to `1.0.3` | [From 1.0.4 to 1.1.0](#from-104-to-110), plus the release notes of the versions in between |

How to use it:

1. Change the repository and the dependencies first ([Dependencies](#dependencies)). Nothing else
   compiles until they resolve.
2. Rewrite the imports. Most of them are mechanical: run the script in
   [Import rewrite script](#import-rewrite-script), then check the result against
   [Packages and imports](#packages-and-imports).
3. Fix what still fails to compile with [API changes](#api-changes). Each entry shows the 1.0.0
   call and its 1.x form.
4. Read [From 1.0.4 to 1.1.0](#from-104-to-110): those changes compile without errors but change
   what the screen looks like or does.

The 1.0.0 API in this guide was read from the published 1.0.0 artifacts. The 1.x API was checked
against the API dumps in this repository (`*/api/jvm/*.api`). Where a call is not listed, the exact
1.x signature is in [`signatures.md`](../skills/areca-ui-v1/reference/signatures.md) and the module
READMEs.

## Dependencies

### Repository

Areca 1.x is published to GitHub Packages. Add the repository to `settings.gradle.kts`, limited to
the Areca group, and remove any `mavenLocal()` you kept for Areca:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        exclusiveContent {
            forRepository {
                maven("https://maven.pkg.github.com/VoirDev/areca-ui") {
                    name = "Areca"
                    credentials(PasswordCredentials::class)
                }
            }
            filter { includeGroup("dev.voir.areca") }
        }
    }
}
```

GitHub Packages asks for credentials even for a public package. Set `ArecaUsername` and
`ArecaPassword` in `~/.gradle/gradle.properties`, never in the project. [Getting
started](getting-started.md#add-the-repository) has the details and the CI setup.

### Coordinates

The group is still `dev.voir.areca`. Every Areca artifact is released together: use one version
for all of them.

| 1.0.0 artifact | 1.x artifact | Note |
| --- | --- | --- |
| `dev.voir.areca:foundation` | `dev.voir.areca:foundation` | Now also holds what was in `components`, `fields` and `table` |
| `dev.voir.areca:components` | `dev.voir.areca:foundation` | Merged. Remove the dependency |
| `dev.voir.areca:fields` | `dev.voir.areca:foundation` | Merged. Remove the dependency |
| `dev.voir.areca:table` | `dev.voir.areca:foundation` | Merged into `dev.voir.areca.foundation.table` (`Table`, `LazyTable`, `TableColumn`, `TableStyle`, `TableSize`). The 1.0.0 table API is not covered by this guide |
| `dev.voir.areca:viewModel` | `dev.voir.areca:viewmodel` | Renamed: lower-case `m` |
| `dev.voir.areca:icons` | `dev.voir.areca:icons` | Same package, `dev.voir.areca.icons` |
| `dev.voir.areca:charts` | `dev.voir.areca:charts` | |
| `dev.voir.areca:datetime` | `dev.voir.areca:datetime` | Calendar types moved to `dev.voir.areca.datetime.calendar` |
| `dev.voir.areca:keyboard` | `dev.voir.areca:keyboard` | |

New in 1.x, add only if you use them: `crop`, `media`, `flow-auth`, `flow-profile`,
`flow-gallery`, `flow-attachments`. See [`modules.md`](../skills/areca-ui-v1/reference/modules.md).

A version catalog before and after:

```toml
# 1.0.0
areca-components = { module = "dev.voir.areca:components", version = "1.0.0" }
areca-fields = { module = "dev.voir.areca:fields", version = "1.0.0" }
areca-foundation = { module = "dev.voir.areca:foundation", version = "1.0.0" }
areca-table = { module = "dev.voir.areca:table", version = "1.0.0" }
areca-viewModel = { module = "dev.voir.areca:viewModel", version = "1.0.0" }

# 1.x
[versions]
areca = "1.1.0"

[libraries]
areca-foundation = { module = "dev.voir.areca:foundation", version.ref = "areca" }
areca-viewmodel = { module = "dev.voir.areca:viewmodel", version.ref = "areca" }
```

Then remove `libs.areca.components`, `libs.areca.fields` and `libs.areca.table` from every
`build.gradle.kts`, and rename `libs.areca.viewModel` to `libs.areca.viewmodel`.

The 1.x libraries are built with Kotlin 2.3 and Compose Multiplatform 1.11; use those or later.

## Packages and imports

In 1.0.0 most of `foundation` sat in one flat package, `dev.voir.areca.foundation`. In 1.x each
component family has its own package: `dev.voir.areca.foundation.button`, `.text`, `.layout` and so
on. The packages of `components` and `fields` moved into those families too.

These packages did not change: `dev.voir.areca` (`ExperimentalArecaApi`), `dev.voir.areca.theme`
(`ArecaTheme`, `ArecaThemeProvider`), `dev.voir.areca.icons`, `dev.voir.areca.indication`,
`dev.voir.areca.shape`, `dev.voir.areca.foundation.layout` (`BaseLayout`,
`PullToRefreshLayout`), `dev.voir.areca.foundation.slot` and `dev.voir.areca.foundation.tabbar`.

The table lists every Areca import of a real 1.0.0 app and the known renames. A wildcard import
such as `import dev.voir.areca.foundation.*` no longer covers the moved names: replace it with the
family packages you use.

### Components and fields

| 1.0.0 import | 1.x import |
| --- | --- |
| `dev.voir.areca.components.ConfirmationDialog` | `dev.voir.areca.foundation.overlay.ConfirmationDialog` |
| `dev.voir.areca.components.ConfirmationDialogStyle` | `dev.voir.areca.foundation.overlay.ConfirmationDialogStyle` |
| `dev.voir.areca.components.PagedContentList` | `dev.voir.areca.foundation.collection.PagedContentList` |
| `dev.voir.areca.components.PagedContentListState` | `dev.voir.areca.foundation.collection.PagedContentListState` |
| `dev.voir.areca.components.navigation.NavItem` | `dev.voir.areca.foundation.navigation.NavItem` |
| `dev.voir.areca.components.settings.Section` | `dev.voir.areca.foundation.settings.Section` |
| `dev.voir.areca.components.settings.Settings` | `dev.voir.areca.foundation.settings.Settings` (not `dev.voir.areca.icons.Settings`, which is an icon) |
| `dev.voir.areca.components.settings.SettingsRow` | `dev.voir.areca.foundation.settings.SettingsRow` |
| `dev.voir.areca.fields.FieldDefaults` | `dev.voir.areca.foundation.field.FieldDefaults` (see [Fields](#fields)) |
| `dev.voir.areca.fields.FieldGroup` | `dev.voir.areca.foundation.field.FieldGroup` |
| `dev.voir.areca.fields.PasswordField` | `dev.voir.areca.foundation.field.PasswordField` |
| `dev.voir.areca.fields.TextField` | `dev.voir.areca.foundation.field.TextField` |
| `dev.voir.areca.fields.select.SelectFlowModal` | `dev.voir.areca.foundation.field.select.SelectFlowModal` |
| `dev.voir.areca.fields.select.SelectOption` | `dev.voir.areca.foundation.field.select.SelectOption` |

### Foundation

| 1.0.0 import | 1.x import |
| --- | --- |
| `dev.voir.areca.foundation.Avatar` | `dev.voir.areca.foundation.avatar.Avatar` |
| `dev.voir.areca.foundation.AvatarSize` | `dev.voir.areca.foundation.avatar.AvatarSize` |
| `dev.voir.areca.foundation.Badge` | `dev.voir.areca.foundation.badge.Badge` |
| `dev.voir.areca.foundation.BadgeSize` | `dev.voir.areca.foundation.badge.BadgeSize` |
| `dev.voir.areca.foundation.BadgeStyle` | `dev.voir.areca.foundation.badge.BadgeStyle` |
| `dev.voir.areca.foundation.IndicatorDot` | `dev.voir.areca.foundation.badge.IndicatorDot` |
| `dev.voir.areca.foundation.LocalBadgeStyle` | `dev.voir.areca.foundation.badge.LocalBadgeStyle` |
| `dev.voir.areca.foundation.NotificationBadge` | `dev.voir.areca.foundation.badge.NotificationBadge` |
| `dev.voir.areca.foundation.Button` | `dev.voir.areca.foundation.button.Button` |
| `dev.voir.areca.foundation.ButtonColor` | `dev.voir.areca.foundation.button.ActionColor` (see [Colours](#buttoncolor-iconbuttoncolor-and-featurediconcolor)) |
| `dev.voir.areca.foundation.ButtonSize` | `dev.voir.areca.foundation.button.ButtonSize` |
| `dev.voir.areca.foundation.ButtonStyle` | `dev.voir.areca.foundation.button.ButtonStyle` |
| `dev.voir.areca.foundation.IconButton` | `dev.voir.areca.foundation.button.IconButton` |
| `dev.voir.areca.foundation.IconButtonColor` | `dev.voir.areca.foundation.button.ActionColor` |
| `dev.voir.areca.foundation.IconButtonSize` | `dev.voir.areca.foundation.button.IconButtonSize` |
| `dev.voir.areca.foundation.IconButtonStyle` | `dev.voir.areca.foundation.button.IconButtonStyle` |
| `dev.voir.areca.foundation.LocalButtonStyle` | `dev.voir.areca.foundation.button.LocalButtonStyle` |
| `dev.voir.areca.foundation.LocalIconButtonStyle` | `dev.voir.areca.foundation.button.LocalIconButtonStyle` |
| `dev.voir.areca.foundation.ErrorBoundary` | `dev.voir.areca.foundation.feedback.Fallback` (see [ErrorBoundary](#errorboundary)) |
| `dev.voir.areca.foundation.ProgressBar` | `dev.voir.areca.foundation.feedback.ProgressBar` |
| `dev.voir.areca.foundation.Skeleton` | `dev.voir.areca.foundation.feedback.Skeleton` |
| `dev.voir.areca.foundation.ToastColor` | `dev.voir.areca.foundation.feedback.ToastColor` |
| `dev.voir.areca.foundation.ToastHost` | `dev.voir.areca.foundation.feedback.ToastHost` |
| `dev.voir.areca.foundation.ToastState` | `dev.voir.areca.foundation.feedback.ToastState` |
| `dev.voir.areca.foundation.rememberToastState` | `dev.voir.areca.foundation.feedback.rememberToastState` |
| `dev.voir.areca.foundation.Chevron` | `dev.voir.areca.foundation.icon.Chevron` |
| `dev.voir.areca.foundation.ChevronDirection` | `dev.voir.areca.foundation.icon.ChevronDirection` |
| `dev.voir.areca.foundation.ChevronSize` | `dev.voir.areca.foundation.icon.ChevronSize` |
| `dev.voir.areca.foundation.FeaturedIcon` | `dev.voir.areca.foundation.icon.FeaturedIcon` |
| `dev.voir.areca.foundation.FeaturedIconColor` | `dev.voir.areca.foundation.button.ActionColor` |
| `dev.voir.areca.foundation.FeaturedIconSize` | `dev.voir.areca.foundation.icon.FeaturedIconSize` |
| `dev.voir.areca.foundation.FeaturedIconStyle` | `dev.voir.areca.foundation.icon.FeaturedIconStyle` |
| `dev.voir.areca.foundation.FeaturedIconVariant` | No direct replacement: the variant is the factory name (see [FeaturedIconVariant](#featurediconvariant)) |
| `dev.voir.areca.foundation.Icon` | `dev.voir.areca.foundation.icon.Icon` |
| `dev.voir.areca.foundation.IconSize` | `dev.voir.areca.foundation.icon.IconSize` |
| `dev.voir.areca.foundation.Card` | `dev.voir.areca.foundation.layout.Card` |
| `dev.voir.areca.foundation.CardStyle` | `dev.voir.areca.foundation.layout.CardStyle` |
| `dev.voir.areca.foundation.DividerTextPosition` | `dev.voir.areca.foundation.layout.DividerTextPosition` |
| `dev.voir.areca.foundation.HorizontalDivider` | `dev.voir.areca.foundation.layout.HorizontalDivider` |
| `dev.voir.areca.foundation.rememberExpandableState` | `dev.voir.areca.foundation.layout.rememberExpandableState` |
| `dev.voir.areca.foundation.FlowModal` | `dev.voir.areca.foundation.overlay.FlowModal` |
| `dev.voir.areca.foundation.FlowModalController` | `dev.voir.areca.foundation.overlay.FlowModalController` |
| `dev.voir.areca.foundation.rememberFlowModal` | `dev.voir.areca.foundation.overlay.rememberFlowModal` |
| `dev.voir.areca.foundation.CheckboxStyle` | `dev.voir.areca.foundation.selection.CheckboxStyle` |
| `dev.voir.areca.foundation.LocalCheckboxStyle` | `dev.voir.areca.foundation.selection.LocalCheckboxStyle` |
| `dev.voir.areca.foundation.LocalToggleStyle` | `dev.voir.areca.foundation.selection.LocalToggleStyle` |
| `dev.voir.areca.foundation.Toggle` | `dev.voir.areca.foundation.selection.Toggle` |
| `dev.voir.areca.foundation.ToggleDefaults` | `dev.voir.areca.foundation.selection.ToggleStyle` and `ToggleSize` (see [ToggleDefaults](#toggledefaults)) |
| `dev.voir.areca.foundation.ToggleState` | `dev.voir.areca.foundation.selection.ToggleState` |
| `dev.voir.areca.foundation.ToggleStyle` | `dev.voir.areca.foundation.selection.ToggleStyle` |
| `dev.voir.areca.foundation.Text` | `dev.voir.areca.foundation.text.Text` |
| `dev.voir.areca.foundation.TopBar` | `dev.voir.areca.foundation.topbar.TopBar` |
| `dev.voir.areca.foundation.TopBarDefaults` | `dev.voir.areca.foundation.topbar.Action` and `BackAction` (see [TopBarDefaults](#topbardefaults)) |
| `dev.voir.areca.foundation.TopBarSize` | `dev.voir.areca.foundation.topbar.TopBarSize` |
| `dev.voir.areca.foundation.TopBarStyle` | `dev.voir.areca.foundation.topbar.TopBarStyle` |
| `dev.voir.areca.foundation.layout.BaseLayout` | Unchanged |
| `dev.voir.areca.foundation.layout.PullToRefreshLayout` | Unchanged |
| `dev.voir.areca.foundation.slot.SlotRow` | Unchanged |
| `dev.voir.areca.foundation.slot.SlotRowLayout` | Unchanged |
| `dev.voir.areca.foundation.slot.SlotSize` | Unchanged |
| `dev.voir.areca.foundation.tabbar.FloatingTabBar` | Unchanged |
| `dev.voir.areca.foundation.tabbar.FloatingTabBarIndicatorStyle` | Unchanged |
| `dev.voir.areca.foundation.tabbar.FloatingTabBarItemContent` | Unchanged |
| `dev.voir.areca.foundation.tabbar.FloatingTabBarSize` | Unchanged |
| `dev.voir.areca.foundation.tabbar.FloatingTabBarStyle` | Unchanged |
| `dev.voir.areca.foundation.tabbar.TabBarItem` | Unchanged |
| `dev.voir.areca.foundation.tabbar.rememberTabBar` | `dev.voir.areca.foundation.tabbar.rememberTabBarState` |
| `dev.voir.areca.ExperimentalArecaApi` | Unchanged |

### Theme types

| 1.0.0 import | 1.x import |
| --- | --- |
| `dev.voir.areca.theme.ArecaTheme` | Unchanged; some members renamed (see [Theme](#theme)) |
| `dev.voir.areca.theme.ArecaThemeProvider` | Unchanged; parameters renamed (see [Theme](#theme)) |
| `dev.voir.areca.theme.BaseColors` | `dev.voir.areca.theme.ArecaBaseColors` |
| `dev.voir.areca.theme.ColorVariables` | `dev.voir.areca.theme.ArecaColors` |
| `dev.voir.areca.theme.FontVariables` | `dev.voir.areca.theme.ArecaFonts` |
| `dev.voir.areca.theme.BaseTypography` | `dev.voir.areca.theme.ArecaBaseTypography` |
| `dev.voir.areca.theme.TypographyVariables` | `dev.voir.areca.theme.ArecaTypography` |
| `dev.voir.areca.theme.RadiusVariables` | `dev.voir.areca.theme.ArecaRadius` |
| `dev.voir.areca.theme.SpaceVariables` | `dev.voir.areca.theme.ArecaSpacing` |

### ViewModel types

| 1.0.0 import | 1.x import |
| --- | --- |
| `dev.voir.areca.viewModel.BaseViewModel` | `dev.voir.areca.viewmodel.BaseViewModel` |
| `dev.voir.areca.viewModel.RefreshGate` | `dev.voir.areca.viewmodel.RefreshGate` |
| `dev.voir.areca.viewModel.ReloadStrategy` | `dev.voir.areca.viewmodel.list.ReloadStrategy` (moved to `list`) |
| `dev.voir.areca.viewModel.UiState` | `dev.voir.areca.viewmodel.UiState` |
| `dev.voir.areca.viewModel.displayOrNull` | `dev.voir.areca.viewmodel.displayOrNull` |
| `dev.voir.areca.viewModel.list.ListState` | `dev.voir.areca.viewmodel.list.ListState` |
| `dev.voir.areca.viewModel.list.PagingMeta` | `dev.voir.areca.viewmodel.list.PagingMeta` |

### Icons

`dev.voir.areca.icons.ArecaIcons` and every icon import (`dev.voir.areca.icons.Trash`, `.User`,
`.ArrowLeft` and so on) are unchanged.

The 1.0.0 `foundation` artifact also carried a few icons of its own under
`dev.voir.areca.foundation.icons` (`ArecaFoundationIcons`). They are not public in 1.x. Add the
`icons` artifact and use `ArecaIcons.Check`, `ArecaIcons.ChevronDown` and so on.

## API changes

Most components kept their names and parameters and only moved package. This section covers the
calls that need more than a new import.

### ButtonColor, IconButtonColor and FeaturedIconColor

The three colour enums became one, `ActionColor`, in `dev.voir.areca.foundation.button`. The values
are the same: `Brand`, `Danger`, `Success`, `Warning`, `Neutral`, `NeutralDark`. The style
factories take it in the same place:

```kotlin
// 1.0.0
import dev.voir.areca.foundation.ButtonColor
import dev.voir.areca.foundation.ButtonStyle
import dev.voir.areca.foundation.IconButtonColor
import dev.voir.areca.foundation.IconButtonStyle

Button("Delete", onClick = onDelete, style = ButtonStyle.plain(ButtonColor.Danger))
IconButton(ArecaIcons.Trash, "Delete", onClick = onDelete, style = IconButtonStyle.solid(IconButtonColor.Neutral))

// 1.x
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.button.IconButtonStyle

Button("Delete", onClick = onDelete, style = ButtonStyle.plain(ActionColor.Danger))
IconButton(ArecaIcons.Trash, "Delete", onClick = onDelete, style = IconButtonStyle.solid(ActionColor.Neutral))
```

| Factory | 1.x parameters |
| --- | --- |
| `ButtonStyle.solid`, `.soft`, `.outlined`, `.plain` | `color: ActionColor = ActionColor.Brand`, `shape: Shape` |
| `IconButtonStyle.solid`, `.soft`, `.outlined`, `.plain` | `color: ActionColor = ActionColor.Brand`, `shape: Shape` |
| `FeaturedIconStyle.solid`, `.soft`, `.outlined` | `color: ActionColor = ActionColor.Brand` |

The style data classes lost some fields:

| Type | 1.0.0 field | 1.x |
| --- | --- | --- |
| `ButtonStyle` | `spinner`, `spinnerDisabled` | Removed. The spinner takes `extra` and `extraDisabled` |
| `ButtonStyle` | `cornerRadius` | Removed. Use `shape` |
| `IconButtonStyle` | `extra`, `extraDisabled`, `spinner`, `spinnerDisabled` | Removed. Content and spinner take `content` |
| `IconButtonStyle` | (shape was an `IconButton` parameter) | `shape` is a field of the style; `IconButton(shape = …)` is gone |

```kotlin
// 1.0.0
ButtonStyle.solid(ButtonColor.Brand).copy(
    content = carbon,
    extra = carbon,
    spinner = carbon,
)
IconButtonStyle.solid(IconButtonColor.Brand).copy(content = carbon, extra = carbon, spinner = carbon)

// 1.x
ButtonStyle.solid(ActionColor.Brand).copy(content = carbon, extra = carbon)
IconButtonStyle.solid(ActionColor.Brand).copy(content = carbon)
```

### FeaturedIconVariant

`FeaturedIconVariant` (`Solid`, `Soft`, `Outlined`) and `FeaturedIconStyle.style(variant, color)` are
gone. Call the factory named after the variant:

```kotlin
// 1.0.0
FeaturedIcon(
    imageVector = ArecaIcons.AlertCircle,
    contentDescription = null,
    size = FeaturedIconSize.Medium,
    style = FeaturedIconStyle.style(
        variant = FeaturedIconVariant.Soft,
        color = FeaturedIconColor.Danger,
    ),
)

// 1.x
FeaturedIcon(
    imageVector = ArecaIcons.AlertCircle,
    contentDescription = null,
    size = FeaturedIconSize.Medium,
    style = FeaturedIconStyle.soft(ActionColor.Danger),
)
```

| 1.0.0 | 1.x |
| --- | --- |
| `FeaturedIconStyle.style(FeaturedIconVariant.Solid, color)` | `FeaturedIconStyle.solid(color)` |
| `FeaturedIconStyle.style(FeaturedIconVariant.Soft, color)` | `FeaturedIconStyle.soft(color)` |
| `FeaturedIconStyle.style(FeaturedIconVariant.Outlined, color)` | `FeaturedIconStyle.outlined(color)` |

If the variant was a variable, branch on your own value:

```kotlin
val style = when (emphasis) {
    Emphasis.Strong -> FeaturedIconStyle.solid(color)
    Emphasis.Quiet -> FeaturedIconStyle.soft(color)
}
```

`FeaturedIconStyle` fields `background` and `backgroundBrush` are now `bg` and `bgBrush`.

### ErrorBoundary

`ErrorBoundary`, `ErrorBoundaryStyle` and `ErrorBoundarySize` are removed. The name suggested that
it caught failures, but it only drew a message: Compose cannot recover from an exception thrown
during composition. The replacement is `Fallback` (`dev.voir.areca.foundation.feedback`), which the
screen shows when its own UI state says the content is missing: a failed load, an empty list,
no connection.

`Fallback` takes composable slots only. The string overload `ErrorBoundary(title, message)` has no
direct counterpart: wrap the strings in `Text`.

| `ErrorBoundary` (1.0.0) | `Fallback` (1.x) |
| --- | --- |
| `top` | `top` |
| `title: String` or `heading` slot | `heading` slot |
| `message: String` or `body` slot | `body` slot |
| `content` | `content` |
| `style: ErrorBoundaryStyle(background, titleColor, captionColor)` | `style: FallbackStyle(bg, title, caption)` |
| `ErrorBoundaryStyle.default(bg, title, caption)` | `FallbackStyle.default(bg, title, caption)` |
| `size: ErrorBoundarySize(padding, gap, titleTextStyle, captionTextStyle)` | `size: FallbackSize(padding, gap, textGap, titleTextStyle, captionTextStyle)` |
| Always centred | Centred by default; `horizontalAlignment` from 1.1.0 |

```kotlin
// 1.0.0
ErrorBoundary(
    modifier = Modifier.fillMaxWidth().padding(padding),
    title = "Couldn’t load your accounts",
    message = message,
    content = {
        Button(text = "Try again", onClick = onRetry)
    },
)

// 1.x
Fallback(
    modifier = Modifier.fillMaxWidth().padding(padding),
    heading = { Text("Couldn’t load your accounts") },
    body = { Text(message) },
    content = {
        Button(text = "Try again", onClick = onRetry)
    },
)
```

Drive it from the screen's state, not from a wrapper around the content:

```kotlin
when (val current = state) {
    UiState.Initial, UiState.Loading -> Skeleton(count = 3)
    is UiState.Error -> Fallback(
        top = { FeaturedIcon(ArecaIcons.AlertCircle, contentDescription = null) },
        heading = { Text("Couldn’t load your accounts") },
        body = if (current.message.isNotEmpty()) {
            { Text(current.message) }
        } else {
            null
        },
        content = { Button(text = "Try again", onClick = onRetry) },
    )
    is UiState.Display -> AccountsList(current.data)
}
```

`Fallback` gives the `top` slot a larger icon size, and styles `heading` and `body` for you: plain
`Text` calls pick up the right type and colour.

### viewModel package

The artifact and the package lost the capital `M`: `dev.voir.areca:viewModel` is now
`dev.voir.areca:viewmodel`, and `dev.voir.areca.viewModel` is `dev.voir.areca.viewmodel`.
`ReloadStrategy` also moved into `dev.voir.areca.viewmodel.list`.

```kotlin
// 1.0.0
import dev.voir.areca.viewModel.BaseViewModel
import dev.voir.areca.viewModel.ReloadStrategy
import dev.voir.areca.viewModel.UiState
import dev.voir.areca.viewModel.displayOrNull

// 1.x
import dev.voir.areca.viewmodel.BaseViewModel
import dev.voir.areca.viewmodel.UiState
import dev.voir.areca.viewmodel.displayOrNull
import dev.voir.areca.viewmodel.list.ReloadStrategy
```

`BaseViewModel` keeps `dispatch`, `reduce`, `state`, `effects`, `emitEffect`, `mutateDisplay`,
`setDisplay`, `setLoading`, `setError`, `setInitial` and `currentDisplayOrNull`. These 1.0.0
members are gone:

| 1.0.0 | 1.x |
| --- | --- |
| `launchAction(block, onSuccess, onFailure)` | `runAction(block, onSuccess, onFailure)`. It suspends instead of launching: called from `reduce`, it holds later actions until it returns. Launch it in `viewModelScope` when they must not wait |
| `withDisplay(block)` | No direct replacement. Read `display` or `currentDisplayOrNull()` |
| `effect(effect)` | No direct replacement. Emit from inside the ViewModel with `emitEffect` |

`UiState.Error` holds `message: String` and `cause: Throwable?`. An empty `message` means the
ViewModel supplied none, and the UI words the failure itself. The ViewModel conventions are in
the [ViewModel README](../standalone/viewmodel/README.md).

### Theme

The theme types gained the `Areca` prefix, and the provider's parameters lost their `Variables`
suffix.

| 1.0.0 | 1.x |
| --- | --- |
| `BaseColors(...)` | `ArecaBaseColors(...)`, same fields |
| `ColorVariables(baseColors)` | `ArecaColors.light(baseColors)` or `ArecaColors.dark(baseColors)` |
| `FontVariables(primary, secondary)` | `ArecaFonts(primary, secondary)` |
| `BaseTypography(font)` | `ArecaBaseTypography.from(fonts)` |
| `TypographyVariables(baseTypography)` | `ArecaTypography.from(baseTypography)` |
| `RadiusVariables` | `ArecaRadius`. Same steps, plus `xs` |
| `SpaceVariables` | `ArecaSpacing`. `xxs` is now `xs2` |
| `ArecaThemeProvider(colorVariables = …)` | `ArecaThemeProvider(colors = …)` |
| `ArecaThemeProvider(fontVariables = …)` | `ArecaThemeProvider(fonts = …)` |
| `ArecaThemeProvider(typographyVariables = …)` | `ArecaThemeProvider(typography = …)` |
| `ArecaThemeProvider(radiusVariables = …)` | `ArecaThemeProvider(radius = …)` |
| `ArecaThemeProvider(spaceVariables = …)` | `ArecaThemeProvider(spacing = …)` |
| `ArecaTheme.space` | `ArecaTheme.spacing` |
| `ArecaTheme.space.xxs` | `ArecaTheme.spacing.xs2` |
| `ArecaTheme.font` | `ArecaTheme.fonts` |

`ArecaTheme.colors`, `.baseColors`, `.radius`, `.typography` and `.baseTypography` keep their
names, and so do the colour and type tokens a 1.0.0 app reads (`bgCanvas`, `fgMuted`,
`bodySmall`, `headingExtraSmall` and the rest). The spacing step names other than `xxs` match, but
check the values in [Theme](theme.md) if a layout looks off after the upgrade.

```kotlin
// 1.0.0
private val brandBase = BaseColors(baseBlack = carbon, brand50 = gold, /* … */)
private val brandColors = ColorVariables(brandBase).copy(bgCanvas = Color.White, fgBrand = gold)

val fonts = FontVariables(primary = inter, secondary = sora)
val baseTypography = BaseTypography(fonts)
val typography = TypographyVariables(baseTypography).let {
    it.copy(display = it.display.copy(fontFamily = sora))
}

ArecaThemeProvider(
    baseColors = brandBase,
    colorVariables = brandColors,
    fontVariables = fonts,
    baseTypography = baseTypography,
    typographyVariables = typography,
) { content() }

// 1.x
private val brandBase = ArecaBaseColors(baseBlack = carbon, brand50 = gold, /* … */)
private val brandColors = ArecaColors.light(brandBase).copy(bgCanvas = Color.White, fgBrand = gold)

val fonts = ArecaFonts(primary = inter, secondary = sora)
val baseTypography = ArecaBaseTypography.from(fonts)
val typography = ArecaTypography.from(baseTypography).let {
    it.copy(display = it.display.copy(fontFamily = sora))
}

ArecaThemeProvider(
    baseColors = brandBase,
    colors = brandColors,
    fonts = fonts,
    baseTypography = baseTypography,
    typography = typography,
) { content() }
```

New in 1.x: `ArecaThemeProvider(darkTheme = …)`. Nothing follows the system on its own; pass
`isSystemInDarkTheme()` or the user's setting, and give a dark palette with
`ArecaColors.dark(baseColors)`. If you pass your own `typography`, it replaces the provider's
default, which steps body text down for pointer input; leave `typography` out unless you change a
style.

### Theme-wide component styles

Most `LocalXStyle` locals kept their names and moved with their component. Some style types changed
fields:

| Type | 1.0.0 | 1.x |
| --- | --- | --- |
| `BadgeStyle` | `bgPassive`, `fg`, `fgDisabled`, `fgPassive`, `borderPassive` | `bgSoft`, `content`, `contentDisabled`, `contentSoft`, `borderSoft` |
| `Badge` | `passive = true` | `soft = true` |
| `CheckboxStyle.default` | `readonlyBg`, `readonlyBorder`, `readonlyCheckedBg`, `readonlyCheckedBorder`, `readonlyMark` | `readOnlyBg`, `readOnlyBorder`, `readOnlyCheckedBg`, `readOnlyCheckedBorder`, `readOnlyMark` |
| `CardStyle.default` | `background`, `contentColor` | `bg`, `content` |
| `TopBarStyle.default` | `background`, `extra` | `bg`; `extra` removed (actions take `actions: TopBarActionStyle`) |
| `FloatingTabBarStyle.default` | `background` | `bg` |
| `FloatingTabBarIndicatorStyle.fill`, `.underline` | `selectedContent` | `contentSelected` |

```kotlin
// 1.0.0
BadgeStyle(
    bgPassive = ArecaTheme.colors.bgBrandMuted,
    bgDisabled = ArecaTheme.colors.bgDisabled,
    bg = ArecaTheme.colors.bgBrandBase,
    fgPassive = carbon,
    fgDisabled = ArecaTheme.colors.fgDisabled,
    fg = carbon,
    border = Color.Transparent,
)

// 1.x
BadgeStyle(
    bgSoft = ArecaTheme.colors.bgBrandMuted,
    bgDisabled = ArecaTheme.colors.bgDisabled,
    bg = ArecaTheme.colors.bgBrandBase,
    contentSoft = carbon,
    contentDisabled = ArecaTheme.colors.fgDisabled,
    content = carbon,
    border = Color.Transparent,
)
```

### ToggleDefaults

`ToggleDefaults` is gone. Its members moved to the companions of `ToggleStyle` and `ToggleSize`
(`dev.voir.areca.foundation.selection`):

| 1.0.0 | 1.x |
| --- | --- |
| `ToggleDefaults.DefaultStyle` | `ToggleStyle.Default` |
| `ToggleDefaults.defaultStyle(...)` | `ToggleStyle.default(...)`, same colour parameters plus `trackReadOnly`, `thumbReadOnly` and `border` |
| `ToggleDefaults.SmallSize`, `.MediumSize`, `.LargeSize` | `ToggleSize.Small`, `.Medium`, `.Large` |
| `ToggleState.Disabled` | Removed. Pass `enabled = false` to `Toggle` |

`ToggleStyle` itself is no longer a flat list of track and thumb colours, so build a variation with
`ToggleStyle.default(...)`, not with the constructor or `copy`. An extension you declared on
`ToggleDefaults` moves to `ToggleStyle.Companion`:

```kotlin
// 1.0.0
internal val ToggleDefaults.Brand: ToggleStyle
    @Composable get() = defaultStyle(thumbOn = carbon, thumbIndeterminate = carbon)

// 1.x
internal val ToggleStyle.Companion.Brand: ToggleStyle
    @Composable get() = ToggleStyle.default(thumbOn = carbon, thumbIndeterminate = carbon)
```

### TopBarDefaults

`TopBarDefaults.Action` and `TopBarDefaults.BackButton` are gone. In 1.x the `leading` and
`trailing` slots of `TopBar` run in a `TopBarActionScope`, and the actions are extensions on it, in
`dev.voir.areca.foundation.topbar`:

| 1.0.0 | 1.x |
| --- | --- |
| `TopBarDefaults.BackButton(onClick) { Icon(…) }` | `BackAction(onClick)`. It draws its own chevron; `contentDescription` defaults to `"Back"` |
| `TopBarDefaults.Action(onClick) { … }` | `Action(onClick) { … }` |
| `TopBarDefaults.Action(onClick) { Avatar(…) }` | `AvatarAction(onClick, letters = …)` or `AvatarAction(onClick) { … }` |

```kotlin
// 1.0.0
TopBar(
    leading = {
        TopBarDefaults.BackButton(onClick = onBack) {
            Icon(ArecaIcons.ArrowLeft, contentDescription = "Back")
        }
    },
    trailing = {
        TopBarDefaults.Action(onClick = onOpenProfile) { ProfileAvatar() }
    },
) { Text("Library") }

// 1.x
import dev.voir.areca.foundation.topbar.AvatarAction
import dev.voir.areca.foundation.topbar.BackAction

TopBar(
    title = "Library",
    leading = { BackAction(onClick = onBack) },
    trailing = { AvatarAction(onClick = onOpenProfile) { ProfileAvatar() } },
)
```

There are ready-made actions for common jobs too: `CloseAction`, `MenuAction`, `IconAction`,
`TextAction`, `DoneAction`, `SearchAction`, `ShareAction` and others. See
[`signatures.md`](../skills/areca-ui-v1/reference/signatures.md).

### Fields

`FieldDefaults` is now an object with the field's `style` and `size` in scope and the label and
message composables. The presets moved to the companions of `FieldStyle` and `FieldSize`:

| 1.0.0 | 1.x |
| --- | --- |
| `FieldDefaults.inline()` | `FieldStyle.inline()` |
| `FieldDefaults.outlined()` | `FieldStyle.outlined()` |
| `FieldDefaults.solidNeutral()` | `FieldStyle.solidNeutral()` |
| `FieldDefaults.extraSmall()` | `FieldSize.ExtraSmall` |
| `FieldDefaults.small()` | `FieldSize.Small` |
| `FieldDefaults.medium()` | `FieldSize.Medium` |
| `TextField(shape = …)`, `PasswordField(shape = …)` | Removed. Every factory takes `shape`: `FieldStyle.outlined(shape = …)` |
| `FieldGroupRow` | `FieldGroup(labelPlacement = FieldLabelPlacement.Beside())` |
| `FieldGroupColumn` | `FieldGroup(labelPlacement = FieldLabelPlacement.Above)`, the default |

`FieldGroup` takes its label and messages as text, not as composables, and hands the field a
`FieldScope`. Pass it on, or the label is drawn but not announced:

```kotlin
// 1.0.0
FieldGroup(
    label = { Text("New playlist name") },
    assistive = { Text("Optional") },
) {
    TextField(value = name, onValueChange = onNameChange, style = FieldDefaults.solidNeutral())
}

// 1.x
FieldGroup(label = "New playlist name", assistive = "Optional") { field ->
    TextField(
        value = name,
        onValueChange = onNameChange,
        style = FieldStyle.solidNeutral(),
        field = field,
    )
}
```

`FieldGroup` also takes `error`, `required` and `status`. Its `space` and `reservedSpace`
parameters are gone; the gaps are `labelSpace` and `messageSpace`.

### Smaller changes

| Component | 1.0.0 | 1.x |
| --- | --- | --- |
| `Button`, `IconButton` | `clickable = false` | Removed. Pass `onClick = null` for a non-interactive button |
| `Card` | `padding`, `borderWidth` | `size = CardSize.default(padding = …, borderWidth = …)`. The content lambda still receives the padding |
| `BaseLayout` | `backgroundColor` | `bg` (defaults to the page ground of the surface layout from 1.1.0) |
| `HorizontalDivider` | `DividerTextPosition.Start`, `.End` | `DividerTextPosition.Leading`, `.Trailing`; `Center` is unchanged |
| `HorizontalDivider`, `VerticalDivider` | `HorizontalDividerStyle`, `VerticalDividerStyle`, `…Size`, `Local…` | `DividerStyle`, `DividerSize`, `LocalDividerStyle`, `LocalDividerSize` |
| `NotificationBadge` | `textStyle` | Removed; the type comes from `size: NotificationBadgeSize` |
| `ProgressBar` | `height`, `backgroundColor`, `progressColor`, `cornerRadius`, `animationDuration`, `labelTextStyle` | `style: ProgressBarStyle` (`track`, `fill`, `label`) and `size: ProgressBarSize` (`thickness`, `shape`, `labelTextStyle`, `animationDuration`) |
| `Skeleton` | `baseColor`, `highlightColor` | `style: SkeletonStyle` (`base`, `highlight`) |
| `Skeleton` | `shape`, `lineSpacing`, `duration` | `size: SkeletonSize` (`shape`, `lineSpacing`, `shimmerPeriod`) |
| `Skeleton` | `inline`, `direction`, `enableAnimation` | `arrangement = SkeletonArrangement.Row`, `reversed`, `animated` |
| `Text` | `Text(text, size = TextSize.X)` | `Text(text, variant = TextVariant.X)`, same values |
| `Spinner` | `LocalSpinnerColor` | `LocalSpinnerStyle` |
| `NavItem` | `colors`, `sizes`, `shape`, `active` | `style: NavItemStyle`, `size: NavItemSize`, `selected` |
| `Section` (settings) | `label`, `description` | `header`. `description` has no direct replacement; put the text in `footer` |
| `Settings` | `itemLayout` | Removed |
| `SettingsRow` | `content` | Removed. Use `title`, `subtitle`, `caption`, `value` and `badge` |
| `PagedContentList` | `sizes: ContentListSizes` | `size: ContentListSize`, plus `style` and `labels` |
| `rememberTabBar(items, initialSelectedValue)` | | `rememberTabBarState(items, initialSelectedValue)` |
| `Tabs` | `activeValue`, `onTabClick` | `selectedValue`, `onValueSelected` |
| `BadgeSelect` | `rememberBadgeSelect`, `BadgeSelectState` | Removed. `BadgeSelect` takes `selected` and `onSelect`; hold the selection in your own state |
| SSO buttons | `AppleSSOButton`, `GoogleSSOButton`, `FacebookSSOButton` | `SsoButton(AuthProvider.Apple, …)` and so on, in `dev.voir.areca.flow.auth.sso` (`flow-auth` artifact). `TwitterSSOButton` has no direct replacement |
| Keyboard | `CustomKeyboardController`, `LocalCustomKeyboardController`, `CustomKeyboardHost` | `KeyboardController`, `LocalKeyboardController`, `KeyboardHost` |
| Charts | `HorizontalStackedBarChart` | `StackedBarChart` |
| Date and time | `Calendar`, `CalendarDay`, `MonthHeader`, `CalendarStyle`, `CalendarSize` in `dev.voir.areca.datetime` | Same names in `dev.voir.areca.datetime.calendar` |
| Modifiers | `Modifier.conditional(condition, modifier)` | `Modifier.thenIf(condition) { … }` |

These 1.0.0 names have no direct replacement in 1.x. The recommended approach follows each one.

| 1.0.0 | Recommended approach in 1.x |
| --- | --- |
| `ExpandableSplitLayout`, `SidebarLayout`, `WithFloating` | The adaptive layouts: `ListDetail`, `SupportingPane`, `AdaptiveNavigation`. See [the component catalogue](components.md) |
| `SettingsAsyncTrailing`, `SettingsAsyncState` | `SettingsRow(status = …)` with `SettingsRowStatus.Idle`, `.Loading` or `.Error(message)` |
| `RowDivider` | `Settings` sections draw their own separators (`Section(separated = …)`) |
| `FormatVisualTransformation` | The `format` parameter of `TextField` and `PasswordField` |
| `PullToRefreshDefaults` | `PullToRefreshStyle` and `PullToRefreshSize` |
| `WheelPickerTheme` | `WheelPickerStyle` and `WheelPickerSize` |
| `ToastDefaults` | `ToastStyle` |
| `NavItemDefaults` and every `…Tokens` class | The component's `XStyle` and `XSize` |
| `ToggleState.Disabled` | `Toggle(enabled = false)` |
| `Modifier.gradientBorder`, `animateColorFromState` | Compose's own `Modifier.border(width, brush, shape)` and `animateColorAsState` |

## From 1.0.4 to 1.1.0

These changes compile without edits, except where noted. Check each screen they touch.

| Change | What to do |
| --- | --- |
| The default surface layout is `SurfaceLayout.Grouped`: a grey page (`neutral15`) with white cards. In 1.0.4 the page was white | Pass `ArecaThemeProvider(surfaceLayout = SurfaceLayout.Plain)` to keep a white page with grey cards. Components now take their fills from the surface they are drawn on — see [Surfaces](theme.md#surfaces) |
| `BaseLayout(bg)` defaults to `Color.Unspecified`, the page ground of the surface layout, not `Color.Transparent` | Screens are now opaque, so one no longer shows through another during the iOS back swipe. Remove an explicit `bg = ArecaTheme.colors.bgCanvas`, and a `bgCanvas` background painted around the app's root: it would paint a white page under a grouped layout. A `BaseLayout` drawn over something meant to show through must now pass `bg = Color.Transparent`. `AdaptiveNavigation` already does this for its own shell |
| `LocalCardStyle` is seeded with `CardStyle.Default`, not `CardStyle.Outlined`. `CardStyle.default(bg)` defaults to `Color.Unspecified`, the `raised` fill of the surface the card sits on | A card with no style is now filled — white on a grouped page, grey on a plain one or in a sheet — with no border. Pass `style = CardStyle.Outlined`, or provide it as `LocalCardStyle`, to keep the border. `Outlined` is now transparent, keeping the ground it sits on; `SoftNeutral` is the surface's `well`, with no border; `GhostNeutral` presses to the `well`. Every preset's shape is `shapes.surface` |
| Settings sections, `Notice`, `ContentList` inset groups and `ExpandableListItemContainer.Soft` take the surface's `raised` fill; their hairlines its `separator`, a section's band the card's `band` | `SettingsDefaults.sectionStyle(bg, separator, band)` default to `Color.Unspecified`, `NoticeStyle.of(bg)` to `ArecaTheme.surface.raised`. Drop a per-screen section or notice style that only picked a ground for where it sits; pass a colour only to fix one |
| `FieldStyle` backgrounds default to `Color.Unspecified`, resolved from the new `bgRole` and `bgFocusedRole: SurfaceRole` | `outlined()` takes `SurfaceRole.Field` (white wherever it is in light); `filled()`, `soft()` and `solidNeutral()` take `SurfaceRole.Well`, and `filled()` turns to `Field` while focused. A style built with `FieldStyle(bg = …)` and a colour is unchanged |
| Fills that read a fixed ground now read a surface role: soft `Neutral` buttons, icon buttons and featured icons, `CardFooterStyle.soft(Neutral)`, the tracks and thumbs of `SlidingSelector` and `Tabs`, `ProgressBar`, `Skeleton`, `WheelPicker`, `Avatar`, `BadgeStyle.FilledChip` and a soft badge (`BadgeStyle.default(bgSoft)`), `PageCounter`, `NumberStepper`, `Table` rows, `TopBar` (`bg = Color.Unspecified`), and the charts, datetime, media and attachment grounds | Nothing, unless a style of yours copied the old token: replace `bgCanvas`, `bgSubtle`, `bgMuted` or `bgSoft` with the matching `ArecaTheme.surface` role |
| `FlowModal`, `Dialog` (`ModalStyle`) and `DropdownPopup` default to the sheet ground, `ArecaTheme.surfaces.sheet.page.ground`: white in light whatever the layout, a step above the page in dark | Their content starts the sheet's own ladder, so cards in a sheet are grey and fields in them white. A sheet given the grey of a sheet's card is laid out as one, and its cards turn white |
| `TabBar`, `BottomActionBar`, `NavRail` and the `AdaptiveNavigation` column default to `ArecaTheme.surfaces.bar` | White in light, as before; `neutral90` in dark, where it was `neutral100` |
| `LocalAvatarStyle`, `LocalBadgeStyle`, `LocalProgressBarStyle`, `LocalSkeletonStyle`, `LocalSlidingSelectorStyle`, `LocalTabsStyle`, `LocalWheelPickerStyle` and `LocalPageCounterStyle` are seeded `null` | Each component builds its style where it is drawn, from the surface under it. Reading one of these locals now gives `null`; use the type's `Default` instead |
| Line heights sit on a 4-point grid: `xs` 12/16, `sm` 14/20, `md` 16/24, `lg` 18/26, `xl` 22/28, `xl2` 26/32, `xl3` 32/40, `xl7` 72/80 | Text grows taller — `bodyMedium` from 20 to 24sp a line — so rows, buttons and cards sized by their text grow with it. Check fixed heights that held text. |
| Light `fgMuted` is a fifth of the way from `neutral60` to `neutral70` | Slightly darker, for 4.5:1 on the grey page |
| Dark `bg{Brand,Danger,Success,Warn,Info}Muted` and `accentMuted` are `neutral90` with 14% of the tone, not the ramp's `90` step; dark `bgNeutralBase` is `neutral80`, not `neutral70` | Dark muted fills are quieter. Check text you placed on them for contrast |
| `ArecaDarkTheme` keeps the surface layout and the provider's new `componentDefaults` | Move app-wide component locals from a `CompositionLocalProvider` around the content into `ArecaThemeProvider(componentDefaults = ComponentDefaults { arrayOf(…) })`, so they survive into an `ArecaDarkTheme` region |
| New `ArecaShapes.surface`, `lg` by default | The corner of every `CardStyle` preset, settings section, notice, `ContentList` inset group and `ExpandableListItemContainer.Soft`. Change all of them with `shapes = ArecaShapes.from(radius).copy(surface = …)` |
| New `ArecaTypography.overline` and `TextVariant.Overline` | A small tracked label above a group or a title. An `ArecaTypography` built by hand gets `labelSmall` tracked out |
| New `ArecaTheme.surface`, `surfaces`, `surfaceLevel`, `RaisedSurface` and `ProvideSurface` | Fill containers of your own from `ArecaTheme.surface` roles and wrap their content in `RaisedSurface`, instead of picking `bgSurface`, `bgSubtle` or an app grey |
| `Checkbox` and `CheckboxListItem` draw the dash whenever `indeterminate` is `true`, whatever `checked` is | In 1.0.4 `indeterminate` only showed while `checked` was `true`. A tap still asks for `!checked`: pass `checked = false` for a tap that selects every child, `checked = true` for one that clears them. The box takes the checked colours and reports the mixed state while the dash shows, and a `CheckboxListItem` wears its selected background |
| `FlowBadgeSelect` spaces chips on a line by `spacing.lg` by default (was `Arrangement.Start`) | Drop an explicit `horizontalArrangement = Arrangement.spacedBy(…)` that matched it. The default vertical gap stays none; each chip's touch target already leaves room |
| `Tabs(size)` is `TabsSize? = null` | `null` picks the variant's own size: `TabsSize.Segmented` for `TabsVariant.Segmented`, otherwise `LocalTabsSize` or `TabsSize.Default`. A segmented strip that relied on `LocalTabsSize` now takes `TabsSize.Segmented`; pass `size` to keep yours |
| `LocalSettingsStyles` and `LocalSettingsSizes` are public | Provide them once beside the theme instead of passing `styles` and `sizes` to every `Settings`. `Settings` and rows outside a list read them, falling back to `SettingsDefaults` |
| New `SlidingSelector(items, selected, onSelectedChange)` overload | For a selection that always has a value. `selected` is non-null and `onSelectedChange` takes `T`. Replace `mode = SelectorMode.Required` and the null check in `onSelect` with it |
| New `ConfirmSheetStyle.Danger` | For a question whose yes destroys something: the danger tile and a soft danger confirm button. Replace a hand-built danger `ConfirmSheetStyle` |
| New `Fallback(horizontalAlignment)` | Defaults to `Alignment.CenterHorizontally`. Pass `Alignment.Start` for a message that belongs to a section; the text aligns with it |
| `SelectFlowModalDefaults.Search` defaults to `FieldDefaults.style` and `FieldDefaults.size`, not a small pill (`FieldStyle.outlined(shape = CircleShape)`, `FieldSize.Small`) | The search above a picker now looks like the app's other fields. Pass `style` and `size` to keep the pill |
| New `FlowModal(visible, onDismissRequest)` and `FlowModal(item, onDismissRequest) { item -> }` overloads, `rememberLastNonNull` and `FlowModalController.hideAndWait()` | Drive a sheet from state rather than `show()` / `hide()` effects. Replace `if (item != null) FlowModal(…)` and an app `rememberLastNonNull` with the `item` form, which keeps the last item while the sheet slides out. Navigate after `hideAndWait()` |
| New `FlowModalTopBar(title, onClose, …)` | Replace an app sheet header — a `TopBar` with `FlowModalHeaderInsets`, a modal style and a `CloseAction` — with it. `bottom` holds a pinned search field |
| New `BaseLayout(floatingAction, toastState)` | Move a floating button out of `overlay` into `floatingAction`, and drop its hand-made offset and bottom spacer: the frame places it above the bar and adds its height to the content padding. Replace `overlay = { ToastHost(toasts) }` with `toastState = toasts`, which keeps toasts above the bar and the floating action |
| New `BottomActionBar(caption, ready)` and `BottomActionBarStyle(buttonNotReady, caption)` | `caption` is a line above the button. `ready = false` softens the button but keeps it pressable, for a press that runs validation; `enabled = false` still blocks the press |
| New `LocalTabReselect`, `OnTabReselect` and `ScrollToTopOnTabReselect()` on `ScrollState`, `LazyListState` and `LazyGridState`; `AdaptiveNavigation` emits when the selected destination is tapped again | Replace an app tab-reselect mechanism with `listState.ScrollToTopOnTabReselect()`. An app with its own tab bar provides `LocalTabReselect` |
| New `Fallback(title, …, tone: FallbackTone)` overload | The stock empty state (`FallbackTone.Empty`, a tray) and failure (`FallbackTone.Failure`, an alert circle) in words. Replace app `EmptyState` / `EmptyContent` wrappers with it. The glyphs ship in foundation |
| New `SkeletonBlock`, `SkeletonGroup` and `rememberSettledLoading` | A placeholder shape sized by its modifier, a region of placeholders announced once, and a loading flag without the flash on a fast load |
| New `PanelLayout` | A header on the page's ground over a rounded-top panel that fills the rest of the screen |
| New `ArecaHues`, `ArecaTheme.hues` and `ArecaThemeProvider(hues)`; `FeaturedIconStyle.solidHue(hue)` / `softHue(hue)`; `SettingsRowIcon(imageVector, hue, soft = false, …)` overload | Ten categorical colours for tiles that tell unrelated rows apart — a settings menu, categories, tags. `ArecaDarkTheme` keeps them. Replace an app `SettingsTint` / `HueTint` palette, and a `blue` ramp recoloured so `Info` tiles read as an accent, with `ArecaTheme.hues`; keep `ActionColor` for colour that means something. See [Hues](theme.md#hues) |
| New `autoSize: TextAutoSize?` on both `Text` overloads, last, `null` by default | Replace an app `FitText` built on `BasicText` with `Text(…, maxLines = 1, autoSize = TextAutoSize.StepBased(…))` |
| New `AmountText`, `Amount` and `AmountSymbolPlacement` (`….foundation.text`) | Draws an amount the app formatted, in parts: tabular figures, the minor units and the symbol a step smaller, kept in order beside a symbol in another script, `masked` dots, `autoSize`. Replace amounts split into two `Text`s with `fontWeight`, and per-style `textDirection` overrides made to stop bidi reordering |
| New `ChoiceCard` and `ChoiceCardMode` (`….foundation.layout`) | A card that is one answer of a choice, with an accent ring, a radio or checkbox mark and semantics, and a badge on its top edge. Replace plan and option cards built from `clip`, `border` and `selectable`; put the set in a `selectableGroup()` column |
| New `IconToggleButton` (`….foundation.button`) | An icon that is on or off, announced as a toggle with its state. Replace an `IconButton` whose `contentDescription` changed with the state |
| New `Chip` (`….foundation.badge`) | One chip: a toggle, or an applied filter with a remove cross and a leading mark. Sets of selecting chips stay `BadgeSelect`, `FlowBadgeSelect` and `LazyBadgeSelect` |
| `KeyboardKeys` (and `KeyboardLayout(grid)`) plays the platform keyboard tap on every press of an enabled key, through `LocalHapticFeedback`; new `LocalKeyboardHaptics`, `true` by default | Remove app vibration on key presses, and the `VIBRATE` permission it needed. Provide `LocalKeyboardHaptics provides false` where a keyboard must stay silent |

```kotlin
// A white page with grey cards, as in 1.0.4; app-wide component defaults that survive ArecaDarkTheme
ArecaThemeProvider(
    surfaceLayout = SurfaceLayout.Plain,
    componentDefaults = ComponentDefaults {
        arrayOf(
            LocalCardStyle provides CardStyle.Outlined,
            LocalSettingsStyles provides SettingsDefaults.styles(),
            LocalSettingsSizes provides SettingsDefaults.sizes(),
        )
    },
) {
    AppNavigation()
}

// SlidingSelector: required mode
SlidingSelector(
    items = periods,
    selected = period,
    onSelectedChange = { period = it },
)

// ConfirmSheet: destructive question
ConfirmSheet(
    visible = confirmingDelete,
    icon = ArecaIcons.Trash,
    title = "Delete account?",
    description = "Your synced music is removed from this device.",
    confirmText = "Delete",
    onConfirm = onDelete,
    onDismiss = { confirmingDelete = false },
    style = ConfirmSheetStyle.Danger,
)
```

Replace `SettingsDefaults.styles()` and `SettingsDefaults.sizes()` in the first example with your
app's own variation; passing the defaults changes nothing. Keep `CardStyle.Outlined` only if your
cards had no style of their own and should keep their 1.0.4 border.

### More additions

| Change | What to do |
| --- | --- |
| `ArecaThemeProvider(labels = ArecaLabels(…))`, `ArecaTheme.labels` | Pass the app's words once; drop wrappers that existed only to localise `BackAction`, `CloseAction`, `SearchField` and the like |
| `LocalTopBarTitleAlignment` | Provide `TopBarTitleAlignment.Center` once in `componentDefaults` instead of passing it to every bar |
| `ContentColumn(gutter = false)` | For children that inset themselves — settings sections, cards |
| `FallbackSize.actionGap`; `Fallback(actionLoading, actionStyle)`; the title is announced as a heading | — |
| `Chip(style, size, onClick)`; a chip with only `onRemove` is removed by a press anywhere; a chip that does nothing when pressed takes no touch-target room | — |
| `FlowModalTopBar(style, size, titleAlignment)`, `AmountText(textAlign)`, `Avatar(maxLetters)` (letters shrink to fit) | — |
| Unchecked `Radio` and `Checkbox` rings use `borderIntense` | A step darker, so an unchosen control shows on white and grey |
| `DividerStyle.line`, `ListItemStyle.surface()` / `outlined()`, chart tracks and grounds follow the surface | — |
| `BaseLayout(floatingAction)`: the content's bottom padding clears the floating action and the navigation bar under it | Drop a hand-added bottom spacer on screens with a floating action |
| Keyboard surface: `ArecaTheme.surfaces.bar` | — |
| `Text` lays a paragraph out in the interface's direction unless its style sets one | Drop an app's own stamping of the direction on its type scale |
| `SelectFlowModal` draws its rows flat on the sheet, in the settings row metrics, and gains `title`, `closeOnSelect`; `SelectOption(description, leading)` | Replace an app's own choice sheet (outlined cards, a `LazyColumn` with its own header) with `SelectFlowModal(title = …)`; drop `controller.hide()` from a single-choice `onChange`, which now closes the sheet itself |
| `SelectFlowModal`: a single choice is required — pressing the chosen option keeps it (`clearable = true` for the old clearing); a closed sheet clears its search and calls `onQueryChange("")`; the sectioned overload takes `searchPlaceholder`; `LocalSelectFlowModalLabels`; `SelectOption(detail)` | Drop guards that ignored an empty single selection and code that cleared the search on close; provide the app's labels once in `componentDefaults` |
| Sheets are flat: a `Section` on the ground of a `FlowModal`, `Dialog` or `DropdownPopup` with no `bg` of its own draws no card — rows edge to edge, hairlines from the text, the header above. The attachment and avatar source sheets list their choices as plain rows, on the 16dp sheet gutter | Remove cards, `RaisedSurface` blocks and side padding that only wrapped a sheet's form, list or settings; keep a `Card` in a sheet only for emphasis. Pass a section `bg` to keep a card on purpose |
| `CheckmarkListItem` (and so `SettingsOptionRow`, `SelectFlowModal`, `SelectField` menus) ticks in the brand colour by default | Drop `checkmarkColor = fgBrand`; pass `LocalContentColor.current` for the old ink |
| Light `fgBrandText` is the first of `brand70`, `brand80`, `brand90` that reads at 4.5:1 on `brand20`, so a light brand (a yellow) keeps soft buttons and brand text readable; the default brand keeps `brand70` | Drop an app override that darkened brand text for a light brand |
| `ExpandableListItem(trailing)` | A control before the chevron — a checkbox for the whole group — instead of a hand-built row |
| `ConfirmSheetRows` | Wrap choice rows in a `ConfirmSheet`'s `extra` in it, so they run edge to edge instead of being inset twice |
| Every `FlowModal` is as tall as its content, up to `maxHeightFraction`; the fixed height and `autoHeight` are gone (`autoHeight` is deprecated and ignored). `allowPartial` defaults to `false`, so a sheet opens at its full height; a partial height is opt-in and exists only for content taller than it; a partial `show()` on shorter content opens fully and `state` reports `Expanded`. Partially open, the footer stays in sight. The keyboard counts against the cap. `allowExpand = false` with `allowPartial` caps the sheet at the partial height, where it rests as `Expanded` | Remove `autoHeight = true`. A sheet that must stay tall whatever it holds (a search over a list that narrows) fills its body with `Modifier.fillMaxHeight()`; a long body must scroll (a lazy list or `verticalScroll`), or it is cut off at the cap. Check sheets that relied on the fixed 90% height, and code that compares `state` with `PartiallyOpened`. Pass `allowPartial = true` only to a long list that may peek first; drop `allowPartial = false`, now the default |
| `SelectField` and `SelectSegment` draw their dropdown with `SelectFlowModalDefaults.MenuList` | A custom `content` that called `SelectFlowModalDefaults.List` for a dropdown calls `MenuList` |
| Light `ArecaSurface.band`: a card's footer band and a settings section's band are a step darker than the well, so a white card's band does not merge with a grey page; the well (fields, chips) stays light | — |

### Notes

- **Dark muted fills no longer read the `*90` steps.** In 1.1 the dark `bg*Muted` fills are a dark
  card with 14% of the tone (`*60`, yellow `50`) mixed in. An app that overrode `blue90`, `red90` and
  the like to recolour those fills must override the `*60` step or `colors` instead.
- **A type scale of your own** (`ArecaThemeProvider(baseTypography = …)` or `typography = …`) does
  not get the 4pt line heights; restate them in your steps. Leave your steps' `textDirection` unset:
  Areca's `Text` lays a paragraph out in the interface's direction (`LocalLayoutDirection`) unless its style sets one, so an amount written `1 234,50 د.إ` keeps its order in an English interface. A symbol in another script *before* the figures (`د.إ 1 234,50`) is reordered by the bidi algorithm whatever the paragraph's direction — draw amounts with `AmountText`, which isolates each part.
- **Text direction.** An app that stamped the interface's direction on its type scale to keep
  amounts in order can drop it; `Text` does it now. Amounts with a symbol before the figures in
  another script need `AmountText`.
- **Custom `surfaces` and `colors` are one direction.** `ArecaDarkTheme` derives the dark ones from
  `baseColors` and the surface layout, as it always did for `colors`; re-theme through
  `baseColors` (the neutral ramp builds the surfaces) when a dark region must follow the app.
- **Desktop screenshot tests** render at the dense pointer sizes unless the content is wrapped in
  `ArecaThemeProvider(inputMode = InputMode.Touch)`.
- **Bottom insets.** `BaseLayout`'s `contentWindowInsets` still defaults to none; a pushed screen
  that relied on an app wrapper padding the navigation bar passes
  `WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)` or docks a `BottomActionBar`.

## Checklist

1. Add the GitHub Packages repository and credentials. Remove `mavenLocal()` for Areca.
2. Set every Areca artifact to one 1.x version. Remove `components`, `fields` and `table`. Rename
   `viewModel` to `viewmodel`.
3. Run the [import rewrite script](#import-rewrite-script) over your sources.
4. Replace wildcard imports of `dev.voir.areca.foundation.*` with the family packages.
5. Run these renames. They do not touch imports. Commit first and review the diff:

   ```sh
   find . -name '*.kt' -exec sed -i '' -E \
     -e 's/ArecaTheme\.space\./ArecaTheme.spacing./g' \
     -e 's/ArecaTheme\.spacing\.xxs/ArecaTheme.spacing.xs2/g' \
     -e 's/ArecaTheme\.font([^s]|$)/ArecaTheme.fonts\1/g' \
     -e 's/(IconButtonColor|FeaturedIconColor|ButtonColor)\./ActionColor./g' \
     -e 's/FeaturedIconStyle\.style\(variant = FeaturedIconVariant\.Solid, color = /FeaturedIconStyle.solid(/g' \
     -e 's/FeaturedIconStyle\.style\(variant = FeaturedIconVariant\.Soft, color = /FeaturedIconStyle.soft(/g' \
     -e 's/FeaturedIconStyle\.style\(variant = FeaturedIconVariant\.Outlined, color = /FeaturedIconStyle.outlined(/g' \
     -e 's/ToggleDefaults\.DefaultStyle/ToggleStyle.Default/g' \
     -e 's/ToggleDefaults\.defaultStyle\(/ToggleStyle.default(/g' \
     -e 's/ToggleDefaults\.(Small|Medium|Large)Size/ToggleSize.\1/g' \
     -e 's/FieldDefaults\.(inline|outlined|solidNeutral)\(\)/FieldStyle.\1()/g' \
     -e 's/FieldDefaults\.extraSmall\(\)/FieldSize.ExtraSmall/g' \
     -e 's/FieldDefaults\.small\(\)/FieldSize.Small/g' \
     -e 's/FieldDefaults\.medium\(\)/FieldSize.Medium/g' \
     -e 's/rememberTabBar\(/rememberTabBarState(/g' \
     {} +
   ```

   On Linux, use `sed -i -E` (no `''`). The expressions avoid `\b`, which macOS `sed` does not
   support, so a name of your own that ends in `ButtonColor` is rewritten too; the diff shows it.
   `FeaturedIconStyle.style` calls written with positional arguments or over several lines are not
   caught; fix them by hand.
6. Fix the imports these renames need: `ActionColor`, `ToggleSize`, `FieldStyle`, `FieldSize`.
7. Rewrite by hand: `ErrorBoundary`, `TopBarDefaults`, `FieldGroup` labels, theme setup, and each
   row of [Smaller changes](#smaller-changes) your app hits.
8. Read [From 1.0.4 to 1.1.0](#from-104-to-110) and check the screens it touches: the page and card
   grounds, cards with no style, text heights, tri-state checkboxes, chip groups, segmented tabs.

## Import rewrite script

The script rewrites every import of a public 1.0.0 name that exists under the same name, or a
listed new name, in 1.x: 215 names, every import rename in the tables above, and the whole
`dev.voir.areca.viewModel` package. It only touches
`import` lines; a name that is gone stays as it was and fails to compile, which is the list of
what to fix by hand.

Save it as `areca-imports.sed` and run `find . -name '*.kt' -exec sed -i '' -E -f areca-imports.sed {} +`
(`sed -i -E -f` on Linux). Commit first, so you can review the diff.

```sed
s/^import dev\.voir\.areca\.foundation\.ButtonColor$/import dev.voir.areca.foundation.button.ActionColor/
s/^import dev\.voir\.areca\.foundation\.ErrorBoundary$/import dev.voir.areca.foundation.feedback.Fallback/
s/^import dev\.voir\.areca\.foundation\.FeaturedIconColor$/import dev.voir.areca.foundation.button.ActionColor/
s/^import dev\.voir\.areca\.foundation\.IconButtonColor$/import dev.voir.areca.foundation.button.ActionColor/
s/^import dev\.voir\.areca\.foundation\.TextSize$/import dev.voir.areca.foundation.text.TextVariant/
s/^import dev\.voir\.areca\.foundation\.tabbar\.rememberTabBar$/import dev.voir.areca.foundation.tabbar.rememberTabBarState/
s/^import dev\.voir\.areca\.theme\.BaseColors$/import dev.voir.areca.theme.ArecaBaseColors/
s/^import dev\.voir\.areca\.theme\.BaseTypography$/import dev.voir.areca.theme.ArecaBaseTypography/
s/^import dev\.voir\.areca\.theme\.ColorVariables$/import dev.voir.areca.theme.ArecaColors/
s/^import dev\.voir\.areca\.theme\.FontVariables$/import dev.voir.areca.theme.ArecaFonts/
s/^import dev\.voir\.areca\.theme\.RadiusVariables$/import dev.voir.areca.theme.ArecaRadius/
s/^import dev\.voir\.areca\.theme\.SpaceVariables$/import dev.voir.areca.theme.ArecaSpacing/
s/^import dev\.voir\.areca\.theme\.TypographyVariables$/import dev.voir.areca.theme.ArecaTypography/
s/^import dev\.voir\.areca\.component\.navigation\.(NavData|NavNode)$/import dev.voir.areca.foundation.navigation.\1/
s/^import dev\.voir\.areca\.components\.(BadgeOption|BadgeSelect|FlowBadgeSelect|LazyBadgeSelect)$/import dev.voir.areca.foundation.badge.\1/
s/^import dev\.voir\.areca\.components\.(BaseContentListState|ContentList|ContentListDefaults|ContentListGroup|GroupedContentList|GroupedPagedContentList|LimitedContentListState|PagedContentList|PagedContentListState)$/import dev.voir.areca.foundation.collection.\1/
s/^import dev\.voir\.areca\.components\.(ColorItem|ColorShade|NumberStepper|NumberStepperSize|NumberStepperStyle|SwatchPicker|SwatchPickerLayout)$/import dev.voir.areca.foundation.selection.\1/
s/^import dev\.voir\.areca\.components\.(ConfirmationDialog|ConfirmationDialogSize|ConfirmationDialogStyle)$/import dev.voir.areca.foundation.overlay.\1/
s/^import dev\.voir\.areca\.components\.(ProgressBar)$/import dev.voir.areca.foundation.feedback.\1/
s/^import dev\.voir\.areca\.components\.navigation\.(NavItem|NavList)$/import dev.voir.areca.foundation.navigation.\1/
s/^import dev\.voir\.areca\.components\.settings\.(Section|Settings|SettingsDefaults|SettingsDsl|SettingsRow|SettingsScope|SettingsSectionScope|SettingsSectionSize|SettingsSectionStyle|SettingsSizes|SettingsStyles|SettingsTheme)$/import dev.voir.areca.foundation.settings.\1/
s/^import dev\.voir\.areca\.fields\.(AffixTextField|Field|FieldDefaults|FieldGroup|FieldGroupDefaults|FieldSize|FieldStatus|FieldStyle|PasswordField|SearchField|TextField)$/import dev.voir.areca.foundation.field.\1/
s/^import dev\.voir\.areca\.fields\.select\.(SelectField|SelectFlowModal|SelectFlowModalDefaults|SelectFlowModalState|SelectOption|SelectSelectionMode|rememberSelectFlowModalState)$/import dev.voir.areca.foundation.field.select.\1/
s/^import dev\.voir\.areca\.foundation\.(AnchoredPlacement|AnchoredState|Dialog|DropdownPopup|DropdownPopupSize|DropdownPopupState|DropdownPopupStyle|DropdownWidth|FlowModal|FlowModalController|FlowModalSize|FlowModalState|FlowModalStyle|LocalFlowModalSize|LocalFlowModalStyle|LocalModalSize|LocalModalStyle|ModalDefaults|ModalSize|ModalStyle|rememberAnchoredState|rememberDropdownPopup|rememberFlowModal)$/import dev.voir.areca.foundation.overlay.\1/
s/^import dev\.voir\.areca\.foundation\.(Avatar|AvatarSize|AvatarStyle|LocalAvatarSize|LocalAvatarStyle)$/import dev.voir.areca.foundation.avatar.\1/
s/^import dev\.voir\.areca\.foundation\.(Badge|BadgeSize|BadgeStyle|IndicatorDot|LocalBadgeSize|LocalBadgeStyle|LocalIndicatorDotSize|NotificationBadge)$/import dev.voir.areca.foundation.badge.\1/
s/^import dev\.voir\.areca\.foundation\.(Button|ButtonSize|ButtonStyle|IconButton|IconButtonSize|IconButtonStyle|LocalButtonSize|LocalButtonStyle|LocalIconButtonSize|LocalIconButtonStyle)$/import dev.voir.areca.foundation.button.\1/
s/^import dev\.voir\.areca\.foundation\.(Card|CardStyle|DividerTextPosition|Expandable|ExpandableState|HorizontalDivider|LocalCardStyle|Overlay|RelativePosition|RelativePositionLayout|VerticalDivider|rememberExpandableState)$/import dev.voir.areca.foundation.layout.\1/
s/^import dev\.voir\.areca\.foundation\.(Checkbox|CheckboxSize|CheckboxStyle|LocalCheckboxSize|LocalCheckboxStyle|LocalRadioSize|LocalRadioStyle|LocalSwatchSize|LocalSwatchStyle|LocalToggleSize|LocalToggleStyle|Radio|RadioSize|RadioStyle|Swatch|SwatchSize|SwatchStyle|Toggle|ToggleSize|ToggleState|ToggleStyle)$/import dev.voir.areca.foundation.selection.\1/
s/^import dev\.voir\.areca\.foundation\.(Chevron|ChevronDirection|ChevronSize|FeaturedIcon|FeaturedIconSize|FeaturedIconStyle|Icon|IconSize|LocalChevronSize|LocalFeaturedIconSize|LocalFeaturedIconStyle|LocalIconSize)$/import dev.voir.areca.foundation.icon.\1/
s/^import dev\.voir\.areca\.foundation\.(GroupedGridItem|LazyHorizontalGroupedGrid|LazyVerticalGroupedGrid)$/import dev.voir.areca.foundation.collection.\1/
s/^import dev\.voir\.areca\.foundation\.(Heading|HeadingSize|HeadingStyle|Text|TextColor)$/import dev.voir.areca.foundation.text.\1/
s/^import dev\.voir\.areca\.foundation\.(LazyList|List|ListDsl|ListEntry|ListItem|ListItemSize|ListItemStyle|ListScope|LocalListItemSize|LocalListItemStyle)$/import dev.voir.areca.foundation.list.\1/
s/^import dev\.voir\.areca\.foundation\.(LocalSpinnerSize|ProgressBar|Skeleton|Spinner|SpinnerSize|Toast|ToastAnimation|ToastColor|ToastData|ToastHost|ToastState|ToastStyle|rememberToastState)$/import dev.voir.areca.foundation.feedback.\1/
s/^import dev\.voir\.areca\.foundation\.(LocalTabsSize|LocalTabsStyle|TabItem|TabItemScope|Tabs|TabsHorizontalPosition|TabsOrientation|TabsSize|TabsState|TabsStyle|TabsVariant|TabsVerticalPosition|rememberTabsState)$/import dev.voir.areca.foundation.tabs.\1/
s/^import dev\.voir\.areca\.foundation\.(LocalTopBarSize|LocalTopBarStyle|TopBar|TopBarSize|TopBarStyle)$/import dev.voir.areca.foundation.topbar.\1/
s/^import dev\.voir\.areca\.foundation\.(WheelPicker|WheelPickerColumn|WheelPickerColumnState|rememberWheelPickerColumnState)$/import dev.voir.areca.foundation.wheel.\1/
s/^import dev\.voir\.areca\.viewModel\.ReloadStrategy$/import dev.voir.areca.viewmodel.list.ReloadStrategy/
s/^import dev\.voir\.areca\.viewModel\./import dev.voir.areca.viewmodel./
```

Three imports need a second look after the script:

- `ActionColor` may now be imported twice, where a file used two of the old colour enums. Remove
  the duplicate.
- `Fallback` replaces the `ErrorBoundary` import, but the call still needs rewriting
  ([ErrorBoundary](#errorboundary)).
- `FeaturedIconVariant`, `ToggleDefaults`, `TopBarDefaults` and the other names in
  [Smaller changes](#smaller-changes) are left alone on purpose.
