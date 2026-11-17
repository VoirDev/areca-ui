package dev.voir.areca.playground.docs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.gallery.part.GalleryCaption
import dev.voir.areca.flow.gallery.part.GalleryPrimaryAction
import dev.voir.areca.foundation.adaptive.AdaptiveNavigation
import dev.voir.areca.foundation.adaptive.AdaptiveToolbar
import dev.voir.areca.foundation.adaptive.AdaptiveToolbarAction
import dev.voir.areca.foundation.adaptive.EdgeButton
import dev.voir.areca.foundation.adaptive.EdgeButtonEmphasis
import dev.voir.areca.foundation.adaptive.EdgeStatus
import dev.voir.areca.foundation.adaptive.NavDestination
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.feedback.ProgressBar
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.navigation.PageCounter
import dev.voir.areca.foundation.navigation.PageCounterStyle
import dev.voir.areca.foundation.navigation.PageIndicator
import dev.voir.areca.foundation.navigation.PageIndicatorStyle
import dev.voir.areca.foundation.navigation.PageIndicatorVariant
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.stat.Stat
import dev.voir.areca.foundation.stat.StatGrid
import dev.voir.areca.foundation.stat.StatSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.FavoriteAction
import dev.voir.areca.foundation.topbar.ShareAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.icons.Activity
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Check
import dev.voir.areca.icons.Download
import dev.voir.areca.icons.Home
import dev.voir.areca.icons.Plus
import dev.voir.areca.icons.Share
import dev.voir.areca.icons.User
import dev.voir.areca.icons.Wallet
import dev.voir.areca.media.LocalMediaImageLoader
import dev.voir.areca.media.carousel.Carousel
import dev.voir.areca.media.carousel.CarouselPageSize
import dev.voir.areca.media.carousel.rememberCarouselState
import dev.voir.areca.media.compare.CompareSlider
import dev.voir.areca.media.image.MediaFit
import dev.voir.areca.media.image.MediaFrame
import dev.voir.areca.media.image.MediaFrameSize
import dev.voir.areca.media.image.MediaFrameStyle
import dev.voir.areca.media.image.MediaImage
import dev.voir.areca.media.image.MediaPlaceholder
import dev.voir.areca.media.image.MediaScrim
import dev.voir.areca.media.thumbnail.ThumbnailStrip
import dev.voir.areca.media.thumbnail.ThumbnailStripStyle
import dev.voir.areca.media.viewer.MediaViewer
import dev.voir.areca.media.viewer.MediaViewerImage
import dev.voir.areca.media.viewer.MediaViewerStyle
import dev.voir.areca.playground.flow.attachments.SampleAttachmentImageLoader
import dev.voir.areca.playground.media.SampleLoad
import dev.voir.areca.playground.media.SampleMediaImageLoader
import dev.voir.areca.playground.media.SamplePicture
import dev.voir.areca.playground.media.SampleScene
import dev.voir.areca.playground.media.SampleShape
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WindowClass
import dev.voir.areca.window.adaptive

/*
 * The documentation images of the media module, the gallery flow and the adaptive shell.
 */

/** A trail shoe's pictures: its sides, a detail, a campaign shot, a scene and another colour. */
private val ShoePictures = listOf(
    SamplePicture(201, "Trail runner, side"),
    SamplePicture(202, "Trail runner, other side", flip = true),
    SamplePicture(204, "Autumn campaign", scene = SampleScene.Studio, width = 900, height = 1100),
    SamplePicture(205, "On the trail", scene = SampleScene.Landscape, hue = 150f, width = 1200, height = 800),
    SamplePicture(206, "Colourway: Lake", hue = 205f),
)

/** The shoe on the trail, a scene rather than a product. */
private val TrailPicture = ShoePictures.first { it.scene == SampleScene.Landscape }

/** Products of a shop's front page, the last of which fails to load. */
private val ShopPictures = listOf(
    SamplePicture(211, "Water bottle", shape = SampleShape.Bottle, hue = 340f),
    SamplePicture(212, "Canvas tote", shape = SampleShape.Bag, hue = 30f),
    SamplePicture(213, "Lounge chair", shape = SampleShape.Chair, hue = 200f, load = SampleLoad.Fails),
)

