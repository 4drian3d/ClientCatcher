package net.dreamerzero.clientcatcher.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import de.leonhard.storage.Yaml;

import com.velocitypowered.api.event.PostOrder;

import net.dreamerzero.clientcatcher.Catcher;
import net.kyori.adventure.audience.Audience;
import static net.kyori.adventure.text.Component.newline;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class ConnectListener {
    private final ProxyServer server;
    private final Catcher plugin;
    private Yaml config;
    private boolean broadcastToOp;
    private MiniMessage mm;
    private long delay;

    public ConnectListener(final ProxyServer server, final Catcher plugin, Yaml config) {
        this.server = server;
        this.plugin = plugin;
        this.config = config;
        this.mm = MiniMessage.miniMessage();
    }

    @Subscribe(order = PostOrder.LATE)
    public void onPlayerConnect(ServerPostConnectEvent event) {
        if(event.getPreviousServer() != null) return;
        Player player = event.getPlayer();
        broadcastToOp = config.getBoolean("settings.broadcast-alert-to-op");
        delay = config.getLong("settings.check-delay");

        // The client sends the client brand seconds after logging in,
        // so you should wait a few seconds before trying to get it.
        server.getScheduler().buildTask(plugin, () -> {
            List<Template> templates = List.of(
                Template.of("player", player.getUsername()),
                Template.of("newline", newline()));

            Optional<String> client = Optional.ofNullable(player.getClientBrand());

            ArrayList<Audience> ops = new ArrayList<>();
            server.getAllPlayers().stream().filter(
                user -> user.hasPermission("clientcatcher.notifications")).forEach(ops::add);
            ops.add(server.getConsoleCommandSource());

            if (client.isEmpty() && config.getBoolean("settings.show-null-client-message")) {
                server.getConsoleCommandSource().sendMessage(mm.parse(
                    config.getString("messages.null-client"),
                    templates));
                return;
            }

            templates.add(Template.of("client", client.get()));

            if(config.getStringList("settings.blocked-clients")
                .stream().anyMatch(blockedClient -> client.get().contains(blockedClient))){
                player.disconnect(mm.parse(
                        config.getString("messages.client-disconnect-message"), templates));
                    return;
            }

            if(player.getModInfo().isPresent()){
                if(player.getModInfo().get().getMods().stream()
                .anyMatch(mod -> config.getStringList("settings.blocked-mods")
                .contains(mod.getId()))){

                    player.disconnect(mm.parse(
                        config.getString("messages.mods-disconnect-message"), templates));
                    return;
                }
                templates.add(Template.of("mods", player.getModInfo().get().getMods().toString()));
                if(broadcastToOp){
                    Audience.audience(ops).sendMessage(mm.parse(
                        config.getString("messages.client-with-mods-alert-message"),
                        templates));
                    return;
                }
            }

            if(broadcastToOp){
                Audience.audience(ops).sendMessage(mm.parse(
                    config.getString("messages.client-alert-message"),
                    templates));
            }
        })
        .delay(delay, TimeUnit.SECONDS)
        .schedule();
    }
}
