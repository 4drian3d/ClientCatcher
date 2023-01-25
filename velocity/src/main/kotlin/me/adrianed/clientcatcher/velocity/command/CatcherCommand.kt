package me.adrianed.clientcatcher.velocity.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandSource
import me.adrianed.clientcatcher.velocity.ClientCatcher

fun register(manager: CommandManager, plugin: ClientCatcher) {
    val command = BrigadierCommand(literal<CommandSource>("clientcatcher")
        .then(literal<CommandSource>("reload")
            .executes {
                plugin.loadConfig().thenAccept { result ->
                    if (result) {

                    } else {

                    }
                }
                Command.SINGLE_SUCCESS
            }
        )
        .then(literal<CommandSource>("client")
            .then(argument<CommandSource, String>("player", StringArgumentType.word())
                .executes {
                    plugin.proxyServer.getPlayer(StringArgumentType.getString(it, "player"))
                        .ifPresentOrElse({ player ->

                        }, {

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