/** The landscape before it was colour-graded, for the comparison. */
private val Ungraded = SamplePicture(220, "On the trail, ungraded", scene = SampleScene.Landscape, hue = 220f, width = 1200, height = 800)

/**
 * The sample pictures, painted at once rather than after a wait, so a frame holds its picture by
 * the time the image is taken; every other model goes to the attachment loader the frame provides.
 *
 * @param content What loads the pictures.
 */
@Composable
private fun SamplePictures(content: @Composable () -> Unit) {
    val loader = remember { SampleMediaImageLoader(waitMillis = 0, fallback = SampleAttachmentImageLoader) }
    CompositionLocalProvider(LocalMediaImageLoader provides loader, content = content)
}

/** The screen gutter `horizontalContentInsets()` applies at the width class in scope, for a scroller's content padding. */
@Composable
private fun docGutter(): Dp {
    val spacing = ArecaTheme.spacing
    return adaptive(
        compact = spacing.horizontalContentInsets,
        medium = spacing.mediumHorizontalContentInsets,
        expanded = spacing.expandedHorizontalContentInsets,
    )
}

/** `media`: a carousel with its indicator, a thumbnail strip, frames with badges and a failed face, a comparison. */
internal val MediaShot = DocShot(name = "media", width = 720.dp) {
    SamplePictures {
        DocColumns(
            {
                DocGroup("Carousel") {}
                val state = rememberCarouselState(itemCount = ShoePictures.size)
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                    // Edge to edge, the gutter as content padding, so the next picture peeks in.
                    Carousel(
                        state = state,
                        pageSize = CarouselPageSize.Fraction(0.86f),
                        itemSpacing = ArecaTheme.spacing.md,
                        contentPadding = PaddingValues(horizontal = docGutter()),
                        contentDescription = "Trail runner pictures",
                    ) { index ->
                        val picture = ShoePictures[index]
                        MediaFrame(
                            model = picture,
                            contentDescription = picture.title,
                            aspectRatio = 1f,
                            fit = if (picture.scene == SampleScene.Product) MediaFit.Whole else MediaFit.Fill,
                            size = MediaFrameSize.Product,
                            onClick = {},
                            onClickLabel = "Open pictures",
                        )
                    }
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        PageIndicator(
                            pageCount = state.pageCount,
                            currentPage = state.currentPage,
                            contentDescription = "Picture 1 of ${ShoePictures.size}",
                        )
                    }
                }
                DocGroup("Thumbnail strip") {}
                ThumbnailStrip(
                    count = ShoePictures.size,
                    selectedIndex = 0,
                    onSelect = {},
                    // Five thumbnails fit the column, so the strip has nothing to scroll.
                    contentPadding = PaddingValues(start = docGutter()),
                    label = { index, total -> "${ShoePictures[index].title}, ${index + 1} of $total" },
                ) { index ->
                    MediaFrame(model = ShoePictures[index], contentDescription = null, fit = MediaFit.Fill)
                }
            },
            {
                DocGroup("Frames, badges and a failed picture") {
                    Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                        ShopPictures.forEachIndexed { index, picture ->
                            MediaFrame(
                                model = picture,
                                contentDescription = picture.title,
                                modifier = Modifier.weight(1f),
                                aspectRatio = 1f,
                                fit = MediaFit.Whole,
                                failedText = "Couldn't load",
                            ) {
                                when (index) {
                                    0 -> Badge(
                                        modifier = Modifier.align(Alignment.TopStart).padding(ArecaTheme.spacing.sm),
                                        style = BadgeStyle.Brand,
                                        size = BadgeSize.Small,
                                    ) { Text("New") }
                                    1 -> Badge(
                                        modifier = Modifier.align(Alignment.TopStart).padding(ArecaTheme.spacing.sm),
                                        style = BadgeStyle.Danger,
                                        size = BadgeSize.Small,
                                    ) { Text("−20%") }
                                }
                            }
                        }
                    }
                    MediaFrame(
                        model = TrailPicture,
                        contentDescription = TrailPicture.title,
                        aspectRatio = 2f,
                        scrim = MediaScrim.Bottom,
                    ) {
                        Text(
                            "On the trail",
                            variant = TextVariant.HeadingSmall,
                            color = TextColor.OnFill,
                            modifier = Modifier.align(Alignment.BottomStart).padding(ArecaTheme.spacing.lg),
                        )
                    }
                }
                DocGroup("Compare slider") {
                    CompareSlider(
                        before = {
                            MediaImage(Ungraded, Ungraded.title, fit = MediaFit.Fill, style = MediaFrameStyle.bare())
                        },
                        after = {
                            MediaImage(TrailPicture, TrailPicture.title, fit = MediaFit.Fill, style = MediaFrameStyle.bare())
                        },
                        modifier = Modifier.fillMaxWidth(),
                        initialPosition = 0.45f,
                    )
                }
            },
        )
    }
}

