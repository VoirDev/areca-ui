package dev.voir.areca.playground.showcase.chrome

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.theme.ArecaTheme

/**
 * Roles the showcase shell renders text in.
 *
 * Deliberately fewer roles than the design system offers: the shell is a frame around the component
 * being inspected, so it should never compete with it typographically.
 */
enum class ChromeTextRole {
    /** Page title in the showcase header. */
    PageTitle,

    /** One-sentence page summary under the title. */
    PageSummary,

    /** Section heading. */
    SectionTitle,

    /** One-sentence description under a section heading. */
    SectionDescription,

    /** Control name, variant name, tab label. */
    Label,

    /** Supporting detail — a variant subtitle, a control hint, a dp readout. */
    Detail,
}

/**
 * Foreground roles available to the shell.
 */
enum class ChromeTone {
    Primary,
    Secondary,
    Muted,
    Accent,

    /** A value the shell is reporting as wrong — a ratio under its floor. */
    Critical,

    /** Content on an accent-filled chrome surface. */
    OnAccent,
}

/**
 * Text inside the showcase shell.
 *
 * Built on [BasicText] and theme tokens rather than the design system's own `Text`, so the shell
 * keeps rendering when the component under inspection does not. See `SHOWCASES.md` §5.
 *
 * @param text Text to render.
 * @param role Typographic role; resolves to a theme text style.
 * @param modifier Modifier applied to the text node.
 * @param tone Foreground role; resolves to a theme color.
 * @param maxLines Maximum rendered lines before truncation.
 */
@Composable
fun ChromeText(
    text: String,
    role: ChromeTextRole,
    modifier: Modifier = Modifier,
    tone: ChromeTone = role.defaultTone,
    maxLines: Int = Int.MAX_VALUE,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = role.style.merge(color = tone.color),
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

private val ChromeTextRole.defaultTone: ChromeTone
    get() = when (this) {
        ChromeTextRole.PageTitle -> ChromeTone.Primary
        ChromeTextRole.PageSummary -> ChromeTone.Secondary
        ChromeTextRole.SectionTitle -> ChromeTone.Primary
        ChromeTextRole.SectionDescription -> ChromeTone.Secondary
        ChromeTextRole.Label -> ChromeTone.Primary
        ChromeTextRole.Detail -> ChromeTone.Muted
    }

internal val ChromeTextRole.style: TextStyle
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        ChromeTextRole.PageTitle -> ArecaTheme.typography.headingLarge
        ChromeTextRole.PageSummary -> ArecaTheme.typography.bodyLarge
        ChromeTextRole.SectionTitle -> ArecaTheme.typography.headingExtraSmall
        ChromeTextRole.SectionDescription -> ArecaTheme.typography.bodySmall
        ChromeTextRole.Label -> ArecaTheme.typography.labelMedium
        ChromeTextRole.Detail -> ArecaTheme.typography.bodyExtraSmall
    }

internal val ChromeTone.color: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        ChromeTone.Primary -> ArecaTheme.colors.fgBase
        ChromeTone.Secondary -> ArecaTheme.colors.fgSubtle
        ChromeTone.Muted -> ArecaTheme.colors.fgMuted
        ChromeTone.Accent -> ArecaTheme.colors.fgBrandText
        ChromeTone.Critical -> ArecaTheme.colors.fgDangerText
        ChromeTone.OnAccent -> ArecaTheme.colors.fgOnFill
    }
