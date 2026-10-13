package dev.voir.areca.playground.showcase.section

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.voir.areca.playground.showcase.ShowcaseScope
import dev.voir.areca.playground.showcase.ShowcaseSectionContainer
import dev.voir.areca.playground.showcase.canvas.PreviewCanvas
import dev.voir.areca.playground.showcase.canvas.PreviewCanvasState
import dev.voir.areca.playground.showcase.canvas.PreviewCanvasToolbar
import dev.voir.areca.playground.showcase.canvas.rememberPreviewCanvasState
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.playground.showcase.chrome.style
import dev.voir.areca.theme.ArecaTheme
import kotlin.math.pow
import kotlin.math.roundToInt

/*
 * Sections for a foundations page — one that documents tokens rather than a component.
 *
 * A token has no props to wire to controls and no size ramp, so `Variants` and friends do not fit:
 * they give every cell its own canvas, which is right for three buttons and unreadable for sixty
 * swatches. These builders put one canvas around a whole section instead, so a section is a single
 * ground the reader scans across. See SHOWCASES.md §12.
 */

/** Cell width for a swatch grid. Three columns at the scaffold's full content width. */
private val SwatchMinWidth = 196.dp
private val SwatchMaxWidth = 280.dp

/** Height of a swatch chip. Tall enough to judge a colour, short enough to fit a long list. */
private val SwatchHeight = 40.dp

/** Height of one step in a ramp. */
private val RampStepHeight = 44.dp

/** Size of the dot standing in for an icon in a foreground sample. */
private val SampleDotSize = 14.dp

/** Thickness of the rule standing in for a drawn stroke in a foreground sample. */
private val SampleRuleHeight = 2.dp

/** Receiver a swatch grid is written against. */
@Stable
class TokenScope internal constructor() {

    /**
     * Adds one token.
     *
     * @param name Token name exactly as it is spelled on `ArecaColors` — this is what a reader
     *   copies into their code.
     * @param color Value the token resolves to in the theme the section is rendering in.
     * @param description What the token means, in one line. The same sentence its KDoc opens with.
     */
    @Composable
    fun token(name: String, color: Color, description: String? = null) {
        Column(
            modifier = Modifier.widthIn(min = SwatchMinWidth, max = SwatchMaxWidth),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
        ) {
            Swatch(color = color, modifier = Modifier.fillMaxWidth().height(SwatchHeight))

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ChromeText(text = name, role = ChromeTextRole.Label, maxLines = 1)
                    ChromeText(text = color.readout(), role = ChromeTextRole.Detail, maxLines = 1)
                }

                if (description != null) {
                    ChromeText(
                        text = description,
                        role = ChromeTextRole.Detail,
                        tone = ChromeTone.Secondary,
                        maxLines = 3,
                    )
                }
            }
        }
    }
}

/** Receiver a set of palette ramps is written against. */
@Stable
class RampScope internal constructor() {

    /**
     * Adds one ramp, lightest step first.
     *
     * @param name Ramp name as it prefixes its steps on `ArecaBaseColors` — `brand`, `neutral`.
     * @param description What the ramp is for, in one line.
     * @param steps Step suffix to value, in ramp order.
     */
    @Composable
    fun ramp(name: String, description: String? = null, steps: List<Pair<String, Color>>) {
        Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ChromeText(text = name, role = ChromeTextRole.Label, maxLines = 1)
                if (description != null) {
                    ChromeText(text = description, role = ChromeTextRole.Detail, maxLines = 1)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
            ) {
                steps.forEach { (step, color) ->
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Swatch(
                            color = color,
                            modifier = Modifier.fillMaxWidth().height(RampStepHeight),
                        )
                        ChromeText(text = step, role = ChromeTextRole.Detail, maxLines = 1)
                    }
                }
            }
        }
    }
}

/** Receiver a set of foreground samples is written against. */
@Stable
class ForegroundScope internal constructor() {

    /**
     * Adds one foreground token, sampled on each ground it is allowed to land on.
     *
     * Renders the token the three ways it is actually used — as type, as an icon, and as a drawn
     * stroke — over every ground, with the measured ratio under each. A token that is legible in
     * one column and not the next is the failure this section exists to make obvious.
     *
     * @param name Token name exactly as it is spelled on `ArecaColors`.
     * @param color Value the token resolves to in the theme the section is rendering in.
     * @param grounds Ground name to value, in the order a reader should scan them.
     * @param floor Ratio this token is required to clear — 4.5 for type, 3.0 for a graphic.
     * @param description What the token is for, in one line.
     */
    @Composable
    fun foreground(
        name: String,
        color: Color,
        grounds: List<Pair<String, Color>>,
        floor: Double,
        description: String? = null,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ChromeText(text = name, role = ChromeTextRole.Label, maxLines = 1)
                ChromeText(text = color.readout(), role = ChromeTextRole.Detail, maxLines = 1)
                ChromeText(
                    text = "needs ${floor.ratioText()}",
                    role = ChromeTextRole.Detail,
                    maxLines = 1,
                )
            }

            if (description != null) {
                ChromeText(
                    text = description,
                    role = ChromeTextRole.Detail,
                    tone = ChromeTone.Secondary,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                grounds.forEach { (ground, bg) ->
                    Sample(
                        modifier = Modifier.weight(1f),
                        ground = ground,
                        groundColor = bg,
                        color = color,
                        floor = floor,
                    )
                }
            }
        }
    }
}

