package dev.voir.areca.foundation.tabbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.text.Text

/**
 * Renders a centered icon with an optional single-line label for a floating tab item.
 *
 * Passing `null` for [label] produces an icon-only item. Custom item layouts can continue using
 * [TabBarItem.content] directly.
 *
 * @param modifier Modifier applied to the icon and label column.
 * @param label Optional text shown beneath the icon.
 * @param icon Icon content rendered above the optional label.
 */
@Composable
public fun FloatingTabBarItemContent(
    modifier: Modifier = Modifier,
    label: String? = null,
    icon: @Composable () -> Unit,
) {
    val size = LocalFloatingTabBarSize.current ?: FloatingTabBarSize.Default

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        icon()

        label?.let {
            Spacer(Modifier.height(size.itemContentGap))
            Text(
                text = it,
                maxLines = 1,
                softWrap = false,
            )
        }
    }
}
