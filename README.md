# Areca

A Compose Multiplatform design system for Android, iOS and the JVM, published as
`dev.voir.areca:*`.

## Repository layout

The top level answers one question: what does this code need in order to be useful?

```text
ui/            The design system. Theme-bound — every module here reads ArecaTheme tokens and
               expects an ArecaThemeProvider above it.
flow/          Whole screens and screen sequences, assembled from `ui`. Theme-bound and opinionated
               about composition, but free of business logic.
standalone/    Independently usable. No theme, no dependency on anything else in this repository.
playground/    The showcase application. Not published.
skills/        Agent Skills for code built on Areca, shipped as the `areca` Claude Code plugin.
```

### `ui/`

| Module | Artifact | What it is |
| --- | --- | --- |
| `ui/foundation` | `dev.voir.areca:foundation` | The design system itself: tokens, components, fields, lists, layouts, table, navigation, settings, notices and status scenes. Every other `ui` module depends on it. See [ui/foundation/README.md](ui/foundation/README.md) |
| `ui/charts` | `dev.voir.areca:charts` | Bar and stacked-bar charts. See [ui/charts/README.md](ui/charts/README.md) |
| `ui/crop` | `dev.voir.areca:crop` | Image cropper: the stage, its toolbar and dial, masks, aspect ratios, straightening, zoom, rotation and mirroring, and rendering the result |
| `ui/datetime` | `dev.voir.areca:datetime` | Calendar, date range, time picker and the composed date-time field. Puts `kotlinx-datetime` on your API surface, which is why it is separate. See [ui/datetime/README.md](ui/datetime/README.md) |
| `ui/keyboard` | `dev.voir.areca:keyboard` | On-screen keyboard: host, layouts, key grids |

### `flow/`

| Module | Artifact | What it is |
| --- | --- | --- |
| `flow/auth` | `dev.voir.areca:flow-auth` | Parts for authentication screens — scaffold, credential and code fields, provider buttons — and recipes for assembling them, email-first sign-in and emailed-link pages included |
| `flow/profile` | `dev.voir.areca:flow-profile` | Parts for account screens — profile cards, settings-list rows for a profile, the account switcher, devices, confirmation sheets — and recipes for assembling them |
| `flow/attachments` | `dev.voir.areca:flow-attachments` | Parts for attaching files — the strip of files, the sheets that add one, the viewer — and recipes for assembling them |

A flow module owns composition, not behaviour: it draws the pieces of a sign-in screen and reports
that a button was pressed. It does not know what signing in means, never reaches the network, and
carries no navigation or dependency-injection library.

It also does not ship whole screens. A screen is a handful of product decisions, and a composable
taking all of them is worse than the assembly it replaces — so the module ships parts and the
README ships recipes to copy. What a flow module may and may not do is
[flow/FLOWS.md](flow/FLOWS.md); how to use each is in its own README —
[flow/auth/README.md](flow/auth/README.md), [flow/profile/README.md](flow/profile/README.md),
[flow/attachments/README.md](flow/attachments/README.md).

Flow modules never depend on one another. `flow-auth`, `flow-profile` and `flow-attachments` are
separate flows, and an application can take any of them without the others.

These are the only modules whose artifact id carries its group. `:flow:auth` would otherwise
publish as `dev.voir.areca:auth`, which reads as an authentication library rather than a set of
screens, and `:flow:profile` as `dev.voir.areca:profile`. See FLOWS.md §8.

### `standalone/`

| Module | Artifact | What it is |
| --- | --- | --- |
| `standalone/icons` | `dev.voir.areca:icons` | 1,062 vector icons: 1,059 converted from Lucide and Tabler Icons, plus three sign-in marks (see `standalone/icons/README.md` and `NOTICE.md`). Nothing else in this repository depends on it — see below |
| `standalone/viewmodel` | `dev.voir.areca:viewmodel` | Multiplatform ViewModel, UI state and paged-list helpers. Knows nothing about Areca or Compose UI |

