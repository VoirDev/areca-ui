package dev.voir.areca.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import dev.voir.areca.ExperimentalArecaApi
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.avatar.AvatarStyle
import dev.voir.areca.foundation.avatar.LocalAvatarSize
import dev.voir.areca.foundation.avatar.LocalAvatarStyle
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.IndicatorDotSize
import dev.voir.areca.foundation.badge.LocalBadgeSize
import dev.voir.areca.foundation.badge.LocalBadgeStyle
import dev.voir.areca.foundation.badge.LocalIndicatorDotSize
import dev.voir.areca.foundation.badge.LocalNotificationBadgeSize
import dev.voir.areca.foundation.badge.LocalNotificationBadgeStyle
import dev.voir.areca.foundation.badge.NotificationBadgeSize
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.button.LocalButtonSize
import dev.voir.areca.foundation.button.LocalButtonStyle
import dev.voir.areca.foundation.button.LocalIconButtonSize
import dev.voir.areca.foundation.button.LocalIconButtonStyle
import dev.voir.areca.foundation.collection.ContentListSize
import dev.voir.areca.foundation.collection.ContentListStyle
import dev.voir.areca.foundation.collection.LocalContentListSize
import dev.voir.areca.foundation.collection.LocalContentListStyle
import dev.voir.areca.foundation.feedback.FallbackSize
import dev.voir.areca.foundation.feedback.FallbackStyle
import dev.voir.areca.foundation.feedback.LocalFallbackSize
import dev.voir.areca.foundation.feedback.LocalFallbackStyle
import dev.voir.areca.foundation.feedback.LocalProgressBarSize
import dev.voir.areca.foundation.feedback.LocalProgressBarStyle
import dev.voir.areca.foundation.feedback.LocalRequirementListSize
import dev.voir.areca.foundation.feedback.LocalRequirementListStyle
import dev.voir.areca.foundation.feedback.LocalSkeletonSize
import dev.voir.areca.foundation.feedback.LocalSkeletonStyle
import dev.voir.areca.foundation.feedback.LocalSpinnerSize
import dev.voir.areca.foundation.feedback.LocalSpinnerStyle
import dev.voir.areca.foundation.feedback.LocalStepIndicatorSize
import dev.voir.areca.foundation.feedback.LocalStepIndicatorStyle
import dev.voir.areca.foundation.feedback.LocalToastStyle
import dev.voir.areca.foundation.feedback.ProgressBarSize
import dev.voir.areca.foundation.feedback.ProgressBarStyle
import dev.voir.areca.foundation.feedback.RequirementListSize
import dev.voir.areca.foundation.feedback.RequirementListStyle
import dev.voir.areca.foundation.feedback.SkeletonSize
import dev.voir.areca.foundation.feedback.SkeletonStyle
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.feedback.StepIndicatorSize
import dev.voir.areca.foundation.feedback.StepIndicatorStyle
import dev.voir.areca.foundation.feedback.ToastStyle
import dev.voir.areca.foundation.field.FieldSize
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.field.LocalFieldSize
import dev.voir.areca.foundation.field.LocalFieldStyle
import dev.voir.areca.foundation.field.LocalOtpFieldSize
import dev.voir.areca.foundation.field.OtpFieldSize
import dev.voir.areca.foundation.icon.ChevronSize
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.icon.LocalChevronSize
import dev.voir.areca.foundation.icon.LocalFeaturedIconSize
import dev.voir.areca.foundation.icon.LocalFeaturedIconStyle
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.foundation.layout.CardSize
import dev.voir.areca.foundation.layout.CardStyle
import dev.voir.areca.foundation.layout.DividerSize
import dev.voir.areca.foundation.layout.DividerStyle
import dev.voir.areca.foundation.layout.LocalCardSize
import dev.voir.areca.foundation.layout.LocalCardStyle
import dev.voir.areca.foundation.layout.LocalDividerSize
import dev.voir.areca.foundation.layout.LocalDividerStyle
import dev.voir.areca.foundation.layout.LocalPullToRefreshSize
import dev.voir.areca.foundation.layout.LocalPullToRefreshStyle
import dev.voir.areca.foundation.layout.PullToRefreshSize
import dev.voir.areca.foundation.layout.PullToRefreshStyle
import dev.voir.areca.foundation.list.ListItemSize
import dev.voir.areca.foundation.list.ListItemStyle
import dev.voir.areca.foundation.list.ListSize
import dev.voir.areca.foundation.list.ListStyle
import dev.voir.areca.foundation.list.LocalListItemSize
import dev.voir.areca.foundation.list.LocalListItemStyle
import dev.voir.areca.foundation.list.LocalListSize
import dev.voir.areca.foundation.list.LocalListStyle
import dev.voir.areca.foundation.navigation.LocalNavItemSize
import dev.voir.areca.foundation.navigation.LocalNavItemStyle
import dev.voir.areca.foundation.navigation.LocalNavListSize
import dev.voir.areca.foundation.navigation.LocalPageIndicatorSize
import dev.voir.areca.foundation.navigation.LocalPageIndicatorStyle
import dev.voir.areca.foundation.navigation.NavItemSize
import dev.voir.areca.foundation.navigation.NavItemStyle
import dev.voir.areca.foundation.navigation.NavListSize
import dev.voir.areca.foundation.navigation.PageIndicatorSize
import dev.voir.areca.foundation.navigation.PageIndicatorStyle
import dev.voir.areca.foundation.notice.LocalNoticeSize
import dev.voir.areca.foundation.notice.LocalNoticeStyle
import dev.voir.areca.foundation.notice.NoticeSize
import dev.voir.areca.foundation.overlay.ConfirmationDialogSize
import dev.voir.areca.foundation.overlay.ConfirmationDialogStyle
import dev.voir.areca.foundation.overlay.DropdownPopupSize
import dev.voir.areca.foundation.overlay.DropdownPopupStyle
import dev.voir.areca.foundation.overlay.FlowModalActionBarSize
import dev.voir.areca.foundation.overlay.FlowModalActionBarStyle
import dev.voir.areca.foundation.overlay.FlowModalSize
import dev.voir.areca.foundation.overlay.FlowModalStyle
import dev.voir.areca.foundation.overlay.LocalConfirmSheetSize
import dev.voir.areca.foundation.overlay.LocalConfirmSheetStyle
import dev.voir.areca.foundation.overlay.LocalConfirmationDialogSize
import dev.voir.areca.foundation.overlay.LocalConfirmationDialogStyle
import dev.voir.areca.foundation.overlay.LocalDropdownPopupSize
import dev.voir.areca.foundation.overlay.LocalDropdownPopupStyle
import dev.voir.areca.foundation.overlay.LocalFlowModalActionBarSize
import dev.voir.areca.foundation.overlay.LocalFlowModalActionBarStyle
import dev.voir.areca.foundation.overlay.LocalFlowModalSize
import dev.voir.areca.foundation.overlay.LocalFlowModalStyle
import dev.voir.areca.foundation.overlay.LocalModalSize
import dev.voir.areca.foundation.overlay.LocalModalStyle
import dev.voir.areca.foundation.overlay.ModalSize
import dev.voir.areca.foundation.overlay.ModalStyle
import dev.voir.areca.foundation.selection.CheckboxSize
import dev.voir.areca.foundation.selection.CheckboxStyle
import dev.voir.areca.foundation.selection.LocalCheckboxSize
import dev.voir.areca.foundation.selection.LocalCheckboxStyle
import dev.voir.areca.foundation.selection.LocalNumberStepperSize
import dev.voir.areca.foundation.selection.LocalNumberStepperStyle
import dev.voir.areca.foundation.selection.LocalRadioSize
import dev.voir.areca.foundation.selection.LocalRadioStyle
import dev.voir.areca.foundation.selection.LocalSwatchPickerSize
import dev.voir.areca.foundation.selection.LocalSwatchPickerStyle
import dev.voir.areca.foundation.selection.LocalSwatchSize
import dev.voir.areca.foundation.selection.LocalSwatchStyle
import dev.voir.areca.foundation.selection.LocalToggleSize
import dev.voir.areca.foundation.selection.LocalToggleStyle
import dev.voir.areca.foundation.selection.NumberStepperSize
import dev.voir.areca.foundation.selection.NumberStepperStyle
import dev.voir.areca.foundation.selection.RadioSize
import dev.voir.areca.foundation.selection.RadioStyle
import dev.voir.areca.foundation.selection.SwatchPickerSize
import dev.voir.areca.foundation.selection.SwatchPickerStyle
import dev.voir.areca.foundation.selection.SwatchSize
import dev.voir.areca.foundation.selection.SwatchStyle
import dev.voir.areca.foundation.selection.ToggleSize
import dev.voir.areca.foundation.selection.ToggleStyle
import dev.voir.areca.foundation.status.LocalStatusMarkSize
import dev.voir.areca.foundation.status.LocalStatusMarkStyle
import dev.voir.areca.foundation.status.LocalStatusSceneActionsSize
import dev.voir.areca.foundation.status.LocalStatusSceneActionsStyle
import dev.voir.areca.foundation.status.LocalStatusSceneSize
import dev.voir.areca.foundation.status.LocalStatusSceneStyle
import dev.voir.areca.foundation.status.StatusMarkSize
import dev.voir.areca.foundation.status.StatusMarkStyle
import dev.voir.areca.foundation.status.StatusSceneActionsSize
import dev.voir.areca.foundation.status.StatusSceneActionsStyle
import dev.voir.areca.foundation.status.StatusSceneSize
import dev.voir.areca.foundation.status.StatusSceneStyle
import dev.voir.areca.foundation.tabbar.FloatingTabBarSize
import dev.voir.areca.foundation.tabbar.FloatingTabBarStyle
import dev.voir.areca.foundation.tabbar.LocalFloatingTabBarSize
import dev.voir.areca.foundation.tabbar.LocalFloatingTabBarStyle
import dev.voir.areca.foundation.tabbar.LocalTabBarSize
import dev.voir.areca.foundation.tabbar.LocalTabBarStyle
import dev.voir.areca.foundation.tabbar.TabBarSize
import dev.voir.areca.foundation.tabbar.TabBarStyle
import dev.voir.areca.foundation.table.LocalTableSize
import dev.voir.areca.foundation.table.LocalTableStyle
import dev.voir.areca.foundation.table.TableSize
import dev.voir.areca.foundation.table.TableStyle
import dev.voir.areca.foundation.stat.LocalStatSize
import dev.voir.areca.foundation.stat.LocalStatStyle
import dev.voir.areca.foundation.stat.StatSize
import dev.voir.areca.foundation.stat.StatStyle
import dev.voir.areca.foundation.tabs.LocalTabsSize
import dev.voir.areca.foundation.tabs.LocalTabsStyle
import dev.voir.areca.foundation.tabs.TabsSize
import dev.voir.areca.foundation.tabs.TabsStyle
import dev.voir.areca.foundation.text.HeadingSize
import dev.voir.areca.foundation.text.HeadingStyle
import dev.voir.areca.foundation.text.LocalHeadingSize
import dev.voir.areca.foundation.text.LocalHeadingStyle
import dev.voir.areca.foundation.text.LocalSensitiveTextSize
import dev.voir.areca.foundation.text.LocalSensitiveTextStyle
import dev.voir.areca.foundation.text.SensitiveTextSize
import dev.voir.areca.foundation.text.SensitiveTextStyle
import dev.voir.areca.foundation.topbar.LocalTopBarSize
import dev.voir.areca.foundation.topbar.LocalTopBarStyle
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.foundation.wheel.LocalWheelPickerSize
import dev.voir.areca.foundation.wheel.LocalWheelPickerStyle
import dev.voir.areca.foundation.wheel.WheelPickerSize
import dev.voir.areca.foundation.wheel.WheelPickerStyle

