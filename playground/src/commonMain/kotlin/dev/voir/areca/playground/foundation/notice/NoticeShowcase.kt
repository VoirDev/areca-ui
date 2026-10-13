package dev.voir.areca.playground.foundation.notice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.notice.AnimatedNotice
import dev.voir.areca.foundation.notice.Notice
import dev.voir.areca.foundation.notice.NoticeEmphasis
import dev.voir.areca.foundation.notice.NoticeIconPlacement
import dev.voir.areca.foundation.notice.NoticeSize
import dev.voir.areca.foundation.notice.NoticeStyle
import dev.voir.areca.foundation.notice.NoticeText
import dev.voir.areca.foundation.notice.NoticeTone
import dev.voir.areca.icons.AlertCircle
import dev.voir.areca.icons.AlertTriangle
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.CheckCircle
import dev.voir.areca.icons.CloudOff
import dev.voir.areca.icons.FileX
import dev.voir.areca.icons.InfoCircle
import dev.voir.areca.icons.Lightbulb
import dev.voir.areca.icons.RefreshCcw
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Width the constrained-container edge case squeezes a notice into. */
private val NarrowWidth = 220.dp

/**
 * Showcase for [Notice], [NoticeText] and [AnimatedNotice].
 *
 * The emphasis and placement controls offer "Auto" as well as each value, because both props
 * default to something that depends on another — emphasis on the tone, placement on the title — and
 * the default is the behaviour most worth seeing.
 */
