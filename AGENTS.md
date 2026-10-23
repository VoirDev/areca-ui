# Repository Instructions

## Before changing code

- Read [CONTRIBUTING.md](CONTRIBUTING.md) for building, verification, API dumps, docs coverage and
  the contract each part of the tree follows, and
  [docs/maintainers/architecture.md](docs/maintainers/architecture.md) for the repository layout and
  namespaces. Releasing is [docs/maintainers/releasing.md](docs/maintainers/releasing.md).
- Run `./ci/verify` before proposing a change; CI runs exactly it.
- A change to a published module's public API rewrites its dumps with `./gradlew updateLegacyAbi`
  (on macOS) and commits them with the change.

## Areca skills

The Agent Skills in `skills/` ship as the `areca` Claude Code plugin (`.claude-plugin/plugin.json`).
Load them as `areca:areca-ui-v1` and `areca:areca-viewmodel-v1`, or read `skills/<name>/SKILL.md`.

| Skill | Applies to |
| --- | --- |
| `areca-ui-v1` | Playground showcase and screen pages under `playground/`, which build UI from the published modules as an app does; apply it with `playground/SHOWCASES.md`. |
| `areca-viewmodel-v1` | Code that consumes `dev.voir.areca:viewmodel`. Nothing in this repository does; the skill describes how apps use `standalone/viewmodel`. |

## Keeping the Areca skills current

- A published module's public API and behaviour are described in two places: the skills in
  `skills/` (the agent-facing source of truth, self-contained because they ship to other
  repositories) and the module's README — plus, for foundation, the catalogue
  `docs/components.md`. A change to a published module's public API or behaviour updates the
  affected files in `skills/`, the module's README and, for a foundation component,
  `docs/components.md` in the same change.
- `ui/foundation/AGENTS.md` is only a pointer to the `areca-ui-v1` skill; never add API detail to
  it.
- `./ci/check-docs-coverage`, run by `./ci/verify`, fails when a public composable is not named in
  its module's README, in `skills/areca-ui-v1/` or, for foundation, in `docs/components.md`.
- `.claude-plugin/plugin.json` has its own `version`, separate from `VERSION`. Every library release
  raises it (Prepare Release); a skills-only release raises it alone (Prepare Skills Release). Only
  those two workflows change it, and installs update only when it changes.
- Edit skills in `skills/` only; `.agents/skills/` holds symlinks to them.
