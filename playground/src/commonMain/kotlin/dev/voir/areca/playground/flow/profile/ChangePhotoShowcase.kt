package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import dev.voir.areca.crop.CropSpec
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.part.AvatarUploadState
import dev.voir.areca.flow.profile.part.AvatarUploadStatus
import dev.voir.areca.flow.profile.part.EditableAvatar
import dev.voir.areca.flow.profile.sheet.AvatarSourceOptions
import dev.voir.areca.flow.profile.sheet.AvatarSourceSheet
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.playground.crop.CropScreen
import dev.voir.areca.playground.crop.SamplePhoto
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Changing the profile picture: pick, crop, upload. */
@Composable
fun ChangePhotoShowcase() = FlowShowcase(
    title = "Change photo",
    summary = "The avatar's source sheet, the application's crop screen opened with CropSpec.Avatar, " +
        "and the upload band, as one sequence.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "ChangePhotoRecipe in this file is the sequence. The module reports intents only — the " +
                "avatar was tapped, the library was chosen — and the application picks the photo, " +
                "opens its own crop screen from :ui:crop, and uploads the result. flow-profile never " +
                "depends on the cropper.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "The crop is rendered square and unmasked: EditableAvatar clips it to a circle where " +
                "it is shown, so the stored picture works in any avatar shape. Tap the avatar, choose " +
                "a source, frame the photo and press Done.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val camera by booleanControl("camera available", default = true)
    val fails by booleanControl("upload fails", default = false)

    preview {
        val scope = rememberCoroutineScope()
        val photo = remember(scope) { SamplePhotoChange(scope) }
        photo.uploadFails = fails

        DeviceFrame(size = DeviceSize.Regular) {
            ChangePhotoRecipe(
                photo = photo,
                cameraAvailable = camera,
                windowInsets = DeviceSize.Regular.windowInsets,
            )
        }
    }
}

/**
 * The recipe: the avatar, its source sheet, the application's crop screen and the upload band.
 *
 * @param photo The sample application's picture change.
 * @param cameraAvailable Whether this platform can take a photo.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun ChangePhotoRecipe(
    photo: SamplePhotoChange,
    cameraAvailable: Boolean,
    windowInsets: WindowInsets,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ArecaTheme.colors.bgCanvas)
                .windowInsetsPadding(windowInsets)
                // On a tablet or a desktop the column stops at a readable width, centred; a phone is narrower.
                .wrapContentWidth()
                .widthIn(max = AdaptiveDefaults.ReadableWidth)
                .fillMaxWidth(),
        ) {
            Spacer(Modifier.height(ArecaTheme.spacing.xl3))

            EditableAvatar(
                summary = photo.summary,
                busy = photo.upload is AvatarUploadState.Uploading,
                onChangePhoto = photo::chooseSource,
                changePhotoLabel = "Change photo",
                modifier = Modifier.horizontalContentInsets(),
            )

            AvatarUploadStatus(
                state = photo.upload,
                onRetry = photo::retry,
                onDismiss = photo::dismissFailure,
                uploadingSubtitle = "Keep the app open until it finishes.",
                modifier = Modifier.horizontalContentInsets(),
            )
        }

        // The application's crop screen, over the screen while a picked photo is being framed. A
        // product with navigation opens it as a destination instead.
        if (photo.cropping) {
            CropScreen(
                photo = SamplePhoto,
                spec = CropSpec.Avatar,
                title = "Move and scale",
                onCancel = photo::cancelCrop,
                onDone = photo::upload,
                windowInsets = windowInsets,
                // EditableAvatar clips the picture to its own circle; store the square.
                applyMask = false,
            )
        }
    }

    AvatarSourceSheet(
        visible = photo.choosingSource,
        options = buildList {
            if (cameraAvailable) add(AvatarSourceOptions.camera(onClick = photo::pick))
            add(AvatarSourceOptions.library(onClick = photo::pick))
            if (photo.summary.avatar != null) add(AvatarSourceOptions.remove(onClick = photo::remove))
        },
        onDismiss = photo::dismissSource,
        subtitle = "Choose where your new photo comes from.",
    )
}

/**
 * Stands in for the application's side of a picture change: which step is up, the upload, and the
 * account's picture.
 *
 * @param scope Scope the simulated upload runs in.
 */
private class SamplePhotoChange(private val scope: CoroutineScope) {

    /** Who is signed in, with the current picture. */
    var summary: ProfileSummary by mutableStateOf(ProfileSamples.summary())
        private set

    /** Whether the source sheet is up. */
    var choosingSource: Boolean by mutableStateOf(false)
        private set

    /** Whether the crop screen is up. */
    var cropping: Boolean by mutableStateOf(false)
        private set

    /** What the upload is doing. */
    var upload: AvatarUploadState by mutableStateOf(AvatarUploadState.Idle)
        private set

    /** Whether the next upload fails, as a service sometimes does. */
    var uploadFails: Boolean = false

    /** The crop waiting to be uploaded, kept for a retry. */
    private var lastCrop: ImageBitmap? = null

    /** The running upload, if any. */
    private var uploadJob: Job? = null

    /** The avatar was tapped. */
    fun chooseSource() {
        choosingSource = true
    }

    /** The source sheet was dismissed. */
    fun dismissSource() {
        choosingSource = false
    }

    /** A source was chosen and the platform picker returned a photo: frame it. */
    fun pick() {
        choosingSource = false
        cropping = true
    }

    /** The crop screen was left without a crop. */
    fun cancelCrop() {
        cropping = false
    }

    /** The picture was removed. */
    fun remove() {
        choosingSource = false
        summary = summary.copy(avatar = null)
    }

    /**
     * Uploads a crop, then makes it the account's picture.
     *
     * @param crop The rendered crop.
     */
    fun upload(crop: ImageBitmap) {
        cropping = false
        lastCrop = crop
        uploadJob?.cancel()
        uploadJob = scope.launch {
            for (step in 1..UploadSteps) {
                upload = AvatarUploadState.Uploading(progress = step / UploadSteps.toFloat())
                delay(StepMillis)
                if (uploadFails && step == UploadSteps / 2) {
                    upload = AvatarUploadState.Failed("The connection dropped before the photo was saved.")
                    return@launch
                }
            }
            summary = summary.copy(avatar = crop)
            upload = AvatarUploadState.Idle
        }
    }

    /** Uploads the last crop again. */
    fun retry() {
        lastCrop?.let(::upload)
    }

    /** Dismisses a failed upload, keeping the old picture. */
    fun dismissFailure() {
        upload = AvatarUploadState.Idle
    }

    private companion object {
        /** Steps of the simulated upload. */
        const val UploadSteps = 10

        /** Time between those steps. */
        const val StepMillis = 140L
    }
}
