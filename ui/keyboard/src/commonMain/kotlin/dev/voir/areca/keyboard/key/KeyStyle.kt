package dev.voir.areca.keyboard.key

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.indication.FocusOutline
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.theme.ArecaTheme

/**
 * The look of each [KeyRole] on a keyboard.
 *
 * Keys are drawn with the foundation's [dev.voir.areca.foundation.button.Button], so each look *is*
 * a [ButtonStyle] and anything that styles a button styles a key. A key takes the look of its
 * [Key.role] unless it carries a [Key.style] of its own.
 *
 * Carried by [dev.voir.areca.keyboard.layout.KeyboardStyle.keys], so one keyboard style themes the
 * surface and its keys together.
 *
 * @property digit Look of [KeyRole.Digit] keys: neutral and tinted, the quiet majority.
 * @property action Look of [KeyRole.Action] keys: brand-tinted, so operators read apart from digits
 *   without shouting over them.
 * @property confirm Look of [KeyRole.Confirm] keys: filled brand, the loudest thing on the keyboard.
 * @property destructive Look of [KeyRole.Destructive] keys: danger-tinted.
 * @property caution Look of [KeyRole.Caution] keys: warning-tinted, a step below [destructive].
 * @property info Look of [KeyRole.Info] keys: info-tinted.
 * @property ghost Look of [KeyRole.Ghost] keys: no fill at all.
 */
@Immutable
public data class KeyStyle(
    public val digit: ButtonStyle,
    public val action: ButtonStyle,
    public val confirm: ButtonStyle,
    public val destructive: ButtonStyle,
    public val caution: ButtonStyle,
    public val info: ButtonStyle,
    public val ghost: ButtonStyle,
) {
    /**
     * The look of [role].
     *
     * @param role What a key means.
     * @return The style keys of that role are drawn with.
     */
    public fun of(role: KeyRole): ButtonStyle = when (role) {
        KeyRole.Digit -> digit
        KeyRole.Action -> action
        KeyRole.Confirm -> confirm
        KeyRole.Destructive -> destructive
        KeyRole.Caution -> caution
        KeyRole.Info -> info
        KeyRole.Ghost -> ghost
    }

    /** Theme-backed [KeyStyle] preset and its builder. */
    public companion object {
        /** Theme default: every role in the theme's large rounded shape. */
        public val Default: KeyStyle
            @Composable
            get() = default()

        /**
         * Builds the key looks from the current theme.
         *
         * Every look defaults to [shape], so changing the corner of every key is one argument.
         *
         * @param shape Shape of every key unless its own look says otherwise.
         * @param digit Look of [KeyRole.Digit] keys.
         * @param action Look of [KeyRole.Action] keys.
         * @param confirm Look of [KeyRole.Confirm] keys.
         * @param destructive Look of [KeyRole.Destructive] keys.
         * @param caution Look of [KeyRole.Caution] keys.
         * @param info Look of [KeyRole.Info] keys.
         * @param ghost Look of [KeyRole.Ghost] keys.
         * @return Key looks built from the given values.
         */
        @Composable
        public fun default(
            shape: Shape = ArecaTheme.shapes.lg,
            digit: ButtonStyle = ButtonStyle.soft(ActionColor.Neutral, shape),
            action: ButtonStyle = ButtonStyle.soft(ActionColor.Brand, shape),
            confirm: ButtonStyle = ButtonStyle.solid(ActionColor.Brand, shape),
            destructive: ButtonStyle = ButtonStyle.soft(ActionColor.Danger, shape),
            caution: ButtonStyle = ButtonStyle.soft(ActionColor.Warning, shape),
            info: ButtonStyle = infoKey(shape),
            ghost: ButtonStyle = ButtonStyle.plain(ActionColor.Neutral, shape),
        ): KeyStyle = KeyStyle(
            digit = digit,
            action = action,
            confirm = confirm,
            destructive = destructive,
            caution = caution,
            info = info,
            ghost = ghost,
        )

        /**
         * The info-tinted key look.
         *
         * Built from the palette's info tokens directly rather than through an [ActionColor],
         * because the foundation deliberately exposes no informational action family — an
         * informational *button* is almost always a mistake. On a keyboard it is not: it marks the
         * one key whose effect comes from somewhere other than the keyboard itself.
         *
         * @param shape Container shape.
         * @return An info-tinted key style.
         */
        @Composable
        private fun infoKey(shape: Shape): ButtonStyle = ButtonStyle(
            bg = ArecaTheme.colors.bgInfoMuted,
            bgDisabled = ArecaTheme.colors.bgDisabled,
            border = Color.Transparent,
            content = ArecaTheme.colors.fgInfoText,
            contentDisabled = ArecaTheme.colors.fgDisabled,
            shape = shape,
            indication = TouchIndication(
                color = ArecaTheme.colors.bgInfoBase,
                focusOutline = FocusOutline(color = ArecaTheme.colors.bgInfoBase),
                shape = shape,
            ),
        )
    }
}
