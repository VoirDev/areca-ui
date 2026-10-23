# Keyboard — `dev.voir.areca:keyboard`

Add it when a screen needs its own on-screen keyboard docked to the bottom edge — amounts, PINs,
codes, a calculator, a phone pad — instead of the platform keyboard.

- Artifact `dev.voir.areca:keyboard`. Depends on `foundation` as `api` (keys are foundation
  `Button`s; `ButtonStyle` and `ButtonSize` appear in its API).
- Needs the app's `ArecaThemeProvider` above it.
- It carries no domain knowledge and never edits text: keys report a `KeyAction`, the app applies it
  to its own state.

| Package | Holds |
| --- | --- |
| `dev.voir.areca.keyboard` | `KeyboardHost`, `KeyboardController`, `rememberKeyboardController`, `LocalKeyboardController`, `KeyboardTarget`, `rememberKeyboardTarget`, `Modifier.keyboardTarget`, `Modifier.keyboardAnchor`, `KeyboardScrim`, `KeyboardAnimation` |
| `dev.voir.areca.keyboard.layout` | `KeyboardLayout`, `KeyboardStyle`, `KeyboardSize`, `LocalKeyboardStyle`, `LocalKeyboardSize` |
| `dev.voir.areca.keyboard.key` | `KeyboardKeys`, `Key`, `KeyContent`, `KeyRole`, `KeyStyle`, `KeyRow`, `KeyboardGrid`, `KeyboardGrids`, `KeyboardGridLabels`, `KeyAction`, `KeyOperation`, `toKeyAction` |

## Choose

| Need | Use | Types |
| --- | --- | --- |
| Dock a keyboard to the bottom of a screen; open it for an input; scroll the input clear | `KeyboardHost(keyboardContent) { … }` + `rememberKeyboardController()` + `rememberKeyboardTarget(key)` | `KeyboardController`, `KeyboardTarget`, `KeyboardScrim`, `KeyboardAnimation` |
| A pressable display (a big amount) that opens the keyboard | `Modifier.keyboardTarget(controller, target, onClickLabel = …)` | — |
| A real text field driven by the keyboard | `Modifier.keyboardAnchor(controller, target)` on a `readOnly` field | — |
| The keyboard's surface with header and footer slots | `KeyboardLayout(grid = …)` or `KeyboardLayout { … }` | `KeyboardStyle`, `KeyboardSize` |
| Keys without the surface — in a sheet, a panel, a screen | `KeyboardKeys(grid)` | `KeyStyle`, `KeyboardSize` |
| A stock pad | `KeyboardGrids.numeric` / `decimal` / `phone` / `calculator` | `KeyboardGridLabels` |
| A layout of your own | `KeyboardGrid(KeyRow(Key.text("1") { … }, …), …)` | `Key`, `KeyRole`, `KeyRow` |
| Hardware keys mapped to the same actions | `KeyEvent.toKeyAction(decimalSeparator)` | `KeyAction` |

## Signatures

```kotlin
@Composable fun KeyboardHost(
    keyboardContent: @Composable (KeyboardTarget?) -> Unit,   // receives the open target
    modifier: Modifier = Modifier,
    controller: KeyboardController = rememberKeyboardController(),
    compressContent: Boolean = true,
    scrim: KeyboardScrim? = null,
    animation: KeyboardAnimation = KeyboardAnimation.Default,
    paneTitle: String? = "Keyboard",                          // announced on open; null = none
    onVisibilityChange: ((Boolean) -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
)
@Composable fun rememberKeyboardController(): KeyboardController     // saveable
@Composable fun rememberKeyboardTarget(key: Any? = null): KeyboardTarget

fun Modifier.keyboardTarget(
    controller: KeyboardController,
    target: KeyboardTarget,
    enabled: Boolean = true,
    scroll: Boolean = true,               // scroll the target above the keyboard once open
    stealFocus: Boolean = true,           // move focus here so hardware keys reach it
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,       // no press feedback by default
): Modifier
fun Modifier.keyboardAnchor(              // for a text field: wires focus, scroll, expanded state; adds no press
    controller: KeyboardController,
    target: KeyboardTarget,
    scroll: Boolean = true,
    stealFocus: Boolean = true,
): Modifier

// dev.voir.areca.keyboard.layout
@Composable fun KeyboardLayout(
    grid: KeyboardGrid,
    modifier: Modifier = Modifier,
    style: KeyboardStyle = LocalKeyboardStyle.current ?: KeyboardStyle.Default,
    size: KeyboardSize = LocalKeyboardSize.current ?: KeyboardSize.Default,
    windowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Bottom),
    header: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
)
@Composable fun KeyboardLayout(           // free content instead of a grid
    modifier: Modifier = Modifier,
    style: KeyboardStyle = LocalKeyboardStyle.current ?: KeyboardStyle.Default,
    size: KeyboardSize = LocalKeyboardSize.current ?: KeyboardSize.Default,
    windowInsets: WindowInsets = /* as above */,
    header: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
)

// dev.voir.areca.keyboard.key
@Composable fun KeyboardKeys(
    grid: KeyboardGrid,
    modifier: Modifier = Modifier,
    style: KeyStyle = (LocalKeyboardStyle.current ?: KeyboardStyle.Default).keys,
    size: KeyboardSize = LocalKeyboardSize.current ?: KeyboardSize.Default,
)
```

