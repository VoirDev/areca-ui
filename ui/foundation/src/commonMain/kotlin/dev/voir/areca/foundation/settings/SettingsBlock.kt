package dev.voir.areca.foundation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle

/**
 * Free-form content on a section card, inset like a row.
 *
 * For what a row cannot say: a preview of the setting being changed, a picker of theme swatches, a
 * promotional panel with its own button. The block takes the row's horizontal and vertical padding
 * and its title color and style, so it lines up with the rows around it, and the section separates
 * it from them like any other row.
 *
 * Pass [PaddingValues] of zero for content that runs to the card's edges, such as an image.
 *
 * @param modifier Modifier applied to the block.
 * @param contentPadding Inset around the content; defaults to a row's.
 * @param content Content of the block.
 */
@Composable
public fun SettingsBlock(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = SettingsTheme.sizes.row.contentPadding,
    content: @Composable ColumnScope.() -> Unit,
) {
    val row = SettingsTheme.styles.row
    val size = SettingsTheme.sizes.row

    CompositionLocalProvider(
        LocalContentColor provides row.title,
        LocalTextStyle provides size.titleTextStyle,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(contentPadding),
            content = content,
        )
    }
}
