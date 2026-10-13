package dev.voir.areca.viewmodel

/**
 * Reads the display data from outside the ViewModel.
 *
 * @param T Screen-specific display state.
 * @return [UiState.Display.data] when this is [UiState.Display], otherwise `null`.
 */
public fun <T> UiState<T>.displayOrNull(): T? = (this as? UiState.Display)?.data
