package net.dreamerzero.clientcatcher.listener;

import java.util.List;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerModInfoEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.ModInfo.Mod;

import net.dreamerzero.clientcatcher.Catcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class ModListener {
    private final ProxyServer server;
    public ModListener(final ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onModInfo(PlayerModInfoEvent event){
        Player player = event.getPlayer();
        List<Mod> mods = event.getModInfo().getMods();
        StringBuilder builder = new StringBuilder();
        for(Mod mod : mods){
            builder = builder.append("["+mod.getId()+"] ");
        }

        List<Template> templates = List.of(
            Template.of("player", player.getUsername()),
            Template.of("newline", Component.newline()),
            Template.of("mods", builder.toString()));

        server.getConsoleCommandSource().sendMessage(MiniMessage.miniMessage().parse(
            Catcher.getConfig().getOrSetDefault(
                "messages.mods-console-message",
                "<player> has joined with mods: <mods>"), 
                templates));
    }
}
