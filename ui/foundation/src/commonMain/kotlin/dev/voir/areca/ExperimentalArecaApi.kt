package dev.voir.areca

/**
 * Marks Areca API that is public but not yet stable.
 *
 * A declaration carrying it may change shape or behaviour, or be removed, in any release — including
 * a minor or patch one — without a deprecation cycle. Everything without it follows the library's
 * compatibility promise.
 *
 * Using a marked declaration is a compile error until the call site opts in, so depending on one is
 * always a deliberate choice:
 *
 * ```kotlin
 * @OptIn(ExperimentalArecaApi::class)
 * @Composable
 * fun Feed() {
 *     // Experimental components are usable here.
 * }
 * ```
 *
 * A module that builds on several can opt in once with the compiler argument
 * `-opt-in=dev.voir.areca.ExperimentalArecaApi`.
 */
@RequiresOptIn(
    message = "This API is experimental and may change without notice.",
    level = RequiresOptIn.Level.ERROR
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY
)
public annotation class ExperimentalArecaApi
