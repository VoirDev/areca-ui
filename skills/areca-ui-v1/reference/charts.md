# Charts — `dev.voir.areca:charts`

Add it when a screen draws grouped bars, a split of a whole, a period picker made of columns, a
running total against another, an amount against a usual or a target, or the key to any of them.

- Artifact `dev.voir.areca:charts`; one package, `dev.voir.areca.charts`.
- Depends on `foundation` as `api`: no separate `foundation` line needed.
- Draws with the theme: needs the app's `ArecaThemeProvider` above it.
- The charts draw numbers they are handed. Aggregate, round, pick tick values and format every label
  in the app. Every drawn string is the app's.

## Choose a chart

| Need | Use | Types |
| --- | --- | --- |
| Values as vertical bars, side by side in groups, against an axis, guides and tick labels | `BarChart(groups, …)` | `BarChartGroup`, `BarChartBar`, `BarChartFill`, `BarChartOptions`, `BarChartBarWidth`, `BarChartShape`, `BarChartHorizontalAlignment`, `BarChartYAxisLabel` |
| One horizontal bar split into parts of a whole | `StackedBarChart(segments, …)` | `StackedBarSegment` |
| Periods as columns that scroll, open on the latest and select like tabs — a chart that is the period picker | `ColumnChart(columns, selectedKey = …, onSelect = …)` | `ColumnChartColumn<K>`, `ColumnChartBar`, `ColumnChartSegment`, `ColumnChartReferenceLine`, `ColumnChartValueLabels`, `ColumnChartLayout`, `ColumnChartState` |
| Lines across a run of steps — a running total this month against last month | `LineChart(series, …)` | `LineChartSeries`, `LineChartStroke`, `LineChartMarker`, `LineChartXLabel`, `LineChartYLabel`, `LineChartTooltip` |
| One amount against a track, with a marker for a usual amount or a target | `MeterBar(fraction, marker = …)` | — |
| The key to a chart's colours | `ChartLegend(items)` | `ChartLegendItem`, `ChartSwatch` |

Not a chart: work under way is foundation's `ProgressBar`; a single figure is foundation's `Stat`.

## Signatures

```kotlin
@Composable fun BarChart(
    groups: List<BarChartGroup>,
    modifier: Modifier = Modifier,
    style: BarChartStyle = LocalBarChartStyle.current ?: BarChartStyle.Default,
    size: BarChartSize = LocalBarChartSize.current ?: BarChartSize.Medium,
    options: BarChartOptions = BarChartOptions(),
    yAxisLabels: List<BarChartYAxisLabel> = emptyList(),
    contentDescription: String? = null,
    valueFormatter: (Float) -> String = BarChartBar.Companion::formatValue,
)

@Composable fun StackedBarChart(
    segments: List<StackedBarSegment>,
    modifier: Modifier = Modifier,
    style: StackedBarChartStyle = LocalStackedBarChartStyle.current ?: StackedBarChartStyle.Default,
    size: StackedBarChartSize = LocalStackedBarChartSize.current ?: StackedBarChartSize.Medium,
    contentDescription: String? = null,
    fractionFormatter: (Float) -> String = /* whole percent, "52%" */,
)

@Composable fun <K> ColumnChart(
    columns: List<ColumnChartColumn<K>>,
    modifier: Modifier = Modifier,
    selectedKey: K? = null,
    onSelect: ((K) -> Unit)? = null,
    state: ColumnChartState = rememberColumnChartState(),
    referenceLines: List<ColumnChartReferenceLine> = emptyList(),
    valueLabels: ColumnChartValueLabels = ColumnChartValueLabels.Selected,
    layout: ColumnChartLayout = ColumnChartLayout.Scroll,
    maxValue: Float? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    style: ColumnChartStyle = LocalColumnChartStyle.current ?: ColumnChartStyle.Default,
    size: ColumnChartSize = LocalColumnChartSize.current ?: ColumnChartSize.Medium,
)
@Composable fun rememberColumnChartState(): ColumnChartState

@Composable fun LineChart(
    series: List<LineChartSeries>,
    modifier: Modifier = Modifier,
    xLabels: List<LineChartXLabel> = emptyList(),
    yLabels: List<LineChartYLabel> = emptyList(),
    rule: Int? = null,                       // step index of a dashed vertical rule (today)
    tooltip: LineChartTooltip? = null,
    maxValue: Float? = null,
    style: LineChartStyle = LocalLineChartStyle.current ?: LineChartStyle.Default,
    size: LineChartSize = LocalLineChartSize.current ?: LineChartSize.Default,
    contentDescription: String? = null,      // last, after style and size
)

@Composable fun MeterBar(
    fraction: Float,
    modifier: Modifier = Modifier,
    marker: Float? = null,
    color: Color? = null,                    // replaces the style's fill for this bar
    style: MeterBarStyle = LocalMeterBarStyle.current ?: MeterBarStyle.Default,
    size: MeterBarSize = LocalMeterBarSize.current ?: MeterBarSize.Medium,
    contentDescription: String? = null,
)

@Composable fun ChartLegend(
    items: List<ChartLegendItem>,
    modifier: Modifier = Modifier,
    style: ChartLegendStyle = LocalChartLegendStyle.current ?: ChartLegendStyle.Default,
    size: ChartLegendSize = LocalChartLegendSize.current ?: ChartLegendSize.Default,
)
```