/**
 * Puts the Areca design tokens and component defaults in scope for [content].
 *
 * Wrap an application once, near its root. Nesting a second provider is supported and scopes the
 * override to that subtree — the usual reason is a region that is always dark, such as a media
 * viewer, or a preview that needs a brand palette.
 *
 * Each parameter defaults to a value derived from the one above it, so a caller overrides at the
 * highest level that expresses the intent and everything below follows. Supplying [baseColors]
 * re-themes both directions; supplying [colors] replaces the mapping for this direction only.
 * Reading the tokens back is [ArecaTheme]'s job.
 *
 * @param darkTheme Whether tokens are resolved for the dark theme. Read to derive the default
 *   [colors] and exposed as [ArecaTheme.isDark]; an explicit [colors] wins over it.
 * @param baseColors Raw palette the semantic colors are derived from.
 * @param colors Semantic color tokens. Defaults to [baseColors] mapped for [darkTheme].
 * @param radius Corner radius lengths.
 * @param shapes Corner shapes. Defaults to shapes built from [radius].
 * @param spacing Spacing scale and screen gutters.
 * @param elevation Shadow elevation scale.
 * @param motion Animation duration and easing tokens.
 * @param fonts Font families the type scale is rendered with.
 * @param baseTypography Raw type scale. Defaults to the scale rendered with [fonts].
 * @param typography Named text styles. Defaults to styles built on [baseTypography].
 * @param content Content composed with the provided tokens.
 */
