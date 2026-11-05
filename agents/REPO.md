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
  Tensura Quests (`tensura_quests` `[1.0.0-alpha.8,)` - our task and reward
  types), Tensura Compat: FTB (`tensura_ftb` `[2.0.0.4,)` - the native
  race/spirit/ability/awakening/existence types the Reincarnation line reads),
  Tensura Skill Books (`tensura_skill_books` `[2.2.3,)` - the study-book item
  the First Skills chapter requires).
- Optional: none declared yet. Individual sub-questlines may need optional
  content systems (Sympathetic Jobs, Infinite Dungeon suite, Lightman's
  Currency) - add those dependencies together with the line that needs them,
  gated the way AGENTS requires.

**Test:** `./gradlew runGameTestServer` — `TensuraQuestlinesGameTests` and
`ChapterDeliveryGameTests` (6 tests). `expectedTests = 6` in `build.gradle`
must match the `@GameTest` count.

**Remote:** `github.com/HexaHexes/tensura-questlines` (public, default branch
`main`).

---

## Gotchas (do not regress them)

- **Chapter delivery is programmatic** (verdict, HexaHexes/tensura-quests#27):
  FTB Quests 2101 reads chapters only from
  `config/ftbquests/quests/chapters/*.snbt` - there is no datapack path - so
  the mod builds chapters at server start through the FTB API, reusing FTB's
  own `/ftbquests generate_chapter` sequence. Never hand-author SNBT or JSON
  content files under `src/main/resources`; content is code, reviewed
  through PRs.
- **Delivery is additive and idempotent, never destructive**: chapters,
  quests, tasks and rewards are matched by stable id and refreshed in place,
  so player progression survives content revisions. Removing or renaming
  authored objects is a deliberate manual content revision; reconcile of
  removals is tracked as a follow-up issue.
- **Delivery never writes mid-session**: it marks the quest file dirty and
  lets FTB's normal save cycle (world save / server stop) persist it - the
  same path an editor edit takes. A fresh instance has no quest folder yet,
  and writing during startup fights FTB's file watching. Content is code and
  is re-created on the next boot, so nothing is lost to an early crash.
- Authoring map: specs live in `content/` (one class per major line, e.g.
  `ReincarnationContent`), the registry is `QuestlineContent`, and the only
  object ids you may write are the ones `ChapterIds` allocates.
- Native `tensura_ftb` types (Race, Spirit, Ability, Awakening, Existence)
  are consumed exactly as registered - never reimplemented, wrapped or
  re-derived (the never-duplicate rule).
- The GameTest FTB stack pins track the pack's shipped versions, and they
  are coupled: FTB Chunks (staged for `tensura_ftb`'s mixin) requires FTB
  Library `2101.1.39` or above, so bumping one means bumping its neighbours
  and re-running `runGameTestServer`.
- Authoring a *named* ability quest goes through `AbilityTask.readData` with
  the task's own `ability`/`status` keys (`LEARNT`, `MASTERED`,
  `AVAILABLE`): its fields are private and it has no setters. The skill id
  must exist in the pack or the task can never complete, so check
  `config/tensura_skill_books/tensura_skill_books-loot-skills.txt` (a
  generated index of every registered skill id) before naming one.
- Modded items in specs are **resolved lazily**, when the task is created
  rather than when the spec is built: a chapter's class load must never touch
  a registry its optional mod's absence could leave unusable. Delivery's
  per-chapter fault isolation is the second net, and the chapter's
  `requiredMods` entry is the first.
- The Lightman's Currency overlap audit came back clean: `lc_ftb_hook` sells
  FTB Chunks claiming (claim ports, refunds, force-load takeover, marketplace
  access) and registers **no quest tasks or rewards**, so the Commerce line
  duplicates nothing.
- Existence and awakening quests are authored through the same
  `readData` route (`existence`/`value` and `status` keys; the existence
  threshold also has the public `setValue`). Existence counters that track
  peaks (`MAX_AURA`, `MAX_EP`) keep the feat earned once reached. Every
  threshold and reward value in `content/` is a first-pass number: the
  game-testing round tunes them, and any change is an ordinary content
  revision.
- A chapter that reads an **optional content system** declares it through
  `AuthoredChapter.requiring(...)`: delivery skips the chapter when a listed
  mod is absent (dormant content) and isolates failures per chapter, so one
  broken family can never take the rest of the questlines down. Professions
  uses `sympatheticjobs`; the same shape is ready for Commerce
  (`lightmanscurrency`), the Depths and Trials.
- Slot unlocks work the way T:SJ reads them: `effectiveSlotLimit(player)` is
  the player's override when one is set, and `MAX_JOBS_GLOBAL` (the pack's
  default, 3) otherwise. Our unlock reward raises the override through SJ's
  own write path, and SJ clamps the result to its job count, so a rung is
  always meaningful yet can never over-grant. Rungs are one-time quests -
  nothing in the ladder needs repeatable quest support.
- The Professions ladder tops out at the pack's cap: Sympathetic Jobs sets
  `maxLevel = 25` on the shared curve (`XP(25) = 5188`), so the major's
  original "25/50" beat was amended to a 25 capstone when Competence landed.
  Competence carries 5 / 10 / 15; only Master of Trades asks for the cap.
- **Job names must be one of the pack's twelve** (`miner`, `artisan`,
  `brewer`, `fighter`, `hunter`, `enchanter`, `explorer`, `farmer`, `cook`,
  `shaman`, `lumberjack`, `guardian`): the type warns about unknown names
  rather than failing, so a typo would be a silently uncompletable quest.
  Sympathetic Jobs also caps held jobs globally (`maxJobsGlobal` is 3 in the
  pack), which multi-job chapters must respect.
- The build compiles against `tensura_quests` from GitHub Packages, so every
  CI Gradle step needs the package read secret: the junit step now carries it
  alongside static, codeql and dependency-submission.
- Editing issue or PR text on Windows? Never pipe it through PowerShell 5.1
  (the console mangles non-ASCII, arrows arrive as mojibake): write a UTF-8
  file and use `gh --body-file` / `gh --input`.
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

- `ci.yml` runs the template's checks (junit, gametest, static - SpotBugs +
  Checkstyle + Spotless) plus the version doctrine.
- **PRs never touch `gradle.properties`** (neoforge-mod-template#81): the
  `-alpha.N` slot is owned by CI/CD. `version-bump-post-merge.yml` bumps it on
  every real push to `main` (`.github/**`-only pushes are ignored) and
  dispatches `release.yml`, which cuts `v<version>` and kicks the publishing
  workflows. Clean and beta bumps stay owner-driven isolated PRs, which is the
  only shape of `gradle.properties` change CI accepts beside nothing else.
- `publish.yml` uploads the artifact to the org's GitHub Packages registry for
  the released version.
