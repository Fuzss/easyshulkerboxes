# Easy Shulker Boxes

A Minecraft mod. Downloads can be found on [CurseForge](https://www.curseforge.com/members/fuzs_/projects)
and [Modrinth](https://modrinth.com/user/Fuzs).

![](https://i.imgur.com/vz0tBff.png)

## Outline

Easy Shulker Boxes offers some great features to supercharge using items with an inventory directly from your inventory
without ever having to open the item's dedicated container. Those features are:

- Item tooltips with a rich preview of all inventory contents
- Bundle-like behavior for inserting and extracting individual item stacks
- Scroll through all items on the item's tooltip to choose which item to extract next
- A tooltip next to the container item's tooltip for the item you have currently scrolled to in the container, great for
  telling apart tools with different enchantments!
- Drag above other items in your inventory while holding a container item to add all those items to its inventory, drag
  above empty slots to extract items from the container

By default, all those features are enabled for all compatible vanilla items.
This works via data packs, where a single provider for Easy Shulker Boxes' features must be included per item.

This way items from mods (mainly backpacks and larger shulker boxes) can be supported, too, when providers are added. Of
course not all mods will work, this depends on how the mod stores an item's inventory contents.

Default mod support in Easy Shulker Boxes is available for the following mods: Bag Of Holding (Forge+Fabric), Inmis (
Fabric), Reinforced Shulker Boxes (Fabric), Simple Backpack (Fabric), Backpacked (Forge).

Note that providers only support the default config settings from those mods for backpack sizes, if you change those
config settings, you'll have to manually update providers with the correct values with your own data pack.

## Enabling item container providers via data packs

All providers are found in `data/<modId>/item_container_providers/<itemName>.json` with `<modId>:<itemName>`
representing the item identifier. Go check out the default providers available in the Easy Shulker Boxes' jar when
making your own.

Depending on the kind of item that the provider is for, different provider types are available:

### Item Provider Type: `easyshulkerboxes:item`

This type is used for normal items that simply store an inventory, and in most cases show the corresponding screen when
right-clicked from the hotbar. This provider is intended for backpack items from other mods.

**Examples:** None in vanilla

<details>
<summary>Available string keys</summary>

| Key                      | Required | Description                                                                                                                                                                                                                    |
|--------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `inventory_width`        | `true`   | Inventory slots width (amount of columns in the item's container screen, e.g. 9 for a simple chest).                                                                                                                           |
| `inventory_height`       | `true`   | Inventory slots height (amount of rows in the item's container screen, e.g. 3 for a simple chest).                                                                                                                             |
| `background_color`       | `false`  | The background color used on the item tooltip, defaults to vanilla's gray container background color.                                                                                                                          |
| `nbt_key`                | `false`  | The string key used in the item nbt tag to store inventory contents, defaults to `Items`. This is treated like a path with parts separated by `/` in case the inventory contents tag is not on the root level of the item tag. |
| `disallowed_items`       | `false`  | Json array of items and item tags included by their internal identifier not allowed to be put into the container belonging to this item. Empty by default.                                                                     |
| `filter_container_items` | `false`  | Are shulker boxes (and similar modded items) **NOT** allowed to be put into the container belonging to this item, defaults to `false`.                                                                                         |

</details>

### Item Provider Type: `easyshulkerboxes:block_entity`

This type is used for block items that provide a block entity when placed down. The item inventory can only be
interacted with (adding/removing items) in creative mode by default.

**Examples:** `minecraft:shulker_box`, `minecraft:hopper`

<details>
<summary>Available string keys</summary>

| Key                      | Required | Description                                                                                                                                                                                                                    |
|--------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `inventory_width`        | `true`   | Inventory slots width (amount of columns in the item's container screen, e.g. 9 for a simple chest).                                                                                                                           |
| `inventory_height`       | `true`   | Inventory slots height (amount of rows in the item's container screen, e.g. 3 for a simple chest).                                                                                                                             |
| `block_entity_type`      | `true`   | The block entity type id.                                                                                                                                                                                                      |
| `background_color`       | `false`  | The background color used on the item tooltip, defaults to vanilla's gray container background color.                                                                                                                          |
| `nbt_key`                | `false`  | The string key used in the item nbt tag to store inventory contents, defaults to `Items`. This is treated like a path with parts separated by `/` in case the inventory contents tag is not on the root level of the item tag. |
| `disallowed_items`       | `false`  | Json array of items and item tags included by their internal identifier not allowed to be put into the container belonging to this item. Empty by default.                                                                     |
| `filter_container_items` | `false`  | Are shulker boxes (and similar modded items) **NOT** allowed to be put into the container belonging to this item, defaults to `false`.                                                                                         |
| `any_game_mode`          | `false`  | Can the player interact with the item's inventory in any game mode, not just creative, defaults to `false`. This is enabled for the built-in shulker box providers.                                                            |

</details>

### Item Provider Type: `easyshulkerboxes:block_entity_view`

This type is the same as the normal block entity provider, but does not allow for any interaction with the item's
inventory contents, not even in creative mode. It is simply used for viewing inventory contents of items with certain
restrictions on their inventory slots (like output and fuel slots).

**Examples:** `minecraft:furnace`, `minecraft:brewing_stand`

<details>
<summary>Available string keys</summary>

| Key                      | Required | Description                                                                                                                                                                                                                    |
|--------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `inventory_width`        | `true`   | Inventory slots width (amount of columns in the item's container screen, e.g. 9 for a simple chest).                                                                                                                           |
| `inventory_height`       | `true`   | Inventory slots height (amount of rows in the item's container screen, e.g. 3 for a simple chest).                                                                                                                             |
| `block_entity_type`      | `true`   | The block entity type id.                                                                                                                                                                                                      |
| `background_color`       | `false`  | The background color used on the item tooltip, defaults to vanilla's gray container background color.                                                                                                                          |
| `nbt_key`                | `false`  | The string key used in the item nbt tag to store inventory contents, defaults to `Items`. This is treated like a path with parts separated by `/` in case the inventory contents tag is not on the root level of the item tag. |
| `disallowed_items`       | `false`  | Json array of items and item tags included by their internal identifier not allowed to be put into the container belonging to this item. Empty by default.                                                                     |
| `filter_container_items` | `false`  | Are shulker boxes (and similar modded items) **NOT** allowed to be put into the container belonging to this item, defaults to `false`.                                                                                         |

</details>

### Item Provider Type: `easyshulkerboxes:ender_chest`

This type is used for blocks and items providing access to a player's ender chest.

**Examples:** `minecraft:ender_chest`

There are no additional settings available for this type.

### Item Provider Type: `easyshulkerboxes:bundle`

This type is used for bundle items. Instead of specifying inventory dimensions, the capacity of the bundle must be set.
Shulker boxes (and similar modded items) **CANNOT** be added to the bundle inventory.

**Examples:** `minecraft:bundle`

<details>
<summary>Available string keys</summary>

| Key                      | Required | Description                                                                                                                                                                                                                    |
|--------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `capacity`               | `true`   | Total capacity of the bundle (the available weight), is 64 for the vanilla bundle.                                                                                                                                             |
| `background_color`       | `false`  | The background color used on the item tooltip, defaults to vanilla's gray container background color.                                                                                                                          |
| `nbt_key`                | `false`  | The string key used in the item nbt tag to store inventory contents, defaults to `Items`. This is treated like a path with parts separated by `/` in case the inventory contents tag is not on the root level of the item tag. |
| `disallowed_items`       | `false`  | Json array of items and item tags included by their internal identifier not allowed to be put into the container belonging to this item. Empty by default.                                                                     |

</details>

## Adding custom item container provider types (for developers only)

Easy Shulker Boxes features a very basic api allowing developers to create and register their own item container
provider types for supporting items where the default provider types are not sufficient.

To do so, create your own provider by
implementing `fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProvider`. The provider itself is **NOT**
registered anywhere.

Instead, make sure to add a serializer for your provider, so Easy Shulker Boxes knows how to handle the provider type
when deserializing data pack contents.
This is done by calling `fuzs.easyshulkerboxes.api.world.item.container.ItemContainerProviderSerializers::register`.

Also note that it is not necessary for an `ItemContainerProvider` to provide all the functions Easy Shulker Boxes has to
offer. It is entirely possible to e.g. only implement a provider for showing an item tooltip image (for an example
see `fuzs.easyshulkerboxes.world.item.container.MapProvider`).

An example for registering a mod specific provider can be found
here: `fuzs.easyshulkerboxes.integration.inmis.InmisProvider` (this provider is necessary as Inmis does not use
vanilla's nbt format for storing an item inventory, and also to prevent Inmis backpack nesting).
