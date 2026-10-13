package dev.voir.areca.playground.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.playground.showcase.chrome.ChromeBadge
import dev.voir.areca.playground.showcase.chrome.ChromeBadgeTone
import dev.voir.areca.playground.showcase.chrome.ChromeLevel
import dev.voir.areca.playground.showcase.chrome.ChromeSurface
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.theme.ArecaTheme

/** Widest the page content grows to before it stops filling the window. */
private val ContentMaxWidth = 960.dp

/**
 * Receiver a showcase page's body is written against.
 *
 * Section builders are extension functions on this type, so a page can only emit the section kinds
 * the guide defines. It carries the enclosing [ColumnScope] rather than extending it, to keep
 * `weight` and `align` out of a page author's reach — page rhythm belongs to the scaffold.
 */
@Stable
class ShowcaseScope internal constructor(
    internal val columnScope: ColumnScope,
)

/**
 * The frame every showcase page is built in.
 *
 * Owns scrolling, page width, padding, the header and the spacing between sections. A page never
 * creates its own scroll container or outer padding — see `SHOWCASES.md` §4.
 *
 * @param title Component name, as a reader would search for it.
 * @param summary One sentence saying what the component is for.
 * @param modifier Modifier applied to the page container.
 * @param module Owning module, shown as a badge beside the title.
 * @param status API status, shown as a badge beside the title.
 * @param content Sections making up the page.
 */
@Composable
fun ShowcaseScaffold(
    title: String,
    summary: String,
    modifier: Modifier = Modifier,
    module: String? = null,
    status: ShowcaseStatus = ShowcaseStatus.Stable,
    content: @Composable ShowcaseScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = ContentMaxWidth)
                .fillMaxWidth()
                .padding(
                    horizontal = ArecaTheme.spacing.xl3,
                    vertical = ArecaTheme.spacing.xl4,
                ),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3),
        ) {
            ShowcaseHeader(
                title = title,
                summary = summary,
                module = module,
                status = status,
            )

            val scope = ShowcaseScope(this)
            scope.content()
        }
    }
}

/**
 * API maturity of the component a page documents.
 */
enum class ShowcaseStatus {
    Stable,
    Beta,
    Deprecated,
}

@Composable
private fun ShowcaseHeader(
    title: String,
    summary: String,
    module: String?,
    status: ShowcaseStatus,
) {
    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ChromeText(text = title, role = ChromeTextRole.PageTitle, maxLines = 1)

            if (module != null) {
                ChromeBadge(text = module, tone = ChromeBadgeTone.Neutral)
            }
            if (status != ShowcaseStatus.Stable) {
                ChromeBadge(text = status.name, tone = status.tone)
            }
        }

        ChromeText(text = summary, role = ChromeTextRole.PageSummary)
    }
}

/**
 * A titled region of a showcase page.
 *
 * The typed section builders defined in the guide all render through this, so every section on every
 * page has the same heading treatment, description placement and internal spacing.
 *
 * @param title Section heading.
 * @param modifier Modifier applied to the section container.
 * @param description One sentence saying what the reader should look for here.
 * @param content Section body.
 */
@Composable
fun ShowcaseScope.ShowcaseSectionContainer(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    ChromeSurface(
        modifier = modifier.fillMaxWidth(),
        level = ChromeLevel.Raised,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArecaTheme.spacing.xl2),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs)) {
                ChromeText(text = title, role = ChromeTextRole.SectionTitle)
                if (description != null) {
                    ChromeText(text = description, role = ChromeTextRole.SectionDescription)
                }
            }

            content()
        }
    }
}

private val ShowcaseStatus.tone: ChromeBadgeTone
    get() = when (this) {
        ShowcaseStatus.Stable -> ChromeBadgeTone.Positive
        ShowcaseStatus.Beta -> ChromeBadgeTone.Caution
        ShowcaseStatus.Deprecated -> ChromeBadgeTone.Critical
    }
