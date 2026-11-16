package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.collection.ContentListGroup
import dev.voir.areca.foundation.collection.ContentListGroupLayout
import dev.voir.areca.foundation.collection.GroupedContentList
import dev.voir.areca.foundation.collection.GroupedPagedContentList
import dev.voir.areca.foundation.collection.LimitedContentListState
import dev.voir.areca.foundation.collection.PagedContentListState
import dev.voir.areca.foundation.field.select.SelectFlowModalDefaults
import dev.voir.areca.foundation.field.select.SelectFlowModalLabels
import dev.voir.areca.foundation.field.select.SelectOption
import dev.voir.areca.foundation.field.select.SelectOptionSection
import dev.voir.areca.foundation.layout.BottomActionBar
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.list.CheckboxListItem
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsTextFieldRow
import dev.voir.areca.foundation.settings.SettingsTextFieldSuffix
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the parts are rendered at. */
private const val WIDTH = 390

/** Pixel density the scenes are rasterised at. */
private const val DENSITY = 2f

/** Where the rendered parts are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

private val NoInsets = WindowInsets(0, 0, 0, 0)

/**
 * Renders one part to a PNG on the screen canvas.
 *
 * @param name File name, without the extension.
 * @param height Logical height of the scene.
 * @param dark Whether to render in the dark theme.
 * @param content Part to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, height: Int, dark: Boolean, content: @Composable () -> Unit) {
    ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (height * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) {
            Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) {
                content()
            }
        }
    }.let { scene ->
        try {
            scene.render(0)
            val data = scene.render(16_000_000).encodeToData(EncodedImageFormat.PNG)!!
            OUTPUT.mkdirs()
            File(OUTPUT, "$name${if (dark) "-dark" else ""}.png").writeBytes(data.bytes)
        } finally {
            scene.close()
        }
    }
}

private fun brand(label: String) = SelectOption(key = label, value = label, label = label)

private val Groups = listOf(
    ContentListGroup("month", "Ends this month", listOf("Espresso machine" to "Kitchen", "Robot vacuum" to "Hallway")),
    ContentListGroup("quarter", "Ends in the next 3 months", listOf("Laptop" to "Study", "Projector" to "Living room")),
    ContentListGroup("later", "Later", listOf("Bicycle" to "Garage")),
)

@Composable
private fun Thing(item: Pair<String, String>) {
    ListItem(title = { Text(item.first) }, subtitle = { Text(item.second) }, onClick = {})
}

/**
 * Renders the sectioned select list, labelled form rows, action bars and inset grouped lists to
 * `build/screens`, in both themes.
 *
 * Not an assertion: it fails when a configuration throws during composition or layout, and is how
 * these parts are looked at on a machine with no display.
 */
class FormAndListPartsRenderTest {

    @Test
    fun `renders the parts`() {
        for (dark in listOf(false, true)) {
            render("select-sections", height = 640, dark = dark) {
                SelectFlowModalDefaults.SectionedList(
                    sections = listOf(
                        SelectOptionSection("yours", "Yours", listOf(brand("Gaggia"))),
                        SelectOptionSection("popular", "Popular", listOf(brand("Apple"), brand("Bosch"))),
                        SelectOptionSection(
                            key = "all",
                            title = "All",
                            options = listOf("3M", "Acer", "Apple", "Bosch", "Braun").map(::brand),
                            indexed = true,
                        ),
                    ),
                    values = setOf("Apple"),
                    onSelect = {},
                    query = "Gorenje",
                    onCreateOption = {},
                )
            }

            render("settings-labelled-fields", height = 300, dark = dark) {
                Settings(modifier = Modifier.fillMaxWidth()) {
                    Section(header = { Text("Item") }) {
                        SettingsTextFieldRow(value = "ThinkPad X1", onValueChange = {}, title = "Model", placeholder = "Required")
                        SettingsTextFieldRow(value = "", onValueChange = {}, title = "Serial number", placeholder = "Optional")
                        SettingsTextFieldRow(
                            value = "80",
                            onValueChange = {},
                            title = "Width",
                            placeholder = "0",
                            suffix = { SettingsTextFieldSuffix(text = "cm", onClick = {}) },
                        )
                        SettingsTextFieldRow(
                            value = "1.2",
                            onValueChange = {},
                            title = "Weight",
                            placeholder = "0",
                            suffix = { SettingsTextFieldSuffix(text = "kg") },
                        )
                        SettingsTextFieldRow(value = "", onValueChange = {}, placeholder = "Notes")
                    }
                }
            }

            render("bottom-action-bars", height = 360, dark = dark) {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    BottomActionBar(actionText = "Add item", onAction = {}, windowInsets = NoInsets)
                    BottomActionBar(
                        actionText = "Move",
                        onAction = {},
                        windowInsets = NoInsets,
                        leading = { Text("3 selected") },
                    )
                    BottomActionBar(
                        actionText = "Save",
                        onAction = {},
                        enabled = false,
                        hint = "Add a name to save",
                        windowInsets = NoInsets,
                    )
                }
            }

            render("bottom-action-bar-screen", height = 520, dark = dark) {
                BaseLayout(
                    modifier = Modifier.fillMaxSize(),
                    bg = ArecaTheme.colors.bgCanvas,
                    topBar = { TopBar(title = "Move items") },
                    bottomBar = {
                        BottomActionBar(
                            actionText = "Move",
                            onAction = {},
                            windowInsets = NoInsets,
                            leading = { Text("2 selected") },
                        )
                    },
                ) { padding ->
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = padding) {
                        items(List(12) { "Item ${it + 1}" }) { item ->
                            CheckboxListItem(
                                checked = item == "Item 2" || item == "Item 5",
                                onCheckedChange = {},
                                title = { Text(item) },
                            )
                        }
                    }
                }
            }

            render("grouped-list-inset", height = 560, dark = dark) {
                Column {
                    Spacer(Modifier.height(16.dp))
                    GroupedContentList(
                        state = LimitedContentListState(Groups),
                        groupLayout = ContentListGroupLayout.Inset,
                        groupHeaderContent = { Text(it) },
                        itemContent = { Thing(it) },
                    )
                }
            }

            render("grouped-paged-list-inset", height = 560, dark = dark) {
                GroupedPagedContentList(
                    state = PagedContentListState(Groups, hasNextPage = false),
                    groupLayout = ContentListGroupLayout.Inset,
                    itemKey = { it.first },
                    groupHeaderContent = { Text(it) },
                    itemContent = { Thing(it) },
                )
            }
        }
    }
}
