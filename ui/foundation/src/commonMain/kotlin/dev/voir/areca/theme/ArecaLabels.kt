package dev.voir.areca.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * The words Areca's components say on their own — what a back arrow, a close cross, a sheet's
 * scrim or a busy button announces — in one place, so an app gives them in its language once.
 *
 * Every component that has such a word reads its default from here; a value passed at the call
 * site still wins. Pass a localized set to `ArecaThemeProvider(labels = …)`, built from the app's
 * string resources:
 *
 * ```kotlin
 * ArecaThemeProvider(
 *     labels = ArecaLabels(
 *         back = stringResource(Res.string.back),
 *         close = stringResource(Res.string.close),
 *         // …
 *     ),
 * ) { App() }
 * ```
 *
 * Component-specific copy — a list's empty face, a picker's search placeholder — stays in each
 * component's own `…Labels`.
 *
 * @property back What a back control does.
 * @property close What a close cross does.
 * @property dismiss What pressing a sheet's or a dialog's scrim does.
 * @property busy What a control running its action is announced as.
 * @property cancel A cancel button's word.
 * @property more What an overflow control opens.
 * @property remove What a remove cross does.
 * @property search What a search control or field does.
 * @property clearSearch What a search field's clear cross does.
 * @property showPassword What reveals a password.
 * @property hidePassword What hides it again.
 */
@Immutable
public data class ArecaLabels(
    public val back: String = "Back",
    public val close: String = "Close",
    public val dismiss: String = "Dismiss",
    public val busy: String = "Busy",
    public val cancel: String = "Cancel",
    public val more: String = "More",
    public val remove: String = "Remove",
    public val search: String = "Search",
    public val clearSearch: String = "Clear search",
    public val showPassword: String = "Show password",
    public val hidePassword: String = "Hide password",
)

internal val LocalLabels = staticCompositionLocalOf { ArecaLabels() }
