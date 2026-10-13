# Areca showcase authoring guide

> **Audience:** Anyone adding or changing a page in `:playground`

The playground is the reference surface for Areca. It is where a component is designed, reviewed,
visually QA'd and demonstrated. A showcase is a product, not a scratch file: it is read far more
often than it is written.

This document is the contract. Every showcase follows it. Deviations are argued in review, not
invented per file.

---

## 1. Principles

1. **One component, one page.** A page covers exactly one public component (or one tightly coupled
   family, such as `TextField` + `AffixTextField`). No page mixes unrelated components.
   The exception is a **foundations page**, which documents a token set rather than a component —
   see §12. There is one per token family, and it follows §12 instead of §3.
2. **Every page has the same skeleton.** A reader who has seen one page can navigate any page.
   Section order is fixed (§3).
3. **Props are discoverable by manipulation, not by reading source.** Anything a consumer can pass
   must be reachable from a control or visible in a variant.
4. **The chrome is built from tokens, never from Areca components.** The shell must survive a broken
   component, because the shell is what you use to look at the broken component (§5).
5. **No local helpers.** If a page needs a section header, card, demo avatar or muted label, it comes
   from the showcase toolkit. A `private fun ShowcaseSection` inside a page file is a defect.
6. **Demo content is realistic and boring.** Real labels ("Archive workspace"), real lengths, no
   `Lorem ipsum`, no `foo`/`bar`.

---

## 2. File and naming conventions

| Concern | Rule |
| --- | --- |
| Location | `playground/src/commonMain/kotlin/dev/voir/areca/playground/<module>/` where `<module>` mirrors the owning Gradle module: `foundation`, `charts`, `crop`, `datetime`, `keyboard`, `flow/auth`, `flow/profile`, `flow/attachments`. Within `foundation`, a page sits in the subpackage named after its component's package — `foundation/button/ButtonShowcase.kt` for `dev.voir.areca.foundation.button.Button`, `foundation/theme/ColorsShowcase.kt` for `dev.voir.areca.theme` |
| File name | `<Component>Showcase.kt` — always singular, always this suffix |
| Entry point | `@Composable fun <Component>Showcase()` — no `Page`, no `Screen`, no `Example`, no `Gallery` suffix |
| Parameters | The entry point takes no parameters. State belongs inside the page |
| Visibility | Entry point is `public`. Everything else in the file is `private` |
| Registration | One `ShowcaseEntry` in the registry (§8). The sidebar, search and deep links derive from it |

---

## 3. Page anatomy

A page is a sequence of sections in this order. Sections marked *required* appear on every page;
the rest appear when the component has something to say there. Never reorder, never invent a new
top-level section type without adding it to this document first.

| # | Section | Required | Purpose |
| --- | --- | --- | --- |
| 1 | **Header** | yes | Title, one-sentence summary, module badge, status badge. Emitted by the scaffold — you only pass the strings |
| 2 | **Playground** | yes | One instance, every meaningful prop wired to a control. The section a designer or consumer lands on |
| 3 | **Anatomy** | when the component has slots | Named callouts for leading / content / trailing / overlay slots, so slot semantics are visible |
| 4 | **Variants** | yes when the component has a style axis | The style × color (or style × tone) matrix as a scannable grid |
| 5 | **Sizes** | when the component has a size token | The full size ramp side by side, with the concrete dp in the subtitle |
| 6 | **States** | yes | default, hover/pressed where applicable, focused, selected, disabled, loading, error, empty |
| 7 | **Recipes** | when the component is usually composed | Two or three realistic compositions: a form footer, a settings row, a list header |
| 8 | **Edge cases** | yes | Long text, no text, overflow, very small container, very large container, missing optional slots |
| 9 | **Guidance** | encouraged | Short do / don't pairs, rendered side by side |

Do **not** add a section that merely restates another. "Colors" is not a section; it is an axis of
**Variants**.

---

## 4. The authoring DSL

A page is a `@Composable` function that calls `ShowcaseScaffold` and describes sections inside it.
State stays local to the section that owns it, so a keystroke in a text control recomposes one
preview, not the page.

