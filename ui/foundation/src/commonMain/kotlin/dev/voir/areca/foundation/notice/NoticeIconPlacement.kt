package dev.voir.areca.foundation.notice

/** Where a [Notice]'s mark sits relative to its copy. */
public enum class NoticeIconPlacement {
    /**
     * Above the copy, as the block's heading.
     *
     * The copy then runs the full width of the notice rather than being pushed in by a column the
     * mark claims for the whole height of the block. The default for a notice with a title.
     */
    Top,

    /**
     * Before the copy, level with its first line.
     *
     * The shape of a one-line note, where a mark above a single sentence would be a heading with
     * nothing under it. The default for a notice without a title.
     */
    Start,
}
