package dev.voir.areca.foundation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier

/** Marks the receivers of the settings DSL so their scopes cannot be mixed up. */
@DslMarker
public annotation class SettingsDsl

/** Receiver of the content of a [Settings] list, where its [Section]s are declared. */
@SettingsDsl
@Immutable
public class SettingsScope internal constructor() {
    /** Holder of the shared receiver. */
    internal companion object {
        /** The one receiver every list hands out; the scope carries no state of its own. */
        val Instance = SettingsScope()
    }
}

/**
 * A screen of settings: sections of rows, each section on its own card.
 *
 * Provides the settings design system to its content and spaces its sections by
 * [SettingsSectionSize.sectionGap], the only source of vertical rhythm between them. Padding above
 * the first and below the last section belongs to the screen, and so does scrolling: put the list
 * in a scrolling container on `bgCanvas`.
 *
 * ```kotlin
 * Settings {
 *     Section(header = { Text("Notifications") }, footer = { Text("Applies to this device.") }) {
 *         SettingsToggleRow(state = push, onToggle = { push = it }, title = { Text("Push") })
 *         SettingsSelectRow(
 *             title = { Text("Sound") },
 *             options = sounds,
 *             selected = sound,
 *             onSelect = { sound = it },
 *             optionLabel = { it.label },
 *         )
 *     }
 * }
 * ```
 *
 * @param modifier Modifier applied to the list container.
 * @param styles Colors used by the list.
 * @param sizes Metrics used by the list.
 * @param headerPlacement Where the sections of the list draw their headers, unless a section says
 * otherwise.
 * @param content Sections of the list.
 */
@Composable
public fun Settings(
    modifier: Modifier = Modifier,
    styles: SettingsStyles = SettingsDefaults.styles(),
    sizes: SettingsSizes = SettingsDefaults.sizes(),
    headerPlacement: SettingsHeaderPlacement = SettingsHeaderPlacement.Inside,
    content: @Composable SettingsScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalSettingsStyles provides styles,
        LocalSettingsSizes provides sizes,
        LocalSettingsHeaderPlacement provides headerPlacement,
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(sizes.section.sectionGap),
        ) {
            SettingsScope.Instance.content()
        }
    }
}
