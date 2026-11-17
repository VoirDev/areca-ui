package dev.voir.areca.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/**
 * How the grounds of a screen alternate: which of the page and the cards on it is grey.
 *
 * Both layouts keep one rule — a surface laid on another takes the other ground — so a card always
 * reads as a card, a field or a chip inside it always reads as set into it, and none of them has to
 * know what it sits on. In the dark theme both layouts share one ladder, where a surface nearer the
 * eye is lighter.
 */
public enum class SurfaceLayout {
    /** A grey page with white cards on it, the grouped look of a settings or finance screen. */
    Grouped,

    /** A white page with grey cards on it. */
    Plain,
}

/** Where a surface sits on the ladder of the root it belongs to. */
public enum class SurfaceLevel {
    /** The ground of a screen or a sheet itself. */
    Page,

    /** A card, a section or a notice laid on the page. */
    Card,

    /** A tile, a group or a panel set inside a card. Deeper nesting alternates with [Card]. */
    Inset,
}

/** Which ladder a surface belongs to: a screen's, or one that opens over it. */
public enum class SurfaceRoot {
    /** A screen, framed by `BaseLayout`. */
    Screen,

    /** A sheet, a dialog or a menu: always white in the light theme, lifted in the dark. */
    Sheet,
}

/**
 * The colours one surface hands to what is drawn on it, by role.
 *
 * Components read the roles of the surface they sit on — [ArecaTheme.surface] — instead of a fixed
 * token, which is what lets one card or one field be right on a grey page, a white card and a sheet.
 *
 * @property ground The surface's own fill.
 * @property raised The fill of a card, a section or a neutral notice laid on this surface.
 * @property well The fill of something set into this surface: a track, a filled field, a soft chip,
 *   a skeleton, a footer band.
 * @property onWell The fill of something raised inside a [well]: a selector's thumb, a switch knob.
 * @property separator A hairline drawn on this surface, between rows.
 * @property field The fill of an outlined field or a stepper on this surface: white wherever it is
 *   in the light theme, a step lighter than the surface in the dark.
 * @property band The fill of a strip that closes this surface along its edge — a card's footer, a
 *   section's band. A step past the [well], so it reads against the surface *and* against the page
 *   around it: a band in a white card's light well would run into a grey page. Defaults to [well].
 */
@Immutable
public data class ArecaSurface(
    public val ground: Color,
    public val raised: Color,
    public val well: Color,
    public val onWell: Color,
    public val separator: Color,
    public val field: Color,
    public val band: Color = well,
) {
    /**
     * @param role The role to read.
     * @return This surface's colour for [role].
     */
    public operator fun get(role: SurfaceRole): Color = when (role) {
        SurfaceRole.Ground -> ground
        SurfaceRole.Raised -> raised
        SurfaceRole.Well -> well
        SurfaceRole.OnWell -> onWell
        SurfaceRole.Separator -> separator
        SurfaceRole.Field -> field
        SurfaceRole.Band -> band
    }
}

/**
 * One of the colours an [ArecaSurface] hands out, named so a style can hold a role instead of a
 * colour and resolve it where it is drawn — the same field style is then right on a grey page and
 * inside a white card.
 */
public enum class SurfaceRole {
    /** [ArecaSurface.ground]. */
    Ground,

    /** [ArecaSurface.raised]. */
    Raised,

    /** [ArecaSurface.well]. */
    Well,

    /** [ArecaSurface.onWell]. */
    OnWell,

    /** [ArecaSurface.separator]. */
    Separator,

    /** [ArecaSurface.field]. */
    Field,

    /** [ArecaSurface.band]. */
    Band,
}

/**
 * The surfaces of one root, from its page inwards.
 *
 * @property page The root's own ground.
 * @property card A surface laid on [page].
 * @property inset A surface set inside [card]. A surface laid on an inset is a [card] again.
 */
