package dev.voir.areca.icons

/**
 * Namespace for the Areca icon set; every icon is an extension property on it, such as `ArecaIcons.Check`.
 *
 * Each icon is an `ImageVector` with a 24 dp intrinsic size on a 24-unit grid, drawn with a 2-unit stroke and
 * round caps and joins, in black so that any tint replaces the colour. The vector is built on first access and
 * reused for the life of the process. Directional icons (arrows, chevrons, corners, back and forward, log in and
 * out, send, indent) set `autoMirror`, so they flip in right-to-left layouts; media transport, clocks, text
 * alignment and two-way arrows do not.
 *
 * The drawings are converted from Lucide (ISC) and Tabler Icons (MIT), whose notices ship in the artifact as
 * `META-INF/dev.voir.areca/icons/NOTICE.md`. `SocialApple`, `SocialFacebook` and `SocialGoogle` are the
 * providers' own trademarked sign-in marks and keep their brand colours unless tinted.
 */
public object ArecaIcons
