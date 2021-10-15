package net.dreamerzero.clientcatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.ModInfo;
import com.velocitypowered.api.util.ModInfo.Mod;

public class ModdedClient {
    public static Map<UUID, ModdedClient> moddedClients = new HashMap<>();
    private Optional<String> client;
    private Optional<ModInfo> mods;
    private Player player;
    private final String username;

    public ModdedClient(Player player){
        this.player = player;
        this.username = player.getUsername();
        this.mods = player.getModInfo();
        this.client = Optional.ofNullable(player.getClientBrand());
    }

    public Player getPlayer(){
        return this.player;
    }

    public String username(){
        return this.username;
    }

    public Optional<String> getClient(){
        return this.client;
    }

    public Optional<ModInfo> getMods(){
        return this.mods;
    }

    public void setMods(ModInfo newModInfo){
        this.mods = Optional.of(newModInfo);
    }

    public void setClient(String client){
        this.client = Optional.of(client);
    }

    public static ModdedClient getModdedClient(UUID uuid){
        if(moddedClients.containsKey(uuid)){
            return moddedClients.get(uuid);
        } else {
            Optional<Player> optionalPlayer = Catcher.getProxy().getPlayer(uuid);
            if(optionalPlayer.isEmpty()) return null;
            ModdedClient mclient = new ModdedClient(optionalPlayer.get());
            moddedClients.put(uuid, mclient);
            return mclient;
        }
    }

    public static boolean isModdedClient(UUID uuid){
        return moddedClients.containsKey(uuid);
    }

    public static List<ModdedClient> getModdedClients(){
        return moddedClients.entrySet().stream().map(Entry::getValue).collect(Collectors.toList());
    }

    public boolean hasMods(){
        return mods.isPresent();
    }

    public List<Mod> getModList(){
        return mods.get().getMods();
    }
}
