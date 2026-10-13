package dev.voir.areca.flow.profile.row

import dev.voir.areca.foundation.settings.SettingsRowStatus
import kotlin.test.Test
import kotlin.test.assertEquals

/** How the data-export row reads in each state of the request. */
class DataExportPresentationTest {

    @Test
    fun `idle asks for a copy`() {
        assertEquals(
            DataExportPresentation(DataExportLine.Idle, DataExportAction.Request, SettingsRowStatus.Idle),
            DataExportPresentation.of(DataExportState.Idle),
        )
    }

    @Test
    fun `preparing shows the spinner and offers nothing`() {
        assertEquals(
            DataExportPresentation(DataExportLine.Preparing, DataExportAction.None, SettingsRowStatus.Loading),
            DataExportPresentation.of(DataExportState.Preparing),
        )
    }

    @Test
    fun `ready offers the download`() {
        assertEquals(
            DataExportPresentation(DataExportLine.Ready, DataExportAction.Download, SettingsRowStatus.Idle),
            DataExportPresentation.of(DataExportState.Ready),
        )
    }

    @Test
    fun `a failure shows its reason under the idle line and offers to try again`() {
        assertEquals(
            DataExportPresentation(
                DataExportLine.Idle,
                DataExportAction.Retry,
                SettingsRowStatus.Error("The service is down"),
            ),
            DataExportPresentation.of(DataExportState.Failed("The service is down")),
        )
    }
}