```kotlin
@Composable
fun ButtonShowcase() = ShowcaseScaffold(
    title = "Button",
    summary = "Primary action control with variant, color, size and loading state.",
) {
    Playground(description = "Adjust the props a consumer passes most often.") {
        val label by textControl("label", default = "Continue")
        val variant by enumControl("variant", default = ButtonVariantOption.Solid)
        val color by enumControl("color", default = ButtonColor.Brand)
        val size by enumControl("size", default = ButtonSizeOption.Medium)
        val enabled by booleanControl("enabled", default = true)
        val loading by booleanControl("loading", default = false)
        val fill by booleanControl("fill", default = false, description = "Expand to available width.")

        preview {
            Button(
                style = variant.toStyle(color),
                size = size.toSize(),
                enabled = enabled,
                loading = loading,
                modifier = Modifier.conditional(fill) { fillMaxWidth() },
                onClick = {},
            ) {
                Text(label)
            }
        }
    }

    Variants(
        title = "Style × color",
        description = "The combinations a product surface picks from.",
        layout = VariantLayout.Grid,
    ) {
        for (variant in ButtonVariantOption.entries) {
            for (color in ButtonColor.entries) {
                variant(name = "${variant.name} / ${color.name}") {
                    Button(style = variant.toStyle(color), onClick = {}) { Text("Action") }
                }
            }
        }
    }

    Sizes(layout = VariantLayout.Horizontal) {
        size(name = "Medium", detail = "48dp min height") {
            Button(size = ButtonSize.Medium, onClick = {}) { Text("Continue") }
        }
    }

    States {
        state("Default") { Button(onClick = {}) { Text("Continue") } }
        state("Disabled") { Button(enabled = false, onClick = {}) { Text("Continue") } }
        state("Loading") { Button(loading = true, onClick = {}) { Text("Continue") } }
    }

    EdgeCases {
        case("Very long label", "Truncates at one line rather than wrapping.") { /* … */ }
        case("Narrow container", "Constrained to 120dp.") { /* … */ }
    }

    Guidance {
        good("Use one solid brand button per view as the primary action.") { /* … */ }
        bad("Do not stack two solid buttons — the hierarchy disappears.") { /* … */ }
    }
}
```

### Scaffold

```kotlin
@Composable
fun ShowcaseScaffold(
    title: String,
    summary: String,
    modifier: Modifier = Modifier,
    content: @Composable ShowcaseScope.() -> Unit,
)
```

The scaffold owns scrolling, page padding, the header, and the section rhythm. A page never creates
its own `LazyColumn`, `verticalScroll`, or outer padding.

### Section builders on `ShowcaseScope`

| Builder | Shape |
| --- | --- |
| `Playground(title, description) { … }` | Control panel + one live preview |
| `Anatomy(title, description) { slot(name, description) { … } }` | Annotated slot map |
| `Variants(title, description, layout) { variant(name, subtitle) { … } }` | Scannable matrix |
| `Sizes(layout) { size(name, detail) { … } }` | Size ramp, `detail` carries the dp |
| `States { state(name, note) { … } }` | State list, one per row |
| `Recipes { recipe(name, description) { … } }` | Realistic compositions |
| `EdgeCases { case(name, expectation) { … } }` | `expectation` states what *should* happen |
| `Guidance { good(text) { … }; bad(text) { … } }` | Side-by-side do / don't |
| `Notes { … }` | Free prose for anything the above cannot express. Use sparingly |

Foundations pages (§12) use a different set, and never mix the two:

| Builder | Shape |
| --- | --- |
| `Tokens(title, description) { token(name, color, description) }` | Grid of named swatches |
| `Ramps(title, description) { ramp(name, description, steps) }` | Raw palette ramps, lightest first |
| `Foregrounds(title, description) { foreground(name, color, grounds, floor, description) }` | One foreground sampled on each ground, with the measured contrast |

`VariantLayout` is `Grid`, `Horizontal` or `Vertical`. Pick `Grid` for a matrix, `Horizontal` for an
ordered ramp, `Vertical` when each item needs full width.

---

## 5. The chrome contract

Everything under `dev.voir.areca.playground.showcase` is **chrome**. Chrome is built from:

