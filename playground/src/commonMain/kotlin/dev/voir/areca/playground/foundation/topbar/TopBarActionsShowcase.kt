package dev.voir.areca.playground.foundation.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.topbar.Action
import dev.voir.areca.foundation.topbar.AddAction
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.CancelAction
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.DeleteAction
import dev.voir.areca.foundation.topbar.DoneAction
import dev.voir.areca.foundation.topbar.DownloadAction
import dev.voir.areca.foundation.topbar.EditAction
import dev.voir.areca.foundation.topbar.FavoriteAction
import dev.voir.areca.foundation.topbar.FilterAction
import dev.voir.areca.foundation.topbar.MenuAction
import dev.voir.areca.foundation.topbar.NotificationsAction
import dev.voir.areca.foundation.topbar.OverflowAction
import dev.voir.areca.foundation.topbar.SearchAction
import dev.voir.areca.foundation.topbar.SettingsAction
import dev.voir.areca.foundation.topbar.ShareAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarActionScope
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Flash
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The bar presets a reader can switch between in the playground, by name. */
private val PresetNames = listOf("Default", "Tonal", "Modal", "Floating", "Scrim")

/** Showcase for the controls of a [TopBar]: their appearances, the named verbs, and scenarios. */
@Composable
fun TopBarActionsShowcase() = ShowcaseScaffold(
    title = "Top bar actions",
    summary = "One appearance per bar, a named control for every common verb, and a preset for " +
        "every place a bar is drawn.",
    module = "foundation",
) {
    Playground {
        val preset by enumControl(key = "preset", default = "Default", options = PresetNames)
        val loading by booleanControl("loading", default = false, label = "done is working")
        val badge by textControl("badge", default = "3")

        preview {
            PresetGround(preset) {
                TopBar(
                    title = "Receipt",
                    modifier = Modifier.fillMaxWidth(),
                    style = presetStyle(preset),
                    leading = { BackAction(onClick = {}) },
                    trailing = {
                        NotificationsAction(onClick = {}, badge = badge.ifEmpty { null })
                        DoneAction(onClick = {}, loading = loading)
                    },
                )
            }
        }
    }

    Variants(
        title = "Appearances",
        description = "Set once on TopBarStyle.actions, never per control. Neutral controls take " +
            "the appearance as it is; a colored one climbs one step of the emphasis ladder — " +
            "plain, soft, solid — so it stands out without becoming a different object.",
        layout = VariantLayout.Vertical,
    ) {
        TopBarAppearances.forEach { appearance ->
            variant(appearance.name, detail = appearance.recipe) { appearance.bar() }
        }
    }

    Recipes(
        title = "Scenarios",
        description = "Pick the preset from where the bar sits and the controls from what the " +
            "screen is for. These ten cover nearly every screen a product has.",
    ) {
        TopBarScenarios.forEach { scenario ->
            recipe(scenario.name, description = scenario.recipe) { scenario.bar() }
        }
    }

    Variants(
        title = "Named controls",
        description = "Each verb has its glyph, words and color settled once. Reach for IconAction " +
            "only for a verb not listed here, and for Action when the mark is not an icon.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Navigation", detail = "BackAction · CloseAction · MenuAction") {
            ActionsBar(
                leading = { BackAction(onClick = {}) },
                trailing = {
                    CloseAction(onClick = {})
                    MenuAction(onClick = {})
                },
            )
        }
        variant("Finding", detail = "SearchAction · FilterAction(badge) · NotificationsAction(badge)") {
            ActionsBar {
                SearchAction(onClick = {})
                FilterAction(onClick = {}, badge = "2")
                NotificationsAction(onClick = {}, badge = "9")
            }
        }
        variant("Acting on the screen", detail = "ShareAction · EditAction · DownloadAction · OverflowAction") {
            ActionsBar {
                ShareAction(onClick = {})
                EditAction(onClick = {})
                DownloadAction(onClick = {})
                OverflowAction(onClick = {})
            }
        }
        variant("Creating and configuring", detail = "AddAction · SettingsAction") {
            ActionsBar {
                AddAction(onClick = {})
                SettingsAction(onClick = {})
            }
        }
        variant("Stateful and destructive", detail = "FavoriteAction(selected) · DeleteAction") {
            var favorite by remember { mutableStateOf(false) }

            ActionsBar {
                FavoriteAction(selected = favorite, onClick = { favorite = !favorite })
                DeleteAction(onClick = {})
            }
        }
        variant("Worded pair", detail = "CancelAction | DoneAction") {
            ActionsBar(
                leading = { CancelAction(onClick = {}) },
                trailing = { DoneAction(onClick = {}) },
            )
        }
        variant("Your own mark", detail = "Action { } — still sized and drawn by the bar") {
            ActionsBar(style = TopBarStyle.Tonal) {
                Action(onClick = {}) {
                    Icon(imageVector = ArecaIcons.Flash, contentDescription = "Boost")
                }
                DoneAction(onClick = {}, text = "Upgrade")
            }
        }
    }
}

/**
 * A bar of controls under comparison, titled only to show where the title would sit.
 *
 * @param style Colors of the bar.
 * @param leading Controls before the title.
 * @param trailing Controls after the title.
 */
@Composable
private fun ActionsBar(
    style: TopBarStyle = TopBarStyle.Default,
    leading: (@Composable TopBarActionScope.() -> Unit)? = null,
    trailing: @Composable TopBarActionScope.() -> Unit,
) {
    TopBar(
        title = "Title",
        modifier = Modifier.fillMaxWidth(),
        style = style,
        leading = leading,
        trailing = trailing,
    )
}

/**
 * The bar style a preset name stands for.
 *
 * @param name One of [PresetNames].
 * @return The preset.
 */
@Composable
private fun presetStyle(name: String): TopBarStyle = when (name) {
    "Tonal" -> TopBarStyle.Tonal
    "Modal" -> TopBarStyle.Modal
    "Floating" -> TopBarStyle.Floating
    "Scrim" -> TopBarStyle.Scrim
    else -> TopBarStyle.Default
}

/**
 * The ground a preset is meant to sit on: a panel, a file's backdrop or a photograph.
 *
 * @param name One of [PresetNames].
 * @param content The bar.
 */
@Composable
private fun PresetGround(name: String, content: @Composable () -> Unit) {
    val ground = when (name) {
        "Modal" -> Modifier.background(ArecaTheme.colors.bgSurface)
        "Floating" -> Modifier.background(ArecaTheme.colors.bgSubtle)
        "Scrim" -> Modifier.background(
            Brush.verticalGradient(listOf(Color(0xFFF2B880), Color(0xFFB5667A), Color(0xFF3B2F4A))),
        )
        else -> Modifier
    }

    Box(Modifier.fillMaxWidth().then(ground)) { content() }
}
