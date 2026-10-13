package dev.voir.areca.flow.profile.part

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** What the upload band says to assistive technology. */
class UploadAnnouncementTest {

    private fun announce(state: AvatarUploadState, subtitle: String? = null): String? =
        uploadAnnouncement(
            state = state,
            uploadingTitle = "Uploading photo",
            uploadingSubtitle = subtitle,
            failedTitle = "Photo not updated",
        )

    @Test
    fun `nothing is said while idle`() {
        assertNull(announce(AvatarUploadState.Idle))
    }

    @Test
    fun `an upload is announced by its heading and subtitle`() {
        assertEquals("Uploading photo", announce(AvatarUploadState.Uploading(0.2f)))
        assertEquals(
            "Uploading photo. Keep the app open",
            announce(AvatarUploadState.Uploading(0.2f), subtitle = "Keep the app open"),
        )
    }

    @Test
    fun `the announcement holds still while the progress moves`() {
        assertEquals(
            announce(AvatarUploadState.Uploading(0.1f)),
            announce(AvatarUploadState.Uploading(0.9f)),
        )
        assertEquals(announce(AvatarUploadState.Uploading(null)), announce(AvatarUploadState.Uploading(0.5f)))
    }

    @Test
    fun `a failure is announced with its reason`() {
        assertEquals(
            "Photo not updated. The file is too large",
            announce(AvatarUploadState.Failed("The file is too large")),
        )
    }
}
