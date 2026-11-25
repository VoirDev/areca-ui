# Changelog

All notable changes to the Areca libraries. The format follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and the libraries use
[semantic versioning](https://semver.org/).

## Unreleased

### Added

- `foundation`: `AdaptiveNavigation(navigationVisible = false)` hides the rail, sidebar, edge column
  or bottom bar and gives the content the whole window. The navigation slides away and back on the
  theme's motion tokens, and the content keeps its place in the composition, so a navigation host
  inside it keeps its state.
- `foundation`: `AdaptiveNavigation(compactNavigation = CompactNavigation.None)` draws no navigation
  on a compact width or a compact height (a phone on its side), for an app whose phone screens reach
  each other themselves. `CompactNavigation.Bar`, the default, keeps the `TabBar` or `bottomBar` and
  the label-less rail.
- `foundation`: `AdaptiveNavigationLayout.None`, what the content of `AdaptiveNavigation` reads when
  no navigation is drawn.
- `keyboard`: `KeyboardKeys(maxKeyHeight = …)` fills the height the keys are given, from
  `KeyboardSize.keyHeight` up to `maxKeyHeight`, and places the grid at the bottom of a taller space.
  The default, `Dp.Unspecified`, keeps every key at `KeyboardSize.keyHeight`.

### Fixed

- `foundation`: `AdaptiveNavigation` no longer recreates its content when the form changes — a
  resize, a turned tablet, an unfolded foldable, the sidebar toggled, the navigation hidden. The
  content is moved, so `remember` and `rememberSaveable` state inside it, a navigation host's
  included, survives.

### Changed

- `foundation`: an exhaustive `when` over `AdaptiveNavigationLayout` needs a `None` branch.
- `foundation`, `keyboard`: `AdaptiveNavigation` and `KeyboardKeys` take new trailing parameters
  with defaults. Calls compile unchanged; code compiled against an earlier version must be rebuilt.
