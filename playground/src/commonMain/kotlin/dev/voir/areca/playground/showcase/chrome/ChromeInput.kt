package dev.voir.areca.playground.showcase.chrome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * A single-line text input in the showcase shell, used by text controls.
 *
 * @param value Current text.
 * @param onValueChange Invoked with the edited text.
 * @param modifier Modifier applied to the field.
 * @param placeholder Shown when [value] is empty.
 * @param enabled Whether the field accepts input.
 */
@Composable
fun ChromeInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val shape = RoundedCornerShape(ArecaTheme.radius.md)

    val borderColor = when {
        !enabled -> ArecaTheme.colors.borderDisabled
        focused -> ArecaTheme.colors.accentBase
        else -> ArecaTheme.colors.borderBase
    }

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 160.dp)
            .clip(shape)
            .background(
                if (enabled) ArecaTheme.colors.bgSurface else ArecaTheme.colors.bgDisabled,
                shape,
            )
            .border(1.dp, borderColor, shape)
            .padding(horizontal = ArecaTheme.spacing.md, vertical = ArecaTheme.spacing.md),
        contentAlignment = Alignment.CenterStart,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = true,
            interactionSource = interactionSource,
            textStyle = ChromeTextRole.Label.style.merge(color = ChromeTone.Primary.color),
            cursorBrush = SolidColor(ArecaTheme.colors.accentBase),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            decorationBox = { innerTextField ->
                if (value.isEmpty() && placeholder.isNotEmpty()) {
                    ChromeText(
                        text = placeholder,
                        role = ChromeTextRole.Label,
                        tone = ChromeTone.Muted,
                        maxLines = 1,
                    )
                }
                innerTextField()
            },
        )
    }
}
