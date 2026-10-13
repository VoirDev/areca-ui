package dev.voir.areca.keyboard.key

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.keyboard.layout.KeyboardSize
import dev.voir.areca.keyboard.layout.KeyboardStyle
import dev.voir.areca.keyboard.layout.LocalKeyboardSize
import dev.voir.areca.keyboard.layout.LocalKeyboardStyle

/**
 * Draws a [KeyboardGrid] as rows of weighted keys.
 *
 * Each row divides the full width among its keys by [Key.weight], independently of every other row,
 * which is what lets a grid hold a row of five operators above rows of four digits without a column
 * count to reconcile them. Each key is composed under its [Key.id], so a key keeps its pressed and
 * focused state when the keys around it change.
 *
 * Rows run left to right in every layout direction: a keypad keeps its physical order in a
 * right-to-left locale, as number pads conventionally do. What each key draws still gets the
 * caller's layout direction.
 *
 * Normally reached through [dev.voir.areca.keyboard.layout.KeyboardLayout]'s grid overload, which
 * wraps this in the keyboard surface. Call it directly to put a grid somewhere else — inside a
 * sheet, a panel, or a screen of its own.
 *
 * @param grid Keys to draw.
 * @param modifier Modifier applied to the column of rows.
 * @param style Look of each kind of key. Defaults to the keys of [LocalKeyboardStyle], or of
 *   [KeyboardStyle.Default].
 * @param size Row and key spacing, the key height, and the metrics of a single key. Defaults to
 *   [LocalKeyboardSize], or [KeyboardSize.Default].
 */
@Composable
public fun KeyboardKeys(
    grid: KeyboardGrid,
    modifier: Modifier = Modifier,
    style: KeyStyle = (LocalKeyboardStyle.current ?: KeyboardStyle.Default).keys,
    size: KeyboardSize = LocalKeyboardSize.current ?: KeyboardSize.Default,
) {
    val contentDirection = LocalLayoutDirection.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(size.rowGap),
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            grid.rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(size.keyGap),
                ) {
                    row.keys.forEach { item ->
                        key(item.id) {
                            KeyItem(
                                key = item,
                                height = row.height ?: size.keyHeight,
                                size = size.key,
                                labelMaxLines = size.keyLabelMaxLines,
                                style = item.style ?: style.of(item.role),
                                contentDirection = contentDirection,
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Draws one key, sized to its share of the row.
 *
 * @param key Key to draw.
 * @param height Height of the key's row.
 * @param size Metrics of the key.
 * @param labelMaxLines Most lines a label takes before it is cut short with an ellipsis.
 * @param style Resolved look of the key.
 * @param contentDirection Layout direction of the caller, restored for what the key draws.
 */
@Composable
private fun RowScope.KeyItem(
    key: Key,
    height: Dp,
    size: ButtonSize,
    labelMaxLines: Int,
    style: ButtonStyle,
    contentDirection: LayoutDirection,
) {
    val modifier = Modifier
        .weight(key.weight)
        .height(height)

    if (key.content is KeyContent.Blank) {
        // Not a disabled button: a blank is a hole in the grid, and announcing it or greying it out
        // would tell the user there is something here to press.
        Spacer(modifier)
        return
    }

    val description = key.contentDescription
    // The description replaces whatever the content would have announced — an icon key has nothing
    // to say for itself, and a label key given a description meant to override its label — so the
    // content is silenced and the description set on the button's own node. Clearing that node
    // instead would also erase the role, click action, and disabled state the button puts there.

    Button(
        onClick = key.onClick,
        modifier = if (description != null) {
            modifier.semantics { contentDescription = description }
        } else {
            modifier
        },
        style = style,
        size = size,
        enabled = key.enabled,
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides contentDirection) {
            when (val content = key.content) {
                is KeyContent.Label -> Text(
                    text = content.text,
                    modifier = if (description != null) Modifier.clearAndSetSemantics { } else Modifier,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = labelMaxLines,
                )

                is KeyContent.Icon -> Icon(imageVector = content.image, contentDescription = null)

                is KeyContent.Custom -> if (description != null) {
                    Box(Modifier.clearAndSetSemantics { }) { content.content() }
                } else {
                    content.content()
                }

                KeyContent.Blank -> Unit
            }
        }
    }
}
