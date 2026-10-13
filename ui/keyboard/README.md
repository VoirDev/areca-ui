# Keyboard

A custom keyboard docked to the bottom of a screen, for Compose Multiplatform.

Three layers, each usable without the one above it:

| Layer | What it does | Key types |
| --- | --- | --- |
| **Host** | Docks a keyboard to the bottom edge, and coordinates focus and scrolling | `KeyboardHost`, `KeyboardController`, `KeyboardTarget` |
| **Chrome** | The styled surface the keyboard sits on, with header and footer slots | `KeyboardLayout`, `KeyboardStyle`, `KeyboardSize` |
| **Keys** | Rows of weighted keys, and the stock layouts | `Key`, `KeyRole`, `KeyStyle`, `KeyRow`, `KeyboardGrid`, `KeyboardGrids` |

The module carries no domain knowledge. It knows how to lay out a numeric pad; it does not know what
the numbers are for. Keys report intent as a `KeyAction`, and the application decides what that does
to its own state. The module never edits text itself.

**Requirements.** Everything here must sit under an `ArecaThemeProvider`: every default reads
`ArecaTheme`. The module depends on `dev.voir.areca:foundation` as an API dependency — keys are
foundation `Button`s, and `ButtonStyle` and `ButtonSize` appear in this module's API.

---

## Quick start

```kotlin
@Composable
fun AmountScreen() {
    var amount by remember { mutableStateOf("") }
    val keyboard = rememberKeyboardController()
    val target = rememberKeyboardTarget()

    KeyboardHost(
        keyboardContent = {
            KeyboardLayout(
                grid = KeyboardGrids.decimal(
                    onAction = { action ->
                        when (action) {
                            is KeyAction.Digit -> amount += action.value
                            KeyAction.DecimalSeparator -> if (',' !in amount) amount += ','
                            KeyAction.Backspace -> amount = amount.dropLast(1)
                            else -> Unit
                        }
                    },
                    decimalSeparator = ',',
                ),
                header = { Text(amount, textAlign = TextAlign.End) },
            )
        },
        controller = keyboard,
    ) {
        Text(
            text = amount.ifEmpty { "0" },
            modifier = Modifier.keyboardTarget(keyboard, target, onClickLabel = "Edit amount"),
        )
    }
}
```

---

## Host

`KeyboardHost` goes at the root of a screen. It puts a `KeyboardController` in scope for everything
below and draws the keyboard over the bottom edge while it is open.

```kotlin
KeyboardHost(
    keyboardContent = { target -> },
    controller = rememberKeyboardController(),
    compressContent = true,
    scrim = null,
    animation = KeyboardAnimation.Default,
    paneTitle = "Keyboard",
    onVisibilityChange = { open -> },
    content = { },
)
```

**`rememberKeyboardController()`** is saveable: an open keyboard stays open across an Android
rotation, drawn for a target with the same `key`. Give targets keys that can be saved — a string, a
number, an enum — or the keyboard comes back closed. Focus is not restored; pressing the target
again picks it back up.

**`compressContent`** decides whether the screen makes room. `true` pads the content by the
keyboard's height, keeping the whole screen reachable, and consumes that much of the bottom window
inset, so content padded by the navigation bar or safe area does not pad again above the keyboard.
`false` draws the keyboard *over* the content, which is what a screen with a docked action bar
wants — compressing would lift that bar onto the keyboard's shoulder.