## Controller, target, keys and actions

| `KeyboardController` | Meaning |
| --- | --- |
| `isVisible`, `target` | Open, and for which target |
| `open(target: KeyboardTarget? = null, scroll: Boolean = true, stealFocus: Boolean = true)` | Open for a target |
| `close()`; `toggle(target, scroll, stealFocus)` | Close; open or close |
| `LocalKeyboardController` | The host's controller, for anything below it |

```kotlin
KeyboardTarget(key: Any? = null, focusRequester: FocusRequester, bringIntoViewRequester: BringIntoViewRequester)

Key.text(label: String, id: String = label, weight: Float = 1f, enabled: Boolean = true,
    role: KeyRole = KeyRole.Digit, style: ButtonStyle? = null, contentDescription: String? = null, onClick: () -> Unit)
Key.icon(image: ImageVector, id: String, contentDescription: String? = null, weight: Float = 1f, enabled: Boolean = true,
    role: KeyRole = KeyRole.Digit, style: ButtonStyle? = null, onClick: () -> Unit)
Key.custom(id: String, weight: Float = 1f, enabled: Boolean = true, role: KeyRole = KeyRole.Digit,
    style: ButtonStyle? = null, contentDescription: String? = null, onClick: () -> Unit, content: @Composable () -> Unit)
Key.blank(id: String, weight: Float = 1f)          // holds a slot so the row does not reflow
KeyRow(vararg keys: Key)  /  KeyRow(keys: List<Key>, height: Dp? = null)
KeyboardGrid(vararg rows: KeyRow)  /  KeyboardGrid(rows: List<KeyRow>)
grid.key(id) · grid.mapKeys { } · grid.replaceKey(id) { it.copy(…) } · grid.removeKey(id) · grid.appendRow(row) · grid.prependRow(row)

KeyboardGrids.numeric(onAction, leadingKey: Key = Key.blank("leading"), labels = KeyboardGridLabels())
KeyboardGrids.decimal(onAction, decimalSeparator: Char = '.', labels = KeyboardGridLabels())
KeyboardGrids.phone(onAction, labels = KeyboardGridLabels())          // 1-9, * 0 #, backspace row; * and # report Insert
KeyboardGrids.calculator(onAction, decimalSeparator: Char = '.', auxiliaryKey: Key = Key.blank("auxiliary"), labels = KeyboardGridLabels())
KeyboardGridLabels(backspace = "Backspace", clear = "Clear", hide = "Hide keyboard", done = "Done")

KeyAction.Digit(value: Char) | DecimalSeparator | Operation(operation: KeyOperation) | Insert(text: String)
    | Backspace | Clear | Evaluate | Done | Hide | Custom(id: String)
KeyOperation.Add | Subtract | Multiply | Divide
fun KeyEvent.toKeyAction(decimalSeparator: Char = '.'): KeyAction?   // null for unrecognised keys (Tab still moves focus)
```

| `KeyRole` | For | Default look (`KeyStyle.default(shape = shapes.lg, …)`) |
| --- | --- | --- |
| `Digit` (default) | Numbers, separators | `ButtonStyle.soft(Neutral)` |
| `Action` | Operators, modifiers | `soft(Brand)` |
| `Confirm` | The key that ends entry | `solid(Brand)` |
| `Destructive` | Backspace, delete | `soft(Danger)` |
| `Caution` | Clear, reset | `soft(Warning)` |
| `Info` | Paste, scan | info tint |
| `Ghost` | Hide, switch layout | `plain(Neutral)` |

## Rules

- Put **one** `KeyboardHost` at the root of the screen; branch on `target?.key` in `keyboardContent`
  so one host serves every input.
- Give targets saveable keys (string, number, enum) so an open keyboard survives rotation; focus is not
  restored.
- Every key `id` is unique in its grid and every `weight` finite and above zero, or the grid is
  rejected. Rows divide their own width — no column count. Grids are values: start from a preset and
  transform it.
- `compressContent = true` pads the content by the keyboard's height (and consumes that bottom inset);
  use `false` when the screen has a docked action bar, so the keyboard covers rather than lifts it.
- `scrim = null` by default (a keyboard is not modal). `KeyboardScrim.Default` /
  `KeyboardScrim.default(color, dismissOnTap = true, dismissLabel = "Close keyboard")` blocks the
  content beneath; pass a localised `dismissLabel`.
- The host does not handle Back: wire it where the app's back stack lives
  (`isBackEnabled = keyboard.isVisible`, `onBackCompleted = { keyboard.close() }`).
