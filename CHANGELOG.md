# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog].

## [v4.1.0-1.19.1] - 2022-07-30
- Compiled for Minecraft 1.19.1
- Updated to Puzzles Lib v4.1.0

## [v4.0.3-1.19] - 2022-07-13
- Fully compatible with Forge 41.0.98+ which is also now required

## [v4.0.2-1.19] - 2022-07-10
### Changed
- Added a check for the mod to only work on shulker boxes with a stack size of one to prevent item duplication with some mods that allow for empty shulker box items to stack (such as the Carpet mod)

## [v4.0.1-1.19] - 2022-07-09
### Changed
- Slightly reduced mod loader specific code by moving some parts to Puzzles Lib 

## [v4.0.0-1.19] - 2022-07-06
- Ported to Minecraft 1.19
- Split into multi-loader project

[Keep a Changelog]: https://keepachangelog.com/en/1.0.0/