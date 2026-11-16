package dev.voir.areca.playground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.layout.BottomActionBar
import dev.voir.areca.foundation.navigation.PageIndicator
import dev.voir.areca.foundation.navigation.PageIndicatorStyle
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.stat.Stat
import dev.voir.areca.foundation.stat.StatGrid
import dev.voir.areca.foundation.text.Amount
import dev.voir.areca.foundation.text.AmountText
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.FavoriteAction
import dev.voir.areca.foundation.topbar.ShareAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.media.carousel.Carousel
import dev.voir.areca.media.carousel.rememberCarouselState
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

// The "Product detail" recipe from skills/areca-ui-v1/reference/recipes.md, compiled so the recipe
// cannot drift from the API. Keep the two in step.

/**
 * What a product page shows.
 *
 * @property name The product's name.
 * @property maker Who makes it, the line above the name.
 * @property price Its price, formatted by the app.
 * @property pictures Models the media image loader understands.
 * @property rating Its rating, formatted.
 * @property reviews How many reviews, formatted.
 * @property facts Label and value pairs: concentration, size, year.
 * @property favourite Whether the person has saved it.
 */
@Immutable
internal data class ProductUi(
    val name: String,
    val maker: String,
    val price: Amount,
    val pictures: List<Any>,
    val rating: String,
    val reviews: String,
    val facts: List<Pair<String, String>>,
    val favourite: Boolean,
)

/**
 * A product page: its pictures under a transparent bar, what it is, its figures, its facts, and the
 * one thing to do with it pinned to the bottom.
 *
 * The pictures run under the status bar and the bar, so the content takes only the bottom padding;
 * the bar's [TopBarStyle.Scrim] puts its controls in dark discs that read over any picture.
 *
 * @param product What to show.
 * @param onBack Leaves the page.
 * @param onShare Shares the product.
 * @param onFavouriteChange Saves or unsaves it.
 * @param onBuy The page's primary action.
 */
@Composable
internal fun ProductDetailScreen(
    product: ProductUi,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onFavouriteChange: () -> Unit,
    onBuy: () -> Unit,
) {
    val carousel = rememberCarouselState(itemCount = product.pictures.size)

    BaseLayout(
        topBar = {
            TopBar(
                title = "",
                style = TopBarStyle.Scrim,
                leading = { BackAction(onClick = onBack, contentDescription = "Back") },
                trailing = {
                    ShareAction(onClick = onShare, contentDescription = "Share")
                    FavoriteAction(selected = product.favourite, onClick = onFavouriteChange, contentDescription = "Save")
                },
            )
        },
        bottomBar = { BottomActionBar(actionText = "Add to bag", onAction = onBuy) },
    ) { padding ->
        // The hero runs under the bar: only the bottom padding is applied.
        LazyColumn(contentPadding = PaddingValues(bottom = padding.calculateBottomPadding())) {
            item(key = "hero") {
                Box {
                    Carousel(state = carousel, contentDescription = product.name) { index ->
                        MediaFrame(
                            model = product.pictures[index],
                            contentDescription = null,
                            aspectRatio = 1f,
                            // Full bleed: square corners, so the page never shows at its edges.
                            style = MediaFrameStyle.default(shape = ArecaTheme.shapes.none),
                        )
                    }
                    PageIndicator(
                        pageCount = carousel.pageCount,
                        currentPage = carousel.currentPage,
                        currentPageOffset = carousel.currentPageOffset,
                        style = PageIndicatorStyle.onImage(),
                        modifier = Modifier.align(Alignment.BottomCenter).padding(ArecaTheme.spacing.lg),
                    )
                }
            }
            item(key = "title") {
                Column(
                    modifier = Modifier.horizontalContentInsets().padding(vertical = ArecaTheme.spacing.xl),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
                ) {
                    Text(product.maker, variant = TextVariant.Overline, color = TextColor.Subtle)
                    Text(product.name, variant = TextVariant.HeadingMedium)
                    AmountText(product.price, style = ArecaTheme.typography.headingSmall)
                }
            }
            item(key = "figures") {
                StatGrid(modifier = Modifier.horizontalContentInsets(), columns = 2) {
                    Stat(label = "Rating", value = product.rating)
                    Stat(label = "Reviews", value = product.reviews)
                }
            }
            item(key = "facts") {
                Settings(modifier = Modifier.padding(top = ArecaTheme.spacing.xl)) {
                    Section(header = { Text("Details") }) {
                        product.facts.forEach { (label, value) ->
                            SettingsRow(title = { Text(label) }, value = { Text(value) })
                        }
                    }
                }
            }
        }
    }
}
