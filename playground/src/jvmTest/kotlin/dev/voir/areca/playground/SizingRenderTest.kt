package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.list.ListItemSize
import dev.voir.areca.foundation.selection.Checkbox
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Search
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical size of the comparison: the touch column beside the pointer column. */
private const val WIDTH = 1000
private const val HEIGHT = 820

/** Pixel density the comparison is rasterised at, so the PNG is legible when opened. */
private const val DENSITY = 1.5f

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders the same controls under touch and under a pointer, side by side, to `build/screens`.
 *
 * Not an assertion: it fails when a size preset throws, and it is how the two densities are compared
 * at a glance — that a field, a button and an icon button still line up, and that rows shrink with them.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*SizingRenderTest*'
 * ```
 */
class SizingRenderTest {

    /** Buttons, a field row, list rows, a top bar and a checkbox under each input. */
    @Test
    fun touchBesidePointer() {
        render("sizing-touch-pointer")
    }

    @OptIn(ExperimentalComposeUiApi::class)
    private fun render(name: String) {
        val scene = ImageComposeScene(
            width = (WIDTH * DENSITY).toInt(),
            height = (HEIGHT * DENSITY).toInt(),
            density = Density(DENSITY),
        ) {
            ArecaThemeProvider(inputMode = InputMode.Touch) {
                Row(
                    modifier = Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas).padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    for (input in InputMode.entries) {
                        ArecaThemeProvider(inputMode = input) {
                            Controls(input, Modifier.width(452.dp))
                        }
                    }
                }
            }
        }
        try {
            scene.render(0L)
            val data = requireNotNull(scene.render(1_000_000_000L).encodeToData(EncodedImageFormat.PNG))
            OUTPUT.mkdirs()
            File(OUTPUT, "$name.png").writeBytes(data.bytes)
        } finally {
            scene.close()
        }
    }

    @Composable
    private fun Controls(input: InputMode, modifier: Modifier) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(input.name, variant = TextVariant.HeadingSmall)
            Text("Button, ExtraSmall to ExtraLarge", variant = TextVariant.BodySmall, color = TextColor.Muted)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                for (size in listOf(ButtonSize.ExtraSmall, ButtonSize.Small, ButtonSize.Medium, ButtonSize.Large)) {
                    Button(onClick = {}, size = size) { Text("Save") }
                }
            }
            Button(onClick = {}, size = ButtonSize.ExtraLarge) { Text("Get started") }
            Text("Field, button and icon button at Medium", variant = TextVariant.BodySmall, color = TextColor.Muted)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                TextField(value = "Lisbon", onValueChange = {}, modifier = Modifier.weight(1f))
                Button(onClick = {}) { Text("Search") }
                IconButton(imageVector = ArecaIcons.Search, contentDescription = "Search", onClick = {})
            }
            Text("List rows: Compact, Medium, Comfortable", variant = TextVariant.BodySmall, color = TextColor.Muted)
            Column {
                ListItem(title = { Text("Notifications") }, size = ListItemSize.Compact, onClick = {})
                ListItem(title = { Text("Exchange rates") }, size = ListItemSize.Medium, onClick = {})
                ListItem(
                    title = { Text("Everyday") },
                    subtitle = { Text("Debit card · Revolut") },
                    size = ListItemSize.Comfortable,
                    onClick = {},
                    trailing = { Checkbox(checked = true, onCheckedChange = {}) },
                )
            }
            TopBar(title = "Activity")
        }
    }
}
