package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.charts.BarChart
import dev.voir.areca.charts.BarChartBar
import dev.voir.areca.charts.BarChartBarWidth
import dev.voir.areca.charts.BarChartColor
import dev.voir.areca.charts.BarChartFill
import dev.voir.areca.charts.BarChartGroup
import dev.voir.areca.charts.BarChartHorizontalAlignment
import dev.voir.areca.charts.BarChartOptions
import dev.voir.areca.charts.BarChartSize
import dev.voir.areca.charts.BarChartStyle
import dev.voir.areca.charts.BarChartYAxisLabel
import dev.voir.areca.charts.StackedBarChart
import dev.voir.areca.charts.StackedBarSegment
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the charts are rendered at. */
private const val WIDTH = 390

/** Logical height of the device the charts are rendered at. */
private const val HEIGHT = 900

/** Pixel density the scenes are rasterised at. */
private const val DENSITY = 2f

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/** Groups of two bars, one per quarter. */
private val Quarters = listOf(
    BarChartGroup(label = "Q1", bars = listOf(BarChartBar(42f, label = "2025"), BarChartBar(28f, label = "2024"))),
    BarChartGroup(label = "Q2", bars = listOf(BarChartBar(58f, label = "2025"), BarChartBar(36f, label = "2024"))),
    BarChartGroup(label = "Q3", bars = listOf(BarChartBar(71f, label = "2025"), BarChartBar(44f, label = "2024"))),
)

/** Tick labels for [Quarters]. */
private val Ticks = listOf(
    BarChartYAxisLabel(value = 0f, label = "0"),
    BarChartYAxisLabel(value = 40f, label = "40"),
    BarChartYAxisLabel(value = 80f, label = "80"),
)

/**
 * Renders one composition to a PNG.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param content Composition to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark) {
            Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas).padding(16.dp)) { content() }
        }
    }
    try {
        val data = requireNotNull(scene.render().encodeToData(EncodedImageFormat.PNG))
        OUTPUT.mkdirs()
        File(OUTPUT, "$name.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/**
 * Renders the bar and stacked bar charts to `build/screens`: every colour family, both directions,
 * and a chart given unbounded width.
 *
 * Like the other render tests it has no golden image; it fails when a configuration throws, and it
 * is how the charts are looked at on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*ChartsRenderTest*'
 * ```
 */
class ChartsRenderTest {

    /** Each colour family in both themes, with a stacked bar beneath. */
    @Test
    fun `charts render in every colour family`() {
        listOf(false, true).forEach { dark ->
            render("charts-families${if (dark) "-dark" else ""}", dark = dark) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    BarChartColor.entries.take(3).forEach { color ->
                        BarChart(
                            groups = Quarters,
                            modifier = Modifier.fillMaxWidth(),
                            style = BarChartStyle.of(color),
                            size = BarChartSize.Small,
                            yAxisLabels = Ticks,
                        )
                    }
                    StackedBarChart(
                        segments = List(6) { StackedBarSegment(fraction = 0.15f) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }

    /** Right to left, with a horizontal gradient and negative values, and inside a horizontal scroll. */
    @Test
    fun `charts render right to left and unbounded`() {
        render("charts-rtl-unbounded") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    BarChart(
                        groups = BarChartGroup.singleBars(
                            listOf(42f, -20f, 71f, 64f).map { value ->
                                BarChartBar(
                                    value = value,
                                    fill = BarChartFill.Gradient(
                                        Brush.horizontalGradient(
                                            listOf(ArecaTheme.colors.bgBrandBase, ArecaTheme.colors.bgInfoBase),
                                        ),
                                    ),
                                )
                            },
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        size = BarChartSize.Small,
                        yAxisLabels = Ticks,
                        options = BarChartOptions(
                            minValue = -40f,
                            barWidth = BarChartBarWidth.Fixed(32.dp),
                            horizontalAlignment = BarChartHorizontalAlignment.Start,
                        ),
                    )
                    StackedBarChart(
                        segments = listOf(
                            StackedBarSegment(fraction = 0.5f),
                            StackedBarSegment(fraction = -0.3f),
                            StackedBarSegment(fraction = 0.3f),
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                BarChart(
                    groups = Quarters + Quarters.map { it.copy(label = it.label + "'") },
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    size = BarChartSize.Small,
                    yAxisLabels = Ticks,
                )
            }
        }
    }
}
