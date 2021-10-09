package net.dreamerzero.clientcatcher.commands;

import java.util.List;
import java.util.Optional;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.ModInfo.Mod;

import de.leonhard.storage.Yaml;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class ClientCommand implements SimpleCommand {
    private final ProxyServer server;
    private Yaml config;
    private MiniMessage mm;
    public ClientCommand(ProxyServer server, Yaml config) {
        this.server = server;
        this.config = config;
        this.mm = MiniMessage.miniMessage();
    }

    @Override
    public void execute(final Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] args = invocation.arguments();

        // The specified argument may or may not be a player, 
        // if it is not a player, the value will be null.
        Optional<Player> optionalPlayer;

        if (args.length == 0) {
            source.sendMessage(
                mm.parse(
                    config.getOrSetDefault(
                        "messages.usage",
                        "<gradient:red:white>ClientCatcher <gray>| <red>Usage: <white>/client <aqua>[user]")));
            return;
        } else if(args.length >= 1) {
            optionalPlayer = server.getPlayer(args[0]);
            List<Template> templates = List.of(Template.of("newline", Component.newline()));

			if (!optionalPlayer.isPresent()) {
                templates.add(Template.of("name", args[0]));
                source.sendMessage(
                    mm.parse(config.getOrSetDefault(
                        "messages.unknown-player",
                        "<red><name> is not a player or is not online"),
                    templates));
                return;
            }

            Player player = optionalPlayer.get();

            templates.add(Template.of("client", player.getClientBrand()));
            templates.add(Template.of("player", player.getUsername()));
            if(player.getModInfo().isPresent()) {
                StringBuilder builder = new StringBuilder();
                for(Mod mod : player.getModInfo().get().getMods()){
                    builder = builder.append("["+mod.getId()+"] ");
                }
                templates.add(Template.of("mods", builder.toString()));

                source.sendMessage(
                    mm.parse(config.getOrSetDefault(
                        "messages.client-with-mods-command",
                        "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client><newline> <red>Mods of the client: <aqua><mods>"), 
                    templates));
            } else {
                source.sendMessage(
                    mm.parse(config.getOrSetDefault(
                        "messages.client-command",
                        "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client>"),
                    templates));
            }
        }
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        return server.getAllPlayers().stream().limit(30).map(player -> player.getUsername()).toList();
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("clientcatcher.command");
    }
}
