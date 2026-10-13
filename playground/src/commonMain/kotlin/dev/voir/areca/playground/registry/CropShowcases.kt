package dev.voir.areca.playground.registry

import dev.voir.areca.playground.crop.CropDialShowcase
import dev.voir.areca.playground.crop.CropToolbarShowcase
import dev.voir.areca.playground.crop.ImageCropperShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus

/**
 * Component pages for `:ui:crop`, in the [ShowcaseGroup.Crop] group.
 *
 * Source order is for reading only: the sidebar sorts each section by title.
 */
internal val CropShowcases: List<ShowcaseEntry> = listOf(
    ShowcaseEntry(
        id = "crop/image-cropper",
        group = ShowcaseGroup.Crop,
        title = "Image cropper",
        summary = "Frames a photo under a fixed frame, with masks, aspect ratios, straightening, zoom, " +
            "quarter turns and mirroring.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("crop", "photo", "image", "avatar", "picture", "mask", "rotate", "straighten", "editor"),
        content = { ImageCropperShowcase() },
    ),
    ShowcaseEntry(
        id = "crop/crop-toolbar",
        group = ShowcaseGroup.Crop,
        title = "Crop toolbar",
        summary = "Reset, Angle, Zoom, Format, Mirror and Rotate over a Cancel and Done bar, for an image cropper.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("aspect ratio", "format", "rotate", "flip", "mirror", "reset", "undo", "photo editor"),
        content = { CropToolbarShowcase() },
    ),
    ShowcaseEntry(
        id = "crop/crop-dial",
        group = ShowcaseGroup.Crop,
        title = "Crop dial",
        summary = "A ruler sliding under a fixed mark, for straightening and zooming a photo.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("ruler", "straighten", "angle", "zoom", "slider", "scrubber"),
        content = { CropDialShowcase() },
    ),
)