@OptIn(ExperimentalArecaApi::class)
@Composable
public fun ArecaThemeProvider(
    darkTheme: Boolean = false,
    baseColors: ArecaBaseColors = ArecaBaseColors(),
    colors: ArecaColors = if (darkTheme) {
        ArecaColors.dark(baseColors)
    } else {
        ArecaColors.light(baseColors)
    },
    radius: ArecaRadius = ArecaRadius(),
    shapes: ArecaShapes = ArecaShapes.from(radius),
    spacing: ArecaSpacing = ArecaSpacing(),
    elevation: ArecaElevation = ArecaElevation(),
    motion: ArecaMotion = ArecaMotion(),
    fonts: ArecaFonts = ArecaFonts(),
    baseTypography: ArecaBaseTypography = ArecaBaseTypography.from(fonts),
    typography: ArecaTypography = ArecaTypography.from(baseTypography),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalIsDarkTheme provides darkTheme,
        LocalBaseColors provides baseColors,
        LocalColors provides colors,
        LocalRadius provides radius,
        LocalShapes provides shapes,
        LocalSpacing provides spacing,
        LocalElevation provides elevation,
        LocalMotion provides motion,
        LocalFonts provides fonts,
        LocalBaseTypography provides baseTypography,
        LocalTypography provides typography,
    ) {
        CompositionLocalProvider(
            // Component defaults. Each is overridable on its own for a subtree.
            LocalContentColor provides colors.fgBase,
            LocalTextStyle provides baseTypography.md.merge(
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                letterSpacing = TextUnit.Unspecified,
                textDecoration = TextDecoration.None,
                textAlign = TextAlign.Unspecified
            ),
            LocalIconSize provides IconSize.Small,
            LocalSpinnerStyle provides SpinnerStyle(colors.accentBase),
            LocalSpinnerSize provides SpinnerSize.Default,
            LocalAvatarSize provides AvatarSize.Default,
            LocalAvatarStyle provides AvatarStyle.Default,
            LocalBadgeStyle provides BadgeStyle.Default,
            LocalBadgeSize provides BadgeSize.Default,
            LocalNotificationBadgeSize provides NotificationBadgeSize.Medium,
            LocalNotificationBadgeStyle provides BadgeStyle.Brand,
            LocalButtonSize provides ButtonSize.Medium,
            LocalButtonStyle provides ButtonStyle.solid(ActionColor.Brand),
            LocalCardSize provides CardSize.Default,
            LocalCardStyle provides CardStyle.Outlined,
            LocalCheckboxSize provides CheckboxSize.Default,
            LocalCheckboxStyle provides CheckboxStyle.Default,
            LocalChevronSize provides ChevronSize.Medium,
            LocalProgressBarSize provides ProgressBarSize.Default,
            LocalProgressBarStyle provides ProgressBarStyle.Default,
            LocalStepIndicatorSize provides StepIndicatorSize.Compact,
            LocalStepIndicatorStyle provides StepIndicatorStyle.Default,
            LocalPageIndicatorSize provides PageIndicatorSize.Default,
            LocalPageIndicatorStyle provides PageIndicatorStyle.Default,
            LocalRadioSize provides RadioSize.Default,
            LocalRadioStyle provides RadioStyle.Default,
            LocalDividerSize provides DividerSize.Default,
            LocalDropdownPopupStyle provides DropdownPopupStyle.Default,
            LocalDividerStyle provides DividerStyle.Default,
            LocalIconButtonSize provides IconButtonSize.Medium,
            LocalIconButtonStyle provides IconButtonStyle.solid(ActionColor.Brand),
            LocalIndicatorDotSize provides IndicatorDotSize.Medium,
            LocalListItemSize provides ListItemSize.Default,
            LocalListItemStyle provides ListItemStyle.Default,
            LocalModalSize provides ModalSize.Default,
            LocalNoticeSize provides NoticeSize.Default,
            // Null on purpose: each notice derives its style from its own tone and emphasis.
            LocalNoticeStyle provides null,
            LocalModalStyle provides ModalStyle.Default,
            LocalOtpFieldSize provides OtpFieldSize.Default,
            LocalRequirementListSize provides RequirementListSize.Default,
            LocalRequirementListStyle provides RequirementListStyle.Default,
            LocalHeadingSize provides HeadingSize.Medium,
            LocalHeadingStyle provides HeadingStyle.Default,
            LocalFallbackSize provides FallbackSize.Default,
            LocalFallbackStyle provides FallbackStyle.Default,
            LocalFeaturedIconSize provides FeaturedIconSize.Medium,
            LocalFeaturedIconStyle provides FeaturedIconStyle.Default,
            LocalFieldSize provides FieldSize.Default,
            LocalFieldStyle provides FieldStyle.outlined(),
            LocalFlowModalSize provides FlowModalSize.Default,
            LocalFlowModalStyle provides FlowModalStyle.Default,
            LocalFlowModalActionBarSize provides FlowModalActionBarSize.Default,
            LocalFlowModalActionBarStyle provides FlowModalActionBarStyle.Default,
            LocalSkeletonSize provides SkeletonSize.Default,
            LocalSkeletonStyle provides SkeletonStyle.Default,
            LocalStatusMarkSize provides StatusMarkSize.Default,
            LocalStatusMarkStyle provides StatusMarkStyle.Default,
            LocalStatusSceneSize provides StatusSceneSize.Default,
            LocalStatusSceneStyle provides StatusSceneStyle.Default,
            LocalStatusSceneActionsSize provides StatusSceneActionsSize.Default,
            LocalStatusSceneActionsStyle provides StatusSceneActionsStyle.Default,
            LocalSensitiveTextSize provides SensitiveTextSize.Default,
            LocalSensitiveTextStyle provides SensitiveTextStyle.Default,
            LocalSwatchSize provides SwatchSize.Default,
            LocalSwatchStyle provides SwatchStyle.Default,
            LocalFloatingTabBarSize provides FloatingTabBarSize.Default,
            LocalFloatingTabBarStyle provides FloatingTabBarStyle.Default,
            LocalTabBarSize provides TabBarSize.Default,
            LocalTabBarStyle provides TabBarStyle.Default,
            LocalStatSize provides StatSize.Default,
            LocalStatStyle provides StatStyle.Default,
            LocalTabsSize provides TabsSize.Default,
            LocalTabsStyle provides TabsStyle.Default,
            LocalToastStyle provides ToastStyle.Default,
            LocalToggleSize provides ToggleSize.Default,
            LocalToggleStyle provides ToggleStyle.Default,
            LocalWheelPickerSize provides WheelPickerSize.Default,
            LocalWheelPickerStyle provides WheelPickerStyle.Default,
            LocalTopBarSize provides TopBarSize.Default,
            LocalTopBarStyle provides TopBarStyle.Default,
            LocalContentListSize provides ContentListSize.Default,
            LocalContentListStyle provides ContentListStyle.Default,
            LocalListSize provides ListSize.Default,
            LocalListStyle provides ListStyle.Default,
            LocalPullToRefreshSize provides PullToRefreshSize.Default,
            LocalPullToRefreshStyle provides PullToRefreshStyle.Default,
            LocalTableSize provides TableSize.Default,
            LocalTableStyle provides TableStyle.Default,
            LocalSwatchPickerSize provides SwatchPickerSize.Default,
            LocalSwatchPickerStyle provides SwatchPickerStyle.Default,
            LocalNumberStepperSize provides NumberStepperSize.Default,
            LocalNumberStepperStyle provides NumberStepperStyle.Default,
            LocalNavItemSize provides NavItemSize.Default,
            LocalNavItemStyle provides NavItemStyle.Default,
            LocalNavListSize provides NavListSize.Default,
            LocalDropdownPopupSize provides DropdownPopupSize.Default,
            LocalConfirmationDialogSize provides ConfirmationDialogSize.Default,
            LocalConfirmationDialogStyle provides ConfirmationDialogStyle.Default,
            // Null on purpose: a confirm sheet is built on the flow modal's style and size, and a
            // default built here would read them from *outside* this provider — the light sheet
            // of an application's own override under a nested dark theme, say. Unseeded, each
            // sheet builds its default where it is drawn, from the flow modal defaults in scope.
            LocalConfirmSheetSize provides null,
            LocalConfirmSheetStyle provides null,
            content = content,
        )
    }
}
