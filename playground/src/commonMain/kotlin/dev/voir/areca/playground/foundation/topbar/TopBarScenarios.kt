package dev.voir.areca.playground.foundation.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.topbar.AvatarAction
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.CancelAction
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.DeleteAction
import dev.voir.areca.foundation.topbar.DoneAction
import dev.voir.areca.foundation.topbar.FavoriteAction
import dev.voir.areca.foundation.topbar.MenuAction
import dev.voir.areca.foundation.topbar.NotificationsAction
import dev.voir.areca.foundation.topbar.OverflowAction
import dev.voir.areca.foundation.topbar.SearchAction
import dev.voir.areca.foundation.topbar.ShareAction
import dev.voir.areca.foundation.topbar.TextAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarProgress
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.theme.ArecaTheme

/**
 * One situation a screen's top bar is drawn in, and the bar Areca recommends for it.
 *
 * @property name What the situation is.
 * @property recipe The preset and controls the bar is built from, as a caller would write them.
 * @property bar The bar, on the ground it is meant to sit on.
 */
class TopBarScenario(
    val name: String,
    val recipe: String,
    val bar: @Composable () -> Unit,
)

/** Every scenario, in the order a product usually meets them: root, depth, task, overlay. */
val TopBarScenarios: List<TopBarScenario> = listOf(
    TopBarScenario(
        name = "Root screen",
        recipe = "Default · MenuAction | title | SearchAction, NotificationsAction",
    ) {
        TopBar(
            title = "Inbox",
            modifier = Modifier.fillMaxWidth(),
            leading = { MenuAction(onClick = {}) },
            trailing = {
                SearchAction(onClick = {})
                NotificationsAction(onClick = {}, badge = "3")
            },
        )
    },
    TopBarScenario(
        name = "Hub screen",
        recipe = "Tonal · title | NotificationsAction, AvatarAction",
    ) {
        TopBar(
            title = "Good morning",
            subtitle = "Tuesday, 27 September",
            modifier = Modifier.fillMaxWidth(),
            style = TopBarStyle.Tonal,
            trailing = {
                NotificationsAction(onClick = {}, badge = "3")
                AvatarAction(onClick = {}, letters = "GB")
            },
        )
    },
    TopBarScenario(
        name = "Detail screen",
        recipe = "Default · BackAction | title | ShareAction, OverflowAction",
    ) {
        TopBar(
            title = "Invoice #4821",
            modifier = Modifier.fillMaxWidth(),
            leading = { BackAction(onClick = {}) },
            trailing = {
                ShareAction(onClick = {})
                OverflowAction(onClick = {})
            },
        )
    },
    TopBarScenario(
        name = "Full-screen task",
        recipe = "Default, centred · CancelAction | title | DoneAction",
    ) {
        TopBar(
            title = "New expense",
            modifier = Modifier.fillMaxWidth(),
            titleAlignment = TopBarTitleAlignment.Center,
            leading = { CancelAction(onClick = {}) },
            trailing = { DoneAction(onClick = {}, text = "Save") },
        )
    },
    TopBarScenario(
        name = "Selection",
        recipe = "Default · CloseAction | count | ShareAction, DeleteAction",
    ) {
        TopBar(
            title = "3 selected",
            modifier = Modifier.fillMaxWidth(),
            leading = { CloseAction(onClick = {}) },
            trailing = {
                ShareAction(onClick = {})
                DeleteAction(onClick = {})
            },
        )
    },
    TopBarScenario(
        name = "Step in a flow",
        recipe = "Default · BackAction | step | TextAction(Neutral) · TopBarProgress",
    ) {
        TopBar(
            title = "Your details",
            subtitle = "Step 2 of 4",
            modifier = Modifier.fillMaxWidth(),
            size = TopBarSize.Default.copy(bottomPadding = PaddingValues()),
            leading = { BackAction(onClick = {}) },
            trailing = { TextAction(text = "Skip", onClick = {}, color = ActionColor.Neutral) },
            bottom = { TopBarProgress(progress = 0.5f) },
        )
    },
    TopBarScenario(
        name = "Sheet header",
        recipe = "Modal · title | CloseAction",
    ) {
        SheetGround {
            TopBar(
                title = "Rename file",
                modifier = Modifier.fillMaxWidth(),
                size = TopBarSize.Modal,
                style = TopBarStyle.Modal,
                trailing = { CloseAction(onClick = {}) },
            )
        }
    },
    TopBarScenario(
        name = "Sheet that confirms",
        recipe = "Modal, centred · CancelAction | title | DoneAction",
    ) {
        SheetGround {
            TopBar(
                title = "Birthday",
                modifier = Modifier.fillMaxWidth(),
                size = TopBarSize.Modal,
                style = TopBarStyle.Modal,
                titleAlignment = TopBarTitleAlignment.Center,
                leading = { CancelAction(onClick = {}) },
                trailing = { DoneAction(onClick = {}) },
            )
        }
    },
    TopBarScenario(
        name = "Viewer over a file",
        recipe = "Floating, centred · CloseAction | name, position | ShareAction, OverflowAction",
    ) {
        Box(Modifier.fillMaxWidth().background(ArecaTheme.colors.bgSubtle)) {
            TopBar(
                title = "receipt-march.jpg",
                subtitle = "2 of 3",
                modifier = Modifier.fillMaxWidth(),
                style = TopBarStyle.Floating,
                titleAlignment = TopBarTitleAlignment.Center,
                leading = { CloseAction(onClick = {}) },
                trailing = {
                    ShareAction(onClick = {})
                    OverflowAction(onClick = {})
                },
            )
        }
    },
    TopBarScenario(
        name = "Over a photograph",
        recipe = "Scrim · BackAction | – | ShareAction, FavoriteAction",
    ) {
        var favorite by remember { mutableStateOf(true) }

        Box(Modifier.fillMaxWidth().height(PHOTO_HEIGHT).background(SamplePhoto)) {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                style = TopBarStyle.Scrim,
                leading = { BackAction(onClick = {}) },
                trailing = {
                    ShareAction(onClick = {})
                    FavoriteAction(selected = favorite, onClick = { favorite = !favorite })
                },
            ) {}
        }
    },
)

