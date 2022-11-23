# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog].

## [v4.3.4-1.19.2] - 2022-11-23
### Changed
- For Easy Shulker Boxes to support an item it must now be manually registered, this means only vanilla items (shulker boxes, ender chests, bundles) will be supported by default, mainly to prevent larger shulker boxes from other mods from breaking
- Other mods can manually register their content though for support
- Tooltips now have a thicker border, so they look more pretty (actually had to redo most of that rendering code so this could work, oof)
- Reverse tooltip scrolling direction
- Ender Chests have a fancy custom tooltip color
### Fixed
- Fixed shulker box block entity data other than stored items being deleted when interacting with the item stack in your inventory (this ifx allows for compatibility with mods that e.g. add shulker box enchantments)
### Removed
- Removed bundle-like tutorial banners when using newly added item interactions for the first time

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