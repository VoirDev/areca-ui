package dev.voir.areca.flow.profile.session

import androidx.compose.runtime.Immutable

/** The kind of device a session is signed in on, which decides the glyph beside it. */
public enum class SessionDevice {
    /** A handheld phone. */
    Phone,

    /** A tablet. */
    Tablet,

    /** A desktop or laptop application. */
    Desktop,

    /** A web browser. */
    Browser,

    /** Anything the service could not identify. */
    Unknown,
}

/**
 * One device the account is signed in on, already worded.
 *
 * @property id Identifier handed back when the session is revoked. Opaque to the module.
 * @property device Kind of device, for the glyph.
 * @property name What the device is called, such as "MacBook Pro".
 * @property client What the session runs, such as "Desktop 7.2.9 arm64" or "Safari on macOS", or
 *   `null` for none.
 * @property seen Where and when it was last active, such as "Belgrade, Serbia · 10:18" or
 *   "online", or `null` for none. Worded by the application, which knows the clock and the locale.
 * @property isCurrent Whether this is the session the application itself is signed in with. It is
 *   listed apart, and offers nothing to revoke: ending it is signing out, which does more than a
 *   revoke.
 * @property isRevoking Whether a revoke of this session is in flight.
 */
@Immutable
public data class SessionItem(
    public val id: String,
    public val device: SessionDevice,
    public val name: String,
    public val client: String? = null,
    public val seen: String? = null,
    public val isCurrent: Boolean = false,
    public val isRevoking: Boolean = false,
)

/** What the active-sessions section draws. */
@Immutable
public sealed interface ActiveSessionsState {
    /** The list has not arrived yet. */
    public data object Loading : ActiveSessionsState

    /**
     * The list could not be fetched.
     *
     * @property message Why, already worded.
     */
    public data class Failed(public val message: String) : ActiveSessionsState

    /**
     * The list.
     *
     * @property sessions Every session, the current one included, in the order to draw them.
     * @property isSigningOutOthers Whether ending every other session is in flight: the row that
     *   asked shows its spinner, and the other sessions stop accepting presses. For an application
     *   that ends them without a sheet, or closes `SignOutOthersSheet` on confirm; one that keeps the
     *   sheet up while the work runs shows `confirming` there instead.
     */
    public data class Loaded(
        public val sessions: List<SessionItem>,
        public val isSigningOutOthers: Boolean = false,
    ) : ActiveSessionsState
}
