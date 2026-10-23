# Architecture

How the repository is divided, and the reasoning behind the rules contributors follow. Users of
Areca do not need this page; the [docs index](../README.md) is for them.

## Three groups, by what a module needs

The top level answers one question: what does this code need in order to be useful?

| Group | Needs | Modules |
| --- | --- | --- |
| `ui/` | The theme. Every module reads `ArecaTheme` tokens and expects an `ArecaThemeProvider` above it | `foundation`, `charts`, `crop`, `datetime`, `keyboard` |
| `flow/` | The theme and `ui`. Whole screens assembled from `ui`, opinionated about composition, free of business logic | `auth`, `profile`, `attachments` |
| `standalone/` | Nothing in this repository | `icons`, `viewmodel` |

`foundation` is the only module every other `ui` and `flow` module depends on, so an application
has exactly one theme to set up. It carries `compose-runtime`, `compose-foundation` and `compose-ui`
as API dependencies and nothing else.

`datetime` is its own module because it puts `kotlinx-datetime` on its API; an application that
draws no dates does not take that dependency.

## Coordinates and namespaces

Gradle project paths carry the group (`:ui:foundation`, `:standalone:icons`); published coordinates
do not (`dev.voir.areca:foundation`). Moving a module between groups does not rename its artifact.

The `flow` modules are the exception: their artifact id carries the group. `:flow:auth` would
otherwise publish as `dev.voir.areca:auth`, which reads as an authentication library rather than a
set of screens. See [FLOWS.md §8](../../flow/FLOWS.md).

Within `foundation`, one rule decides where a declaration lives:

- `dev.voir.areca.foundation.*` — things you place on screen. Every component lives in the package
  of the family it belongs to; the root holds only what every family uses: `LocalContentColor`,
  `LocalTextStyle` and `AnnounceOnChange`.
- `dev.voir.areca.*` — the machinery underneath them: `theme`, `modifier`, `indication`, `shape`,
  `platform`, and `ExperimentalArecaApi` at the root.

A new component joins the family it belongs to. It gets a package of its own only when it starts a
family nothing else fits.

Every other module owns its own namespace (`dev.voir.areca.charts`, `dev.voir.areca.flow.auth`, …).
Within a flow module the package says what a declaration is: configuration at the root, composable
pieces in `part`, and module-specific families such as `sso`, `row`, `session`, `sheet` and
`viewer`. The provider buttons live in `flow/auth`'s `sso` rather than in `foundation` because their
fixed brand colours are not theme tokens and nothing but an authentication screen draws them.

## Flows ship parts, not screens

A flow module owns composition, not behaviour: it draws the pieces of a sign-in screen and reports
that a button was pressed. It does not know what signing in means, never reaches the network, and
carries no navigation or dependency-injection library.

It does not ship whole screens either. A screen is a handful of product decisions, and a composable
taking all of them is worse than the assembly it replaces — so the module ships parts and its README
ships recipes to copy. Flow modules never depend on one another. The full contract is
[flow/FLOWS.md](../../flow/FLOWS.md).

## Icons are opt-in

`:standalone:icons` ships over a thousand icons, and no library module depends on it. A module that
draws an icon vendors its own `internal` copy instead, so a consumer that wants two icons does not
inherit all of them. How the copies are kept in sync is in
[CONTRIBUTING.md](../../CONTRIBUTING.md#bundled-icons).

## One component model

Every foundation component follows the same model — a style and a size with `Default`, presets and
`default(...)`, nullable locals seeded by `ArecaThemeProvider`, and one parameter order — so that a
reader who has learned one component can use any of them. The model is described for users in
[customising.md](../customising.md); new components follow it.

## Documentation

The public API is described in three places that must agree, and `ci/check-docs-coverage` keeps
them complete:

- the module's README — for people;
- `docs/components.md` — the catalogue of foundation components;
- the Agent Skills in `skills/` — for coding agents in other repositories, self-contained because
  they ship as a plugin.