@Immutable
public data class SurfaceLadder(
    public val page: ArecaSurface,
    public val card: ArecaSurface,
    public val inset: ArecaSurface,
) {
    /**
     * @param level The level to read.
     * @return The surface at [level].
     */
    public fun at(level: SurfaceLevel): ArecaSurface = when (level) {
        SurfaceLevel.Page -> page
        SurfaceLevel.Card -> card
        SurfaceLevel.Inset -> inset
    }
}

/**
 * Every surface of the theme: the screen's ladder, the sheet's ladder and the bars.
 *
 * Built by [from] from the neutral ramp of the theme's base colours, so an application that
 * re-themes its neutrals re-themes its surfaces with them. Pass a whole value to
 * `ArecaThemeProvider(surfaces = …)` to set every ground by hand.
 *
 * @property layout Which of the page and its cards is grey.
 * @property screen Grounds of a screen, from its page inwards.
 * @property sheet Grounds of a sheet, a dialog or a menu, from its own ground inwards.
 * @property bar Ground of the bars docked to a screen's edges — a tab bar, a bottom action bar.
 */
@Immutable
public data class ArecaSurfaces(
    public val layout: SurfaceLayout,
    public val screen: SurfaceLadder,
    public val sheet: SurfaceLadder,
    public val bar: Color,
) {
    /**
     * @param root The ladder to read.
     * @return [screen] or [sheet].
     */
    public fun ladder(root: SurfaceRoot): SurfaceLadder = when (root) {
        SurfaceRoot.Screen -> screen
        SurfaceRoot.Sheet -> sheet
    }

    /** Builders. */
    public companion object {
        /**
         * Builds the surfaces of a theme from its base colours.
         *
         * In the light theme the two layouts alternate white and grey from opposite ends; a sheet is
         * white in both, with grey cards inside it. In the dark theme both layouts share one ladder
         * on which a surface nearer the eye is lighter: page, card, inset; a sheet starts a step above
         * the page.
         *
         * @param baseColors The theme's raw palette.
         * @param layout Which of the page and its cards is grey.
         * @param darkTheme Whether the surfaces are for the dark theme.
         * @return The surfaces.
         */
        public fun from(
            baseColors: ArecaBaseColors,
            layout: SurfaceLayout,
            darkTheme: Boolean,
        ): ArecaSurfaces = if (darkTheme) dark(baseColors, layout) else light(baseColors, layout)

        private fun light(c: ArecaBaseColors, layout: SurfaceLayout): ArecaSurfaces {
            val white = c.baseWhite
            // A white surface: the page of a plain layout, a card of a grouped one, a sheet.
            // Its well — a filled field, a chip, a track — is the light grey; the band closing it is a
            // full step past the grey of a grouped page, so it never runs into the page around it.
            val whiteGround = ArecaSurface(
                ground = white,
                raised = c.neutral15,
                well = c.neutral15,
                onWell = white,
                separator = c.neutral20,
                field = white,
                band = c.neutral30,
            )
            // A grey surface: a card of a plain layout, the page of a grouped one.
            val greyGround = ArecaSurface(
                ground = c.neutral15,
                raised = white,
                well = c.neutral30,
                onWell = white,
                separator = c.neutral30,
                field = white,
            )
            // Plain: white page, grey cards, white insets. Also every sheet.
            val plain = SurfaceLadder(page = whiteGround, card = greyGround, inset = whiteGround)
            val screen = when (layout) {
                SurfaceLayout.Plain -> plain
                // Grouped: grey page, white cards, and inside a white card a tile a little off
                // white — the page's grey would read as a hole through the card.
                SurfaceLayout.Grouped -> SurfaceLadder(
                    page = greyGround,
                    card = whiteGround.copy(raised = c.neutral10),
                    inset = ArecaSurface(
                        ground = c.neutral10,
                        raised = white,
                        well = c.neutral30,
                        onWell = white,
                        separator = c.neutral30,
                        field = white,
                    ),
                )
            }
            return ArecaSurfaces(layout = layout, screen = screen, sheet = plain, bar = white)
        }

        private fun dark(c: ArecaBaseColors, layout: SurfaceLayout): ArecaSurfaces {
            // The ramp has no steps between neutral90 and neutral80, where a dark interface needs
            // three: a sheet, an inset tile and the well a control sits in.
            val sheetGround = lerp(c.neutral90, c.neutral80, 0.4f)
            val insetGround = lerp(c.neutral90, c.neutral80, 0.75f)
            val page = ArecaSurface(
                ground = c.neutral100,
                raised = c.neutral90,
                well = c.neutral90,
                onWell = c.neutral80,
                separator = c.neutral90,
                field = c.neutral90,
            )
            val card = ArecaSurface(
                ground = c.neutral90,
                raised = insetGround,
                well = c.neutral80,
                onWell = c.neutral70,
                separator = c.neutral80,
                field = insetGround,
            )
            // A hairline on the inset ground: neutral80 sits too close to it to read.
            val insetLine = lerp(c.neutral80, c.neutral70, 0.5f)
            val inset = ArecaSurface(
                ground = insetGround,
                raised = c.neutral80,
                // neutral80 sits too close to the inset ground for a field or a track to show.
                well = lerp(c.neutral80, c.neutral70, 0.5f),
                onWell = c.neutral70,
                separator = insetLine,
                field = c.neutral80,
            )
            val sheet = SurfaceLadder(
                page = ArecaSurface(
                    ground = sheetGround,
                    raised = insetGround,
                    well = c.neutral80,
                    onWell = c.neutral70,
                    separator = c.neutral80,
                    field = insetGround,
                ),
                card = inset,
                inset = inset.copy(
                    ground = c.neutral80,
                    raised = insetGround,
                    well = lerp(c.neutral80, c.neutral70, 0.5f),
                    onWell = c.neutral60,
                    separator = c.neutral70,
                    field = insetGround,
                ),
            )
            return ArecaSurfaces(
                layout = layout,
                screen = SurfaceLadder(page = page, card = card, inset = inset),
                sheet = sheet,
                bar = c.neutral90,
            )
        }
    }
}

