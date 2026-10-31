# Repo rules — Tensura Questlines

Repo-specific rules and notes. The org-wide `AGENTS.md` beside this file is
maintained in [`neoforge-mod-template`](https://github.com/HexaHexes/neoforge-mod-template)
and pulled in verbatim — **never hand-edit it.** Everything in this file is this
repo's own and is edited freely; keep it aligned with the org rules rather than
restating them.

**Repo:** `HexaHexes/tensura-questlines` • **Mod ID:** `tensura_questlines` • **Branch:** `main`
**Platform:** Minecraft 1.21.1 / NeoForge 21.1.259 / JDK 21 • **Jar:** `build/libs/tensura_questlines-<version>.jar`

The pack's authored questline content: five major lines - Reincarnation,
Professions, Commerce, Infinite Depths, Trials of Power - plus their
sub-questlines and the crossover bridges between them, delivered as FTB Quests
chapters from this mod jar. The mod registers no gameplay of its own; it exists
to ship chapters and any content-side glue the delivery mechanism needs. Lines
are planned through the major-line issues in `HexaHexes/tensura-quests`
(#29-#33), each gated on maintainer plot approval before its sub-questline
issues are detailed.

**Dependencies**
- Required: Minecraft, NeoForge, Tensura (`tensura` `[2.0.1.3,)` - every line is
  set in its world), FTB Quests (`ftbquests` `[2101.1.36,)` - the host system),
  Tensura Quests (`tensura_quests` `[1.0.0-alpha.8,)` - the task and reward
  types the chapters use).
- Optional: none declared yet. Individual sub-questlines may need optional
  content systems (Sympathetic Jobs, Infinite Dungeon suite, Lightman's
  Currency) - add those dependencies together with the line that needs them,
  gated the way AGENTS requires.

**Test:** `./gradlew runGameTestServer` — `TensuraQuestlinesGameTests`
(1 test). `expectedTests = 1` in `build.gradle` must match the `@GameTest`
count.

**Remote:** `github.com/HexaHexes/tensura-questlines` (public, default branch
`main`).

---

## Gotchas (do not regress them)

- **Chapter delivery is under investigation** (HexaHexes/tensura-quests#27):
  datapack-from-datagen vs programmatic construction. Do not author chapter
  files until it closes, and never hand-author JSON under
  `src/main/resources` - the org datapack rule forbids it outright for a Java
  mod.
- The template's Checkstyle gates are live: Javadoc is exempt from the
  three-line rule, but 4+ consecutive `//` line comments fail the build - let
  the code speak instead.
- Identity lives only in `gradle.properties`; `settings.gradle`
  `rootProject.name` and the mixins config filename must stay in sync with
  `mod_id` (the template README's rename checklist walks every site).
- Quest content must respect the pack's `tensura_skill_disabled` curation:
  never require a Tensura: Reincarnated skill the pack has disabled.
- The mod's required `tensura_quests` dependency means it loads only where
  that mod is installed - install both jars together in the pack.

## Naming - this repo

- Repo and folder: `tensura-questlines`; `mod_id`: `tensura_questlines`;
  Java base package `org.bitwisemadness.tensuraquestlines`, with the Maven
  `group` in `build.gradle` matching.

## CI & release model - this repo

- `ci.yml` runs the template's checks (junit, gametest, version-bump) plus the
  static job (SpotBugs + Checkstyle + Spotless).
- `release.yml` cuts the GitHub release for `v<mod_version>` on a push to
  `main` (pre-releases only when the repo variable `ALPHA_RELEASES` is true),
  and `publish.yml` uploads the artifact to the org's GitHub Packages registry
  on release, under the version in `gradle.properties`.
