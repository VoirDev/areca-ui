package dev.voir.areca.foundation.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.list.ListItemColor
import dev.voir.areca.foundation.list.ListItemSize
import dev.voir.areca.foundation.list.ListItemStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * Theme-backed defaults for [Settings].
 *
 * Every default is neutral except the [SettingsRowTone.Brand] row. To give a screen brand accents,
 * pass the brand explicitly where it is wanted:
 *
 * ```kotlin
 * Settings(
 *     styles = SettingsDefaults.styles(
 *         section = SettingsDefaults.sectionStyle(header = ArecaTheme.colors.fgBrandText),
 *         interactiveValue = ArecaTheme.colors.fgBrandText,
 *     ),
 * ) { ... }
 * ```
 *
 * A settings list is a grouped list: rows on cards, the cards on the screen canvas. Rows take the
 * [ListItemStyle.plain] appearance, so the card owns the background and the corners and a row
 * only ever adds its hover, press and selection layers over it.
 */
public object SettingsDefaults {

    /**
     * Colors for a [Settings] list.
     *
     * @param row Colors of a [SettingsRowTone.Default] row.
     * @param brandRow Colors of a [SettingsRowTone.Brand] row.
     * @param dangerRow Colors of a [SettingsRowTone.Danger] row.
     * @param section Colors of the section cards and the copy around them.
     * @param interactiveValue Color of the inline value on a row that can be pressed;
     * [Color.Unspecified] keeps the neutral value color.
     * @param error Color of a row's error message.
     * @param spinner Color of a row's loading spinner.
     * @param placeholder Color of the placeholder in a [SettingsTextFieldRow].
     * @param cursor Color of the caret in a [SettingsTextFieldRow].
     * @param badge Colors of a badge in a row's `badge` slot.
     * @return Colors of the list.
     */
    @Composable
    public fun styles(
        row: ListItemStyle = rowStyle(SettingsRowTone.Default),
        brandRow: ListItemStyle = rowStyle(SettingsRowTone.Brand),
        dangerRow: ListItemStyle = rowStyle(SettingsRowTone.Danger),
        section: SettingsSectionStyle = sectionStyle(),
        interactiveValue: Color = Color.Unspecified,
        error: Color = ArecaTheme.colors.fgDangerText,
        spinner: Color = ArecaTheme.colors.fgMuted,
        placeholder: Color = ArecaTheme.colors.fgMuted,
        cursor: Color = ArecaTheme.colors.fgBase,
        badge: BadgeStyle = BadgeStyle.Neutral,
    ): SettingsStyles = SettingsStyles(
        row = row,
        brandRow = brandRow,
        dangerRow = dangerRow,
        section = section,
        interactiveValue = interactiveValue,
        error = error,
        spinner = spinner,
        placeholder = placeholder,
        cursor = cursor,
        badge = badge,
    )

    /**
     * Row colors for a tone.
     *
     * A selected row is marked by a neutral fill alone. The list item's default rail is drawn in the
     * brand, and a settings list does not reach for the brand unasked.
     *
     * @param tone Tone of the row.
     * @return A transparent row that tints on hover and press in the tone's color.
     */
    @Composable
    public fun rowStyle(tone: SettingsRowTone = SettingsRowTone.Default): ListItemStyle =
        when (tone) {
            SettingsRowTone.Default -> ListItemStyle.plain(ListItemColor.Neutral)
                .copy(selectedRail = Color.Unspecified)

            SettingsRowTone.Brand -> ListItemStyle.plain(ListItemColor.Brand)
            SettingsRowTone.Danger -> ListItemStyle.plain(ListItemColor.Danger)
        }

    /**
     * Colors of a section.
     *
     * The card is the grouped-region fill, `bgSubtle`: place the list on `bgCanvas` and the cards
     * separate from it in both themes.
     *
     * The hairlines between rows take `borderOnSubtle`, one step past the card's fill in each theme;
     * `borderBase` sits level with the light card and would vanish on it.
     *
     * @param bg Card background.
     * @param border Card border color; [Color.Unspecified] draws no border.
     * @param header Header color inside the card.
     * @param headerOutside Header color above the card.
     * @param headerDisabled Header color while the section is disabled.
     * @param footer Footer color.
     * @param footerDisabled Footer color while the section is disabled.
     * @param separator Color of the hairlines between rows.
     * @param shape Shape of the card.
     * @param band Fill of a section's band: a step past the card, so the band reads as the card's
     * closing strip. Pass the danger tint for a band that holds a danger row.
     * @return Colors of a section.
     */
    @Composable
    public fun sectionStyle(
        bg: Color = ArecaTheme.colors.bgSubtle,
        border: Color = Color.Unspecified,
        header: Color = ArecaTheme.colors.fgSubtle,
        headerOutside: Color = ArecaTheme.colors.fgSubtle,
        headerDisabled: Color = ArecaTheme.colors.fgDisabled,
        footer: Color = ArecaTheme.colors.fgMuted,
        footerDisabled: Color = ArecaTheme.colors.fgDisabled,
        separator: Color = ArecaTheme.colors.borderOnSubtle,
        shape: Shape = ArecaTheme.shapes.lg,
        band: Color = ArecaTheme.colors.bgMuted,
    ): SettingsSectionStyle = SettingsSectionStyle(
        bg = bg,
        border = border,
        header = header,
        headerOutside = headerOutside,
        headerDisabled = headerDisabled,
        footer = footer,
        footerDisabled = footerDisabled,
        separator = separator,
        shape = shape,
        band = band,
    )

