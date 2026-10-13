package dev.voir.areca.foundation.field

/**
 * What a field is currently reporting about its value.
 *
 * This is the outcome of validation, not the interaction state: focus, hover and the disabled
 * and read-only flags are tracked separately and resolved against the status by [Field].
 */
public enum class FieldStatus {
    /** Nothing to report. */
    Default,

    /** The value is not acceptable and the field cannot be submitted as it stands. */
    Error,

    /** The value is accepted but worth a second look. */
    Warning,

    /** The value was checked and is good — a confirmation worth drawing. */
    Success,
}
