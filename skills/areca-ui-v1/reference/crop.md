# Crop — `dev.voir.areca:crop`

Add it when the app frames a photo before saving or uploading it: an avatar, a cover banner, a post.

- Artifact `dev.voir.areca:crop`; one package, `dev.voir.areca.crop`.
- Uses `foundation` internally and does **not** pass it on: declare
  `dev.voir.areca:foundation` yourself (it holds `ArecaThemeProvider`, `SystemBars`,
  `SquircleShape`, `ArecaDarkTheme`).
- Needs the app's `ArecaThemeProvider` above it. The cropper is always dark whatever the app's theme;
  do not wrap it in a dark provider.
- It decodes nothing and uploads nothing: it takes a `Painter` the app decoded and returns an
  `ImageBitmap` or a `CropResult` the app stores or sends.

## Choose

| Need | Use | Types |
| --- | --- | --- |
| What one place that crops decides: aspect, mask, grips, chips, output size | `CropSpec` (`CropSpec.Avatar`, `CropSpec.Photo`, or `copy`) | `CropAspect`, `CropShapes` |
| The crop's state, commands, undo and the finished crop | `rememberImageCropState(spec, key = photo)` | `ImageCropState`, `CropResult` |
| The photo under a fixed frame, with every gesture | `ImageCropper(painter, state, contentDescription)` | `ImageCropperStyle`, `ImageCropperSize` |
| Reset, Angle, Zoom, Format, Mirror, Rotate over a Cancel / Done bar | `CropToolbar(state, onCancel, onDone)` | `CropToolbarLabels`, `CropToolbarStyle`, `CropToolbarSize` |
| A straightening dial in controls of your own | `CropDial(value, onValueChange, valueRange, contentDescription)` | `CropDialStyle`, `CropDialSize` |
| The crop screen over the current one, with system bars in the editor's black | `CropDialog(onDismissRequest) { … }` | — |

## Signatures

```kotlin
@Composable fun rememberImageCropState(spec: CropSpec = CropSpec(), key: Any? = null): ImageCropState

@Composable fun ImageCropper(
    painter: Painter,
    state: ImageCropState,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = state.spec.mask,
    style: ImageCropperStyle = LocalImageCropperStyle.current ?: ImageCropperStyle.Default,
    size: ImageCropperSize = LocalImageCropperSize.current ?: ImageCropperSize.Default,
    labels: CropToolbarLabels = CropToolbarLabels(),     // names the stage's accessibility actions
    interactionSource: MutableInteractionSource? = null,
)

@Composable fun CropToolbar(
    state: ImageCropState,
    onCancel: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    aspects: List<CropAspect> = state.spec.aspects,
    style: CropToolbarStyle = LocalCropToolbarStyle.current ?: CropToolbarStyle.Default,
    size: CropToolbarSize = LocalCropToolbarSize.current ?: CropToolbarSize.Default,
    labels: CropToolbarLabels = CropToolbarLabels(),
)

@Composable fun CropDial(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tickStep: Float = 1f,
    majorTickEvery: Int = 5,
    origin: Float = 0f,
    stateDescription: String? = null,
    style: CropDialStyle = LocalCropDialStyle.current ?: CropDialStyle.Default,
    size: CropDialSize = LocalCropDialSize.current ?: CropDialSize.Default,
    interactionSource: MutableInteractionSource? = null,
    onAdjustStart: () -> Unit = {},
    onAdjustEnd: () -> Unit = {},
)

@Composable fun CropDialog(
    onDismissRequest: () -> Unit,
    color: Color = (LocalImageCropperStyle.current ?: ImageCropperStyle.Default).stage,
    content: @Composable () -> Unit,
)
```

## Spec, state and result

