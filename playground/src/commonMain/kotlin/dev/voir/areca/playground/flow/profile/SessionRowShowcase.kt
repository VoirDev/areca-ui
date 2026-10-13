package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.session.SessionDevice
import dev.voir.areca.flow.profile.session.SessionItem
import dev.voir.areca.flow.profile.session.SessionRow
import dev.voir.areca.flow.profile.sheet.RevokeSessionSheet
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import kotlinx.coroutines.delay

/** Which of the two detail lines the playground session carries under its name. */
private enum class DetailLines(
    /** Label of the option in the control panel. */
    val label: String,
    /** Whether the client line is drawn. */
    val client: Boolean,
    /** Whether the last-seen line is drawn. */
    val seen: Boolean,
) {
    /** Client and last seen. */
    Both("Both", client = true, seen = true),

    /** Only what the session runs. */
    Client("Client", client = true, seen = false),

    /** Only where and when it was last active. */
    Seen("Seen", client = false, seen = true),

    /** The device name alone. */
    None("None", client = false, seen = false),
}

/** Showcase for [SessionRow], the [SessionItem] it draws and its [SessionDevice] glyphs. */
@Composable
fun SessionRowShowcase() = ShowcaseScaffold(
    title = "Session row",
    summary = "One device the account is signed in on, pressed to sign that device out.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Build one session and press it; the current session ignores presses.") {
        val name by textControl("name", default = ProfileSamples.OtherSession.name)
        val device by enumControl(
            key = "device",
            default = SessionDevice.Desktop,
            options = SessionDevice.entries,
            optionLabel = { it.name },
        )
        val lines by enumControl(
            key = "detail lines",
            default = DetailLines.Both,
            options = DetailLines.entries,
            optionLabel = { it.label },
        )
        val current by booleanControl(
            key = "isCurrent",
            default = false,
            description = "This device: an inverted tile, and nothing to press.",
        )
        val revoking by booleanControl("isRevoking", default = false)
        val enabled by booleanControl("enabled", default = true)

        preview {
            var presses by remember { mutableIntStateOf(0) }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Settings(modifier = Modifier.fillMaxWidth()) {
                    Section {
                        SessionRow(
                            session = ProfileSamples.OtherSession.copy(
                                device = device,
                                name = name,
                                client = ProfileSamples.OtherSession.client.takeIf { lines.client },
                                seen = ProfileSamples.OtherSession.seen.takeIf { lines.seen },
                                isCurrent = current,
                                isRevoking = revoking,
                            ),
                            onClick = { presses++ },
                            enabled = enabled,
                        )
                    }
                }

                Text(
                    text = "Pressed $presses times",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Muted,
                )
            }
        }
    }

    Anatomy(description = "A device tile, the device's name and up to two lines of detail.") {
        slot(
            name = "Device tile",
            description = "Round neutral tile with the device glyph; inverted for this device.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(session = ProfileSamples.Sessions[0], onClick = {})
                    SessionRow(session = ProfileSamples.OtherSession, onClick = {})
                }
            }
        }
        slot(
            name = "Name",
            description = "The device, in semibold, on one line.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(
                        session = ProfileSamples.OtherSession.copy(client = null, seen = null),
                        onClick = {},
                    )
                }
            }
        }
        slot(
            name = "Client and last seen",
            description = "What the session runs in the base colour, then where and when it was active.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(session = ProfileSamples.OtherSession, onClick = {})
                }
            }
        }
        slot(
            name = "Status",
            description = "The list's spinner at the end of the row while a sign-out is in flight.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(
                        session = ProfileSamples.OtherSession.copy(isRevoking = true),
                        onClick = {},
                    )
                }
            }
        }
    }

    Variants(
        title = "Device",
        description = "The glyph each kind of device gets; phones and tablets share the bezelled screen.",
        layout = VariantLayout.Vertical,
    ) {
        SessionDevice.entries.forEach { kind ->
            variant(name = kind.name, detail = "device = SessionDevice.${kind.name}") {
                Settings(modifier = Modifier.fillMaxWidth()) {
                    Section {
                        SessionRow(
                            session = ProfileSamples.OtherSession.copy(
                                device = kind,
                                name = DeviceNames.getValue(kind),
                            ),
                            onClick = {},
                        )
                    }
                }
            }
        }
    }

    Variants(
        title = "Detail lines",
        description = "The row is as tall as the lines it has; a missing line leaves no gap.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Two lines", detail = "client and seen") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(session = ProfileSamples.OtherSession, onClick = {})
                }
            }
        }
        variant(name = "One line", detail = "seen = null") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(session = ProfileSamples.OtherSession.copy(seen = null), onClick = {})
                }
            }
        }
        variant(name = "No lines", detail = "client and seen null") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(
                        session = ProfileSamples.OtherSession.copy(client = null, seen = null),
                        onClick = {},
                    )
                }
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Other session", detail = "Pressable") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(session = ProfileSamples.OtherSession, onClick = {})
                }
            }
        }
        variant(name = "This device", detail = "isCurrent: inert, inverted tile") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(session = ProfileSamples.Sessions[0], onClick = {})
                }
            }
        }
        variant(name = "Revoking", detail = "isRevoking: spinner") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(
                        session = ProfileSamples.OtherSession.copy(isRevoking = true),
                        onClick = {},
                    )
                }
            }
        }
        variant(name = "Disabled", detail = "While every other device signs out") {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(session = ProfileSamples.OtherSession, onClick = {}, enabled = false)
                }
            }
        }
    }

    Recipes {
        recipe(
            name = "Active sessions list",
            description = "Press a device to sign it out; it spins, then leaves the list.",
        ) {
            var sessions by remember { mutableStateOf(ProfileSamples.Sessions.drop(1)) }
            var pending by remember { mutableStateOf<SessionItem?>(null) }
            var revokingId by remember { mutableStateOf<String?>(null) }

            Settings(modifier = Modifier.fillMaxWidth()) {
                Section(header = { Text("Active sessions") }) {
                    if (sessions.isEmpty()) {
                        SettingsRow(
                            title = { Text("No other devices") },
                            subtitle = { Text("Only this device is signed in") },
                        )
                    }
                    sessions.forEach { session ->
                        key(session.id) {
                            SessionRow(
                                session = session.copy(isRevoking = session.id == revokingId),
                                onClick = { pending = session },
                            )
                        }
                    }
                }
            }

            RevokeSessionSheet(
                session = pending,
                onConfirm = { session ->
                    pending = null
                    revokingId = session.id
                },
                onDismiss = { pending = null },
            )

            revokingId?.let { id ->
                LaunchedEffect(id) {
                    delay(RevokeDelayMillis)
                    sessions = sessions.filterNot { it.id == id }
                    revokingId = null
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long device name",
            expectation = "Every line stays on one line and ends in an ellipsis; the tile never moves.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(
                        session = ProfileSamples.OtherSession.copy(
                            name = "Ada's MacBook Pro (16-inch, Nov 2023) — Analytical Engine office",
                            client = "Areca for macOS 7.2.9 arm64, Developer ID build, beta channel",
                            seen = "Kragujevac, Šumadija District, Serbia · 10:18",
                        ),
                        onClick = {},
                    )
                }
            }
        }
        case(
            name = "Unidentified device",
            expectation = "An unknown device still gets a tile — the question mark — never an empty circle.",
        ) {
            Settings(modifier = Modifier.fillMaxWidth()) {
                Section {
                    SessionRow(
                        session = SessionItem(
                            id = "unknown",
                            device = SessionDevice.Unknown,
                            name = "Unknown device",
                            seen = "Frankfurt, Germany · 2 Sep",
                        ),
                        onClick = {},
                    )
                }
            }
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 240dp: the lines ellipsise and the spinner keeps its place.",
        ) {
            Settings(modifier = Modifier.width(240.dp)) {
                Section {
                    SessionRow(
                        session = ProfileSamples.OtherSession.copy(isRevoking = true),
                        onClick = {},
                    )
                }
            }
        }
    }
}

/** A realistic device name for each kind, so the glyph is read against a name that fits it. */
private val DeviceNames: Map<SessionDevice, String> = mapOf(
    SessionDevice.Phone to "Pixel 9 Pro",
    SessionDevice.Tablet to "iPad Air",
    SessionDevice.Desktop to "MacBook Pro",
    SessionDevice.Browser to "Firefox",
    SessionDevice.Unknown to "Unknown device",
)

/** How long the recipe's pretend sign-out takes, so its spinner can be seen. */
private const val RevokeDelayMillis: Long = 1_500
