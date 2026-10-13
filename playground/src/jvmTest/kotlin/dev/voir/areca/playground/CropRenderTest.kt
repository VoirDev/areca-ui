package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import dev.voir.areca.crop.CropAspect
import dev.voir.areca.crop.CropResult
import dev.voir.areca.crop.CropShapes
import dev.voir.areca.crop.CropSpec
import dev.voir.areca.crop.CropToolbar
import dev.voir.areca.crop.ImageCropState
import dev.voir.areca.crop.ImageCropper
import dev.voir.areca.crop.rememberImageCropState
import dev.voir.areca.playground.crop.CropScreen
import dev.voir.areca.playground.crop.CropPreset
import dev.voir.areca.playground.crop.SamplePhoto
import dev.voir.areca.playground.crop.SampleTilt
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

/** Logical width of the device the editor is rendered at. */
private const val WIDTH = 390

/** Logical height of the device the editor is rendered at. */
private const val HEIGHT = 760

/** Pixel density the scenes are rasterised at. */
private const val DENSITY = 2f

/** Time between the frames stepped through before the final one, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** How long the clock runs before the kept frame: past the first layout and every entrance. */
private const val SETTLE_NANOS = 1_500_000_000L

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one composition to a PNG once it has settled.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param content Composition to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark) {
            Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) { content() }
        }
    }
    try {
        var time = 0L
        while (time < SETTLE_NANOS) {
            scene.render(time)
            time += FRAME_NANOS
        }
        val data = requireNotNull(scene.render(SETTLE_NANOS).encodeToData(EncodedImageFormat.PNG))
        OUTPUT.mkdirs()
        File(OUTPUT, "$name.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/**
 * Renders the crop editor recipe for each preset to `build/screens`, and a rendered result.
 *
 * Like the other render tests it has no golden image; it fails when a configuration throws, and it
 * is how the editor is looked at on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*CropRenderTest*'
 * ```
 */
class CropRenderTest {

    /** Every preset in the light theme, and the photo preset in the dark theme, which should look the same. */
    @Test
    fun `editor renders for every preset`() {
        CropPreset.entries.forEach { preset ->
            render("crop-${preset.name.lowercase()}") { Editor(preset) }
        }
        render("crop-photo-dark", dark = true) { Editor(CropPreset.Photo) }
    }

    /** The stage mid-adjustment: straightening with its fine grid, a turned 4:3 frame, and a mirror. */
    @Test
    fun `editor states render`() {
        render("crop-state-straightening") {
            Stage {
                setStraighten(SampleTilt)
                startAdjusting(straightening = true)
            }
        }
        render("crop-state-rotated") {
            Stage {
                selectAspect(CropAspect.Ratio(4, 3))
                rotateLeft()
                setZoom(1.6f)
                startAdjusting()
            }
        }
        render("crop-state-mirrored", dark = true) {
            Stage {
                selectAspect(CropAspect.Square)
                flipHorizontally()
            }
        }
    }

    /** An oval crop of the sample, rendered to a 512px PNG with transparent corners. */
    @Test
    fun `result renders to the requested size`() {
        val result = CropResult(
            imageSize = SamplePhoto.intrinsicSize,
            center = Rect(Offset.Zero, SamplePhoto.intrinsicSize).center,
            size = Size(1000f, 1000f),
            rotation = 3.2f,
            isMirrored = false,
        )

        val bitmap = result.render(SamplePhoto, IntSize(512, 512), mask = CropShapes.Oval)

        val pixels = bitmap.asSkiaBitmap()
        assertEquals(512, bitmap.width)
        assertEquals(0, pixels.getColor(500, 500) ushr 24, "Corners outside the oval are transparent")
        assertEquals(0xFF, pixels.getColor(256, 256) ushr 24, "The centre of the oval is the photo")
        OUTPUT.mkdirs()
        val png = Image.makeFromBitmap(bitmap.asSkiaBitmap()).encodeToData(EncodedImageFormat.PNG)
        File(OUTPUT, "crop-result-oval.png").writeBytes(requireNotNull(png).bytes)
    }
}

/**
 * A stage and toolbar set up by [setUp] once the photo and size are known.
 *
 * @param setUp Commands applied to the crop.
 */
@Composable
private fun Stage(setUp: ImageCropState.() -> Unit) {
    val state = rememberImageCropState(CropSpec.Photo)
    LaunchedEffect(state.isReady) {
        if (state.isReady) state.setUp()
    }
    Column(Modifier.fillMaxSize()) {
        ImageCropper(SamplePhoto, state, contentDescription = null, modifier = Modifier.weight(1f).fillMaxWidth())
        CropToolbar(state = state, onCancel = {}, onDone = {}, title = "Crop photo")
    }
}

/**
 * The editor recipe with no system insets.
 *
 * @param preset Product configuration.
 */
@Composable
private fun Editor(preset: CropPreset) {
    CropScreen(
        photo = SamplePhoto,
        spec = preset.spec,
        title = preset.title,
        onCancel = {},
        onDone = {},
        windowInsets = WindowInsets(0, 0, 0, 0),
    )
}
