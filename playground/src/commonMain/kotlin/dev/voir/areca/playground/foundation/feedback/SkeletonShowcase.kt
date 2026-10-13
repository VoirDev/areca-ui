package dev.voir.areca.playground.foundation.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlin.time.Duration.Companion.milliseconds
import dev.voir.areca.foundation.feedback.Skeleton
import dev.voir.areca.foundation.feedback.SkeletonArrangement
import dev.voir.areca.foundation.feedback.SkeletonSize
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [Skeleton]. */
@Composable
fun SkeletonShowcase() = ShowcaseScaffold(
    title = "Skeleton",
    summary = "Shimmering placeholder standing in for content that has not loaded.",
    module = "foundation",
) {
    Playground {
        val count by numberControl(
            key = "count",
            default = 3f,
            range = 1f..6f,
            step = 1f,
            format = { it.toInt().toString() },
            description = "Number of lines.",
        )
        val inline by booleanControl(
            key = "inline",
            default = false,
            description = "Lay the lines out in a row instead of a column.",
        )
        val animated by booleanControl("enableAnimation", default = true)
        val rightToLeft by booleanControl("RTL sweep", default = false)
        val duration by numberControl(
            key = "duration",
            default = 1.5f,
            range = 0.5f..4f,
            step = 0.5f,
            format = { "${it}s" },
        )
        val lineStyle by enumControl(
            key = "textStyle",
            default = SkeletonLineStyle.Ambient,
            options = SkeletonLineStyle.entries,
            description = "Style of the line each bar stands in for; sizes a bar given no height.",
        )

        preview {
            Skeleton(
                modifier = Modifier.fillMaxWidth(),
                count = count.toInt(),
                arrangement = if (inline) {
                    SkeletonArrangement.Row
                } else {
                    SkeletonArrangement.Lines
                },
                textStyle = lineStyle.textStyle(),
                animated = animated,
                reversed = rightToLeft,
                size = SkeletonSize.default(
                    shimmerPeriod = (duration * 1000).toLong().milliseconds,
                ),
            )
        }
    }

    Variants(
        title = "Shapes",
        description = "Shape is a parameter, so a skeleton can stand in for any element.",
        layout = VariantLayout.Grid,
    ) {
        variant("Default", detail = "small radius") {
            Skeleton(width = 160.dp, height = 16.dp)
        }
        variant("Pill") {
            Skeleton(width = 160.dp, height = 16.dp, size = SkeletonSize.default(shape = CircleShape))
        }
        variant("Avatar") {
            Skeleton(width = 48.dp, height = 48.dp, size = SkeletonSize.default(shape = CircleShape))
        }
        variant("Tile", detail = "shapes.tile, a settings icon tile") {
            Skeleton(width = 32.dp, height = 32.dp, size = SkeletonSize.default(shape = ArecaTheme.shapes.tile))
        }
        variant("Card") {
            Skeleton(
                width = 200.dp,
                height = 96.dp,
                size = SkeletonSize.default(shape = ArecaTheme.shapes.lg),
            )
        }
    }

    Variants(
        title = "Layout",
        description = "Lines stack by default and flow inline when asked.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Stacked", detail = "count = 3") {
            Skeleton(modifier = Modifier.fillMaxWidth(), count = 3)
        }
        variant("Row", detail = "count = 3, arrangement = Row") {
            Skeleton(count = 3, width = 64.dp, arrangement = SkeletonArrangement.Row)
        }
        variant("Static", detail = "animated = false") {
            Skeleton(modifier = Modifier.fillMaxWidth(), count = 2, animated = false)
        }
    }

    Variants(
        title = "Text styles",
        description = "Given the style of the line it replaces, a bar is exactly that line's height.",
        layout = VariantLayout.Vertical,
    ) {
        for (style in SkeletonLineStyle.entries) {
            variant("textStyle = ${style.name}", detail = style.detail) {
                Skeleton(modifier = Modifier.fillMaxWidth(0.6f), textStyle = style.textStyle())
            }
        }
    }

    Recipes {
        recipe(
            name = "Article header",
            description = "A heading bar over two body lines, each sized by the style it stands in for.",
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                Skeleton(
                    modifier = Modifier.fillMaxWidth(0.55f),
                    textStyle = ArecaTheme.typography.headingSmall,
                )
                Skeleton(
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = ArecaTheme.typography.bodyMedium,
                )
                Skeleton(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    textStyle = ArecaTheme.typography.bodyMedium,
                )
            }
        }
        recipe(
            name = "Feed row",
            description = "Avatar, title and body — the shape of the content it replaces.",
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            ) {
                Skeleton(width = 40.dp, height = 40.dp, size = SkeletonSize.default(shape = CircleShape))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                ) {
                    Skeleton(width = 140.dp, height = 14.dp)
                    Skeleton(modifier = Modifier.fillMaxWidth(), count = 2, height = 10.dp)
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "No explicit height",
            expectation = "Falls back to the current text line height, so it matches the copy it replaces.",
        ) {
            Skeleton(modifier = Modifier.fillMaxWidth(), count = 2)
        }
        case(
            name = "Animation disabled",
            expectation = "Renders the resting base color with no sweep — the reduced-motion path.",
        ) {
            Skeleton(modifier = Modifier.fillMaxWidth(), count = 2, animated = false)
        }
        case(
            name = "Right-to-left sweep",
            expectation = "Highlight travels the other way; everything else is unchanged.",
        ) {
            Skeleton(
                modifier = Modifier.fillMaxWidth(),
                count = 2,
                reversed = true,
            )
        }
    }
}

/** A line of copy a skeleton bar can be sized to stand in for, as the page offers them. */
private enum class SkeletonLineStyle(
    /** Where the style is used, shown under its cell. */
    val detail: String,
) {
    /** No style passed: the bar takes the ambient text style's font size. */
    Ambient("textStyle = null, the surrounding style"),

    /** A section or card heading. */
    HeadingSmall("typography.headingSmall"),

    /** A paragraph of body copy. */
    BodyMedium("typography.bodyMedium"),

    /** A caption under an image or a figure. */
    Caption("typography.caption"),
    ;

    /**
     * Resolves the style from the current theme.
     *
     * @return The style to pass as `textStyle`, or `null` for [Ambient].
     */
    @Composable
    fun textStyle(): TextStyle? = when (this) {
        Ambient -> null
        HeadingSmall -> ArecaTheme.typography.headingSmall
        BodyMedium -> ArecaTheme.typography.bodyMedium
        Caption -> ArecaTheme.typography.caption
    }
}
