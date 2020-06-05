# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Since 2.0.0 versions change log same for all supported minecraft versions.

## [Unreleased]

## [2.0.0-RC.1] - 2020-06-05

### Changed
- Core dependency updated.
- Localization updated.
- Applying localization strategy changed.

### Fixed
- Localization and provider registering fixed.
- Incorrect spawn configuration saving fixed.

## [2.0.0-SNAPSHOT.1] - 2020-06-02

### Added
- Add Chinese simplified translation.
- `gradle-wrapper.jar` added.
- `updatev2.json` added.
- `PULL_REQUEST_TEMPLATE.md` added.
- `SpawnConfigurationModel.kt` added.
- `SpawnConfiguration.kt` implemented.
- Base `ModuleObject.kt` implemented.
- `firstLaunch` assigning added.

### Changed
- `.gitignore` synced with core module.
- `gradle.properties` synced with basic module.
- `build.gradle` synced with basic module.
- `FUNDING.yml` updated.
- `bug_report.md` updated.
- Update file end-point now is v2.
- Core required version now `2.X`.
- Credits in `mods.toml` updated.
- Description in `mods.toml` updated.
- `SpawnConfiguration.kt` path changed.
- Position now has integer type.
- Spawn command re-written.
- `set-spawn` command re-written.

### Removed
- Internal assets removed.
- An internal documentation removed.
- `Messaging.kt` removed.
- `at` path removed.
- `brigadier_version` removed.
- `EntryPoint.kt` removed.

## [1.15.2-1.1.0] - 2020-03-18

### Added
- Project Essentials dependencies added to `build.gradle`.
- Respawning at first home \ bed point \ world spawn added.
- Back command compatibility added for respawning.
- Back command compatibility added for spawn command.
- `Messaging.kt` with `sendMessage` method added.
- Compatibility with new localization API.

### Changed
- Kotlin version updated to `1.3.70`.
- KotlinX Serialization updated to `0.20.0`.
- Forge API version updated to `28.2.0`.
- `SetSpawnCommand.kt`: `sendMsg` replaced with `sendMessage`.
- `SetSpawnCommand.kt`: deprecated api replaced on new.
- `SpawnCommand.kt`: `sendMsg` replaced with `sendMessage`.
- `SpawnCommand.kt`: deprecated api replaced on new.

### Removed
- Essentials dependencies removed from `gradle.properties`.
- `curseforge` maven repository removed from repositories in `build.gradle`.

## [1.14.4-1.2.0] - 2020-03-18

### Added
- Project Essentials dependencies added to `build.gradle`.
- Respawning at first home \ bed point \ world spawn added.
- Back command compatibility added for respawning.
- Back command compatibility added for spawn command.
- `Messaging.kt` with `sendMessage` method added.
- Compatibility with new localization API.

### Changed
- Kotlin version updated to `1.3.70`.
- KotlinX Serialization updated to `0.20.0`.
- Forge API version updated to `28.2.0`.
- `SetSpawnCommand.kt`: `sendMsg` replaced with `sendMessage`.
- `SetSpawnCommand.kt`: deprecated api replaced on new.
- `SpawnCommand.kt`: `sendMsg` replaced with `sendMessage`.
- `SpawnCommand.kt`: deprecated api replaced on new.

### Removed
- Essentials dependencies removed from `gradle.properties`.
- `curseforge` maven repository removed from repositories in `build.gradle`.

## [1.15.2-1.0.0] - 2020-02-08

### Added
- Initial release.

## [1.14.4-1.1.2] - 2020-02-08

### Changed
- Uses `permissionAPIClassPath` from CoreAPI.
- Uses `cooldownAPIClassPath` from CoreAPI.

## [1.14.4-1.1.1] - 2020-01-23

### Added
- German support by [@BixelPitch](https://github.com/BixelPitch).
- Checking on availability cooldown and permissions modules.
- Compatibility with 1.1.0 and 1.1.0 permissions \ core module versions.

### Changed
- Gradle wrapper updated to `5.6.4`.
- `Kotlin` and `KotlinX Serialization` updated.
- All essentials dependencies updated.
- Variable naming in gradle.properties improved.
- Version number updated.
- Version number updated in `mods.toml` and `EntryPoint.kt`.
- Usings `jsonInstance` from core module.

### Fixed
- Double slash for command in logs.
- Package naming.

### Removed
- Redundant logger messages.
- Java plugin from buildscript.
- Permissions module from mandatory dependencies.
- `UseExperimental` annotation for `SpawnModelBase` class.
- `createConfigDirs` method in `SpawnModelBase` class.

## [1.14.4-1.1.0 ~~.0~~] - 2019-10-12

### Added
- Logging loaded spawn data.
- ProjectEssentials-Cooldown as dependencity.
- Assigning aliases for Cooldown module.

## [1.14.4-1.0.1 ~~.0~~] - 2019-10-07

### Changed
- Small mod behavior improvements.

## [1.14.4-1.0.0 ~~.2~~] - 2019-10-06

### Changed
- Project Essentials Permissions API dependency updated.

## [1.14.4-1.0.0 ~~.1~~] - 2019-10-06

### Fixed
- Some wrong meta information for mod.

## [1.14.4-1.0.0 ~~.0~~] - 2019-10-06

### Added
- Initial release of Project Essentials Spawn as Project Essentials part.
