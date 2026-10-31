package dev.voir.areca.playground.registry

import dev.voir.areca.playground.flow.attachments.AttachmentAddTileShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentCroppingShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentDetailsShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentDocumentsShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentFormShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentIndicatorShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentOptionsSheetShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentRenameSheetShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentRowShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentSourceSheetShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentStripShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentTagChipsShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentThumbnailShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentTileShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentViewerPageShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentViewerShowcase
import dev.voir.areca.playground.flow.attachments.AttachmentWideDetailsShowcase
import dev.voir.areca.playground.flow.attachments.PendingAttachmentSheetShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus

/**
 * Screen and part pages for `:flow:attachments`, in the [ShowcaseGroup.AttachmentsFlow] group.
 *
 * Source order is for reading only: the sidebar sorts each section by title.
 */
internal val AttachmentsFlowShowcases: List<ShowcaseEntry> = listOf(
    ShowcaseEntry(
        id = "flow-attachments/form",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsScreens,
        title = "Form",
        summary = "A form that writes an expense, with its files in a strip under the fields, saved with the form.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachments form", "new record", "add receipt", "upload", "screen", "recipe"),
        content = { AttachmentFormShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/cropping",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsScreens,
        title = "Cropping",
        summary = "A picked photo cropped in the application's crop screen before it is attached.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("crop", "edit photo", "image cropper", "pending file", "screen", "recipe"),
        content = { AttachmentCroppingShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/details",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsScreens,
        title = "Details",
        summary = "A saved expense's details, with its files opening the viewer and every change saved at once.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachments details", "record", "transaction", "receipt", "screen", "recipe"),
        content = { AttachmentDetailsShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/tablet-desktop",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsScreens,
        title = "Tablet and desktop",
        summary = "A saved expense's details with its files in a pane beside them on a wide window, and in a sheet on a narrow one.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "attachments tablet", "desktop", "adaptive", "supporting pane", "wide window", "dialog", "screen", "recipe",
        ),
        content = { AttachmentWideDetailsShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/documents",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsScreens,
        title = "Documents",
        summary = "A record's files as a list read by name, grouped by what they are.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachments documents", "file list", "folder", "screen", "recipe"),
        content = { AttachmentDocumentsShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/thumbnail",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsTiles,
        title = "Thumbnail",
        summary = "The square a file is recognised by: the file itself where it can be drawn, its kind " +
            "of file where it cannot.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment thumbnail", "file", "preview", "image", "pdf", "download", "upload"),
        content = { AttachmentThumbnailShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/tile",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsTiles,
        title = "Tile",
        summary = "One file in a strip: its square, what it is, and optionally its size and a remove mark.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment tile", "file", "receipt", "remove", "caption"),
        content = { AttachmentTileShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/add-tile",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsTiles,
        title = "Add tile",
        summary = "The square that adds a file, shaped like the files beside it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment add tile", "add file", "upload", "plus", "attach"),
        content = { AttachmentAddTileShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/strip",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsTiles,
        title = "Strip",
        summary = "A record's files as a row of squares that scrolls sideways, on its form or its details.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment strip", "attachments field", "files", "gallery", "carousel", "limit"),
        content = { AttachmentStripShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/row",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsRows,
        title = "Row",
        summary = "One file as a settings-list row, for places where files are read by name.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment row", "file row", "document", "list", "settings"),
        content = { AttachmentRowShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/indicator",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsRows,
        title = "Indicator",
        summary = "A paperclip, and optionally how many, on something that has files attached.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment indicator", "paperclip", "has attachments", "badge", "count"),
        content = { AttachmentIndicatorShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/source-sheet",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsSheets,
        title = "Source sheet",
        summary = "The sheet that asks where a new file comes from: camera, library, files, or the product's own.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment source sheet", "picker", "camera", "gallery", "files", "add file"),
        content = { AttachmentSourceSheetShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/pending-sheet",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsSheets,
        title = "Pending file sheet",
        summary = "Asked once a file has been picked, before it is attached: the file, and what it is.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("pending attachment", "what is this", "kind", "type", "confirm", "copying", "too large"),
        content = { PendingAttachmentSheetShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/options-sheet",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsSheets,
        title = "Options sheet",
        summary = "What can be done to one attached file: change what it is, rename, share, save, remove.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment options", "actions", "menu", "share", "save", "remove", "more"),
        content = { AttachmentOptionsSheetShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/rename-sheet",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsSheets,
        title = "Rename sheet",
        summary = "Gives an attached file a new name, editing only the part before its extension.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment rename", "file name", "extension", "edit name"),
        content = { AttachmentRenameSheetShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/tag-chips",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsSheets,
        title = "Tag chips",
        summary = "What a file is, chosen in one tap from every tag the product has.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment tag chips", "kind", "type", "category", "receipt", "invoice"),
        content = { AttachmentTagChipsShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/viewer",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsViewer,
        title = "Viewer",
        summary = "A record's files one at a time, full screen: swiped, zoomed, read page by page, fetched when opened.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment viewer", "gallery", "lightbox", "preview", "pdf", "zoom", "filmstrip", "pager"),
        content = { AttachmentViewerShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-attachments/viewer-page",
        group = ShowcaseGroup.AttachmentsFlow,
        section = ShowcaseSection.AttachmentsViewer,
        title = "Viewer page",
        summary = "One file in the viewer: a zoomable picture, PDF pages, or stated when it cannot be drawn.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("attachment viewer page", "zoom", "pdf pages", "download", "offline", "open in another app"),
        content = { AttachmentViewerPageShowcase() },
    ),
)
