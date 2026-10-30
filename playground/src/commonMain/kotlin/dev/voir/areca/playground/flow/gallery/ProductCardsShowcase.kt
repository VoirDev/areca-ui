package dev.voir.areca.playground.flow.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.adaptive.AdaptiveGrid
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.navigation.PageIndicator
import dev.voir.areca.foundation.navigation.PageIndicatorStyle
import dev.voir.areca.foundation.navigation.PageIndicatorVariant
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Heart
import dev.voir.areca.media.carousel.Carousel
import dev.voir.areca.media.carousel.rememberCarouselState
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameSize
import dev.voir.areca.media.zoom.ZoomBehavior
import dev.voir.areca.playground.media.SamplePicture
import dev.voir.areca.playground.media.SampleScene
import dev.voir.areca.playground.media.SampleShape
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.adaptive

/**
 * A product in a list.
 *
 * @property name Its name.
 * @property price Its price, worded.
 * @property was Its price before a reduction, worded, or `null`.
 * @property badge A word on its picture and the badge it is drawn in, or `null`.
 * @property soldOut Whether it can no longer be bought.
 * @property pictures Its pictures.
 */
private data class SampleProduct(
    val name: String,
    val price: String,
    val was: String? = null,
    val badge: Pair<String, String>? = null,
    val soldOut: Boolean = false,
    val pictures: List<SamplePicture>,
)

/** The products of a "New in" list. */
private val SampleProducts = listOf(
    SampleProduct("Field Tote", "$89", badge = "New" to "brand", pictures = productPictures(10, SampleShape.Bag, 30f)),
    SampleProduct("Runner 2", "$99", was = "$129", badge = "−20%" to "danger", pictures = productPictures(20, SampleShape.Sneaker, 12f)),
    SampleProduct("Amber No. 5", "$135", pictures = productPictures(30, SampleShape.Bottle, 35f)),
    SampleProduct("Ridge Chair", "$420", badge = "Sold out" to "neutral", soldOut = true, pictures = productPictures(40, SampleShape.Chair, 160f)),
)

/**
 * A product's three pictures: from the side, the other side, and in a scene.
 *
 * @param id The first picture's id.
 * @param shape The product.
 * @param hue Its colour.
 * @return The pictures.
 */
private fun productPictures(id: Int, shape: SampleShape, hue: Float): List<SamplePicture> = listOf(
    SamplePicture(id, "Front", shape, hue),
    SamplePicture(id + 1, "Back", shape, hue, flip = true),
    SamplePicture(id + 2, "In the studio", shape, hue, scene = SampleScene.Studio, width = 900, height = 1100),
)

/** Screen page: products in a list, each with its own small carousel. */
@Composable
fun ProductCardsShowcase() = FlowShowcase(
    title = "Product cards",
    summary = "Products two across on a phone and more as the window widens, each card's pictures swiped on their own " +
        "without opening; a tap opens that product's gallery.",
    module = "flow-gallery",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "On a tablet or desktop", role = ChromeTextRole.Label)
        ChromeText(
            text = "ProductCardsRecipe lays the cards in AdaptiveGrid: two across on a phone, three on an unfolded " +
                "foldable or a tablet held upright, four from 840dp, the cards stopping at the content width on a " +
                "monitor.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val preset by enumControl(
        key = "window",
        default = WindowPreset.Phone,
        options = WindowPreset.entries,
        optionLabel = { it.label },
    )

    preview {
        GalleryWindowFrame(preset) {
            ProductCardsRecipe(products = SampleProducts, windowInsets = preset.windowInsets)
        }
    }
}

/**
 * The recipe: an adaptive grid of cards, each a carousel with dots, a badge and a favourite over it,
 * and the name and price beneath.
 *
 * @param products The products.
 * @param windowInsets The screen's insets.
 */
@Composable
private fun ProductCardsRecipe(products: List<SampleProduct>, windowInsets: WindowInsets) {
    var open by remember { mutableStateOf<Pair<SampleProduct, Int>?>(null) }
    val favourites = remember { mutableStateOf(setOf<String>()) }

    AdaptiveGrid(
        modifier = Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas),
        minCellWidth = 140.dp,
        maxColumns = adaptive(compact = 2, medium = 3, expanded = 4),
        contentPadding = PaddingValues(
            top = windowInsets.asPaddingValues().calculateTopPadding() + ArecaTheme.spacing.lg,
            bottom = windowInsets.asPaddingValues().calculateBottomPadding(),
        ),
        horizontalSpacing = ArecaTheme.spacing.md,
        verticalSpacing = ArecaTheme.spacing.xl,
    ) {
        items(products, key = { product -> product.name }) { product ->
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
                val state = rememberCarouselState(itemCount = product.pictures.size)
                Box {
                    Carousel(state = state, contentDescription = "${product.name} pictures") { index ->
                        MediaFrame(
                            model = product.pictures[index],
                            contentDescription = product.pictures[index].title,
                            aspectRatio = 4f / 5f,
                            fit = if (product.pictures[index].scene == SampleScene.Product) MediaFit.Whole else MediaFit.Fill,
                            zoom = ZoomBehavior.Peek,
                            onClick = { open = product to index },
                            onClickLabel = "Open ${product.name}",
                            size = MediaFrameSize.default(inset = ArecaTheme.spacing.lg),
                        )
                    }
                    product.badge?.let { (word, tone) ->
                        Badge(
                            modifier = Modifier.align(Alignment.TopStart).padding(ArecaTheme.spacing.sm),
                            style = when (tone) {
                                "brand" -> BadgeStyle.Brand
                                "danger" -> BadgeStyle.Danger
                                else -> BadgeStyle.NeutralDark
                            },
                            size = BadgeSize.Small,
                        ) { Text(word) }
                    }
                    val liked = product.name in favourites.value
                    IconButton(
                        imageVector = ArecaIcons.Heart,
                        contentDescription = if (liked) "Remove ${product.name} from favourites" else "Add ${product.name} to favourites",
                        onClick = { favourites.value = if (liked) favourites.value - product.name else favourites.value + product.name },
                        modifier = Modifier.align(Alignment.TopEnd).padding(ArecaTheme.spacing.xs),
                        style = IconButtonStyle.plain(if (liked) ActionColor.Danger else ActionColor.Neutral),
                        size = IconButtonSize.Small,
                    )
                    PageIndicator(
                        pageCount = state.pageCount,
                        currentPage = state.currentPage,
                        currentPageOffset = state.currentPageOffset,
                        variant = PageIndicatorVariant.Dots,
                        style = PageIndicatorStyle.onImage(),
                        modifier = Modifier.align(Alignment.BottomCenter).padding(ArecaTheme.spacing.sm),
                    )
                }
                Text(product.name, style = ArecaTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
                    Text(product.price, style = ArecaTheme.typography.bodySmall)
                    if (product.was != null) {
                        Text(
                            product.was,
                            style = ArecaTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                            color = ArecaTheme.colors.fgMuted,
                        )
                    }
                }
            }
        }
    }

    open?.let { (product, start) ->
        SampleGalleryViewer(
            pictures = product.pictures,
            start = start,
            onDismiss = { open = null },
            action = if (product.soldOut) null else "Add to bag · ${product.price}",
        )
    }
}
