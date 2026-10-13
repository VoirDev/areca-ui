# Charts

Bar charts for Compose Multiplatform, drawn with the Areca theme.

| Chart | What it shows | Key types |
| --- | --- | --- |
| **Bar chart** | Values as vertical bars, side by side in groups, against a scale with an axis, guides and tick labels | `BarChart`, `BarChartGroup`, `BarChartBar`, `BarChartOptions`, `BarChartYAxisLabel` |
| **Stacked bar** | One horizontal bar split into colored parts of a whole | `StackedBarChart`, `StackedBarSegment` |

The module draws numbers it is handed. It does not aggregate, round, pick tick values or format
them for display: every label it draws is a string the application wrote, and the scale is either
the one the application states or the largest value it was given.

---

## Setup

```kotlin
implementation("dev.voir.areca:charts:1.0.0")
```

`charts` depends on `dev.voir.areca:foundation` as an API dependency, so it comes along. Both charts
draw with the theme, so an `ArecaThemeProvider` must sit above them — usually the one at the
application's root:

```kotlin
ArecaThemeProvider {
    App()
}
```

---

## Quick start

```kotlin
@Composable
fun WeeklySteps(steps: List<Pair<String, Float>>) {
    BarChart(
        groups = BarChartGroup.singleBars(
            steps.map { (day, count) -> BarChartBar(value = count, label = day) },
        ),
        modifier = Modifier.fillMaxWidth(),
        options = BarChartOptions(maxValue = 10_000f),
        yAxisLabels = listOf(
            BarChartYAxisLabel(0f, label = "0"),
            BarChartYAxisLabel(5_000f, label = "5k"),
            BarChartYAxisLabel(10_000f, label = "10k"),
        ),
    )
}
```

```kotlin
@Composable
fun SpendingSplit() {
    StackedBarChart(
        segments = listOf(
            StackedBarSegment(fraction = 0.52f, label = "Housing"),
            StackedBarSegment(fraction = 0.23f, label = "Food"),
            StackedBarSegment(fraction = 0.12f, label = "Transport"),
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}
```

---

## Bar chart

```kotlin
BarChart(
    groups = groups,
    modifier = Modifier,
    style = LocalBarChartStyle.current ?: BarChartStyle.Default,
    size = LocalBarChartSize.current ?: BarChartSize.Medium,
    options = BarChartOptions(),
    yAxisLabels = emptyList(),
    contentDescription = null,
    valueFormatter = BarChartBar.Companion::formatValue,
)
```

### Groups and bars

A chart is a list of **`BarChartGroup`**s, drawn start to end, and each group is a list of
**`BarChartBar`**s drawn side by side. Bars in one group sit `BarChartSize.barGap` apart; groups sit
`groupGap` apart, which is what makes them read as groups.

```kotlin
val revenue = listOf(
    BarChartGroup(
        label = "Q1",
        bars = listOf(
            BarChartBar(42f, label = "2025"),
            BarChartBar(28.5f, label = "2024"),
        ),
    ),
    BarChartGroup(
        label = "Q2",
        bars = listOf(
            BarChartBar(51f, label = "2025"),
            BarChartBar(36f, label = "2024"),
        ),
    ),
)
```

A chart of ungrouped bars puts each bar in a group of its own; **`BarChartGroup.singleBars(bars)`**
does that, as in the quick start.

- A bar's `label` is drawn under it, a group's `label` under the bar labels. The bar-label row is
  left out when no bar has a label, and the group-label row when no group has one, so an unlabelled
  chart ends at its plot. Bar labels wrap to `BarChartSize.xLabelMaxLines` lines (two by default)
  and group labels to `groupLabelMaxLines` (one), and are then cut short with an ellipsis.
- A group without bars is dropped. With no bars at all the chart still draws its empty plot at full
  size, so a chart waiting for data does not collapse.

### The scale

`BarChartOptions.minValue` is the value at the bottom of the plot, `0f` by default.
`BarChartOptions.maxValue` is the value at the top, or `null` to use the largest bar value.

