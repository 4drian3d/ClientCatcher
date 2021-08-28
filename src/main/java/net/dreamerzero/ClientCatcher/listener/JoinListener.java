package net.dreamerzero.ClientCatcher.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.PostOrder;

import org.slf4j.Logger;

import net.dreamerzero.ClientCatcher.Catcher;

public class JoinListener {
    private Logger logger;
    private ProxyServer server;
    private Catcher plugin;
	public JoinListener(ProxyServer server, Logger logger, Catcher plugin){
		this.logger = logger;
        this.server = server;
        this.plugin = plugin;
	}
	
    @Subscribe(order = PostOrder.LATE)
    public void onPlayerJoin(PostLoginEvent event){
        var player = event.getPlayer();
		var playerName = player.getUsername();
        server.getScheduler()
            .buildTask(plugin, () -> {
                var client = player.getClientBrand();
                logger.info("Player " + playerName + " has joined with client: " + client);
            })
            .delay(6L, TimeUnit.SECONDS)
            .schedule();
    }
}
