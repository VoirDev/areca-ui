package dev.voir.areca.playground.crop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.crop.CropAspect
import dev.voir.areca.crop.CropDialog
import dev.voir.areca.crop.CropShapes
import dev.voir.areca.crop.CropSpec
import dev.voir.areca.crop.ImageCropState
import dev.voir.areca.crop.ImageCropper
import dev.voir.areca.crop.rememberImageCropState
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.shape.SquircleShape
import dev.voir.areca.theme.ArecaTheme

/**
 * How one product configures the cropper.
 *
 * @property label Name in the control.
 * @property title Title of the editor's top bar.
 * @property spec What the product decides.
 */
internal enum class CropPreset(val label: String, val title: String, val spec: CropSpec) {
    /** A profile photo: a fixed circle the photo is moved and zoomed under. */
    Avatar("avatar", "Edit photo", CropSpec.Avatar),

    /** A general photo editor: every aspect, and a frame with grips. */
    Photo("photo", "Crop", CropSpec.Photo),

    /** A 3:1 banner: a fixed ratio whose frame can still be dragged smaller. */
    Cover(
        "cover",
        "Cover image",
        CropSpec(aspect = CropAspect.Ratio(3, 1), isAspectLocked = true, maxOutputDimension = 1500),
    ),

    /** A badge in the product's own outline. */
    Badge("badge", "Badge", CropSpec.Avatar.copy(mask = HexagonShape, maxOutputDimension = 1024)),
}

/**
 * Masks offered by the playground, each a plain [Shape].
 *
 * @property label Name in the control.
 * @property shape The mask, or `null` to use the preset's.
 */
private enum class MaskOption(val label: String, val shape: Shape?) {
    /** Whatever the preset uses. */
    Preset("preset", null),

    /** No mask. */
    Rectangle("rectangle", RectangleShape),

    /** Rounded corners at 12% of the shorter side. */
    Rounded("rounded", RoundedCornerShape(12)),

    /** An ellipse, a circle on a square frame. */
    Oval("oval", CropShapes.Oval),

    /** Areca's squircle. */
    Squircle("squircle", SquircleShape(0.24f)),

    /** A product's own outline. */
    Hexagon("hexagon", HexagonShape),
}

/** A pointy-topped hexagon filling its bounds: the stand-in for a product's own mask. */
private val HexagonShape: Shape = GenericShape { size, _ ->
    moveTo(size.width / 2f, 0f)
    lineTo(size.width, size.height * 0.25f)
    lineTo(size.width, size.height * 0.75f)
    lineTo(size.width / 2f, size.height)
    lineTo(0f, size.height * 0.75f)
    lineTo(0f, size.height * 0.25f)
    close()
}

