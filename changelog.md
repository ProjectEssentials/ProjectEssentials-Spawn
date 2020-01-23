# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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
