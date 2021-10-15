package net.dreamerzero.clientcatcher.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerModInfoEvent;
import com.velocitypowered.api.proxy.Player;

import net.dreamerzero.clientcatcher.ModdedClient;

public class ModListener {
    @Subscribe
    public void onModInfo(PlayerModInfoEvent event){
        Player player = event.getPlayer();
        ModdedClient mclient = ModdedClient.getModdedClient(player.getUniqueId());
        mclient.setMods(event.getModInfo());
    }
}
