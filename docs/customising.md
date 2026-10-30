# Customising components

Every foundation component is configured the same way: a **style** for its colours and shape, a
**size** for its metrics, a default for each that comes from the theme, and a way to change that
default for a whole part of the screen. Learn it once here; the [component catalogue](components.md)
and the module READMEs only name each component's types.

To change the tokens every component reads — the brand colour, the corner radius, the font — change
the [theme](theme.md) instead.

## Style and size

A component's look is two values, each a data class:

- **`XStyle`** — colours, the shape, and the indication that draws press, hover and focus.
- **`XSize`** — paddings, gaps, type, minimum bounds and the touch target.

`Button` takes a `ButtonStyle` and a `ButtonSize`, `Card` a `CardStyle` and a `CardSize`, and so on.
Each type's companion holds the ready-made values:

| On the companion | What it is | Example |
| --- | --- | --- |
| `Default` | The theme's value, used when the component is given nothing | `CardSize.Default` |
| Named presets | A ladder of sizes or a set of looks | `ButtonSize.Small`, `BadgeStyle.Success`, `CardStyle.Outlined` |
| `default(...)` | `Default` with any field replaced; every parameter defaults to the theme | `ProgressBarStyle.default(fill = …)` |
| Style factories | Where the look is a choice | `ButtonStyle.soft(ActionColor.Danger)`, `FieldStyle.filled()` |

Pass them to the one component that should look different:

```kotlin
Button(
    "Delete",
    onClick = onDelete,
    style = ButtonStyle.soft(ActionColor.Danger),
    size = ButtonSize.Small,
)

ProgressBar(
    0.4f,
    style = ProgressBarStyle.default(fill = ArecaTheme.colors.fgSuccess),
    size = ProgressBarSize.Small,
)

Card(
    style = CardStyle.softNeutral(),
    size = CardSize.default(padding = PaddingValues(ArecaTheme.spacing.xl3)),
) { padding ->
    Text("Card body", modifier = Modifier.padding(padding))
}
```

`copy` changes one field of a style you already have:

```kotlin
Badge(style = BadgeStyle.Success.copy(content = ArecaTheme.colors.fgSuccessText)) {
    Text("Paid")
}

Button(
    "Save",
    onClick = onSave,
    style = ButtonStyle.solid(shape = ArecaTheme.shapes.full),
    size = ButtonSize.Large,
)
```

Most presets and every `default(...)` are `@Composable`: they read the theme where they are called,
so a style built inside `ArecaDarkTheme` is a dark one.

Never wrap a component only to restyle it. Pass a style, or provide one for the subtree.

## Defaults for a subtree

A component given no style or size reads one from a composition local. `Button`'s `style` defaults
to `LocalButtonStyle.current ?: ButtonStyle.solid()` and its `size` to
`LocalButtonSize.current ?: ButtonSize.Medium`; every component follows the same pattern with its
own `LocalXStyle` and `LocalXSize`, in the component's package.

`ArecaThemeProvider` seeds the locals. Provide your own to change a default for a subtree — a
toolbar of small outlined buttons, a dense form:

```kotlin
CompositionLocalProvider(
    LocalButtonSize provides ButtonSize.Small,
    LocalButtonStyle provides ButtonStyle.outlined(ActionColor.Neutral),
) {
    Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        Button("Archive", onClick = onArchive)
        Button("Move", onClick = onMove)
    }
}
```

- **For the whole application**, provide the locals just inside the application's
  `ArecaThemeProvider`.
- **A nested `ArecaThemeProvider` or `ArecaDarkTheme`** seeds every local again, so an override made
  above one does not reach inside it. Provide it again inside.
- **The fallback after `?:`** is what a component uses with no provider at all, as in a preview.

A few families resolve their defaults differently:

