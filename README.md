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
But this can be changed. It's possible to customize responses based on either **all** or **only certain flags** in which you can **send chat messages, an actionbar, title/subtitle, or play sounds** all with [**MiniMessage**](https://docs.papermc.io/adventure/minimessage/) and [**PlaceholderAPI**](https://www.spigotmc.org/resources/placeholderapi.6245/) support included.
<br>
<br>
Everything can be done in-game via commands and are **fully tab-completable**, which will certainly help.
<br>
<br>
Additionally, this plugin relies on **no additional plugin or library** in order to work. Simply take a **stone axe**, enter **creative mode** and select the area you wish to protect, nothing more.
<br>
<br>
The idea is to make the plugin as **easy to use** as possible, without adding too many things.

<br>

## How it works and what it offers!
#### Create a region
> Go to **creative mode** and hold a **stone axe**. **Right click on a block** to select one point and **left click on another block** to select the other point of your area. Then execute the command `/rg create <region>`.

<br>

#### Make a region with no height/dept limit (ignore Y axis)
> `/rg ignore-y <region> true/false`
> <br>
> **region** = Name of your region
> <br>
> **true** = Region area is not a cube anymore, making the **Y axis** for the area infinite.
> <br>
> **false** = Region area becomes a cube, making the **Y axis** limited.

<br>

#### View information on a region
> `/rg info`
> <br>
> Fetches the information on the current region the executor of the command is standing on.
> <br>
> <br>
> `/rg info <region>`
> <br>
> **region** = Name of your region
> <br>
> Fetches the information on the specified region.

<br>

#### Manage a region flag
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
> <br>
> `/rg flag test break deny`
> <br>
> `/rg flag test break allow grass_block`

<br>

#### Change a response
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

## What flags do exist?


**Flag**: Name of the flag.
<br>
**Default state**: Allowed or disallowed by default when creating a region.
<br>
**Specifiable**: If the flag state can be specified for a certain entity/item/block only.
<br>

| Flag                 | Default State | Specifiable |
|----------------------|---------------|-------------|
| ENTER                | allowed       | yes         |
| LEAVE                | allowed       | yes         |
| PLACE                | denied        | yes         |
| BREAK                | denied        | yes         |
| PISTON               | denied        | yes         |
| EXPLODE_BLOCKS       | denied        | yes         |
| FIRE_SPREAD          | denied        | no          |
| INTERACT_BLOCK       | denied        | yes         |
| TRAMPLE_CROPS        | denied        | no          |
| FLOW                 | denied        | yes         |
| PROJECTILE           | allowed       | yes         |
| BUCKET_FILL          | denied        | yes         |
| BUCKET_EMPTY         | denied        | yes         |
| MILK_ENTITY          | denied        | yes         |
| HUNGER               | denied        | no          |
| PVP                  | denied        | no          |
| PVE                  | denied        | yes         |
| INTERACT_ENTITY      | allowed       | yes         |
| INTERACT_ITEM        | allowed       | yes         |
| FISHING              | denied        | yes         |
| DROP                 | denied        | yes         |
| PICKUP               | denied        | yes         |
| FALLING_BLOCK_DAMAGE | denied        | no          |
| FALL_DAMAGE          | denied        | no          |
| BURN_DAMAGE          | denied        | no          |
| DROWNING_DAMAGE      | denied        | no          |
| MONSTER_SPAWN        | denied        | yes         |
| ANIMAL_SPAWN         | denied        | yes         |

<br>

## Permission to bypass regions/flags:

Bypass all regions/flags:
<br>
`proregions.bypass`

Bypass all flags for a region:
<br>
`proregions.bypass.<region>`
<br>
`e.g: proregions.bypass.spawn`

Bypass a certain flag for a region:
<br>
`proregions.bypass.<region>.<flag>`
<br>
`e.g: proregions.bypass.spawn.place`

Bypass a flag for a certain specification for a region:
<br>
`proregions.bypass.<region>.<flag>.<specification>`
<br>
`e.g: proregions.bypass.spawn.place.dirt`

<br>

## Permission for commands:

| Command            | Permission                  |
|--------------------|-----------------------------|
| **admin commands** | proregions.use              |
| create             | proregions.command.create   |
| delete             | proregions.command.delete   |
| flag               | proregions.command.flag     |
| ignorey            | proregions.command.ignorey  |
| info               | proregions.command.info     |
| list               | proregions.command.list     |
| reload             | proregions.command.reload   |
| response           | proregions.command.response |

<br>

# PlaceholderAPI placeholders

List of all regions:
> IN: `%proregions_regions%`
> <br>
> OUT: `region1, region2, region3, region4`

<br>

List of all regions a player is in:
> IN: `%proregions_player_regions%`
> <br>
> OUT: `region1, region3`

<br>

If a placeholder is inside a region:
> IN: `%proregions_inside_<region>_<true>_<false>%`
> <br>
> <br>
> e.g: `%proregions_inside_spawn_Inside spawn_No`
> <br>
> OUT: `You are in spawn`
> <br>
> OUT: `No`

<br>

# Developer API

Implement the API to your project:
```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/rayzsyt/proregions</url>
    </repository>
</repositories>


<dependencies>
    <!-- LATEST VERSION: https://github.com/RayzsYT/ProRegions/packages/2878076 -->
    <dependency>
        <groupId>de.rayzs.proregions</groupId>
        <artifactId>proregions-api</artifactId>
        <version>LATEST-API-VERSION</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

I'll just show it with a simple example code.

```java
// Bukkit imports
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

// ProRegions imports
import de.rayzs.proregions.api.ProRegions;
import de.rayzs.proregions.api.ProRegionsAPI;

import de.rayzs.proregions.api.events.PlayerEnterRegionEvent;
import de.rayzs.proregions.api.events.PlayerLeaveRegionEvent;

import de.rayzs.proregions.api.region.Region;

public class Example extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Get API instance.
        ProRegionsAPI api = ProRegions.get();

        // Get region provider to manage/create/save regions.
        api.getRegionProvider();

        // get message provider to get messages
        // or send chat messages/titles and actionbar messages.
        api.getMessageProvider();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerEnterRegion(PlayerEnterRegionEvent event) {
        Player player = event.getPlayer();
        Region region = event.getRegion();

        // To cancel/disallow the event.
        event.setCancelled(true);

        // To disable the response.
        event.setSendResponse(false);

        player.sendMessage("You entered the region: " + region.getRegionName());
    }

    @EventHandler
    public void onPlayerLeaveRegion(PlayerLeaveRegionEvent event) {
        Player player = event.getPlayer();
        Region region = event.getRegion();

        // To cancel/disallow the event.
        event.setCancelled(true);

        // To disable the response.
        event.setSendResponse(false);

        player.sendMessage("You left the region: " + region.getRegionName());
    }
}

```