```kotlin
CropSpec(
    aspect: CropAspect = CropAspect.Free,     // Free | Original | Ratio(width, height) | CropAspect.Square
    isAspectLocked: Boolean = false,          // keeps the aspect through rotation; no Format chips
    isResizable: Boolean = true,              // false = no grips; frame by moving and zooming the photo
    mask: Shape = RectangleShape,             // CropShapes.Oval, RoundedCornerShape(…), SquircleShape(…), your own
    aspects: List<CropAspect> = emptyList(),  // chips the toolbar offers, e.g. CropAspect.PhotoPresets
    maxZoom: Float = 10f,
    maxOutputDimension: Int? = null,          // longest side of the render; never scales up
)
CropSpec.Avatar   // Square, Oval mask, 512 px, no grips — CropSpec(aspect = Square, mask = Oval, maxOutputDimension = 512)
CropSpec.Photo    // Free, every PhotoPresets chip, grips, full detail
CropAspect.PhotoPresets   // Free, Original, Square, 4:5, 4:3, 3:2, 16:9
CropShapes.Oval

CropResult(imageSize: Size, center: Offset, size: Size, rotation: Float, isMirrored: Boolean)
result.scaledTo(sourceSize: Size): CropResult           // map a preview's crop onto the full photo
result.outputSize(maxDimension: Int? = null): IntSize   // cap > 0 or null; ≤ 0 throws
result.render(
    painter: Painter,
    outputSize: IntSize = outputSize(),
    mask: Shape = RectangleShape,
    density: Density = Density(1f),
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
): ImageBitmap
```

| `ImageCropState` | Meaning |
| --- | --- |
| `spec`, `isReady` | The spec it was made with; a photo is laid out |
| `aspect`, `isOrientationSwapped`, `straighten`, `zoom`, `rotation`, `isMirrored` | Current values — an animation's target from its first frame |
| `isAdjusting`, `isStraightening` | A gesture or control is under way (guides show) |
| `canSwapOrientation`, `canReset`, `canUndo` | For controls of your own |
| `render(painter: Painter, applyMask: Boolean = true): ImageBitmap?` | The crop, cut to the spec's mask and capped at `maxOutputDimension`; `null` before layout |
| `result(): CropResult?` | The crop in source pixels: mirror about `center`, rotate clockwise by `rotation` about `center`, cut `size` around `center` |
| `selectAspect(aspect)`, `swapOrientation()`, `rotateLeft()`, `flipHorizontally()`, `setStraighten(degrees)`, `setZoom(zoom)`, `resetStraighten()`, `resetZoom()`, `reset()`, `undo()` | Commands, each one undo step |
| `startAdjusting(straightening: Boolean = false)`, `stopAdjusting()` | Bracket a continuous change from a control of your own: records the undo step, shows the guides |

## Rules

- **Cropping belongs to the app, not to a flow.** Write one crop screen (below) and open it from every
  place that crops, each with that place's `CropSpec`. Flows stop where cropping starts:
  `EditableAvatar`'s `onChangePhoto`, `PendingAttachmentSheet`'s `onEdit`.
- **Define specs once**, as top-level values or `copy`s of the presets. A spec that compares unequal
  starts a fresh crop; give a custom mask a stable instance (not an inline `GenericShape`).
- **Key the state on the photo:** `rememberImageCropState(spec, key = photo)`; without it a second
  photo of the same size opens on the first one's crop and undo history.
- Use `CropShapes.Oval`, never `CircleShape`, for a round mask (`CircleShape` draws a pill on a
  non-square frame). Shape is separate from aspect: an oval over 1:1 is an avatar.
- **The photo:** an upright `Painter` (EXIF orientation applied) with an intrinsic size. On Android
  decode without hardware bitmaps (Coil `allowHardware(false)`; otherwise `render` throws
  `IllegalArgumentException`) and to at most about 4096 px on the long side for the stage. Pass the
  full-resolution painter to `state.render(fullPhoto)` when Done is pressed; it is scaled to.
  Preview and full photo must have the same proportions within 2%, or it throws.
- `render` allocates the whole bitmap (`w × h × 4` bytes) on the calling thread: cap it with
  `maxOutputDimension`, move large renders off the main thread.
- `applyMask = false` stores the rectangle, to be masked where shown (an avatar that clips anyway).
- `CropResult.render` resolves the mask at the output's size: give it a proportional mask
  (`CropShapes.Oval`, `RoundedCornerShape(percent = 12)`) or use `state.render`.
- The state and the toolbar's open tool survive rotation and process death; the photo does not —
  keep its URI or path in the screen's saved state and decode it again.
- Paint the crop screen with `ImageCropperStyle.Default.stage` and pad it by
  `WindowInsets.safeDrawing`. Over the current screen use `CropDialog` (paints the system bars,
  routes Back to the toolbar first: close a tool, ask before discarding, then `onDismissRequest`).
  As a navigation destination call foundation `SystemBars(ImageCropperStyle.Default.stage)`.
