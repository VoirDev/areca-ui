package dev.voir.areca.window

/**
 * The input mode an application gets when it names none: what this platform is usually driven by.
 *
 * Constant for the life of the process. A device that breaks the rule — a tablet with a trackpad, a
 * laptop with a touchscreen — is reported correctly only when the application passes its own mode to
 * `ArecaThemeProvider`.
 */
internal expect val PlatformInputMode: InputMode
