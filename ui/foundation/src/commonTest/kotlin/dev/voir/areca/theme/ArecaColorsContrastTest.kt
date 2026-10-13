package dev.voir.areca.theme

import androidx.compose.ui.graphics.Color
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.filledBackground
import dev.voir.areca.foundation.button.mutedBackground
import dev.voir.areca.foundation.button.onFilled
import dev.voir.areca.foundation.button.onMuted
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Contrast floors the light and dark themes must both clear.
 *
 * A dark theme is easy to write and easy to get subtly wrong: a token that stays light, or a
 * foreground that no longer separates from the surface it moved onto. These assertions pin the
 * pairings that actually appear on screen, so a remapped token cannot quietly become unreadable.
 *
 * Ratios are WCAG 2.1 relative-luminance contrast. Body text targets 4.5:1, primary text 7:1, and
 * secondary text 3:1 — secondary text is deliberately lower because it is supporting copy, never the
 * only carrier of meaning.
 */
class ArecaColorsContrastTest {

    @Test
    fun `primary text clears AAA on every resting surface`() {
        forEachTheme { name, colors ->
            colors.assertContrast(name, "fgBase on bgCanvas", colors.fgBase, colors.bgCanvas, 7.0)
            colors.assertContrast(name, "fgBase on bgSurface", colors.fgBase, colors.bgSurface, 7.0)
            colors.assertContrast(name, "fgBase on bgSubtle", colors.fgBase, colors.bgSubtle, 7.0)
            colors.assertContrast(name, "fgBase on bgMuted", colors.fgBase, colors.bgMuted, 7.0)
        }
    }

    @Test
    fun `secondary and muted text stay legible`() {
        forEachTheme { name, colors ->
            colors.assertContrast(name, "fgSubtle on bgCanvas", colors.fgSubtle, colors.bgCanvas, 4.5)
            colors.assertContrast(name, "fgSubtle on bgSurface", colors.fgSubtle, colors.bgSurface, 4.5)
            colors.assertContrast(name, "fgMuted on bgCanvas", colors.fgMuted, colors.bgCanvas, 3.0)
            colors.assertContrast(name, "fgMuted on bgSurface", colors.fgMuted, colors.bgSurface, 3.0)
        }
    }

    /**
     * `fgOnFill` is the foreground for solid semantic fills — brand buttons, danger badges, the
     * checkbox mark. `bgWarnBase` is excluded because callers pair it with a dark foreground
     * instead; white on the warning yellow does not reach a usable ratio in either theme.
     */
    @Test
    fun `content on a solid fill clears AA`() {
        forEachTheme { name, colors ->
            val fg = colors.fgOnFill
            colors.assertContrast(name, "fgOnFill on bgBrandBase", fg, colors.bgBrandBase, 4.5)
            colors.assertContrast(name, "fgOnFill on bgDangerBase", fg, colors.bgDangerBase, 4.5)
            colors.assertContrast(name, "fgOnFill on bgSuccessBase", fg, colors.bgSuccessBase, 4.5)
            colors.assertContrast(name, "fgOnFill on bgInfoBase", fg, colors.bgInfoBase, 4.5)
        }
    }

    /**
     * The warning fill is light in both themes, so it is the one semantic fill whose content is
     * dark rather than white. `fgOnWarn` exists so that rule lives in the theme instead of being
     * re-derived as a special case in every component that offers a warning variant.
     */
    @Test
    fun `content on the warning fill clears AA`() {
        forEachTheme { name, colors ->
            colors.assertContrast(name, "fgOnWarn on bgWarnBase", colors.fgOnWarn, colors.bgWarnBase, 4.5)
        }
    }

    /**
     * The neutral family is the low-emphasis fill — a Neutral button, a Neutral badge — and the one
     * components would otherwise be tempted to read straight from the palette.
     */
    @Test
    fun `content on a neutral fill clears AA`() {
        forEachTheme { name, colors ->
            val fg = colors.fgNeutral
            colors.assertContrast(name, "fgNeutral on bgNeutralMuted", fg, colors.bgNeutralMuted, 4.5)
            colors.assertContrast(name, "fgNeutral on bgNeutralBase", fg, colors.bgNeutralBase, 4.5)
        }
    }

    /**
     * `fgOnContrast` means content on the inverted surface and nothing else. Keeping it separate
     * from [fgOnFill] is what lets `bgContrast` actually invert in dark instead of staying a dark
     * neutral so one shared foreground could serve both roles.
     */
    @Test
    fun `content on the inverted surface clears AA`() {
        forEachTheme { name, colors ->
            colors.assertContrast(
                name,
                "fgOnContrast on bgContrast",
                colors.fgOnContrast,
                colors.bgContrast,
                4.5,
            )
            colors.assertContrast(
                name,
                "fgOnContrast on bgContrastMuted",
                colors.fgOnContrast,
                colors.bgContrastMuted,
                4.5,
            )
        }
    }

