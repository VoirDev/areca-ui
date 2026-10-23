# Recipes

Whole screens built from Areca components, to copy and adapt. Each recipe says what it builds,
which components it uses, the code with its imports, and variations.

Literal strings stand in for your string resources (see [Localisation](../localisation.md)), and
screen state would normally come from a view model. Every recipe needs an `ArecaThemeProvider` above
it — see [Getting started](../getting-started.md).

## Foundation screens

| Recipe | Builds | Main components | Modules |
| --- | --- | --- | --- |
| [Settings screen](settings.md) | Sections of rows on cards: a toggle with a save status, a choice from a menu, a danger action | `Settings`, `Section`, `SettingsToggleRow`, `SettingsSelectRow`, `SettingsRow` | foundation, icons |
| [Form with validation](form.md) | A sign-up form with labelled fields, errors after the first attempt, a server error banner and a submit button with progress | `FieldGroup`, `TextField`, `PasswordField`, `Notice`, `Button` | foundation |
| [List screen](list.md) | A paged list with pull-to-refresh and loading, error, empty and end-of-list faces | `PagedContentList`, `NavigationListItem`, `Fallback` | foundation, icons |
| [Confirmation flow](confirmation.md) | A destructive action confirmed in a sheet, with progress, a failure note and a toast | `ConfirmSheet`, `ToastHost`, `Button` | foundation, icons |
| [Tabbed screen](tabs.md) | Primary destinations in a bottom tab bar, and tabs inside one destination | `TabBar`, `Tabs`, `TopBar` | foundation, icons |
| [Analytics overview](analytics.md) | A period picker drawn as a column chart, the period's figures, and categories with meters that open | `ColumnChart`, `StackedBarChart`, `MeterBar`, `StatGrid`, `ExpandableListItem` | foundation, charts, icons |

## Flow screens

The flow modules ship the parts of whole screens; their recipes assemble them and live in each
module's README.

| Module | Recipes |
| --- | --- |
| [flow-auth](../../flow/auth/README.md) | Sign in; providers only, with no form; content above and below the form; a way back, and steps; email first; verify a code; pages an emailed link opens; a custom footer |
| [flow-profile](../../flow/profile/README.md) | The profile; editing the profile; the username; the account menu; devices; changing the photo, with cropping |
| [flow-attachments](../../flow/attachments/README.md) | The sheets and the viewer; attachments in a form; a record's details; documents; cropping a photo before it is attached |

## Building your own

- Start from `BaseLayout` and `TopBar`, and apply the padding `BaseLayout` hands its content — as a
  lazy list's `contentPadding`, or `Modifier.padding(padding)`.
- Pick components from the [catalogue](../components.md); restyle them through their style and size
  ([Customising](../customising.md)), never with a wrapper.
- Read every value from the [theme](../theme.md): `ArecaTheme.spacing.xl`, not `16.dp`.
- Name what has no words and localise what is spoken ([Accessibility](../accessibility.md),
  [Localisation](../localisation.md)).
- The playground (`./gradlew :playground:run`) shows most components with live controls, and the
  flows assembled into screens.