Gradle project paths carry the group (`:ui:foundation`, `:standalone:icons`); published
coordinates do not (`dev.voir.areca:foundation`), apart from the `flow` modules noted above.
Moving a module between groups does not rename its artifact.

## Namespaces

Within `foundation`, one rule decides where a declaration lives:

- `dev.voir.areca.foundation.*` — things you place on screen. Every component lives in the package
  of the family it belongs to; the root holds only what every family uses: `LocalContentColor`,
  `LocalTextStyle` and `AnnounceOnChange`.
- `dev.voir.areca.*` — the machinery underneath them: `theme`, `modifier`, `indication`, `shape`,
  `platform`, and `ExperimentalArecaApi` at the root. A region that is dark whatever the theme — a
  photo editor, a media viewer — is `ArecaDarkTheme` in `theme`, with `ArecaTheme.darkColors` for
  style defaults resolved outside it, and paints the bars around it with `SystemBars` in `platform`.

How to use each family is [ui/foundation/README.md](ui/foundation/README.md).

| Package | What it holds |
| --- | --- |
| `button` | `Button`, `IconButton`, and the `ActionColor` family both resolve |
| `text` | `Text`, `Heading`, `TextLink`, `TextLinkPrompt`, `SensitiveText` |
| `icon` | `Icon`, `FeaturedIcon`, `Chevron` |
| `badge` | `Badge`, `NotificationBadge`, `IndicatorDot`; `BadgeSelect`, `LazyBadgeSelect`, `FlowBadgeSelect` |
| `avatar` | `Avatar` |
| `selection` | `Checkbox` and `Radio` and their shared `SelectionStyle`; `Toggle`; `Swatch`, `SwatchPicker`, `NumberStepper` |
| `field` | Text, password, search and affix fields; `OtpField` for one-time codes; `FieldRow`, `FieldGroup`, `Field`. `SelectField` and `SelectFlowModal` are in `field.select` |
| `feedback` | `Spinner`, `ProgressBar`, `StepIndicator`, `Skeleton`, `RequirementList`, `Fallback`, `Toast` and `ToastHost` |
| `notice`, `status` | `Notice`; `StatusMark` and `StatusScene` |
| `overlay` | `Dialog`, `ConfirmationDialog`, `FlowModal` and its `FlowModalActionBar`, `ConfirmSheet`, `DropdownPopup` and the modal and anchoring metrics behind them; `edgeToEdgeDialogProperties` for a window that draws behind the system bars |
| `layout` | Screen layouts (`BaseLayout`, `CollapsingHeaderLayout`, `PullToRefreshLayout`), `Card`, `HorizontalDivider` and `VerticalDivider`, `Expandable`, `Overlay`, `RelativePositionLayout` |
| `slot` | `SlotRow` and its `SlotRowLayout`, which most rows are built on |
| `list`, `settings` | Grouped lists and their items; the settings list built on them |
| `collection` | `ContentList` and the lazy, paged `PagedContentList`, with their grouped forms; `LazyVerticalGroupedGrid` and `LazyHorizontalGroupedGrid` |
| `table` | `Table` and `LazyTable` |
| `navigation`, `tabs`, `tabbar`, `topbar` | `NavList`; `SwipePager`, its `PagerControls` and `PagerController`, and `PageIndicator`; `Tabs`; `TabBar` and `FloatingTabBar`; `TopBar` and `LargeTopBar`, their named controls (`BackAction`, `ShareAction`, `DoneAction`, …) and the four control appearances (`TopBarActionStyle`) |
| `wheel` | `WheelPicker` |

A new component joins the family it belongs to. It gets a package of its own only when it starts a
family nothing else fits.

Every other module owns its own namespace: `dev.voir.areca.charts`, `dev.voir.areca.crop`,
`dev.voir.areca.datetime`, `dev.voir.areca.keyboard`, `dev.voir.areca.icons`, `dev.voir.areca.viewmodel`,
`dev.voir.areca.flow.auth`, `dev.voir.areca.flow.profile`, `dev.voir.areca.flow.attachments`.