- `ArecaTheme.colors`, `ArecaTheme.space`, `ArecaTheme.radius`, `ArecaTheme.typography`
- raw Compose foundation: `Box`, `Column`, `Row`, `Surface`, `BasicText`, `BasicTextField`,
  `clickable`, `border`, `background`

Chrome must **not** import `dev.voir.areca.foundation.*` or anything from
`androidx.compose.material3`.

`verifyShowcaseConventions` (see `showcase-conventions.gradle.kts`) enforces this as part of
`check`, so `ci/verify` fails on a violation. It treats `dev.voir.areca.playground.shell` as chrome
too, and also rejects imports from the other component modules: charts, crop, datetime, keyboard,
icons and the flows.

The reason is practical: when `Card` regresses, you open the Card showcase to see it. If the section
cards *are* `Card`, the page you need is the page that is broken.

Areca components appear only inside preview content — that is, inside the lambdas a page author
writes. That is where the dogfooding belongs.

`:playground` has no Material3 dependency, and does not take one.

---

## 6. Controls

Controls are declared inside `Playground`. Each returns a delegate, so the value reads as a plain
local. Every control takes a stable `key` (used for state identity), a
`default`, and an optional one-line `description`.

| Control | Signature | Renders as |
| --- | --- | --- |
| `booleanControl` | `(key, default, description?)` | Switch |
| `enumControl` | `(key, default, entries?, label?)` | Segmented picker, or dropdown past 5 options |
| `textControl` | `(key, default, placeholder?, description?)` | Single-line input |
| `numberControl` | `(key, default, range, step, description?)` | Stepper + value |
| `dpControl` | `(key, default, range, description?)` | Slider with dp readout |
| `colorControl` | `(key, default, palette?, description?)` | Swatch row from theme colors |
| `optionalControl` | `(key, default, description?) { inner }` | Null toggle wrapping another control |
| `selectionControl` | `(key, default, options, label?)` | Multi-select chips |
| `actionControl` | `(key, label, onInvoke)` | Button that fires an event into the preview |

Rules:

- Do not add a control for a prop a consumer would never change (`modifier`, internal callbacks).
- Do not add a control that duplicates a variant axis *and* leave the variant section out — the
  Playground answers "what does this prop do", Variants answers "what do they look like together".
- `actionControl` exists for imperative surfaces (show a toast, open a modal, scroll to item).
- Every playground gets a **Reset** affordance from the toolkit. Do not build your own.

---

## 7. Preview canvas

`preview { … }` and every variant / state / case body render inside a canvas the toolkit owns. The
toolbar lets the reader switch:

- **Theme** — light or dark, applied to the preview only, so both can be compared without leaving
  the page.
- **Canvas** — `Surface`, `Canvas`, `Subtle`, `Contrast`, or `Checkerboard` for checking
  transparency and elevation.

Page authors do not set a background, do not wrap previews in their own `Surface`, and do not apply
their own padding. Pass content; the canvas handles presentation.

---

## 8. Registration

Each page contributes one registry entry, in the `registry/*Showcases.kt` file for its group. The
sidebar, the search box and deep links all read from the registry; nothing is hand-maintained in
more than one place.

```kotlin
ShowcaseEntry(
    id = "foundation/button",
    group = ShowcaseGroup.Components,
    section = ShowcaseSection.Actions,
    title = "Button",
    summary = "Primary action control with variant, color, size and loading state.",
    status = ShowcaseStatus.Stable,
    keywords = listOf("cta", "action", "submit"),
    content = { ButtonShowcase() },
)
```

- `id` is the route and the deep link, as `module/component`: the owning module (`foundation`,
  `icons`, `charts`, `crop`, `datetime`, `keyboard`, `flow-auth`, `flow-profile`, `flow-attachments`) and the component in
  kebab case. It never encodes the group, so a page's group or section can change without its id
  changing.
- `group` is the collapsible top-level heading. There is one for token and asset pages
  (**Foundations**) and one per owning module after that: **Components** (`:ui:foundation`),
  **Charts**, **Date & time**, **Keyboard**, **Crop**, **Auth flow**, **Profile flow**,
  **Attachments flow**.
