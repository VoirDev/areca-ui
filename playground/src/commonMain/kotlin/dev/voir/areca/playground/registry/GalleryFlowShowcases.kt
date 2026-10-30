package dev.voir.areca.playground.registry

import dev.voir.areca.playground.flow.gallery.DesktopProductShowcase
import dev.voir.areca.playground.flow.gallery.GalleryPartsShowcase
import dev.voir.areca.playground.flow.gallery.GridPageShowcase
import dev.voir.areca.playground.flow.gallery.PhotoMatchShowcase
import dev.voir.areca.playground.flow.gallery.ProductCardsShowcase
import dev.voir.areca.playground.flow.gallery.ProductPageShowcase
import dev.voir.areca.playground.flow.gallery.ReviewPhotoShowcase
import dev.voir.areca.playground.flow.gallery.RowsShowcase
import dev.voir.areca.playground.flow.gallery.ShopTheLookShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus

/**
 * Screen and part pages for `:flow:gallery`, in the [ShowcaseGroup.GalleryFlow] group.
 *
 * Source order is for reading only: the sidebar sorts each section by title.
 */
internal val GalleryFlowShowcases: List<ShowcaseEntry> = listOf(
    ShowcaseEntry(
        id = "flow-gallery/product-page",
        group = ShowcaseGroup.GalleryFlow,
        section = ShowcaseSection.GalleryScreens,
        title = "Product page",
        summary = "A hero carousel that opens a detail gallery, the picture growing out of the hero; the desktop page from 840dp.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("gallery", "product", "hero", "carousel", "viewer", "pdp", "adaptive", "tablet", "foldable", "screen", "recipe"),
        content = { ProductPageShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-gallery/grid-page",
        group = ShowcaseGroup.GalleryFlow,
        section = ShowcaseSection.GalleryScreens,
        title = "Grid page",
        summary = "Every picture in its group, three across on a phone and more on wider windows, opening the viewer at a picture.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("gallery", "grid", "photos", "readers", "groups", "adaptive grid", "tablet", "screen", "recipe"),
        content = { GridPageShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-gallery/photo-match",
        group = ShowcaseGroup.GalleryFlow,
        section = ShowcaseSection.GalleryScreens,
        title = "Photo match",
        summary = "The user's photo next to the catalogue's; either opens both full screen.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("compare", "pair", "scan", "match", "screen", "recipe"),
        content = { PhotoMatchShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-gallery/parts",
        group = ShowcaseGroup.GalleryFlow,
        section = ShowcaseSection.GalleryParts,
        title = "Parts",
        summary = "A viewer's top bar and actions, caption, author, primary action, and a grid's header and tile.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("gallery top bar", "share", "save", "favourite", "caption", "author", "tile", "group header"),
        content = { GalleryPartsShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-gallery/product-cards",
        group = ShowcaseGroup.GalleryFlow,
        section = ShowcaseSection.GalleryScreens,
        title = "Product cards",
        summary = "Products two across on a phone and more on wider windows, each card swiping its own pictures and opening its gallery.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("cards", "list", "grid", "adaptive grid", "carousel", "badge", "favourite", "screen", "recipe"),
        content = { ProductCardsShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-gallery/rows",
        group = ShowcaseGroup.GalleryFlow,
        section = ShowcaseSection.GalleryScreens,
        title = "Rows",
        summary = "Rows of pictures peeking, two to a page and as round tiles, each opening a gallery.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("rows", "discover", "peek", "two up", "tiles", "screen", "recipe"),
        content = { RowsShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-gallery/review-photo",
        group = ShowcaseGroup.GalleryFlow,
        section = ShowcaseSection.GalleryScreens,
        title = "Review photo",
        summary = "A review's photos, one opening alone with whose photo it is under it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("review", "single picture", "photo", "author", "screen", "recipe"),
        content = { ReviewPhotoShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-gallery/shop-the-look",
        group = ShowcaseGroup.GalleryFlow,
        section = ShowcaseSection.GalleryScreens,
        title = "Shop the look",
        summary = "A picture with its products marked on it and listed under it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("hotspots", "look", "markers", "shoppable", "screen", "recipe"),
        content = { ShopTheLookShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-gallery/desktop-product-page",
        group = ShowcaseGroup.GalleryFlow,
        section = ShowcaseSection.GalleryScreens,
        title = "Desktop product page",
        summary = "The product page from 840dp: a thumbnail rail, the details beside, arrows and hover zoom under a pointer.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("desktop", "tablet", "rail", "arrows", "hover zoom", "adaptive", "screen", "recipe"),
        content = { DesktopProductShowcase() },
    ),
)