/**
 * Where the content in scope sits: on which root's ladder, and how deep.
 *
 * @property root The ladder.
 * @property level The step on it.
 */
@Immutable
internal data class SurfacePosition(val root: SurfaceRoot, val level: SurfaceLevel) {
    /** @return The position of a surface laid on this one. */
    fun raised(): SurfacePosition = copy(
        level = when (level) {
            SurfaceLevel.Page -> SurfaceLevel.Card
            SurfaceLevel.Card -> SurfaceLevel.Inset
            SurfaceLevel.Inset -> SurfaceLevel.Card
        },
    )
}

internal val LocalSurfaces = staticCompositionLocalOf {
    ArecaSurfaces.from(ArecaBaseColors(), SurfaceLayout.Grouped, darkTheme = false)
}

internal val LocalSurfacePosition = staticCompositionLocalOf {
    SurfacePosition(SurfaceRoot.Screen, SurfaceLevel.Page)
}

/**
 * Puts [content] one step up the surface ladder: on the surface a card laid on the current one
 * takes. Every Areca component with a fill of its own — a card, a section, a notice — does this for
 * what it holds; call it around content drawn on a fill of your own with
 * `ArecaTheme.surface.raised`.
 *
 * @param content Content drawn on the raised surface.
 */
@Composable
public fun RaisedSurface(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalSurfacePosition provides LocalSurfacePosition.current.raised(),
        content = content,
    )
}

/**
 * Puts [content] at a chosen step of the current root's ladder — a white panel laid across a grey
 * page is [SurfaceLevel.Card] — or of another root's.
 *
 * @param level The step.
 * @param root The ladder; the current one when omitted.
 * @param content Content drawn on that surface.
 */
