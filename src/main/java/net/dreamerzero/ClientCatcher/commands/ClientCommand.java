package net.dreamerzero.ClientCatcher.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.space;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class ClientCommand implements SimpleCommand {
    private final ProxyServer server;
	
	public ClientCommand(ProxyServer server){
		this.server = server;
	}

    static final TextComponent helpMessage = text()
        .append(text("Client")
            .color(NamedTextColor.RED))
        .append(text("Catcher")
            .color(NamedTextColor.WHITE))
        .append(text(" | ", NamedTextColor.GRAY))
        .append(text("Use:", NamedTextColor.RED))
        .append(space())
        .append(text("/client [user]")).build();

    @Override
    public void execute(final Invocation invocation) {
        final CommandSource source = invocation.source();
        final String[] args = invocation.arguments();
        // The specified argument may or may not be a player, 
        // if it is not a player, the value will be null.
		Optional<Player> player;

        if (args.length == 0){
            source.sendMessage(helpMessage);
            return;
        } else if(args.length >= 1){
            player = server.getPlayer(args[0]);

			if(!player.isPresent()){
                source.sendMessage(
                    text(args[0])
                    .color(NamedTextColor.RED)
                    .append(space())
                        .append(text("is not a player or is not online").color(NamedTextColor.RED)));
                return;
            }
			
            final var playerName = player.get().getUsername();
			final var clientbrand = player.get().getClientBrand();
            source.sendMessage(
                text("Client of")
                    .color(NamedTextColor.RED)
                .append(space())
                .append(text(playerName)
                    .color(NamedTextColor.AQUA))
                .append(text(":")
                    .color(NamedTextColor.GRAY))
                .append(space())
                .append(text(clientbrand)
                    .color(NamedTextColor.GOLD)));
        }
    }

    @Override
    public List<String> suggest(final Invocation invocation){
        List<String> players = new ArrayList<>();
        var allplayers = server.getAllPlayers();
        for (Player player : allplayers) {
            var playername = player.getUsername();
            players.add(playername);
        }
        return players;
    }

    @Override
    public boolean hasPermission(final Invocation invocation){
        return invocation.source().hasPermission("clientcatcher.command");
    }
}
