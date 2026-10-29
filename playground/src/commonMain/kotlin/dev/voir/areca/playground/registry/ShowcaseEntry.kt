package dev.voir.areca.playground.registry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import dev.voir.areca.playground.showcase.ShowcaseStatus

/**
 * A top-level, collapsible group in the sidebar: the token pages, then one group per owning
 * module. The order of the enum is the order groups appear.
 *
 * @property label What a reader sees as the group heading.
 */
enum class ShowcaseGroup(val label: String) {
    /** Tokens and assets every component draws from: colors, interaction feedback, icons. */
    Foundations("Foundations"),

    /** Components from `:ui:foundation`, split into [ShowcaseSection]s by what they do. */
    Components("Components"),

    /** Charts from `:ui:charts`. */
    Charts("Charts"),

    /** Calendars, date fields and time pickers from `:ui:datetime`. */
    DateTime("Date & time"),

    /** The on-screen keyboard from `:ui:keyboard`. */
    Keyboard("Keyboard"),

    /** The image cropper, its toolbar and its dial from `:ui:crop`. */
    Crop("Crop"),

    /** Pictures from `:ui:media`: frames, zoom, carousels and thumbnails. */
    Media("Media"),

    /** Screens from `:flow:auth`, and the parts they are assembled from. */
    AuthFlow("Auth flow"),

    /** Screens from `:flow:profile`, and the parts they are assembled from. */
    ProfileFlow("Profile flow"),

    /** Screens from `:flow:attachments`, and the parts they are assembled from. */
    AttachmentsFlow("Attachments flow"),

    /** Screens from `:flow:gallery`, and the parts they are assembled from. */
    GalleryFlow("Gallery flow"),
}

/**
 * A heading inside a [ShowcaseGroup]. The order of the enum is the order sections appear within
 * their group.
 *
 * A group either puts every page in a section or none: small groups are listed flat.
 *
 * @property label What a reader sees as the section heading.
 * @property group Group whose pages this section divides.
 */
enum class ShowcaseSection(val label: String, val group: ShowcaseGroup) {
    /** Controls that trigger an action. */
    Actions("Actions", ShowcaseGroup.Components),

    /** Fields and the rows and groups that hold them. */
    Inputs("Inputs", ShowcaseGroup.Components),

    /** Controls that pick a value from a fixed set. */
    Selection("Selection", ShowcaseGroup.Components),

    /** Components that present content without being interactive themselves. */
    Display("Display", ShowcaseGroup.Components),

    /** Lists, rows, grids and tables. */
    ListsAndData("Lists & data", ShowcaseGroup.Components),

    /** Loading, progress, errors and transient messages. */
    Feedback("Feedback", ShowcaseGroup.Components),

    /** Dialogs and modals drawn above the page. */
    Overlays("Overlays", ShowcaseGroup.Components),

    /** Bars, tabs and lists that move between screens. */
    Navigation("Navigation", ShowcaseGroup.Components),

    /** Screen structure and containers that arrange other content. */
    Layout("Layout", ShowcaseGroup.Components),

    /** Whole authentication screens, each a recipe a consumer copies. */
    AuthScreens("Screens", ShowcaseGroup.AuthFlow),

    /** The scaffold, heading and banner an authentication screen is framed by. */
    AuthLayout("Layout", ShowcaseGroup.AuthFlow),

    /** Credential fields and the consent checkbox. */
    AuthFields("Fields", ShowcaseGroup.AuthFlow),

    /** Inline links and the footer prompt that switch between screens. */
    AuthLinks("Links", ShowcaseGroup.AuthFlow),

    /** Single sign-on provider buttons. */
    AuthSso("SSO", ShowcaseGroup.AuthFlow),

    /** Whole profile and account screens, each a recipe a consumer copies. */
    ProfileScreens("Screens", ShowcaseGroup.ProfileFlow),

    /** Avatars, the greeting, and the cards that open a profile screen. */
    ProfileCards("Cards & avatar", ShowcaseGroup.ProfileFlow),

    /** Settings rows for profile details, sign-in methods and account exits. */
    ProfileRows("Rows", ShowcaseGroup.ProfileFlow),

    /** Active sessions and the rows that list them. */
    ProfileSessions("Sessions", ShowcaseGroup.ProfileFlow),

    /** Bottom sheets that pick an avatar source or confirm a destructive action. */
    ProfileSheets("Sheets", ShowcaseGroup.ProfileFlow),

    /** Upload, stale-data and load-failure states. */
    ProfileStates("States", ShowcaseGroup.ProfileFlow),

    /** Whole screens with attachments, each a recipe a consumer copies. */
    AttachmentsScreens("Screens", ShowcaseGroup.AttachmentsFlow),

    /** A file's square, the add square, and the strip that holds them. */
    AttachmentsTiles("Tiles & strip", ShowcaseGroup.AttachmentsFlow),

    /** A file as a list row, and the paperclip that marks something with files on it. */
    AttachmentsRows("Rows & marks", ShowcaseGroup.AttachmentsFlow),

    /** The sheets that add a file, ask what it is, act on it and rename it, and their pieces. */
    AttachmentsSheets("Sheets", ShowcaseGroup.AttachmentsFlow),

    /** The full-screen viewer, the page it draws each file on, and the layout of a file it cannot draw. */
    AttachmentsViewer("Viewer", ShowcaseGroup.AttachmentsFlow),

    /** Whole gallery screens. */
    GalleryScreens("Screens", ShowcaseGroup.GalleryFlow),

    /** The bars, words and grid pieces they are built from. */
    GalleryParts("Parts", ShowcaseGroup.GalleryFlow),
}

/**
 * One page in the playground.
 *
 * The single place a showcase is declared. The sidebar, the search box and the navigation graph all
 * read from the registry, so adding a page is one edit.
 *
 * @param id Stable route, as `module/component`, where `module` is the owning module (`foundation`,
 *   `charts`, `flow-auth`, …). It does not encode the group, so regrouping a page never moves it.
 * @param group Sidebar group this page belongs to.
 * @param title Component name, as a reader would search for it. Inside a flow group, without the
 *   flow's own name: the group heading already says it.
 * @param summary One sentence saying what the component is for.
 * @param section Heading the page sits under inside its [group], or `null` in a group listed flat.
 * @param status API maturity of the component itself, shown as a badge on the page.
 * @param keywords Extra search terms — the words someone would type who does not know the
 *   component's name.
 * @param content The page.
 * @throws IllegalArgumentException When [section] divides a group other than [group].
 */
@Immutable
class ShowcaseEntry(
    val id: String,
    val group: ShowcaseGroup,
    val title: String,
    val summary: String,
    val section: ShowcaseSection? = null,
    val status: ShowcaseStatus = ShowcaseStatus.Stable,
    val keywords: List<String> = emptyList(),
    val content: @Composable () -> Unit,
) {
    init {
        require(section == null || section.group == group) {
            "Showcase $id is in section ${section?.name}, which divides ${section?.group}, not $group."
        }
    }

    /**
     * Whether this entry should appear for [query].
     *
     * Matches the title, the group, the section and the keywords but deliberately not the summary:
     * summaries are prose and matching them turns a specific search into a long list.
     *
     * @param query Raw text from the sidebar filter.
     * @return `true` when [query] is blank or matches any searchable field.
     */
    fun matches(query: String): Boolean {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return true

        return title.contains(trimmed, ignoreCase = true) ||
            group.label.contains(trimmed, ignoreCase = true) ||
            section?.label?.contains(trimmed, ignoreCase = true) == true ||
            keywords.any { it.contains(trimmed, ignoreCase = true) }
    }
}