**Bars grow from zero.** The baseline is zero, clamped into the scale, and each bar runs from it to
its value, itself clamped into the scale:

- With the default `minValue` of `0f` the baseline is the bottom of the plot, and **a negative
  value clamps to zero and draws no bar**. The auto-scale only ever finds a top, never a bottom, so
  to show negative values state a negative `minValue`; bars below zero then hang down from a
  baseline partway up the plot.
- With a `minValue` above zero the baseline clamps to the bottom, and a value at or below
  `minValue` draws no bar.
- A scale entirely below zero puts the baseline at the top, and every bar hangs from it.
- A value above the top is drawn full height.

```kotlin
BarChart(
    groups = BarChartGroup.singleBars(monthlyBalance),
    options = BarChartOptions(minValue = -500f, maxValue = 1_000f),
    yAxisLabels = listOf(
        BarChartYAxisLabel(-500f, label = "−500"),
        BarChartYAxisLabel(0f, label = "0"),
        BarChartYAxisLabel(1_000f, label = "1,000"),
    ),
)
```

**The auto-scale fits the data exactly.** With `maxValue = null` the tallest bar touches the top of
the plot, and a tick label above it is outside the scale and hidden. When the chart has tick labels,
state `maxValue` as the value of the top tick, as the quick start does. When no value lies above
`minValue` — no bars, or none above it — the top falls back to `minValue + 1f`.

**Small values stay visible.** A bar off the baseline is drawn at least `BarChartSize.minBarHeight`
tall, never past the plot's edge. A value exactly on the baseline draws no bar.

**Non-finite values mean "no value".** A bar whose value is `NaN` or infinite draws only its track,
is left out of the auto-scale, and is left out of the description — a missing day in a week of data,
not a zero.

### Axis, guides and tick labels

| Option | What it draws |
| --- | --- |
| `showXAxis` | The horizontal axis line, on the baseline — the bottom of the plot unless the scale reaches below zero |
| `showYAxis` | The vertical axis line, on the plot's start edge |
| `showYGuides` | A dashed line across the plot at each visible tick label |
| `showBarTrack` | A full-height track behind each bar |

All four are on by default. Lines are inset so their whole stroke stays inside the plot.

Tick labels are the **`yAxisLabels`** parameter: a `BarChartYAxisLabel(value, label)` sits level
with its value, in a column `BarChartSize.yLabelWidth` wide beside the plot, set against the edge
nearest the plot. A tick whose value is outside the scale is hidden, along with its guide. The
column, and the gap after it, take no room while no tick is visible. Guides are drawn only where
there are tick labels: a chart without `yAxisLabels` has no guides whatever `showYGuides` says.

A tick label is one line and is cut short with an ellipsis when it is wider than the column, so
widen `yLabelWidth` (through `BarChartSize.copy`) for long numbers rather than shortening them.

### Bar widths and alignment

`BarChartOptions.barWidth` decides how wide each bar is:

| `BarChartBarWidth` | Width of each bar |
| --- | --- |
| `Fill` | An equal share of the plot after the gaps. The default |
| `Fixed(width)` | `width`, or narrower when that many bars do not fit |
| `Range(min, max)` | `Fill`'s share kept within `min..max`; `min` holds only while that many bars of that width fit |

Whatever the mode, bars never extend past the plot. When the bars together are narrower than the
plot, `BarChartOptions.horizontalAlignment` places them: `Start`, beside the y axis; `Center`, the
default; or `End`.

```kotlin
BarChart(
    groups = BarChartGroup.singleBars(bars),
    modifier = Modifier.fillMaxWidth(),
    options = BarChartOptions(
        barWidth = BarChartBarWidth.Range(min = 12.dp, max = 32.dp),
        horizontalAlignment = BarChartHorizontalAlignment.Start,
    ),
)
```

**Unbounded width.** The chart fills the width it is given. Given unbounded width — inside a
horizontally scrolling row — there is nothing to fill, so it takes the width its bars need: a
`Fill` bar is `BarChartSize.unboundedBarWidth` wide, a `Fixed` bar its width, and a `Range` bar its
`max`. The bars then start at the plot's start edge and the alignment has no effect.

