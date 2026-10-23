package dev.voir.areca.playground.registry

import dev.voir.areca.playground.foundation.avatar.AvatarShowcase
import dev.voir.areca.playground.foundation.badge.BadgeShowcase
import dev.voir.areca.playground.foundation.button.ButtonShowcase
import dev.voir.areca.playground.foundation.button.IconButtonShowcase
import dev.voir.areca.playground.foundation.collection.ContentListShowcase
import dev.voir.areca.playground.foundation.collection.GroupedGridShowcase
import dev.voir.areca.playground.foundation.feedback.FallbackShowcase
import dev.voir.areca.playground.foundation.feedback.ProgressShowcase
import dev.voir.areca.playground.foundation.feedback.RequirementListShowcase
import dev.voir.areca.playground.foundation.feedback.SkeletonShowcase
import dev.voir.areca.playground.foundation.feedback.ToastShowcase
import dev.voir.areca.playground.foundation.field.FieldGroupShowcase
import dev.voir.areca.playground.foundation.field.FieldRowShowcase
import dev.voir.areca.playground.foundation.field.OtpFieldShowcase
import dev.voir.areca.playground.foundation.field.SelectFieldShowcase
import dev.voir.areca.playground.foundation.field.SelectFlowModalShowcase
import dev.voir.areca.playground.foundation.field.TextFieldShowcase
import dev.voir.areca.playground.foundation.icon.ChevronShowcase
import dev.voir.areca.playground.foundation.icon.FeaturedIconShowcase
import dev.voir.areca.playground.foundation.layout.BaseLayoutShowcase
import dev.voir.areca.playground.foundation.layout.CardShowcase
import dev.voir.areca.playground.foundation.layout.CollapsingHeaderLayoutShowcase
import dev.voir.areca.playground.foundation.layout.DividerShowcase
import dev.voir.areca.playground.foundation.layout.ExpandableShowcase
import dev.voir.areca.playground.foundation.layout.PullToRefreshShowcase
import dev.voir.areca.playground.foundation.list.ListItemShowcase
import dev.voir.areca.playground.foundation.list.ListShowcase
import dev.voir.areca.playground.foundation.navigation.NavListShowcase
import dev.voir.areca.playground.foundation.notice.NoticeShowcase
import dev.voir.areca.playground.foundation.overlay.ConfirmSheetShowcase
import dev.voir.areca.playground.foundation.overlay.ConfirmationDialogShowcase
import dev.voir.areca.playground.foundation.overlay.DialogShowcase
import dev.voir.areca.playground.foundation.overlay.FlowModalShowcase
import dev.voir.areca.playground.foundation.selection.CheckboxShowcase
import dev.voir.areca.playground.foundation.selection.NumberStepperShowcase
import dev.voir.areca.playground.foundation.selection.RadioShowcase
import dev.voir.areca.playground.foundation.selection.SwatchShowcase
import dev.voir.areca.playground.foundation.selection.ToggleShowcase
import dev.voir.areca.playground.foundation.settings.SettingsShowcase
import dev.voir.areca.playground.foundation.slot.ThreeSlotRowShowcase
import dev.voir.areca.playground.foundation.stat.StatShowcase
import dev.voir.areca.playground.foundation.status.StatusMarkShowcase
import dev.voir.areca.playground.foundation.status.StatusSceneShowcase
import dev.voir.areca.playground.foundation.tabbar.FloatingTabBarShowcase
import dev.voir.areca.playground.foundation.tabbar.TabBarShowcase
import dev.voir.areca.playground.foundation.table.TableShowcase
import dev.voir.areca.playground.foundation.tabs.TabsShowcase
import dev.voir.areca.playground.foundation.text.SensitiveTextShowcase
import dev.voir.areca.playground.foundation.text.TextLinkShowcase
import dev.voir.areca.playground.foundation.text.TextShowcase
import dev.voir.areca.playground.foundation.topbar.LargeTopBarShowcase
import dev.voir.areca.playground.foundation.topbar.TopBarActionsShowcase
import dev.voir.areca.playground.foundation.topbar.TopBarShowcase
import dev.voir.areca.playground.foundation.wheel.WheelPickerShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus

/**
 * Component pages for `:ui:foundation`, in the [ShowcaseGroup.Components] group.
 *
 * Source order is for reading only: the sidebar sorts each section by title.
 */
