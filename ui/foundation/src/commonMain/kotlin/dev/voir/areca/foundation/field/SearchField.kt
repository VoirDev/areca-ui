package dev.voir.areca.foundation.field

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.Cross
import dev.voir.areca.icon.Search

/**
 * [TextField] shaped for a query: a magnifier in front of it, and a clear control that appears
 * once there is something to clear. The clear control is withheld while the field is disabled or
 * read-only, since clearing is an edit and neither state allows one.
 *
 * Clearing puts focus back on the field rather than dismissing the keyboard, because somebody who
 * clears a query is almost always about to type a different one.
 *
 * The field always asks the IME for a search action, so [onSearch] fires on the software
 * keyboard's search key. Override [keyboardOptions] and that stops being true — keep
 * [ImeAction.Search] if you still want the callback.
 *
 * @param value Current query.
 * @param onValueChange Called with the new query on every edit, and with an empty string when
 *   the clear control is used.
 * @param modifier Modifier applied to the field container.
 * @param style Color palette and container shape. See [FieldStyle].
 * @param size Typography, paddings and vertical metrics.
 * @param field Scope from an enclosing [FieldGroup]: the status to report, and a label to
 *   draw inside the container when the group placed it there. Forward it and an internal
 *   label placement takes effect; leave it out and the group's label has nowhere to go.
 * @param status Semantic state — Default, Error, Success, Warning. Comes from [field]
 *   unless it is given explicitly.
 * @param enabled When false, non-interactive and rendered as disabled. Comes from
 *   [field] unless it is given explicitly.
 * @param readOnly Visible/selectable but not editable.
 * @param loading Shows a loading indicator in place of the clear control, for a query whose
 *   results are still on their way.
 * @param placeholder Shown when the query is empty.
 * @param onSearch Called when the IME's search action fires. Shorthand for building
 *   [keyboardActions] yourself; ignored when you pass your own.
 * @param keyboardActions IME action handlers. Defaults to routing the search action to
 *   [onSearch].
 * @param keyboardOptions Software keyboard configuration. Defaults to a text keyboard whose
 *   action key is [ImeAction.Search].
 * @param searchContentDescription Accessibility label for the leading magnifier.
 * @param clearContentDescription Accessibility label for the clear control.
 */
@Composable
public fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true,
    readOnly: Boolean = false,
    loading: Boolean = false,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    onSearch: (KeyboardActionScope.() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions(onSearch = onSearch),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Search,
    ),
    searchContentDescription: String = "Search",
    clearContentDescription: String = "Clear search",
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.focusRequester(focusRequester),
        size = size,
        style = style,
        field = field,
        status = status,
        enabled = enabled,
        readOnly = readOnly,
        loading = loading,
        leading = {
            Icon(
                imageVector = BundledIcons.Search,
                contentDescription = searchContentDescription,
            )
        },
        trailing = {
            if (enabled && !readOnly && value.isNotBlank()) {
                IconButton(
                    onClick = {
                        onValueChange("")
                        focusRequester.requestFocus()
                    },
                    // Focus belongs to the field. Letting the clear control take a tab stop puts
                    // a dead end between the query and whatever comes after it.
                    modifier = Modifier.focusProperties {
                        canFocus = false
                    },
                ) {
                    Icon(
                        imageVector = BundledIcons.Cross,
                        contentDescription = clearContentDescription,
                    )
                }
            }
        },
        placeholder = placeholder,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        // A query is one line. Without this the field grows a second one on Enter.
        singleLine = true,
    )
}
