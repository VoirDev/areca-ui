# Areca documentation

Everything about using Areca, by what you want to do. New here? Start with
[Getting started](getting-started.md).

## Learn

| Page | What it covers |
| --- | --- |
| [Getting started](getting-started.md) | Add the repository and credentials, set up the theme, build a first screen, add more modules |
| [Theme](theme.md) | Every token — colour, typography, spacing, radius, elevation, motion — changing the theme, always-dark regions |
| [Customising](customising.md) | How every component takes a style and a size, presets, app-wide defaults, `ActionColor`, focus and touch targets |
| [Accessibility](accessibility.md) | What components announce on their own and what you must supply |
| [Localisation](localisation.md) | Every built-in string, module by module, and how to replace it |

## Find a component

| Page | What it covers |
| --- | --- |
| [Component catalogue](components.md) | Every foundation component by family: what it is for, its package, its playground page |
| [Foundation](../ui/foundation/README.md) | Usage of every foundation component |
| [Charts](../ui/charts/README.md) | `dev.voir.areca:charts` — bar, stacked-bar, column and line charts, meter bar, legend |
| [Date and time](../ui/datetime/README.md) | `dev.voir.areca:datetime` — calendar, date range, month and year picker, time picker, date-time field |
| [Crop](../ui/crop/README.md) | `dev.voir.areca:crop` — the image cropper |
| [Keyboard](../ui/keyboard/README.md) | `dev.voir.areca:keyboard` — the on-screen keyboard |
| [Icons](../standalone/icons/README.md) | `dev.voir.areca:icons` — 1,062 icons |
| [ViewModel](../standalone/viewmodel/README.md) | `dev.voir.areca:viewmodel` — state, effects and paged lists |

## Build a screen

| Page | What it covers |
| --- | --- |
| [Recipes](recipes/README.md) | Settings, form with validation, list with refresh and empty states, confirmation, tabs, analytics overview |
| [Auth flow](../flow/auth/README.md) | `dev.voir.areca:flow-auth` — sign-in screens and their recipes |
| [Profile flow](../flow/profile/README.md) | `dev.voir.areca:flow-profile` — account screens and their recipes |
| [Attachments flow](../flow/attachments/README.md) | `dev.voir.areca:flow-attachments` — attaching and viewing files |

## Work with an AI coding agent

The `areca` Claude Code plugin teaches agents to build with Areca:

```text
/plugin marketplace add VoirDev/areca-ui
```

```text
/plugin install areca@areca-ui
```

| Skill | Covers |
| --- | --- |
| [`areca-ui-v1`](../skills/areca-ui-v1/SKILL.md) | Theme, foundation, charts, date and time, crop, keyboard, flows and icons: component choice, signatures, rules, recipes |
| [`areca-viewmodel-v1`](../skills/areca-viewmodel-v1/SKILL.md) | Presentation code on `dev.voir.areca:viewmodel` |

## Contribute

| Page | What it covers |
| --- | --- |
| [CONTRIBUTING.md](../CONTRIBUTING.md) | Building, verifying, API dumps, docs coverage, icons |
| [Architecture](maintainers/architecture.md) | How the repository is divided, and why |
| [Releasing](maintainers/releasing.md) | Library and skills releases |
| [Flow modules](../flow/FLOWS.md) | The contract every flow module follows |
| [Playground pages](../playground/SHOWCASES.md) | The contract every showcase page follows |
