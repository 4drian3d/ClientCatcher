package net.dreamerzero.ClientCatcher.commands;

import java.util.Optional;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ClientCommand implements SimpleCommand {
    private ProxyServer server;
	
	public ClientCommand(ProxyServer server){
		this.server = server;
	}

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
		Optional<Player> player;
        
		if (!source.hasPermission("clientcatcher.command")){
            source.sendMessage(
                Component.text("You do not have permission to execute this command.", 
                NamedTextColor.DARK_RED));
            return;
        }

        if (args.length == 0){
            source.sendMessage(Component.text("Client", NamedTextColor.RED)
                .append(Component.text("Catcher", NamedTextColor.WHITE))
				.append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text("Use:", NamedTextColor.RED))
                .append(Component.space())
                .append(Component.text("/client [user]")));
            return;
        } else if(args.length >= 1){
            player = server.getPlayer(args[0]);
            var playerName = player.get().getUsername();

			if(!player.isPresent()){
                source.sendMessage(
                    Component.text(args[0] + "is not a player or is not online", 
                    NamedTextColor.RED));
                return;
            }
			
			var clientbrand = player.get().getClientBrand();
            source.sendMessage(Component.text("Client of", NamedTextColor.RED)
                .append(Component.space())
                .append(Component.text(playerName, NamedTextColor.AQUA))
                .append(Component.text(":", NamedTextColor.GRAY))
                .append(Component.space())
                .append(Component.text(clientbrand, NamedTextColor.GOLD)));
        }
    }
}
