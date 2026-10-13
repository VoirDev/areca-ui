package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import dev.voir.areca.flow.attachments.LocalAttachmentImageLoader
import dev.voir.areca.flow.attachments.LocalAttachmentPdfRenderer
import dev.voir.areca.flow.profile.LocalAvatarImageLoader
import dev.voir.areca.playground.flow.attachments.SampleAttachmentImageLoader
import dev.voir.areca.playground.flow.attachments.SampleAttachmentPdfRenderer
import dev.voir.areca.playground.flow.profile.SampleAvatarLoader
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.voir.areca.playground.registry.ShowcaseRegistry
import dev.voir.areca.playground.shell.ShowcaseSidebar
import dev.voir.areca.playground.showcase.canvas.LocalPlaygroundDarkTheme
import dev.voir.areca.playground.showcase.chrome.ChromeSegmented
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider

private val SidebarWidth = 264.dp

/** Theme the whole playground shell renders in. */
private enum class ShellTheme { Light, Dark }

/**
 * The Areca playground.
 *
 * Sidebar, content area and the shell theme switch. Every page comes from [ShowcaseRegistry]: the
 * navigation graph, the sidebar and search all read the same list, so a page is declared once
 * instead of being wired separately into routes, nav and the graph.
 */
@Composable
fun App() {
    var shellTheme by rememberSaveable { mutableStateOf(ShellTheme.Light) }
    val dark = shellTheme == ShellTheme.Dark

    ArecaThemeProvider(darkTheme = dark) {
        CompositionLocalProvider(
            LocalPlaygroundDarkTheme provides dark,
            // Every avatar in the playground draws through the sample loader, the way an
            // application provides its own once at its root. See flow/profile/README.md.
            LocalAvatarImageLoader provides SampleAvatarLoader,
            // The same for attachments: pictures and PDFs are drawn by samples that need no network,
            // the way an application provides its own. See flow/attachments/README.md.
            LocalAttachmentImageLoader provides SampleAttachmentImageLoader,
            LocalAttachmentPdfRenderer provides SampleAttachmentPdfRenderer,
        ) {
            val navController = rememberNavController()

            // Selection is derived from the back stack rather than held alongside it, so it stays
            // correct on first render and through back navigation.
            val backStackEntry by navController.currentBackStackEntryAsState()
            val selectedId = backStackEntry?.destination?.route ?: ShowcaseRegistry.first().id

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ArecaTheme.colors.bgCanvas),
            ) {
                Column(
                    modifier = Modifier
                        .width(SidebarWidth)
                        .fillMaxHeight(),
                ) {
                    ShellHeader(theme = shellTheme, onThemeChange = { shellTheme = it })

                    ShowcaseSidebar(
                        entries = ShowcaseRegistry,
                        selectedId = selectedId,
                        onSelect = { id ->
                            if (id != selectedId) {
                                navController.navigate(id) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(ShowcaseRegistry.first().id) { saveState = true }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                NavHost(
                    navController = navController,
                    startDestination = ShowcaseRegistry.first().id,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ShowcaseRegistry.forEach { entry ->
                        composable(entry.id) { entry.content() }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShellHeader(
    theme: ShellTheme,
    onThemeChange: (ShellTheme) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ArecaTheme.colors.bgSubtle)
            .padding(ArecaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ChromeText(text = "Areca", role = ChromeTextRole.SectionTitle, maxLines = 1)
            }
            ChromeText(
                text = "${ShowcaseRegistry.size} pages",
                role = ChromeTextRole.Detail,
                tone = ChromeTone.Muted,
                maxLines = 1,
            )
        }

        ChromeSegmented(
            options = ShellTheme.entries,
            selected = theme,
            onSelect = onThemeChange,
            label = { it.name },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
