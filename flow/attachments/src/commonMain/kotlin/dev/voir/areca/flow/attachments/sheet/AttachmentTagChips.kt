package dev.voir.areca.flow.attachments.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.voir.areca.foundation.badge.BadgeOption
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.FlowBadgeSelect
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * What a file is, chosen in one tap: every tag the product has, one chip each, wrapping onto as many
 * lines as it needs.
 *
 * All of them on screen at once rather than behind a picker, because choosing is then one tap and a
 * handful of short words fit in two lines. The chosen chip is the dark one; the others sit on a
 * grey so they read as chips on a white sheet.
 *
 * The tags are the product's own type — an enum of receipt, invoice and warranty, a list of folders,
 * anything — and [label] words each one, so the module neither knows nor translates them. Whether
 * one must be chosen before a file is attached is the caller's to decide.
 *
 * @param T Type of the tags.
 * @param tags Every tag, in the order shown.
 * @param selected The chosen tag, or `null` while none has been chosen.
 * @param onSelect Called with the tag pressed.
 * @param label Words a tag.
 * @param modifier Modifier applied to the group.
 * @param bg Fill of a chip that is not chosen. The subtle grey reads on a white sheet.
 * @param enabled Whether a chip can be pressed.
 */
@Composable
public fun <T> AttachmentTagChips(
    tags: List<T>,
    selected: T?,
    onSelect: (T) -> Unit,
    label: (T) -> String,
    modifier: Modifier = Modifier,
    bg: Color = ArecaTheme.colors.bgSubtle,
    enabled: Boolean = true,
) {
    FlowBadgeSelect(
        options = tags.map { tag ->
            BadgeOption(value = tag, label = { Text(label(tag)) }, enabled = enabled)
        },
        onSelect = { option -> onSelect(option.value) },
        selected = listOfNotNull(selected),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        style = BadgeStyle.Chip.copy(bgSoft = bg),
        size = BadgeSize.Chip,
    )
}
