package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.AvatarImageState
import dev.voir.areca.flow.profile.part.ProfileAvatar
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Which of [ProfileAvatar]'s picture parameters the playground passes. */
private enum class AvatarSource(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** `image: Any?`, resolved by the loader in scope. */
    Loader("image (loader)"),

    /** `state: AvatarImageState?`, passed directly, which bypasses the loader. */
    State("state"),
}

/** The avatar presets the playground offers, smallest first. */
private enum class AvatarSizeOption(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** [AvatarSize.Tiny]. */
    Tiny("Tiny"),

    /** [AvatarSize.ExtraSmall]. */
    ExtraSmall("ExtraSmall"),

    /** [AvatarSize.Small]. */
    Small("Small"),

    /** [AvatarSize.Medium]. */
    Medium("Medium"),

    /** [AvatarSize.Large]. */
    Large("Large"),

    /** [AvatarSize.ExtraLarge], the component's default. */
    ExtraLarge("ExtraLarge"),
    ;

    /** The preset this option stands for. */
    val size: AvatarSize
        @Composable
        get() = when (this) {
            Tiny -> AvatarSize.Tiny
            ExtraSmall -> AvatarSize.ExtraSmall
            Small -> AvatarSize.Small
            Medium -> AvatarSize.Medium
            Large -> AvatarSize.Large
            ExtraLarge -> AvatarSize.ExtraLarge
        }
}

/**
 * The state an application that decodes pictures itself would pass for [avatar].
 *
 * @receiver The sample picture model.
 * @param painter Picture drawn for [SampleAvatar.Picture].
 * @return The state, or `null` for no picture.
 */
private fun SampleAvatar.toState(painter: Painter): AvatarImageState? = when (this) {
    SampleAvatar.None -> null
    SampleAvatar.Picture -> AvatarImageState.Ready(painter)
    SampleAvatar.Loading -> AvatarImageState.Loading
    SampleAvatar.Broken -> AvatarImageState.Failed
}

/** Showcase for [ProfileAvatar], with its picture from the loader and from a state. */
@Composable
fun ProfileAvatarShowcase() = ShowcaseScaffold(
    title = "Profile avatar",
    summary = "The account's picture, or the initials that stand in for it while it loads or when " +
        "it cannot be drawn.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground {
        val source by enumControl(
            key = "source",
            default = AvatarSource.Loader,
            options = AvatarSource.entries,
            optionLabel = { it.label },
            description = "A state passed directly draws a brand-coloured picture, to tell the two apart.",
        )
        val avatar by enumControl(
            key = "picture",
            default = SampleAvatar.Picture,
            options = SampleAvatar.entries,
            optionLabel = { it.label },
        )
        val name by textControl("name", default = "Ada Lovelace")
        val size by enumControl(
            key = "size",
            default = AvatarSizeOption.ExtraLarge,
            options = AvatarSizeOption.entries,
            optionLabel = { it.label },
        )
        val busy by booleanControl(
            key = "busy",
            default = false,
            description = "Spinner regardless of the picture, as while a new one uploads.",
        )

        preview {
            when (source) {
                AvatarSource.Loader -> ProfileAvatar(
                    name = name,
                    image = avatar.takeUnless { it == SampleAvatar.None },
                    size = size.size,
                    busy = busy,
                )

                AvatarSource.State -> ProfileAvatar(
                    name = name,
                    state = avatar.toState(ColorPainter(ArecaTheme.colors.bgBrandBase)),
                    size = size.size,
                    busy = busy,
                )
            }
        }
    }

    Variants(
        title = "Where the picture comes from",
        description = "The loader in scope resolves a model; a state passed directly carries a picture the " +
            "application already decoded.",
        layout = VariantLayout.Horizontal,
    ) {
        variant(name = "image", detail = "LocalAvatarImageLoader") {
            ProfileAvatar(name = "Ada Lovelace", image = SampleAvatar.Picture)
        }
        variant(name = "state", detail = "AvatarImageState.Ready") {
            ProfileAvatar(
                name = "Ada Lovelace",
                state = AvatarImageState.Ready(ColorPainter(ArecaTheme.colors.bgBrandBase)),
            )
        }
    }

    Sizes(description = "The foundation avatar presets, smallest first; ExtraLarge is the default.") {
        AvatarSizeOption.entries.forEach { option ->
            variant(name = option.label) {
                ProfileAvatar(name = "Ada Lovelace", image = SampleAvatar.Picture, size = option.size)
            }
        }
    }

    States(layout = VariantLayout.Horizontal) {
        variant(name = "Picture", detail = "Ready") {
            ProfileAvatar(name = "Ada Lovelace", image = SampleAvatar.Picture)
        }
        variant(name = "Loading", detail = "Initials under the spinner") {
            ProfileAvatar(name = "Ada Lovelace", image = SampleAvatar.Loading)
        }
        variant(name = "Failed", detail = "Initials, never an empty circle") {
            ProfileAvatar(name = "Ada Lovelace", image = SampleAvatar.Broken)
        }
        variant(name = "No picture", detail = "image = null") {
            ProfileAvatar(name = "Ada Lovelace", image = null)
        }
        variant(name = "Busy", detail = "busy = true") {
            ProfileAvatar(name = "Ada Lovelace", image = SampleAvatar.Picture, busy = true)
        }
    }

    EdgeCases(layout = VariantLayout.Horizontal) {
        case(
            name = "Very long name",
            expectation = "First and last word only: \"AL\", never a wider circle.",
        ) {
            ProfileAvatar(
                name = "Augusta Ada King, Countess of Lovelace",
                image = null,
            )
        }
        case(
            name = "Single word",
            expectation = "The first two characters, so a username still reads.",
        ) {
            ProfileAvatar(name = "lovelace", image = null)
        }
        case(
            name = "Non-Latin name",
            expectation = "Initials in the name's own script.",
        ) {
            ProfileAvatar(name = "Ада Лавлейс", image = null)
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 240dp: keeps its size; it never shrinks to fit.",
        ) {
            ProfileAvatar(
                name = "Ada Lovelace",
                image = SampleAvatar.Picture,
                modifier = Modifier.width(240.dp),
            )
        }
    }

    Recipes {
        recipe(
            name = "Comment author",
            description = "A small avatar beside a name and a timestamp, as in a comment thread.",
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProfileAvatar(name = "Ada Lovelace", image = SampleAvatar.Picture, size = AvatarSize.Small)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ada Lovelace",
                        style = ArecaTheme.typography.labelMedium,
                        color = ArecaTheme.colors.fgBase,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "Replied 12 minutes ago",
                        style = ArecaTheme.typography.bodySmall,
                        color = ArecaTheme.colors.fgMuted,
                    )
                }
            }
        }
    }
}
