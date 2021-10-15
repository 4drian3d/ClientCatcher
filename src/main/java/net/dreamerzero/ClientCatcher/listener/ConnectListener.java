package net.dreamerzero.clientcatcher.listener;

import static net.kyori.adventure.text.Component.newline;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.ModInfo.Mod;

import de.leonhard.storage.Yaml;
import net.dreamerzero.clientcatcher.Catcher;
import net.dreamerzero.clientcatcher.ModdedClient;
import net.kyori.adventure.audience.Audience;
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
        ModdedClient mclient = ModdedClient.getModdedClient(player.getUniqueId());
        broadcastToOp = config.getBoolean("settings.broadcast-alert-to-op");
        delay = config.getLong("settings.check-delay");

        // The client sends the client brand seconds after logging in,
        // so you should wait a few seconds before trying to get it.
        server.getScheduler().buildTask(plugin, () -> {
            List<Template> templates = List.of(
                Template.of("player", mclient.username()),
                Template.of("newline", newline()));

            Optional<String> client = mclient.getClient();
            Audience cSource = server.getConsoleCommandSource();

            if (client.isEmpty()){
                if(config.getBoolean("settings.show-null-client-message")) {
                    cSource.sendMessage(mm.parse(
                        config.getString("messages.null-client"),
                        templates));
                }
                return;
            }

            ArrayList<Audience> ops = new ArrayList<>();
            server.getAllPlayers().stream().filter(
                user -> user.hasPermission("clientcatcher.notifications")).forEach(ops::add);
            ops.add(cSource);

            String playerClient = client.get();
            templates.add(Template.of("client", playerClient));

            if(config.getStringList("settings.blocked-clients")
                .stream().anyMatch(blockedClient -> playerClient.contains(blockedClient))){
                player.disconnect(mm.parse(
                        config.getString("messages.client-disconnect-message"), templates));
                    return;
            }

            if(mclient.getMods().isPresent()){
                List<Mod> modList = mclient.getMods().get().getMods();
                if(modList.stream()
                .anyMatch(mod -> config.getStringList("settings.blocked-mods")
                .contains(mod.getId()))){

                    player.disconnect(mm.parse(
                        config.getString("messages.mods-disconnect-message"), templates));
                    return;
                }
                templates.add(Template.of("mods", modList.toString()));
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
