package net.dreamerzero.ClientCatcher;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import net.dreamerzero.ClientCatcher.commands.ClientCommand;
import net.dreamerzero.ClientCatcher.listener.JoinListener;
import org.slf4j.Logger;

public class Catcher {
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public Catcher(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // :)
        logger.info("ClientCatcher has started, have a nice day.");
        // Register the PostLogin listener
        server.getEventManager().register(this, new JoinListener(server, logger, this));
        // Register the "/client" command
        server.getCommandManager().register("client", new ClientCommand(server));
    }
}