## Data types

```kotlin
// Bar chart
BarChartGroup(label: String? = null, bars: List<BarChartBar>)
BarChartGroup.singleBars(bars: List<BarChartBar>): List<BarChartGroup>   // one bar per group
BarChartBar(value: Float, label: String? = null, fill: BarChartFill? = null, trackColor: Color? = null)
BarChartBar.formatValue(value: Float): String       // default valueFormatter: "42", "28.5"
BarChartFill.Solid(color: Color) | BarChartFill.Gradient(brush: Brush)  // brush spans the whole plot
BarChartYAxisLabel(value: Float, label: String)
BarChartOptions(
    showXAxis: Boolean = true, showYAxis: Boolean = true,
    showYGuides: Boolean = true, showBarTrack: Boolean = true,
    shape: BarChartShape = BarChartShape.Rounded,                  // Rounded | Square
    contentPadding: PaddingValues = PaddingValues(0.dp),
    minValue: Float = 0f, maxValue: Float? = null,
    barWidth: BarChartBarWidth = BarChartBarWidth.Fill,            // Fill | Fixed(width) | Range(min, max)
    horizontalAlignment: BarChartHorizontalAlignment = BarChartHorizontalAlignment.Center, // Start | Center | End
)

// Stacked bar
StackedBarSegment(fraction: Float, color: Color? = null, label: String? = null)

// Column chart
ColumnChartColumn<K>(
    key: K, bars: List<ColumnChartBar>, label: String,
    sublabel: String? = null, valueLabel: String? = null,
    enabled: Boolean = true, placeholder: Boolean = false,
    contentDescription: String? = null,
)
ColumnChartBar(segments: List<ColumnChartSegment>, companion: Boolean = false)
ColumnChartBar.of(value: Float, color: Color? = null)          // plain bar
ColumnChartBar.companion(value: Float, color: Color? = null)   // slim bar: the same period a year earlier
ColumnChartSegment(value: Float, color: Color? = null)         // several stack from the baseline up
ColumnChartReferenceLine(value: Float, label: String? = null, color: Color? = null)
ColumnChartValueLabels.None | Selected | All
ColumnChartLayout.Scroll | Fill

// Line chart
LineChartSeries(
    points: List<Float?>, label: String, color: Color? = null,
    stroke: LineChartStroke = LineChartStroke.Solid,               // Solid | Dashed
    area: Boolean = false,
    endMarker: LineChartMarker? = LineChartMarker.Filled,          // Filled | Hollow | null
)
LineChartXLabel(index: Int, label: String)
LineChartYLabel(value: Float, label: String)
LineChartTooltip(series: Int, index: Int, text: String)

// Legend
ChartLegendItem(label: String, swatch: ChartSwatch)
ChartSwatch.Square(color) | Dot(color) | Line(color) | DashedLine(color) | Tick(color)
```

| `ChartSwatch` | Keys |
| --- | --- |
| `Square` | A bar or a stacked segment |
| `Dot` | A figure's colour |
| `Line` / `DashedLine` | A solid series / a comparison series or reference line |
| `Tick` | A meter bar's marker |

## `ColumnChartState`

`rememberColumnChartState()` — hoist it to scroll from outside or read the position. Survives
configuration change.

| Member | Meaning |
| --- | --- |
| `isAtEnd`, `canScroll` | Pinned to the last column; content wider than the chart |
| `firstVisibleIndex`, `lastVisibleIndex` | Columns at least partly in view, in `columns` order; `-1` before layout or with no columns |
| `suspend scrollToEnd(animate: Boolean = true)` | Back to the latest; pins again |
| `suspend scrollToColumn(index: Int, animate: Boolean = true)` | Brings a column fully into view; does not select it |

## Rules

### Scales

- The top of the scale is the `maxValue` given — `BarChartOptions(maxValue = …)` for the bar chart —
  or, when `null`, the largest value. For `ColumnChart` that is the tallest bar (a stack counted
  whole) or the highest reference line, whichever is higher.