    /**
     * Metrics for a [Settings] list.
     *
     * @param row Metrics and typography of every row.
     * @param section Metrics and typography of the sections.
     * @param iconTile Size of the tile drawn by [SettingsRowIcon].
     * @param splitDividerHeight Height of the rule in a split [SettingsToggleRow].
     * @param spinner Size of a row's loading spinner.
     * @param statusTextStyle Text style of a row's error message and a text field's counter.
     * @param badge Size of a badge in a row's `badge` slot.
     * @return Metrics of the list.
     */
    @Composable
    public fun sizes(
        row: ListItemSize = rowSize(),
        section: SettingsSectionSize = sectionSize(),
        iconTile: FeaturedIconSize = FeaturedIconSize(container = 28.dp, icon = 16.dp, borderWidth = 1.dp),
        splitDividerHeight: Dp = 24.dp,
        spinner: SpinnerSize = SpinnerSize(container = 18.dp),
        statusTextStyle: TextStyle = ArecaTheme.typography.bodySmall,
        badge: BadgeSize = BadgeSize.ExtraSmall,
    ): SettingsSizes = SettingsSizes(
        row = row,
        section = section,
        iconTile = iconTile,
        splitDividerHeight = splitDividerHeight,
        spinner = spinner,
        statusTextStyle = statusTextStyle,
        badge = badge,
    )

    /**
     * Metrics of a settings row.
     *
     * The medium list density with four changes. The row is padded 16dp across and 12dp down —
     * roughly 4:3 rather than the medium density's 2:1, so a row with a subtitle breathes instead of
     * reading as a squashed line. A single-line text row still comes to 48dp, one touch target and
     * no taller, so a long screen of switches does not become a long scroll. The value is set at
     * title size, because in a settings row the value is the answer to the title rather than a
     * footnote to it. And the leading slot stands a full step away from the text, so a column of
     * icon tiles reads as its own column.
     *
     * @param titleTextStyle Text style of the title.
     * @param subtitleTextStyle Text style of the subtitle.
     * @param valueTextStyle Text style of the inline value.
     * @param captionTextStyle Text style of the caption under the subtitle.
     * @return Metrics of a settings row.
     */
    @Composable
    public fun rowSize(
        titleTextStyle: TextStyle = ArecaTheme.typography.bodyMedium,
        subtitleTextStyle: TextStyle = ArecaTheme.typography.bodySmall,
        valueTextStyle: TextStyle = ArecaTheme.typography.bodyMedium,
        captionTextStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
    ): ListItemSize = ListItemSize.Medium.copy(
        titleTextStyle = titleTextStyle,
        subtitleTextStyle = subtitleTextStyle,
        valueTextStyle = valueTextStyle,
        captionTextStyle = captionTextStyle,
        contentPadding = PaddingValues(
            horizontal = ArecaTheme.spacing.xl,
            vertical = ArecaTheme.spacing.lg,
        ),
        minHeight = 24.dp,
        leadingGap = ArecaTheme.spacing.xl,
        icon = 22.dp,
    )

    /**
     * Metrics of a section.
     *
     * @param outerPadding Padding around the section, header and footer included.
     * @param sectionGap Spacing between sections.
     * @param headerInsidePadding Padding around a header inside the card.
     * @param headerOutsidePadding Padding around a header above the card.
     * @param footerPadding Padding around the footer.
     * @param borderWidth Width of the card border.
     * @param separatorThickness Thickness of the hairlines between rows.
     * @param separatorInset Start inset of a hairline next to a row that reports no text start.
     * @param headerTextStyle Text style of the header.
     * @param footerTextStyle Text style of the footer.
     * @return Metrics of a section.
     */
    @Composable
    public fun sectionSize(
        outerPadding: PaddingValues = PaddingValues(
            horizontal = ArecaTheme.spacing.horizontalContentInsets,
        ),
        sectionGap: Dp = ArecaTheme.spacing.lg,
        headerInsidePadding: PaddingValues = PaddingValues(
            start = ArecaTheme.spacing.xl,
            end = ArecaTheme.spacing.xl,
            top = ArecaTheme.spacing.lg,
            bottom = ArecaTheme.spacing.xs,
        ),
        headerOutsidePadding: PaddingValues = PaddingValues(
            start = ArecaTheme.spacing.xl,
            end = ArecaTheme.spacing.xl,
            bottom = ArecaTheme.spacing.sm,
        ),
        footerPadding: PaddingValues = PaddingValues(
            start = ArecaTheme.spacing.xl,
            end = ArecaTheme.spacing.xl,
            top = ArecaTheme.spacing.sm,
            bottom = ArecaTheme.spacing.xs,
        ),
        borderWidth: Dp = 1.dp,
        separatorThickness: Dp = 1.dp,
        separatorInset: Dp = ArecaTheme.spacing.xl,
        headerTextStyle: TextStyle = ArecaTheme.typography.labelMedium,
        footerTextStyle: TextStyle = ArecaTheme.typography.bodySmall,
    ): SettingsSectionSize = SettingsSectionSize(
        outerPadding = outerPadding,
        sectionGap = sectionGap,
        headerInsidePadding = headerInsidePadding,
        headerOutsidePadding = headerOutsidePadding,
        footerPadding = footerPadding,
        borderWidth = borderWidth,
        separatorThickness = separatorThickness,
        separatorInset = separatorInset,
        headerTextStyle = headerTextStyle,
        footerTextStyle = footerTextStyle,
    )
}
