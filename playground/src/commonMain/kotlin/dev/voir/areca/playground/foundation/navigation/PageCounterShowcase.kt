package dev.voir.areca.playground.foundation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.navigation.PageCounter
import dev.voir.areca.foundation.navigation.PageCounterStyle
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [PageCounter]. */
@Composable
fun PageCounterShowcase() = ShowcaseScaffold(
    title = "Page counter",
    summary = "Where the page on screen sits among the rest, as a count in a capsule.",
    module = "foundation",
) {
    Playground {
        val page by numberControl(
            key = "page",
            default = 3f,
            range = 1f..99f,
            step = 1f,
            format = { it.toInt().toString() },
        )
        val count by numberControl(
            key = "count",
            default = 9f,
            range = 1f..99f,
            step = 1f,
            format = { it.toInt().toString() },
        )
        val onImage by booleanControl("on image", default = false, description = "PageCounterStyle.onImage.")

        preview {
            val counter = @Composable {
                PageCounter(
                    currentPage = page.toInt() - 1,
                    pageCount = count.toInt(),
                    contentDescription = "Picture ${page.toInt().coerceAtMost(count.toInt())} of ${count.toInt()}",
                    style = if (onImage) PageCounterStyle.onImage() else PageCounterStyle.Default,
                )
            }
            if (onImage) PictureCorner { counter() } else counter()
        }
    }

    Variants(title = "Styles", layout = VariantLayout.Grid) {
        variant("Default", detail = "On the page") {
            PageCounter(currentPage = 2, pageCount = 9)
        }
        variant("onImage", detail = "In a picture's corner") {
            PictureCorner { PageCounter(currentPage = 2, pageCount = 9, style = PageCounterStyle.onImage()) }
        }
    }

    Recipes {
        recipe(
            name = "Worded count",
            description = "text words the count for the language; contentDescription says it in full.",
        ) {
            PageCounter(
                currentPage = 2,
                pageCount = 9,
                text = { index, total -> "${index + 1} of $total" },
                contentDescription = "Picture 3 of 9",
            )
        }
    }

    EdgeCases {
        case(name = "One page", expectation = "Still drawn; leave it out where one page needs no count.") {
            PageCounter(currentPage = 0, pageCount = 1)
        }
        case(name = "Three-digit count", expectation = "Tabular figures keep the capsule's width steady.") {
            PageCounter(currentPage = 107, pageCount = 128)
        }
    }
}

/**
 * A stand-in for a picture with the counter in its top end corner.
 *
 * @param content The counter.
 */
@Composable
private fun PictureCorner(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .width(240.dp)
            .height(160.dp)
            .background(Brush.linearGradient(listOf(Color(0xFFF4F1EC), Color(0xFFD9CFC1))), ArecaTheme.shapes.lg),
        contentAlignment = Alignment.TopEnd,
    ) {
        Box(Modifier.padding(ArecaTheme.spacing.md)) { content() }
    }
}
