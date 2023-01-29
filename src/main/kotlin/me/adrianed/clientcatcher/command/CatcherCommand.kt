package me.adrianed.clientcatcher.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType.*
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandSource
import me.adrianed.clientcatcher.ClientCatcher
import me.adrianed.clientcatcher.asMiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

fun register(manager: CommandManager, plugin: ClientCatcher) {
    val command = BrigadierCommand(literal<CommandSource>("clientcatcher")
        .requires { it.hasPermission("clientcatcher.command") }
        .executes {
            it.source.sendMessage(plugin.messages.command.usage.asMiniMessage())
            Command.SINGLE_SUCCESS
        }
        .then(literal<CommandSource>("reload")
            .requires { it.hasPermission("clientcatcher.command.reload") }
            .executes {
                plugin.loadConfig().thenApplyAsync { result ->
                    if (result) plugin.messages.reload.successfully.asMiniMessage()
                    else plugin.messages.reload.error.asMiniMessage()
                }.thenAcceptAsync(it.source::sendMessage)
                Command.SINGLE_SUCCESS
            }
        )
        .then(literal<CommandSource>("client")
            .requires { it.hasPermission("clientcatcher.command.client") }
            .then(
                playerArgument()
                .executes { ctx ->
                    val name = getString(ctx, "player")
                    plugin.proxyServer.getPlayer(name)
                        .ifPresentOrElse({ player ->
                            ctx.source.sendMessage(
                                with(plugin.messages.command) {
                                    if (player.modInfo.isPresent) client.withMods.asMiniMessage(
                                        Placeholder.unparsed("player", player.username),
                                        Placeholder.unparsed("client", player.clientBrand ?: "UNKNOWN"),
                                        Placeholder.unparsed("mods",
                                            player.modInfo.get().mods.joinToString(", ") { "${it.id}:${it.version}" })
                                    )
                                    else client.client.asMiniMessage(
                                        Placeholder.unparsed("player", player.username),
                                        Placeholder.unparsed("client", player.clientBrand ?: "UNKNOWN")
                                    )
                                }
                            )
                        }, {
                            ctx.source.sendMessage(
                                plugin.messages.command.unknownPlayer.asMiniMessage(
                                    Placeholder.unparsed("name", name)
                                )
                            )
                        })
                    Command.SINGLE_SUCCESS
                }
            )
        )
        .then(literal<CommandSource>("mods")
            .requires { it.hasPermission("clientcatcher.command.mods") }
            .then(
                playerArgument()
                .executes { ctx ->
                    val name = getString(ctx, "player")
                    plugin.proxyServer.getPlayer(name)
                        .ifPresentOrElse({ player ->
                            ctx.source.sendMessage(
                                with(plugin.messages.command.mods) {
                                    if (player.modInfo.isPresent) modsFound.asMiniMessage(
                                        Placeholder.unparsed("player", player.username),
                                        Placeholder.unparsed("mods",
                                            player.modInfo.get().mods.joinToString(", ") { "${it.id}:${it.version}" })
                                    )
                                    else notModsFound.asMiniMessage(
                                        Placeholder.unparsed(
                                            "player",
                                            player.username
                                        )
                                    )
                                }
                            )

                        }, {
                            ctx.source.sendMessage(
                                plugin.messages.command.unknownPlayer.asMiniMessage(
                                    Placeholder.unparsed("name", name)
                                )
                            )
                        })
                    Command.SINGLE_SUCCESS
                }
            )
        )
        .build()
    )

    val meta = manager.metaBuilder(command)
        .plugin(plugin)
        .aliases("ccatcher")
        .build()

    manager.register(meta, command)
}

private fun playerArgument() = argument<CommandSource, String>("player", word())