/**
 * The same bar in every appearance, to compare how the neutral controls and the colored ones
 * beside them change together.
 *
 * @property name Name of the appearance.
 * @property recipe How a caller asks for it.
 * @property bar The bar, on the ground it is meant to sit on.
 */
class TopBarAppearance(
    val name: String,
    val recipe: String,
    val bar: @Composable () -> Unit,
)

/** Plain, Tonal, Floating and Scrim, each on its own ground. */
val TopBarAppearances: List<TopBarAppearance> = listOf(
    TopBarAppearance(name = "Plain", recipe = "TopBarActionStyle.Plain") {
        AppearanceBar(style = TopBarStyle.Default)
    },
    TopBarAppearance(name = "Tonal", recipe = "TopBarActionStyle.Tonal") {
        AppearanceBar(style = TopBarStyle.Tonal)
    },
    TopBarAppearance(name = "Floating", recipe = "TopBarActionStyle.Floating") {
        Box(Modifier.fillMaxWidth().background(ArecaTheme.colors.bgSubtle)) {
            AppearanceBar(style = TopBarStyle.Floating)
        }
    },
    TopBarAppearance(name = "Scrim", recipe = "TopBarActionStyle.Scrim") {
        Box(Modifier.fillMaxWidth().background(SamplePhoto)) {
            AppearanceBar(style = TopBarStyle.Scrim)
        }
    },
)

/**
 * One bar holding a neutral control on each side, a colored glyph and a colored word.
 *
 * @param style Colors of the bar, carrying the appearance under comparison.
 */
@Composable
private fun AppearanceBar(style: TopBarStyle) {
    var favorite by remember { mutableStateOf(true) }

    TopBar(
        title = "Receipt",
        modifier = Modifier.fillMaxWidth(),
        style = style,
        leading = { BackAction(onClick = {}) },
        trailing = {
            ShareAction(onClick = {})
            FavoriteAction(selected = favorite, onClick = { favorite = !favorite })
            DoneAction(onClick = {})
        },
    )
}

/**
 * The top of a sheet: its surface with rounded upper corners, on the dimmed screen behind it.
 *
 * @param content The sheet's header.
 */
@Composable
private fun SheetGround(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxWidth().background(ArecaTheme.colors.bgOverlayDark).padding(top = 12.dp)) {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = ArecaTheme.radius.xl, topEnd = ArecaTheme.radius.xl))
                .background(ArecaTheme.colors.bgCanvas),
        ) {
            content()
        }
    }
}

/** A stand-in for a photograph: warm sky over a dark hillside. */
private val SamplePhoto = Brush.verticalGradient(
    listOf(Color(0xFFF2B880), Color(0xFFB5667A), Color(0xFF3B2F4A)),
)

/** Height of the photograph a scrim bar is shown over. */
private val PHOTO_HEIGHT = 120.dp