- `section` divides a large group by what its pages do — **Components** into Actions, Inputs,
  Selection, Display, Lists & data, Feedback, Overlays, Navigation and Layout; each flow into
  Screens and the module's own part packages. A section belongs to exactly one group, and a group
  sections every page or none; small groups are listed flat.
- **Order is derived, never hand-kept.** Groups and sections appear in enum order, and pages sort
  alphabetically by title within their section. Source order in the registry files is for reading
  only.
- `title` is the component name, as a reader would search for it. Inside a flow group it drops the
  flow's own name — `Header`, not `Auth header` — because the group heading already says it; put the
  full name in `keywords` so a search for it still lands.
- `status` is `Stable`, `Beta` or `Deprecated` — the maturity of the **component's API**, rendered as
  a badge in the page header.
- `keywords` feed search — add the words a person would type who does not know the component name.
  Search matches title, group, section and keywords, but not `summary`: summaries are prose, and
  matching them turns a specific search into a long list.

`SidebarTreeTest` enforces the rules that the types cannot: unique ids with a module prefix, unique
titles within a section, no empty or half-sectioned group, and no flow title repeating its flow.

---

## 9. Writing the copy

- **Summary** — one sentence, says what the component is for, not what it is made of.
  Good: "Primary action control with variant, color, size and loading state."
  Bad: "Showcase for Button."
- **Section description** — one sentence, says what the reader should look for here.
  Bad: "Variants." Bad: "This section shows variants."
- **Variant name** — the prop values, exactly: `Solid / Brand`, not `Primary button`.
- **Edge-case expectation** — state the intended behaviour so a regression is obvious:
  "Truncates at one line rather than wrapping."

---

## 10. Checklist

Before opening a PR that adds or changes a showcase:

- [ ] File is `<Component>Showcase.kt`, entry point is `<Component>Showcase()`, no parameters
- [ ] No `private fun` section helpers — everything comes from the toolkit
- [ ] No `material3` import anywhere in the file
- [ ] No `LazyColumn`, `verticalScroll` or page padding at the top level
- [ ] Header summary is one sentence and is not "Showcase for X"
- [ ] Every public prop is a control, a variant axis, or deliberately excluded
- [ ] Playground resets cleanly and every control is labelled
- [ ] States section includes disabled and, where applicable, loading and error
- [ ] Edge cases include long text and a constrained container
- [ ] Renders correctly in both light and dark, on every canvas background
- [ ] Registered exactly once, with `status` and `keywords`
- [ ] Demo content is realistic — no `Lorem ipsum`, no `foo`

---

## 11. Anti-patterns

Each of these is a defect, and a review rejects it.

| Anti-pattern | Why it hurts |
| --- | --- |
| A local `ShowcaseSection` / `ShowcaseCard` / `DemoRow` per file | Near-duplicate helpers; every page drifts visually |
| Material3 `Card`, `Text`, `MaterialTheme.colorScheme` in a page | Two visual languages; previews sit on non-Areca surfaces |
| Page owns its own scroll container and padding | Page rhythm differs file to file; nested-scroll bugs in layout showcases |
| Building the whole page model inside a `@Composable` that reads control state | One keystroke rebuilds every section |
| A wall of instances with no controls | Reader cannot answer "what happens if I turn this off" |
| Section title with no description | Reader does not know what to look at |
| `Text("←")` standing in for an icon | The real icon has different metrics; the preview lies |
| Route, nav entry and `NavHost` edited in three separate files | Pages get added to two of the three and silently go missing |

---

## 12. Foundations pages

A token set is not a component. It has no props to wire to controls, no size ramp, no states and no
edge cases, so §3's anatomy does not apply to it — and the component sections actively work against
it, because they give every cell its own canvas, which is right for three buttons and unreadable for
sixty swatches.

A foundations page documents one token family, such as `Colors`.

### Anatomy

| # | Section | Required | Purpose |
| --- | --- | --- | --- |
| 1 | **Header** | yes | Same as §3. The module badge is the module the tokens live in |
| 2 | **Raw material** | when the family has one | The unthemed ramps or scale the semantic tokens are derived from |
| 3 | **Semantic groups** | yes | One section per group, in the order the token type declares them |

