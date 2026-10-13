package dev.voir.areca.flow.profile.row

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.Download
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.text.Text

/** Where a request for a copy of the account's data has got to. */
@Immutable
public sealed interface DataExportState {
    /** Nothing has been asked for, or the last copy has expired. */
    public data object Idle : DataExportState

    /** Asked for, and being put together. Usually takes long enough to leave the screen. */
    public data object Preparing : DataExportState

    /** Put together and waiting to be downloaded. */
    public data object Ready : DataExportState

    /**
     * Could not be put together.
     *
     * @property message Why, already worded.
     */
    public data class Failed(public val message: String) : DataExportState
}

/**
 * "Download your data": one row that walks through asking for a copy and collecting it.
 *
 * The row is the control in every state that has one — asking when idle, collecting when ready,
 * asking again after a failure — and its value names what pressing does. While the copy is prepared
 * the row shows the settings list's spinner and ignores presses; a failure shows its reason under
 * the row, the way every settings row reports one.
 *
 * @param state Where the request has got to.
 * @param onRequest Called to ask for a copy, and to ask again after a failure.
 * @param onDownload Called to collect a ready copy.
 * @param modifier Modifier applied to the row.
 * @param title Title of the row.
 * @param idleSubtitle Line under the title before a copy is asked for, and after a failure, or `null`
 *   for none.
 * @param preparingSubtitle Line while the copy is put together, or `null` for none.
 * @param readySubtitle Line once the copy can be collected, or `null` for none.
 * @param requestLabel Value before a copy is asked for, naming what pressing does.
 * @param downloadLabel Value once the copy is ready.
 * @param retryLabel Value after a failure.
 * @param onClickLabel What pressing the row does, for assistive technology, or `null` for none —
 *   the value already names it.
 * @param enabled Whether the row accepts input.
 */
@Composable
public fun DataExportRow(
    state: DataExportState,
    onRequest: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Download your data",
    idleSubtitle: String? = null,
    preparingSubtitle: String? = null,
    readySubtitle: String? = null,
    requestLabel: String = "Request",
    downloadLabel: String = "Download",
    retryLabel: String = "Try again",
    onClickLabel: String? = null,
    enabled: Boolean = true,
) {
    val presentation = DataExportPresentation.of(state)

    val subtitle = when (presentation.line) {
        DataExportLine.Idle -> idleSubtitle
        DataExportLine.Preparing -> preparingSubtitle
        DataExportLine.Ready -> readySubtitle
    }

    SettingsRow(
        title = { Text(title) },
        subtitle = subtitle?.let { line -> { Text(line) } },
        value = when (presentation.action) {
            DataExportAction.Request -> ({ Text(requestLabel) })
            DataExportAction.Download -> ({ Text(downloadLabel) })
            DataExportAction.Retry -> ({ Text(retryLabel) })
            DataExportAction.None -> null
        },
        modifier = modifier,
        leading = { SettingsRowIcon(BundledIcons.Download) },
        status = presentation.status,
        enabled = enabled,
        onClick = when (presentation.action) {
            DataExportAction.Download -> onDownload
            // Preparing keeps `onRequest`: the row's loading status is what refuses the press.
            DataExportAction.Request, DataExportAction.Retry, DataExportAction.None -> onRequest
        },
        onClickLabel = onClickLabel,
    )
}

/** Which of its lines a [DataExportRow] shows under the title. */
internal enum class DataExportLine {
    /** The line before a copy is asked for, also shown after a failure. */
    Idle,

    /** The line while the copy is put together. */
    Preparing,

    /** The line once the copy can be collected. */
    Ready,
}

/** What pressing a [DataExportRow] does, as its value names it. */
internal enum class DataExportAction {
    /** Asks for a copy. */
    Request,

    /** Collects the ready copy. */
    Download,

    /** Asks for a copy again after a failure. */
    Retry,

    /** Nothing while the copy is put together; the row shows its spinner instead of a value. */
    None,
}

/**
 * How a [DataExportRow] reads in one [DataExportState].
 *
 * @property line Which line goes under the title.
 * @property action What pressing does, named by the row's value.
 * @property status The row's progress: the spinner while preparing, the reason after a failure.
 */
internal data class DataExportPresentation(
    val line: DataExportLine,
    val action: DataExportAction,
    val status: SettingsRowStatus,
) {
    /** Construction from the state. */
    companion object {
        /**
         * Maps a state onto the row.
         *
         * @param state Where the request has got to.
         * @return How the row reads.
         */
        fun of(state: DataExportState): DataExportPresentation = when (state) {
            DataExportState.Idle ->
                DataExportPresentation(DataExportLine.Idle, DataExportAction.Request, SettingsRowStatus.Idle)

            DataExportState.Preparing ->
                DataExportPresentation(DataExportLine.Preparing, DataExportAction.None, SettingsRowStatus.Loading)

            DataExportState.Ready ->
                DataExportPresentation(DataExportLine.Ready, DataExportAction.Download, SettingsRowStatus.Idle)

            is DataExportState.Failed ->
                DataExportPresentation(DataExportLine.Idle, DataExportAction.Retry, SettingsRowStatus.Error(state.message))
        }
    }
}
