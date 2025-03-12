![ClientCatcher](https://www.bisecthosting.com/images/CF/CLIENT_CATCHER/CLIENT_CATCHER_Header.webp)

[![WorkFlow Status](https://img.shields.io/github/actions/workflow/status/4drian3d/ClientCatcher/ClientCatcherBuild.yml)](https://github.com/4drian3d/ClientCatcher/actions/workflows/ClientCatcherBuild.yml)
[![Version](https://img.shields.io/github/v/release/4drian3d/ClientCatcher?color=FFF0&style=flat-square)](https://github.com/4drian3d/ClientCatcher/releases)
![](https://img.shields.io/github/downloads/4drian3d/ClientCatcher/total?logo=GitHub&style=flat-square)
[![Discord](https://img.shields.io/discord/899740810956910683?color=7289da&label=Discord)](https://discord.gg/5NMMzK5mAn)
[![Telegram](https://img.shields.io/badge/Telegram-Support-229ED9)](https://t.me/Adrian3dSupport)
[![Telegram](https://img.shields.io/badge/Telegram-Updates-229ED9)](https://t.me/Adrian3dUpdates)

![Description Banner](https://www.bisecthosting.com/images/CF/CLIENT_CATCHER/CLIENT_CATCHER_Description.webp)

Simple Velocity plugin to get the client with which a player has connected to your server

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

## Permissions

#### Bypass Client Detection

`clientcatcher.bypass.brand`

#### Bypass Mod Detection

`clientcatcher.bypass.mods`

![Instalation Banner](https://www.bisecthosting.com/images/CF/CLIENT_CATCHER/CLIENT_CATCHER_Installation.webp)

- Download it from Modrinth
- Download MCKotlin
- Put both plugins in your Velocity proxy
- And configure the plugin to block the mods and clients of your choice

![FAQ Banner](https://www.bisecthosting.com/images/CF/CLIENT_CATCHER/CLIENT_CATCHER_Faq.webp)

The plugin is not fully effective, several malicious clients hide their client branding when entering the server or impersonate vanilla clients.
However, the plugin can detect several clients such as Forge, Fabric, LiteLoader, Lunar, Vanilla, and others.

It also detects Forge 1.7.10 - 1.12.2 mods.

If you want to use ClientCatcher in Fabric, you can try https://modrinth.com/mod/clientcatcher-fabric

## Placeholders

ClientCatcher provides several placeholders for use in other [MiniPlaceholders](https://modrinth.com/plugin/miniplaceholders)-compatible plugins

- `<clientcatcher_client>`
- `<clientcatcher_mods>`
- `<clientcatcher_player_client:(player name)>`
- `<clientcatcher_player_mods:(player name)>`

## Discord WebHook Configuration

If you have a Discord server on which you want to show alerts of which clients your players are using, you can activate the webhooks module and configure it like this

1. Copy the URL of the WebHook.
2. Extract the ID and Token from the URL.
3. Put those fields in the specific configuration options.
4. Restart your proxy

![Final Webhook result](https://cdn.modrinth.com/data/cached_images/0fe108e5ed7a5ecd33ee67c47798deff5c4d58e5.png)

[![Support Server](https://www.bisecthosting.com/images/CF/CLIENT_CATCHER/CLIENT_CATCHER_Discord.webp)](https://discord.gg/5NMMzK5mAn)

[![Bisect Promo](https://www.bisecthosting.com/images/CF/CLIENT_CATCHER/CLIENT_CATCHER_Promo.webp)](https://www.bisecthosting.com/4drian3d)