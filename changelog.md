# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.15.2-1.1.0] - 2020-03-18

## Added
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
