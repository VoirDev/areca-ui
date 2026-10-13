package dev.voir.areca.foundation.field

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.foundation.text.Text

/**
 * Style every field below this point uses unless it is given one.
 *
 * `ArecaThemeProvider` seeds it with [FieldStyle.outlined]. It stays nullable, like the other
 * component locals, so a field outside any provider still resolves: [FieldDefaults.style] falls
 * back to [FieldStyle.outlined] when it is null. Provide it yourself to theme a screen or an
 * application:
 *
 * ```kotlin
 * CompositionLocalProvider(LocalFieldStyle provides FieldStyle.solidNeutral()) {
 *     // every field in here is filled rather than outlined
 * }
 * ```
 */
public val LocalFieldStyle: ProvidableCompositionLocal<FieldStyle?> =
    compositionLocalOf { null }

/**
 * Size every field below this point uses unless it is given one.
 *
 * `ArecaThemeProvider` seeds it with [FieldSize.Default]; [FieldDefaults.size] falls back to the
 * same step when it is null. See [LocalFieldStyle] for why it is nullable.
 */
public val LocalFieldSize: ProvidableCompositionLocal<FieldSize?> =
    compositionLocalOf { null }

/**
 * Stock slots and the resolved ambient style and size for [Field] and everything built on it.
 */
public object FieldDefaults {
    /**
     * The style a field takes when it is not given one: the ambient [LocalFieldStyle], or
     * [FieldStyle.outlined] when nothing has been provided.
     */
    public val style: FieldStyle
        @Composable get() = LocalFieldStyle.current ?: FieldStyle.outlined()

    /**
     * The size a field takes when it is not given one: the ambient [LocalFieldSize], or
     * [FieldSize.Default] when nothing has been provided.
     */
    public val size: FieldSize
        @Composable get() = LocalFieldSize.current ?: FieldSize.Default

    /**
     * A placeholder clamped to one line, for a field whose value is one line.
     *
     * @param text Hint shown while the field is empty.
     */
    @Composable
    public fun Placeholder(text: String) {
        Text(
            text = text,
            minLines = 1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
