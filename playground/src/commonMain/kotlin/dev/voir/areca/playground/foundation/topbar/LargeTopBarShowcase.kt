package dev.voir.areca.playground.foundation.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.LargeTopBar
import dev.voir.areca.foundation.topbar.OverflowAction
import dev.voir.areca.foundation.topbar.SearchAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarScrollBehavior
import dev.voir.areca.foundation.topbar.TopBarScrollState
import dev.voir.areca.foundation.topbar.rememberTopBarScrollState
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Height every example is given, since a screen frame otherwise fills the window. */
private val FrameHeight = 340.dp

/** Showcase for [LargeTopBar] and the scroll behaviors a [TopBar] answers with. */
@Composable
fun LargeTopBarShowcase() = ShowcaseScaffold(
    title = "Large top bar",
    summary = "A heading bar that knows what the content under it is doing: it shows its own " +
        "separator, travels with the gesture, or collapses a headline into itself.",
    module = "foundation",
) {
    Variants(
        title = "Scroll behaviors",
        description = "Scroll inside each frame. Every behavior fades the separator in once " +
            "content is under the bar — no screen decides that by hand. What differs is whether " +
            "the bar itself moves.",
        layout = VariantLayout.Vertical,
    ) {
        variant(
            name = "Pinned",
            detail = "the bar stays; only its separator changes",
        ) {
            ScrollFrame(behavior = TopBarScrollBehavior.Pinned) { scrollState ->
                TopBar(
                    title = "Inbox",
                    subtitle = "128 conversations",
                    scrollState = scrollState,
                    leading = { BackAction(onClick = {}) },
                    trailing = {
                        SearchAction(onClick = {})
                    },
                )
            }
        }
        variant(
            name = "Enter always",
            detail = "the bar leaves going up and returns on the first pixel going down",
        ) {
            ScrollFrame(behavior = TopBarScrollBehavior.EnterAlways) { scrollState ->
                TopBar(
                    title = "Long read",
                    scrollState = scrollState,
                    leading = { BackAction(onClick = {}) },
                    trailing = { OverflowAction(onClick = {}) },
                )
            }
        }
        variant(
            name = "Exit until collapsed",
            detail = "the headline scrolls away and the compact title fades in behind it",
        ) {
            ScrollFrame(behavior = TopBarScrollBehavior.ExitUntilCollapsed) { scrollState ->
                LargeTopBar(
                    title = "Library",
                    subtitle = "Everything you have saved",
                    scrollState = scrollState,
                    leading = { BackAction(onClick = {}) },
                    trailing = {
                        SearchAction(onClick = {})
                    },
                )
            }
        }
    }

    Recipes {
        recipe(
            name = "Large title over a list",
            description = "The screen opens with its name spelled out and trades it for content. " +
                "Hand the same state to the bar and to the list's nested scroll.",
        ) {
            ScrollFrame(behavior = TopBarScrollBehavior.ExitUntilCollapsed) { scrollState ->
                LargeTopBar(
                    title = "Transactions",
                    subtitle = "March 2026",
                    scrollState = scrollState,
                    leading = { BackAction(onClick = {}) },
                    trailing = { OverflowAction(onClick = {}) },
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Content shorter than the frame",
            expectation = "Nothing to scroll, so the headline stays open and no separator appears.",
        ) {
            ScrollFrame(
                behavior = TopBarScrollBehavior.ExitUntilCollapsed,
                itemCount = 2,
            ) { scrollState ->
                LargeTopBar(
                    title = "Archive",
                    scrollState = scrollState,
                    leading = { BackAction(onClick = {}) },
                )
            }
        }
        case(
            name = "A pinned behavior on a large bar",
            expectation = "The headline never collapses. A fine static header, but not what the " +
                "large bar is for.",
        ) {
            ScrollFrame(behavior = TopBarScrollBehavior.Pinned) { scrollState ->
                LargeTopBar(
                    title = "Settings",
                    scrollState = scrollState,
                    leading = { BackAction(onClick = {}) },
                )
            }
        }
    }
}

/**
 * A 340dp screen frame holding a bar over a scrollable list, both driven by one scroll state.
 *
 * The frame is what makes these examples honest: a bar only shows what it does once there is
 * content moving under it, and a showcase page's own scrolling is not that content.
 *
 * @param behavior How the bar should answer a scroll.
 * @param itemCount Rows in the list. Enough to scroll, by default.
 * @param bar The bar, handed the state it should read the scroll position from.
 */
@Composable
private fun ScrollFrame(
    behavior: TopBarScrollBehavior,
    itemCount: Int = 24,
    bar: @Composable (TopBarScrollState) -> Unit,
) {
    val scrollState = rememberTopBarScrollState(behavior)

    Box(modifier = Modifier.fillMaxWidth().height(FrameHeight)) {
        BaseLayout(
            modifier = Modifier.fillMaxSize(),
            bg = ArecaTheme.colors.bgCanvas,
            topBar = { bar(scrollState) },
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollState.nestedScrollConnection),
                contentPadding = padding,
            ) {
                items(itemCount) { index ->
                    ListItem(
                        title = { Text(text = "Row ${index + 1}") },
                        subtitle = {
                            Text(
                                text = "Scroll to see what the bar does",
                                variant = TextVariant.BodySmall,
                                color = TextColor.Subtle,
                            )
                        },
                    )
                }
            }
        }
    }
}
