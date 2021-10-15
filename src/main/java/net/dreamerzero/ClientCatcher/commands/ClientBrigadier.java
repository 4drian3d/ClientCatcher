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
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.ModInfo.Mod;

import de.leonhard.storage.Yaml;
import net.dreamerzero.clientcatcher.ModdedClient;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class ClientBrigadier {
    public static void registerBrigadierCommand(ProxyServer server, Yaml config){
        MiniMessage mm = MiniMessage.miniMessage();
        // return 1 == yes | return 0 == false
        LiteralCommandNode<CommandSource> clientCommand = LiteralArgumentBuilder
            .<CommandSource>literal("client")
            .executes(cmd -> {
                    if(!cmd.getSource().hasPermission("clientcatcher.command")) return 0;
                    cmd.getSource().sendMessage(mm.parse(config.getString("messages.usage")));
                    return 1;
                }
            ).build();

        ArgumentCommandNode<CommandSource, String> playerArgument = RequiredArgumentBuilder
            .<CommandSource, String>argument("player", StringArgumentType.word())
            .suggests((argument, builder) -> {
                server.getAllPlayers().forEach(player -> builder.suggest(player.getUsername()));
                return builder.buildFuture();
            })
            .executes(arg -> {
                CommandSource source = arg.getSource();

                Optional<Player> optionalPlayer = server.getPlayer(arg.getArgument("player", String.class));

                List<Template> templates = new ArrayList<>();
                templates.add(Template.of("newline", Component.newline()));

                if(optionalPlayer.isEmpty()) {
                    templates.add(Template.of("name", arg.getArgument("player", String.class)));
                    source.sendMessage(
                        mm.parse(config.getString("messages.unknown-player"), templates));
                    return 1;
                }

                Player player = optionalPlayer.get();

                ModdedClient mClient = ModdedClient.getModdedClient(player.getUniqueId());

                templates.add(Template.of("client", mClient.getClient().isPresent() ? mClient.getClient().get() : "Client not avialable"));
                templates.add(Template.of("player", player.getUsername()));

                if(mClient.hasMods()) {
                    StringBuilder builder = new StringBuilder();
                    for(Mod mod : mClient.getModList()){
                        builder = builder.append("["+mod.getId()+"] ");
                    }
                    templates.add(Template.of("mods", builder.toString()));

                    source.sendMessage(
                        mm.parse(config.getString("messages.client-with-mods-command"), templates));
                } else {
                    source.sendMessage(
                        mm.parse(config.getString("messages.client-command"), templates));
                }
                return 1;
            })
            .build();

        clientCommand.addChild(playerArgument);

        BrigadierCommand bCommand = new BrigadierCommand(clientCommand);

        CommandMeta meta = server.getCommandManager().metaBuilder(bCommand).aliases("vclient","cliente").build();

        server.getCommandManager().register(meta, bCommand);
    }
}
