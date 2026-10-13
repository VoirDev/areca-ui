package dev.voir.areca.foundation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.list.ListGroupClip
import dev.voir.areca.foundation.list.LocalListEnabled
import dev.voir.areca.foundation.list.LocalListGroupClip
import dev.voir.areca.foundation.list.PlacedCoordinates
import dev.voir.areca.foundation.list.recordPlacement
import dev.voir.areca.modifier.thenIf

/** Receiver of the content of a [Section], where its rows are declared. */
@SettingsDsl
@Immutable
public class SettingsSectionScope internal constructor() {
    /** Holder of the shared receiver. */
    internal companion object {
        /** The one receiver every section hands out; the scope carries no state of its own. */
        val Instance = SettingsSectionScope()
    }
}

/**
 * A group of settings rows on one card, with an optional header and footer.
 *
 * **Header.** Names the group and carries the heading role, so a screen reader can jump between
 * groups. By default it sits inside the card above the first row, in a quiet neutral; pass
 * [SettingsHeaderPlacement.Outside] — here or to the whole [Settings] list — to hang it above the
 * card instead.
 *
 * **Footer.** Sits below the card and says what the group's settings mean, or what changing them
 * costs. It is ordinary content, so an inline link to a related setting fits in it.
 *
 * **Separators.** Drawn between adjacent rows automatically, starting at the rows' text and running
 * to the card's end edge — there is no call-site bookkeeping, and a section can never end up with a
 * stray hairline at its top or bottom. Turn them off with [separated] for a group of rows that each
 * carry a leading tile, where the tiles already separate the rows.
 *
 * **Enabled.** Disabling the section disables every row inside it and shades the header and
 * footer, which is how to present a group the current user cannot act on.
 *
 * The card does not clip its rows, so a row's focus ring is drawn whole at the card's edge; each row
 * clips its own background and press state to the card's shape instead, so the first and last row
 * still follow its corners.
 *
 * @param modifier Modifier applied to the section, header and footer included.
 * @param header Name of the group.
 * @param footer Copy below the card.
 * @param enabled Whether the rows of the section accept input and use their enabled colors.
 * @param separated Whether hairlines are drawn between adjacent rows.
 * @param headerPlacement Where [header] is drawn.
 * @param content Rows of the section, top to bottom.
 */
@Composable
public fun SettingsScope.Section(
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    separated: Boolean = true,
    headerPlacement: SettingsHeaderPlacement = LocalSettingsHeaderPlacement.current,
    content: @Composable SettingsSectionScope.() -> Unit,
) {
    val styles = SettingsTheme.styles.section
    val sizes = SettingsTheme.sizes.section
    val sectionEnabled = enabled && LocalListEnabled.current
    val cardPlacement = remember { PlacedCoordinates() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(sizes.outerPadding),
    ) {
        if (header != null && headerPlacement == SettingsHeaderPlacement.Outside) {
            SectionCopy(
                color = if (sectionEnabled) styles.headerOutside else styles.headerDisabled,
                style = sizes.headerTextStyle,
                modifier = Modifier
                    .padding(sizes.headerOutsidePadding)
                    // Merged so the heading flag lands on the node that carries the header's text.
                    .semantics(mergeDescendants = true) { heading() },
                content = header,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (sectionEnabled) styles.bg else styles.bgDisabled,
                    shape = styles.shape,
                )
                .thenIf(sizes.borderWidth > 0.dp && styles.border != Color.Unspecified) {
                    border(
                        width = sizes.borderWidth,
                        color = if (sectionEnabled) styles.border else styles.borderDisabled,
                        shape = styles.shape,
                    )
                }
                .recordPlacement(cardPlacement),
        ) {
            if (header != null && headerPlacement == SettingsHeaderPlacement.Inside) {
                SectionCopy(
                    color = if (sectionEnabled) styles.header else styles.headerDisabled,
                    style = sizes.headerTextStyle,
                    modifier = Modifier
                        .padding(sizes.headerInsidePadding)
                        .semantics(mergeDescendants = true) { heading() },
                    content = header,
                )
            }

            CompositionLocalProvider(
                LocalListEnabled provides sectionEnabled,
                LocalListGroupClip provides ListGroupClip(
                    shape = styles.shape,
                    placement = cardPlacement,
                ),
            ) {
                SettingsRowColumn(
                    modifier = Modifier.fillMaxWidth(),
                    separatorColor = if (separated) styles.separator else Color.Unspecified,
                    separatorThickness = sizes.separatorThickness,
                    fallbackInset = sizes.separatorInset,
                ) {
                    SettingsSectionScope.Instance.content()
                }
            }
        }

        if (footer != null) {
            SectionCopy(
                color = if (sectionEnabled) styles.footer else styles.footerDisabled,
                style = sizes.footerTextStyle,
                modifier = Modifier.padding(sizes.footerPadding),
                content = footer,
            )
        }
    }
}

/**
 * Header or footer copy of a section, with its color and text style provided to [content].
 *
 * @param color Content color of the copy.
 * @param style Text style of the copy.
 * @param modifier Modifier applied to the copy's container.
 * @param content The copy.
 */
@Composable
private fun SectionCopy(
    color: Color,
    style: TextStyle,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides color,
        LocalTextStyle provides style,
    ) {
        Box(modifier = modifier) { content() }
    }
}