| Family | Default comes from | To change it |
| --- | --- | --- |
| Fields (`TextField`, `PasswordField`, `SearchField`, `SelectField`, …, and `FieldGroup`) | `FieldDefaults.style` and `FieldDefaults.size`, which read `LocalFieldStyle` and `LocalFieldSize` | `CompositionLocalProvider(LocalFieldStyle provides FieldStyle.filled())` for a whole form. The looks are `outlined()` (the default), `filled()`, `soft()`, `solidNeutral()`, `underlined()` and `inline()` |
| `Notice` | Its `tone` and `emphasis`, through `NoticeStyle.of(...)`. `LocalNoticeStyle` is `null` | Provide `LocalNoticeStyle` only where every notice should look the same: it replaces the tone's colours |
| `ConfirmSheet` | The flow modal's style and size in scope where it is drawn. `LocalConfirmSheetStyle` and `LocalConfirmSheetSize` are `null` | Change `LocalFlowModalStyle` and every sheet follows, or provide the confirm sheet locals |
| `Settings` and its rows | `styles = SettingsDefaults.styles(...)` and `sizes = SettingsDefaults.sizes(...)`, which the list hands to its rows | Pass them to `Settings` |
| `charts`, `datetime` | Their own locals (`LocalColumnChartStyle`, `LocalCalendarStyle`, …), which `ArecaThemeProvider` does not seed — it lives in `foundation` | Provide them around the screens that want other defaults; unprovided, each falls back to its type's `Default` |

```kotlin
CompositionLocalProvider(LocalFieldStyle provides FieldStyle.filled()) {
    SignUpForm()
}

Settings(sizes = SettingsDefaults.sizes(statusTextStyle = ArecaTheme.typography.bodyExtraSmall)) {
    // sections
}
```

### Content locals

Three locals carry content rather than a component's look: `LocalContentColor` and
`LocalTextStyle` (in `dev.voir.areca.foundation`) and `LocalIconSize` (in
`dev.voir.areca.foundation.icon`). A container sets them for its slots — a button its label colour,
a field its type, a list row each slot's role — which is why a plain `Text` or `Icon` inside a slot
comes out right without arguments. Pass slots plain content and let the container style it.

## What an action means: `ActionColor`

What an action *means* is an `ActionColor` — `Brand`, `Danger`, `Success`, `Warning`, `Neutral` or
`NeutralDark`. How *loud* it is, is the style factory. `Button`, `IconButton` and `FeaturedIcon`
resolve the same family the same way:

| Factory | Use it for |
| --- | --- |
| `solid` | The screen's one primary action. Two solid buttons on a screen means no primary action |
| `soft` | A secondary action that still reads as its family |
| `outlined` | A secondary action on a busy surface |
| `plain` | An action that must not pull the eye — Cancel, a toolbar control (`Button` and `IconButton` only) |

```kotlin
Button("Save", onClick = onSave)                                              // solid(Brand)
Button("Discard", onClick = onDiscard, style = ButtonStyle.plain(ActionColor.Neutral))
IconButton(
    ArecaIcons.Trash,
    contentDescription = "Delete draft",
    onClick = onDelete,
    style = IconButtonStyle.plain(ActionColor.Danger),
)
```

`Danger` is destructive whether it is a filled button or a plain icon. The theme's default button is
`solid(ActionColor.Brand)`. The factories also take a `shape`, which defaults to the theme's
`shapes.button`.

## Parameter order

Signatures share one order:

1. The required values — the value and its change callback, or the action and its label.
2. `modifier`, the first optional parameter (a few components place it later).
3. Everything else, with `style` and `size` among them.
4. The main content slot last, so it can be a trailing lambda.

Pass everything after the leading values by name, and the order never matters at a call site.

Callbacks are nullable where *no* callback means something. `Button(onClick = null)` draws a button
with no action, for one inside a row that is itself pressable. A `Checkbox` given a `null`
`onCheckedChange` draws a display-only box whose state the enclosing row reports.

State is hoisted: components take `value` and `onValueChange`, `visible` and `onDismiss`,
`selectedValue` and `onValueSelected`. The `rememberXState` helpers (`rememberTabsState`,
`rememberToastState`, `rememberFlowModal`, …) are for state that belongs to the screen and nothing
else.