internal val ComponentShowcases: List<ShowcaseEntry> = listOf(
    ShowcaseEntry(
        id = "foundation/button",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Actions,
        title = "Button",
        summary = "Primary action control with variant, color, size and loading state.",
        keywords = listOf("cta", "action", "submit", "press"),
        content = { ButtonShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/icon-button",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Actions,
        title = "Icon button",
        summary = "A button whose whole content is an icon, for actions that need no label.",
        keywords = listOf("icon", "action", "toolbar", "close", "more"),
        content = { IconButtonShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/text-link",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Actions,
        title = "Text link",
        summary = "Navigates or acts from within running text, standalone or inline.",
        keywords = listOf("anchor", "href", "hyperlink", "inline", "prompt"),
        content = { TextLinkShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/field-group",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Inputs,
        title = "Field group",
        summary = "Wraps a control with its label, required marker and validation message.",
        keywords = listOf("form", "label", "validation", "error", "helper"),
        content = { FieldGroupShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/field-row",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Inputs,
        title = "Field row",
        summary = "Several controls inside one field, sharing its border, focus and status.",
        keywords = listOf(
            "composite", "multipart", "segment", "currency", "unit", "prefix", "combo",
        ),
        content = { FieldRowShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/number-stepper",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Inputs,
        title = "Number stepper",
        summary = "Increment and decrement a bounded integer without a keyboard.",
        keywords = listOf("quantity", "counter", "increment", "spinner", "plus minus"),
        content = { NumberStepperShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/select-field",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Inputs,
        title = "Select field",
        summary = "Field that opens a dropdown of options, choosing one value or several.",
        keywords = listOf("dropdown", "picker", "combobox", "options", "form"),
        content = { SelectFieldShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/select-flow-modal",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Inputs,
        title = "Select flow modal",
        summary = "Full-surface option picker with search, for lists too long for a dropdown.",
        keywords = listOf("dropdown", "picker", "sheet", "multi select", "search"),
        content = { SelectFlowModalShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/text-field",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Inputs,
        title = "Text field",
        summary = "Single-line and multi-line text input, with affix, password and search variants.",
        keywords = listOf("input", "form", "entry", "password", "search", "affix"),
        content = { TextFieldShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/otp-field",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Inputs,
        title = "OTP field",
        summary = "A one-time code typed one character to a cell, over a single editor that still " +
            "pastes, autofills and reads as one field.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "otp", "one-time code", "verification code", "pin", "code input", "2fa", "passcode",
            "segmented", "OtpFieldSize",
        ),
        content = { OtpFieldShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/checkbox",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Selection,
        title = "Checkbox",
        summary = "Multi-select control with checked, unchecked and indeterminate marks.",
        keywords = listOf("tick", "check", "select", "form"),
        content = { CheckboxShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/radio",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Selection,
        title = "Radio",
        summary = "Single-select control: choosing one option clears the others in its group.",
        keywords = listOf("option", "choice", "select", "form"),
        content = { RadioShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/swatch",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Selection,
        title = "Swatch",
        summary = "A single colour chip, and the grouped picker built from them.",
        keywords = listOf("color", "colour", "palette", "picker", "chip"),
        content = { SwatchShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/toggle",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Selection,
        title = "Toggle",
        summary = "Switch that cycles through on, off and optionally an indeterminate middle state.",
        keywords = listOf("switch", "on off", "tri-state", "setting"),
        content = { ToggleShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/wheel-picker",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Selection,
        title = "Wheel picker",
        summary = "Columns of snapping wheels, driven by drag, throw, scroll wheel or keyboard.",
        keywords = listOf("wheel", "picker", "spinner", "drum", "scroll", "snap", "column"),
        content = { WheelPickerShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/stat",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Display,
        title = "Stat",
        summary = "A figure with what it is and what it means, alone or in a grid of figures.",
        keywords = listOf("figure", "metric", "kpi", "number", "summary", "grid", "statistic"),
        content = { StatShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/avatar",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Display,
        title = "Avatar",
        summary = "Identity marker showing initials, an image slot or a loading placeholder.",
        keywords = listOf("profile", "user", "initials", "picture", "identity"),
        content = { AvatarShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/badge",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Display,
        title = "Badge",
        summary = "Compact status marker, optionally clickable and carrying icons.",
        keywords = listOf("chip", "pill", "tag", "status", "count", "notification"),
        content = { BadgeShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/card",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Display,
        title = "Card",
        summary = "Grouping surface that can be static, clickable, selectable or expandable.",
        keywords = listOf("surface", "panel", "container", "tile", "expandable"),
        content = { CardShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/chevron",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Display,
        title = "Chevron",
        summary = "Directional affordance that animates when its direction changes.",
        keywords = listOf("arrow", "caret", "disclosure", "direction", "expand"),
        content = { ChevronShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/divider",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Display,
        title = "Divider",
        summary = "Separates content, optionally carrying a label and a dashed line.",
        keywords = listOf("separator", "rule", "line", "hr"),
        content = { DividerShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/featured-icon",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Display,
        title = "Featured icon",
        summary = "Emphasised icon container for empty states, dialogs and status callouts.",
        keywords = listOf("icon", "avatar", "illustration", "empty state"),
        content = { FeaturedIconShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/sensitive-text",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Display,
        title = "Sensitive text",
        summary = "Value laid out at its real size and hidden under a veil until it is revealed.",
        keywords = listOf("redact", "mask", "blur", "privacy", "hidden", "password"),
        content = { SensitiveTextShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/text",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Display,
        title = "Text",
        summary = "Typography primitives: named text sizes and colors, and the composed heading block.",
        keywords = listOf("typography", "type", "heading", "label", "caption", "copy"),
        content = { TextShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/content-list",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.ListsAndData,
        title = "Content list",
        summary = "List wrapper that resolves loading, empty, error and loaded into one component.",
        keywords = listOf("state", "empty", "error", "loading", "pagination"),
        content = { ContentListShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/grouped-grid",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.ListsAndData,
        title = "Grouped grid",
        summary = "Lazy grid whose items are grouped under headers, scrolling vertically or horizontally.",
        keywords = listOf("grid", "group", "section", "lane", "carousel", "lazy"),
        content = { GroupedGridShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/list",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.ListsAndData,
        title = "List",
        summary = "Rows and dividers declared as data, laid out eagerly or lazily.",
        keywords = listOf("list", "rows", "lazy", "menu", "entries"),
        content = { ListShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/list-item",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.ListsAndData,
        title = "List item",
        summary = "A row with leading, content and trailing slots, optionally clickable.",
        keywords = listOf("row", "cell", "menu", "settings"),
        content = { ListItemShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/settings",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.ListsAndData,
        title = "Settings",
        summary = "Grouped settings screens: rows that state, switch, pick, edit and act.",
        keywords = listOf("preferences", "list", "section", "row", "options", "toggle", "select", "account"),
        content = { SettingsShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/table",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.ListsAndData,
        title = "Table",
        summary = "Column-driven data table, eagerly laid out or lazily scrolled.",
        keywords = listOf("table", "grid", "rows", "columns", "data", "spreadsheet"),
        content = { TableShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/fallback",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Feedback,
        title = "Fallback",
        summary = "Centred block for the moments a screen has nothing to show.",
        keywords = listOf("empty", "error", "placeholder", "offline", "retry", "zero state"),
        content = { FallbackShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/progress",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Feedback,
        title = "Progress",
        summary = "Indeterminate spinner, determinate bar and the small state dot.",
        keywords = listOf("spinner", "loading", "progress", "bar", "dot", "status", "busy"),
        content = { ProgressShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/notice",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Feedback,
        title = "Notice",
        summary = "Block that says what happened or what is true, as a titled banner or a note.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "banner", "alert", "callout", "message", "note", "inline error", "warning", "info",
            "success", "caveat",
        ),
        content = { NoticeShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/status-mark",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Feedback,
        title = "Status mark",
        summary = "Disc that says work is running, then lands as a tick or holds still when it stopped.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("progress", "working", "success", "done", "tick", "failed", "halo", "result"),
        content = { StatusMarkShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/status-scene",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Feedback,
        title = "Status scene",
        summary = "Screen given over to one piece of work: that it is running, or how it ended.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "result screen", "success screen", "import", "backup", "complete", "failed", "stopped",
            "progress", "done",
        ),
        content = { StatusSceneShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/skeleton",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Feedback,
        title = "Skeleton",
        summary = "Shimmering placeholder standing in for content that has not loaded.",
        keywords = listOf("loading", "placeholder", "shimmer", "ghost"),
        content = { SkeletonShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/requirement-list",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Feedback,
        title = "Requirement list",
        summary = "The rules a value has to meet, each marked as pending, met or unmet as it is typed.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "password rules", "password requirements", "checklist", "validation", "criteria",
            "constraints", "Requirement", "RequirementState",
        ),
        content = { RequirementListShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/toast",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Feedback,
        title = "Toast",
        summary = "Transient message queued through a host and dismissed on a timer.",
        keywords = listOf("snackbar", "notification", "message", "transient", "alert"),
        content = { ToastShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/confirmation-dialog",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Overlays,
        title = "Confirmation dialog",
        summary = "Blocking prompt for an action that cannot easily be undone.",
        keywords = listOf("alert", "modal", "confirm", "destructive", "prompt"),
        content = { ConfirmationDialogShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/confirm-sheet",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Overlays,
        title = "Confirm sheet",
        summary = "Bottom sheet that asks once before an action that is hard to take back.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "confirm", "confirmation", "bottom sheet", "destructive", "delete", "discard",
            "sign out", "are you sure", "modal",
        ),
        content = { ConfirmSheetShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/dialog",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Overlays,
        title = "Dialog",
        summary = "Centred modal surface with header, content and footer regions.",
        keywords = listOf("modal", "popup", "overlay", "alert", "centred"),
        content = { DialogShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/flow-modal",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Overlays,
        title = "Flow modal",
        summary = "Bottom sheet that moves between hidden, partial and expanded heights.",
        keywords = listOf("modal", "sheet", "dialog", "bottom sheet", "drag"),
        content = { FlowModalShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/floating-tab-bar",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Navigation,
        title = "Floating tab bar",
        summary = "Floating navigation bar whose indicator animates between destinations.",
        keywords = listOf("bottom bar", "tab", "navigation", "indicator"),
        content = { FloatingTabBarShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/large-top-bar",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Navigation,
        title = "Large top bar",
        summary = "A heading bar that answers the content scrolling under it: separator, " +
            "travel, or a headline that collapses into itself.",
        keywords = listOf("appbar", "large title", "collapse", "scroll", "header"),
        content = { LargeTopBarShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/nav-list",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Navigation,
        title = "Nav list",
        summary = "Sidebar navigation: a collapsible tree of sections built from selectable rows.",
        keywords = listOf("sidebar", "menu", "tree", "navigation", "nav list", "nav item"),
        content = { NavListShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/tab-bar",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Navigation,
        title = "Tab bar",
        summary = "Docked navigation bar that spreads its destinations across the full width.",
        keywords = listOf("bottom bar", "tab", "navigation", "docked"),
        content = { TabBarShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/tabs",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Navigation,
        title = "Tabs",
        summary = "Switches between sibling views, horizontally or vertically.",
        keywords = listOf("tab", "segment", "switcher", "navigation"),
        content = { TabsShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/top-bar",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Navigation,
        title = "Top bar",
        summary = "Screen header whose controls, title blocks and bottom band all take their " +
            "measurements from one place.",
        keywords = listOf(
            "appbar",
            "header",
            "toolbar",
            "navigation",
            "action",
            "back",
            "breadcrumb",
        ),
        content = { TopBarShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/top-bar-actions",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Navigation,
        title = "Top bar actions",
        summary = "One appearance per bar, a named control for every common verb, and a preset " +
            "for every place a bar is drawn.",
        keywords = listOf(
            "appbar",
            "toolbar",
            "action",
            "icon button",
            "circle",
            "floating",
            "scrim",
            "sheet header",
            "done",
            "cancel",
        ),
        content = { TopBarActionsShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/base-layout",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Layout,
        title = "Base layout",
        summary = "Screen frame that measures top and bottom bars, then pads content around them.",
        keywords = listOf("scaffold", "screen", "frame", "structure"),
        content = { BaseLayoutShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/collapsing-header",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Layout,
        title = "Collapsing header",
        summary = "Header that scrolls away while a pinned region and the content stay in place.",
        keywords = listOf("scroll", "appbar", "parallax", "sticky"),
        content = { CollapsingHeaderLayoutShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/expandable",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Layout,
        title = "Expandable",
        summary = "Reveals and hides content under a trigger the caller draws.",
        keywords = listOf("accordion", "collapse", "disclosure", "faq", "reveal"),
        content = { ExpandableShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/pull-to-refresh",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Layout,
        title = "Pull to refresh",
        summary = "Wraps a scrollable so an overscroll at the top triggers a refresh.",
        keywords = listOf("refresh", "reload", "swipe", "overscroll", "gesture"),
        content = { PullToRefreshShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/three-slot-row",
        group = ShowcaseGroup.Components,
        section = ShowcaseSection.Layout,
        title = "Three slot row",
        summary = "Row primitive that divides its width between start, center and end slots.",
        keywords = listOf("slot", "row", "layout", "alignment", "toolbar"),
        content = { ThreeSlotRowShowcase() },
    ),
)
