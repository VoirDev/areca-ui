# Areca flow modules

> **Audience:** Anyone adding a module under `flow/`, or a part to one

`ui/` ships components. A product assembles them into screens, and across products those screens
come out the same: every application that has accounts has a sign-in form, a registration form, a
password it can reset and a code it can verify. Rebuilding them per application is how four
applications end up with four sign-in screens that are each subtly wrong in a different way.

A `flow` module is where that assembly lives once.

---

## 1. What a flow module is

A flow module is **theme-bound and opinionated about composition, and nothing else**. It decides
what a sign-in screen looks like. It does not decide what signing in means.

| It may | It may not |
| --- | --- |
| Depend on `:ui:foundation` and other `ui` modules | Depend on another `flow` module |
| Read `ArecaTheme` tokens | Ship its own theme, colors or type ramp |
| Hold UI state that describes what to draw (a reveal toggle, `AvatarUploadState`) | Hold form state, application state, sessions or user records |
| Draw a complaint it is given — `error: String?` | Validate: decide what is empty, malformed, too short or allowed |
| Report an intent: `onSubmit`, `onProviderClick(Google)` | Perform it — no network, no storage, no SDK |
| Default a required word to English (§4) | Invent optional copy, or hardcode a word a consumer cannot replace |

Three dependencies are banned outright, each because it would put a choice on the consumer's API
surface that belongs to the consumer:

- **No navigation library.** A flow sequences its own steps with plain state. An application that
  uses `navigation-compose` wires the screens up itself. This is the same reasoning that put
  `kotlinx-datetime` in `ui/datetime` rather than `ui/foundation`.
- **No dependency injection.** No Koin, no Hilt, no service locator.
- **No `:standalone:viewmodel`.** A screen here is a function of its arguments. What produces those
  arguments is the application's business.

## 2. Parts, not screens

A flow module ships **parts and a scaffold**, and documents the assemblies in its README. It does
not ship a `SignInScreen`.

The temptation is obvious and the failure is predictable. A sign-in screen is five product
decisions — whether there is a form at all, what sits above it, what sits below it, which providers
appear and in what shape, what the bottom edge says. A composable that takes all five as parameters
is longer to read than the assembly it replaces and still wrong for the sixth product, the one that
needs a workspace box between the email and the password. Every escape hatch added to rescue it
makes it worse.

So:

| Layer | Package | What it is |
| --- | --- | --- |
| **Scaffold** | `part/` | Insets, scrolling, gutter, hero placement, the pinned footer |
| **Parts** | `part/`, `sso/` | Header, credential fields, banner, links, legal line, provider buttons |
| **Recipes** | `README.md` | Whole screens, as code to copy and edit |

A recipe is documentation, and it must stay honest: each one in the README is also a compiled page
in the playground, so an assembly that stopped building cannot stay in the docs.

What a part must carry is everything a product would otherwise get wrong — autofill types, keyboard
and IME configuration, a heading role, a live region on an error, an accessibility label on an
icon-only button. That is the actual value on offer, and it is why a part is worth using even
though the assembly is the consumer's.

## 3. Configuration

Inputs kept orthogonal, so a product overrides only the axis it cares about:

| Input | Answers |
| --- | --- |
| `AuthBranding` | What the screen is made of — the wordmark, the hero artwork, the layout |
| Each part's word parameters | What it says — see §4 |
| The application's own state | What is in it right now — values, complaints, progress — passed to each part as plain parameters |

There is deliberately no `AuthConfig`. Which providers appear and which boxes are collected are
decided by what the assembly calls, not by a value it is passed — that is what removing the screen
layer means in practice.

Rules:

1. **A slot beats a boolean** wherever a slot is plausible. `hero: @Composable (() -> Unit)?` lets a
   consumer put anything there; `showHero: Boolean` lets them put one thing there.
2. **A boolean is for something with exactly two shapes.** Everything else is a slot or an enum.
3. **A part's defaults are the common case.** `AuthEmailField(value, onValueChange)` must be the
   field most screens want, with the rest reachable by named argument.
4. **Adding an axis must not break a consumer's source.** Configuration types are data classes with a
   default for every property, so code that names its arguments keeps compiling when one is added.
   A new property still changes the binary signatures (`copy`, `componentN`, the constructor); the
   ABI dump records that change so a release states it.