```kotlin
Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
    BarChart(
        groups = BarChartGroup.singleBars(dailyTotals),
        options = BarChartOptions(barWidth = BarChartBarWidth.Fixed(width = 12.dp)),
    )
}
```

### Fills, tracks and shape

A bar is filled with the style's `bar` color unless it carries a `fill` of its own, and its track
takes the style's `track` color unless it carries a `trackColor`:

```kotlin
BarChartBar(
    value = today,
    label = "Today",
    fill = BarChartFill.Solid(ArecaTheme.colors.bgWarnBase),
)
BarChartBar(
    value = total,
    fill = BarChartFill.Gradient(
        Brush.verticalGradient(listOf(ArecaTheme.colors.bgBrandBase, ArecaTheme.colors.bgInfoBase)),
    ),
)
```

A `Gradient`'s brush is sized to the whole plot, not to the bar, so a vertical gradient shades every
bar from one ramp and a tall bar reaches further along it than a short one.

`BarChartOptions.shape` rounds the bars and their tracks by `BarChartSize.barCornerRadius`
(`BarChartShape.Rounded`, the default) or squares them (`Square`). `contentPadding` insets
everything inside the chart's background.

### Right to left

The chart mirrors in a right-to-left layout. The first bar sits at the start edge beside the y axis,
the tick labels move to the other side, the labels under the plot follow their bars, and a
horizontal gradient mirrors with the plot so its first color stays at the start edge.

### What assistive technology reads

The chart is one node to assistive technology. It reads `contentDescription` when one is given, and
otherwise a description generated from the data: each group as its label, a colon and its bars;
each bar as its label and its value; groups separated by semicolons and bars by commas. The
revenue groups above read as:

```text
Q1: 2025 42, 2024 28.5; Q2: 2025 51, 2024 36
```

Bars with a non-finite value are left out, and so are blank labels. The tick labels and the drawn
bar labels are not read separately — the description is the whole chart.

Values are written by `valueFormatter`. The default, `BarChartBar.formatValue`, writes whole numbers
without a fractional part (`42`, not `42.0`) and anything else with Kotlin's `Float.toString`. It is
not localized — no grouping separators, `.` as the decimal mark — so pass the product's own
formatter when values are large, fractional, or carry a unit:

```kotlin
BarChart(
    groups = revenue,
    valueFormatter = { value -> "${value.roundToInt()} thousand euros" },
)
```

The punctuation joining the parts is English. When it does not suit the locale, or when a sentence
says more than the numbers — "Revenue rose every quarter" — pass `contentDescription` instead.

---

## Stacked bar

```kotlin
StackedBarChart(
    segments = segments,
    modifier = Modifier,
    style = LocalStackedBarChartStyle.current ?: StackedBarChartStyle.Default,
    size = LocalStackedBarChartSize.current ?: StackedBarChartSize.Medium,
    contentDescription = null,
    fractionFormatter = { fraction -> "${(fraction * 100f).roundToInt()}%" },
)
```

(The last line spells out what the default does; the default itself is private.)

### Fractions

Each `StackedBarSegment` takes `fraction` of the bar, from the start edge, in order:

- **Fractions summing to at most `1f` are drawn as given**, and whatever they leave uncovered shows
  the style's `track`. Segments of `0.5f` and `0.3f` fill 80% of the bar.
- **Fractions summing above `1f` are scaled down together** so the bar is exactly full. Segments of
  `3f`, `2f` and `1f` draw as a half, a third and a sixth — so raw counts work without dividing them
  yourself, as long as they add up to the whole.
- **A fraction that is zero, negative or not finite draws nothing** and counts toward nothing: it
  is left out of the total that decides whether to scale, and out of the description.

Segments run from the start edge, so they mirror in a right-to-left layout.

### Colors