- **Text fields:** the module never hides the platform IME. Either `readOnly = true` +
  `Modifier.keyboardAnchor(keyboard, target, stealFocus = false)` + open on focus, applying actions to
  the app's value; or keep the field editable inside
  `InterceptPlatformTextInput(interceptor = { _, _ -> awaitCancellation() })`
  (`@ExperimentalComposeUiApi`). `KeyboardOptions(showKeyboardOnFocus = false)` alone is not enough.
- On desktop map hardware keys with `onPreviewKeyEvent { it.toKeyAction(',')?.let { a -> onKeyAction(a); true } ?: false }`.
- Rows keep left-to-right order in right-to-left locales.

## Accessibility and words

- Pass the app's `paneTitle` (announced on open) or `null`; `onClickLabel` on a `keyboardTarget`
  ("Edit amount"). Targets read collapsed / expanded with open and close actions.
- Glyph keys need words: pass `KeyboardGridLabels` from the app's strings to every preset, and a
  `contentDescription` to every `Key.icon` / `Key.custom`.

## Styles and sizes

| Type | Local | Presets / factories |
| --- | --- | --- |
| `KeyboardStyle(bg, content, border, borderWidth, shape, elevation, divider, keys: KeyStyle)` | `LocalKeyboardStyle` | `Default`, `default(…)`, `flat(bg, border, keys)` — square, no elevation, top hairline, for a permanently docked keyboard |
| `KeyStyle(digit, action, confirm, destructive, caution, info, ghost)` — each a `ButtonStyle`; `of(role)` | via `KeyboardStyle.keys` | `Default`, `default(shape, digit, …)`; one key: `Key(style = …)` |
| `KeyboardSize(padding, rowGap, keyGap, keyHeight, headerGap, footerGap, key: ButtonSize, keyLabelMaxLines = 1)` | `LocalKeyboardSize` | `Default`, `Compact` (short screens), `default(…)` (every parameter defaulted; `keyHeight = 52.dp`) |
| `KeyboardAnimation(enter, exit, scrimEnter, scrimExit, insetEnter, insetExit)` | — | `Default`, `default(duration, enterEasing, exitEasing)` |

`ArecaThemeProvider` does not seed `LocalKeyboardStyle` / `LocalKeyboardSize`; provide them:
`CompositionLocalProvider(LocalKeyboardStyle provides KeyboardStyle.flat()) { … }`.

## Snippets

```kotlin
// Amount entry: one host, a pressable display as the target
@Composable
fun AmountScreen(amount: String, onKey: (KeyAction) -> Unit, labels: KeyboardGridLabels) {
    val keyboard = rememberKeyboardController()
    val target = rememberKeyboardTarget(key = "amount")
    KeyboardHost(
        controller = keyboard,
        paneTitle = keyboardPaneTitle,
        keyboardContent = {
            KeyboardLayout(
                grid = KeyboardGrids.decimal(onAction = onKey, decimalSeparator = ',', labels = labels),
                header = { Text(amount, textAlign = TextAlign.End) },
            )
        },
    ) {
        Text(
            text = amount.ifEmpty { "0" },
            variant = TextVariant.Display,
            modifier = Modifier.keyboardTarget(keyboard, target, onClickLabel = editAmountLabel),
        )
    }
}

// The app applies actions to its own state
fun onKey(action: KeyAction) {
    when (action) {
        is KeyAction.Digit -> amount += action.value
        KeyAction.DecimalSeparator -> if (',' !in amount) amount += ','
        KeyAction.Backspace -> amount = amount.dropLast(1)
        KeyAction.Done, KeyAction.Hide -> keyboard.close()
        else -> Unit
    }
}

// Two inputs, one host
KeyboardHost(
    keyboardContent = { target ->
        when (target?.key) {
            "amount" -> KeyboardLayout(grid = KeyboardGrids.decimal(onAction = ::onAmountKey, labels = labels))
            "pin" -> KeyboardLayout(grid = KeyboardGrids.numeric(onAction = ::onPinKey, labels = labels))
        }
    },
) { Form() }

// Adapt a preset
KeyboardGrids.calculator(onAction = ::onKey, decimalSeparator = ',', labels = labels)
    .replaceKey("clear") { it.copy(role = KeyRole.Destructive) }
    .removeKey("hide")
    .appendRow(KeyRow(Key.text(doneLabel, role = KeyRole.Confirm) { commit() }))

// Keys inside a sheet, no surface
KeyboardKeys(grid = KeyboardGrids.phone(onAction = ::onKey, labels = labels))

// Read-only foundation text field driven by the keyboard
TextField(
    value = amount,
    onValueChange = {},
    readOnly = true,
    modifier = Modifier
        .keyboardAnchor(keyboard, target, stealFocus = false)
        .onFocusChanged { if (it.isFocused) keyboard.open(target, stealFocus = false) },
)
```
