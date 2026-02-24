**(!) This project is just for fun since I wanted to test my own abilities on how a region protection plugin might turn out if I were to do it. There's no guarantee for it to be actively maintained!** 
## About
**ProRegions** is a Minecraft plugin which allows you to protect certain areas of your world and decide what action is allowed and when exactly. Similar to the known plugin [**WorldGuard**](https://dev.bukkit.org/projects/worldguard) but on a much smaller scale with additional features,

<br>

## What makes this unique?
**ProRegions** has a small range of flags to work with, but they will certainly suffice.
<br> <br>
What makes it unique is how it interacts with the player. By default, there are **no** responses which makes it appear clean. 
<br>
<br>
But this can be changed. It's possible to customize responses based on either **all** or **only certain flags** in which you can **send a chat message, an actionbar, title/subtitle, play sounds** all with [**MiniMessage**](https://docs.papermc.io/adventure/minimessage/) and [**PlaceholderAPI**](https://www.spigotmc.org/resources/placeholderapi.6245/) support included.
<br>
<br>
Everything can be done in-game via commands and are **fully tab-completable**, which will certainly help.
<br>
<br>
Additionally, this plugin relies on **no additional plugin or library** in order to work. Simply take a **stone axe**, enter **creative mode** and select the area you wish to protect, nothing more.
<br>
<br>
The idea is to make it plugin as **easy to use** as possible, without adding too many things.

<br>

# How it works and what it offers!
### Create a region
> Go to **creative mode** and hold a **stone axe**. **Right click on a block** to select one point and **left click on another block** to select the other point of your area. Then execute the command `/rg create <region>`.

<br>

### Make a region with no height/dept limit (ignore Y axis)
> `/rg ignore-y true/false`
> <br>
> **true** = Region area is not a cube anymore, making the **Y axis** for the area infinite.
> <br>
> **false** = Region area becomes a cube, making the **Y axis** limited.

<br>

### View information on a region
> `/rg info`
> <br>
> Fetches the information on the current region the executor of the command is standing on.
> <br>
> <br>
> `/rg info <region>`
> <br>
> Fetches the information on the specified region.

<br>

### Manage a region flag
> `/rg flag <region> <flag> <state> (specification)`
> <br>
> **region** = Name of your region
> <br>
> **flag** = Your flag. (e.g: break, place, interact_item...)
> <br>
> **specification** = Specify the flag state to a certain item/entity/block only.
> <br>
> <br>
> **Example:**
> `/rg flag test break deny`
> `/rg flag test break allow grass_block`

<br>

### Change a response
> `/rg response <region> set/unset default/<flag> <type> <...>`
> <br>
> **region** = Name of your region
> <br>
> **set/unset** = To set or remove a response and its associated type
> <br>
> **default/flag** = By default for all or only a certain flag.
> <br>
> **type** = Your response type. (e.g: chat/actionbar/title/subtitle/sound volume pitch)
> <br>
> <br>
> **Example:**
> <br>
> `/rg response test set default chat &cYou can't do that!`
> <br>
> `/rg response test set break actionbar &cYou cannot break blocks mate!`
> <br>
> `/rg response test set fishing sound entity.ender_dragon_growl 1.0 0.2`

<br>

### What flags do exist?


**Flag**: Name of the flag.
<br>
**Default state**: Allowed or disallowed by default when creating a region.
<br>
**Specifiable**: Refers to if the flag can be allowed/disallowed on for certain items/materials etc...
<br>

| Flag               | Default State | Specifiable |
|-------------------------|--------------|-------------|
| PLACE                   | DENY         | yes        |
| BREAK                   | DENY         | yes        |
| PISTON                  | DENY         | yes        |
| EXPLODE_BLOCKS          | DENY         | yes        |
| FIRE_SPREAD             | DENY         | no       |
| INTERACT_BLOCK          | DENY         | yes        |
| TRAMPLE_CROPS           | DENY         | no       |
| FLOW                    | DENY         | yes        |
| PROJECTILE              | ALLOW        | yes        |
| BUCKET_FILL             | DENY         | no       |
| BUCKET_EMPTY            | DENY         | no       |
| MILK_ENTITY             | DENY         | no       |
| HUNGER                  | DENY         | no       |
| PVP                     | DENY         | no       |
| PVE                     | DENY         | yes        |
| INTERACT_ENTITY         | ALLOW        | yes        |
| INTERACT_ITEM           | ALLOW        | yes        |
| FISHING                 | DENY         | yes        |
| DROP                    | DENY         | yes        |
| PICKUP                  | DENY         | yes        |
| FALLING_BLOCK_DAMAGE    | DENY         | no       |
| FALL_DAMAGE             | DENY         | no       |
| BURN_DAMAGE             | DENY         | no       |
| DROWNING_DAMAGE         | DENY         | no       |
| MONSTER_SPAWN           | DENY         | yes        |
| ANIMAL_SPAWN            | DENY         | yes        |