    /**
     * Status text on the page. These cleared only 3.2–3.4:1 before the semantic foregrounds moved
     * down the ramp, which is below AA for body text.
     */
    @Test
    fun `semantic status text clears AA on the canvas and on a surface`() {
        forEachTheme { name, colors ->
            val grounds = listOf("bgCanvas" to colors.bgCanvas, "bgSurface" to colors.bgSurface)
            grounds.forEach { (ground, bg) ->
                colors.assertContrast(name, "fgBrandText on $ground", colors.fgBrandText, bg, 4.5)
                colors.assertContrast(name, "fgDangerText on $ground", colors.fgDangerText, bg, 4.5)
                colors.assertContrast(name, "fgSuccessText on $ground", colors.fgSuccessText, bg, 4.5)
                colors.assertContrast(name, "fgInfoText on $ground", colors.fgInfoText, bg, 4.5)
                colors.assertContrast(name, "fgWarnText on $ground", colors.fgWarnText, bg, 4.5)
            }
        }
    }

    /**
     * The same text on its own tinted chip — a danger message inside a danger-muted panel. This is
     * the pairing that kept the foregrounds moving: it fails at both step 50 and step 60, so it is
     * what fixes the ramp position rather than the canvas pairing above.
     */
    @Test
    fun `semantic status text clears AA on its own muted ground`() {
        forEachTheme { name, colors ->
            colors.assertContrast(name, "fgBrandText on bgBrandMuted", colors.fgBrandText, colors.bgBrandMuted, 4.5)
            colors.assertContrast(name, "fgDangerText on bgDangerMuted", colors.fgDangerText, colors.bgDangerMuted, 4.5)
            colors.assertContrast(
                name,
                "fgSuccessText on bgSuccessMuted",
                colors.fgSuccessText,
                colors.bgSuccessMuted,
                4.5,
            )
            colors.assertContrast(name, "fgInfoText on bgInfoMuted", colors.fgInfoText, colors.bgInfoMuted, 4.5)
            colors.assertContrast(name, "fgWarnText on bgWarnMuted", colors.fgWarnText, colors.bgWarnMuted, 4.5)
        }
    }

    /**
     * The icon half of each semantic family, on every ground it can land on.
     *
     * The floor is 3:1 rather than the 4.5:1 its text sibling owes, because these tokens colour
     * icons, rules and indicator dots, not type — and buying the extra ratio is exactly what pushed
     * them dark enough to read as brown. They do clear 4.5:1 on the canvas today, but only by 0.02
     * at the tightest (`yellow60` on white), so pinning the test there would make an unrelated
     * palette nudge fail for the wrong reason.
     */
    @Test
    fun `semantic icons clear the non-text floor everywhere`() {
        forEachTheme { name, colors ->
            val shared = listOf(
                "bgCanvas" to colors.bgCanvas,
                "bgSurface" to colors.bgSurface,
                "bgMuted" to colors.bgMuted,
            )
            val families = listOf(
                Triple("fgBrand", colors.fgBrand, colors.bgBrandMuted),
                Triple("fgDanger", colors.fgDanger, colors.bgDangerMuted),
                Triple("fgSuccess", colors.fgSuccess, colors.bgSuccessMuted),
                Triple("fgInfo", colors.fgInfo, colors.bgInfoMuted),
                Triple("fgWarn", colors.fgWarn, colors.bgWarnMuted),
            )

            families.forEach { (token, fg, ownMuted) ->
                shared.forEach { (ground, bg) ->
                    colors.assertContrast(name, "$token on $ground", fg, bg, 3.0)
                }
                colors.assertContrast(name, "$token on its own muted ground", fg, ownMuted, 3.0)
            }
        }
    }

    /**
     * A solid featured icon reverses its glyph out of the fill, so every family's glyph has to clear
     * the non-text floor on its own fill — including the inverted one, which turns light in dark.
     */
    @Test
    fun `solid featured icon glyphs clear the non-text floor on their fill`() {
        forEachTheme { name, colors ->
            ActionColor.entries.forEach { color ->
                val style = FeaturedIconStyle.solid(color, colors)
                val fill = assertNotNull(style.bg, "$name: solid $color has no fill")
                colors.assertContrast(name, "solid $color glyph on its fill", style.content, fill, 3.0)
            }
        }
    }

    /**
     * The label of a solid button or icon button, on its own fill, for every action family.
     *
     * Pins the pairing the components actually draw rather than the tokens behind it, so remapping
     * which token a family's content reads cannot silently drop a label below AA.
     */
    @Test
    fun `solid action content clears AA on its fill`() {
        forEachTheme { name, colors ->
            ActionColor.entries.forEach { color ->
                colors.assertContrast(
                    name,
                    "solid $color content on its fill",
                    color.onFilled(colors),
                    color.filledBackground(colors),
                    4.5,
                )
            }
        }
    }

