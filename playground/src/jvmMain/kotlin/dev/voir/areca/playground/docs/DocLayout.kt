package dev.voir.areca.playground.docs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

/*
 * The few layout pieces the documentation images share. They arrange published components; they
 * draw nothing of their own beyond a caption.
 */

/**
 * Columns side by side, each taking an equal share of the width.
 *
 * @param columns One slot per column.
 */
@Composable
internal fun DocColumns(vararg columns: @Composable ColumnScope.() -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        columns.forEach { column ->
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3),
                content = column,
            )
        }
    }
}

/**
 * A block of content inside the screen gutter, under an optional caption naming what it shows.
 *
 * Settings sections bring their own gutter and sit outside a group.
 *
 * @param caption What the block shows, or `null` for none.
 * @param gap Space between the caption and the items of the block.
 * @param content The block.
 */
@Composable
internal fun DocGroup(
    caption: String? = null,
    gap: Dp = ArecaTheme.spacing.lg,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().horizontalContentInsets(),
        verticalArrangement = Arrangement.spacedBy(gap),
    ) {
        if (caption != null) DocCaption(caption)
        content()
    }
}

/**
 * Items in a row with the theme's gap, wrapped onto no second line.
 *
 * @param content The items.
 */
@Composable
internal fun DocRow(content: @Composable RowScope.() -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/**
 * The caption over a block.
 *
 * @param text What the block shows.
 */
@Composable
internal fun DocCaption(text: String) {
    Text(text, variant = TextVariant.LabelMedium, color = TextColor.Muted)
}
