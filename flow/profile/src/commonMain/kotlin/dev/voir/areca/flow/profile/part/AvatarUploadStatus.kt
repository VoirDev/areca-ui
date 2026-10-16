package dev.voir.areca.flow.profile.part

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.profile.icon.AlertTriangle
import dev.voir.areca.flow.profile.rememberLastNonNull
import dev.voir.areca.foundation.AnnounceOnChange
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.UploadCloud
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.feedback.ProgressBar
import dev.voir.areca.foundation.feedback.ProgressBarSize
import dev.voir.areca.foundation.feedback.ProgressBarStyle
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.settings.SettingsTheme
import dev.voir.areca.foundation.slot.SlotRow
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.foundation.slot.firstLineCenter
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import kotlin.math.roundToInt

/** Where a change of picture has got to. */
@Immutable
public sealed interface AvatarUploadState {
    /** Nothing is being changed. */
    public data object Idle : AvatarUploadState

    /**
     * The new picture is being written.
     *
     * @property progress Fraction written, from 0 to 1, or `null` when the upload cannot report
     *   one — the bar then runs indeterminately and no percentage is drawn. A value that is not a
     *   finite number — `0f / 0f` from an upload of unknown size — is drawn the same way as `null`.
     */
    public data class Uploading(public val progress: Float? = null) : AvatarUploadState

    /**
     * The change stopped before it finished, and the old picture is still the account's.
     *
     * @property message Why, already worded — the reasons an upload fails are the service's.
     */
    public data class Failed(public val message: String) : AvatarUploadState
}

/**
 * What a change of picture is doing, when it is doing anything.
 *
 * Collapses to nothing while [AvatarUploadState.Idle], gap above it included, rather than reserving
 * a slot: an upload is the exception on an edit screen, and a permanently empty band above the form
 * would cost more than the movement it saves.
 *
 * The band's heading and the line under it are announced to assistive technology when the band
 * appears and when they change, so the failure is heard when it replaces the upload. The percentage
 * and the bar are not: an announcement on every tick of the upload would drown out everything else.
 *
 * The bar is filled with the inverted neutral, like every other mark in the module, rather than with
 * the brand.
 *
 * @param state Where the change has got to.
 * @param onRetry Called when a failed upload is asked to try again.
 * @param onDismiss Called when a failed upload is dismissed.
 * @param modifier Modifier applied to the band.
 * @param uploadingTitle Heading while the picture uploads.
 * @param uploadingSubtitle Line under [uploadingTitle], or `null` for none.
 * @param formatProgress Writes the percentage at the band's end from a whole number, 0–100, or
 *   `null` to show the bar alone. Composable, so it can read the application's string resources.
 * @param failedTitle Heading after a failure; the failure's message goes under it.
 * @param retryLabel Label of the button that tries again.
 * @param dismissLabel Label of the button that gives up.
 */
@Composable
public fun AvatarUploadStatus(
    state: AvatarUploadState,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    uploadingTitle: String = "Uploading photo",
    uploadingSubtitle: String? = null,
    formatProgress: (@Composable (percent: Int) -> String)? = { "$it%" },
    failedTitle: String = "Photo not updated",
    retryLabel: String = "Try again",
    dismissLabel: String = "Dismiss",
) {
    val active = state.takeUnless { it is AvatarUploadState.Idle }
    // Held over so the band does not empty itself halfway through its own exit animation.
    val shown = rememberLastNonNull(active)

    // Keyed on what is in force rather than on what is drawn: nothing is said as the band leaves.
    AnnounceOnChange(
        uploadAnnouncement(
            state = state,
            uploadingTitle = uploadingTitle,
            uploadingSubtitle = uploadingSubtitle,
            failedTitle = failedTitle,
        ),
    )

    AnimatedVisibility(
        visible = active != null,
        modifier = modifier,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        Column(
            modifier = Modifier
                .padding(top = ArecaTheme.spacing.xl2)
                .fillMaxWidth()
                .background(SettingsTheme.styles.section.bg, SettingsTheme.styles.section.shape)
                .padding(ArecaTheme.spacing.xl),
        ) {
            val current = shown ?: AvatarUploadState.Idle
            val drawnFailure = current as? AvatarUploadState.Failed
            val progress = (current as? AvatarUploadState.Uploading)?.progress?.takeIf { it.isFinite() }
            val percent = uploadPercent(progress)

            StatusHeading(
                failed = drawnFailure != null,
                title = if (drawnFailure != null) failedTitle else uploadingTitle,
                message = drawnFailure?.message ?: uploadingSubtitle,
                end = if (drawnFailure == null && percent != null && formatProgress != null) {
                    { ProgressLabel(formatProgress(percent)) }
                } else {
                    null
                },
            )

            Spacer(Modifier.height(ArecaTheme.spacing.lg))

            when (current) {
                is AvatarUploadState.Uploading -> ProgressBar(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth(),
                    style = ProgressBarStyle.default(
                        track = ArecaTheme.colors.bgMuted,
                        fill = ArecaTheme.colors.bgContrast,
                    ),
                    size = ProgressBarSize.Small,
                )

                is AvatarUploadState.Failed -> FailedActions(
                    retryLabel = retryLabel,
                    dismissLabel = dismissLabel,
                    onRetry = onRetry,
                    onDismiss = onDismiss,
                )

                AvatarUploadState.Idle -> Unit
            }
        }
    }
}