Within a flow module, the package says what a declaration is: configuration at the root, composable
pieces in `part`, and — in `flow/auth` — the provider buttons in `sso`, which live there rather
than in `foundation` because their fixed brand colours are not theme tokens and nothing but an
authentication screen draws them. `flow/profile` adds `row` for its
settings-list rows, `session` for the device list and `sheet` for its bottom sheets.

## Icons are opt-in

`:standalone:icons` ships 1,062 icons (1,059 converted from Lucide and Tabler Icons, plus three
sign-in marks), and no library module depends on it. A module that draws an icon vendors its own
`internal` copy instead, so a consumer that wants two icons does not inherit 1,062 of them.

The copies are ordinary committed sources under `dev.voir.areca.icon` (or `<module>.icon`),
rendered from `:standalone:icons` by `syncBundledIcons` and checked by `verifyBundledIcons`, which
runs as part of `check`. The package belongs to the task: a file there that is not a declared icon is
deleted by the sync. The task also writes `src/commonMain/resources/META-INF/<package>/ICONS-NOTICE.md`,
the Lucide and Tabler licence notices the copies carry into the module's jar, AAR and sources jars.
To change what a module draws, edit `bundledIcons` in its build script and re-run the task:

```bash
./gradlew :ui:foundation:syncBundledIcons
```

Applications that want the full set depend on `dev.voir.areca:icons` themselves — it is not
inherited from `foundation`.

## Verifying

`ci/verify` is the single entry point, and CI runs exactly it:

```bash
./ci/verify
```

It builds and checks every module for every target, then confirms the libraries still publish.

### Public API

Every published module commits its public API as a dump under its `api/` directory:
`api/jvm/<module>.api` for the desktop JVM target and `api/<module>.klib.api` for the iOS klibs.
The Android target has no dump of its own; it shares the same common API. `check`, and therefore
`./gradlew build` and `ci/verify`, runs `checkLegacyAbi`, which compiles the module and fails when
its public API differs from the dump — a public declaration added, removed or changed, including
one made public by accident.

When an API change is intended, rewrite the dumps and commit them with the change:

```bash
./gradlew updateLegacyAbi
```

Run it on macOS, where the iOS targets compile; elsewhere the klib dump keeps the iOS declarations
it already has instead of recomputing them. The dump diff is what a reviewer reads to see a pull
request's API changes: anything it adds is a commitment to support, and anything it removes or
changes breaks consumers.

## Agent skills

Two Agent Skills teach coding agents to build on Areca in other repositories:

| Skill | Use it for |
| --- | --- |
| `areca-ui-v1` | Compose Multiplatform screens with `foundation` and the other `ui`, `flow` and `icons` modules: theme, tokens, component choice, customisation, accessibility, localisation and recipes |
| `areca-viewmodel-v1` | Presentation code on `dev.voir.areca:viewmodel`: state, actions, effects, lists, concurrency and tests |

They live in [`skills/`](skills), with symlinks under `.agents/skills/` for agents that read that
path. The repository root is the `areca` Claude Code plugin (`.claude-plugin/plugin.json`) and
the `areca-ui` marketplace that lists it (`.claude-plugin/marketplace.json`), so the skills load as
`areca:areca-ui-v1` and `areca:areca-viewmodel-v1`:

```text
/plugin marketplace add VoirDev/areca-ui
```

```text
/plugin install areca@areca-ui
```

The plugin version is the library version: `plugin.json` repeats `VERSION`, Prepare Release
changes both, and `check` fails when they differ. An installed plugin updates with
`/plugin update areca@areca-ui` once a release raises that version.

The skills describe the public API and behaviour, so a change to either updates `skills/`,
[ui/foundation/AGENTS.md](ui/foundation/AGENTS.md) and the module's README in the same pull
request.

