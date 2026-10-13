package dev.voir.areca.crop

import androidx.compose.ui.input.key.Key

/**
 * Tells a key held down from separate presses of it.
 *
 * A held key repeats its key-down events without a key-up between them, on every platform. Each
 * repeat is part of one adjustment, so a crop records the hold as one step for undo, the way it
 * records one turn of a scroll wheel. Another key, a release, or any other step ends the hold.
 *
 * Not thread-safe: it belongs to one control and is driven from its key events.
 */
internal class CropKeyHold {
    /** The key whose repeats belong to the running hold, or `null` when no key is held. */
    private var held: Key? = null

    /** Whether a key is held now. */
    val isHolding: Boolean get() = held != null

    /**
     * Records a key-down event.
     *
     * @param key Key pressed.
     * @return `true` when the event starts a new adjustment: the first press of [key], or a press
     *   while another key was held. `false` for a repeat of the held key.
     */
    fun press(key: Key): Boolean {
        val starts = held != key
        held = key
        return starts
    }

    /**
     * Records a key-up event.
     *
     * @param key Key released.
     * @return `true` when it ended the running hold; `false` for a key that was not the held one.
     */
    fun release(key: Key): Boolean {
        if (held != key) return false
        held = null
        return true
    }

    /**
     * Ends the running hold without a key-up, as when focus leaves or another step begins.
     *
     * @return `true` when a hold was running.
     */
    fun clear(): Boolean {
        val wasHeld = held != null
        held = null
        return wasHeld
    }
}