@Composable
public fun ProvideSurface(
    level: SurfaceLevel,
    root: SurfaceRoot = LocalSurfacePosition.current.root,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalSurfacePosition provides SurfacePosition(root, level),
        content = content,
    )
}

/**
 * Puts [content] on the surface that a fill of your own stands for — a docked bar, a panel — so the
 * fills inside it (a soft button, a key, a track) take that surface's roles and stay visible on it.
 * A fill that is the ground of a step of the current ladder puts the content on that step; any
 * other opaque fill, one step up; a transparent one leaves the surface as it is.
 *
 * ```kotlin
 * Box(Modifier.background(ArecaTheme.surfaces.bar)) {
 *     ProvideSurface(fill = ArecaTheme.surfaces.bar) { BarContent() }
 * }
 * ```
 *
 * @param fill The fill drawn behind [content].
 * @param content Content drawn on it.
 */
@Composable
public fun ProvideSurface(fill: Color, content: @Composable () -> Unit) {
    SurfaceOf(fill = fill, content = content)
}

/** @return The surface in scope. */
@Composable
@ReadOnlyComposable
internal fun currentSurface(): ArecaSurface {
    val position = LocalSurfacePosition.current
    return LocalSurfaces.current.ladder(position.root).at(position.level)
}

/**
 * Where content drawn on a fill of [fill] sits: the step of the ladder whose ground it is, or one
 * step up for any other opaque fill. A transparent fill on the current ladder stays where it is.
 *
 * @param fill The fill.
 * @param root A ladder to start, or `null` for the current one.
 * @return The position.
 */
@Composable
@ReadOnlyComposable
internal fun surfacePositionFor(fill: Color, root: SurfaceRoot? = null): SurfacePosition {
    val current = LocalSurfacePosition.current
    if (root == null && fill.alpha == 0f) return current
    val start = if (root == null) current else SurfacePosition(root, SurfaceLevel.Page)
    val ladder = LocalSurfaces.current.ladder(start.root)
    val level = when {
        root != null -> SurfaceLevel.entries.firstOrNull { ladder.at(it).ground == fill } ?: SurfaceLevel.Page
        fill == ladder.at(start.level).ground -> start.level
        fill == ladder.at(start.raised().level).ground -> start.raised().level
        else -> SurfaceLevel.entries.firstOrNull { ladder.at(it).ground == fill } ?: start.raised().level
    }
    return start.copy(level = level)
}

/**
 * The roles of the surface a fill of [fill] stands for; see [surfacePositionFor].
 *
 * @param fill The fill.
 * @return Its surface.
 */
@Composable
@ReadOnlyComposable
internal fun surfaceFor(fill: Color): ArecaSurface {
    val position = surfacePositionFor(fill)
    return LocalSurfaces.current.ladder(position.root).at(position.level)
}

/**
 * Puts [content] on the surface a fill of [fill] stands for; see [surfacePositionFor]. A transparent
 * fill leaves the surface as it is — what shows through is still the surface below.
 *
 * @param fill The fill drawn behind [content].
 * @param root A ladder to start: the fill is then that root's page unless it is the ground of a
 *   deeper step — a grey sheet is a sheet's card. `null` stays on the current ladder.
 * @param content Content drawn on it.
 */
@Composable
internal fun SurfaceOf(fill: Color, root: SurfaceRoot? = null, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalSurfacePosition provides surfacePositionFor(fill, root),
        content = content,
    )
}

/**
 * Whether the content in scope lies straight on the ground of a sheet, a dialog or a menu — not on
 * a card inside one. A sheet is already a segment of its own, so what groups content on a screen —
 * a settings section's card — draws flat there.
 */
@Composable
internal fun onSheetGround(): Boolean {
    val position = LocalSurfacePosition.current
    return position.root == SurfaceRoot.Sheet && position.level == SurfaceLevel.Page
}