    /** The label of a soft button or the glyph of a soft icon button, on its tint. */
    @Test
    fun `soft action content clears AA on its tint`() {
        forEachTheme { name, colors ->
            ActionColor.entries.forEach { color ->
                colors.assertContrast(
                    name,
                    "soft $color content on its tint",
                    color.onMuted(colors),
                    color.mutedBackground(colors),
                    4.5,
                )
            }
        }
    }

    /**
     * The soft brand button's label departs from the design's accent pairing, which sits at
     * 4.0–4.4:1 on the brand tint, to clear AA.
     */
    @Test
    fun `soft brand button label clears AA on the brand tint`() {
        forEachTheme { name, colors ->
            colors.assertContrast(
                name,
                "soft Brand label on bgBrandMuted",
                ActionColor.Brand.onMuted(colors),
                colors.bgBrandMuted,
                4.5,
            )
        }
    }

    /**
     * A soft featured icon's glyph is a mark on its family's tint, so it owes the non-text floor
     * there in both themes.
     */
    @Test
    fun `soft featured icon glyphs clear the non-text floor on their tint`() {
        forEachTheme { name, colors ->
            ActionColor.entries.forEach { color ->
                val style = FeaturedIconStyle.soft(color, colors)
                val fill = assertNotNull(style.bg, "$name: soft $color has no fill")
                colors.assertContrast(name, "soft $color glyph on its tint", style.content, fill, 3.0)
            }
        }
    }

    /**
     * The split has to stay a split.
     *
     * Both halves are drawn from the same ramp one step apart, so a palette edit that flattens the
     * ramp there would leave every token still passing its own floor while silently undoing the
     * reason they are two tokens.
     */
    @Test
    fun `text and icon foregrounds never collapse onto one value`() {
        forEachTheme { name, colors ->
            val pairs = mapOf(
                "brand" to (colors.fgBrand to colors.fgBrandText),
                "danger" to (colors.fgDanger to colors.fgDangerText),
                "success" to (colors.fgSuccess to colors.fgSuccessText),
                "info" to (colors.fgInfo to colors.fgInfoText),
                "warn" to (colors.fgWarn to colors.fgWarnText),
            )
            val collapsed = pairs.filterValues { (icon, text) -> icon == text }.keys

            assertTrue(
                collapsed.isEmpty(),
                "$name: icon and text foregrounds share one value for: $collapsed",
            )
        }
    }

    /**
     * The surface ramp has to stay a ramp. Separation is deliberately slight in places — a resting
     * border on the light canvas is barely over 1.1:1 — so the requirement is not a ratio floor but
     * that no two roles collapse onto the same value. That is the failure mode when a theme runs out
     * of neutral steps and reuses one for two roles.
     */
    @Test
    fun `no two surface roles share a value`() {
        forEachTheme { name, colors ->
            val ramp = mapOf(
                "bgCanvas" to colors.bgCanvas,
                "bgSubtle" to colors.bgSubtle,
                "bgMuted" to colors.bgMuted,
                "bgContrast" to colors.bgContrast,
            )
            val collisions = ramp.entries
                .flatMapIndexed { index, left ->
                    ramp.entries.drop(index + 1)
                        .filter { right -> left.value == right.value }
                        .map { right -> "${left.key} == ${right.key}" }
                }

            assertTrue(collisions.isEmpty(), "$name: surface roles collapsed onto one value: $collisions")
        }
    }

    /**
     * `bgSurface` intentionally equals `bgCanvas` in light, so it is checked separately: what must
     * hold everywhere is that a border is visible against the surface it encloses.
     */
    @Test
    fun `borders separate from the surface they enclose`() {
        forEachTheme { name, colors ->
            assertTrue(
                colors.borderBase != colors.bgSurface,
                "$name: borderBase is the same color as bgSurface, so edges disappear",
            )
        }
    }

    @Test
    fun `the two themes are actually different`() {
        val light = ArecaColors.light()
        val dark = ArecaColors.dark()

        assertTrue(
            light.bgCanvas != dark.bgCanvas && light.fgBase != dark.fgBase,
            "dark() returned the light mapping for the core surface and foreground tokens",
        )
        assertTrue(
            contrastRatio(dark.bgCanvas, Color.White) > contrastRatio(light.bgCanvas, Color.White),
            "dark() canvas is not darker than light() canvas",
        )
    }

    private fun forEachTheme(block: (String, ArecaColors) -> Unit) {
        block("light", ArecaColors.light())
        block("dark", ArecaColors.dark())
    }

    private fun ArecaColors.assertContrast(
        theme: String,
        pair: String,
        foreground: Color,
        background: Color,
        minimum: Double,
    ) {
        val ratio = contrastRatio(foreground, background)
        assertTrue(
            ratio >= minimum,
            "$theme: $pair is ${ratio.format()}:1, below the ${minimum.format()}:1 floor",
        )
    }
}

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

private fun Double.format(): String {
    val scaled = (this * 100).toLong()
    return "${scaled / 100}.${(scaled % 100).toString().padStart(2, '0')}"
}
