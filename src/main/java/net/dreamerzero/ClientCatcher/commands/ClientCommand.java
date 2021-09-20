package net.dreamerzero.clientcatcher.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.ModInfo.Mod;

import net.dreamerzero.clientcatcher.Catcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class ClientCommand implements SimpleCommand {
    private final ProxyServer server;
	
	public ClientCommand(ProxyServer server) {
        this.server = server;
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
                MiniMessage.get().parse(
                    Catcher.getConfig().getOrSetDefault(
                        "messages.usage",
                        "<gradient:red:white>ClientCatcher <gray>| <red>Usage: <white>/client <aqua>[user]")));
            return;
        } else if(args.length >= 1) {
            optionalPlayer = server.getPlayer(args[0]);

			if (!optionalPlayer.isPresent()) {
                source.sendMessage(
                    MiniMessage.get().parse(
                        Catcher.getConfig().getOrSetDefault(
                            "messages.unknown-player", 
                            "<red><name> is not a player or is not online"), 
                        Template.of("name", args[0]), Template.of("newline", Component.newline())));
                return;
            }

            Player player = optionalPlayer.get();

            final String playerName = player.getUsername();
            final String clientbrand = player.getClientBrand();
            List<Template> templates = List.of(
                Template.of("player", playerName), 
                Template.of("client", clientbrand), 
                Template.of("newline", Component.newline()));
            if(player.getModInfo().isPresent()) {
                StringBuilder builder = new StringBuilder();
                for(Mod mod : player.getModInfo().get().getMods()){
                    builder = builder.append("["+mod.getId()+"] ");
                }
                templates.add(Template.of("mods", builder.toString()));

                source.sendMessage(
                MiniMessage.get().parse(
                    Catcher.getConfig().getOrSetDefault(
                        "messages.client-with-mods-command",
                        "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client><newline> <red>Mods of the client: <aqua><mods>"), 
                    templates));
            } else {
                source.sendMessage(
                MiniMessage.get().parse(
                    Catcher.getConfig().getOrSetDefault(
                        "messages.client-command",
                        "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client>"), 
                    templates));
            }
        }
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        List<String> players = new ArrayList<>();
        var allplayers = server.getAllPlayers();
        for (Player player : allplayers) {
            String playername = player.getUsername();
            players.add(playername);
        }
        return players;
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("clientcatcher.command");
    }
}
