package dev.voir.areca.playground.crop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.crop.CropAspect
import dev.voir.areca.crop.CropSpec
import dev.voir.areca.crop.CropToolbar
import dev.voir.areca.crop.CropToolbarLabels
import dev.voir.areca.crop.ImageCropState
import dev.voir.areca.crop.ImageCropper
import dev.voir.areca.crop.ImageCropperStyle
import dev.voir.areca.crop.rememberImageCropState
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout

/**
 * Aspect lists a product might offer.
 *
 * @property label Name in the control.
 * @property aspects The list passed to the toolbar.
 */
private enum class AspectSet(val label: String, val aspects: List<CropAspect>) {
    /** Everything a photo editor offers. */
    Photo("photo presets", CropAspect.PhotoPresets),

    /** Print ratios only. */
    Print("print ratios", listOf(CropAspect.Square, CropAspect.Ratio(4, 5), CropAspect.Ratio(3, 2))),

    /** No choice: Format is left out. */
    None("none", emptyList()),
}

/** Showcase for [CropToolbar]. */
@Composable
fun CropToolbarShowcase() = ShowcaseScaffold(
    title = "Crop toolbar",
    summary = "The controls under an image cropper: Reset, Angle, Zoom, Format, Mirror and Rotate over " +
        "a bar with Cancel and Done. Angle, Zoom and Format open in place, with Undo and OK.",
    module = "crop",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "The toolbar drives the stage above it. Open Angle, Zoom or Format: the row becomes " +
            "the tool and the bar becomes Undo, the tool's action and OK. Every word is in CropToolbarLabels.",
    ) {
        val set by enumControl(
            key = "aspects",
            default = AspectSet.Photo,
            options = AspectSet.entries,
            optionLabel = { it.label },
        )
        val locked by booleanControl(
            key = "isAspectLocked",
            default = false,
            description = "A locked aspect leaves Format out whatever list is passed.",
        )
        val title by textControl(key = "title", default = "Crop photo")
        val formatLabel by textControl(key = "labels.format", default = "Format")
        val resetLabel by textControl(key = "labels.reset", default = "Reset")

        preview {
            key(locked) {
                val state = rememberImageCropState(CropSpec(isAspectLocked = locked))
                StageWithToolbar(state) {
                    CropToolbar(
                        state = state,
                        onCancel = state::reset,
                        onDone = {},
                        title = title.ifEmpty { null },
                        aspects = set.aspects,
                        labels = CropToolbarLabels(format = formatLabel, reset = resetLabel),
                    )
                }
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Fresh", "No option carries a dot, Mirror is off and Reset is disabled.") {
            val state = rememberImageCropState()
            StageWithToolbar(state) { SampleToolbar(state) }
        }
        variant("Changed", "Angle and Format carry a dot, Mirror is filled and Reset is live.") {
            val state = rememberImageCropState()
            LaunchedEffect(state.isReady) {
                if (state.isReady) {
                    state.setStraighten(-SampleTilt)
                    state.selectAspect(CropAspect.Ratio(4, 3))
                    state.flipHorizontally()
                }
            }
            StageWithToolbar(state) { SampleToolbar(state) }
        }
    }

    EdgeCases {
        case("Narrow toolbar", "The options scroll sideways; a long name is cut short under its circle.") {
            val state = rememberImageCropState()
            StageWithToolbar(state, width = 320.dp) {
                SampleToolbar(state, labels = CropToolbarLabels(reset = "Start over"))
            }
        }
        case("Locked aspect", "Format is left out, so the row is five options.") {
            val state = rememberImageCropState(CropSpec.Avatar)
            StageWithToolbar(state) { SampleToolbar(state) }
        }
    }
}

/**
 * The toolbar with the photo presets, a title, and Cancel wired to reset the crop.
 *
 * @param state Crop the toolbar drives.
 * @param labels Words.
 */
@Composable
private fun SampleToolbar(state: ImageCropState, labels: CropToolbarLabels = CropToolbarLabels()) {
    CropToolbar(
        state = state,
        onCancel = state::reset,
        onDone = {},
        title = "Crop photo",
        aspects = CropAspect.PhotoPresets,
        labels = labels,
    )
}

/**
 * A short stage with [toolbar] under it, the way a toolbar is always seen.
 *
 * @param state Crop shared by the stage and the toolbar.
 * @param width Width of the pair.
 * @param toolbar The toolbar.
 */
@Composable
private fun StageWithToolbar(
    state: ImageCropState,
    width: Dp = 390.dp,
    toolbar: @Composable () -> Unit,
) {
    Column(modifier = Modifier.width(width).background(ImageCropperStyle.Default.stage)) {
        ImageCropper(
            painter = SamplePhoto,
            state = state,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(260.dp),
        )
        toolbar()
    }
}
