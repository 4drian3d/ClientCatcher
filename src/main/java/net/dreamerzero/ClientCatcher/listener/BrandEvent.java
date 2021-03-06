package net.dreamerzero.clientcatcher.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.event.player.PlayerClientBrandEvent;

import net.dreamerzero.clientcatcher.ModdedClient;

public class BrandEvent {
    @Subscribe
    public void onClientBrand(PlayerClientBrandEvent event){
        final Player player = event.getPlayer();
        final ModdedClient mclient = ModdedClient.getModdedClient(player.getUniqueId());
        mclient.setClient(event.getBrand());
    }
}
