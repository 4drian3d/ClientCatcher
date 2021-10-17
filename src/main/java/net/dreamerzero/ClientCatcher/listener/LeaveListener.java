package net.dreamerzero.clientcatcher.listener;

import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

import net.dreamerzero.clientcatcher.ModdedClient;

public class LeaveListener {
    @Subscribe
    public void onPlayerLeave(DisconnectEvent event){
        final UUID playerUuid = event.getPlayer().getUniqueId();
        if(ModdedClient.isModdedClient(playerUuid)){
            ModdedClient.moddedClients.remove(playerUuid);
        }
    }
}
