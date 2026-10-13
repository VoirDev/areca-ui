package dev.voir.areca.flow.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * [value] while it is set, and the last value it was set to once it is `null` again.
 *
 * For a part that animates out after the caller lets go of what it shows — a sheet closing on a
 * session, a band collapsing on a failure — so the content does not empty itself halfway through
 * its own exit. The held value is recorded after each composition that applies, never during one.
 *
 * @param value Current value, or `null` once the caller has let go of it.
 * @return [value] when it is set, else the last non-null value seen, or `null` when there never
 *   was one.
 */
@Composable
internal fun <T : Any> rememberLastNonNull(value: T?): T? {
    var held by remember { mutableStateOf(value) }
    SideEffect {
        if (value != null) held = value
    }
    return value ?: held
}
