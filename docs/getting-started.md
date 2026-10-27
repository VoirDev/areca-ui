# Getting started

From an empty Compose Multiplatform project to a first Areca screen: add the repository, add the
dependency, wrap the application in the theme, and build a screen from the components.

## Requirements

| | |
| --- | --- |
| Project | Kotlin Multiplatform with Compose Multiplatform, or a single-platform Compose project on Android or the desktop JVM |
| Targets | Android (minimum SDK 23), iOS (`iosArm64`, `iosSimulatorArm64`) and the desktop JVM |
| Desktop JVM | Java 17 or later. The libraries are published as Java 17 bytecode |
| Versions | Areca is built with Kotlin 2.3 and Compose Multiplatform 1.11; use those or later |

## Add the repository

Areca is published to GitHub Packages. Add its repository to `settings.gradle.kts`, limited to the
`dev.voir.areca` group so no other dependency is looked up there:

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

GitHub Packages asks for credentials even to read a public package.
`credentials(PasswordCredentials::class)` reads them from the Gradle properties `ArecaUsername` and
`ArecaPassword` (the repository's `name` plus `Username` and `Password`): your GitHub user name and a
personal access token (classic) with the `read:packages` scope. Keep them out of the project, in
`~/.gradle/gradle.properties`:

```properties
ArecaUsername=<your GitHub user name>
ArecaPassword=<a classic token with read:packages>
```

In GitHub Actions, pass them as environment variables. A workflow's own `GITHUB_TOKEN` reads the
packages when they are public, or when the package settings grant the consuming repository access:

```yaml
env:
  ORG_GRADLE_PROJECT_ArecaUsername: ${{ github.actor }}
  ORG_GRADLE_PROJECT_ArecaPassword: ${{ secrets.GITHUB_TOKEN }}
```

## Add the dependency

`foundation` is the design system: the theme and every everyday component. `icons` is the optional
icon set the examples in these docs use (`ArecaIcons.*`); `foundation` does not bring it.

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("dev.voir.areca:foundation:<version>")
            implementation("dev.voir.areca:icons:<version>") // optional: ArecaIcons.*
        }
    }
}
```

The released versions are listed on the repository's
[GitHub Releases](https://github.com/VoirDev/areca-ui/releases). Every Areca module is released
together, so use one version for all of them.

Each icon is an extension property on `ArecaIcons`, imported one by one:

```kotlin
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Trash
```

Without `icons`, pass your own `ImageVector` or `Painter` wherever a component takes an icon.

## Wrap the application in the theme

Put one `ArecaThemeProvider` near the root of the application. It puts the tokens in scope and seeds
every component's default style and size:

```kotlin
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider

@Composable
fun App() {
    ArecaThemeProvider(darkTheme = isSystemInDarkTheme()) {
        Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) {
            NewsletterScreen()
        }
    }
}
```

- `darkTheme` picks light or dark. Nothing follows the system on its own: pass
  `isSystemInDarkTheme()` or the user's own setting.
- The provider paints nothing. Give the screen its ground, `ArecaTheme.colors.bgCanvas`, yourself.
- Without a provider, components fall back to the light defaults, so a preview or a test composes
  unwrapped. Only the provider can make the theme dark or change a token.

To use your own colours, type, corners and spacing, see [Theme](theme.md).

## Build a first screen

A screen is a `BaseLayout` with a `TopBar`, and content that applies the padding the layout hands
it. This one asks for an email address and subscribes it:

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.contentInsets

@Composable
fun NewsletterScreen() {
    var email by rememberSaveable { mutableStateOf("") }
    var subscribed by rememberSaveable { mutableStateOf(false) }

    BaseLayout(
        bg = ArecaTheme.colors.bgCanvas,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = { TopBar("Newsletter") },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .contentInsets(),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
        ) {
            Text(
                "One email a month, nothing else.",
                variant = TextVariant.BodyMedium,
                color = TextColor.Subtle,
            )
            FieldGroup(label = "Email", required = true) { field ->
                TextField(
                    email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    field = field,
                    singleLine = true,
                )
            }
            Button(
                if (subscribed) "Subscribed" else "Subscribe",
                onClick = { subscribed = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = "@" in email && !subscribed,
            )
        }
    }
}
```

What each piece does:

