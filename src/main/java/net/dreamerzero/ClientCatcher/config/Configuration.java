package net.dreamerzero.clientcatcher.config;

import java.util.List;

import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;

public class Configuration {
    public static void setDefaultConfig(Yaml config){
        config.setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
        config.setDefault(
            "messages.usage",
            "<gradient:red:white>ClientCatcher <gray>| <red>Usage: <white>/client <aqua>[user]");
        config.setDefault(
            "messages.unknown-player",
            "<red><name> is not a player or is not online");
        config.setDefault(
            "messages.client-command",
            "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client>");
        config.setDefault(
            "messages.client-with-mods-command",
            "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client><newline><red>Mods of the client: <aqua><mods>");
        config.setDefault("settings.broadcast-alert-to-op", true);
        config.setDefault("settings.show-null-client-message", false);
        config.setDefault(
            "settings.check-delay",
            "3");
        config.setDefault(
            "messages.client-alert-message",
            "<aqua><player> <red>has joined with client <aqua><client>");
        config.setDefault(
            "messages.client-with-mods-alert-message",
            "<aqua><player> <red>has joined with mods: <aqua><mods>");
        config.setDefault(
            "messages.null-client",
            "<color:#AB8138>The client of <white><player></white> has returned a null value");
        config.setDefault(
            "messages.client-disconnect-message",
            "<red>You have been disconnected for the use of forbidden clients.</red> <newline><color:#E96E6E>Please change to an allowed client to log in again.</color>");
        config.setDefault(
            "messages.mods-disconnect-message",
            "<red>You have been disconnected for the use of forbidden mods.</red> <newline><color:#E96E6E>Please remove <mod> in order to join the server.</color>");
        config.setDefault(
            "settings.blocked-clients",
            List.of(
                "wurst",
                "huzuni"));
        config.setDefault(
            "settings.blocked-mods",
            List.of(
                "worlddownloader",
                "forge-wurst",
                "xray"));
        config.setHeader(
                " ClientCatcher | v2.2.0",
                " ",
                " The plugin uses MiniMessage to colorize the messages.",
                " Check out their guide at:",
                " https://docs.adventure.kyori.net/minimessage.html#format",
                " Spanish Guide:",
                " https://gist.github.com/4drian3d/9ccce0ca1774285e38becb09b73728f3");
    }
}
