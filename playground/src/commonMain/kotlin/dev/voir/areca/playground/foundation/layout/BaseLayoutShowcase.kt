package dev.voir.areca.playground.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.theme.ArecaTheme

/** Height every example is given, since a screen frame otherwise fills the window. */
private val FrameHeight = 320.dp

/** Showcase for [BaseLayout]. */
@Composable
fun BaseLayoutShowcase() = ShowcaseScaffold(
    title = "Base layout",
    summary = "Screen frame that measures top and bottom bars, then pads content around them.",
    module = "foundation",
) {
    Playground(description = "A screen frame fills its parent, so each example is 320dp tall.") {
        val withTopBar by booleanControl("topBar", default = true)
        val withBottomBar by booleanControl("bottomBar", default = true)
        val withBackground by booleanControl(
            key = "background",
            default = false,
            description = "Painted below everything, behind the bars.",
        )
        val withOverlay by booleanControl(
            key = "overlay",
            default = false,
            description = "Painted above everything, ignoring the content padding.",
        )

        preview {
            Frame {
                BaseLayout(
                    modifier = Modifier.fillMaxSize(),
                    bg = ArecaTheme.colors.bgCanvas,
                    topBar = if (withTopBar) ({ SampleTopBar() }) else ({}),
                    bottomBar = if (withBottomBar) ({ SampleBottomBar() }) else ({}),
                    background = if (withBackground) {
                        { Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgBrandMuted)) }
                    } else {
                        {}
                    },
                    overlay = if (withOverlay) ({ SampleOverlay() }) else ({}),
                ) { padding ->
                    ScrollingContent(Modifier.padding(padding))
                }
            }
        }
    }

    Anatomy(description = "Five slots. Only content is required; the rest default to empty.") {
        slot(name = "topBar", description = "Measured first; content is padded below it.") {
            Frame {
                BaseLayout(
                    modifier = Modifier.fillMaxSize(),
                    bg = ArecaTheme.colors.bgCanvas,
                    topBar = { SampleTopBar() },
                ) { padding -> ScrollingContent(Modifier.padding(padding)) }
            }
        }
        slot(name = "bottomBar", description = "Measured too; content is padded above it.") {
            Frame {
                BaseLayout(
                    modifier = Modifier.fillMaxSize(),
                    bg = ArecaTheme.colors.bgCanvas,
                    bottomBar = { SampleBottomBar() },
                ) { padding -> ScrollingContent(Modifier.padding(padding)) }
            }
        }
        slot(
            name = "background",
            description = "Behind the bars and the content. For gradients and imagery.",
        ) {
            Frame {
                BaseLayout(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { SampleTopBar() },
                    background = {
                        Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgBrandMuted))
                    },
                ) { padding -> ScrollingContent(Modifier.padding(padding)) }
            }
        }
        slot(
            name = "overlay",
            description = "Above everything and outside the content padding. For FABs and toasts.",
        ) {
            Frame {
                BaseLayout(
                    modifier = Modifier.fillMaxSize(),
                    bg = ArecaTheme.colors.bgCanvas,
                    topBar = { SampleTopBar() },
                    overlay = { SampleOverlay() },
                ) { padding -> ScrollingContent(Modifier.padding(padding)) }
            }
        }
    }

    EdgeCases {
        case(
            name = "No bars",
            expectation = "Content padding is zero and the content fills the frame.",
        ) {
            Frame {
                BaseLayout(
                    modifier = Modifier.fillMaxSize(),
                    bg = ArecaTheme.colors.bgCanvas,
                ) { padding -> ScrollingContent(Modifier.padding(padding)) }
            }
        }
        case(
            name = "Content ignoring the padding",
            expectation = "Content slides under the bars — the padding is offered, not imposed.",
        ) {
            Frame {
                BaseLayout(
                    modifier = Modifier.fillMaxSize(),
                    bg = ArecaTheme.colors.bgCanvas,
                    topBar = { SampleTopBar() },
                    bottomBar = { SampleBottomBar() },
                ) { ScrollingContent(Modifier) }
            }
        }
    }
}

@Composable
private fun Frame(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(FrameHeight),
    ) {
        content()
    }
}

@Composable
private fun SampleTopBar() {
    TopBar(modifier = Modifier.fillMaxWidth().background(ArecaTheme.colors.bgSubtle)) {
        Text(text = "Top bar", variant = TextVariant.LabelLarge)
    }
}

@Composable
private fun SampleBottomBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ArecaTheme.colors.bgSubtle)
            .padding(ArecaTheme.spacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Bottom bar", variant = TextVariant.LabelMedium, color = TextColor.Subtle)
    }
}

@Composable
private fun SampleOverlay() {
    Box(
        modifier = Modifier.fillMaxSize().padding(ArecaTheme.spacing.xl),
        contentAlignment = Alignment.BottomEnd,
    ) {
        Box(
            modifier = Modifier
                .background(ArecaTheme.colors.bgBrandBase, CircleShape)
                .padding(ArecaTheme.spacing.lg),
        ) {
            Text(text = "Overlay", variant = TextVariant.LabelSmall, color = TextColor.OnFill)
        }
    }
}

@Composable
private fun ScrollingContent(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = ArecaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
    ) {
        repeat(12) { index ->
            Text(text = "Content row ${index + 1}", variant = TextVariant.BodyMedium)
        }
    }
}
