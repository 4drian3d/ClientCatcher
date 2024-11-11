package io.github._4drian3d.clientcatcher.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.word
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.BrigadierCommand.*
import com.velocitypowered.api.command.CommandManager
import io.github._4drian3d.clientcatcher.ClientCatcher
import io.github._4drian3d.clientcatcher.sendMini
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import kotlin.jvm.optionals.getOrNull

class ClientCatcherCommand(
    private val manager: CommandManager,
    private val plugin: ClientCatcher
) {
    fun register() {
        val command = BrigadierCommand(literalArgumentBuilder("clientcatcher")
            .requires { it.hasPermission("clientcatcher.command") }
            .executes {
                it.source.sendMini(plugin.messages.command.usage, plugin.componentLogger)
                Command.SINGLE_SUCCESS
            }
            .then(literalArgumentBuilder("reload")
                .requires { it.hasPermission("clientcatcher.command.reload") }
                .executes { ctx ->
                    plugin.loadConfig().thenApply { result ->
                        if (result) plugin.messages.reload.successfully
                        else plugin.messages.reload.error
                    }.thenAccept { ctx.source.sendMini(it, plugin.componentLogger) }
                    Command.SINGLE_SUCCESS
                }
            )
            .then(literalArgumentBuilder("client")
                .requires { it.hasPermission("clientcatcher.command.client") }
                .then(
                    playerArgument()
                        .executes { ctx ->
                            val name = getString(ctx, "player")
                            plugin.proxyServer.getPlayer(name).getOrNull()?.let { player ->
                                with(plugin.messages.command) {
                                    if (player.modInfo.isPresent) ctx.source.sendMini(client.withMods, plugin.componentLogger,
                                        Placeholder.unparsed("player", player.username),
                                        Placeholder.unparsed("client", player.clientBrand ?: "UNKNOWN"),
                                        Placeholder.unparsed("mods",
                                            player.modInfo.get().mods.joinToString(", ") { "${it.id}:${it.version}" })
                                    )
                                    else ctx.source.sendMini(client.client, plugin.componentLogger,
                                        Placeholder.unparsed("player", player.username),
                                        Placeholder.unparsed("client", player.clientBrand ?: "UNKNOWN")
                                    )
                                }
                            } ?: {
                                ctx.source.sendMini(plugin.messages.command.unknownPlayer,
                                    plugin.componentLogger, Placeholder.unparsed("name", name))
                            }
                            Command.SINGLE_SUCCESS
                        }
                )
            )
            .then(literalArgumentBuilder("mods")
                .requires { it.hasPermission("clientcatcher.command.mods") }
                .then(
                    playerArgument()
                        .executes { ctx ->
                            val name = getString(ctx, "player")
                            plugin.proxyServer.getPlayer(name).getOrNull()?.let { player ->
                                with(plugin.messages.command.mods) {
                                    if (player.modInfo.isPresent) ctx.source.sendMini(found, plugin.componentLogger,
                                        Placeholder.unparsed("player", player.username),
                                        Placeholder.unparsed("mods",
                                            player.modInfo.get().mods.joinToString(", ") { "${it.id}:${it.version}" })
                                    )
                                    else ctx.source.sendMini(notFound,
                                        plugin.componentLogger, Placeholder.unparsed("player", player.username))
                                }
                            } ?: {
                                ctx.source.sendMini(
                                    plugin.messages.command.unknownPlayer,
                                    plugin.componentLogger,
                                    Placeholder.unparsed("name", name)
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

    private fun playerArgument() = requiredArgumentBuilder<String>("player", word())
        .suggests { _, builder ->
            plugin.proxyServer.allPlayers.map { it.username }.forEach(builder::suggest)
            builder.buildFuture()
        }
}

