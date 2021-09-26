package net.dreamerzero.clientcatcher.listener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
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
        final Player player = event.getPlayer();
        final String playerName = player.getUsername();
        long delay = Integer.parseInt(Catcher.getConfig().getOrSetDefault("settings.delay", "7"));
        List<String> blockedClients = Catcher.getConfig().getStringList("settings.blocked-clients");

        // The client sends the client brand seconds after logging in,
        // so you should wait a few seconds before trying to get it.
        server.getScheduler()
            .buildTask(plugin, () -> {
                String client = player.getClientBrand();
                if (client == null) {
                    server.getConsoleCommandSource().sendMessage(MiniMessage.miniMessage().parse(
                        Catcher.getConfig().getOrSetDefault(
                            "messages.null-client",
                            "The client of <player> has returned a null value"),
                            Template.of("player", playerName), Template.of("newline", Component.newline())));
                    return;
                }
                List<Template> templates = List.of(
                    Template.of("player", playerName),
                    Template.of("newline", Component.newline()),
                    Template.of("client", client));
                server.getConsoleCommandSource().sendMessage(MiniMessage.miniMessage().parse(
                    Catcher.getConfig().getOrSetDefault(
                        "messages.client-console-message",
                        "<player> has joined with client <client>"),
                    templates));
                for(String blockedClient : blockedClients){
                    if(client.contains(blockedClient)){
                        player.disconnect(MiniMessage.miniMessage().parse(
                            Catcher.getConfig().getString("messages.disconnect-message"), templates));
                    }
                }
            })
            .delay(delay, TimeUnit.SECONDS)
            .schedule();
    }
}
