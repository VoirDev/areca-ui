package dev.voir.areca.keyboard.key

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.voir.areca.foundation.button.ButtonStyle

/**
 * What a key shows.
 *
 * Separating the appearance from [Key] is what keeps a key a single type: every key has the same
 * identity, weight and behaviour whether it shows a digit, an icon, or something the caller drew
 * itself, so a grid transform can rewrite any key without knowing which kind it is.
 */
@Immutable
public sealed interface KeyContent {
    /**
     * A short piece of text, drawn in the key style's content color on a single line.
     *
     * @property text The label, such as `"7"` or `"AC"`.
     */
    public data class Label(public val text: String) : KeyContent

    /**
     * An icon, tinted with the key style's content color.
     *
     * @property image The vector to draw.
     */
    public data class Icon(public val image: ImageVector) : KeyContent

    /**
     * Arbitrary content, for a key no label or icon describes.
     *
     * The escape hatch: a key showing a flag and a currency code, a key with its own gesture
     * handling, a key that animates. It is composed inside the key's container, so it still gets the
     * key's fill, shape and press feedback.
     *
     * @property content What to draw inside the key.
     */
    public data class Custom(public val content: @Composable () -> Unit) : KeyContent

    /**
     * Nothing at all — a hole in the grid that takes up space.
     *
     * A row of four keys with one blank keeps its other three aligned with the rows above and below,
     * which dropping the key would not.
     */
    public data object Blank : KeyContent
}

/**
 * What a key means to the person pressing it, which decides how loud it looks.
 *
 * Named for meaning rather than loudness, like [dev.voir.areca.foundation.button.ActionColor]: a
 * grid marks its numbers [Digit], its operators [Action], and the key that concludes entry
 * [Confirm], and the keyboard's [KeyStyle] turns each role into a look. A key carrying a
 * [Key.style] of its own ignores its role's look.
 */
public enum class KeyRole {
    /** A number or a separator. The quiet majority of the keyboard. */
    Digit,

    /** An operator or a modifier: `+`, `×`, shift, a currency switch. */
    Action,

    /** A key that concludes something: equals, done, submit. Spend it sparingly. */
    Confirm,

    /** A key that throws work away: delete, backspace, clear all. */
    Destructive,

    /** A key that resets rather than destroys: clear the entry, undo the last operation. */
    Caution,

    /** A key that brings something in from outside: paste, scan, pick from history. */
    Info,

    /** A key that must not pull the eye: hide the keyboard, switch layout. */
    Ghost,
}

/**
 * One key of a [KeyboardGrid].
 *
 * A single type rather than a variant per appearance, so `mapKeys` and `replaceKey` can rewrite any
 * key in a grid — restyle it, disable it, relabel it — without matching on what it happens to show.
 * Build them through the factories on the companion rather than the constructor.
 *
 * @property id Identity, unique within a grid. Used to address the key in a transform, and as its
 *   stable key during composition.
 * @property content What the key shows.
 * @property onClick Called when the key is pressed. Never called for a [KeyContent.Blank] key.
 * @property weight Share of its row's width, above zero. Two keys at `1f` and `2f` split it
 *   one-third to two-thirds; a row of four `1f` keys is an even quarter each.
 * @property enabled Whether the key can be pressed. A disabled key stays visible and greys out.
 * @property role What the key means, which picks its look from the keyboard's [KeyStyle].
 * @property style Colors, shape, and press feedback overriding [role]'s look. `null` keeps the
 *   role's look, which is what most keys want.
 * @property contentDescription What assistive technology announces. Required in practice for an
 *   icon key, where there is no label to read; `null` on a label key lets the label speak for
 *   itself.
 * @throws IllegalArgumentException When [weight] is not a finite number above zero.
 */
