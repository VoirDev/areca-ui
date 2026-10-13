package dev.voir.areca.foundation.settings

import androidx.compose.runtime.Immutable

/**
 * Progress of the change a settings row last asked for.
 *
 * Most settings save somewhere — a server, a keychain, a system permission — and the row that
 * started the change is the right place to say how it went. A row keeps its layout in every
 * status, so the list never jumps while a change settles.
 */
@Immutable
public sealed interface SettingsRowStatus {
    /** Nothing in flight. The row shows its value and accepts input. */
    public data object Idle : SettingsRowStatus

    /**
     * A change is being applied. The row shows a spinner in place of its value, keeps its control
     * visible but still, and ignores presses until the status moves on.
     */
    public data object Loading : SettingsRowStatus

    /**
     * The last change failed. The row accepts input again and shows [message] under its text.
     *
     * @property message What went wrong and, ideally, what to do about it.
     */
    public data class Error(public val message: String) : SettingsRowStatus
}
