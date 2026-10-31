package dev.voir.areca.foundation.field

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.icon.ChevronSize
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics for the label that describes a field.
 *
 * @property textStyle Type for the label and its required marker.
 * @property gap Space between the label and the container it describes, for a
 *   [FieldLabelPlacement.External] placement.
 * @property requiredGap Space between the label text and the required marker.
 * @property insideTextStyle Type for a label drawn inside the container under
 *   [FieldLabelPlacement.Inside]. Smaller than [textStyle], so the value stays the loudest
 *   thing in the box. [FieldLabelPlacement.InsideLeading] keeps [textStyle] instead, because
 *   there the label and the value sit on one line and shrinking one of them reads as an error.
 * @property insideGap Space between a [FieldLabelPlacement.Inside] label and the value beneath
 *   it.
 */
@Stable
public data class FieldLabelMetrics(
    public val textStyle: TextStyle,
    public val gap: Dp,
    public val requiredGap: Dp,
    public val insideTextStyle: TextStyle,
    public val insideGap: Dp,
)

/**
 * Metrics for the single line of text that explains a field.
 *
 * @property textStyle Type for the assistive or validation message.
 * @property gap Space between the container and the message.
 * @property reservedHeight Height held below the container under
 *   [FieldMessageBehavior.Reserve] when no height is named. One line of message type.
 */
@Stable
public data class FieldMessageMetrics(
    public val textStyle: TextStyle,
    public val gap: Dp,
    public val reservedHeight: Dp,
)

/**
 * Metrics for a [Field] at one size on the ramp.
 *
 * Sizes travel as a unit rather than as loose dimensions so that everything inside a field —
 * icons, spinner, icon buttons, type — scales together. [Field] publishes them through the
 * ambient locals its children read, so a slot does not have to be told what size it is in.
 *
 * @property borderWidth Container border thickness, and the thickness of the rule between the
 *   segments of a [FieldRow].
 * @property focusRing Thickness of the halo [FieldStyle.focusRing] draws just outside the
 *   container. Drawn outside the field's own bounds, so it costs no layout and can overlap a
 *   neighbour that sits tight against it.
 * @property padding Inset between the container edge and its row of content.
 * @property gap Space between the leading slot, the value, and the trailing slot.
 * @property icon Size for icons in the leading and trailing slots.
 * @property spinner Size of the loading indicator.
 * @property iconButton Size for icon buttons in the trailing slot.
 * @property textStyle Type for the value and the placeholder.
 * @property minHeight Container height floor. A multi-line value grows past it.
 * @property label Metrics for the label describing the field.
 * @property message Metrics for the assistive or validation line.
 * @property chevron Size of the chevron a select draws in its trailing slot.
 */
