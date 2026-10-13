package dev.voir.areca.foundation.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.list.ListItemSize
import dev.voir.areca.foundation.list.ListItemStyle

/**
 * Entry point for the styles and sizes the enclosing [Settings] list provides.
 *
 * **A settings list is neutral by default.** Headers, values, badges, spinners and selection are
 * drawn in the neutral ramp, so the colors on a settings screen are the ones its content brings —
 * a switch that is on, an icon tile, a destructive row. Where a screen needs an accent, it takes the
 * brand: a [SettingsRowTone.Brand] row, or the brand passed explicitly to [SettingsDefaults.styles]
 * and [SettingsDefaults.sectionStyle] for headers and pressable values.
 */
public object SettingsTheme {
    /**
     * Colors used by the enclosing [Settings] list, or the theme defaults outside of one.
     *
     * The fallback is what lets a settings row stand on its own — inside a sheet, a dialog or a
     * card of the caller's — without the list around it.
     */
    public val styles: SettingsStyles
        @Composable
        get() = LocalSettingsStyles.current ?: SettingsDefaults.styles()

    /** Metrics used by the enclosing [Settings] list, or the theme defaults outside of one. */
    public val sizes: SettingsSizes
        @Composable
        get() = LocalSettingsSizes.current ?: SettingsDefaults.sizes()
}

/** Where a [Section] draws its header relative to its card. */
public enum class SettingsHeaderPlacement {
    /**
     * On the card, above the first row.
     *
     * The header reads as the first line of the group rather than as a caption floating over it,
     * which is what keeps a screen of many short sections from turning into a ladder of labels.
     */
    Inside,

    /** Above the card, in a quiet color, for a screen that wants its groups named from outside. */
    Outside,
}

/**
 * What a settings row does, which decides the color of its title and leading glyph.
 *
 * Tone is a property of the row's *meaning*, not of its value: a row that changes a setting stays
 * [Default] however important the setting is.
 */
public enum class SettingsRowTone {
    /** Opens, states or changes something. The tone of almost every row. */
    Default,

    /**
     * Performs an action rather than opening a setting — "Change wallpaper", "Add device". The title
     * and glyph take the brand color so the row reads as a button inside the list. The one place a
     * settings list reaches for the brand on its own.
     */
    Brand,

    /** Destroys, signs out or revokes. The title, glyph and press all take the danger color. */
    Danger,
}

/**
 * Colors of a settings section: its card, the copy around it and the separators between its rows.
 *
 * @property bg Card background.
 * @property bgDisabled Card background while the section is disabled.
 * @property border Card border color; [Color.Unspecified] draws no border.
 * @property borderDisabled Card border color while the section is disabled.
 * @property header Header color when it sits inside the card.
 * @property headerOutside Header color when it sits above the card.
 * @property headerDisabled Header color while the section is disabled, in either placement.
 * @property footer Footer color.
 * @property footerDisabled Footer color while the section is disabled.
 * @property separator Color of the hairlines between rows.
 * @property shape Shape of the card; rows clip their states to it so they keep its corners.
 */
@Immutable
public data class SettingsSectionStyle(
    public val bg: Color,
    public val bgDisabled: Color = bg,
    public val border: Color = Color.Unspecified,
    public val borderDisabled: Color = border,
    public val header: Color,
    public val headerOutside: Color,
    public val headerDisabled: Color,
    public val footer: Color,
    public val footerDisabled: Color,
    public val separator: Color,
    public val shape: Shape,
)

/**
 * All colors used by a [Settings] list.
 *
 * Rows are [dev.voir.areca.foundation.list.ListItem]s, so their colors are ordinary
 * [ListItemStyle]s — one per [SettingsRowTone].
 *
 * @property row Colors of a [SettingsRowTone.Default] row.
 * @property brandRow Colors of a [SettingsRowTone.Brand] row.
 * @property dangerRow Colors of a [SettingsRowTone.Danger] row.
 * @property section Colors of the section cards and the copy around them.
 * @property interactiveValue Color of the inline value on a row that can be pressed, so a value
 * that opens a picker reads as changeable. [Color.Unspecified] keeps the row's own value color,
 * which brightens while the row is reached for.
 * @property error Color of the message a row shows for [SettingsRowStatus.Error].
 * @property spinner Color of the spinner a row shows for [SettingsRowStatus.Loading].
 * @property placeholder Color of the placeholder in a [SettingsTextFieldRow].
 * @property cursor Color of the caret in a [SettingsTextFieldRow].
 * @property badge Colors given to a badge in a row's `badge` slot, unless it names its own.
 */
