# Crop

An image cropper for Compose Multiplatform: frame a photo for an avatar, a cover or a post.

| Piece | What it does | Key types |
| --- | --- | --- |
| **Spec** | What one use of the cropper decides: aspect, mask, grips, choices, output size | `CropSpec`, `CropAspect`, `CropShapes` |
| **State** | Where the photo sits under the frame, the commands that move it, and the finished crop | `ImageCropState`, `rememberImageCropState`, `CropResult` |
| **Stage** | The photo under a fixed frame, with the outside veiled, and every gesture | `ImageCropper`, `ImageCropperStyle`, `ImageCropperSize` |
| **Controls** | Reset, Angle, Zoom, Format, Mirror and Rotate over a Cancel and Done bar | `CropToolbar`, `CropToolbarLabels`, `CropDial` |
| **Window** | The crop screen over everything, with the system bars in the editor's black | `CropDialog` |

The module decodes nothing and uploads nothing. It takes a `Painter` the application decoded, and
gives back a bitmap or a `CropResult` the application stores or sends.

---

## Setup

```kotlin
implementation("dev.voir.areca:crop:<version>")
implementation("dev.voir.areca:foundation:<version>")
```

The cropper draws with the Areca theme, so an `ArecaThemeProvider` must sit above it — usually the one
at the application's root:

```kotlin
ArecaThemeProvider {
    App()
}
```