@Stable
public data class FieldSize(
    public val borderWidth: Dp,
    public val focusRing: Dp,
    public val padding: PaddingValues,
    public val gap: Dp,
    public val icon: Dp,
    public val spinner: SpinnerSize,
    public val iconButton: IconButtonSize,
    public val textStyle: TextStyle,
    public val minHeight: Dp,
    public val label: FieldLabelMetrics,
    public val message: FieldMessageMetrics,
    public val chevron: ChevronSize = ChevronSize.Small,
) {
    /** The steps of the ramp, and the label and message metrics they share. */
    public companion object {
        /**
         * Theme default: the [Medium] step, comfortable to hit on a touch screen. What
         * `ArecaThemeProvider` seeds [LocalFieldSize] with, and what [FieldDefaults.size] falls back
         * to when nothing has been provided.
         */
        public val Default: FieldSize
            @Composable
            get() = Medium

        /**
         * The label metrics every stock size shares.
         *
         * Label type does not currently scale with the container, so that a form mixing sizes
         * keeps one voice for its labels. Override this on a single size to change that.
         *
         * @return Label type and spacing.
         */
        @Composable
        public fun defaultLabel(): FieldLabelMetrics = FieldLabelMetrics(
            textStyle = ArecaTheme.typography.labelMedium,
            gap = ArecaTheme.spacing.xs,
            requiredGap = ArecaTheme.spacing.xs2,
            insideTextStyle = ArecaTheme.typography.labelSmall,
            insideGap = ArecaTheme.spacing.xs2,
        )

        /**
         * The message metrics every stock size shares.
         *
         * @return Message type and spacing.
         */
        @Composable
        public fun defaultMessage(): FieldMessageMetrics = FieldMessageMetrics(
            textStyle = ArecaTheme.typography.bodySmall,
            gap = ArecaTheme.spacing.xs,
            reservedHeight = 20.dp,
        )

        /**
         * The tightest size on the ramp, 32dp tall under touch, 28dp under a pointer.
         *
         * Below the 44dp comfortable touch target, so keep it to pointer-driven density —
         * toolbars, table cells, filter bars — rather than a form somebody fills in on a phone.
         */
        public val ExtraSmall: FieldSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return FieldSize(
                    borderWidth = 1.dp,
                    focusRing = 3.dp,
                    padding = PaddingValues(
                        horizontal = sizing.select(touch = spacing.md, pointer = spacing.sm),
                        vertical = sizing.select(touch = spacing.sm, pointer = spacing.xs),
                    ),
                    gap = sizing.select(touch = spacing.sm, pointer = spacing.xs),
                    icon = sizing.select(touch = 14.dp, pointer = 12.dp),
                    textStyle = ArecaTheme.baseTypography.sm,
                    minHeight = sizing.controlXs,
                    spinner = SpinnerSize(container = 12.dp, stroke = 1.dp),
                    iconButton = IconButtonSize(
                        borderWidth = 1.dp,
                        container = sizing.select(touch = 16.dp, pointer = 14.dp),
                        icon = sizing.select(touch = 14.dp, pointer = 12.dp),
                        spinner = SpinnerSize(container = 12.dp, stroke = 1.dp),
                    ),
                    label = defaultLabel(),
                    message = defaultMessage(),
                    chevron = ChevronSize.ExtraSmall,
                )
            }

        /**
         * A compact size, 40dp tall under touch, 32dp under a pointer — dense forms and search
         * fields.
         */
        public val Small: FieldSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return FieldSize(
                    borderWidth = 1.dp,
                    focusRing = 3.dp,
                    padding = PaddingValues(
                        horizontal = sizing.select(touch = spacing.lg, pointer = spacing.md),
                        vertical = sizing.select(touch = spacing.md, pointer = spacing.sm),
                    ),
                    gap = sizing.select(touch = spacing.md, pointer = spacing.sm),
                    icon = sizing.select(touch = 20.dp, pointer = 16.dp),
                    textStyle = sizing.select(touch = ArecaTheme.baseTypography.md, pointer = ArecaTheme.baseTypography.sm),
                    minHeight = sizing.controlSm,
                    spinner = sizing.select(
                        touch = SpinnerSize(container = 18.dp, stroke = 2.dp),
                        pointer = SpinnerSize(container = 14.dp, stroke = 1.dp),
                    ),
                    iconButton = IconButtonSize(
                        borderWidth = 1.dp,
                        container = sizing.select(touch = 20.dp, pointer = 18.dp),
                        icon = sizing.select(touch = 16.dp, pointer = 14.dp),
                        spinner = sizing.select(
                            touch = SpinnerSize(container = 16.dp, stroke = 1.dp),
                            pointer = SpinnerSize(container = 14.dp, stroke = 1.dp),
                        ),
                    ),
                    label = defaultLabel(),
                    message = defaultMessage(),
                    chevron = ChevronSize.Small,
                )
            }

        /**
         * The default size, 48dp tall under touch, 36dp under a pointer — comfortable to hit on a
         * touch screen.
         */
        public val Medium: FieldSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return FieldSize(
                    borderWidth = 1.dp,
                    focusRing = 3.dp,
                    padding = PaddingValues(
                        horizontal = sizing.select(touch = spacing.xl, pointer = spacing.lg),
                        vertical = sizing.select(touch = spacing.lg, pointer = spacing.md),
                    ),
                    gap = sizing.select(touch = spacing.lg, pointer = spacing.md),
                    icon = sizing.select(touch = 22.dp, pointer = 18.dp),
                    textStyle = sizing.select(touch = ArecaTheme.baseTypography.md, pointer = ArecaTheme.baseTypography.sm),
                    minHeight = sizing.controlMd,
                    spinner = sizing.select(
                        touch = SpinnerSize(18.dp),
                        pointer = SpinnerSize(16.dp),
                    ),
                    iconButton = IconButtonSize(
                        borderWidth = 1.dp,
                        container = sizing.select(touch = 22.dp, pointer = 20.dp),
                        icon = sizing.select(touch = 18.dp, pointer = 16.dp),
                        spinner = sizing.select(
                            touch = SpinnerSize(container = 18.dp, stroke = 1.dp),
                            pointer = SpinnerSize(container = 16.dp, stroke = 1.dp),
                        ),
                    ),
                    label = defaultLabel(),
                    message = defaultMessage(),
                    chevron = ChevronSize.Small,
                )
            }

        /**
         * A roomy size, 56dp tall under touch, 44dp under a pointer — a short form that is the
         * whole screen.
         *
         * Matches [dev.voir.areca.foundation.button.ButtonSize.Large], so a field and the button that
         * submits it line up when they sit side by side.
         */
        public val Large: FieldSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return FieldSize(
                    borderWidth = 1.dp,
                    focusRing = 3.dp,
                    padding = PaddingValues(
                        horizontal = sizing.select(touch = spacing.xl2, pointer = spacing.xl),
                        vertical = sizing.select(touch = spacing.xl, pointer = spacing.lg),
                    ),
                    gap = sizing.select(touch = spacing.lg, pointer = spacing.md),
                    icon = sizing.select(touch = 24.dp, pointer = 20.dp),
                    textStyle = ArecaTheme.baseTypography.md,
                    minHeight = sizing.controlLg,
                    spinner = sizing.select(
                        touch = SpinnerSize(20.dp),
                        pointer = SpinnerSize(18.dp),
                    ),
                    iconButton = IconButtonSize(
                        borderWidth = 1.dp,
                        container = sizing.select(touch = 24.dp, pointer = 22.dp),
                        icon = sizing.select(touch = 20.dp, pointer = 18.dp),
                        spinner = sizing.select(
                            touch = SpinnerSize(container = 20.dp, stroke = 2.dp),
                            pointer = SpinnerSize(container = 18.dp, stroke = 1.dp),
                        ),
                    ),
                    label = defaultLabel(),
                    message = defaultMessage(),
                    chevron = ChevronSize.Medium,
                )
            }

        /**
         * The largest size on the ramp, 64dp tall under touch, 52dp under a pointer, and the only
         * one whose value type grows.
         *
         * For the single field a screen exists for — an amount, a one-time code, a sign-in email.
         * In a form of several fields it reads as a mistake rather than as emphasis.
         */
        public val ExtraLarge: FieldSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return FieldSize(
                    borderWidth = 1.dp,
                    focusRing = 4.dp,
                    padding = PaddingValues(
                        horizontal = sizing.select(touch = spacing.xl3, pointer = spacing.xl2),
                        vertical = sizing.select(touch = spacing.xl2, pointer = spacing.lg),
                    ),
                    gap = sizing.select(touch = spacing.xl, pointer = spacing.lg),
                    icon = sizing.select(touch = 24.dp, pointer = 22.dp),
                    textStyle = ArecaTheme.baseTypography.lg,
                    minHeight = sizing.controlXl,
                    spinner = sizing.select(
                        touch = SpinnerSize(24.dp),
                        pointer = SpinnerSize(20.dp),
                    ),
                    iconButton = IconButtonSize(
                        borderWidth = 1.dp,
                        container = sizing.select(touch = 28.dp, pointer = 24.dp),
                        icon = sizing.select(touch = 22.dp, pointer = 20.dp),
                        spinner = sizing.select(
                            touch = SpinnerSize(container = 22.dp, stroke = 2.dp),
                            pointer = SpinnerSize(container = 20.dp, stroke = 2.dp),
                        ),
                    ),
                    label = defaultLabel(),
                    message = defaultMessage(),
                    chevron = ChevronSize.Medium,
                )
            }
    }
}
