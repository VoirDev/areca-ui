package dev.voir.areca.foundation.field.select

/**
 * One choice offered by a select control.
 *
 * [key] and [value] are separate on purpose. [value] is what your state holds and what selection
 * is compared by, so it has to be something equality is meaningful on; [key] only has to be
 * unique within the list, and exists so lazy lists keep item identity when [value] is a type
 * whose equality is expensive or whose instances are replaced wholesale on every reload.
 *
 * @param T Type of the value this option carries.
 * @property key Stable identity within the option list, used for lazy-list keys.
 * @property value Value written to your state when this option is chosen. Selection is decided
 *   by equality against it, so two options must not share one.
 * @property label Human-readable text for the option, already localized.
 * @property enabled Whether the option can be chosen. A disabled option is still listed — it
 *   explains an absence that hiding it would not.
 */
public data class SelectOption<T>(
    public val key: Any,
    public val value: T,
    public val label: String,
    public val enabled: Boolean = true,
)
