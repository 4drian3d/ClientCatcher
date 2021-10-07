package net.dreamerzero.clientcatcher.listener;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import de.leonhard.storage.Yaml;

import com.velocitypowered.api.event.PostOrder;

import net.dreamerzero.clientcatcher.Catcher;
import net.kyori.adventure.audience.Audience;
import static net.kyori.adventure.text.Component.newline;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class JoinListener {
    private final ProxyServer server;
    private final Catcher plugin;
    private Yaml config;
    private boolean broadcastToOp;
    private MiniMessage mm;
    private long delay;

    public JoinListener(final ProxyServer server, final Catcher plugin, Yaml config) {
        this.server = server;
        this.plugin = plugin;
        this.config = config;
        this.mm = MiniMessage.miniMessage();
    }

    @Subscribe(order = PostOrder.LATE)
    public void onPlayerJoin(final PostLoginEvent event) {
        final Player player = event.getPlayer();
        broadcastToOp = config.getBoolean("settings.broadcast-alert-to-op");
        delay = config.getLong("settings.check-delay");
        List<String> blockedClients = config.getStringList("settings.blocked-clients");

        // The client sends the client brand seconds after logging in,
        // so you should wait a few seconds before trying to get it.
        server.getScheduler().buildTask(plugin, () -> {
            List<Template> templates = List.of(
                Template.of("player", player.getUsername()),
                Template.of("newline", newline()));

            Optional<String> client = Optional.ofNullable(player.getClientBrand());

            Audience ops = Audience.audience(Audience.audience(
                server.getAllPlayers().stream().filter(
                    user -> user.hasPermission("clientcatcher.notifications")).toList()),
                    server.getConsoleCommandSource());

            if (client.isEmpty() && config.getBoolean("settings.show-null-client-message")) {
                server.getConsoleCommandSource().sendMessage(mm.parse(
                    config.getString("messages.null-client"),
                    templates));
                return;
            }

            templates.add(Template.of("client", client.get()));

            for(String blockedClient : blockedClients){
                if(client.get().contains(blockedClient)){
                    player.disconnect(mm.parse(
                        config.getString("messages.client-disconnect-message"), templates));
                    return;
                }
            }

            if(player.getModInfo().isPresent()){
                player.getModInfo().get().getMods().forEach(mod -> {
                    config.getStringList("settings.blocked-mods").forEach(blockedMod -> {
                        if(mod.getId().contains(blockedMod)){
                            player.disconnect(mm.parse(
                                config.getString("messages.mods-disconnect-message"), templates));
                            return;
                        }
                    });
                });
                templates.add(Template.of("mods", player.getModInfo().get().getMods().toString()));
                if(broadcastToOp){
                    ops.sendMessage(mm.parse(
                        config.getString("messages.client-with-mods-alert-message"),
                        templates));
                    return;
                }
            }

            if(broadcastToOp){
                ops.sendMessage(mm.parse(
                    config.getString("messages.client-alert-message"),
                    templates));
            }
        })
        .delay(delay, TimeUnit.SECONDS)
        .schedule();
    }
}
