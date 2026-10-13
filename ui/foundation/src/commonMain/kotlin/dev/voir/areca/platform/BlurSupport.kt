package dev.voir.areca.platform

/**
 * Whether this platform blurs what a composable draws, rather than ignoring the request.
 *
 * `Modifier.blur` and a layer's render effect are no-ops on platforms that cannot honour them, and
 * they fail silently: the content is simply drawn sharp. Anything that blurs for *looks* can ignore
 * that, but anything that blurs to *hide* cannot — a silently skipped blur would leave the content
 * on screen in full. Components in that second group read this and take an opaque path instead.
 *
 * Constant for the life of the process, so it is safe to read outside composition.
 */
internal expect val isBlurSupported: Boolean
