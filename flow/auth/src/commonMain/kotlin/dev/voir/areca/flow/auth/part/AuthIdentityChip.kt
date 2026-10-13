package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import dev.voir.areca.flow.auth.icon.BundledIcons
import dev.voir.areca.flow.auth.icon.Check
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.field.FieldBorderPlacement
import dev.voir.areca.foundation.field.FieldDefaults
import dev.voir.areca.foundation.field.FieldGroupDefaults
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * The account a step of the flow is about — "ada@example.com" — with the way back to choose
 * another.
 *
 * An email-first sign-in asks for the address on one screen and for the password or the code on
 * the next. The second screen has to say which account it is asking about, because a person with
 * two addresses will otherwise type the right password for the wrong one; and it has to offer the
 * way back from right there, rather than only from a back chevron somebody has to know means
 * "different email".
 *
 * It is drawn from the theme's field defaults — the read-only fill, border, text colour, shape,
 * height and inset of the field the address was typed into — so it reads as the answer to the
 * previous screen's question rather than as something new to fill in. A long address is shortened
 * in the middle, which keeps both the start a person recognises and the domain that tells two of
 * their addresses apart.
 *
 * @param identifier The address, username or phone number, as the person typed it.
 * @param modifier Modifier applied to the chip.
 * @param onChange Called when the person asks to use a different account, or `null` for a chip with
 *   no way back — once the address has been verified, say.
 * @param changeLabel Caption of the button that goes back. It is also the button's accessible name,
 *   so what a screen reader says matches what is drawn.
 * @param onChangeLabel What pressing that button does, for assistive technology — "Use a different
 *   email" — read after its [changeLabel] where the platform reads action labels, or `null` for
 *   none. Worth setting: "Change", heard out of context, does not say what changes.
 * @param verified Whether to mark the address as confirmed, with a tick after it.
 * @param verifiedDescription How the tick is read aloud.
 * @param label What the identifier is — "Account", "New email" — drawn above the chip the way a
 *   field's label is, or `null` for none. Worth giving where the chip is the page's only mention of
 *   the account, as on a page an emailed link opens.
 * @param enabled Whether the way back can be pressed. Pass `false` while an attempt is in flight.
 */
@Composable
public fun AuthIdentityChip(
    identifier: String,
    modifier: Modifier = Modifier,
    onChange: (() -> Unit)? = null,
    changeLabel: String = "Change",
    onChangeLabel: String? = null,
    verified: Boolean = false,
    verifiedDescription: String = "Verified",
    label: String? = null,
    enabled: Boolean = true,
) {
    if (label == null) {
        IdentityChip(
            identifier = identifier,
            modifier = modifier,
            onChange = onChange,
            changeLabel = changeLabel,
            onChangeLabel = onChangeLabel,
            verified = verified,
            verifiedDescription = verifiedDescription,
            enabled = enabled,
        )
        return
    }

    val fieldStyle = AuthOnHero.fieldStyle(LocalAuthOnHero.current)
    val fieldSize = FieldDefaults.size

    Column(modifier = modifier.fillMaxWidth()) {
        FieldGroupDefaults.Label(
            text = label,
            color = fieldStyle.label.content,
            textStyle = fieldSize.label.textStyle,
        )
        Spacer(Modifier.height(fieldSize.label.gap))
        IdentityChip(
            identifier = identifier,
            modifier = Modifier,
            onChange = onChange,
            changeLabel = changeLabel,
            onChangeLabel = onChangeLabel,
            verified = verified,
            verifiedDescription = verifiedDescription,
            enabled = enabled,
        )
    }
}

/**
 * The chip itself, without a label.
 *
 * @param identifier The address, username or phone number.
 * @param modifier Modifier applied to the chip.
 * @param onChange Called to use a different account, or `null` for no way back.
 * @param changeLabel Caption and accessible name of the way back.
 * @param onChangeLabel What pressing the way back does, for assistive technology, or `null`.
 * @param verified Whether to mark the identifier as confirmed.
 * @param verifiedDescription How the tick is read aloud.
 * @param enabled Whether the way back can be pressed.
 */
@Composable
private fun IdentityChip(
    identifier: String,
    modifier: Modifier,
    onChange: (() -> Unit)?,
    changeLabel: String,
    onChangeLabel: String?,
    verified: Boolean,
    verifiedDescription: String,
    enabled: Boolean,
) {
    val style = FieldDefaults.style
    val size = FieldDefaults.size
    val layoutDirection = LocalLayoutDirection.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            // The height of the field the address was typed into, so the chip stands in its place.
            .heightIn(min = size.minHeight)
            .background(style.bgReadOnly, style.shape)
            .readOnlyFieldBorder(style = style, width = size.borderWidth)
            .padding(
                start = size.padding.calculateStartPadding(layoutDirection),
                // The button brings its own inset; without one, the text keeps the field's.
                end = if (onChange != null) {
                    ArecaTheme.spacing.xs
                } else {
                    size.padding.calculateEndPadding(layoutDirection)
                },
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = identifier,
            modifier = Modifier.weight(1f),
            style = size.textStyle,
            color = style.contentReadOnly,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis,
        )

        if (verified) {
            Spacer(Modifier.width(size.gap))
            Icon(
                imageVector = BundledIcons.Check,
                contentDescription = verifiedDescription,
                tint = ArecaTheme.colors.fgSuccessText,
                size = size.icon,
            )
        }

        if (onChange != null) {
            Spacer(Modifier.width(ArecaTheme.spacing.xs))
            Button(
                text = changeLabel,
                onClick = onChange,
                modifier = if (onChangeLabel != null && enabled) {
                    // Labels the press rather than renaming the button, so the visible caption
                    // stays its accessible name.
                    Modifier.semantics {
                        onClick(label = onChangeLabel) {
                            onChange()
                            true
                        }
                    }
                } else {
                    Modifier
                },
                style = ButtonStyle.plain(ActionColor.Neutral),
                size = ButtonSize.Small,
                enabled = enabled,
            )
        }
    }
}

/**
 * Strokes a read-only field's border where the field style puts it: all the way round, or as a
 * rule along the bottom edge.
 *
 * @param style Field style, for the read-only border colour, the shape and the placement.
 * @param width Border thickness.
 * @return This modifier, with the border drawn.
 */
private fun Modifier.readOnlyFieldBorder(style: FieldStyle, width: Dp): Modifier =
    when (style.borderPlacement) {
        FieldBorderPlacement.All -> border(width = width, color = style.borderReadOnly, shape = style.shape)
        FieldBorderPlacement.Bottom -> drawBehind {
            val stroke = width.toPx()
            drawRect(
                color = style.borderReadOnly,
                topLeft = Offset(0f, size.height - stroke),
                size = Size(size.width, stroke),
            )
        }
    }
