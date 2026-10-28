package dev.voir.areca.playground.registry

/**
 * Every showcase in the playground, declared per group in the `*Showcases` files beside this one.
 *
 * The first entry is the page the playground opens on. Order otherwise carries no meaning: the
 * sidebar lays pages out by [ShowcaseGroup], then [ShowcaseSection], then title.
 */
val ShowcaseRegistry: List<ShowcaseEntry> =
    FoundationsShowcases +
        ComponentShowcases +
        ChartsShowcases +
        DateTimeShowcases +
        KeyboardShowcases +
        CropShowcases +
        MediaShowcases +
        AuthFlowShowcases +
        ProfileFlowShowcases +
        AttachmentsFlowShowcases +
        GalleryFlowShowcases