`crop` uses `foundation` only internally and does not pass it on, so depend on `foundation` yourself:
it holds `ArecaThemeProvider`, and the `SystemBars`, `SquircleShape` and `ArecaDarkTheme` this file
mentions. The `ArecaIcons.*` icons in [Your own controls](#your-own-controls) come from
`dev.voir.areca:icons`, which nothing here depends on; add it only to use them.

### The photo

- **Upright.** The stage shows the painter as it is, so apply the photo's EXIF orientation when
  decoding it.
- **A known size.** The painter's intrinsic size is taken as the photo's size in pixels; a painter
  without one never shows.
- **Software, on Android.** Rendering draws the painter into a software bitmap, which cannot draw a
  hardware bitmap (`Bitmap.Config.HARDWARE`, Coil's default). Decode with hardware bitmaps turned off
  — for Coil, `allowHardware(false)`. `render` fails with an `IllegalArgumentException` that says so.
- **Display-sized, on Android.** The stage draws the photo on the hardware canvas, which refuses a
  bitmap over 100 MB — `width × height × 4` bytes, about 26 megapixels, so a 48-megapixel photo fails.
  Decode the stage's photo to no more than about 4096 pixels on the longest side, and render from the
  full-resolution photo when the crop needs more detail (see
  [Full resolution from a preview](#full-resolution-from-a-preview)).

---

## Where it goes

**In the application, not in a flow.** Picking, cropping and uploading are one sequence, and it is
the application's: whether to crop at all, and how, is a product decision — a photo yes, a PDF no; a
circle for one product's avatars, a squircle for another's. The flow modules stop exactly where
cropping would start — `EditableAvatar` reports `onChangePhoto`, `PendingAttachmentSheet` reports
`onEdit` — and none of them depends on this module.

So an application writes **one crop screen** and opens it from every place it crops, each time with
that place's **`CropSpec`**:

```kotlin
// The application's, once per place it crops.
val AvatarCrop = CropSpec.Avatar
val CoverCrop = CropSpec(CropAspect.Ratio(3, 1), isAspectLocked = true, maxOutputDimension = 1500)
val PostCrop = CropSpec.Photo.copy(maxOutputDimension = 2048)
```

The flow recipes show the sequence end to end: `flow/profile` "Recipe: changing the photo, with
cropping" and `flow/attachments` "Recipe: cropping a photo before it's attached".

---

## Quick start: the crop screen

```kotlin
@Composable
fun CropScreen(
    photo: Painter,
    spec: CropSpec,
    title: String,
    onCancel: () -> Unit,
    onDone: (ImageBitmap) -> Unit,
    applyMask: Boolean = true,
) {
    val state = rememberImageCropState(spec, key = photo)

    Column(
        Modifier
            .fillMaxSize()
            .background(ImageCropperStyle.Default.stage)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        ImageCropper(
            painter = photo,
            state = state,
            contentDescription = "Photo being cropped",
            modifier = Modifier.weight(1f).fillMaxWidth(),
        )
        CropToolbar(
            state = state,
            title = title,
            onCancel = onCancel,
            onDone = { state.render(photo, applyMask)?.let(onDone) },
        )
    }
}
```

The stage reads the mask, the toolbar reads the aspect choices, and `render` reads the mask and the
output size — all from the spec the state was made with, so none of them is repeated. The screen has
no top bar: Cancel and Done are icon buttons at the ends of the toolbar's bar, with the title between
them.

**Key the state on the photo.** The state keeps its crop and its undo history while the spec and the
`key` stay equal. A photo of another size starts a fresh crop on its own, but most photos from one
camera share a size, so without `key = photo` a second photo would open on the first one's crop, and
Undo would walk into it.

**It survives rotation and process death.** `rememberImageCropState` saves the crop and its undo
history as instance state, and `CropToolbar` saves its open tool and its discard question, so turning
the device or coming back to a killed process finds the editor as it was. The photo is not saved: keep
what identifies it (a content URI, a file path) in the screen's own saved state, decode it again, and
show the same photo. A photo of another size starts a fresh crop. After a configuration change the
stage refits the crop to its new size, keeping its proportions.

### Showing it

Open it as a destination of the application's navigation, or over the current screen in a
**`CropDialog`**:

```kotlin
picked?.let { photo ->
    CropDialog(onDismissRequest = { picked = null }) {
        CropScreen(photo, PostCrop, title = "Crop photo", onCancel = { picked = null }, onDone = ::upload)
    }
}
```

A screen drawn over the product's own window keeps that window's system bars — on Android their
light scrims and dark icons, which read as lighter bands above and below the dark editor. `CropDialog`
is a window of its own that draws behind the status and navigation bars, paints them with the stage's
black, asks for light bar icons and no contrast scrim, and dims nothing. The back gesture (Escape on
the desktop) goes to the toolbar first: it closes an open tool, keeping its changes, then asks
before discarding changes, and only then calls `onDismissRequest`. Pad the screen by
`WindowInsets.safeDrawing`, as above, so the toolbar clears the navigation bar. On iOS the bars are
painted, but the status bar's text keeps the style the application's view controller gives it.

A navigation destination has no window of its own; call foundation's `SystemBars` in it —
`SystemBars(ImageCropperStyle.Default.stage)` — and the activity's bars match the editor while it is
shown, and go back to what they were when it leaves.

---

## How it behaves

**The frame holds still; the photo moves.** Drag, pinch and scroll always act on the photo. Only the
frame's grips move the frame, so a stray touch never resizes the crop.

**The photo always covers the frame.** Straightening zooms in just enough to hide the corners, and the
zoom the user chose is kept on top of that. Dragging past an edge stretches with resistance and
springs back.

**Resizing zooms into the selection.** When a grip is let go, the frame re-centres and grows to fill
the stage after a short pause, carrying the photo with it.

**Guides only while working.** A thirds grid appears while the user adjusts — a finer one while
straightening — and the veil outside the frame lifts at the same time.

**Options first, one tool at a time.** The toolbar is a row of options over a bar. Reset puts
everything back. Angle, Zoom and Format are tools: each replaces the row with its control — a dial or
the aspect chips — and turns the bar into Undo, one action of the tool's own and OK. The action is
Level for Angle, Fit for Zoom and Swap (4:3 to 3:4) for Format; OK keeps the changes and brings the
row back. Mirror is a toggle, filled while on, and Rotate turns a quarter turn left on every tap. A
dot marks a tool or Rotate whose value has left its start. Format is left out when the spec locks the
aspect or offers no choices.

**Undo walks back the whole session.** Every change is a step — a rotation, a mirror, an aspect, a
Reset, a dial drag, a drag or pinch of the photo, a key press on the stage or the dial, an
assistive-technology action — and Undo steps back through them one at a time, across tools and
across earlier visits to the same tool, to the starting crop. Scroll-wheel ticks that follow each
other closely are one step, and so is a key held down, from the moment it goes down until it comes
up. Opening a tool after an OK still finds Undo ready. Undo and Reset turn the photo the short way
round. The history is `state.undo()` and `state.canUndo`, for controls of your own; continuous
commands (`setStraighten`, `setZoom`) are recorded by the `startAdjusting` that begins them.

**Values report where the crop is going.** `state.straighten`, `state.zoom`, `state.rotation` and
`state.isMirrored` hold an animation's target from the moment it starts — Level reads `0.0°` at once,
not on the last frame — as do `result()` and `render`. The stage draws the values in between, and so
do the toolbar's dial and its readout.

**Leaving asks first.** Cancel on a fresh crop leaves at once. Once anything has changed, it asks in
a sheet whether to discard the changes — Discard leaves, Keep editing stays.

**Every drag has another way.** The stage takes the arrow keys (Shift for further), `+` and `−`, and
`[` and `]`; a mouse click on it gives it focus, and a tap does not. For assistive technology it
offers actions: move the photo left, right, up and down, zoom in and out, and — on a resizable `Free`
frame, in place of the grips — make the frame wider or taller. The dial takes the arrow keys and Home
and is an adjustable control for assistive technology; Rotate and Reset are buttons, Mirror a switch,
and the aspect chips a group of radio buttons. The bar's title is read as a heading.

**Foundation's interaction.** The options and the chips answer a press with foundation's tint
indication and show its focus ring, unclipped; the chips are drawn 36dp tall in a 48dp touch target.
The stage and the dial show the same focus ring — the stage just inside its edge, since it fills its
slot — in their style's `focusRing` color.

---

## The spec

```kotlin
CropSpec(
    aspect = CropAspect.Square,       // Free · Original · Ratio(w, h)
    isAspectLocked = true,            // frame keeps its aspect through rotation; no chips
    isResizable = false,              // no grips: frame the photo by moving and zooming it
    mask = CropShapes.Oval,           // any Shape: RectangleShape, RoundedCornerShape(12), SquircleShape(…), your own
    aspects = emptyList(),            // chips the toolbar offers, e.g. CropAspect.PhotoPresets
    maxZoom = 10f,
    maxOutputDimension = 512,         // longest side of the render; never scales a small crop up
)
```

| Product | Spec |
| --- | --- |
| Avatar | `CropSpec.Avatar` — square, locked, no grips, oval, 512 px |
| Avatar, squircle product | `CropSpec.Avatar.copy(mask = SquircleShape(0.24f))` |
| Photo editor | `CropSpec.Photo` — free, every ratio on chips, grips, full detail |
| Cover banner | `CropSpec(CropAspect.Ratio(3, 1), isAspectLocked = true, maxOutputDimension = 1500)` |
| Badge, with a shape of the application's own | `CropSpec.Avatar.copy(mask = MyBadgeShape, maxOutputDimension = 1024)` |

**Shape is separate from aspect.** A circle over 1:1 is an avatar; the same circle over 16:9 is an
ellipse. Use `CropShapes.Oval` rather than `CircleShape` for a mask — `CircleShape` is a fully rounded
rectangle, which on a frame that is not square draws a pill.

**A spec that compares unequal starts a fresh crop.** Define specs once, as top-level values or
`copy`s of the presets, and give a custom mask a stable instance — a `GenericShape` built inline is a
new, unequal shape every time.

---

## The result

`state.render(painter)` is the common case: cut to the spec's mask, no larger than its output size.
Pass `applyMask = false` to store the rectangular crop and mask it only where it is shown — an
avatar component that clips its picture anyway, so the stored picture fits whatever shape the
product later draws avatars in.

The mask comes out as the stage drew it: `state.render` resolves it at the frame's size on screen,
with the stage's density and layout direction, and scales it to the output. A mask sized in dp keeps
the proportions the user saw, and a mask with a different start and end keeps its side in a
right-to-left layout.

For anything else, `state.result()` describes the crop. A straightened crop is not an axis-aligned
rectangle of the source, so `CropResult` gives it in the source's own pixels, as three steps applied
in this order:

1. When `isMirrored`, flip the source left to right about the vertical line through `center`.
2. Rotate it clockwise by `rotation` degrees about `center`.
3. Cut the axis-aligned rectangle of `size` centred on `center`.

The mirror comes first; flipping after rotating gives another picture for any `rotation` that is not
a multiple of 180°. A server that maps output pixels back to the source reads the point at offset `q`
from the output's centre, in source pixels, from

```
center + M · R(−rotation) · q
```

where `R(a)` turns clockwise by `a` degrees with y pointing down — `(x, y) → (x·cos a − y·sin a,
x·sin a + y·cos a)` — and `M` negates x when `isMirrored`.

The result is also enough to store, or to render at a size of your choosing:

```kotlin
val result = state.result() ?: return
val thumbnail = result.render(painter, result.outputSize(maxDimension = 256), mask = CropShapes.Oval)
```

`CropResult.render` knows nothing of the stage, so it resolves a mask at the output's own size, with
the `density` and `layoutDirection` it is given. Give it a proportional mask, such as `CropShapes.Oval`
or `RoundedCornerShape(percent = 12)`, or use `state.render`.

`outputSize(maxDimension)` takes a positive cap, or `null` for none; zero or less throws.

Rendering runs on the calling thread and allocates the whole bitmap, `width × height × 4` bytes; move
a large render off the main thread, and cap it with `maxOutputDimension`. On Android the painter must
not draw a hardware bitmap (see [The photo](#the-photo)).

### Full resolution from a preview

The stage only needs a display-sized photo, and on Android cannot draw a much larger one. Show it a
preview decoded to about 4096 pixels, and hand `render` the full-resolution photo when Done is
pressed: a painter whose intrinsic size differs from the stage's photo gets the crop scaled to it
first, so the output keeps the full photo's detail, capped as usual by `maxOutputDimension`.

```kotlin
CropToolbar(
    state = state,
    onCancel = onCancel,
    onDone = { state.render(fullPhoto)?.let(onDone) },
)
```

`CropResult.scaledTo` does the same for a crop you store or render yourself:

```kotlin
val full = state.result()?.scaledTo(fullPhoto.intrinsicSize) ?: return
val bitmap = full.render(fullPhoto, full.outputSize(maxDimension = 2048))
```

Both copies must be the same picture in the same orientation — the preview a downsample of the full
photo, with its EXIF orientation applied the same way. The centre scales with each side and the size
by their mean, which absorbs the rounding of a downsample; proportions that differ by more than 2%,
such as a photo turned on its side, throw an `IllegalArgumentException`.

---

## Styles

**The cropper is always dark.** `ImageCropper`, `CropToolbar` and `CropDial` draw with the theme's
palette mapped for dark whatever the application's theme — the convention of photo editors, where a
neutral dark surround keeps colors judged fairly. The stage and the toolbar share one black ground,
and the toolbar draws its foundation buttons in foundation's `ArecaDarkTheme`, a dark scope that keeps
the application's palette, type and shapes.

The crop screen paints itself with `ImageCropperStyle.Default.stage`, as in the quick start, so the
regions under the system bars match. Nothing needs wrapping in a dark `ArecaThemeProvider`.

Every color is still a parameter: `ImageCropperStyle.default(…)`, `CropToolbarStyle.default(…)` and
`CropDialStyle.default(…)` take overrides, with the dark defaults filling the rest. The metrics are
`ImageCropperSize`, `CropToolbarSize` (options, the changed dot, the rule after Reset, the chips and
their glyphs, and the dial's `CropDialSize`) and `CropDialSize`, each with constructor defaults.

| Component | Style · local | Size · local |
| --- | --- | --- |
| `ImageCropper` | `ImageCropperStyle` · `LocalImageCropperStyle` | `ImageCropperSize` · `LocalImageCropperSize` |
| `CropToolbar` | `CropToolbarStyle` · `LocalCropToolbarStyle` | `CropToolbarSize` · `LocalCropToolbarSize` |
| `CropDial` | `CropDialStyle` · `LocalCropDialStyle` | `CropDialSize` · `LocalCropDialSize` |

Each component's `style` and `size` default to its local, or to the type's `Default` when the local
is `null`. `ArecaThemeProvider` does not seed these locals — it lives in `foundation`, which does not
depend on `crop` — so provide them yourself around the screens that want other defaults:

```kotlin
CompositionLocalProvider(
    LocalImageCropperStyle provides ImageCropperStyle.default(veil = Color.Black.copy(alpha = 0.8f)),
    LocalCropDialStyle provides CropDialStyle.default(indicator = Color.White),
) {
    CropScreen(photo, PostCrop, title = "Crop photo", onCancel = onCancel, onDone = onDone)
}
```

The defaults follow each other: `CropToolbarStyle.default()` paints its `bg` with the stage of
`LocalImageCropperStyle`, and takes its `dial` from `LocalCropDialStyle`; `CropToolbarSize.Default`
takes its `dial` from `LocalCropDialSize`; and `CropDialog`'s color is the provided stage too.

---

## Your own controls

`CropToolbar` is a convenience. Every command it calls is public on `ImageCropState`, and `CropDial`
is a component of its own:

```kotlin
CropDial(
    value = state.straighten,
    onValueChange = state::setStraighten,
    valueRange = -45f..45f,
    contentDescription = "Angle",
    onAdjustStart = { state.startAdjusting(straightening = true) },
    onAdjustEnd = state::stopAdjusting,
)
IconButton(ArecaIcons.RotateCcwSquare, "Rotate", onClick = state::rotateLeft)
```

`IconButton` is foundation's, and `ArecaIcons.RotateCcwSquare` comes from `dev.voir.areca:icons`
(see [Setup](#setup)).

Pair `startAdjusting` with `stopAdjusting` so a custom control records its undo step and shows the
stage's guides the way the toolbar's dial does. The dial calls `onAdjustStart` and `onAdjustEnd`
around a drag, around a key from the moment it goes down until it comes up (or focus leaves the
dial), and around each change assistive technology makes, so each of those is a step of its own.
`state.straighten` jumps to an animation's target, so a dial of your own fed with it jumps with Level
rather than gliding; the toolbar's own dial follows the stage's drawn value.

---

## Words

Every word the toolbar draws or reads aloud is a property of `CropToolbarLabels`, with an English
default — the options (`reset`, `angle`, `zoom`, `format`, `mirror`, `rotate`), the bar (`cancel`,
`done`, `undo`, `ok`), the tool actions (`level`, `fit`, `swapOrientation`), the discard question
(`discardTitle`, `discardDescription`, `discard`, `keepEditing`) and the `free` and `original` chips.
The stage's assistive-technology actions are named there too (`moveLeft`, `moveRight`, `moveUp`,
`moveDown`, `zoomIn`, `zoomOut`, `widenFrame`, `heightenFrame`). Pass the product's own as `labels`
to both `CropToolbar` and `ImageCropper`; the screen title is the `title` parameter. Ratio chips are
labelled with their numbers (`4:3`, swapping to `3:4`).

The values the toolbar shows and reads aloud are written by functions there too: `angleValue`
(`+3.2°`), `zoomValue` (`2.5×`) and `rotationValue` (`90°`, read by Rotate). Pass function
references or top-level values, since labels that compare unequal recompose the toolbar:

```kotlin
val GermanCropLabels = CropToolbarLabels(
    angle = "Winkel",
    zoomValue = { zoom -> "${(zoom * 10).roundToInt() / 10f}-fach".replace('.', ',') },
)
```

---

## Where to look

Under **Crop** in the playground: the cropper, the toolbar and the dial, each with a page of its own.
[`CropScreen`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/crop/CropScreen.kt)
is the quick start above as compiled code, with the insets it pads by taken as a `windowInsets`
parameter, and the flow recipe pages open it — **Profile flow → Change photo** and **Attachments
flow → Cropping**.