## Interaction and focus

Every interactive component takes `interactionSource: MutableInteractionSource? = null`. `null`
creates one internally; pass your own to observe press, hover and focus, or to drive one
component's state from another:

```kotlin
val interactions = remember { MutableInteractionSource() }
val pressed by interactions.collectIsPressedAsState()

Button(
    if (pressed) "Release to send" else "Hold to send",
    onClick = onSend,
    interactionSource = interactions,
)
```

A style carries its indication, which draws hover, press and keyboard focus. The four are in
`dev.voir.areca.indication`:

| Indication | Feedback | Used by |
| --- | --- | --- |
| `TouchIndication` | A hover tint and a press that spreads from the finger | Solid and soft buttons, swatches — the default for anything tapped |
| `FadeIndication` | The background crossfades | Plain buttons, ghost cards |
| `StrokeIndication` | The outline draws itself and thickens on press | Outlined buttons and cards |
| `LiquidSweepIndication` | A fill sweeps across in reading order | Nothing by default; for a deliberate, commit-style action of your own |

`ButtonStyle` and `IconButtonStyle` keep shape and indication in step:
`style.copy(shape = ArecaTheme.shapes.full)` gives a pill that also reacts as a pill.

Every indication draws keyboard focus as a ring outside the element's bounds, following its shape.
So **never `Modifier.clip` an interactive element** — the clip crops the ring. Use
`Modifier.background(color, shape)` for a shaped fill and pass the same shape to the indication.

## Touch targets

The touch target is `ArecaTheme.sizing.touchTarget`: 48dp under touch and none under a pointer,
which needs no more than the control draws. A control that draws smaller — a 32dp icon button, a 20dp
checkbox — reserves the rest in layout around itself, so a neighbour never sits inside its band. The
sizes of `Button`, `IconButton`, `Badge`, `Checkbox`, `Radio`, `Toggle`, `Swatch` and `SwatchPicker`
carry this as `touchTarget`. It is reserved only for a control with an action: one given a `null`
callback is display-only and keeps its drawn size. `MinimumTouchTarget` (in
`dev.voir.areca.modifier`) is the 48dp constant, for a surface of your own that is always touched.

## Touch and pointer sizes

Every size preset is drawn for the input in scope. `ButtonSize.Medium` is 48dp tall on a phone and
36dp on a laptop; a field, an icon button and a list row beside it shrink by the same step, so they
still line up. The heights come from `ArecaTheme.sizing` (see [Theme](theme.md#sizing)); the
paddings that fit them are chosen per preset with `sizing.select(touch = …, pointer = …)`, which a
size of your own can use the same way:

```kotlin
val sizing = ArecaTheme.sizing
val chipSize = ButtonSize.Small.copy(
    padding = PaddingValues(horizontal = sizing.select(touch = 14.dp, pointer = 10.dp)),
)
```

## Your own pressable surface

When no component fits, build the surface from the same parts: a shaped background, an Areca
indication in the same shape, the minimum touch target, and the pointer cursor on the desktop.

```kotlin
@Composable
fun PlayButton(onPlay: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .minimumTouchTarget()
            .size(32.dp)
            .background(ArecaTheme.colors.bgMuted, ArecaTheme.shapes.full)
            .clickCursor()
            .clickable(
                interactionSource = null,
                indication = TouchIndication(shape = ArecaTheme.shapes.full),
                role = Role.Button,
                onClickLabel = "Play",
                onClick = onPlay,
            ),
    )
}
```

`Modifier.minimumTouchTarget()` goes outside the element's size and before `clickable`.
`Modifier.clickCursor()` (in `dev.voir.areca.modifier`) shows the hand cursor on the desktop.

Before building a row of your own, look at `SlotRow` in `dev.voir.areca.foundation.slot`: the start,
centre and end slots most rows, bars and buttons are built on, so yours lines up with theirs.