@Immutable
public data class SettingsStyles(
    public val row: ListItemStyle,
    public val brandRow: ListItemStyle,
    public val dangerRow: ListItemStyle,
    public val section: SettingsSectionStyle,
    public val interactiveValue: Color,
    public val error: Color,
    public val spinner: Color,
    public val placeholder: Color,
    public val cursor: Color,
    public val badge: BadgeStyle,
) {
    /**
     * Row colors for a tone.
     *
     * A value on a row that can be pressed is an offer to change it, and reads as one; a value on a
     * row that only states it stays quiet.
     *
     * @param tone Tone of the row.
     * @param interactive Whether the row can be pressed, which gives its inline value
     * [interactiveValue] when that is specified.
     * @return The style this list gives rows of [tone].
     */
    public fun rowStyle(tone: SettingsRowTone, interactive: Boolean = false): ListItemStyle {
        val style = when (tone) {
            SettingsRowTone.Default -> row
            SettingsRowTone.Brand -> brandRow
            SettingsRowTone.Danger -> dangerRow
        }
        if (!interactive || !interactiveValue.isSpecified) return style

        return style.copy(
            value = interactiveValue,
            valueActive = interactiveValue,
            valueSelected = interactiveValue,
        )
    }
}

/**
 * Metrics and typography of a settings section.
 *
 * Vertical rhythm between sections comes from [sectionGap] alone, applied by [Settings]; a section
 * carries horizontal insets only, so sections cannot drift apart however they are configured.
 *
 * @property outerPadding Padding around the section, header and footer included.
 * @property sectionGap Spacing between sections of the same list.
 * @property headerInsidePadding Padding around a header that sits inside the card.
 * @property headerOutsidePadding Padding around a header that sits above the card.
 * @property footerPadding Padding around the footer below the card.
 * @property borderWidth Width of the card border.
 * @property separatorThickness Thickness of the hairlines between rows.
 * @property separatorInset Start inset of a hairline next to a row that does not report where its
 * text starts, such as a [SettingsBlock]. Rows that do report it are separated at their text.
 * @property headerTextStyle Text style of the header.
 * @property footerTextStyle Text style of the footer.
 */
@Immutable
public data class SettingsSectionSize(
    public val outerPadding: PaddingValues,
    public val sectionGap: Dp,
    public val headerInsidePadding: PaddingValues,
    public val headerOutsidePadding: PaddingValues,
    public val footerPadding: PaddingValues,
    public val borderWidth: Dp,
    public val separatorThickness: Dp,
    public val separatorInset: Dp,
    public val headerTextStyle: TextStyle,
    public val footerTextStyle: TextStyle,
)

/**
 * All metrics used by a [Settings] list.
 *
 * @property row Metrics and typography of every row.
 * @property section Metrics and typography of the sections.
 * @property iconTile Size of the colored tile drawn by [SettingsRowIcon].
 * @property splitDividerHeight Height of the rule that splits a [SettingsToggleRow] with its own
 * click handler into a pressable row and a switch.
 * @property spinner Size of the spinner a row shows for [SettingsRowStatus.Loading].
 * @property statusTextStyle Text style of the message a row shows for [SettingsRowStatus.Error],
 * and of the counter in a [SettingsTextFieldRow].
 * @property badge Size given to a badge in a row's `badge` slot, unless it names its own. Small
 * enough to sit on the title's line without making the row taller.
 */
@Immutable
public data class SettingsSizes(
    public val row: ListItemSize,
    public val section: SettingsSectionSize,
    public val iconTile: FeaturedIconSize,
    public val splitDividerHeight: Dp,
    public val spinner: SpinnerSize,
    public val statusTextStyle: TextStyle,
    public val badge: BadgeSize,
)

/** Colors of the enclosing [Settings] list; `null` outside of one. */
internal val LocalSettingsStyles = compositionLocalOf<SettingsStyles?> { null }

/** Metrics of the enclosing [Settings] list; `null` outside of one. */
internal val LocalSettingsSizes = compositionLocalOf<SettingsSizes?> { null }

/** Header placement the enclosing [Settings] list gives its sections. */
internal val LocalSettingsHeaderPlacement = compositionLocalOf { SettingsHeaderPlacement.Inside }