@Composable
fun NoticeShowcase() = ShowcaseScaffold(
    title = "Notice",
    summary = "Block set off from the page to say what happened or what is true, as a titled " +
        "banner or a one-line note.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Turn the title off to see the banner become a note.") {
        val titled by booleanControl(
            key = "titled",
            default = true,
            description = "Off passes title = null: the mark moves beside the copy as a bare glyph.",
        )
        val title by textControl("title", default = "Some rows were skipped")
        val message by textControl(
            key = "message",
            default = "12 rows had no date and were left out of the import.",
        )
        val tone by enumControl("tone", default = NoticeTone.Warning, options = NoticeTone.entries)
        val emphasis by enumControl(
            key = "emphasis",
            default = null,
            options = listOf(null, NoticeEmphasis.Subtle, NoticeEmphasis.Filled),
            optionLabel = { it?.name ?: "Auto" },
            description = "Auto is Filled for Danger and Subtle for every other tone.",
        )
        val withIcon by booleanControl("icon", default = true)
        val busy by booleanControl(
            key = "busy",
            default = false,
            description = "A spinner in the mark's footprint, so nothing moves when it ends.",
        )
        val placement by enumControl(
            key = "iconPlacement",
            default = null,
            options = listOf(null, NoticeIconPlacement.Top, NoticeIconPlacement.Start),
            optionLabel = { it?.name ?: "Auto" },
            description = "NoticeSize.iconPlacement. Auto is Top with a title and Start without.",
        )
        val quiet by booleanControl(
            key = "quiet",
            default = true,
            label = "quiet line",
            description = "A last NoticeText(quiet = true): advice rather than the fact.",
        )
        val visible by booleanControl(
            key = "visible",
            default = true,
            description = "Wrapped in AnimatedNotice: toggle to watch it fold in and out.",
        )

        preview {
            val resolvedEmphasis = emphasis ?: defaultEmphasis(tone)

            AnimatedNotice(value = if (visible) message else null, modifier = Modifier.fillMaxWidth()) { shown ->
                Notice(
                    title = if (titled) title else null,
                    icon = if (withIcon) tone.sampleIcon() else null,
                    tone = tone,
                    emphasis = resolvedEmphasis,
                    busy = busy,
                    style = NoticeStyle.of(tone, resolvedEmphasis),
                    size = NoticeSize.default(iconPlacement = placement),
                ) {
                    NoticeText(shown)
                    if (quiet) NoticeText("Add the dates in the file and import it again.", quiet = true)
                }
            }
        }
    }

    Anatomy(description = "A mark, a title and a copy slot; only the copy is required.") {
        slot(name = "icon", description = "A chip on top of a titled notice, a bare glyph beside a note.") {
            Notice(title = "Backup saved", icon = ArecaIcons.CheckCircle, tone = NoticeTone.Success)
        }
        slot(name = "title", description = "The headline. Null turns the banner into a note.") {
            Notice(title = "Backup saved", tone = NoticeTone.Success) {
                NoticeText("All 1,284 transactions were written to the file.")
            }
        }
        slot(name = "content", description = "NoticeText lines, or anything else — a button, a bar.") {
            Notice(title = null, icon = ArecaIcons.CheckCircle, tone = NoticeTone.Success) {
                NoticeText("All 1,284 transactions were written to the file.")
                NoticeText("You'll find it in Downloads.", quiet = true)
            }
        }
    }

    Variants(
        title = "Tone × emphasis",
        description = "A titled banner in every tone, subtle then filled. Only the mark carries a " +
            "subtle tone; a filled one tints the ground and every line.",
        layout = VariantLayout.Grid,
    ) {
        for (tone in NoticeTone.entries) {
            for (emphasis in NoticeEmphasis.entries) {
                variant(name = "${tone.name} / ${emphasis.name}") {
                    Notice(
                        title = tone.sampleTitle(),
                        icon = tone.sampleIcon(),
                        tone = tone,
                        emphasis = emphasis,
                    ) {
                        NoticeText(tone.sampleLine())
                    }
                }
            }
        }
    }

    Variants(
        title = "Notes",
        description = "No title: the glyph sits at the start, level with the first line, and the " +
            "tone's default emphasis applies.",
        layout = VariantLayout.Vertical,
    ) {
        for (tone in NoticeTone.entries) {
            variant(name = tone.name, detail = "title = null") {
                Notice(title = null, icon = tone.sampleIcon(), tone = tone) {
                    NoticeText(tone.sampleNote())
                }
            }
        }
        variant(name = "Danger, two lines", detail = "the glyph stays on the first line") {
            Notice(title = null, icon = ArecaIcons.AlertCircle, tone = NoticeTone.Danger) {
                NoticeText(
                    "We couldn't reach the server, so your changes haven't been saved yet. They'll " +
                        "be sent as soon as you're back online.",
                )
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Default") {
            Notice(title = "Syncing is paused", icon = ArecaIcons.CloudOff) {
                NoticeText("Changes stay on this device until you turn syncing back on.")
            }
        }
        variant("busy = true", detail = "titled: the spinner takes the chip's place") {
            Notice(title = "Restoring your backup", icon = ArecaIcons.RefreshCcw, busy = true) {
                NoticeText("This can take a minute for a large ledger.")
            }
        }
        variant("busy = true", detail = "note: the spinner takes the glyph's place") {
            Notice(title = null, icon = ArecaIcons.RefreshCcw, busy = true) {
                NoticeText("Checking for newer details…")
            }
        }
        variant("icon = null", detail = "no mark; the copy starts at the edge") {
            Notice(title = "Syncing is paused") {
                NoticeText("Changes stay on this device until you turn syncing back on.")
            }
        }
    }

    Recipes {
        recipe(
            name = "Couldn't sign in",
            description = "An untitled danger note above the button it is about, as AuthBanner draws " +
                "it. Press the button to fold it in and out.",
        ) {
            var error by remember { mutableStateOf<String?>("That email and password don't match an account.") }

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl)) {
                AnimatedNotice(value = error, modifier = Modifier.fillMaxWidth()) { shown ->
                    Notice(
                        title = null,
                        icon = ArecaIcons.AlertCircle,
                        tone = NoticeTone.Danger,
                        size = NoticeSize.default(
                            padding = PaddingValues(ArecaTheme.spacing.lg),
                            iconGap = ArecaTheme.spacing.md,
                        ),
                    ) {
                        NoticeText(shown)
                    }
                }
                Button(
                    text = "Sign in",
                    onClick = {
                        error = if (error == null) "That email and password don't match an account." else null
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        recipe(
            name = "Showing saved details",
            description = "Neutral, on a section's ground, with an extra-small chip at the start so " +
                "it reads as one more card in a settings list.",
        ) {
            Notice(
                title = "Showing saved details",
                icon = ArecaIcons.CloudOff,
                tone = NoticeTone.Neutral,
                style = NoticeStyle.of(
                    tone = NoticeTone.Neutral,
                    bg = ArecaTheme.colors.bgSurface,
                    chipShape = ArecaTheme.shapes.tile,
                ),
                size = NoticeSize.default(
                    chip = FeaturedIconSize.ExtraSmall,
                    iconPlacement = NoticeIconPlacement.Start,
                ),
            ) {
                NoticeText("We couldn't reach the server, so these are from your last visit.")
            }
        }
        recipe(
            name = "Nothing was imported",
            description = "Danger, filled by default: two lines of fact and a quiet line of advice.",
        ) {
            Notice(title = "Nothing was imported", icon = ArecaIcons.FileX, tone = NoticeTone.Danger) {
                NoticeText("statement-2026-08.csv isn't a bank export we recognise.")
                NoticeText("None of its 312 rows were added to your ledger.")
                NoticeText("Export the statement as CSV from your bank's website and try again.", quiet = true)
            }
        }
        recipe(
            name = "Sync status",
            description = "A neutral banner whose copy slot also carries the action that fixes it.",
        ) {
            Notice(title = "Last synced 3 hours ago", icon = ArecaIcons.CloudOff) {
                NoticeText("Your phone couldn't reach the server. Edits made here are kept on this device.")
                Button(
                    text = "Sync now",
                    onClick = {},
                    style = ButtonStyle.outlined(ActionColor.Neutral),
                    size = ButtonSize.Small,
                    leading = { Icon(ArecaIcons.RefreshCcw, contentDescription = null) },
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Long title and copy",
            expectation = "Both wrap at the full width; the chip stays on top and does not stretch.",
        ) {
            Notice(
                title = "Some transactions in this import were recorded in a currency you don't use yet",
                icon = ArecaIcons.AlertTriangle,
                tone = NoticeTone.Warning,
            ) {
                NoticeText(
                    "48 rows are in Swiss francs. They were converted to your main currency at the " +
                        "rate on each transaction's date, and the original amounts are kept on each " +
                        "transaction so you can check them later.",
                )
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 220dp: the note's glyph keeps its column and the copy wraps.",
        ) {
            Notice(
                title = null,
                icon = ArecaIcons.InfoCircle,
                modifier = Modifier.widthIn(max = NarrowWidth),
            ) {
                NoticeText("Amounts are shown in your main currency.")
            }
        }
        case(
            name = "Title only",
            expectation = "No copy: the chip sits over the title and nothing is left hanging below it.",
        ) {
            Notice(title = "Backup saved", icon = ArecaIcons.CheckCircle, tone = NoticeTone.Success)
        }
        case(
            name = "Title with Start placement",
            expectation = "The chip is centred on the title line, not hung from the block's top edge.",
        ) {
            Notice(
                title = "Rates are a day old",
                icon = ArecaIcons.AlertTriangle,
                tone = NoticeTone.Warning,
                size = NoticeSize.default(iconPlacement = NoticeIconPlacement.Start),
            ) {
                NoticeText("Today's rates aren't published yet, so yesterday's are used.")
            }
        }
    }
}

/**
 * The emphasis [Notice] picks when none is passed, repeated here so the playground can pass it to
 * [NoticeStyle.of] as well.
 *
 * @param tone Tone of the notice.
 * @return Filled for [NoticeTone.Danger], subtle otherwise.
 */
private fun defaultEmphasis(tone: NoticeTone): NoticeEmphasis =
    if (tone == NoticeTone.Danger) NoticeEmphasis.Filled else NoticeEmphasis.Subtle

/**
 * The glyph a product would pair with this tone.
 *
 * @return An icon from the Areca set.
 */
private fun NoticeTone.sampleIcon(): ImageVector = when (this) {
    NoticeTone.Neutral -> ArecaIcons.CloudOff
    NoticeTone.Info -> ArecaIcons.Lightbulb
    NoticeTone.Success -> ArecaIcons.CheckCircle
    NoticeTone.Warning -> ArecaIcons.AlertTriangle
    NoticeTone.Danger -> ArecaIcons.AlertCircle
}

/**
 * A realistic banner title in this tone.
 *
 * @return The headline.
 */
private fun NoticeTone.sampleTitle(): String = when (this) {
    NoticeTone.Neutral -> "Showing saved details"
    NoticeTone.Info -> "Exports now include receipts"
    NoticeTone.Success -> "Backup saved"
    NoticeTone.Warning -> "Some rows were skipped"
    NoticeTone.Danger -> "Nothing was imported"
}

/**
 * The line under [sampleTitle].
 *
 * @return One sentence explaining the title.
 */
private fun NoticeTone.sampleLine(): String = when (this) {
    NoticeTone.Neutral -> "We couldn't reach the server, so these are from your last visit."
    NoticeTone.Info -> "Photos you attached are added to the archive as separate files."
    NoticeTone.Success -> "All 1,284 transactions were written to the file."
    NoticeTone.Warning -> "12 rows had no date and were left out of the import."
    NoticeTone.Danger -> "The file isn't a bank export we recognise."
}

/**
 * A realistic one-line note in this tone.
 *
 * @return The whole of what the note says.
 */
private fun NoticeTone.sampleNote(): String = when (this) {
    NoticeTone.Neutral -> "Amounts are shown in your main currency."
    NoticeTone.Info -> "Drag a file onto this page to attach it."
    NoticeTone.Success -> "Your changes were saved."
    NoticeTone.Warning -> "Rates are from yesterday; today's aren't published yet."
    NoticeTone.Danger -> "That email and password don't match an account."
}
