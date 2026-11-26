package dev.voir.areca.foundation.adaptive

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

/**
 * The form an [AdaptiveMenu] takes. The menu keeps it on every window and under every input: which
 * form suits the product is the app's choice, not the window's.
 *
 * Give the app's choice once, as `LocalAdaptiveMenuPresentation provides …` in the theme's
 * `componentDefaults`, so every menu of the app opens the same way.
 */
public enum class AdaptiveMenuPresentation {
    /**
     * A card hanging from its trigger: the screen stays in view around it, and the actions open
     * next to the control that was pressed.
     */
    Dropdown,

    /** A sheet from the bottom edge with the menu's title at its head. */
    Sheet,
}

/**
 * The [AdaptiveMenuPresentation] an [AdaptiveMenu] takes when none is passed.
 * [AdaptiveMenuPresentation.Dropdown] unless provided.
 */
public val LocalAdaptiveMenuPresentation: ProvidableCompositionLocal<AdaptiveMenuPresentation> =
    compositionLocalOf { AdaptiveMenuPresentation.Dropdown }
