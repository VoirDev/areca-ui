package dev.voir.areca.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.unit.Density
import dev.voir.areca.foundation.collection.ContentListGroup
import dev.voir.areca.foundation.collection.PagedContentListState
import dev.voir.areca.foundation.text.Amount
import dev.voir.areca.media.LocalMediaImageLoader
import dev.voir.areca.playground.media.SampleMediaImageLoader
import dev.voir.areca.playground.media.SamplePicture
import dev.voir.areca.playground.media.SampleShape
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the screens are rendered at. */
private const val WIDTH = 390

/** Logical height of the device the screens are rendered at. */
private const val HEIGHT = 844

/** Pixel density the screens are rasterised at. */
private const val DENSITY = 2f

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders [content] to a PNG once its animations and the sample pictures have settled.
 *
 * @param name File name, without the extension.
 * @param content The screen.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, content: @Composable () -> Unit) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(inputMode = InputMode.Touch) {
            CompositionLocalProvider(LocalMediaImageLoader provides SampleMediaImageLoader(waitMillis = 0)) {
                content()
            }
        }
    }
    try {
        var time = 0L
        while (time < 1_500_000_000L) {
            scene.render(time)
            time += 50_000_000L
        }
        val data = requireNotNull(scene.render(1_500_000_000L).encodeToData(EncodedImageFormat.PNG))
        OUTPUT.mkdirs()
        File(OUTPUT, "$name.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/**
 * Renders the compiled screen recipes to `build/screens/recipe-*.png`.
 *
 * Not an assertion: it fails when a recipe throws, and it is where the recipes are looked at.
 */
class RecipesRenderTest {

    @Test
    fun `the catalogue with filters renders`() {
        render("recipe-catalogue") {
            CatalogueScreen(
                filters = CatalogueFilters(families = setOf("Woody", "Floral"), inStockOnly = true),
                draft = null,
                families = listOf("Woody", "Floral", "Citrus", "Oriental"),
                results = PagedContentListState(
                    items = listOf(
                        ContentListGroup(
                            key = "Woody",
                            group = "Woody",
                            items = List(4) { CatalogueItem("woody-$it", "Perfume ${it + 1}", "Eau de parfum · 50 ml") },
                        ),
                        ContentListGroup(
                            key = "Floral",
                            group = "Floral",
                            items = List(2) { CatalogueItem("floral-$it", "Perfume ${it + 5}", "Eau de toilette · 100 ml") },
                        ),
                    ),
                    hasNextPage = false,
                ),
                onQueryChange = {},
                onFiltersChange = {},
                onOpenFilters = {},
                onDraftChange = {},
                onApplyDraft = {},
                onDismissFilters = {},
                onLoadNextPage = {},
                onOpenItem = {},
            )
        }
    }

    @Test
    fun `the product detail renders`() {
        render("recipe-product") {
            ProductDetailScreen(
                product = ProductUi(
                    name = "Iris Twelve",
                    maker = "Maison Verte",
                    price = Amount(whole = "142", fraction = "00", symbol = "€"),
                    pictures = listOf(SamplePicture(80, "Bottle", SampleShape.Bottle), SamplePicture(81, "Box", SampleShape.Bag)),
                    rating = "4.6",
                    reviews = "1,204",
                    facts = listOf("Concentration" to "Eau de parfum", "Size" to "100 ml", "Year" to "2004"),
                    favourite = true,
                ),
                onBack = {},
                onShare = {},
                onFavouriteChange = {},
                onBuy = {},
            )
        }
    }
}