@Immutable
public data class Key(
    public val id: String,
    public val content: KeyContent,
    public val onClick: () -> Unit,
    public val weight: Float = 1f,
    public val enabled: Boolean = true,
    public val role: KeyRole = KeyRole.Digit,
    public val style: ButtonStyle? = null,
    public val contentDescription: String? = null,
) {
    init {
        require(weight > 0f && weight.isFinite()) {
            "Key '$id' needs a finite weight above zero, was $weight."
        }
    }

    /** Factories for each kind of [KeyContent], the intended way to build a [Key]. */
    public companion object {
        /**
         * A key showing [label].
         *
         * @param label Text to show, which doubles as the key's [id] unless one is given.
         * @param id Identity within the grid.
         * @param weight Share of the row's width, above zero.
         * @param enabled Whether the key can be pressed.
         * @param role What the key means, which picks its look.
         * @param style Colors, shape, and press feedback overriding [role]'s look.
         * @param contentDescription What assistive technology announces instead of the label.
         * @param onClick Called when the key is pressed.
         * @return A label key.
         * @throws IllegalArgumentException When [weight] is not a finite number above zero.
         */
        public fun text(
            label: String,
            id: String = label,
            weight: Float = 1f,
            enabled: Boolean = true,
            role: KeyRole = KeyRole.Digit,
            style: ButtonStyle? = null,
            contentDescription: String? = null,
            onClick: () -> Unit,
        ): Key = Key(
            id = id,
            content = KeyContent.Label(label),
            onClick = onClick,
            weight = weight,
            enabled = enabled,
            role = role,
            style = style,
            contentDescription = contentDescription,
        )

        /**
         * A key showing [image].
         *
         * @param image Vector to draw.
         * @param id Identity within the grid.
         * @param contentDescription What assistive technology announces. An icon key has no label to
         *   fall back on, so this should almost always be given.
         * @param weight Share of the row's width, above zero.
         * @param enabled Whether the key can be pressed.
         * @param role What the key means, which picks its look.
         * @param style Colors, shape, and press feedback overriding [role]'s look.
         * @param onClick Called when the key is pressed.
         * @return An icon key.
         * @throws IllegalArgumentException When [weight] is not a finite number above zero.
         */
        public fun icon(
            image: ImageVector,
            id: String,
            contentDescription: String? = null,
            weight: Float = 1f,
            enabled: Boolean = true,
            role: KeyRole = KeyRole.Digit,
            style: ButtonStyle? = null,
            onClick: () -> Unit,
        ): Key = Key(
            id = id,
            content = KeyContent.Icon(image),
            onClick = onClick,
            weight = weight,
            enabled = enabled,
            role = role,
            style = style,
            contentDescription = contentDescription,
        )

        /**
         * A key drawing whatever [content] draws.
         *
         * @param id Identity within the grid.
         * @param weight Share of the row's width, above zero.
         * @param enabled Whether the key can be pressed.
         * @param role What the key means, which picks its look.
         * @param style Colors, shape, and press feedback overriding [role]'s look.
         * @param contentDescription What assistive technology announces.
         * @param onClick Called when the key is pressed.
         * @param content What to draw inside the key.
         * @return A custom key.
         * @throws IllegalArgumentException When [weight] is not a finite number above zero.
         */
        public fun custom(
            id: String,
            weight: Float = 1f,
            enabled: Boolean = true,
            role: KeyRole = KeyRole.Digit,
            style: ButtonStyle? = null,
            contentDescription: String? = null,
            onClick: () -> Unit,
            content: @Composable () -> Unit,
        ): Key = Key(
            id = id,
            content = KeyContent.Custom(content),
            onClick = onClick,
            weight = weight,
            enabled = enabled,
            role = role,
            style = style,
            contentDescription = contentDescription,
        )

        /**
         * A hole in the grid, holding a slot open so the keys around it stay aligned.
         *
         * @param id Identity within the grid. Required, and unique like any other key's, so two
         *   blanks in one grid stay distinct.
         * @param weight Share of the row's width to leave empty, above zero.
         * @return A blank key, which draws nothing and does nothing.
         * @throws IllegalArgumentException When [weight] is not a finite number above zero.
         */
        public fun blank(
            id: String,
            weight: Float = 1f,
        ): Key = Key(
            id = id,
            content = KeyContent.Blank,
            onClick = {},
            weight = weight,
            enabled = false,
        )
    }
}
