package dev.voir.areca.playground.foundation.navigation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.navigation.PageIndicator
import dev.voir.areca.foundation.navigation.PageIndicatorStyle
import dev.voir.areca.foundation.navigation.PageIndicatorVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The [PageIndicatorVariant]s, as a selectable list. */
private enum class PageIndicatorVariantOption(val variant: PageIndicatorVariant) {
    /** [PageIndicatorVariant.Elongated]. */
    Elongated(PageIndicatorVariant.Elongated),

    /** [PageIndicatorVariant.Dots]. */
    Dots(PageIndicatorVariant.Dots),

    /** [PageIndicatorVariant.Shrinking] with its default window. */
    Shrinking(PageIndicatorVariant.Shrinking()),

    /** [PageIndicatorVariant.Line]. */
    Line(PageIndicatorVariant.Line),

    /** [PageIndicatorVariant.Segments]. */
    Segments(PageIndicatorVariant.Segments),
}

/** Where the indicator is drawn, which decides its style. */
private enum class PageIndicatorGround {
    /** On the page: [PageIndicatorStyle.Default]. */
    Page,

    /** Over a picture: [PageIndicatorStyle.onImage]. */
    Picture,
}

/** Showcase for [PageIndicator]. */
@Composable
fun PageIndicatorShowcase() = ShowcaseScaffold(
    title = "Page indicator",
    summary = "Marks saying which page is on screen: dots, a sliding window of dots, a line or " +
        "story bars, on the page or over a picture.",
    module = "foundation",
) {
    Playground(description = "Tap a mark with clickable on to jump to its page.") {
        val variant by enumControl(
            key = "variant",
            default = PageIndicatorVariantOption.Elongated,
            options = PageIndicatorVariantOption.entries,
            optionLabel = { it.name },
        )
        val ground by enumControl(
            key = "ground",
            default = PageIndicatorGround.Page,
            options = PageIndicatorGround.entries,
            optionLabel = { it.name },
        )
        val pageCount by numberControl(
            key = "pages",
            default = 7f,
            range = 1f..30f,
            step = 1f,
            format = { it.toInt().toString() },
        )
        val clickable by booleanControl("clickable", default = true)
        var page by remember { mutableIntStateOf(0) }
        val count = pageCount.toInt()

        actionControl(key = "previous", buttonLabel = "Previous page") { page = (page - 1).coerceAtLeast(0) }
        actionControl(key = "next", buttonLabel = "Next page") { page = (page + 1).coerceAtMost(count - 1) }

        preview {
            IndicatorGround(ground = ground) {
                PageIndicator(
                    pageCount = count,
                    currentPage = page.coerceAtMost(count - 1),
                    contentDescription = "Page ${page + 1} of $count",
                    variant = variant.variant,
                    progress = { 0.5f },
                    onPageClick = if (clickable) ({ page = it }) else null,
                    style = if (ground == PageIndicatorGround.Picture) PageIndicatorStyle.onImage() else PageIndicatorStyle.Default,
                    modifier = Modifier.then(
                        if (variant == PageIndicatorVariantOption.Segments) Modifier.width(240.dp) else Modifier,
                    ),
                )
            }
        }
    }

    Variants(
        title = "Variants",
        description = "Seven pages, the third on screen, in every variant.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in PageIndicatorVariantOption.entries) {
            variant(name = option.name) {
                PageIndicator(
                    pageCount = if (option == PageIndicatorVariantOption.Shrinking) 14 else 7,
                    currentPage = 2,
                    variant = option.variant,
                    progress = { 0.4f },
                    modifier = Modifier.width(240.dp),
                )
            }
        }
    }

    Variants(
        title = "Over a picture",
        description = "PageIndicatorStyle.onImage: white marks on a capsule, readable on light and dark " +
            "pictures alike.",
        layout = VariantLayout.Grid,
    ) {
        for (light in listOf(true, false)) {
            variant(name = if (light) "Light picture" else "Dark picture") {
                PictureBox(light = light) {
                    PageIndicator(
                        pageCount = 6,
                        currentPage = 1,
                        variant = PageIndicatorVariant.Dots,
                        style = PageIndicatorStyle.onImage(),
                    )
                }
            }
        }
    }

    Recipes {
        recipe(
            name = "Story bars",
            description = "Segments fed an autoplay timer through progress, read while drawing so the " +
                "bar fills without recomposing.",
        ) {
            var page by remember { mutableIntStateOf(0) }
            val transition = rememberInfiniteTransition(label = "story")
            val progress by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(durationMillis = 3000, easing = LinearEasing), RepeatMode.Restart),
                label = "storyProgress",
            )
            if (progress > 0.99f) page = (page + 1) % 4

            PictureBox(light = false) {
                PageIndicator(
                    pageCount = 4,
                    currentPage = page,
                    variant = PageIndicatorVariant.Segments,
                    progress = { progress },
                    style = PageIndicatorStyle.onImage(capsule = null),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = ArecaTheme.spacing.lg),
                )
            }
        }
        recipe(
            name = "Many pages",
            description = "Twenty-four pictures in the width of five dots; the window slides once the " +
                "page leaves it.",
        ) {
            var page by remember { mutableIntStateOf(0) }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            ) {
                PageIndicator(
                    pageCount = 24,
                    currentPage = page,
                    variant = PageIndicatorVariant.Shrinking(),
                    onPageClick = { page = it },
                )
                Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                    Button(
                        "Previous",
                        onClick = { page = (page - 1).coerceAtLeast(0) },
                        style = ButtonStyle.soft(),
                        size = ButtonSize.Small,
                    )
                    Button(
                        "Next",
                        onClick = { page = (page + 1).coerceAtMost(23) },
                        style = ButtonStyle.soft(),
                        size = ButtonSize.Small,
                    )
                }
            }
        }
    }

    EdgeCases {
        case(name = "One page", expectation = "A single mark; a pager of one has nowhere to go.") {
            PageIndicator(pageCount = 1, currentPage = 0)
        }
        case(
            name = "Shrinking with few pages",
            expectation = "Draws like Dots when every page fits in the window and its edge dots.",
        ) {
            PageIndicator(pageCount = 6, currentPage = 4, variant = PageIndicatorVariant.Shrinking())
        }
        case(name = "Line with many pages", expectation = "The thumb narrows to one page of the track.") {
            PageIndicator(pageCount = 40, currentPage = 30, variant = PageIndicatorVariant.Line)
        }
        case(name = "Current page out of range", expectation = "Coerced to the last page.") {
            PageIndicator(pageCount = 5, currentPage = 12, variant = PageIndicatorVariant.Dots)
        }
    }
}

/**
 * The ground the playground's indicator sits on: nothing for the page, a picture for the on-image
 * style.
 *
 * @param ground Where the indicator is drawn.
 * @param content The indicator.
 */
@Composable
private fun IndicatorGround(ground: PageIndicatorGround, content: @Composable () -> Unit) {
    if (ground == PageIndicatorGround.Picture) PictureBox(light = true, content = content) else content()
}

/**
 * A stand-in for a picture, with the indicator laid along its bottom edge.
 *
 * @param light Whether the picture is a light product shot or a dark photograph.
 * @param content What is laid over it.
 */
@Composable
private fun PictureBox(light: Boolean, content: @Composable () -> Unit) {
    val colors = if (light) listOf(Color(0xFFF4F1EC), Color(0xFFE2DCD2)) else listOf(Color(0xFF2B3A55), Color(0xFF7A5C61))
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(180.dp)
            .background(Brush.linearGradient(colors), ArecaTheme.shapes.lg),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Box(Modifier.fillMaxWidth().padding(bottom = ArecaTheme.spacing.lg), contentAlignment = Alignment.Center) {
            content()
        }
    }
}
