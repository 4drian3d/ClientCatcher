# ClientCatcher
[![WorkFlow Status](https://img.shields.io/github/actions/workflow/status/4drian3d/ClientCatcher/ClientCatcherBuild.yml)](https://github.com/4drian3d/ClientCatcher/actions/workflows/ClientCatcherBuild.yml)
[![Version](https://img.shields.io/github/v/release/4drian3d/ClientCatcher?color=FFF0&style=flat-square)](https://modrinth.com/mod/clientcatcher)
[![Discord](https://img.shields.io/discord/899740810956910683?color=7289da&label=Discord)](https://discord.gg/5NMMzK5mAn)

[![Banner](https://i.imgur.com/6rjflSj.jpg)](https://modrinth.com/mod/clientcatcher)

Simple Velocity plugin to get the client with which a player has connected to your server

The plugin is not fully effective, several malicious clients hide their client branding when entering the server or impersonate vanilla clients.
However, the plugin can detect several clients such as Forge, Fabric, LiteLoader, Lunar, Vanilla, and others.
It also detects Forge 1.7.10 - 1.12.2 mods.

## Installation
- Install the latest version of [MCKotlin](https://modrinth.com/plugin/mckotlin) for Velocity
- Download ClientCatcher from [Modrinth](https://modrinth.com/mod/clientcatcher)
- Drag and drop on your plugins folder
- Start the server

### Commands
##### Permission: `clientcatcher.command`

#### Client `/clientcatcher client <player>`
##### Permission: `clientcatcher.command.client`

Shows you a player's client and mods, if any are installed

#### Mods `/clientcatcher mods <player>`
##### Permission: `clientcatcher.command.mods`

Shows you the player's mods, in case he has any installed

#### Reload `/clientcatcher reload`
##### Permission: clientcatcher.command.reload

Reloads the plugin