## 4. Words

A flow module has no strings type and no composition local of words. Every word a part draws is a
parameter of that part, and the application passes its own — from its `composeResources`, in its
language — where it composes the part.

```kotlin
AuthEmailField(
    value = email,
    onValueChange = onEmailChange,
    label = stringResource(Res.string.email),
    placeholder = stringResource(Res.string.email_placeholder),
)
```

Two rules decide a word parameter's default:

1. **A word the part cannot do without** — a title, a field's label, a button's caption, a sheet's
   question, the accessibility label of an icon-only control — has a plain English default, so the
   part works before the application translates it.
2. **An optional word** — a subtitle, a placeholder, a footer, a note, a hint, an accessibility
   label for something already visible — is nullable and defaults to `null`, which draws nothing. A
   part never invents copy the product did not write.

Anything that names or formats something is a function rather than a string — a provider's label,
a date, a count — so a translation decides where the value goes and a locale decides how it reads.
Complaints about what was typed are always worded by the application and passed in as text. The
module ships no rule that finds a complaint and no sentence that words one.

## 5. Platform work stops at the module boundary

Acquiring a Google identity token needs Credential Manager on Android and `ASAuthorization` on
iOS, and cannot be done at all on the JVM. None of that is in here.

The module draws the button, shows it busy when told to, and calls `onProviderClick(provider)`.
Everything after that is the application's. A consumer that cannot perform a provider's exchange on
the current platform leaves that provider out of the list it passes to `SsoGroup`, and the button
does not appear — which is why providers are a list rather than a flag per provider. `flow-profile`
applies the same rule to picture sources: `AvatarSourceSheet` offers the camera only when the
application lists it.

### Images

A flow module has no image-loading library and never takes one. An application using Coil should
not be handed a second loader, and one not using Coil should not be handed the first.

Artwork the product owns outright — a wordmark, a hero — is a `@Composable` slot, as in
`AuthBranding`. A picture that belongs to the *data* and appears in many parts at once — an
avatar — goes through a small loader interface the module declares and the application implements
once over its own library, provided through a composition local (`LocalAvatarImageLoader` in
`flow-profile`). The module passes the application's model through untouched and only ever sees
loading, ready or failed. Unprovided, the loader reports failure, so the fallback — initials, not
an empty frame — is what an application with no pictures sees.

`flow-attachments` follows the same rule for files (`LocalAttachmentImageLoader`), and passes the
size a picture is drawn at so a strip of photographs is not decoded at full size: the longest edge
for a fitted picture (`load`), and the side of the square a cropped one must cover (`loadSquare`).

### Documents

Drawing a PDF needs the platform's renderer — `PdfRenderer` on Android, PDFKit on iOS — and
Android's reads a seekable file, which means writing the bytes to storage first. That is platform
work, so it stops here too: `flow-attachments` declares a renderer interface
(`LocalAttachmentPdfRenderer`), its README carries implementations for both platforms to copy, and
with none provided a PDF is offered to another application like any other document.

## 6. Accessibility is the module's job, not the consumer's

The things every application gets wrong are exactly the things worth fixing once:

- Autofill content types on every credential field, so password managers work.
- IME actions that advance through the form and submit on the last field.
- Focus order matching visual order.
- An error that is announced, not just drawn.
- Touch targets at or above the platform minimum.

A screen that omits these is not finished, and a review should say so.

## 7. Versioning

A flow module is versioned with the rest of the repository and makes the same compatibility
promise. Screen-level work changes more often than components do, so its configuration types follow
§3.4: adding an axis is never a break.

Shipping parts rather than screens is also what makes that survivable. A recipe that turns out to
be wrong is a documentation change; a screen composable that turns out to be wrong is a breaking
release for everyone who called it.

## 8. Artifact naming

`ui/` and `standalone/` modules take their artifact id from their directory, and a group prefix
never appears in the coordinates — `:ui:foundation` publishes as `dev.voir.areca:foundation`.

Flow modules are the exception. `:flow:auth` would publish as `dev.voir.areca:auth`, which reads as
an authentication library rather than a set of screen parts, next to siblings named `foundation` and
`icons`. Flow modules therefore state their coordinates explicitly and carry the prefix:
`dev.voir.areca:flow-auth`.
