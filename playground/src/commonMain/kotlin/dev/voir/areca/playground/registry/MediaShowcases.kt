package dev.voir.areca.playground.registry

import dev.voir.areca.playground.media.CarouselShowcase
import dev.voir.areca.playground.media.CompareShowcase
import dev.voir.areca.playground.media.HotspotShowcase
import dev.voir.areca.playground.media.MediaFrameShowcase
import dev.voir.areca.playground.media.MediaViewerShowcase
import dev.voir.areca.playground.media.ThumbnailStripShowcase
import dev.voir.areca.playground.media.ZoomShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus

/**
 * Component pages for `:ui:media`, in the [ShowcaseGroup.Media] group.
 *
 * Source order is for reading only: the sidebar sorts each section by title.
 */
internal val MediaShowcases: List<ShowcaseEntry> = listOf(
    ShowcaseEntry(
        id = "media/media-frame",
        group = ShowcaseGroup.Media,
        title = "Media frame",
        summary = "A picture's place on the page: shape, fill, loading and failed faces, scrim, labels and press.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("image", "picture", "photo", "skeleton", "placeholder", "retry", "label", "badge", "scrim"),
        content = { MediaFrameShowcase() },
    ),
    ShowcaseEntry(
        id = "media/carousel",
        group = ShowcaseGroup.Media,
        title = "Carousel",
        summary = "Pictures swiped sideways inside a screen, one at a time, peeking, or grouped.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("slider", "gallery", "pager", "swipe", "hero", "autoplay", "loop", "arrows"),
        content = { CarouselShowcase() },
    ),
    ShowcaseEntry(
        id = "media/thumbnail-strip",
        group = ShowcaseGroup.Media,
        title = "Thumbnail strip",
        summary = "Small pictures, one per item, the selected one ringed.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("thumbnails", "filmstrip", "rail", "gallery", "picker"),
        content = { ThumbnailStripShowcase() },
    ),
    ShowcaseEntry(
        id = "media/zoom",
        group = ShowcaseGroup.Media,
        title = "Zoom",
        summary = "Pinch, double tap and pan, sticky in a viewer or peeking in a list.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("pinch", "zoomable", "peek", "double tap", "pan", "magnify"),
        content = { ZoomShowcase() },
    ),
    ShowcaseEntry(
        id = "media/media-viewer",
        group = ShowcaseGroup.Media,
        title = "Media viewer",
        summary = "Pictures full screen: swiped, zoomed, closed by a swipe down, grown out of their frame.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("viewer", "lightbox", "fullscreen", "gallery", "photo", "zoom", "transition"),
        content = { MediaViewerShowcase() },
    ),
    ShowcaseEntry(
        id = "media/hotspot",
        group = ShowcaseGroup.Media,
        title = "Hotspot",
        summary = "Markers on points of a picture that stay on their point as it zooms.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("marker", "pin", "shop the look", "tag", "annotation"),
        content = { HotspotShowcase() },
    ),
    ShowcaseEntry(
        id = "media/compare",
        group = ShowcaseGroup.Media,
        title = "Compare",
        summary = "Two pictures of the same thing: a pair, a divider, linked zoom, hold or fade.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("before after", "comparison", "slider", "divider", "side by side", "pair"),
        content = { CompareShowcase() },
    ),
)