/** Showcase for [ImageCropper]. */
@Composable
fun ImageCropperShowcase() = ShowcaseScaffold(
    title = "Image cropper",
    summary = "Frames a photo for an avatar, a cover or a post: the photo moves under a fixed frame, " +
        "with masks, aspect ratios, straightening, zoom, quarter turns and mirroring.",
    module = "crop",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Drag, pinch or scroll the photo and drag the frame's corners. Done renders the " +
            "crop the way the product would upload it. Open full screen shows the same screen in a " +
            "CropDialog, which paints the device's status and navigation bars with the stage.",
    ) {
        val preset by enumControl(
            key = "preset",
            default = CropPreset.Photo,
            options = CropPreset.entries,
            optionLabel = { it.label },
        )
        val mask by enumControl(
            key = "mask",
            default = MaskOption.Preset,
            options = MaskOption.entries,
            optionLabel = { it.label },
            description = "Any Shape. Only drawn by the stage; the same shape cuts the rendered result.",
        )

        preview {
            var rendered by remember { mutableStateOf<ImageBitmap?>(null) }
            var session by remember { mutableIntStateOf(0) }
            var fullScreen by remember { mutableStateOf(false) }
            val spec = mask.shape?.let { preset.spec.copy(mask = it) } ?: preset.spec
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DeviceFrame(size = DeviceSize.Regular) {
                    // Cancel starts the editor over, as reopening it from the product would.
                    key(session) {
                        CropScreen(
                            photo = SamplePhoto,
                            spec = spec,
                            title = preset.title,
                            onCancel = { session++ },
                            onDone = { rendered = it },
                            windowInsets = DeviceSize.Regular.windowInsets,
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl)) {
                    RenderedResult(rendered)
                    Button(
                        text = "Open full screen",
                        onClick = { fullScreen = true },
                        style = ButtonStyle.outlined(ActionColor.Neutral),
                        size = ButtonSize.Small,
                    )
                }
            }
            if (fullScreen) {
                // The application's presentation: over everything, with the bars in the stage's color.
                CropDialog(onDismissRequest = { fullScreen = false }) {
                    CropScreen(
                        photo = SamplePhoto,
                        spec = spec,
                        title = preset.title,
                        onCancel = { fullScreen = false },
                        onDone = {
                            rendered = it
                            fullScreen = false
                        },
                        windowInsets = WindowInsets.safeDrawing,
                    )
                }
            }
        }
    }

    Variants(
        title = "Masks",
        description = "The same square crop under each mask. The veil and outline follow the shape; " +
            "the grips stay on the frame's corners.",
        layout = VariantLayout.Grid,
    ) {
        MaskOption.entries.filter { it.shape != null }.forEach { option ->
            variant(name = option.label) { StaticCropper(mask = option.shape ?: RectangleShape) }
        }
    }

    States {
        variant("At rest") { StaticCropper() }
        variant("Adjusting", "Thirds grid, lighter veil.") { StaticCropper { startAdjusting() } }
        variant("Straightening", "Finer grid; the photo zooms just enough to keep the corners filled.") {
            StaticCropper {
                setStraighten(SampleTilt)
                startAdjusting(straightening = true)
            }
        }
        variant("Mirrored and zoomed") {
            StaticCropper {
                flipHorizontally()
                setZoom(2.5f)
            }
        }
    }

    EdgeCases {
        case("Short, wide stage", "The frame fits the height; the photo still covers it.") {
            StaticCropper(height = 140.dp, width = 360.dp)
        }
        case("Narrow stage", "A 3:1 frame fits the width and leaves the stage above and below.") {
            StaticCropper(height = 260.dp, width = 200.dp, aspect = CropAspect.Ratio(3, 1))
        }
    }
}

/**
 * The last crop Done rendered, at most 200dp, or a hint before the first.
 *
 * @param bitmap Rendered crop, or `null` before Done is pressed.
 */
@Composable
private fun RenderedResult(bitmap: ImageBitmap?) {
    Column(
        modifier = Modifier.width(ResultSide),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Text(text = "Result", style = ArecaTheme.typography.labelMedium, color = ArecaTheme.colors.fgMuted)
        if (bitmap == null) {
            Text(
                text = "Press Done to render the crop.",
                style = ArecaTheme.typography.bodySmall,
                color = ArecaTheme.colors.fgSubtle,
            )
        } else {
            Box(
                modifier = Modifier
                    .size(ResultSide)
                    .clip(ArecaTheme.shapes.md)
                    .background(ArecaTheme.colors.bgSubtle),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    bitmap = bitmap,
                    contentDescription = "Rendered crop",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
            }
            Text(
                text = "${bitmap.width} × ${bitmap.height} px",
                style = ArecaTheme.typography.bodySmall,
                color = ArecaTheme.colors.fgSubtle,
            )
        }
    }
}

/**
 * A stage alone, set up once by [setUp], for the cells below the playground.
 *
 * @param mask Shape the frame cuts.
 * @param aspect Proportions of the frame.
 * @param width Width of the stage.
 * @param height Height of the stage.
 * @param setUp Commands applied once the stage knows its photo and size.
 */
@Composable
private fun StaticCropper(
    mask: Shape = RectangleShape,
    aspect: CropAspect = CropAspect.Square,
    width: Dp = 240.dp,
    height: Dp = 240.dp,
    setUp: ImageCropState.() -> Unit = {},
) {
    val state = rememberImageCropState(CropSpec(aspect = aspect, isAspectLocked = true, mask = mask))
    LaunchedEffect(state.isReady) {
        if (state.isReady) state.setUp()
    }
    ImageCropper(
        painter = SamplePhoto,
        state = state,
        contentDescription = null,
        modifier = Modifier.width(width).height(height),
    )
}

/** Side of the rendered result's box. */
private val ResultSide = 200.dp