/**
 * The band's first row: its glyph, the heading and the line under it, and anything at the end.
 *
 * @param failed Whether the band reports a failure, which draws the warning glyph in the danger
 *   tone instead of the upload glyph.
 * @param title Heading.
 * @param message Line under the heading, or `null` for none.
 * @param end Drawn at the row's end — the percentage — or `null` for nothing.
 */
@Composable
private fun StatusHeading(
    failed: Boolean,
    title: String,
    message: String?,
    end: (@Composable () -> Unit)?,
) {
    // Set in the settings row's own roles and aligned like a row, because it sits on a section's
    // card beside rows and should read as one of them.
    val rowSize = SettingsTheme.sizes.row
    val rowStyle = SettingsTheme.styles.row

    SlotRow(
        modifier = Modifier.fillMaxWidth(),
        layout = SlotRowLayout.Auto.copy(gap = rowSize.leadingGap),
        start = {
            FeaturedIcon(
                imageVector = if (failed) BundledIcons.AlertTriangle else BundledIcons.UploadCloud,
                contentDescription = null,
                size = FeaturedIconSize.Small,
                shape = ArecaTheme.shapes.md,
                style = FeaturedIconStyle.soft(if (failed) ActionColor.Danger else ActionColor.Neutral),
            )
        },
        center = {
            Column(verticalArrangement = Arrangement.spacedBy(rowSize.textGap)) {
                Text(
                    text = title,
                    modifier = Modifier.firstLineCenter(rowSize.titleTextStyle),
                    style = rowSize.titleTextStyle,
                    color = rowStyle.title,
                )
                if (message != null) {
                    Text(
                        text = message,
                        style = rowSize.subtitleTextStyle,
                        color = rowStyle.subtitle,
                    )
                }
            }
        },
        end = end?.let { slot -> { slot() } },
    )
}

/**
 * The percentage at the end of the uploading band.
 *
 * @param text The percentage, already worded.
 */
@Composable
private fun ProgressLabel(text: String) {
    Text(
        text = text,
        style = SettingsTheme.sizes.row.valueTextStyle,
        color = SettingsTheme.styles.row.value,
    )
}

/**
 * The two things to do about a failed upload.
 *
 * @param retryLabel Label of the retry button.
 * @param dismissLabel Label of the dismiss button.
 * @param onRetry Called to try the same picture again.
 * @param onDismiss Called to give up on it.
 */
@Composable
private fun FailedActions(
    retryLabel: String,
    dismissLabel: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        Button(
            style = ButtonStyle.solid(ActionColor.NeutralDark),
            size = ButtonSize.Small,
            onClick = onRetry,
        ) {
            Text(retryLabel)
        }

        Button(
            style = ButtonStyle.soft(ActionColor.Neutral),
            size = ButtonSize.Small,
            onClick = onDismiss,
        ) {
            Text(dismissLabel)
        }
    }
}

/**
 * The whole percentage an upload's fraction is drawn as.
 *
 * @param progress Fraction written, or `null` when unknown. Clamped into 0–1 first.
 * @return The percentage, 0–100, or `null` when [progress] is `null` or not a finite number — the
 *   band then draws no percentage.
 */
internal fun uploadPercent(progress: Float?): Int? =
    progress?.takeIf { it.isFinite() }?.let { (it.coerceIn(0f, 1f) * 100).roundToInt() }

/**
 * What the band says to assistive technology in one state: its heading and the line under it.
 *
 * The progress is left out on purpose, so the sentence holds still while an upload runs and is
 * spoken once rather than on every tick.
 *
 * @param state Where the change has got to.
 * @param uploadingTitle Heading while the picture uploads.
 * @param uploadingSubtitle Line under [uploadingTitle], or `null` for none.
 * @param failedTitle Heading after a failure.
 * @return The sentence, or `null` while [AvatarUploadState.Idle], when there is nothing to say.
 */
internal fun uploadAnnouncement(
    state: AvatarUploadState,
    uploadingTitle: String,
    uploadingSubtitle: String?,
    failedTitle: String,
): String? = when (state) {
    AvatarUploadState.Idle -> null
    is AvatarUploadState.Uploading -> listOfNotNull(uploadingTitle, uploadingSubtitle).joinToString(". ")
    is AvatarUploadState.Failed -> "$failedTitle. ${state.message}"
}
