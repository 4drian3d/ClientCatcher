package net.dreamerzero.clientcatcher.messaging;

import java.util.Optional;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

public class PlaceholderProvider {
    private final ProxyServer server;

    private final ChannelIdentifier clientChannel =
			MinecraftChannelIdentifier.from("clientcatcher:playerclient");

    public PlaceholderProvider(final ProxyServer server) {
        this.server = server;
    }
    @Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		// Register the custom messaging channel
		server.getChannelRegistrar().register(clientChannel);
	}

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().getId().equals("clientcatcher:client")) {
            return;
        }
        ByteArrayDataInput input = event.dataAsDataStream();
        String subchannel = input.readUTF();

        if (!subchannel.equals("playerclient")) return;

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        String user = input.readUTF();
        Optional<Player> player = server.getPlayer(user);

        if (!player.isPresent()) return;

        String playerClient = player.get().getClientBrand();

        ByteArrayDataOutput buf = ByteStreams.newDataOutput();
        buf.writeUTF("playerclient");
        buf.writeUTF(player.get().getUsername());
		buf.writeUTF(playerClient);

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        player.get().sendPluginMessage(clientChannel, buf.toByteArray());
    }
}
