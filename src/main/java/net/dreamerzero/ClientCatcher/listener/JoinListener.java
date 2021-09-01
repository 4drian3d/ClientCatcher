package net.dreamerzero.ClientCatcher.listener;

import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.event.PostOrder;
import org.slf4j.Logger;

import net.dreamerzero.ClientCatcher.Catcher;

public class JoinListener {
    private final Logger logger;
    private final ProxyServer server;
    private final Catcher plugin;

    public JoinListener(final ProxyServer server, final Logger logger, final Catcher plugin) {
        this.logger = logger;
        this.server = server;
        this.plugin = plugin;
    }
	
    @Subscribe(order = PostOrder.LATE)
    public void onPlayerJoin(final PostLoginEvent event) {
        final var player = event.getPlayer();
        final var playerName = player.getUsername();

        // The client sends the client brand seconds after logging in, 
        // so you should wait a few seconds before trying to get it.
        server.getScheduler()
            .buildTask(plugin, () -> {
                var client = player.getClientBrand();
                if (client == null) {
                    logger.info("The client of " + playerName + " has returned a null value");
                    return;
                }
                logger.info(playerName + " has joined with client: " + client);
            })
            .delay(7L, TimeUnit.SECONDS)
            .schedule();
    }
}