**`scrim`** is `null` by default, because a keyboard is not modal: the screen normally stays
readable and scrollable while it is up. Pass `KeyboardScrim.Default` for a keyboard that genuinely
interrupts. A scrim takes every press that lands on it, so the content beneath is out of reach
while it shows; with `dismissOnTap = false` a tap simply goes nowhere. A scrim that closes the
keyboard on tap is read by assistive technology as its `dismissLabel` (`"Close keyboard"` by
default — pass the product's own), and is not a focus stop.

**`paneTitle`** is announced by assistive technology when the keyboard appears. English by default;
pass the product's own, or `null` for no announcement.

Two hosts may share one hoisted controller; focus follows the host that entered the composition
last, and a host leaving does not detach the one that replaced it.

### One host, several keyboards

`keyboardContent` receives the open target, so one host serves a whole screen. While the keyboard
slides out it still receives the target it was last opened for, so it leaves with its keys:

```kotlin
val amount = rememberKeyboardTarget(key = "amount")
val date = rememberKeyboardTarget(key = "date")

KeyboardHost(
    keyboardContent = { target ->
        when (target?.key) {
            "amount" -> KeyboardLayout(grid = KeyboardGrids.decimal(onAction = ::onAmountKey))
            "date" -> DateKeyboard()
        }
    },
) { }
```

### Targets

A `KeyboardTarget` is what to focus and what to scroll clear of the keyboard. One per input.

```kotlin
Modifier.keyboardTarget(
    controller = keyboard,
    target = target,
    enabled = true,
    scroll = true,      // scroll the target above the keyboard once it has opened
    stealFocus = true,  // move focus here, so physical keys reach it
    onClickLabel = "Edit amount",
)
```

It draws no press feedback by default — a keyboard target is usually a large custom display where a
ripple across the whole area reads as a mistake. Pass `indication` when you want one. The target is
focusable in touch mode too, so `stealFocus` lands focus on it on a phone or tablet as well as on
desktop. Assistive technology hears it as collapsed while the keyboard is closed for it and
expanded while it is open, with actions to open and close it.

Focus the keyboard moved is cleared again when it closes. A keyboard opened with
`stealFocus = false` leaves focus where it is on close.

**`scroll`** waits until the keyboard has finished opening, then scrolls the nearest scroll container
around the target. With `compressContent = true` the container has shrunk by then, and the target is
brought into what remains. With `compressContent = false` the keyboard covers the content, so the
target is brought into view together with a band the keyboard's height below it — it ends up at
least that far above the container's bottom edge. A target outside any scroll container does not
move.

### Back press

**Not handled by the host, deliberately.** An application already owns its back stack, so wire it
where that stack lives. With Compose Multiplatform's navigation events
(`org.jetbrains.androidx.navigationevent:navigationevent-compose`), for example:

```kotlin
NavigationBackHandler(
    state = rememberNavigationEventState(NavigationEventInfo.None),
    isBackEnabled = keyboard.isVisible,
    onBackCompleted = { keyboard.close() },
)
```

---

## Chrome

`KeyboardLayout` is the surface: fill, shape, elevation, safe-area padding, and the spacing between
everything inside it.

```kotlin
KeyboardLayout(
    style = KeyboardStyle.Default,   // or .flat() for a permanently docked keyboard
    size = KeyboardSize.Default,     // or .Compact for a short screen
    header = { Text(runningTotal) },
    footer = { CurrencyToolbar() },
) {
    // anything at all
}
```

`header` and `footer` are plain slots with no assumptions about their contents. A header is normally
the value being edited — a running total, a formula, the digits so far.

`KeyboardStyle` carries the surface's colours and the look of its keys (`keys: KeyStyle`);
`KeyboardSize` carries the spacing, the one key height (`keyHeight`), and the metrics of a single key
(`key: ButtonSize`). Both default to the nullable composition locals `LocalKeyboardStyle` and
`LocalKeyboardSize`, falling back to `KeyboardStyle.Default` and `KeyboardSize.Default`.
`ArecaThemeProvider` does not seed them, because `keyboard` depends on `foundation` and the reverse
wiring would be a cycle. Theme a subtree yourself:

```kotlin
CompositionLocalProvider(LocalKeyboardStyle provides KeyboardStyle.flat()) { }
```

The surface clears the bottom of the system bars and display cutout by default, so the last key row
is not under Android's gesture pill or an iPhone's home indicator. The platform IME is not part of
those insets, so the keyboard does not jump while it shows or hides. The fill still reaches the
screen edge.

`KeyboardStyle.flat()` draws its hairline along the top edge only, as the style's `divider`; a
style's `border` follows the whole outline.

---

## Keys

A `KeyboardGrid` is a list of `KeyRow`s, and a row divides its own width among its keys by
`Key.weight`. There is no column count — which is what lets an operator row of five sit above digit
rows of four without anything reconciling them.

```kotlin
KeyboardGrid(
    KeyRow(Key.text("7") { }, Key.text("8") { }, Key.text("9") { }),
    KeyRow(
        Key.blank("gap"),
        Key.text("0") { },
        Key.icon(ArecaIcons.Delete, id = "back", contentDescription = "Backspace", role = KeyRole.Destructive) { },
    ),
)
```

`Key.blank(id)` holds a slot open so the keys around it stay aligned — dropping the key instead would
let its row reflow.

Every key's `id` is unique within its grid — the transforms address keys by it, and the keyboard
keys its composition by it — so a grid holding two keys with one id is rejected, as is a key whose
`weight` is not a finite number above zero. A label key's text stays on one line
(`KeyboardSize.keyLabelMaxLines`) and ends in an ellipsis rather than wrapping.

### Roles and styles

A key says what it *means* with a `KeyRole`, rather than how loud it is, and the keyboard's
`KeyStyle` turns each role into a look. Keys are drawn with the foundation's `Button`, so each look
**is** a `ButtonStyle`: anything that styles a button styles a key.

| Role | For |
| --- | --- |
| `KeyRole.Digit` | numbers and separators (the default) |
| `KeyRole.Action` | operators and modifiers |
| `KeyRole.Confirm` | the key that concludes entry |
| `KeyRole.Destructive` | backspace, delete |
| `KeyRole.Caution` | clear, reset |
| `KeyRole.Info` | paste, scan — something pulled in from outside |
| `KeyRole.Ghost` | hide, switch layout |

Restyle every key of a role through `KeyboardStyle.default(keys = KeyStyle.default(…))`, or one key
through its own `style`, which overrides its role's look.

### Stock grids

`KeyboardGrids.numeric()`, `.decimal()`, `.phone()`, `.calculator()`.

Each returns a **value**, not a component, and needs no composition — keys carry roles, which become
looks only when drawn — so a preset is where a keyboard starts rather than something to fork:

```kotlin
KeyboardGrids.calculator(onAction = ::onKeyAction, decimalSeparator = ',')
    .replaceKey("clear") { it.copy(role = KeyRole.Destructive) }
    .removeKey("hide")
    .appendRow(KeyRow(Key.text("Done", role = KeyRole.Confirm) { commit() }))
```

Transforms: `key(id)`, `mapKeys`, `replaceKey`, `removeKey`, `appendRow`, `prependRow`.

`calculator()` takes an `auxiliaryKey` for the slot beside the `9` — a paste key, a scan key — since
the module ships no clipboard of its own. `phone()` is the standard four-by-three keypad — `1 2 3`,
`4 5 6`, `7 8 9`, `* 0 #` — with backspace in a fifth row, beneath `#`; its `*` and `#` report
`KeyAction.Insert`.

Keys show glyphs that need no translation — a backspace arrow, a chevron, a tick, `AC`. What
assistive technology reads for them is a word, from the `labels` every preset takes; the defaults
are English:

```kotlin
KeyboardGrids.calculator(
    onAction = ::onKeyAction,
    labels = KeyboardGridLabels(
        backspace = stringResource(Res.string.backspace),
        clear = stringResource(Res.string.clear),
        hide = stringResource(Res.string.hide_keyboard),
        done = stringResource(Res.string.done),
    ),
)
```

Rows keep their left-to-right order in a right-to-left locale, as keypads conventionally do; what
each key draws still follows the layout direction.

### Actions

```kotlin
fun onKeyAction(action: KeyAction) = when (action) {
    is KeyAction.Digit -> calculator.digit(action.value)
    KeyAction.DecimalSeparator -> calculator.decimal()
    is KeyAction.Operation -> calculator.operation(action.operation)
    is KeyAction.Insert -> calculator.insert(action.text)
    KeyAction.Backspace -> calculator.delete()
    KeyAction.Clear -> calculator.clear()
    KeyAction.Evaluate -> calculator.evaluate()
    KeyAction.Done -> { calculator.evaluate(); keyboard.close() }
    KeyAction.Hide -> keyboard.close()
    is KeyAction.Custom -> handleCustom(action.id)
}
```

### Physical keyboards

A custom keyboard replaces the platform's, which on desktop would otherwise leave the hardware keys
doing nothing. `toKeyAction` maps a press onto the same action:

```kotlin
Modifier
    .keyboardTarget(keyboard, target)
    .onPreviewKeyEvent { event ->
        val action = event.toKeyAction(decimalSeparator = ',') ?: return@onPreviewKeyEvent false
        onKeyAction(action)
        true
    }
```

Returning `null` for an unrecognised press is what lets Tab still move focus.

---

## Text fields and the system IME

The module never shows or hides the platform keyboard, so a real text field has to be kept from
opening it. Two common-code routes:

**Read-only field.** A field with `readOnly = true` never starts an input session, so the platform
keyboard stays down while the custom one drives the value. A text field handles its own taps and
focus, so open the keyboard when the field takes focus, and attach the target with
`Modifier.keyboardAnchor` rather than `Modifier.keyboardTarget` — the anchor wires the target's
focus and scroll requesters, measures the field so a keyboard drawn over the content scrolls it
fully clear, and reports the field as expanded while the keyboard is open for it, without adding a
press of its own:

```kotlin
val amount = rememberTextFieldState()
val target = rememberKeyboardTarget()

BasicTextField(
    state = amount,
    readOnly = true,
    modifier = Modifier
        .keyboardAnchor(keyboard, target, stealFocus = false)
        .onFocusChanged { if (it.isFocused) keyboard.open(target, stealFocus = false) },
)
```

Opened with `stealFocus = false`, the keyboard leaves the field focused when it closes.

Apply each `KeyAction` to the field with `amount.edit { … }`.

**Editable field with a cursor.** To keep the cursor, selection and editing gestures, leave the field
editable and block the platform keyboard around it with `InterceptPlatformTextInput`
(`@ExperimentalComposeUiApi`): an interceptor that never passes the request on suppresses the IME.

```kotlin
@OptIn(ExperimentalComposeUiApi::class)
InterceptPlatformTextInput(interceptor = { _, _ -> awaitCancellation() }) {
    BasicTextField(state = amount, modifier = /* as above */)
}
```

`KeyboardOptions(showKeyboardOnFocus = false)` is not enough on its own: it keeps the IME down when
focus moves to the field programmatically, but a tap on an editable field still opens it.
