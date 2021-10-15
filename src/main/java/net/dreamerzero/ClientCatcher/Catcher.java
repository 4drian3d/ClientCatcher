package net.dreamerzero.clientcatcher;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.clientcatcher.commands.ClientBrigadier;
import net.dreamerzero.clientcatcher.config.Configuration;
import net.dreamerzero.clientcatcher.listener.ConnectListener;
import net.dreamerzero.clientcatcher.utils.Constants;

@Plugin(
    id = "clientcatcher",
    name = Constants.NAME,
    version = Constants.VERSION,
    description = Constants.DESCRIPTION,
    url = Constants.URL,
    authors = {"4drian3d"})
public class Catcher {
    private static ProxyServer proxy;
    private final Logger logger;
    private Yaml config;

    @Inject
    public Catcher(final ProxyServer server, final Logger logger) {
        proxy = server;
        this.logger = logger;
        this.config = new Yaml("config", "plugins/ClientCatcher");
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        // :)
        logger.info("ClientCatcher has started, have a nice day.");
        // Default config
        Configuration.setDefaultConfig(config);
        // Register the PostLogin listener
        proxy.getEventManager().register(this, new ConnectListener(proxy, this, config));
        // Register the "/client" command
        ClientBrigadier.registerBrigadierCommand(proxy, config);
    }

    public static ProxyServer getProxy(){
        return proxy;
    }
}