package dev.voir.areca.playground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.voir.areca.foundation.avatar.Avatar
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.Chip
import dev.voir.areca.foundation.button.IconToggleButton
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.layout.ChoiceCard
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.text.Amount
import dev.voir.areca.foundation.text.AmountSymbolPlacement
import dev.voir.areca.foundation.text.AmountText
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.text.color
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Bell
import dev.voir.areca.icons.Download
import dev.voir.areca.icons.Lock
import dev.voir.areca.icons.Palette
import dev.voir.areca.icons.Heart
import dev.voir.areca.icons.HeartFill
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the scene is rendered at. */
private const val WIDTH = 390

/** Logical height of the scene. */
private const val HEIGHT = 1400

/** Pixel density the scene is rasterised at. */
private const val DENSITY = 2f

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders the 1.1 components in both themes to `build/screens/new-components-*.png`: hue tiles in a
 * settings section, amounts, choice cards, an icon toggle and chips.
 *
 * Not an assertion: it fails when a part throws, and it is where the parts are looked at.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*NewComponentsRenderTest*'
 * ```
 */
class NewComponentsRenderTest {

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun `the new components render in both themes`() {
        for (dark in listOf(false, true)) {
            val scene = ImageComposeScene(
                width = (WIDTH * DENSITY).toInt(),
                height = (HEIGHT * DENSITY).toInt(),
                density = Density(DENSITY),
            ) {
                ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) { Scene() }
            }
            try {
                var time = 0L
                while (time < 1_000_000_000L) {
                    scene.render(time)
                    time += 50_000_000L
                }
                val data = requireNotNull(scene.render(1_000_000_000L).encodeToData(EncodedImageFormat.PNG))
                OUTPUT.mkdirs()
                File(OUTPUT, "new-components-${if (dark) "dark" else "light"}.png").writeBytes(data.bytes)
            } finally {
                scene.close()
            }
        }
    }
}

@Composable
private fun Scene() {
    BaseLayout { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(vertical = ArecaTheme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
        ) {
            Settings {
                Section(header = { Text("Hue tiles") }) {
                    val hues = ArecaTheme.hues
                    SettingsRow(
                        title = { Text("Appearance") },
                        leading = { SettingsRowIcon(ArecaIcons.Palette, hue = hues.purple) },
                        onClick = {},
                    )
                    SettingsRow(
                        title = { Text("Notifications") },
                        leading = { SettingsRowIcon(ArecaIcons.Bell, hue = hues.orange) },
                        onClick = {},
                    )
                    SettingsRow(
                        title = { Text("Export") },
                        leading = { SettingsRowIcon(ArecaIcons.Download, hue = hues.teal, soft = true) },
                        onClick = {},
                    )
                    SettingsRow(
                        title = { Text("Privacy") },
                        leading = { SettingsRowIcon(ArecaIcons.Lock, hue = hues.indigo, soft = true) },
                        onClick = {},
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                Text("Amounts", variant = TextVariant.Overline)
                AmountText(
                    Amount(whole = "1,284", fraction = "50", symbol = "€", sign = "−"),
                    style = ArecaTheme.typography.headingLarge,
                    color = TextColor.Danger.color,
                )
                AmountText(
                    Amount(whole = "1 284", fraction = "50", decimalMark = ",", symbol = "د.إ"),
                    style = ArecaTheme.typography.headingSmall,
                )
                AmountText(
                    Amount(whole = "12", symbol = "kg", symbolPlacement = AmountSymbolPlacement.After),
                    style = ArecaTheme.typography.bodyLarge,
                )
                Box(Modifier.width(160.dp)) {
                    AmountText(
                        Amount(whole = "123,456,789", fraction = "00", symbol = "$"),
                        style = ArecaTheme.typography.headingLarge,
                        autoSize = TextAutoSize.StepBased(minFontSize = 12.sp, maxFontSize = 32.sp),
                    )
                }
                AmountText(Amount(whole = "1,284", fraction = "50", symbol = "€"), masked = true)
                // Plain text with a symbol in another script keeps the interface's order.
                Text("د.إ 1 234,50", variant = TextVariant.BodyLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                    for (code in listOf("USDT", "USDC", "MATIC", "1INCH")) {
                        Avatar(shape = ArecaTheme.shapes.tile, letters = code, maxLetters = 5)
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl).selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            ) {
                Text("Choice cards", variant = TextVariant.Overline)
                ChoiceCard(
                    selected = true,
                    onClick = {},
                    badge = { Badge(style = BadgeStyle.Brand) { Text("Best value") } },
                    trailing = { Text("€39.99") },
                ) {
                    Text("Yearly", variant = TextVariant.LabelLarge)
                    Text("€3.33 a month", variant = TextVariant.BodySmall, color = TextColor.Subtle)
                }
                ChoiceCard(selected = false, onClick = {}, trailing = { Text("€4.99") }) {
                    Text("Monthly", variant = TextVariant.LabelLarge)
                    Text("Cancel any time", variant = TextVariant.BodySmall, color = TextColor.Subtle)
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                Text("Toggle and chips", variant = TextVariant.Overline)
                Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                    IconToggleButton(
                        checked = true,
                        onCheckedChange = {},
                        imageVector = ArecaIcons.Heart,
                        checkedImageVector = ArecaIcons.HeartFill,
                        contentDescription = "Favourite",
                    )
                    IconToggleButton(
                        checked = false,
                        onCheckedChange = {},
                        imageVector = ArecaIcons.Heart,
                        checkedImageVector = ArecaIcons.HeartFill,
                        contentDescription = "Favourite",
                    )
                }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                    Chip(label = "Food", selected = true, onSelectedChange = {})
                    Chip(label = "Travel", onSelectedChange = {})
                    Chip(
                        label = "Iris",
                        onRemove = {},
                        leading = {
                            Box(
                                Modifier
                                    .size(10.dp)
                                    .background(ArecaTheme.hues.purple, ArecaTheme.shapes.full),
                            )
                        },
                    )
                }
            }
        }
    }
}
