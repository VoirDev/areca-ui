# Releasing

Two things are released from this repository, on their own schedules: the libraries
(`dev.voir.areca:*`, all at one version) and the Agent Skills (the `areca` Claude Code plugin).

## Versions

`VERSION` holds the version every module builds as — the last release, `X.Y.Z` — and local builds
(`publishToMavenLocal` included) publish as it; `-PreleaseVersion=X.Y.Z` overrides it for one build.
Only releases change `VERSION`; it keeps the released version until the next release.

The `version` of `.claude-plugin/plugin.json` is the skills' own. Installs update only when it
changes, so:

- every library release raises it by the same kind as `VERSION` — a `minor` library release raises
  the plugin's minor number — so new library behaviour always reaches the skills;
- a skills-only release raises it alone.

`check` fails when the plugin version is not `MAJOR.MINOR.PATCH`. Only the two release workflows
below change it.

Every library is built with the JDK 21 toolchain and published as Java 17 bytecode, for JVM and
Android alike.

## Library release

1. **Prepare Release** (manual, from `main`) sets `VERSION` to the next release, raises the plugin
   version by the same kind (`ci/release/bump-plugin-version`), and opens the `Release vX.Y.Z` pull
   request from `release/vX.Y.Z` (`ci/release/prepare-version`, `ci/release/validate-changes`,
   `ci/release/create-pull-request`). The `patch`, `minor` or `major` bump gives the lowest release
   of that kind above `VERSION`: from `1.2.3` they give `1.2.4`, `1.3.0` and `2.0.0`; from
   `1.2.3-SNAPSHOT`, `1.2.3`, `1.3.0` and `2.0.0`. Its optional `version` input sets an exact
   `X.Y.Z` instead, which must be greater than `VERSION`, a release counting as greater than its own
   `-SNAPSHOT`. A version already tagged `vX.Y.Z` is rejected.
2. On that pull request, Verify runs `ci/verify` (never skipped there), and **Build release
   artifacts** runs `ci/release/build-artifacts` on macOS: it stages every publication in
   `build/release-staging`, checks coordinates and files, resolves the result as a Java 17 consumer
   (`ci/release/consumer`), and keeps it as the run's `release-artifacts`.
3. Merging starts **Publish Release**. After approval in the `release` environment it uploads the
   kept files unchanged to this repository's GitHub Packages Maven registry
   (`ci/release/publish-github-packages`), then tags `vX.Y.Z` and creates the GitHub Release. A
   failed run is re-run; it never rebuilds, and a rerun uploads only the files the registry does not
   yet serve.

Only a release is ever built for publication: `build-artifacts`, `check-staging` and `detect`
reject a `-SNAPSHOT` version.

## Skills release

**Prepare Skills Release** (manual, from `main`, a `patch`, `minor` or `major` bump) raises the
plugin version alone and opens the `Release skills vX.Y.Z` pull request from
`skills-release/vX.Y.Z` (`ci/skills-release/prepare-version`, `ci/skills-release/validate-changes`,
`ci/skills-release/create-pull-request`). Merging it is the release; no library is built or
published.

After either release merges, an install updates with:

```bash
claude plugin marketplace update areca-ui
```

```bash
claude plugin update areca@areca-ui --scope user
```

## Repository settings

`main` requires the `Build and check whole repository` and `Build release artifacts` checks on an
up-to-date branch. Prepare Release uses the `RELEASE_BOT_TOKEN` secret and the `RELEASE_BOT_NAME`
and `RELEASE_BOT_EMAIL` variables. Publication needs no secret: the publishing job's own
`GITHUB_TOKEN` holds `packages: write`, and the `release` environment only gates the job on its
reviewers' approval.
