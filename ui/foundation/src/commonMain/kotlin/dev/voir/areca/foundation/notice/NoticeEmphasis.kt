package dev.voir.areca.foundation.notice

/** How much of a [Notice] carries its [NoticeTone]. */
public enum class NoticeEmphasis {
    /**
     * Only the mark is toned; the block and its copy stay the page's greys.
     *
     * For a notice that sits among ordinary content and should not outshout it — a status line, a
     * caveat under a total.
     */
    Subtle,

    /**
     * The block is toned throughout: its ground, a solid mark, and every line of copy.
     *
     * For the one block on the page that says something went wrong. A toned mark over plain copy
     * reads as a glyph dropped onto an ordinary paragraph; a toned block reads as the message.
     */
    Filled,
}
