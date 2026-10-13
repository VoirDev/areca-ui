package dev.voir.areca.foundation.notice

/**
 * What a [Notice] is saying about the screen it is on.
 *
 * A tone colours the notice's mark and, when it is [NoticeEmphasis.Filled], the whole block. It is
 * chosen by meaning rather than by look: a notice that states a fact is [Neutral] even when it would
 * be prettier in blue.
 */
public enum class NoticeTone {
    /** Something worth knowing; nothing is wrong. */
    Neutral,

    /** A fact that helps, with no judgement attached — a tip, a change of behaviour. */
    Info,

    /** Something finished as asked. */
    Success,

    /** Something worked, but not entirely — partial, delayed, or about to cost something. */
    Warning,

    /** Something stopped what was asked for. */
    Danger,
}