### Rules

1. **Name the token exactly.** The label under a swatch is what the reader copies into their code —
   `bgBrandMuted`, not "Brand background". A page that renames tokens is worse than no page.
2. **Carry the meaning.** Every token gets the one-line description its KDoc opens with. A gallery
   of anonymous colour chips answers "what exists" and not "which one do I reach for", and the
   second question is the one people actually have.
3. **One canvas state for the whole page.** Pass a single `rememberPreviewCanvasState()` to every
   section so the theme switch flips all of them at once. A token set is only correct if it is
   correct in both directions, and comparing them section by section hides exactly the token that
   stopped moving when the theme did.
4. **Show contrast where contrast is the contract.** A foreground token that documents a ratio
   shows that ratio measured, on each ground it is allowed to land on. The contrast test is the
   enforcement; the page is how someone sees *why* a token sits where it does on the ramp.
5. **Order by the token type, not alphabetically.** The declaration order of `ArecaColors` is a
   designed order — surfaces, then semantic families — and reading the page in that order is what
   makes the structure learnable.

## 13. Flow pages

A screen is not a component, and §3's anatomy does not fit one. A component page is a sequence of
sections because a component has axes — styles, sizes, states — that are only legible side by side.
A screen has none of that. It has one appearance at a time, and the only question worth answering
is what a given configuration looks like, at full size, right now. Ten thumbnails of a sign-in form
teach a reader less than one they can change.

So a flow page is **one screen and the controls that drive it**, and nothing else. It uses
`FlowShowcase`, not `ShowcaseScaffold`.

```kotlin
@Composable
fun SignInShowcase() = FlowShowcase(
    title = "Sign in",
    summary = "A sign-in screen assembled from AuthScaffold, the credential fields and SsoGroup.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
    notes = { /* what the recipe is, what to look at */ },
) {
    val providers by enumControl("providers", AuthProviderSet.Google, AuthProviderSet.entries)
    val form by booleanControl("form", default = true)

    preview {
        DeviceFrame(size = DeviceSize.Regular) {
            SignInRecipe(providers = providers.providers, showForm = form, …)
        }
    }
}
```

Pages live in `playground/flow/<module>/` and browse under their flow's group (**Auth flow**,
**Profile flow**, **Attachments flow**), in its **Screens** section.

A flow module's public parts — a field, a card, a row, a provider button — are components, not
screens. Each gets an ordinary component page (`ShowcaseScaffold`, §3) in
`playground/flow/<module>/`, listed in the same group under the section for the part's package
(Fields, Rows, Sheets, …). The rules below are for
screen pages only; a part page that shows a whole scaffold draws it in `DeviceFrame` at a scale it
picks for the cell.

### Rules

1. **No `Variants`, `Sizes`, `States`, `Recipes` or `EdgeCases`.** Those builders do not apply to a
   screen and must not be used on a flow page. A state worth seeing is a control, not a cell.
2. **The page renders a recipe, and the recipe is the point.** The composable a flow page draws is
   the assembly a consumer is meant to copy, written the way they would write it. It is `private`
   to the page, and it is duplicated in the module's README — deliberately, because a recipe that
   is not compiled anywhere rots.
3. **Every screen renders inside `DeviceFrame`.** A screen fills the viewport it is given, and a
   showcase page's viewport is a desktop window — a shape no phone has.
4. **Pass the device's insets.** `DeviceSize.windowInsets` exists because the real
   `WindowInsets.systemBars` on the desktop playground is zero, which hides exactly the padding
   mistakes the preview is for.
5. **Do not choose a scale.** `DeviceFrame` reads `LocalDeviceScale`, which `FlowShowcase` sets
   from the height the window actually has. A page that picked its own would be wrong on every
   window but the author's.
6. **The preview is live.** Wire the fields to real state, and have the page's sample application
   check them the way a product would — flow modules ship no validation, so the checks and their
   wording belong to the page, never to the module.
7. **Demo content is shared, not per-page.** The wordmark, the artwork and the sample legal links
   live in one file for the whole group, so the pages document one product rather than three.