A segment without a `color` of its own takes the style's palette color at **its index in the
list**: `palette[i % palette.size]`. The index counts every segment, drawn or not, so a category
keeps its color when its share falls to zero.

The default palette is the theme's solid fills — brand, success, warning, info, danger — in an
order that never puts the danger red beside the success green, wrapping around included, since
that pair is the one most readers with a color-vision deficiency cannot tell apart. Pass your own
with `StackedBarChartStyle.default(palette = …)`; a palette must not be empty.

### Labels and the description

The bar draws no text, and color is the only thing telling its segments apart, so show a legend
beside it. A segment's `label` is what assistive technology reads for it, followed by its share:

```text
Housing 52%, Food 23%, Transport 12%
```

The share read is the share drawn, after any scaling. A segment without a label reads its share
alone, and segments that draw nothing are left out. Shares are written by `fractionFormatter`, a
whole percentage by default, which is not localized. As with the bar chart, pass
`contentDescription` when the generated wording does not suit the locale.

---

## Styles and sizes

| Chart | Style · local | Size · local |
| --- | --- | --- |
| `BarChart` | `BarChartStyle` · `LocalBarChartStyle` | `BarChartSize` · `LocalBarChartSize` |
| `StackedBarChart` | `StackedBarChartStyle` · `LocalStackedBarChartStyle` | `StackedBarChartSize` · `LocalStackedBarChartSize` |

**`BarChartStyle.of(color, …)`** builds a bar chart's colors from the theme for one
**`BarChartColor`** family — `Neutral`, `Brand`, `Success`, `Warning`, `Danger` or `Info` — which
decides the bars (the family's solid fill) and the tracks (its tint), so a chart reads as one color
rather than a bar color on an unrelated grey. Everything else — `bg`, `plotBg`, `axis`, `guide` and
the three label colors — is shared by every family and can be overridden in the same call.
`BarChartStyle.Default` is `of(BarChartColor.Brand)`.

```kotlin
BarChartStyle.of(BarChartColor.Success, plotBg = ArecaTheme.colors.bgSubtle)
```

**`StackedBarChartStyle.default(track, palette)`** builds a stacked bar's colors;
`StackedBarChartStyle.Default` is `default()`.

Sizes come in steps:

| Step | `BarChartSize` | `StackedBarChartSize` |
| --- | --- | --- |
| `Small` | A 160dp plot with tight gaps, for a chart in a card or beside other content | A 12dp bar, for a list row or beside a figure |
| `Medium` | A 220dp plot. The default, for a chart that is a section of its screen | A 24dp bar. The default, for a summary above its legend |
| `Large` | A 280dp plot with roomy gaps, for a chart that is the subject of its screen | A 40dp bar, for the subject of its section |

A `BarChartSize` holds the plot height and shape, strokes, guide dash, corner radius, gaps, minimum
bar height, unbounded bar width, the tick-label column, and the type and line limits of the labels.
Change one value with `copy`: `BarChartSize.Small.copy(height = 120.dp)`.

Each chart's `style` and `size` default to its local, or to `Default` and `Medium` when the local is
`null`. `ArecaThemeProvider` does not seed these locals — it lives in `foundation`, which does not
depend on `charts` — so provide them yourself around the screens that want other defaults:

```kotlin
CompositionLocalProvider(
    LocalBarChartStyle provides BarChartStyle.of(BarChartColor.Info),
    LocalBarChartSize provides BarChartSize.Small,
) {
    Dashboard()
}
```

---

## Where to look

**Charts → Bar chart** in the playground shows both charts: the color families, the size steps and
shapes, the stacked bar with palette and custom colors, and the edge cases — a single bar, all-zero
values, fractions above one and below zero, long and missing labels, unbounded width, right to left,
and a chart without axes or guides. The rules behind the scale, the layout and the descriptions are
tested as plain functions in
[`BarChartMathTest`](src/commonTest/kotlin/dev/voir/areca/charts/BarChartMathTest.kt) and
[`StackedBarChartTest`](src/commonTest/kotlin/dev/voir/areca/charts/StackedBarChartTest.kt).