- With tick labels, state `maxValue` as the top label's value. The auto-scale fits the tallest bar
  exactly, so a tick above it is out of scale and hidden.
- Bar chart: bars grow from zero, clamped into `minValue..maxValue`. With the default `minValue = 0f`
  a negative value draws nothing; to show negatives pass a negative `minValue`. A `NaN` or infinite
  value is "no value": track only, left out of the scale and the description.
- `ColumnChart`: zero, negative and non-finite values draw nothing.
- `LineChart`: one scale from zero. A `null` point is "no value yet" and ends the line there; a
  non-finite point counts as `null`; a run of one step draws as a dot.
- `StackedBarChart`: fractions summing to at most `1f` draw as given and leave the rest as track;
  above `1f` they are scaled down together, so raw counts work. A zero, negative or non-finite
  fraction draws nothing.
- `MeterBar`: `fraction` and `marker` clamp to `0f..1f`; a non-finite one draws nothing.

### Column chart

- It holds no selection: it draws `selectedKey` and reports `onSelect`. Without `onSelect` the
  columns are not interactive and `selectedKey` only highlights (the current month).
- `onSelect` is never called for the selected column, a disabled one (`enabled = false`) or a
  `placeholder = true` one (dashed outline for missing history, skipped by assistive technology).
- Under `ColumnChartLayout.Scroll` (the default) put the latest period **last**: the chart opens there
  and stays pinned to the end as periods arrive or it resizes, until the user scrolls away. It
  scrolls to the selected column only when `selectedKey` changes, so "back to the latest" is
  selecting the last column.
- `ColumnChartLayout.Fill` for a handful of columns: they share the width, nothing scrolls, bars
  shrink to fit a narrow column.
- Pass the screen gutter as `contentPadding` so columns scroll to the screen's edge.
- A reference line's label sits on `ColumnChartStyle.referenceLabelBg` (default `bgCanvas`); match it
  to what the chart sits on.

### Line chart

- Draw the comparison first (`stroke = LineChartStroke.Dashed`, `endMarker = LineChartMarker.Hollow`)
  and the series the chart is about last, on top (`area = true` to shade under it).
- A `LineChartXLabel` outside the steps and a `LineChartYLabel` above the top are hidden.

### Colours

- Bar chart: one `BarChartColor` family colours bars and tracks: `BarChartStyle.of(BarChartColor.Info)`.
  Per bar: `fill`, `trackColor`.
- Stacked bar, column, line: a segment, bar or series without its own `color` takes
  `palette[index % palette.size]`. A stacked segment's index counts every segment, drawn or not,
  so a category keeps its colour at zero. A palette must not be empty.
- Give a `ChartLegend` the same colours the chart draws with. `StackedBarChart` draws no text, so it
  always needs a legend.

### Meter bar

- Not a progress indicator: no progress semantics. Use foundation `ProgressBar` for work under way.
- The marker overhangs the bar by `MeterBarSize.markerOverhang` (3 dp) above and below without taking
  space; leave that room.

## Accessibility