## Releasing

Every library is built with the JDK 21 toolchain and published as Java 17 bytecode, for JVM and
Android alike, so desktop consumers need Java 17 or later.

`VERSION` holds the version every module builds as; `-PreleaseVersion=X.Y.Z` overrides it for one
build. It holds either the last release, `X.Y.Z`, or, before the first release, the development
version `1.0.0-SNAPSHOT`, which local builds (`publishToMavenLocal` included) publish as; an app
consuming a local build pins `1.0.0-SNAPSHOT`. Only releases change `VERSION`; it keeps the released version until the
next release, never moving on to a further `-SNAPSHOT`. The `version` of
`.claude-plugin/plugin.json` repeats `VERSION` exactly (see [Agent skills](#agent-skills)), and
`check` fails when the two differ:

1. **Prepare Release** (manual, from `main`) sets `VERSION` and the plugin version to the next
   release and opens the `Release vX.Y.Z` pull request from `release/vX.Y.Z`
   (`ci/release/prepare-version`, `ci/release/validate-changes`, `ci/release/create-pull-request`).
   The `patch`, `minor` or `major` bump gives the lowest release of that kind above `VERSION`: from `1.2.3` they give `1.2.4`, `1.3.0` and `2.0.0`; from
   `1.2.3-SNAPSHOT`, `1.2.3`, `1.3.0` and `2.0.0`. Its optional `version` input sets an exact
   `X.Y.Z` instead, which must be greater than `VERSION`, a release counting as greater than its own
   `-SNAPSHOT`. A version already tagged `vX.Y.Z` is rejected.
2. On that pull request, Verify runs `ci/verify` (never skipped there), and **Build release
   artifacts** runs `ci/release/build-artifacts` on macOS: it stages every publication in
   `build/release-staging`, checks coordinates and files, resolves the result as a Java 17 consumer
   (`ci/release/consumer`), and keeps it as the run's `release-artifacts`.
3. Merging starts **Publish Release**. After approval in the `release` environment it signs the kept
   files and uploads them unchanged to Maven Central (`ci/release/publish-maven-central`), then tags
   `vX.Y.Z` and creates the GitHub Release. A failed run is re-run; it never rebuilds.

The first release is 1.0.0: run Prepare Release with `version` set to `1.0.0`, or with any bump,
since from `1.0.0-SNAPSHOT` all three give `1.0.0`. Its pull request changes `VERSION` and the
plugin version from `1.0.0-SNAPSHOT` to `1.0.0`, and nothing else, and merging it publishes as
above. Only a release is ever built for
publication: `build-artifacts`, `check-staging` and `detect` reject a `-SNAPSHOT`.

`main` requires the `Build and check whole repository` and `Build release artifacts` checks on an
up-to-date branch. Prepare Release uses the `RELEASE_BOT_TOKEN` secret and the `RELEASE_BOT_NAME`
and `RELEASE_BOT_EMAIL` variables; the `release` environment holds `MAVEN_CENTRAL_USERNAME` and
`MAVEN_CENTRAL_PASSWORD` (a Central Portal user token), `SIGNING_KEY` (an ASCII-armoured private
key) and `SIGNING_KEY_PASSWORD`.

## Conventions

- Coding agents working in this repository start from [AGENTS.md](AGENTS.md).
- Showcase pages follow the authoring contract in [playground/SHOWCASES.md](playground/SHOWCASES.md).
  Screen pages follow its §13.
- Flow modules follow the contract in [flow/FLOWS.md](flow/FLOWS.md).
- Foundation components follow the model in
  [ui/foundation/README.md](ui/foundation/README.md#how-every-component-works): a style and a size
  with `Default`, presets and `default(...)`, nullable locals seeded by `ArecaThemeProvider`, and
  one parameter order.
- Agents building screens on `foundation` follow [ui/foundation/AGENTS.md](ui/foundation/AGENTS.md):
  which component answers which intent, exact signatures, and screen recipes.
