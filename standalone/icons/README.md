# Areca Icons

`dev.voir.areca:icons` is a set of 1,062 vector icons for Compose Multiplatform (Android, iOS and the JVM):
1,059 converted from Lucide and Tabler Icons, plus three sign-in marks. It needs no Areca theme and
depends only on `org.jetbrains.compose.ui:ui` (as `api`). No other Areca artifact depends on it, so an
application that wants the set adds it itself:

```kotlin
dependencies {
    implementation("dev.voir.areca:icons:<version>")
}
```

## Usage

Every icon is an extension property on `ArecaIcons` in `dev.voir.areca.icons`. Each property needs its own
import alongside the receiver:

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

- **With Areca:** `Icon(imageVector, contentDescription, tint = LocalContentColor.current, size =
  LocalIconSize.current)` from `dev.voir.areca.foundation.icon`.
- **Without Areca:** `androidx.compose.material3.Icon(ArecaIcons.Check, contentDescription = null)`, or
  `Image(rememberVectorPainter(ArecaIcons.Check), null, colorFilter = ColorFilter.tint(color))`.
- Pass a localised `contentDescription` for an icon that carries meaning and `null` for a decorative one.

Each vector is built on first access and reused for the life of the process.

## Sizing and tinting

Every icon is 24 dp on a 24-unit grid, drawn with a 2-unit stroke and round caps and joins. Size it with
foundation `Icon(size = …)` or `Modifier.size(…)`; the stroke scales with the icon.

The icons are drawn in black, so any tint replaces the colour. The exceptions are the full-colour sign-in
marks `SocialGoogle` and `SocialFacebook`: draw them with `tint = Color.Unspecified`, or the logo flattens to
one colour. `SocialApple` is black and is meant to be tinted black or white.

## Right-to-left layouts

Directional icons set `ImageVector.autoMirror`, so they flip horizontally in a right-to-left layout:
the horizontal and diagonal arrows (`Arrow*Left`, `Arrow*Right`, `ArrowsLeft`, `ArrowsRight`,
`ArrowBlockLeft`/`Right`, and the circle, square, narrow and broken-circle variants), `ChevronLeft`,
`ChevronRight` and their `Double` variants, every `Corner*`, `FlipBackward`/`FlipForward`,
`ReverseLeft`/`ReverseRight`, `LeftIndent`/`RightIndent`, `LogIn`/`LogOut`, `Send` and `Share`.

Media transport (`Play`, `FastForward`, `SkipBack`), clocks, refresh and rotate arrows, text alignment
(`AlignLeft`, `AlignRight`), two-way arrows (`ArrowLeftRight`, `SwitchHorizontal`,
`ChevronSelectorHorizontal`) and toggles do not mirror. The `mirror` flag in `icons.tsv` is the source of
truth.

## Catalogue and naming

Names are `UpperCamelCase` and describe the drawing. Variants use suffixes: `Alt` for a second drawing of the
same idea (often the other upstream set's), `Circle`/`Square` for a shape around the glyph, `Off` for the
crossed-out state, and `Fill` for a filled outline. A plain number repeats the upstream id's (`Dice1`,
`Gamepad2`); a zero-padded number (`Flag01`, `Menu01`, `ShoppingBag03`) is a variant index.

Some names deliberately share one drawing, for example `X`, `XClose` and `Cross`. They are aliases that
existing code uses; `icons.tsv` marks each alias `same-as=<Name>`, and its KDoc names the primary icon. Prefer
the primary name in new code.

The playground's Icons showcase renders the whole set; its list is generated from `icons.tsv` at build time.

## Sources and licences

Every icon except the three sign-in marks, 1,059 in all, is converted from one of two open-source sets:

- [Lucide](https://lucide.dev) `lucide-static` 1.55.0, ISC licence. Some of those Lucide icons derive from
  [Feather](https://feathericons.com), MIT licence.
- [Tabler Icons](https://tabler.io/icons) `@tabler/icons` 3.49.0, outline style, MIT licence.

Each icon's KDoc names its upstream icon and licence ("Converted from Lucide `x` (ISC)"). The full notices
are in [`NOTICE.md`](NOTICE.md), which also ships inside every artifact (the jar, the AAR and the sources
jars) as `META-INF/dev.voir.areca/icons/NOTICE.md`, and the POM lists the ISC and MIT licences beside
Apache-2.0. An application that ships these icons must include the notices too, for example on its
open-source licences screen; Kotlin/Native klibs and frameworks cannot carry the resource, so an iOS
application has to add the notice itself.

## Trademarks

`SocialApple`, `SocialFacebook` and `SocialGoogle` are the providers' own sign-in marks, maintained by hand
and not covered by the icon licences. Apple, Facebook and Google are trademarks of Apple Inc., Meta
Platforms, Inc. and Google LLC; use the marks only as each provider's sign-in branding guidelines require.

The other brand icons (`BrandApple`, `BrandAndroid`, `Codepen`, `Figma`, `Framer`, `GoogleChrome`,
`Youtube`) are Tabler `brand-*` drawings. They depict trademarks of their owners; the icon licences cover
the drawings, not the marks, and their presence implies no endorsement.

## Binary size

Each icon compiles to its own file class. R8 or ProGuard on Android and dead-code elimination in a
Kotlin/Native framework drop the icons an application never references; a JVM desktop build without
shrinking ships all of them.

## Regenerating the set

[`icons.tsv`](icons.tsv) maps every property to its upstream drawing: name, set (`lucide`, `tabler` or
`manual`), upstream id, and flags (`mirror`, `filled`, `same-as=<Name>`). The development-only
`generateIcons` task, defined in [`generate-icons.gradle.kts`](generate-icons.gradle.kts), reads the
mapping and the upstream SVGs and rewrites every generated file and `NOTICE.md`:

```bash
./gradlew :standalone:icons:generateIcons \
    -Plucide=<unpacked lucide-static 1.55.0 package> \
    -Ptabler=<unpacked @tabler/icons 3.49.0 package>
```

It parses `path`, `circle`, `ellipse`, `rect`, `line`, `polyline` and `polygon` elements into absolute
`ImageVector` builder calls, drops Tabler's invisible bounding box, and fails on anything off the 24-unit,
2-unit-stroke grid. It deletes any icon file the mapping no longer names, except `manual` ones and
`ArecaIcons.kt`. It is not part of `check` or CI; it refuses other upstream versions until the expected
versions in the script are changed.

To add an icon: add a row to `icons.tsv` (a `mirror` flag if it points sideways, `same-as` if it repeats
another drawing), run the task, update the playground showcase if useful, run
`./gradlew :standalone:icons:jvmTest`, and review the diff. The test builds every icon and checks its size,
viewport, name, mirroring, black 2-unit stroke and that repeated drawings are declared aliases.

## Copies in other modules

Library modules in this repository never depend on `:standalone:icons`. A module that draws icons declares
them before applying the bundling script:

```kotlin
extra["bundledIconsPackage"] = "dev.voir.areca.crop.icon"
extra["bundledIcons"] = listOf("Check", "XClose")
apply(from = rootProject.file("gradle/bundled-icons.gradle.kts"))
```

`./gradlew syncBundledIcons` (or `:<module>:syncBundledIcons`) writes committed `internal val
BundledIcons.<Name>` copies into that package, which the task owns, and a copy of `NOTICE.md` as
`src/commonMain/resources/META-INF/<package>/ICONS-NOTICE.md`, so the module's jar, AAR and sources jars
carry the notices its copies require. `verifyBundledIcons` runs as part of `check` and fails when a copy or
the notice has drifted. Edit icons only here, then re-sync.
