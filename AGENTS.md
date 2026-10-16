# Repository Instructions

## Before changing code

- Read the root [README.md](README.md) for the repository layout, namespaces, verification and
  releasing. Its Conventions section lists the contract each part of the tree follows.
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

- The skills in `skills/`, `ui/foundation/AGENTS.md` and the module READMEs describe the same public
  API and behaviour. A change to a published module's public API or behaviour updates the affected
  files in `skills/`, `ui/foundation/AGENTS.md` and the module's README in the same change.
- `.claude-plugin/plugin.json` has its own `version`, separate from `VERSION`. Every library release
  raises it (Prepare Release); a skills-only release raises it alone (Prepare Skills Release). Only
  those two workflows change it, and installs update only when it changes.
- Edit skills in `skills/` only; `.agents/skills/` holds symlinks to them.