- `BarChart`, `StackedBarChart`, `LineChart`, `MeterBar` and `ChartLegend` are each one node.
- `BarChart` generates `"Q1: 2025 42, 2024 28.5; Q2: …"`; `StackedBarChart` `"Housing 52%, Food 23%"`;
  `LineChart` the series labels, then the tooltip text. The punctuation is English and numbers are
  unlocalised: pass `valueFormatter` (bar) or `fractionFormatter` (stacked) for real values, and
  `contentDescription` when the locale or the message needs a sentence ("Revenue rose every
  quarter").
- `MeterBar` reads only `contentDescription` ("€612, usual €550"); pass `null` when the row's text
  already says the amount.
- `ColumnChart`: each column is its own node, read as its `contentDescription` or label, sublabel and
  value label; announced as selected; with `onSelect`, a selectable group where a disabled column
  is announced unavailable. Give every real column a `contentDescription` naming the period and its
  figures ("October 2026, income €4,250, expenses €2,340").
- `ChartLegend` reads its labels joined by commas.
- All charts mirror in right-to-left layouts.

## Styles and sizes

| Chart | Style · local · factory | Size · local · steps |
| --- | --- | --- |
| `BarChart` | `BarChartStyle` · `LocalBarChartStyle` · `of(color: BarChartColor, bg, plotBg, axis, guide, xLabel, groupLabel, yLabel)`; `Default` = `of(Brand)` | `BarChartSize` · `LocalBarChartSize` · `Small` (160 dp plot), `Medium` (220), `Large` (280) |
| `StackedBarChart` | `StackedBarChartStyle` · `LocalStackedBarChartStyle` · `default(track, palette)` | `StackedBarChartSize` · `LocalStackedBarChartSize` · `Tiny` (4 dp, hairline gaps, under a pair of figures), `ExtraSmall` (8 dp, hairline gaps), `Small` (12), `Medium` (24), `Large` (40) |
| `ColumnChart` | `ColumnChartStyle` · `LocalColumnChartStyle` · `default(palette, companion, halo, baseline, referenceLine, referenceLabel, referenceLabelBg, label, labelSelected, sublabel, valueLabel, placeholder, dimmedAlpha = 0.45f)` | `ColumnChartSize` · `LocalColumnChartSize` · `Small` (110 dp plot, 48 dp columns), `Medium` (150), `Large` (200) |
| `LineChart` | `LineChartStyle` · `LocalLineChartStyle` · `default(palette, grid, label, rule, markerRing, tooltipBg, tooltipContent, areaAlpha = 0.22f)` | `LineChartSize` · `LocalLineChartSize` · `Default` (140 dp plot), `default(height, lineStroke, …)` |
| `MeterBar` | `MeterBarStyle` · `LocalMeterBarStyle` · `default(track, fill, marker)` | `MeterBarSize` · `LocalMeterBarSize` · `Slim` (4 dp), `Medium` (6), `Large` (10) |
| `ChartLegend` | `ChartLegendStyle` · `LocalChartLegendStyle` · `default(label)` | `ChartLegendSize` · `LocalChartLegendSize` · `Default`, `default(square, dot, lineLength, …, textStyle)` |

- `BarChartColor`: `Neutral`, `Brand`, `Success`, `Warning`, `Danger`, `Info`.
- Change one value with `copy`: `BarChartSize.Small.copy(height = 120.dp)`,
  `BarChartSize.Medium.copy(yLabelWidth = 56.dp)` for long tick labels,
  `ColumnChartSize.Medium.copy(columnWidth = 48.dp)`.
- Every `Default` is `default()` (bar: `of(BarChartColor.Brand)`); all are built in composition.
- `ArecaThemeProvider` does not seed the chart locals; provide them around the screens that want
  another default:

```kotlin
CompositionLocalProvider(
    LocalBarChartStyle provides BarChartStyle.of(BarChartColor.Info),
    LocalBarChartSize provides BarChartSize.Small,
) { Dashboard() }
```

## Snippets

```kotlin
// Weekly bars with ticks
BarChart(
    groups = BarChartGroup.singleBars(days.map { BarChartBar(it.steps, label = it.shortName) }),
    modifier = Modifier.fillMaxWidth(),
    options = BarChartOptions(maxValue = 10_000f),
    yAxisLabels = listOf(
        BarChartYAxisLabel(0f, "0"), BarChartYAxisLabel(5_000f, "5k"), BarChartYAxisLabel(10_000f, "10k"),
    ),
    valueFormatter = { formatSteps(it) },
)

// Split of a whole with its key
val palette = StackedBarChartStyle.Default.palette
StackedBarChart(
    segments = categories.map { StackedBarSegment(it.share, label = it.name) },
    modifier = Modifier.fillMaxWidth(),
    fractionFormatter = { formatPercent(it) },
)
ChartLegend(categories.mapIndexed { i, c -> ChartLegendItem(c.name, ChartSwatch.Square(palette[i % palette.size])) })

// Period picker
ColumnChart(
    columns = months.map { m ->
        ColumnChartColumn(
            key = m.id,
            bars = listOf(ColumnChartBar.of(m.spent), ColumnChartBar.companion(m.spentAYearEarlier)),
            label = m.shortName,
            sublabel = m.yearCaption,
            valueLabel = m.spentText,
            contentDescription = m.description,
        )
    },
    selectedKey = selectedMonth,
    onSelect = onSelectMonth,
    referenceLines = listOf(ColumnChartReferenceLine(value = usualSpend, label = usualLabel)),
    contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.xl),
)

// Running total against last month
LineChart(
    series = listOf(
        LineChartSeries(lastMonth, label = lastMonthName, stroke = LineChartStroke.Dashed, endMarker = LineChartMarker.Hollow),
        LineChartSeries(thisMonth, label = thisMonthName, area = true),
    ),
    modifier = Modifier.fillMaxWidth(),
    xLabels = listOf(LineChartXLabel(0, "1 Oct"), LineChartXLabel(30, "31")),
    yLabels = listOf(LineChartYLabel(1_000f, "€1k"), LineChartYLabel(2_000f, "€2k")),
    rule = todayIndex,
    tooltip = LineChartTooltip(series = 1, index = todayIndex, text = spentSoFarText),
    maxValue = 2_000f,
    contentDescription = summarySentence,
)

// Category against its usual, in a row
MeterBar(fraction = spent / budget, marker = usual / budget, contentDescription = null)
```
