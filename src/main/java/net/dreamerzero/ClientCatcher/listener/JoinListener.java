package net.dreamerzero.clientcatcher.listener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.event.PostOrder;

import net.dreamerzero.clientcatcher.Catcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class JoinListener {
    private final ProxyServer server;
    private final Catcher plugin;

    public JoinListener(final ProxyServer server, final Catcher plugin) {
        this.server = server;
        this.plugin = plugin;
    }

    @Subscribe(order = PostOrder.LATE)
    public void onPlayerJoin(final PostLoginEvent event) {
        final var player = event.getPlayer();
        final var playerName = player.getUsername();
        long delay = Integer.parseInt(Catcher.getConfig().getOrSetDefault("settings.delay", "7"));
        List<String> blockedClients = Catcher.getConfig().getStringList("settings.blocked-clients");

        // The client sends the client brand seconds after logging in, 
        // so you should wait a few seconds before trying to get it.
        server.getScheduler()
            .buildTask(plugin, () -> {
                var client = player.getClientBrand();
                List<Template> templates = List.of(Template.of("player", playerName), Template.of("client", client), Template.of("newline", Component.newline()));
				List<Template> nulltemplates = List.of(Template.of("player", playerName), Template.of("newline", Component.newline()));
                if (client == null) {
                    server.getConsoleCommandSource().sendMessage(MiniMessage.get().parse(
                        Catcher.getConfig().getOrSetDefault(
                            "messages.null-client", 
                            "The client of <player> has returned a null value"), 
                        nulltemplates));
                    return;
                }
                server.getConsoleCommandSource().sendMessage(MiniMessage.get().parse(
                    Catcher.getConfig().getOrSetDefault(
                        "messages.console-message", 
                        "<player> has joined with client <client>"), 
                    templates));
                for(String blockedClient : blockedClients){
                    if(client.contains(blockedClient)){
                        player.disconnect(
                            MiniMessage.get().parse(
                                Catcher.getConfig().getString("messages.disconnect-message"), 
                                templates));
                    }
                }

            })
            .delay(delay, TimeUnit.SECONDS)
            .schedule();
    }
}
