package dev.voir.areca.playground.registry

import dev.voir.areca.playground.flow.profile.AccountRowShowcase
import dev.voir.areca.playground.flow.profile.AccountShowcase
import dev.voir.areca.playground.flow.profile.ActiveSessionsSectionsShowcase
import dev.voir.areca.playground.flow.profile.AvatarSourceSheetShowcase
import dev.voir.areca.playground.flow.profile.AvatarUploadStatusShowcase
import dev.voir.areca.playground.flow.profile.BirthdayRowShowcase
import dev.voir.areca.playground.flow.profile.DataExportRowShowcase
import dev.voir.areca.playground.flow.profile.DeleteAccountRowShowcase
import dev.voir.areca.playground.flow.profile.DeleteAccountSheetShowcase
import dev.voir.areca.playground.flow.profile.DevicesShowcase
import dev.voir.areca.playground.flow.profile.ChangePhotoShowcase
import dev.voir.areca.playground.flow.profile.EditProfileShowcase
import dev.voir.areca.playground.flow.profile.EditableAvatarShowcase
import dev.voir.areca.playground.flow.profile.EmailRowShowcase
import dev.voir.areca.playground.flow.profile.LinkedAccountRowShowcase
import dev.voir.areca.playground.flow.profile.ProfileAvatarShowcase
import dev.voir.areca.playground.flow.profile.ProfileCoverCardShowcase
import dev.voir.areca.playground.flow.profile.ProfileGreetingShowcase
import dev.voir.areca.playground.flow.profile.ProfileHeaderCardShowcase
import dev.voir.areca.playground.flow.profile.ProfileInfoRowShowcase
import dev.voir.areca.playground.flow.profile.ProfileLoadFailureShowcase
import dev.voir.areca.playground.flow.profile.ProfileShowcase
import dev.voir.areca.playground.flow.profile.ProfileSummaryCardShowcase
import dev.voir.areca.playground.flow.profile.RevokeSessionSheetShowcase
import dev.voir.areca.playground.flow.profile.SessionRowShowcase
import dev.voir.areca.playground.flow.profile.SignOutOthersSheetShowcase
import dev.voir.areca.playground.flow.profile.SignOutRowShowcase
import dev.voir.areca.playground.flow.profile.SignOutSheetShowcase
import dev.voir.areca.playground.flow.profile.StaleDetailsNoticeShowcase
import dev.voir.areca.playground.flow.profile.UsernameCheckFooterShowcase
import dev.voir.areca.playground.flow.profile.UsernameShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus

/**
 * Screen and part pages for `:flow:profile`, in the [ShowcaseGroup.ProfileFlow] group.
 *
 * Source order is for reading only: the sidebar sorts each section by title.
 */
