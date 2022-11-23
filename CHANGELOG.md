# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog].

## [v4.3.4-1.19.2] - 2022-11-23
### Changed
- Reversed tooltip scrolling direction
### Fixed
- Fixed block entity data other from items being deleted when interacting with the item stack in your inventory

## [v4.3.3-1.19.2] - 2022-10-22
### Changed
- When extracting an item from a shulker box/ender chest in an inventory menu via right-clicking the last item is no longer the one taken out first. Instead, use the scroll wheel to choose which item you want to take from the shulker box/ender chest.

## [v4.3.2-1.19.2] - 2022-09-15
### Fixed
- Fixed compatibility with Numismatic Overhaul mod

## [v4.3.1-1.19.2] - 2022-09-01
### Added
- Added a `+` indicator that is shown on shulker boxes, ender chests and bundles when the stack carried by the cursor can be added to them in your inventory

## [v4.3.0-1.19.2] - 2022-08-21
- Compiled for Minecraft 1.19.2
- Updated to Puzzles Lib v4.2.0

## [v4.2.1-1.19.1] - 2022-08-06
### Fixed
- Fixed Cardinal Components not being set as a proper dependency in `fabric.mod.json`

## [v4.2.0-1.19.1] - 2022-08-06
### Added
- Inventory interactions and tooltips now work on ender chests
- Added server config to disable individual interactions

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