- In controls of your own, pair `startAdjusting` with `stopAdjusting` around a `CropDial` drag via
  `onAdjustStart` / `onAdjustEnd`.

## Accessibility and words

- Pass a localised `contentDescription` to `ImageCropper` and a `title` to `CropToolbar` (read as a
  heading).
- The stage takes arrow keys (Shift for further), `+` / `−`, `[` / `]`, and offers accessibility
  actions (move, zoom, widen/heighten a `Free` frame). The dial is adjustable; Mirror is a switch;
  aspect chips are radio buttons.
- Every word is in `CropToolbarLabels` (all with English defaults): `reset`, `angle`, `zoom`,
  `format`, `mirror`, `rotate`, `cancel`, `done`, `undo`, `ok`, `level`, `fit`, `swapOrientation`,
  `discardTitle`, `discardDescription`, `discard`, `keepEditing`, `free`, `original`, `moveLeft`,
  `moveRight`, `moveUp`, `moveDown`, `zoomIn`, `zoomOut`, `widenFrame`, `heightenFrame`, and the
  formatters `angleValue: (Float) -> String` (`+3.2°`), `zoomValue: (Float) -> String` (`2.5×`),
  `rotationValue: (Int) -> String` (`90°`).
- Pass the **same** labels to `CropToolbar` and `ImageCropper`, as a top-level value (an unequal
  value recomposes the toolbar).

## Styles and sizes

| Component | Style · local | Size · local |
| --- | --- | --- |
| `ImageCropper` | `ImageCropperStyle` · `LocalImageCropperStyle` · `default(stage, veil, veilActive, frame, handle, grid, focusRing)` | `ImageCropperSize` · `LocalImageCropperSize` · `Default` (constructor defaults: `inset`, `frameStroke`, `handleLength`, `minFrame`, …) |
| `CropToolbar` | `CropToolbarStyle` · `LocalCropToolbarStyle` · `default(bg, option…, chip…, title, value, valueChanged, dial)` | `CropToolbarSize` · `LocalCropToolbarSize` · `Default` (options, chips, `dial`) |
| `CropDial` | `CropDialStyle` · `LocalCropDialStyle` · `default(tick, origin, indicator, focusRing)` | `CropDialSize` · `LocalCropDialSize` · `Default` |

Defaults read the theme's dark palette. `CropToolbarStyle.default()` takes its `bg` from the provided
`LocalImageCropperStyle` stage and its `dial` from `LocalCropDialStyle`; `CropDialog`'s colour is the
provided stage too. `ArecaThemeProvider` does not seed these locals.

## The crop screen

```kotlin
@Composable
fun CropScreen(
    photo: Painter,
    spec: CropSpec,
    title: String,
    photoDescription: String,
    labels: CropToolbarLabels,
    onCancel: () -> Unit,
    onDone: (ImageBitmap) -> Unit,
    fullPhoto: Painter = photo,
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
            contentDescription = photoDescription,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            labels = labels,
        )
        CropToolbar(
            state = state,
            title = title,
            labels = labels,
            onCancel = onCancel,
            onDone = { state.render(fullPhoto)?.let(onDone) },
        )
    }
}

// The app's specs, once per place it crops
val AvatarCrop = CropSpec.Avatar
val CoverCrop = CropSpec(CropAspect.Ratio(3, 1), isAspectLocked = true, maxOutputDimension = 1500)
val PostCrop = CropSpec.Photo.copy(maxOutputDimension = 2048)

// Over the current screen
picked?.let { photo ->
    CropDialog(onDismissRequest = { picked = null }) {
        CropScreen(photo, PostCrop, cropTitle, photoDescription, appCropLabels, onCancel = { picked = null }, onDone = ::upload)
    }
}

// Controls of your own
CropDial(
    value = state.straighten,
    onValueChange = state::setStraighten,
    valueRange = -45f..45f,
    contentDescription = angleLabel,
    onAdjustStart = { state.startAdjusting(straightening = true) },
    onAdjustEnd = state::stopAdjusting,
)
IconButton(rotateIcon, rotateLabel, onClick = state::rotateLeft)

// Store the crop, or render a thumbnail
val result = state.result() ?: return
val thumbnail = result.render(photo, result.outputSize(maxDimension = 256), mask = CropShapes.Oval)
```