internal val ProfileFlowShowcases: List<ShowcaseEntry> = listOf(
    ShowcaseEntry(
        id = "flow-profile/account",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileScreens,
        title = "Account",
        summary = "An account menu: sign-in methods, data export, the account switcher and the ways out.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "account", "menu", "sign out", "logout", "linked accounts", "export", "gdpr",
            "switch account", "delete account", "screen",
        ),
        content = { AccountShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/devices",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileScreens,
        title = "Devices",
        summary = "Where the account is signed in: this device, every other one, and signing them out.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("devices", "sessions", "sign out", "terminate", "security", "screen"),
        content = { DevicesShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/change-photo",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileScreens,
        title = "Change photo",
        summary = "Picking, cropping and uploading a new profile picture, as one sequence.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("avatar", "photo", "crop", "image cropper", "upload", "profile picture", "screen", "recipe"),
        content = { ChangePhotoShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/edit-profile",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileScreens,
        title = "Edit profile",
        summary = "The screen that edits a profile: picture, name, bio, facts and account deletion.",
        status = ShowcaseStatus.Beta,
        keywords = listOf(
            "profile", "edit", "form", "name", "bio", "avatar", "photo", "upload", "birthday",
            "delete account", "screen",
        ),
        content = { EditProfileShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/profile",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileScreens,
        title = "Profile",
        summary = "Who the account is: a profile card, the facts about it, and a way to its devices.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("profile", "account", "avatar", "card", "cover", "info", "screen"),
        content = { ProfileShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/username",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileScreens,
        title = "Username",
        summary = "Choosing a username: a field and the service's verdict on it, under the field.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("username", "handle", "availability", "taken", "screen"),
        content = { UsernameShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/avatar",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileCards,
        title = "Avatar",
        summary = "The account's picture, or the initials that stand in for it while it loads or when " +
            "it cannot be drawn.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("profile avatar", "avatar", "picture", "photo", "initials", "image loader", "account"),
        content = { ProfileAvatarShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/cover-card",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileCards,
        title = "Cover card",
        summary = "The card for a profile with more to say than a name: a cover band, the picture " +
            "overlapping it, then name, handle, bio and caption.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("profile cover card", "cover", "banner", "header", "bio", "profile card"),
        content = { ProfileCoverCardShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/editable-avatar",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileCards,
        title = "Editable avatar",
        summary = "The picture at the top of an edit screen, with the camera mark that says it can be " +
            "changed and the handle under it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("change photo", "camera", "avatar", "edit profile", "picture"),
        content = { EditableAvatarShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/greeting",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileCards,
        title = "Greeting",
        summary = "Who is signed in, across the top of a screen that is not about the account.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("profile greeting", "welcome", "hello", "home header", "signed in", "placeholder"),
        content = { ProfileGreetingShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/header-card",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileCards,
        title = "Header card",
        summary = "The card a profile screen opens with: picture, name, handle, caption and a row of " +
            "action tiles.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("profile header card", "profile", "card", "header", "avatar", "actions", "ProfileCardAction"),
        content = { ProfileHeaderCardShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/summary-card",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileCards,
        title = "Summary card",
        summary = "The compact card that leads an account menu or a settings screen into the account.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("profile summary card", "account", "account menu", "settings header", "caption", "placeholder", "skeleton"),
        content = { ProfileSummaryCardShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/account-row",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileRows,
        title = "Account row",
        summary = "Lists the accounts signed in on this device so the person can switch between them or " +
            "add another.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("account switcher", "switch account", "add account", "multiple accounts"),
        content = { AccountRowShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/birthday-row",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileRows,
        title = "Birthday row",
        summary = "States the account's date of birth and opens a calendar sheet to choose or remove it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("date of birth", "dob", "calendar", "date picker", "your info", "settings row"),
        content = { BirthdayRowShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/data-export-row",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileRows,
        title = "Data export row",
        summary = "Lets the person ask for a copy of their account's data and collect it when it is ready.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("download", "export", "gdpr", "your data", "copy", "takeout", "settings row"),
        content = { DataExportRowShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/delete-account-row",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileRows,
        title = "Delete account row",
        summary = "Asks to delete the account, saying what it costs, from a section of its own at the " +
            "end of the screen.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("delete", "remove account", "close account", "danger zone", "destructive"),
        content = { DeleteAccountRowShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/email-row",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileRows,
        title = "Email row",
        summary = "States the address the account signs in with and, until it is confirmed, offers to " +
            "send a confirmation.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("email", "address", "verify", "unverified", "confirm", "settings row"),
        content = { EmailRowShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/info-row",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileRows,
        title = "Info row",
        summary = "States one fact about the account — a number, a handle — with what kind of fact it " +
            "is underneath, and optionally opens whatever changes it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("profile info row", "fact", "phone", "mobile", "username", "handle", "your info", "settings row"),
        content = { ProfileInfoRowShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/linked-account-row",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileRows,
        title = "Linked account row",
        summary = "Shows whether the account can sign in through another provider and links or unlinks it.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("sso", "oauth", "provider", "google", "link", "unlink", "sign-in methods"),
        content = { LinkedAccountRowShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/sign-out-row",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileRows,
        title = "Sign out row",
        summary = "Signs the person out of this device from a section of its own at the foot of the " +
            "account menu.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("log out", "logout", "sign out", "danger", "account menu", "settings row"),
        content = { SignOutRowShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/username-check-footer",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileRows,
        title = "Username check footer",
        summary = "Says under a username field whether the name can be had, as the service answers.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("username", "handle", "availability", "taken", "available", "section footer"),
        content = { UsernameCheckFooterShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/active-sessions",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileSessions,
        title = "Active sessions",
        summary = "The two sections a devices screen is built from: this device with the way to sign " +
            "out every other, and the other devices, each pressed to sign it out.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("sessions", "devices", "signed in", "sign out others", "terminate", "security"),
        content = { ActiveSessionsSectionsShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/session-row",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileSessions,
        title = "Session row",
        summary = "One device the account is signed in on, pressed to sign that device out.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("session", "device", "signed in", "revoke", "sign out", "active sessions"),
        content = { SessionRowShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/avatar-source-sheet",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileSheets,
        title = "Avatar source sheet",
        summary = "The sheet that asks where a new profile picture comes from, or removes the current one.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("photo", "picture", "camera", "library", "gallery", "remove photo", "upload"),
        content = { AvatarSourceSheetShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/delete-account-sheet",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileSheets,
        title = "Delete account sheet",
        summary = "Asks before deleting the account, optionally behind a phrase the person has to type.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("delete", "remove account", "close account", "confirmation phrase", "danger"),
        content = { DeleteAccountSheetShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/revoke-session-sheet",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileSheets,
        title = "Revoke session sheet",
        summary = "Asks once before signing out one other device, naming it so the right one goes.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("sign out device sheet", "revoke", "session", "device", "sign out", "terminate", "confirm"),
        content = { RevokeSessionSheetShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/sign-out-others-sheet",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileSheets,
        title = "Sign out others sheet",
        summary = "Asks once before ending every session but this one, and reassures that this device " +
            "stays signed in.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("sign out other devices sheet", "sign out all", "other devices", "terminate sessions", "log out", "confirm"),
        content = { SignOutOthersSheetShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/sign-out-sheet",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileSheets,
        title = "Sign out sheet",
        summary = "Asks once before signing out of this device, and says what that costs when anything " +
            "is at stake.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("sign-out sheet", "sign out", "log out", "logout", "confirm", "dialog"),
        content = { SignOutSheetShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/avatar-upload-status",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileStates,
        title = "Avatar upload status",
        summary = "The band under an edit screen's avatar that reports a change of picture while it " +
            "uploads, and offers to retry when it fails.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("upload", "progress", "photo", "retry", "error"),
        content = { AvatarUploadStatusShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/load-failure",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileStates,
        title = "Load failure",
        summary = "The screen when the profile could not be loaded at all and there is nothing to show " +
            "in its place.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("profile load failure", "error", "failed", "retry", "empty state", "offline"),
        content = { ProfileLoadFailureShowcase() },
    ),
    ShowcaseEntry(
        id = "flow-profile/stale-details-notice",
        group = ShowcaseGroup.ProfileFlow,
        section = ShowcaseSection.ProfileStates,
        title = "Stale details notice",
        summary = "The band heading a profile that loaded from memory, saying the details below were " +
            "not just fetched.",
        status = ShowcaseStatus.Beta,
        keywords = listOf("offline", "cached", "saved", "outdated", "banner"),
        content = { StaleDetailsNoticeShowcase() },
    ),
)
