package net.dreamerzero.clientcatcher;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.clientcatcher.commands.ClientCommand;
import net.dreamerzero.clientcatcher.config.Configuration;
import net.dreamerzero.clientcatcher.listener.JoinListener;

public class Catcher {
    private final ProxyServer server;
    private final Logger logger;
    static Yaml config = new Yaml("config", "plugins/clientcatcher");

    @Inject
    public Catcher(final ProxyServer server, final Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        // :)
        logger.info("ClientCatcher has started, have a nice day.");
        // Default config
        Configuration.setDefaultConfig();
        // Register the PostLogin listener
        server.getEventManager().register(this, new JoinListener(server, this));
        // Register the "/client" command
        server.getCommandManager().register("client", new ClientCommand(server));
    }
    public static Yaml getConfig(){
        return config;
    }
}