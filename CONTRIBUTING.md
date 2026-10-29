# Contributing

Thanks for helping with Areca. This page covers building the repository, the checks a change must
pass, and the contracts each part of the tree follows. How the repository is divided, and why, is
[docs/maintainers/architecture.md](docs/maintainers/architecture.md).

## Setup

- JDK 21 (the build's toolchain). Libraries are published as Java 17 bytecode.
- macOS with Xcode to compile the iOS targets. Elsewhere the JVM and Android targets build, and the
  iOS API dumps keep the declarations they already have.
- Android SDK for the Android targets (`local.properties` with `sdk.dir`).

Run the showcase application:

```bash
./gradlew :playground:run
```

## Verify

`ci/verify` is the single entry point, and CI runs exactly it:

```bash
./ci/verify
```

It builds and checks every module for every target, checks that the docs cover every public
component, and confirms the libraries still publish.

Behaviour that needs real input — gestures, presses, what a screen reader can set — is tested with
Compose `ui-test` on the JVM, in a module's `jvmTest` (`ui/media`, `flow/gallery`); it runs with
`build`. What a part looks like is checked by the playground's render tests, which write PNGs to
`playground/build/screens`.

## Public API

Every published module commits its public API as a dump under its `api/` directory:
`api/jvm/<module>.api` for the desktop JVM target and `api/<module>.klib.api` for the iOS klibs.
The Android target shares the common API and has no dump of its own. `check` runs
`checkLegacyAbi`, which fails when the public API differs from the dump — a public declaration
added, removed or changed, including one made public by accident.

When an API change is intended, rewrite the dumps on macOS and commit them with the change:

```bash
./gradlew updateLegacyAbi
```

The dump diff is what a reviewer reads to see a pull request's API changes: anything it adds is a
commitment to support, and anything it removes or changes breaks consumers.

## Documentation

A change to a published module's public API or behaviour updates, in the same pull request:

- the module's README;
- [docs/components.md](docs/components.md), for a foundation component;
- the Agent Skills in [`skills/`](skills) — `areca-ui-v1` for UI modules, `areca-viewmodel-v1` for
  `viewmodel`. Edit skills in `skills/` only; `.agents/skills/` holds symlinks to them.

`ci/check-docs-coverage` (part of `ci/verify`) fails when a public composable is not named in its
module's README, in the `areca-ui-v1` skill, and — for foundation — in the catalogue:

```bash
./ci/check-docs-coverage
```

Docs describe what exists, where it is and how to use it. Rationale belongs in
[architecture.md](docs/maintainers/architecture.md) or KDoc, not in usage docs. Screenshots are
rendered from the playground:

```bash
./gradlew :playground:renderDocImages
```

## Icons

### Adding an icon to the set

[`standalone/icons/icons.tsv`](standalone/icons/icons.tsv) maps every property to its upstream
drawing: name, set (`lucide`, `tabler` or `manual`), upstream id, and flags (`mirror`, `filled`,
`same-as=<Name>`). The development-only `generateIcons` task
([`generate-icons.gradle.kts`](standalone/icons/generate-icons.gradle.kts)) reads the mapping and the
upstream SVGs and rewrites every generated file and `NOTICE.md`:

```bash
./gradlew :standalone:icons:generateIcons -Plucide=<unpacked lucide-static 1.55.0 package> -Ptabler=<unpacked @tabler/icons 3.49.0 package>
```

It parses `path`, `circle`, `ellipse`, `rect`, `line`, `polyline` and `polygon` elements, drops
Tabler's invisible bounding box, and fails on anything off the 24-unit, 2-unit-stroke grid. It
deletes any icon file the mapping no longer names, except `manual` ones and `ArecaIcons.kt`. It is
not part of `check` or CI, and refuses other upstream versions until the expected versions in the
script are changed.

To add an icon: add a row to `icons.tsv` (a `mirror` flag if it points sideways, `same-as` if it
repeats another drawing), run the task, update the playground showcase if useful, run
`./gradlew :standalone:icons:jvmTest`, and review the diff. The test builds every icon and checks
its size, viewport, name, mirroring, stroke, and that repeated drawings are declared aliases.

### Bundled icons

No library module depends on `:standalone:icons`. A module that draws icons declares them before
applying the bundling script:

```kotlin
extra["bundledIconsPackage"] = "dev.voir.areca.crop.icon"
extra["bundledIcons"] = listOf("Check", "XClose")
apply(from = rootProject.file("gradle/bundled-icons.gradle.kts"))
```

`syncBundledIcons` writes committed `internal val BundledIcons.<Name>` copies into that package,
which the task owns — a file there that is not a declared icon is deleted — and a copy of the
licence notices as `src/commonMain/resources/META-INF/<package>/ICONS-NOTICE.md`, so the module's
jar, AAR and sources jars carry them. `verifyBundledIcons` runs as part of `check` and fails when a
copy or the notice has drifted. Edit icons only in `:standalone:icons`, then re-sync:

```bash
./gradlew :ui:foundation:syncBundledIcons
```

## Contracts

| Part | Contract |
| --- | --- |
| Foundation components | The component model in [docs/customising.md](docs/customising.md): a style and a size with `Default`, presets and `default(...)`, nullable locals seeded by `ArecaThemeProvider`, one parameter order. A new component joins the package of its family |
| Flow modules | [flow/FLOWS.md](flow/FLOWS.md) |
| Playground pages | [playground/SHOWCASES.md](playground/SHOWCASES.md); screen pages follow its §13 |
| Coding agents in this repository | [AGENTS.md](AGENTS.md) |

## Releasing

Library and skills releases are run by maintainers; see
[docs/maintainers/releasing.md](docs/maintainers/releasing.md).
