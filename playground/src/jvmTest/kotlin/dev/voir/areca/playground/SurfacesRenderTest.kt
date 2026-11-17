package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.BadgeOption
import dev.voir.areca.foundation.badge.BadgeSelectionMode
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.FlowBadgeSelect
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.feedback.ProgressBar
import dev.voir.areca.foundation.feedback.Skeleton
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.layout.CardFooterStyle
import dev.voir.areca.foundation.notice.Notice
import dev.voir.areca.foundation.selection.SelectorItem
import dev.voir.areca.foundation.selection.SlidingSelector
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.theme.ProvideSurface
import dev.voir.areca.theme.SurfaceLayout
import dev.voir.areca.theme.SurfaceLevel
import dev.voir.areca.theme.SurfaceRoot
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the scene is rendered at. */
private const val WIDTH = 390

/** Logical height of the scene: every surface, one under the other. */
private const val HEIGHT = 1500

/** Pixel density the scene is rasterised at. */
private const val DENSITY = 2f

/** Time between the frames stepped through before the kept one, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** How long the clock runs before the kept frame, in nanoseconds. */
private const val SETTLE_NANOS = 2_000_000_000L

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders [content] in one surface layout and theme to a PNG.
 *
 * @param name File name, without the extension.
 * @param layout Which of the page and its cards is grey.
 * @param dark Whether to render in the dark theme.
 * @param content The scene.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, layout: SurfaceLayout, dark: Boolean, content: @Composable () -> Unit) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark, surfaceLayout = layout, inputMode = InputMode.Touch) {
            content()
        }
    }
    try {
        // Stepped frame by frame, so an animation another one starts — a thumb placed once its
        // option is measured — has settled by the frame that is kept.
        var time = 0L
        while (time < SETTLE_NANOS) {
            scene.render(time)
            time += FRAME_NANOS
        }
        val data = requireNotNull(scene.render(SETTLE_NANOS).encodeToData(EncodedImageFormat.PNG))
        OUTPUT.mkdirs()
        File(OUTPUT, "$name.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/**
 * Renders one scene of every surface-bearing component in both layouts and both themes, to
 * `build/screens/surfaces-*.png`.
 *
 * Not an assertion: the contrast the layouts must keep is asserted by `ArecaSurfacesTest` in
 * foundation. This is where the result is looked at — a card on the page, a field, chips, a
 * selector and a skeleton in it, a card nested in it, a section with a band, a notice holding a soft
 * neutral button, and the same card in a sheet.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*SurfacesRenderTest*'
 * ```
 */
class SurfacesRenderTest {

    @Test
    fun `every surface renders in both layouts and both themes`() {
        for (layout in SurfaceLayout.entries) {
            for (dark in listOf(false, true)) {
                val name = "surfaces-${layout.name.lowercase()}-${if (dark) "dark" else "light"}"
                render(name, layout, dark) { SurfacesScene() }
            }
        }
    }
}

/** The scene: a screen of surfaces, then a sheet of the same card. */
@Composable
private fun SurfacesScene() {
    BaseLayout { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = ArecaTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
        ) {
            Text(
                "Page",
                variant = TextVariant.Overline,
                modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl),
            )
            SampleCard()

            Settings {
                Section(
                    header = { Text("Section") },
                    footer = { Text("Footer text sits on the page.") },
                    band = { SettingsRow(title = { Text("Band row") }, onClick = {}) },
                ) {
                    SettingsRow(title = { Text("Language") }, value = { Text("English") }, onClick = {})
                    SettingsRow(title = { Text("Currency") }, value = { Text("EUR") }, onClick = {})
                }
            }

            Notice(
                title = "Neutral notice",
                modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl),
            ) {
                Text("A soft neutral button inside it stays visible.")
                Button(text = "Soft neutral", onClick = {}, style = ButtonStyle.soft(ActionColor.Neutral))
            }

            Text(
                "Sheet",
                variant = TextVariant.Overline,
                modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl),
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(ArecaTheme.surfaces.sheet.page.ground)
                    .padding(vertical = ArecaTheme.spacing.xl),
            ) {
                ProvideSurface(SurfaceLevel.Page, SurfaceRoot.Sheet) { SampleCard() }
            }
        }
    }
}

/** A card holding a field, chips, a selector, progress, a skeleton and a nested card. */
@Composable
private fun SampleCard() {
    Card(
        modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl),
        footerStyle = CardFooterStyle.soft(),
        footer = { Text("Neutral footer band", modifier = Modifier.weight(1f)) },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        ) {
            Text("Card", variant = TextVariant.HeadingExtraSmall)
            TextField(value = "Outlined field", onValueChange = {})
            TextField(value = "Filled field", onValueChange = {}, style = FieldStyle.filled())
            FlowBadgeSelect(
                options = listOf("Food", "Travel", "Home").map { BadgeOption(label = { Text(it) }, value = it) },
                onSelect = {},
                selected = listOf("Food"),
                selectionMode = BadgeSelectionMode.Multiple,
                style = BadgeStyle.FilledChip,
                size = BadgeSize.Chip,
            )
            SlidingSelector(
                items = listOf("Week", "Month", "Year").map { SelectorItem(it) { Text(it) } },
                selected = "Month",
                onSelectedChange = {},
            )
            ProgressBar(progress = 0.4f)
            Skeleton(count = 2, animated = false)
            Card { inner ->
                Column(Modifier.padding(inner).height(24.dp)) { Text("Nested card") }
            }
        }
    }
}