/**
 * A grid of colour swatches, one per token.
 *
 * @param title Section heading.
 * @param description One sentence saying what the reader should look for here.
 * @param canvasState Background and theme selection for the section.
 * @param content Token declarations.
 */
@Composable
fun ShowcaseScope.Tokens(
    title: String,
    description: String? = null,
    canvasState: PreviewCanvasState = rememberPreviewCanvasState(),
    content: @Composable TokenScope.() -> Unit,
) {
    ShowcaseSectionContainer(title = title, description = description) {
        PreviewCanvasToolbar(state = canvasState)
        PreviewCanvas(state = canvasState, showToolbar = false) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                TokenScope().content()
            }
        }
    }
}

/**
 * The raw palette ramps a theme is assembled from.
 *
 * Ramps carry no theme direction of their own, so this section reads the same in light and dark —
 * what changes with the theme switch is only the ground they are drawn on.
 *
 * @param title Section heading.
 * @param description One sentence saying what the reader should look for here.
 * @param canvasState Background and theme selection for the section.
 * @param content Ramp declarations.
 */
@Composable
fun ShowcaseScope.Ramps(
    title: String,
    description: String? = null,
    canvasState: PreviewCanvasState = rememberPreviewCanvasState(),
    content: @Composable RampScope.() -> Unit,
) {
    ShowcaseSectionContainer(title = title, description = description) {
        PreviewCanvasToolbar(state = canvasState)
        PreviewCanvas(state = canvasState, showToolbar = false) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl2)) {
                RampScope().content()
            }
        }
    }
}

/**
 * Foreground tokens shown doing their job, with the contrast they achieve.
 *
 * @param title Section heading.
 * @param description One sentence saying what the reader should look for here.
 * @param canvasState Background and theme selection for the section.
 * @param content Foreground declarations.
 */
@Composable
fun ShowcaseScope.Foregrounds(
    title: String,
    description: String? = null,
    canvasState: PreviewCanvasState = rememberPreviewCanvasState(),
    content: @Composable ForegroundScope.() -> Unit,
) {
    ShowcaseSectionContainer(title = title, description = description) {
        PreviewCanvasToolbar(state = canvasState)
        PreviewCanvas(state = canvasState, showToolbar = false) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl2)) {
                ForegroundScope().content()
            }
        }
    }
}

/**
 * One foreground on one ground, as type, as an icon and as a stroke.
 */
@Composable
private fun Sample(
    ground: String,
    groundColor: Color,
    color: Color,
    floor: Double,
    modifier: Modifier = Modifier,
) {
    val ratio = contrastRatio(color, groundColor)
    val shape = RoundedCornerShape(ArecaTheme.radius.sm)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(groundColor, shape)
                .border(1.dp, ArecaTheme.colors.borderBase, shape)
                .padding(ArecaTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(SampleDotSize)
                        .clip(CircleShape)
                        .background(color, CircleShape),
                )
                BasicText(
                    text = "Sample",
                    style = ChromeTextRole.Label.style.merge(color = color),
                    maxLines = 1,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SampleRuleHeight)
                    .background(color),
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ChromeText(
                text = if (ratio >= floor) "✓" else "✗",
                role = ChromeTextRole.Detail,
                tone = if (ratio >= floor) ChromeTone.Secondary else ChromeTone.Critical,
                maxLines = 1,
            )
            ChromeText(
                text = "${ratio.ratioText()} on $ground",
                role = ChromeTextRole.Detail,
                maxLines = 1,
            )
        }
    }
}

/**
 * A colour chip.
 *
 * Always bordered: a token whose value is close to the ground it sits on — a subtle border, a
 * surface in the light theme — has no edge of its own, and a swatch with no edge reads as nothing.
 */
@Composable
private fun Swatch(color: Color, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(ArecaTheme.radius.sm)

    Box(
        modifier = modifier
            .clip(shape)
            .background(color, shape)
            .border(1.dp, ArecaTheme.colors.borderBase, shape),
    )
}

/** The value a reader would paste, as `#RRGGBB` plus the alpha when the token is not opaque. */
private fun Color.readout(): String {
    fun channel(value: Float): String =
        (value * 255f).roundToInt().coerceIn(0, 255).toString(16).padStart(2, '0').uppercase()

    val hex = "#${channel(red)}${channel(green)}${channel(blue)}"
    return if (alpha >= 1f) hex else "$hex · ${(alpha * 100).roundToInt()}%"
}

/**
 * WCAG 2.1 relative-luminance contrast between two opaque colours.
 *
 * Deliberately a second copy of the maths in `ArecaColorsContrastTest` rather than a shared helper
 * promoted onto the library's public surface: the test is the enforcement and this is the readout,
 * and neither is worth an exported API that consumers would then depend on.
 */
private fun contrastRatio(a: Color, b: Color): Double {
    val la = a.relativeLuminance()
    val lb = b.relativeLuminance()
    val lighter = maxOf(la, lb)
    val darker = minOf(la, lb)
    return (lighter + 0.05) / (darker + 0.05)
}

private fun Color.relativeLuminance(): Double {
    fun channel(value: Float): Double {
        val c = value.toDouble()
        return if (c <= 0.03928) c / 12.92 else ((c + 0.055) / 1.055).pow(2.4)
    }

    return 0.2126 * channel(red) + 0.7152 * channel(green) + 0.0722 * channel(blue)
}

/** `4.66:1`, rounded to two places without pulling in a formatter. */
private fun Double.ratioText(): String {
    val hundredths = (this * 100).roundToInt()
    return "${hundredths / 100}.${(hundredths % 100).toString().padStart(2, '0')}:1"
}
