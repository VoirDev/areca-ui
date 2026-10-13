package dev.voir.areca.playground.foundation.theme

import androidx.compose.runtime.Composable
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.canvas.PreviewBackground
import dev.voir.areca.playground.showcase.canvas.rememberPreviewCanvasState
import dev.voir.areca.playground.showcase.section.Foregrounds
import dev.voir.areca.playground.showcase.section.Ramps
import dev.voir.areca.playground.showcase.section.Tokens
import dev.voir.areca.theme.ArecaTheme

/**
 * Showcase for the theme's colour tokens.
 *
 * The one page in the playground with no component on it. Every other page answers "what does this
 * control look like"; this one answers "which token do I reach for", which is the question that
 * comes first and had nowhere to be asked.
 *
 * One canvas state drives every section, so the theme switch at the top of any section flips the
 * whole page — which is the point. A token set is only correct if it is correct in both directions,
 * and the failure mode is always a token that stopped moving when the theme did.
 */
@Composable
fun ColorsShowcase() = ShowcaseScaffold(
    title = "Colors",
    summary = "Every token in ArecaColors, and the raw ramps underneath it.",
    module = "foundation",
) {
    val canvas = rememberPreviewCanvasState(initialBackground = PreviewBackground.Canvas)
    val colors = ArecaTheme.colors

    Ramps(
        title = "Palette",
        description = "ArecaBaseColors: the raw material, lightest step first. Swapping this " +
            "re-themes both directions at once.",
        canvasState = canvas,
    ) {
        ramp(
            name = "base",
            description = "Absolute endpoints, unaffected by theme direction.",
            steps = listOf(
                "white" to ArecaTheme.baseColors.baseWhite,
                "black" to ArecaTheme.baseColors.baseBlack,
            ),
        )
        ramp(
            name = "brand",
            description = "Product identity. Drives accents, primary actions and selection.",
            steps = ArecaTheme.baseColors.let {
                listOf(
                    "10" to it.brand10, "20" to it.brand20, "30" to it.brand30, "40" to it.brand40,
                    "50" to it.brand50, "60" to it.brand60, "70" to it.brand70, "80" to it.brand80,
                    "90" to it.brand90, "100" to it.brand100,
                )
            },
        )
        ramp(
            name = "neutral",
            description = "Surfaces, text and borders. The ramp the theme direction inverts.",
            steps = ArecaTheme.baseColors.let {
                listOf(
                    "10" to it.neutral10, "20" to it.neutral20, "30" to it.neutral30,
                    "40" to it.neutral40, "50" to it.neutral50, "60" to it.neutral60,
                    "70" to it.neutral70, "80" to it.neutral80, "90" to it.neutral90,
                    "100" to it.neutral100,
                )
            },
        )
        ramp(
            name = "green",
            description = "Success and positive change.",
            steps = ArecaTheme.baseColors.let {
                listOf(
                    "10" to it.green10, "20" to it.green20, "30" to it.green30, "40" to it.green40,
                    "50" to it.green50, "60" to it.green60, "70" to it.green70, "80" to it.green80,
                    "90" to it.green90, "100" to it.green100,
                )
            },
        )
        ramp(
            name = "blue",
            description = "Information and neutral emphasis.",
            steps = ArecaTheme.baseColors.let {
                listOf(
                    "10" to it.blue10, "20" to it.blue20, "30" to it.blue30, "40" to it.blue40,
                    "50" to it.blue50, "60" to it.blue60, "70" to it.blue70, "80" to it.blue80,
                    "90" to it.blue90, "100" to it.blue100,
                )
            },
        )
        ramp(
            name = "yellow",
            description = "Warnings. Lighter than the other ramps at equal steps.",
            steps = ArecaTheme.baseColors.let {
                listOf(
                    "10" to it.yellow10, "20" to it.yellow20, "30" to it.yellow30,
                    "40" to it.yellow40, "50" to it.yellow50, "60" to it.yellow60,
                    "70" to it.yellow70, "80" to it.yellow80, "90" to it.yellow90,
                    "100" to it.yellow100,
                )
            },
        )
        ramp(
            name = "red",
            description = "Errors and destructive actions.",
            steps = ArecaTheme.baseColors.let {
                listOf(
                    "10" to it.red10, "20" to it.red20, "30" to it.red30, "40" to it.red40,
                    "50" to it.red50, "60" to it.red60, "70" to it.red70, "80" to it.red80,
                    "90" to it.red90, "100" to it.red100,
                )
            },
        )
    }

    Tokens(
        title = "Backgrounds",
        description = "What a region is drawn on: the surface stack first, then one muted / base " +
            "/ intense trio per semantic family.",
        canvasState = canvas,
    ) {
        token("bgCanvas", colors.bgCanvas, "The page itself. Nothing sits behind it.")
        token("bgSubtle", colors.bgSubtle, "Set off from the canvas without a border.")
        token("bgMuted", colors.bgMuted, "Further back than bgSubtle: an inset well, a disabled track.")
        token("bgSurface", colors.bgSurface, "A raised container: cards, sheets, popovers.")
        token("bgContrast", colors.bgContrast, "The inverted surface. Pair with fgOnContrast.")
        token("bgContrastMuted", colors.bgContrastMuted, "A weaker inverted surface.")
        token("bgOverlayDark", colors.bgOverlayDark, "Scrim darkening what is behind a modal.")
        token("bgOverlayLight", colors.bgOverlayLight, "Scrim lightening what is behind it.")
        token("bgDisabled", colors.bgDisabled, "Fill of a control the user cannot act on.")

        token("bgBrandMuted", colors.bgBrandMuted, "Tinted brand ground: a chip, a selected row.")
        token("bgBrandBase", colors.bgBrandBase, "Solid brand fill. Pair with fgOnFill.")
        token("bgBrandIntense", colors.bgBrandIntense, "Brand fill under press or hover.")

        token("bgDangerMuted", colors.bgDangerMuted, "Tinted ground for an inline error region.")
        token("bgDangerBase", colors.bgDangerBase, "Solid danger fill. Pair with fgOnFill.")
        token("bgDangerIntense", colors.bgDangerIntense, "Danger fill under press or hover.")

        token("bgSuccessMuted", colors.bgSuccessMuted, "Tinted ground for a confirmation region.")
        token("bgSuccessBase", colors.bgSuccessBase, "Solid success fill. Pair with fgOnFill.")
        token("bgSuccessIntense", colors.bgSuccessIntense, "Success fill under press or hover.")

        token("bgWarnMuted", colors.bgWarnMuted, "Tinted ground for a caution region.")
        token("bgWarnBase", colors.bgWarnBase, "Solid warning fill. Light in both themes, so pair with fgOnWarn.")
        token("bgWarnIntense", colors.bgWarnIntense, "Warning fill under press or hover.")

        token("bgInfoMuted", colors.bgInfoMuted, "Tinted informational ground.")
        token("bgInfoBase", colors.bgInfoBase, "Solid informational fill. Pair with fgOnFill.")
        token("bgInfoIntense", colors.bgInfoIntense, "Informational fill under press or hover.")

        token("bgNeutralMuted", colors.bgNeutralMuted, "Tinted ground for a status carrying no sentiment.")
        token("bgNeutralBase", colors.bgNeutralBase, "Solid neutral fill.")
        token("bgNeutralIntense", colors.bgNeutralIntense, "Neutral fill under press or hover.")
    }

    Foregrounds(
        title = "Semantic foregrounds",
        description = "Each family carries two: an icon step that keeps the colour's identity, " +
            "and a text step dark enough to read as body copy. Check both columns and the chip.",
        canvasState = canvas,
    ) {
        foreground(
            name = "fgBrand",
            color = colors.fgBrand,
            grounds = listOf(
                "Canvas" to colors.bgCanvas,
                "Surface" to colors.bgSurface,
                "Brand muted" to colors.bgBrandMuted,
            ),
            floor = 3.0,
            description = "Brand icons, graphics and rules.",
        )
        foreground(
            name = "fgBrandText",
            color = colors.fgBrandText,
            grounds = listOf(
                "Canvas" to colors.bgCanvas,
                "Surface" to colors.bgSurface,
                "Brand muted" to colors.bgBrandMuted,
            ),
            floor = 4.5,
            description = "Brand type.",
        )

        foreground(
            name = "fgDanger",
            color = colors.fgDanger,
            grounds = listOf(
                "Canvas" to colors.bgCanvas,
                "Surface" to colors.bgSurface,
                "Danger muted" to colors.bgDangerMuted,
            ),
            floor = 3.0,
            description = "Danger icons, graphics and rules.",
        )
        foreground(
            name = "fgDangerText",
            color = colors.fgDangerText,
            grounds = listOf(
                "Canvas" to colors.bgCanvas,
                "Surface" to colors.bgSurface,
                "Danger muted" to colors.bgDangerMuted,
            ),
            floor = 4.5,
            description = "Danger type, as in a field error message.",
        )

        foreground(
            name = "fgSuccess",
            color = colors.fgSuccess,
            grounds = listOf(
                "Canvas" to colors.bgCanvas,
                "Surface" to colors.bgSurface,
                "Success muted" to colors.bgSuccessMuted,
            ),
            floor = 3.0,
            description = "Success icons, graphics and rules.",
        )
        foreground(
            name = "fgSuccessText",
            color = colors.fgSuccessText,
            grounds = listOf(
                "Canvas" to colors.bgCanvas,
                "Surface" to colors.bgSurface,
                "Success muted" to colors.bgSuccessMuted,
            ),
            floor = 4.5,
            description = "Success type.",
        )

        foreground(
            name = "fgInfo",
            color = colors.fgInfo,
            grounds = listOf(
                "Canvas" to colors.bgCanvas,
                "Surface" to colors.bgSurface,
                "Info muted" to colors.bgInfoMuted,
            ),
            floor = 3.0,
            description = "Informational icons, graphics and rules.",
        )
        foreground(
            name = "fgInfoText",
            color = colors.fgInfoText,
            grounds = listOf(
                "Canvas" to colors.bgCanvas,
                "Surface" to colors.bgSurface,
                "Info muted" to colors.bgInfoMuted,
            ),
            floor = 4.5,
            description = "Informational type.",
        )

        foreground(
            name = "fgWarn",
            color = colors.fgWarn,
            grounds = listOf(
                "Canvas" to colors.bgCanvas,
                "Surface" to colors.bgSurface,
                "Warn muted" to colors.bgWarnMuted,
            ),
            floor = 3.0,
            description = "Warning icons, graphics and rules.",
        )
        foreground(
            name = "fgWarnText",
            color = colors.fgWarnText,
            grounds = listOf(
                "Canvas" to colors.bgCanvas,
                "Surface" to colors.bgSurface,
                "Warn muted" to colors.bgWarnMuted,
            ),
            floor = 4.5,
            description = "Warning type.",
        )

        foreground(
            name = "fgNeutral",
            color = colors.fgNeutral,
            grounds = listOf(
                "Canvas" to colors.bgCanvas,
                "Neutral muted" to colors.bgNeutralMuted,
                "Neutral base" to colors.bgNeutralBase,
            ),
            floor = 4.5,
            description = "The one family with a single foreground: it also carries content on " +
                "the neutral fills, where a lighter step drops below AA.",
        )
    }

    Tokens(
        title = "Structural foregrounds",
        description = "The foregrounds that carry no sentiment — the type ramp, and the three " +
            "tokens naming what sits on a fill.",
        canvasState = canvas,
    ) {
        token("fgBase", colors.fgBase, "Primary text and icons. Clears 7:1 on every resting background.")
        token("fgSubtle", colors.fgSubtle, "Secondary text: supporting lines, inactive labels.")
        token("fgMuted", colors.fgMuted, "Tertiary text: placeholders, timestamps, decorative icons.")
        token("fgOnContrast", colors.fgOnContrast, "Content on bgContrast.")
        token("fgOnFill", colors.fgOnFill, "Content on any solid semantic fill except bgWarnBase.")
        token("fgOnWarn", colors.fgOnWarn, "Content on bgWarnBase, which is light in both themes.")
        token("fgDisabled", colors.fgDisabled, "Text and icons of a control the user cannot act on.")
    }

    Tokens(
        title = "Borders",
        description = "Four neutral steps for separation, then one per state a control can be in.",
        canvasState = canvas,
    ) {
        token("borderSubtle", colors.borderSubtle, "Barely visible divider between rows of a group.")
        token("borderBase", colors.borderBase, "Default border: a card or input at rest.")
        token("borderStrong", colors.borderStrong, "Separates more clearly than borderBase — a hovered control.")
        token("borderIntense", colors.borderIntense, "Strongest neutral border: focused or pressed.")
        token("borderDisabled", colors.borderDisabled, "Border of a control the user cannot act on.")
        token("borderDanger", colors.borderDanger, "Border of a control in an error state.")
        token("borderSuccess", colors.borderSuccess, "Border of a control in a success state.")
        token("borderInfo", colors.borderInfo, "Border of a control carrying information.")
        token("borderWarn", colors.borderWarn, "Border of a control in a warning state.")
    }

    Tokens(
        title = "Accents",
        description = "Selection and focus. Separate from the brand foregrounds so a consumer can " +
            "retune what selection looks like without moving brand text.",
        canvasState = canvas,
    ) {
        token("accentMuted", colors.accentMuted, "Accent tint behind selected content.")
        token("accentSubtle", colors.accentSubtle, "Weak accent, for an indicator that should not compete.")
        token("accentBase", colors.accentBase, "Default accent: a checked control, a focus ring, an active tab.")
        token("accentStrong", colors.accentStrong, "Accent under press or hover.")
        token("accentIntense", colors.accentIntense, "Strongest accent, for a held or dragged state.")
    }
}
