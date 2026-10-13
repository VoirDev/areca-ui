package dev.voir.areca.crop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of a [CropToolbar].
 *
 * The bar's Cancel, Done, Undo and OK buttons and the tool action in its middle are foundation
 * components, drawn in a dark theme scope and following their own styles there; this covers what the
 * toolbar draws itself. Options and chips answer a press with foundation's tint indication, in the
 * dark scope's ink.
 *
 * @property bg Ground of the whole toolbar. Defaults to [ImageCropperStyle.stage], so the editor reads
 *   as one surface.
 * @property optionContent Icon of an option.
 * @property optionBg Circle behind an option's icon.
 * @property optionLabel Name under an option.
 * @property optionSelectedContent Icon of a toggle option that is on, such as Mirror.
 * @property optionSelectedBg Circle behind a toggle option that is on.
 * @property optionSelectedLabel Name under a toggle option that is on.
 * @property optionDisabledContent Icon and name of an option with nothing to do, such as Reset on a
 *   fresh crop.
 * @property optionChangedIndicator Dot on an option whose value has left its default.
 * @property separator Rule between Reset and the other options.
 * @property title Screen title in the middle of the bar while no tool is open.
 * @property value A dial's value above the dial, at its origin.
 * @property valueChanged A dial's value once it has left its origin.
 * @property chipContent Label and glyph of an aspect chip.
 * @property chipBorder Outline of an aspect chip.
 * @property chipSelectedContent Label and glyph of the chosen aspect.
 * @property chipSelectedBg Fill of the chosen aspect.
 * @property dial Colors of the dial.
 */
@Immutable
public data class CropToolbarStyle(
    public val bg: Color,
    public val optionContent: Color,
    public val optionBg: Color,
    public val optionLabel: Color,
    public val optionSelectedContent: Color,
    public val optionSelectedBg: Color,
    public val optionSelectedLabel: Color,
    public val optionDisabledContent: Color,
    public val optionChangedIndicator: Color,
    public val separator: Color,
    public val title: Color,
    public val value: Color,
    public val valueChanged: Color,
    public val chipContent: Color,
    public val chipBorder: Color,
    public val chipSelectedContent: Color,
    public val chipSelectedBg: Color,
    public val dial: CropDialStyle,
) {
    /** Presets. */
    public companion object {
        /**
         * Neutral option circles, a brand fill for a toggle that is on and a brand dot for a changed
         * value, and chips that invert when chosen — in the dark colors the cropper always uses.
         */
        public val Default: CropToolbarStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme's dark colors.
         *
         * @param bg Toolbar ground; the stage's, from [LocalImageCropperStyle] when one is provided.
         * @param optionContent Option icon.
         * @param optionBg Option circle.
         * @param optionLabel Option name.
         * @param optionSelectedContent Icon of a toggle that is on.
         * @param optionSelectedBg Circle of a toggle that is on.
         * @param optionSelectedLabel Name of a toggle that is on.
         * @param optionDisabledContent Option with nothing to do.
         * @param optionChangedIndicator Dot on a changed option.
         * @param separator Rule after Reset.
         * @param title Bar title.
         * @param value Dial value at its origin.
         * @param valueChanged Dial value away from its origin.
         * @param chipContent Aspect chip label.
         * @param chipBorder Aspect chip outline.
         * @param chipSelectedContent Chosen aspect label.
         * @param chipSelectedBg Chosen aspect fill.
         * @param dial Dial colors; [LocalCropDialStyle]'s when one is provided.
         * @return The style.
         */
        @Composable
        public fun default(
            bg: Color = (LocalImageCropperStyle.current ?: ImageCropperStyle.Default).stage,
            optionContent: Color = ArecaTheme.darkColors.fgBase,
            optionBg: Color = ArecaTheme.darkColors.bgSubtle,
            optionLabel: Color = ArecaTheme.darkColors.fgMuted,
            optionSelectedContent: Color = ArecaTheme.darkColors.fgOnFill,
            optionSelectedBg: Color = ArecaTheme.darkColors.bgBrandBase,
            optionSelectedLabel: Color = ArecaTheme.darkColors.fgBrandText,
            optionDisabledContent: Color = ArecaTheme.darkColors.fgDisabled,
            optionChangedIndicator: Color = ArecaTheme.darkColors.bgBrandBase,
            separator: Color = ArecaTheme.darkColors.borderSubtle,
            title: Color = ArecaTheme.darkColors.fgBase,
            value: Color = ArecaTheme.darkColors.fgMuted,
            valueChanged: Color = ArecaTheme.darkColors.fgBrandText,
            chipContent: Color = ArecaTheme.darkColors.fgSubtle,
            chipBorder: Color = ArecaTheme.darkColors.borderStrong,
            chipSelectedContent: Color = ArecaTheme.darkColors.bgCanvas,
            chipSelectedBg: Color = ArecaTheme.darkColors.fgBase,
            dial: CropDialStyle = LocalCropDialStyle.current ?: CropDialStyle.Default,
        ): CropToolbarStyle = CropToolbarStyle(
            bg = bg,
            optionContent = optionContent,
            optionBg = optionBg,
            optionLabel = optionLabel,
            optionSelectedContent = optionSelectedContent,
            optionSelectedBg = optionSelectedBg,
            optionSelectedLabel = optionSelectedLabel,
            optionDisabledContent = optionDisabledContent,
            optionChangedIndicator = optionChangedIndicator,
            separator = separator,
            title = title,
            value = value,
            valueChanged = valueChanged,
            chipContent = chipContent,
            chipBorder = chipBorder,
            chipSelectedContent = chipSelectedContent,
            chipSelectedBg = chipSelectedBg,
            dial = dial,
        )
    }
}

/**
 * Default [CropToolbarStyle] in scope, or `null` to use [CropToolbarStyle.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalImageCropperStyle] for why.
 */
public val LocalCropToolbarStyle: ProvidableCompositionLocal<CropToolbarStyle?> = compositionLocalOf { null }
