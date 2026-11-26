package dev.voir.areca.foundation.adaptive

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

/**
 * The form an [AdaptiveMenu] takes.
 *
 * Give the app's choice once, as `LocalAdaptiveMenuPresentation provides …` in the theme's
 * `componentDefaults`, so every menu of the app opens the same way.
 */
public enum class AdaptiveMenuPresentation {
    /**
     * A dropdown under a pointer or on a window from expanded up; a sheet from the bottom edge on a
     * phone or a portrait tablet under touch, where a finger reaches it.
     */
    Adaptive,

    /**
     * A card hanging from its trigger on every window and under every input: the screen stays in
     * view around it, and the actions open next to the control that was pressed.
     */
    Dropdown,
}

/**
 * The [AdaptiveMenuPresentation] an [AdaptiveMenu] takes when none is passed.
 * [AdaptiveMenuPresentation.Adaptive] unless provided.
 */
public val LocalAdaptiveMenuPresentation: ProvidableCompositionLocal<AdaptiveMenuPresentation> =
    compositionLocalOf { AdaptiveMenuPresentation.Adaptive }
