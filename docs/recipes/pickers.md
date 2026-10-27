# Pickers

A filter panel built from sliding selectors: a segmented choice of level, a light "all or any"
switch drawn as words with a moving pill, and a three-state "has / avoid / either" toggle for each
note, where the thumb takes the colour of what was chosen.

Needs `dev.voir.areca:foundation`.

## Components

| Component | Role here |
| --- | --- |
| `SlidingSelector` with `SlidingSelectorStyle.Segmented` | The level, one of four, always set |
| `SlidingSelector` with `SlidingSelectorStyle.Pill` | "All of them" or "Any one", a choice inside a sentence |
| `SlidingSelector` with `SelectorMode.Optional` | Each note's has / avoid toggle, which can be left unset |
| `SelectorItem` | Each option, with its own thumb colour and spoken name where it needs one |
| `Text` with `TextVariant` | The labels |

Every component is in the [catalogue](../components.md).

## Code

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.selection.SelectorItem
import dev.voir.areca.foundation.selection.SelectorLayout
import dev.voir.areca.foundation.selection.SelectorMode
import dev.voir.areca.foundation.selection.SlidingSelector
import dev.voir.areca.foundation.selection.SlidingSelectorSize
import dev.voir.areca.foundation.selection.SlidingSelectorStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme

enum class Level(val label: String) { Any("Any level"), Top("Top"), Heart("Heart"), Base("Base") }

enum class Match(val label: String) { All("All of them"), AnyOne("Any one") }

enum class Preference { Has, Avoid }

data class NoteFilter(val note: String, val preference: Preference?)

@Composable
fun NoteFilters(
    level: Level,
    onLevelChange: (Level) -> Unit,
    match: Match,
    onMatchChange: (Match) -> Unit,
    notes: List<NoteFilter>,
    onPreferenceChange: (note: String, Preference?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl)) {
        SlidingSelector(
            items = Level.entries.map { SelectorItem(it) { Text(it.label) } },
            selected = level,
            onSelect = { if (it != null) onLevelChange(it) },
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        ) {
            Text("Match", variant = TextVariant.BodyMedium)
            SlidingSelector(
                items = Match.entries.map { SelectorItem(it) { Text(it.label) } },
                selected = match,
                onSelect = { if (it != null) onMatchChange(it) },
                style = SlidingSelectorStyle.Pill,
                size = SlidingSelectorSize.Small,
                layout = SelectorLayout.Hug,
            )
        }

        for (filter in notes) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(filter.note, variant = TextVariant.BodyMedium, modifier = Modifier.weight(1f))
                SlidingSelector(
                    items = listOf(
                        SelectorItem(
                            value = Preference.Has,
                            thumb = ArecaTheme.colors.fgBase,
                            contentDescription = "Has ${filter.note}",
                        ) { Text("Has") },
                        SelectorItem(
                            value = Preference.Avoid,
                            thumb = ArecaTheme.colors.bgDangerBase,
                            contentDescription = "Avoid ${filter.note}",
                        ) { Text("Avoid") },
                    ),
                    selected = filter.preference,
                    onSelect = { onPreferenceChange(filter.note, it) },
                    mode = SelectorMode.Optional,
                    style = SlidingSelectorStyle.Pill,
                    size = SlidingSelectorSize.Small,
                    layout = SelectorLayout.Hug,
                )
            }
        }
    }
}
```

- **Required or optional.** The level and the match always have a value: the default
  `SelectorMode.Required` never reports `null`, and tapping the chosen option does nothing. A note's
  preference may be unset: `SelectorMode.Optional` reports `null` when the chosen option is tapped
  again, and announces each option as a switch.
- **Motion.** Each selector opens with its thumb in place. The has / avoid thumb grows in on the
  first choice, slides and changes colour between the two, and shrinks away when cleared.
- **Names.** "Has" alone does not say what it applies to, so each option's `contentDescription`
  names the note.

## Variations

**Icons.** An option's content is any composable. Give an icon-only option a
`contentDescription`, and its `Icon` a `null` one:

```kotlin
SlidingSelector(
    items = listOf(
        SelectorItem(Theme.Light, contentDescription = "Light theme") { Icon(ArecaIcons.Sun, null) },
        SelectorItem(Theme.Dark, contentDescription = "Dark theme") { Icon(ArecaIcons.MoonAlt, null) },
    ),
    selected = theme,
    onSelect = { if (it != null) onThemeChange(it) },
    layout = SelectorLayout.Hug,
)
```

**Other looks.** `SlidingSelectorStyle.Underline` draws a divider with a bar sliding along it;
`Button` a flat fill with no track. `SlidingSelectorStyle.default(...)` changes any colour, the
thumb's shape and elevation, or the `motion` — a spring with a lower damping ratio settles with a
slight bounce.

**Many options.** `layout = SelectorLayout.Scroll` lets the options overflow and keeps the chosen
one in view; `orientation = SelectorOrientation.Vertical` stacks them.

**Views, not settings.** Switching between views of a screen is `Tabs`, built on the same thumb —
see the [tabbed screen](tabs.md).
