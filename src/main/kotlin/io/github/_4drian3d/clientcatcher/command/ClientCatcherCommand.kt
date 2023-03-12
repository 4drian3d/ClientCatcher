package io.github._4drian3d.clientcatcher.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandSource
import io.github._4drian3d.clientcatcher.ClientCatcher
import io.github._4drian3d.clientcatcher.asMiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import kotlin.jvm.optionals.getOrNull

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
                    plugin.proxyServer.getPlayer(name).getOrNull()?.let { player ->
                        ctx.source.sendMessage(
                            with(plugin.messages.command.mods) {
                                if (player.modInfo.isPresent) found.asMiniMessage(
                                    Placeholder.unparsed("player", player.username),
                                    Placeholder.unparsed("mods",
                                        player.modInfo.get().mods.joinToString(", ") { "${it.id}:${it.version}" })
                                )
                                else notFound.asMiniMessage(
                                    Placeholder.unparsed(
                                        "player",
                                        player.username
                                    )
                                )
                            }
                        )
                    } ?: {
                        ctx.source.sendMessage(
                            plugin.messages.command.unknownPlayer.asMiniMessage(
                                Placeholder.unparsed("name", name)
                            )
                        )
                    }
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
