package me.adrianed.clientcatcher.velocity.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType.*
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandSource
import me.adrianed.clientcatcher.velocity.ClientCatcher
import me.adrianed.clientcatcher.velocity.asMiniMessage

fun register(manager: CommandManager, plugin: ClientCatcher) {
    val command = BrigadierCommand(literal<CommandSource>("clientcatcher")
        .then(literal<CommandSource>("reload")
            .executes {
                plugin.loadConfig().thenApplyAsync { result ->
                    if (result) plugin.messages.reload.successfully.asMiniMessage()
                    else plugin.messages.reload.error.asMiniMessage()
                }.thenAcceptAsync(it.source::sendMessage)
                Command.SINGLE_SUCCESS
            }
        )
        .then(literal<CommandSource>("client")
            .then(playerArgument()
                .executes {
                    plugin.proxyServer.getPlayer(getString(it, "player"))
                        .ifPresentOrElse({ player ->
                            if (player.modInfo.isPresent) {
                                it.source.sendMessage(plugin.messages.command.client.withMods.asMiniMessage())
                            } else {
                                it.source.sendMessage(plugin.messages.command.client.client.asMiniMessage())
                            }
                        }, {
                            it.source.sendMessage(plugin.messages.command.unknownPlayer.asMiniMessage())
                        })
                    Command.SINGLE_SUCCESS
                }
            )
        )
        .then(literal<CommandSource>("mod")
            .then(playerArgument()
                .executes {
                    plugin.proxyServer.getPlayer(getString(it, "player"))
                        .ifPresentOrElse({ player ->
                            if (player.modInfo.isPresent) {
                                it.source.sendMessage(plugin.messages.command.mods.modsFound.asMiniMessage())
                            } else {
                                it.source.sendMessage(plugin.messages.command.mods.notModsFound.asMiniMessage())
                            }
                        }, {
                            it.source.sendMessage(plugin.messages.command.unknownPlayer.asMiniMessage())
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