| Piece | Package | Does |
| --- | --- | --- |
| `BaseLayout` | `dev.voir.areca.foundation.layout` | Draws the bars over full-height content and hands the content their height as `padding`. Apply it, or the bars hide the first and last lines |
| `TopBar` | `dev.voir.areca.foundation.topbar` | The screen's title, with `leading` and `trailing` controls such as `BackAction` |
| `contentInsets()` | `dev.voir.areca.theme` | Pads by the theme's screen gutters. `horizontalContentInsets()` pads the sides only |
| `Text` | `dev.voir.areca.foundation.text` | All text. `variant` names its role, `color` its ink, both from the theme |
| `FieldGroup` | `dev.voir.areca.foundation.field` | The label and the message line. Pass its `field` scope on to the field inside |
| `TextField` | `dev.voir.areca.foundation.field` | Free text |
| `Button` | `dev.voir.areca.foundation.button` | The screen's primary action. The default style is the theme's solid brand button |

Values come from `ArecaTheme`, never literals: `ArecaTheme.spacing.xl`, not `16.dp`. That keeps the
screen right when the theme changes.

A list instead of a form uses the same frame. `LazyList` (in `dev.voir.areca.foundation.list`)
takes the padding as its `contentPadding`, so rows scroll under the bars and rest clear of them; it
draws the group's surface and the hairlines between rows:

```kotlin
BaseLayout(
    bg = ArecaTheme.colors.bgCanvas,
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = { TopBar("Inbox") },
) { padding ->
    LazyList(contentPadding = padding) {
        messages.forEach { message ->
            item(key = message.id, title = { Text(message.subject) }, onClick = { onOpen(message) })
        }
    }
}
```

[Recipes](recipes/README.md) build whole screens this way: settings, forms, lists with loading and
error states, confirmations, tabs, pickers.

## Add more modules

Every module is `dev.voir.areca:<artifact>:<version>`, and every module except `icons` and
`viewmodel` needs `foundation` and the `ArecaThemeProvider` above it.

| You need | Artifact | Read |
| --- | --- | --- |
| The theme and the everyday components | `foundation` | [Components](components.md), [ui/foundation](../ui/foundation/README.md) |
| 1,062 icons as `ArecaIcons.*` | `icons` | [standalone/icons](../standalone/icons/README.md) |
| Bar, stacked bar, column and line charts, a meter bar, a legend | `charts` | [ui/charts](../ui/charts/README.md) |
| A calendar, a date range, a time picker, a date-time field | `datetime` | [ui/datetime](../ui/datetime/README.md) |
| An image cropper | `crop` | [ui/crop](../ui/crop/README.md) |
| An on-screen keyboard: keypads, calculators, custom layouts | `keyboard` | [ui/keyboard](../ui/keyboard/README.md) |
| Sign-in, sign-up and verification screens | `flow-auth` | [flow/auth](../flow/auth/README.md) |
| Profile, account and device screens | `flow-profile` | [flow/profile](../flow/profile/README.md) |
| Attaching, viewing and managing files | `flow-attachments` | [flow/attachments](../flow/attachments/README.md) |
| A multiplatform ViewModel, UI state and paged lists | `viewmodel` | [standalone/viewmodel](../standalone/viewmodel/README.md) |

`datetime`, and `flow-profile` through it, put `kotlinx-datetime` types on your API surface.

```kotlin
commonMain.dependencies {
    implementation("dev.voir.areca:foundation:<version>")
    implementation("dev.voir.areca:charts:<version>")
    implementation("dev.voir.areca:flow-auth:<version>")
}
```

## Run the playground

The playground is a desktop application with a page for most components and every flow: their
variants, states and sizes, with live controls. Clone the repository and run, from its root:

```bash
./gradlew :playground:run
```

## Next steps

- [Components](components.md) — every foundation component, what it is for and where it lives.
- [Theme](theme.md) — the tokens, changing them, and regions that are always dark.
- [Customising](customising.md) — styles, sizes and defaults for a whole subtree.
- [Accessibility](accessibility.md) and [Localisation](localisation.md) — what to pass so every
  screen reads and speaks right in your language.
- [Recipes](recipes/README.md) — whole screens to copy.
- **Coding agents.** The `areca` Claude Code plugin teaches an agent to build on Areca: the
  `areca-ui-v1` skill for screens, `areca-viewmodel-v1` for presentation code. Install it with

  ```text
  /plugin marketplace add VoirDev/areca-ui
  ```

  ```text
  /plugin install areca@areca-ui
  ```

  The skills live in [`skills/`](../skills/areca-ui-v1/SKILL.md).
