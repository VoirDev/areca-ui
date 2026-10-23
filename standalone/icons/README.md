# Areca Icons

1,062 vector icons for Compose Multiplatform (Android, iOS and the JVM): 1,059 converted from Lucide
and Tabler Icons, plus three sign-in marks. Each is an `ImageVector` on `ArecaIcons`, usable with or
without the Areca theme.

## Install

```kotlin
implementation("dev.voir.areca:icons:<version>")
```

`icons` depends only on `org.jetbrains.compose.ui:ui` (as `api`) and needs no Areca theme. No other
Areca artifact depends on it, so add it yourself to use the set. For the repository and credentials,
see [Getting started](../../docs/getting-started.md).

## Usage

Every icon is an extension property on `ArecaIcons` in `dev.voir.areca.icons`; import each property
alongside the receiver:

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Check
import dev.voir.areca.icons.SocialGoogle

@Composable
fun Marks() {
    Icon(ArecaIcons.Check, contentDescription = null) // tinted with LocalContentColor
    Icon(ArecaIcons.SocialGoogle, contentDescription = "Google", tint = Color.Unspecified) // keeps brand colours
}
```

- **With Areca:** foundation's `Icon(imageVector, contentDescription, tint = LocalContentColor.current,
  size = LocalIconSize.current)` from `dev.voir.areca.foundation.icon`, or any component taking an
  `ImageVector`, such as `IconButton`.
- **Without Areca:** `androidx.compose.material3.Icon(ArecaIcons.Check, contentDescription = null)`,
  or `Image(rememberVectorPainter(ArecaIcons.Check), null, colorFilter = ColorFilter.tint(color))`.
- Pass a localised `contentDescription` for an icon that carries meaning and `null` for a decorative
  one. See [Accessibility](../../docs/accessibility.md).

## Sizing and tinting

Every icon is 24 dp on a 24-unit grid, drawn with a 2-unit stroke and round caps and joins. Size it
with foundation's `Icon(size = …)` or `Modifier.size(…)`; the stroke scales with the icon.

The icons are drawn in black, so any tint replaces the colour. The full-colour sign-in marks
`SocialGoogle` and `SocialFacebook` need `tint = Color.Unspecified`, or the logo flattens to one
colour. `SocialApple` is black and is meant to be tinted black or white.

## Right-to-left layouts

Directional icons set `ImageVector.autoMirror` and flip in a right-to-left layout: the horizontal and
diagonal arrows (`Arrow*Left`, `Arrow*Right`, `ArrowsLeft`, `ArrowsRight`, `ArrowBlockLeft`/`Right`,
and their circle, square, narrow and broken-circle variants), `ChevronLeft`, `ChevronRight` and their
`Double` variants, every `Corner*`, `FlipBackward`/`FlipForward`, `ReverseLeft`/`ReverseRight`,
`LeftIndent`/`RightIndent`, `LogIn`/`LogOut`, `Send` and `Share`.

Media transport (`Play`, `FastForward`, `SkipBack`), clocks, refresh and rotate arrows, text
alignment (`AlignLeft`, `AlignRight`), two-way arrows (`ArrowLeftRight`, `SwitchHorizontal`,
`ChevronSelectorHorizontal`) and toggles do not mirror. The `mirror` flag in [`icons.tsv`](icons.tsv)
is the full list.

## Catalogue and naming

The playground's **Foundations → Icons** page shows the whole set, grouped and filterable by name;
[`icons.tsv`](icons.tsv) lists every name with its upstream drawing.

Names are `UpperCamelCase` and describe the drawing. Suffixes mark variants:

| Suffix | Means | Example |
| --- | --- | --- |
| `Alt` | A second drawing of the same idea | `TrashAlt` |
| `Circle`, `Square` | A shape around the glyph | `CheckCircle` |
| `Off` | The crossed-out state | `BellOff` |
| `Fill` | A filled outline | `HeartFill` |
| A plain number | Part of the upstream id | `Dice1`, `Gamepad2` |
| A zero-padded number | A variant index | `Flag01`, `Menu01` |

Some names share one drawing — `X`, `XClose` and `Cross`, for example. Each alias's KDoc names the
primary icon; prefer the primary name in new code.

## Sources and licences

Every icon except the three sign-in marks, 1,059 in all, is converted from one of two open-source
sets:

- [Lucide](https://lucide.dev) `lucide-static` 1.55.0, ISC licence. Some of those Lucide icons derive
  from [Feather](https://feathericons.com), MIT licence.
- [Tabler Icons](https://tabler.io/icons) `@tabler/icons` 3.49.0, outline style, MIT licence.

Each icon's KDoc names its upstream icon and licence ("Converted from Lucide `x` (ISC)"). The full
notices are in [`NOTICE.md`](NOTICE.md), which also ships inside every artifact (the jar, the AAR and
the sources jars) as `META-INF/dev.voir.areca/icons/NOTICE.md`, and the POM lists the ISC and MIT
licences beside Apache-2.0. An application that ships these icons must include the notices too, for
example on its open-source licences screen; Kotlin/Native klibs and frameworks cannot carry the
resource, so an iOS application has to add the notice itself.

## Trademarks

`SocialApple`, `SocialFacebook` and `SocialGoogle` are the providers' own sign-in marks, maintained by
hand and not covered by the icon licences. Apple, Facebook and Google are trademarks of Apple Inc.,
Meta Platforms, Inc. and Google LLC; use the marks only as each provider's sign-in branding
guidelines require.

The other brand icons (`BrandApple`, `BrandAndroid`, `Codepen`, `Figma`, `Framer`, `GoogleChrome`,
`Youtube`) are Tabler `brand-*` drawings. They depict trademarks of their owners; the icon licences
cover the drawings, not the marks, and their presence implies no endorsement.

## Binary size

Each icon is its own class, so R8 or ProGuard on Android and a Kotlin/Native framework drop the
icons an application never references; an unshrunk JVM desktop build ships all of them.

## Where to look

- **Playground**: **Foundations → Icons**.
- **KDoc** on each property: its upstream icon, licence and, for an alias, the primary name.
- **Related**: foundation's `Icon` and `IconButton` ([foundation README](../../ui/foundation/README.md)).
- **Skill**: [`skills/areca-ui-v1/reference/icons.md`](../../skills/areca-ui-v1/reference/icons.md).

## For contributors

Adding or regenerating icons (`icons.tsv`, `generateIcons`) and the bundled copies other modules
carry (`syncBundledIcons`) are covered in [CONTRIBUTING.md](../../CONTRIBUTING.md#icons).