/** A product page: the hero carousel with its indicators, the product's name, price and action. */
@Composable
private fun ProductPage() {
    val hero = rememberCarouselState(itemCount = ShoePictures.size)
    BaseLayout(
        topBar = {
            TopBar(
                title = "",
                leading = { BackAction(onClick = {}) },
                trailing = {
                    ShareAction(onClick = {})
                    FavoriteAction(selected = true, onClick = {})
                },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Box {
                Carousel(state = hero, contentDescription = "Trail runner pictures") { index ->
                    val picture = ShoePictures[index]
                    MediaFrame(
                        model = picture,
                        contentDescription = picture.title,
                        aspectRatio = 1f,
                        fit = MediaFit.Whole,
                        onClick = {},
                        onClickLabel = "Open pictures",
                        style = MediaFrameStyle.default(shape = ArecaTheme.shapes.none),
                        size = MediaFrameSize.Product,
                    )
                }
                PageIndicator(
                    pageCount = hero.pageCount,
                    currentPage = hero.currentPage,
                    variant = PageIndicatorVariant.Dots,
                    contentDescription = "Picture 1 of ${ShoePictures.size}",
                    style = PageIndicatorStyle.onImage(),
                    modifier = Modifier.align(Alignment.BottomCenter).padding(ArecaTheme.spacing.lg),
                )
                PageCounter(
                    currentPage = hero.currentPage,
                    pageCount = hero.pageCount,
                    style = PageCounterStyle.onImage(),
                    modifier = Modifier.align(Alignment.TopEnd).padding(ArecaTheme.spacing.md),
                )
            }
            Column(
                modifier = Modifier.horizontalContentInsets().padding(vertical = ArecaTheme.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                Text("Running shoes", variant = TextVariant.BodySmall, color = TextColor.Muted)
                Text("Trail runner", variant = TextVariant.HeadingSmall)
                Text("€129", variant = TextVariant.LabelLarge)
                Button(text = "Add to bag", onClick = {}, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

/** The gallery the product page opens: its bar, the picture, a caption, thumbnails and the one action. */
@Composable
private fun ProductGallery() {
    val style = MediaViewerStyle.dark()
    MediaViewer(
        itemCount = ShoePictures.size,
        onDismiss = {},
        initialIndex = 1,
        key = { index -> ShoePictures[index].id },
        style = style,
        topBar = { viewer ->
            TopBar(
                title = "${viewer.currentIndex + 1} of ${viewer.itemCount}",
                style = style.topBar(),
                titleAlignment = TopBarTitleAlignment.Center,
                leading = { CloseAction(onClick = viewer::dismiss) },
                trailing = {
                    ShareAction(onClick = {})
                    FavoriteAction(selected = true, onClick = {})
                },
            )
        },
        footer = { viewer ->
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                GalleryCaption(title = ShoePictures[viewer.currentIndex].title, kicker = "From the maker")
                ThumbnailStrip(
                    count = viewer.itemCount,
                    selectedIndex = viewer.currentIndex,
                    onSelect = { viewer.goTo(it) },
                    contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
                    label = { index, total -> "${ShoePictures[index].title}, ${index + 1} of $total" },
                    style = ThumbnailStripStyle.onDark(),
                ) { index ->
                    MediaFrame(model = ShoePictures[index], contentDescription = null, fit = MediaFit.Fill)
                }
                GalleryPrimaryAction(text = "Add to bag · €129", onClick = {})
            }
        },
    ) { index ->
        MediaViewerImage(
            model = ShoePictures[index],
            contentDescription = ShoePictures[index].title,
            placeholder = MediaPlaceholder.Blurred(ShoePictures[index]),
        )
    }
}

/** `flow-gallery`: a product page, and the gallery its pictures open. */
internal val FlowGalleryShot = DocShot(
    name = "flow-gallery",
    panels = listOf(
        DocPanel(width = 352.dp, padded = false) { SamplePictures { ProductPage() } },
        DocPanel(width = 352.dp, padded = false) {
            SamplePictures {
                ProductPage()
                ProductGallery()
            }
        },
    ),
    height = 640.dp,
)

/** Destinations of the tablet shell. */
private enum class ShellTab(val label: String) { Home("Home"), Activity("Activity"), Wallets("Wallets"), Account("Account") }

/** Size of the tablet the adaptive image is drawn at, on its side. */
private val TabletSize = DpSize(1000.dp, 620.dp)

/** `foundation-adaptive`: the edge column of `AdaptiveNavigation` on a tablet on its side, beside a screen of cards. */
internal val FoundationAdaptiveShot = DocShot(
    name = "foundation-adaptive",
    width = TabletSize.width,
    height = TabletSize.height,
    padded = false,
) {
    ProvideWindowClass(WindowClass(TabletSize, InputMode.Touch)) {
        val destinations = remember {
            listOf(
                NavDestination(ShellTab.Home, ShellTab.Home.label, ArecaIcons.Home),
                NavDestination(ShellTab.Activity, ShellTab.Activity.label, ArecaIcons.Activity),
                NavDestination(ShellTab.Wallets, ShellTab.Wallets.label, ArecaIcons.Wallet),
                NavDestination(ShellTab.Account, ShellTab.Account.label, ArecaIcons.User),
            )
        }
        AdaptiveNavigation(
            destinations = destinations,
            selectedValue = ShellTab.Activity,
            onValueSelected = {},
            onSearch = {},
            status = { EdgeStatus(imageVector = ArecaIcons.Check, contentDescription = "Synced just now") },
            header = {
                EdgeButton(
                    imageVector = ArecaIcons.Plus,
                    contentDescription = "New transaction",
                    onClick = {},
                    emphasis = EdgeButtonEmphasis.Primary,
                )
            },
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                AdaptiveToolbar(
                    title = "Activity",
                    actions = listOf(
                        AdaptiveToolbarAction(ArecaIcons.Download, "Export", onClick = {}),
                        AdaptiveToolbarAction(ArecaIcons.Share, "Share", onClick = {}),
                    ),
                )
                DocColumns(
                    {
                        DocGroup {
                            Card(modifier = Modifier.fillMaxWidth()) { cardPadding ->
                                Column(
                                    modifier = Modifier.padding(cardPadding),
                                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
                                ) {
                                    Stat(
                                        label = "Spent in October",
                                        value = "€1,720",
                                        note = "€480 under your usual month",
                                        size = StatSize.Large,
                                    )
                                    StatGrid(columns = 3) {
                                        Stat(label = "Income", value = "€4,250")
                                        Stat(label = "Saved", value = "€2,530")
                                        Stat(label = "Rate", value = "60%")
                                    }
                                }
                            }
                        }
                        Settings {
                            Section(header = { Text("Budgets") }) {
                                BudgetRow("Groceries", "€312 left", 0.55f)
                                BudgetRow("Dining", "€14 left", 0.93f)
                            }
                        }
                    },
                    {
                        TransactionList(SampleTransactions)
                    },
                )
            }
        }
    }
}

/**
 * A budget as a settings row: its category, what is left, and how much is spent under them.
 *
 * @param name The category.
 * @param left What is left to spend, formatted.
 * @param spent Share of the budget spent.
 */
@Composable
private fun BudgetRow(name: String, left: String, spent: Float) {
    SettingsRow(
        title = { Text(name) },
        value = { Text(left) },
        bottom = {
            ProgressBar(
                progress = spent,
                modifier = Modifier.fillMaxWidth().padding(top = ArecaTheme.spacing.md),
                contentDescription = "${(spent * 100).toInt()}% spent",
            )
        },
        onClick = {},
        onClickLabel = "Open budget",
    )
}
