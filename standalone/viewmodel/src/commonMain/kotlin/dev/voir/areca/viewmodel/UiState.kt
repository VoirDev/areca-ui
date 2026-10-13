package dev.voir.areca.viewmodel

/**
 * Represents the top-level lifecycle of a screen.
 *
 * This is intentionally linear and simple:
 * - [Initial]: nothing has started yet
 * - [Loading]: the screen is loading
 * - [Display]: the screen has valid content to render
 * - [Error]: the screen failed and should show an error state
 *
 * Any finer-grained UI details such as:
 * - pull-to-refresh state
 * - pagination loading
 * - inline form errors
 * - empty states
 *
 * should live inside the screen-specific state [T], not here.
 *
 * @param T Screen-specific display state.
 */
public sealed interface UiState<out T> {

    /**
     * Initial state before any work begins.
     */
    public data object Initial : UiState<Nothing>

    /**
     * Full-screen loading state.
     */
    public data object Loading : UiState<Nothing>

    /**
     * Content is available and should be rendered.
     *
     * @param T Screen-specific display state.
     * @property data Screen-specific state used to render the UI.
     */
    public data class Display<out T>(
        public val data: T,
    ) : UiState<T>

    /**
     * Full-screen error state.
     *
     * @property message Localized, presentation-safe text supplied by the ViewModel, or an empty
     * string when the ViewModel supplied none; the UI then words the failure itself, for example
     * from [cause] or with a generic localized message. Never exception or backend text.
     * @property cause Failure behind the error, for logging or for the UI to map, or `null`.
     */
    public data class Error(
        public val message: String,
        public val cause: Throwable? = null,
    ) : UiState<Nothing>
}
