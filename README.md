# ClientCatcher
[![WorkFlow Status](https://img.shields.io/github/actions/workflow/status/4drian3d/ClientCatcher/ClientCatcherBuild.yml)](https://github.com/4drian3d/ClientCatcher/actions/workflows/ClientCatcherBuild.yml)
[![Version](https://img.shields.io/github/v/release/4drian3d/ClientCatcher?color=FFF0&style=flat-square)](https://github.com/4drian3d/ClientCatcher/releases)
[![Discord](https://img.shields.io/discord/899740810956910683?color=7289da&label=Discord)](https://discord.gg/5NMMzK5mAn)
![Github Downloads](https://img.shields.io/github/downloads/4drian3d/ClientCatcher/total?logo=GitHub&style=flat-square)
![Modrinth Downloads](https://img.shields.io/modrinth/dt/Dhqd1a7j?logo=Modrinth&style=flat-square)


Simple Velocity plugin to get the client with which a player has connected to your server

The plugin is not fully effective, several malicious clients hide their client branding when entering the server or impersonate vanilla clients.
However, the plugin can detect several clients such as Forge, Fabric, LiteLoader, Lunar, Vanilla, and others.
It also detects Forge 1.7.10 - 1.12.2 mods.

If you want to use ClientCatcher in Fabric, you can try https://modrinth.com/mod/clientcatcher-fabric

[![](https://www.bisecthosting.com/partners/custom-banners/6fa909d5-ad2b-42c2-a7ec-1c51f8b6384f.webp)](https://www.bisecthosting.com/4drian3d)

## Commands
#### Permission: `clientcatcher.command`

### Client `/clientcatcher client <player>`
#### Permission: `clientcatcher.command.client`

Shows you a player's client and mods, if any are installed

### Mods `/clientcatcher mods <player>`
#### Permission: `clientcatcher.command.mods`

Shows you the player's mods, in case he has any installed

### Reload `/clientcatcher reload`
#### Permission: clientcatcher.command.reload

Reloads the plugin

## Placeholders

- `<clientcatcher_client>`
- `<clientcatcher_mods>`
- `<clientcatcher_player_client:(player name)>`
- `<clientcatcher_player_mods:(player name)>`