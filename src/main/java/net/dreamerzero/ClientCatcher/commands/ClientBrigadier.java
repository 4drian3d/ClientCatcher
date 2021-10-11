package net.dreamerzero.clientcatcher.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import de.leonhard.storage.Yaml;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class ClientBrigadier {
    public static void registerBrigadierCommand(ProxyServer server, Yaml config){
        MiniMessage mm = MiniMessage.miniMessage();
        // return 1 == yes | return 0 == false
        LiteralCommandNode<CommandSource> clientCommand = LiteralArgumentBuilder
            .<CommandSource>literal("vclient")
            .executes(cmd -> {
                    cmd.getSource().sendMessage(mm.parse(
                    config.getOrSetDefault(
                        "messages.usage",
                        "<gradient:red:white>ClientCatcher <gray>| <red>Usage: <white>/client <aqua>[user]")));
                    return 0;
                }
            ).build();

        ArgumentCommandNode<CommandSource, String> playerArgument = RequiredArgumentBuilder
            .<CommandSource, String>argument("player", StringArgumentType.word())
            .suggests((argument, builder) -> {
                server.getAllPlayers().forEach(player -> builder.suggest(player.getUsername()));
                return builder.buildFuture();
            })
            .executes(arg -> {
                Audience source = (Audience)arg.getSource();
                if(arg.getSource().getPermissionValue("clientcatcher.command") != Tristate.TRUE) return 0;
                Optional<Player> optionalPlayer = server.getPlayer(arg.getInput());
                List<Template> templates = new ArrayList<>();
                templates.add(Template.of("newline", Component.newline()));
                if(optionalPlayer.isEmpty()) {
                    templates.add(Template.of("name", arg.getInput()));
                    source.sendMessage(
                        mm.parse(config.getOrSetDefault(
                            "messages.unknown-player",
                            "<red><name> is not a player or is not online"),
                        templates));
                    return 1;
                }
                Player player = optionalPlayer.get();

                templates.add(Template.of("client", player.getClientBrand()));
                templates.add(Template.of("player", player.getUsername()));
                if(player.getModInfo().isPresent()) {
                    StringBuilder builder = new StringBuilder();
                    for(com.velocitypowered.api.util.ModInfo.Mod mod : player.getModInfo().get().getMods()){
                        builder = builder.append("["+mod.getId()+"] ");
                    }
                    templates.add(Template.of("mods", builder.toString()));

                    source.sendMessage(
                        mm.parse(config.getOrSetDefault(
                            "messages.client-with-mods-command",
                            "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client><newline> <red>Mods of the client: <aqua><mods>"), 
                        templates));
                    return 1;
                } else {
                    source.sendMessage(
                        mm.parse(config.getOrSetDefault(
                            "messages.client-command",
                            "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client>"),
                        templates));
                    return 1;
                }
            })
            .build();

        clientCommand.addChild(playerArgument);

        BrigadierCommand bCommand = new BrigadierCommand(clientCommand);

        var mBuilder = server.getCommandManager().metaBuilder(bCommand).aliases("cliente").build();

        server.getCommandManager().register(mBuilder, bCommand);
    }
}
