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
    private Logger logger;

    @Inject
    public Catcher(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new JoinListener(server, logger, this));
        server.getCommandManager().register("client", new ClientCommand(server));
    }
}