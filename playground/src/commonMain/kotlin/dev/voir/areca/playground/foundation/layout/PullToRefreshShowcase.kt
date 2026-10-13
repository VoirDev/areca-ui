package dev.voir.areca.playground.foundation.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.ExperimentalArecaApi
import dev.voir.areca.foundation.layout.PullToRefreshLayout
import dev.voir.areca.foundation.layout.PullToRefreshSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.delay

private val FrameHeight = 320.dp

/** Showcase for [PullToRefreshLayout]. */
@OptIn(ExperimentalArecaApi::class)
@Composable
fun PullToRefreshShowcase() = ShowcaseScaffold(
    title = "Pull to refresh",
    summary = "Wraps a scrollable so an overscroll at the top triggers a refresh.",
    module = "foundation",
) {
    Playground(description = "Drag the list down past the threshold and release.") {
        val threshold by numberControl(
            key = "threshold",
            default = 80f,
            range = 40f..160f,
            step = 20f,
            format = { "${it.toInt()}dp" },
            description = "How far the pull must travel to arm a refresh.",
        )
        val enabled by booleanControl("enabled", default = true)

        var refreshing by remember { mutableStateOf(false) }
        var refreshCount by remember { mutableStateOf(0) }

        LaunchedEffect(refreshing) {
            if (refreshing) {
                delay(1500)
                refreshCount++
                refreshing = false
            }
        }

        preview {
            Box(modifier = Modifier.fillMaxWidth().height(FrameHeight)) {
                PullToRefreshLayout(
                    isRefreshing = refreshing,
                    onRefresh = { refreshing = true },
                    modifier = Modifier.fillMaxSize(),
                    enabled = enabled,
                    size = PullToRefreshSize.default(threshold = threshold.dp),
                ) {
                    RefreshableList(refreshCount)
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Refresh disabled",
            expectation = "The list overscrolls normally; no indicator appears.",
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(FrameHeight)) {
                PullToRefreshLayout(
                    isRefreshing = false,
                    onRefresh = {},
                    modifier = Modifier.fillMaxSize(),
                    enabled = false,
                ) {
                    RefreshableList(0)
                }
            }
        }
        case(
            name = "Already refreshing",
            expectation = "Indicator stays put; further pulls do not re-trigger it.",
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(FrameHeight)) {
                PullToRefreshLayout(
                    isRefreshing = true,
                    onRefresh = {},
                    modifier = Modifier.fillMaxSize(),
                ) {
                    RefreshableList(0)
                }
            }
        }
        case(
            name = "Content shorter than the frame",
            expectation = "Still pulls: the gesture is on the overscroll, not the scroll range.",
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(FrameHeight)) {
                PullToRefreshLayout(
                    isRefreshing = false,
                    onRefresh = {},
                    modifier = Modifier.fillMaxSize(),
                ) {
                    RefreshableList(0, rows = 2)
                }
            }
        }
    }
}

@Composable
private fun RefreshableList(refreshCount: Int, rows: Int = 20) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = "Refreshed $refreshCount time(s)",
                variant = TextVariant.LabelMedium,
                modifier = Modifier.fillMaxWidth().padding(ArecaTheme.spacing.lg),
            )
        }
        items(rows) { index ->
            Text(
                text = "Row ${index + 1}",
                variant = TextVariant.BodyMedium,
                modifier = Modifier.fillMaxWidth().padding(ArecaTheme.spacing.lg),
            )
        }
    }
}
