package dev.voir.areca.playground.foundation.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme

// Demo content the adaptive pages share, so they document one finance app and one perfume shop
// rather than a different product per page.

/**
 * One transaction in the sample finance app.
 *
 * @property id Identifies the transaction.
 * @property merchant Who was paid, or who paid.
 * @property category What the money was for.
 * @property amount Signed amount, formatted.
 * @property date When it was booked.
 * @property wallet Which wallet it was booked to.
 */
internal class Transaction(
    val id: Int,
    val merchant: String,
    val category: String,
    val amount: String,
    val date: String,
    val wallet: String,
) {
    /** Whether money came in. */
    val isIncome: Boolean get() = amount.startsWith("+")
}

/** The sample finance app's recent transactions, newest first. */
internal val Transactions: List<Transaction> = listOf(
    Transaction(1, "Lidl", "Groceries", "−€42.80", "Today, 18:12", "Main account"),
    Transaction(2, "Café Central", "Eating out", "−€6.40", "Today, 08:47", "Main account"),
    Transaction(3, "Northwind GmbH", "Salary", "+€3,200.00", "28 Oct", "Main account"),
    Transaction(4, "Netflix", "Subscriptions", "−€15.99", "27 Oct", "Travel card"),
    Transaction(5, "Rent", "Housing", "−€950.00", "25 Oct", "Main account"),
    Transaction(6, "Uber", "Transport", "−€14.20", "24 Oct", "Travel card"),
)

/** Olfactory family a perfume is filed under. */
internal enum class PerfumeFamily { Woody, Amber, Fresh, Floral, Gourmand }

/**
 * One perfume in the sample shop.
 *
 * @property id Identifies the perfume.
 * @property name The perfume's name.
 * @property house The house that makes it.
 * @property family Its olfactory family.
 * @property price Price of a 50ml bottle, formatted.
 */
internal class Perfume(
    val id: Int,
    val name: String,
    val house: String,
    val family: PerfumeFamily,
    val price: String,
)

/** The sample shop's catalogue. */
internal val Perfumes: List<Perfume> = listOf(
    Perfume(1, "Santal Noir", "Maison Orlan", PerfumeFamily.Woody, "€145"),
    Perfume(2, "Ambre Nuit", "Atelier Verre", PerfumeFamily.Amber, "€168"),
    Perfume(3, "Bergamot 22", "Casa Lumen", PerfumeFamily.Fresh, "€98"),
    Perfume(4, "Iris Poudré", "Maison Orlan", PerfumeFamily.Floral, "€152"),
    Perfume(5, "Vanille Fumée", "Atelier Verre", PerfumeFamily.Gourmand, "€134"),
    Perfume(6, "Vetiver Rain", "Nord Parfums", PerfumeFamily.Woody, "€112"),
    Perfume(7, "Neroli Sun", "Casa Lumen", PerfumeFamily.Fresh, "€94"),
    Perfume(8, "Rose de Mai", "Nord Parfums", PerfumeFamily.Floral, "€126"),
    Perfume(9, "Tonka Café", "Studio Mirra", PerfumeFamily.Gourmand, "€118"),
    Perfume(10, "Oud Ember", "Studio Mirra", PerfumeFamily.Amber, "€210"),
)

/**
 * A perfume as a catalogue card: name, house, family and price.
 *
 * @param perfume The perfume.
 * @param modifier Modifier applied to the card.
 */
@Composable
internal fun PerfumeCard(perfume: Perfume, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), onClick = {}) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
        ) {
            Text(perfume.name, variant = TextVariant.HeadingSmall, maxLines = 1)
            Text(perfume.house, variant = TextVariant.BodySmall, color = TextColor.Muted, maxLines = 1)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = ArecaTheme.spacing.sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Badge(style = BadgeStyle.Neutral, size = BadgeSize.Small) { Text(perfume.family.name) }
                Text(perfume.price, variant = TextVariant.BodyMedium)
            }
        }
    }
}
