package net.dreamerzero.clientcatcher.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

import net.dreamerzero.clientcatcher.ModdedClient;

public class LeaveListener {
    @Subscribe
    public void onPlayerLeave(DisconnectEvent event){
        if(ModdedClient.isModdedClient(event.getPlayer().getUniqueId())){
            ModdedClient.moddedClients.remove(event.getPlayer().getUniqueId());
        }
    }
}
