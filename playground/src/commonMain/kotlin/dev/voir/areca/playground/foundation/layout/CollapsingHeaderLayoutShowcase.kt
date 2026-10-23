package dev.voir.areca.playground.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.layout.CollapsingHeaderLayout
import dev.voir.areca.foundation.layout.rememberCollapsingHeaderState
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.theme.ArecaTheme

private val FrameHeight = 360.dp

/** Showcase for [CollapsingHeaderLayout]. */
@Composable
fun CollapsingHeaderLayoutShowcase() = ShowcaseScaffold(
    title = "Collapsing header",
    summary = "Header that scrolls away while a pinned region and the content stay in place.",
    module = "foundation",
) {
    Playground(description = "Scroll inside the frame — the header collapses, the pinned row does not.") {
        val expandOnlyAtTop by booleanControl(
            key = "expandOnlyWhenScrolledToTop",
            default = true,
            description = "When on, the header only returns once the content is back at the top.",
        )
        val headerHeight by numberControl(
            key = "header height",
            default = 120f,
            range = 60f..200f,
            step = 20f,
            format = { "${it.toInt()}dp" },
        )

        preview {
            Frame {
                CollapsingHeaderLayout(
                    modifier = Modifier.fillMaxSize(),
                    header = { SampleHeader(headerHeight.dp) },
                    pinned = { SamplePinned() },
                    expandOnlyWhenScrolledToTop = expandOnlyAtTop,
                ) { contentModifier ->
                    SampleList(contentModifier)
                }
            }
        }
    }

    Anatomy(description = "Three regions, stacked top to bottom.") {
        slot(
            name = "header",
            description = "Scrolls away with the content. Titles, imagery, summary stats.",
        ) {
            Frame {
                CollapsingHeaderLayout(
                    modifier = Modifier.fillMaxSize(),
                    header = { SampleHeader(120.dp) },
                    pinned = {},
                ) { contentModifier -> SampleList(contentModifier) }
            }
        }
        slot(
            name = "pinned",
            description = "Stays put once the header has gone. Tabs, search, filters.",
        ) {
            Frame {
                CollapsingHeaderLayout(
                    modifier = Modifier.fillMaxSize(),
                    header = { SampleHeader(120.dp) },
                    pinned = { SamplePinned() },
                ) { contentModifier -> SampleList(contentModifier) }
            }
        }
        slot(
            name = "content",
            description = "Receives a modifier carrying the nested-scroll connection. Must use it.",
        ) {
            Frame {
                CollapsingHeaderLayout(
                    modifier = Modifier.fillMaxSize(),
                    header = { SampleHeader(80.dp) },
                    pinned = { SamplePinned() },
                ) { contentModifier -> SampleList(contentModifier) }
            }
        }
    }

    Recipes {
        recipe(
            name = "Summary that leaves a strip",
            description = "The header collapses into a compact band that fades in and stays, " +
                "keeping the period and its figure readable.",
        ) {
            Frame {
                val state = rememberCollapsingHeaderState()
                CollapsingHeaderLayout(
                    modifier = Modifier.fillMaxSize(),
                    header = { SampleHeader(160.dp) },
                    pinned = { SamplePinned() },
                    state = state,
                    collapsed = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ArecaTheme.colors.bgCanvas)
                                .padding(horizontal = ArecaTheme.spacing.xl, vertical = ArecaTheme.spacing.md),
                        ) {
                            Text("October 2026", variant = TextVariant.HeadingExtraSmall)
                            Text("€4,250 in · €2,340 out", variant = TextVariant.BodyExtraSmall, color = TextColor.Subtle)
                        }
                    },
                ) { contentModifier -> SampleList(contentModifier) }
            }
        }
    }

    EdgeCases {
        case(
            name = "Expand from anywhere",
            expectation = "Scrolling up reveals the header immediately, without returning to the top.",
        ) {
            Frame {
                CollapsingHeaderLayout(
                    modifier = Modifier.fillMaxSize(),
                    header = { SampleHeader(120.dp) },
                    pinned = { SamplePinned() },
                    expandOnlyWhenScrolledToTop = false,
                ) { contentModifier -> SampleList(contentModifier) }
            }
        }
        case(
            name = "Content shorter than the frame",
            expectation = "Nothing to scroll, so the header never collapses.",
        ) {
            Frame {
                CollapsingHeaderLayout(
                    modifier = Modifier.fillMaxSize(),
                    header = { SampleHeader(100.dp) },
                    pinned = { SamplePinned() },
                ) { contentModifier -> SampleList(contentModifier, rows = 2) }
            }
        }
    }
}

@Composable
private fun Frame(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(FrameHeight)) { content() }
}

@Composable
private fun SampleHeader(height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(ArecaTheme.colors.bgBrandMuted)
            .padding(ArecaTheme.spacing.xl),
        contentAlignment = Alignment.BottomStart,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs)) {
            Text(text = "Analytics", variant = TextVariant.HeadingSmall)
            Text(
                text = "Scroll the list to collapse this",
                variant = TextVariant.BodySmall,
                color = TextColor.Subtle,
            )
        }
    }
}

@Composable
private fun SamplePinned() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ArecaTheme.colors.bgSubtle)
            .padding(ArecaTheme.spacing.lg),
    ) {
        Text(text = "Pinned row — stays visible", variant = TextVariant.LabelMedium)
    }
}

@Composable
private fun SampleList(modifier: Modifier, rows: Int = 30) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(rows) { index ->
            Text(
                text = "Row ${index + 1}",
                variant = TextVariant.BodyMedium,
                modifier = Modifier.fillMaxWidth().padding(ArecaTheme.spacing.lg),
            )
        }
    }
}
