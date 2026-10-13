package dev.voir.areca.playground.showcase.control

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

/**
 * One editable prop in a playground.
 *
 * A spec is registered once per key and holds the live [MutableState] the page reads, so the control
 * panel renders from the spec while the preview reads the same state directly.
 */
@Stable
sealed interface ControlSpec {
    /** Stable identity for the control. Also the default label. */
    val key: String

    /** Name shown in the control panel. */
    val label: String

    /** Optional one-line hint under the label. */
    val description: String?

    /** Restores the control to the value the page declared. */
    fun reset()
}

/** A control rendered as a switch. */
@Stable
class BooleanControlSpec internal constructor(
    override val key: String,
    override val label: String,
    override val description: String?,
    internal val state: MutableState<Boolean>,
    private val default: Boolean,
) : ControlSpec {
    override fun reset() {
        state.value = default
    }
}

/** A control rendered as a single-line text field. */
@Stable
class TextControlSpec internal constructor(
    override val key: String,
    override val label: String,
    override val description: String?,
    internal val state: MutableState<String>,
    internal val placeholder: String,
    private val default: String,
) : ControlSpec {
    override fun reset() {
        state.value = default
    }
}

/** A control rendered as a value with step buttons. */
@Stable
class NumberControlSpec internal constructor(
    override val key: String,
    override val label: String,
    override val description: String?,
    internal val state: MutableState<Float>,
    internal val range: ClosedFloatingPointRange<Float>,
    internal val step: Float,
    internal val format: (Float) -> String,
    private val default: Float,
) : ControlSpec {
    internal fun nudge(direction: Int) {
        state.value = (state.value + step * direction).coerceIn(range)
    }

    override fun reset() {
        state.value = default
    }
}

/** A control rendered as a button that fires an event into the preview. */
@Stable
class ActionControlSpec internal constructor(
    override val key: String,
    override val label: String,
    override val description: String?,
    internal val buttonLabel: String,
    internal val onInvoke: () -> Unit,
) : ControlSpec {
    /** An action has no value, so there is nothing to restore. */
    override fun reset() = Unit
}

/** A control rendered as a segmented picker over a fixed set of options. */
@Stable
class EnumControlSpec<T> internal constructor(
    override val key: String,
    override val label: String,
    override val description: String?,
    internal val state: MutableState<T>,
    internal val options: List<T>,
    internal val optionLabel: (T) -> String,
    private val default: T,
) : ControlSpec {
    override fun reset() {
        state.value = default
    }
}

/**
 * Receiver for a playground body.
 *
 * Controls declare themselves by calling the `*Control` functions, which register a [ControlSpec]
 * the first time they run and afterwards return the same live state. Because the body composes
 * inside the preview canvas and the control panel renders after it, the panel always sees a complete
 * set of specs — no deferred pass, no first-frame gap.
 */
@Stable
class PlaygroundScope internal constructor() {

    internal val specs = mutableStateListOf<ControlSpec>()

    /**
     * Control state outlives recomposition because the scope itself is remembered. A plain map is
     * enough; the values are snapshot state, so reads inside composition still track correctly.
     */
    private val states = mutableMapOf<String, MutableState<*>>()

    /**
     * A boolean prop, rendered as a switch.
     *
     * @param key Stable identity for the control.
     * @param default Value the control starts at and resets to.
     * @param label Name shown in the panel; defaults to [key].
     * @param description Optional one-line hint.
     */
    fun booleanControl(
        key: String,
        default: Boolean,
        label: String = key,
        description: String? = null,
    ): MutableState<Boolean> {
        val state = stateFor(key, default)
        register(key) { BooleanControlSpec(key, label, description, state, default) }
        return state
    }

    /**
     * A string prop, rendered as a text field.
     *
     * @param key Stable identity for the control.
     * @param default Value the control starts at and resets to.
     * @param label Name shown in the panel; defaults to [key].
     * @param placeholder Shown while the field is empty.
     * @param description Optional one-line hint.
     */
    fun textControl(
        key: String,
        default: String,
        label: String = key,
        placeholder: String = "",
        description: String? = null,
    ): MutableState<String> {
        val state = stateFor(key, default)
        register(key) { TextControlSpec(key, label, description, state, placeholder, default) }
        return state
    }

    /**
     * A prop with a fixed set of values, rendered as a segmented picker.
     *
     * @param key Stable identity for the control.
     * @param default Value the control starts at and resets to.
     * @param options Selectable values, in display order.
     * @param label Name shown in the panel; defaults to [key].
     * @param optionLabel Renders one option as its button text.
     * @param description Optional one-line hint.
     */
    fun <T> enumControl(
        key: String,
        default: T,
        options: List<T>,
        label: String = key,
        optionLabel: (T) -> String = { it.toString() },
        description: String? = null,
    ): MutableState<T> {
        val state = stateFor(key, default)
        register(key) {
            EnumControlSpec(key, label, description, state, options, optionLabel, default)
        }
        return state
    }

    /**
     * A numeric prop, rendered as a value with step buttons.
     *
     * @param key Stable identity for the control.
     * @param default Value the control starts at and resets to.
     * @param range Bounds the value is clamped to.
     * @param step Amount one press moves the value by.
     * @param label Name shown in the panel; defaults to [key].
     * @param format Renders the value for display.
     * @param description Optional one-line hint.
     */
    fun numberControl(
        key: String,
        default: Float,
        range: ClosedFloatingPointRange<Float>,
        step: Float,
        label: String = key,
        format: (Float) -> String = { it.toString() },
        description: String? = null,
    ): MutableState<Float> {
        val state = stateFor(key, default)
        register(key) {
            NumberControlSpec(key, label, description, state, range, step, format, default)
        }
        return state
    }

    /**
     * A control that does something rather than holding a value — open a modal, push a toast,
     * scroll a list. The only way to drive an imperative surface from the panel.
     *
     * @param key Stable identity for the control.
     * @param buttonLabel Text on the button.
     * @param label Name shown in the panel; defaults to [key].
     * @param description Optional one-line hint.
     * @param onInvoke Invoked when the button is pressed.
     */
    fun actionControl(
        key: String,
        buttonLabel: String,
        label: String = key,
        description: String? = null,
        onInvoke: () -> Unit,
    ) {
        register(key) { ActionControlSpec(key, label, description, buttonLabel, onInvoke) }
    }

    /**
     * The live instance under inspection. Call once per playground.
     *
     * @param content The component, wired to the control values declared above.
     */
    @Composable
    fun preview(content: @Composable () -> Unit) {
        content()
    }

    /** Restores every control to its declared value. */
    internal fun resetAll() {
        specs.forEach(ControlSpec::reset)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> stateFor(key: String, default: T): MutableState<T> =
        states.getOrPut(key) { mutableStateOf(default) } as MutableState<T>

    private inline fun register(key: String, build: () -> ControlSpec) {
        if (specs.none { it.key == key }) {
            specs.add(build())
        }
    }
}
