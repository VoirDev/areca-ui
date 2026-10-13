package dev.voir.areca.playground.foundation.indication

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.LiquidSweepIndication
import dev.voir.areca.indication.StrokeIndication
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The indication implementations shipped with Areca, and the presets built on them. */
private enum class IndicationKind(
    /** One line on what the indication draws, shown under its cell. */
    val summary: String,
) {
    /** [TouchIndication]. */
    Touch("hover overlay plus a press radial"),

    /** [StrokeIndication]. */
    Stroke("a stroke that draws itself around the edge"),

    /** [FadeIndication] with a caller-picked colour. */
    Fade("a flat colour fade"),

    /** [FadeIndication.tint]: the press of a neutral list row and a default pressable card. */
    FadeTint("FadeIndication.tint — a tint of the copy colour"),

    /** [LiquidSweepIndication]. */
    LiquidSweep("a sweep that travels across the surface"),
}

/**
 * Showcase for the [Indication] implementations.
 *
 * Every example is interactive by necessity: an indication has no resting appearance, so it can only
 * be judged by hovering, pressing and focusing it.
 */
@Composable
fun IndicationShowcase() = ShowcaseScaffold(
    title = "Indication",
    summary = "Press, hover and focus feedback shared by every interactive component.",
    module = "foundation",
) {
    Playground(description = "Hover, press and tab to the surface — there is nothing to see at rest.") {
        val kind by enumControl(
            key = "indication",
            default = IndicationKind.Touch,
            options = IndicationKind.entries,
            optionLabel = { it.name },
        )
        val circular by booleanControl(
            key = "circular",
            default = false,
            description = "Indications follow the shape they are given.",
        )

        preview {
            val shape = if (circular) CircleShape else RoundedCornerShape(ArecaTheme.radius.lg)

            IndicationSurface(
                label = kind.name,
                indication = kind.resolve(shape),
                shape = shape,
            )
        }
    }

    Variants(
        title = "Implementations",
        description = "Interact with each. They differ most on press, least at rest.",
        layout = VariantLayout.Grid,
    ) {
        for (kind in IndicationKind.entries) {
            variant(name = kind.name, detail = kind.summary) {
                val shape = RoundedCornerShape(ArecaTheme.radius.lg)
                IndicationSurface(
                    label = kind.name,
                    indication = kind.resolve(shape),
                    shape = shape,
                )
            }
        }
    }

    Variants(
        title = "FadeIndication.tint",
        description = "The preset darkens a surface towards its own copy; a dangerous one reddens.",
        layout = VariantLayout.Grid,
    ) {
        variant("ink = fgBase", detail = "the default — cards and neutral rows") {
            val shape = RoundedCornerShape(ArecaTheme.radius.lg)
            IndicationSurface("Open invoice", FadeIndication.tint(shape), shape)
        }
        variant("ink = fgDanger", detail = "a destructive row or tile") {
            val shape = RoundedCornerShape(ArecaTheme.radius.lg)
            IndicationSurface(
                label = "Delete account",
                indication = FadeIndication.tint(shape, ink = ArecaTheme.colors.fgDanger),
                shape = shape,
            )
        }
        variant("shape = CircleShape", detail = "an avatar") {
            IndicationSurface("Avatar", FadeIndication.tint(CircleShape), CircleShape)
        }
    }

    Variants(
        title = "Shapes",
        description = "The shape passed to the indication must match the surface it decorates.",
        layout = VariantLayout.Grid,
    ) {
        variant("Rounded") {
            val shape = RoundedCornerShape(ArecaTheme.radius.lg)
            IndicationSurface("Rounded", TouchIndication(shape = shape), shape)
        }
        variant("Circle") {
            IndicationSurface("Circle", TouchIndication(shape = CircleShape), CircleShape)
        }
        variant("Square") {
            val shape = RoundedCornerShape(0.dp)
            IndicationSurface("Square", TouchIndication(shape = shape), shape)
        }
    }

    EdgeCases {
        case(
            name = "Mismatched shape",
            expectation = "Indication corners do not follow the surface — the shape must be passed to both.",
        ) {
            IndicationSurface(
                label = "Mismatched",
                indication = TouchIndication(shape = RoundedCornerShape(0.dp)),
                shape = CircleShape,
            )
        }
        case(
            name = "Keyboard focus",
            expectation = "Tab to this surface: a focus outline appears without any pointer input.",
        ) {
            val shape = RoundedCornerShape(ArecaTheme.radius.lg)
            IndicationSurface("Tab to me", TouchIndication(shape = shape), shape)
        }
    }
}

/**
 * Builds this kind of indication for a surface of [shape].
 *
 * @param shape Shape of the surface the indication decorates.
 * @return The indication, coloured from the current theme.
 */
@Composable
private fun IndicationKind.resolve(shape: Shape): Indication = when (this) {
    IndicationKind.Touch -> TouchIndication(
        shape = shape,
        color = ArecaTheme.colors.fgBase,
    )

    IndicationKind.Stroke -> StrokeIndication(
        shape = shape,
        strokeColor = ArecaTheme.colors.accentBase,
    )

    IndicationKind.Fade -> FadeIndication(
        shape = shape,
        hoverColor = ArecaTheme.colors.bgBrandMuted.copy(alpha = 0.5f),
        pressedColor = ArecaTheme.colors.bgBrandMuted,
    )

    IndicationKind.FadeTint -> FadeIndication.tint(shape)

    IndicationKind.LiquidSweep -> LiquidSweepIndication(
        shape = shape,
        color = ArecaTheme.colors.accentBase,
    )
}

/**
 * A plain pressable surface that shows nothing but its label and [indication].
 *
 * @param label Copy drawn in the middle of the surface.
 * @param indication Feedback drawn on hover, press and focus.
 * @param shape Shape of the surface, which [indication] should follow.
 */
@Composable
private fun IndicationSurface(
    label: String,
    indication: Indication,
    shape: Shape,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(width = 160.dp, height = 72.dp)
            .background(ArecaTheme.colors.bgSubtle, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                onClick = {},
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, variant = TextVariant.LabelMedium, color = TextColor.Subtle)
    }
}
