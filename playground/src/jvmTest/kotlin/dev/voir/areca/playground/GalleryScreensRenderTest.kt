package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import dev.voir.areca.media.LocalMediaImageLoader
import dev.voir.areca.playground.flow.gallery.DesktopProductShowcase
import dev.voir.areca.playground.flow.gallery.GridPageShowcase
import dev.voir.areca.playground.flow.gallery.PhotoMatchShowcase
import dev.voir.areca.playground.flow.gallery.ProductCardsShowcase
import dev.voir.areca.playground.flow.gallery.ProductPageShowcase
import dev.voir.areca.playground.flow.gallery.ReviewPhotoShowcase
import dev.voir.areca.playground.flow.gallery.RowsShowcase
import dev.voir.areca.playground.flow.gallery.ShopTheLookShowcase
import dev.voir.areca.playground.media.SampleMediaImageLoader
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the image: the page with its controls beside the device. */
private const val WIDTH = 1400

/** Logical height of the image. */
private const val HEIGHT = 1000

/** Time between the frames stepped through, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** How long the clock runs before the kept frame. */
private const val SETTLE_NANOS = 1_500_000_000L

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one page to a PNG, with sample pictures that load at once.
 *
 * @param name File name, without the extension.
 * @param content The page.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, content: @Composable () -> Unit) {
    val scene = ImageComposeScene(width = WIDTH, height = HEIGHT, density = Density(1f)) {
        ArecaThemeProvider {
            CompositionLocalProvider(LocalMediaImageLoader provides SampleMediaImageLoader(waitMillis = 0)) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) { content() }
            }
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
 * Renders every gallery screen page to `build/screens`.
 *
 * Not an assertion, like the other render tests: it fails when a recipe throws during composition
 * or layout, and it is how the recipes are looked at on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*GalleryScreensRenderTest*'
 * ```
 */
class GalleryScreensRenderTest {

    /** Each gallery screen page, as the playground draws it. */
    @Test
    fun `gallery screens render`() {
        render("gallery-product-page") { ProductPageShowcase() }
        render("gallery-product-cards") { ProductCardsShowcase() }
        render("gallery-rows") { RowsShowcase() }
        render("gallery-grid-page") { GridPageShowcase() }
        render("gallery-review-photo") { ReviewPhotoShowcase() }
        render("gallery-shop-the-look") { ShopTheLookShowcase() }
        render("gallery-photo-match") { PhotoMatchShowcase() }
        render("gallery-desktop-product-page") { DesktopProductShowcase() }
    }
}
