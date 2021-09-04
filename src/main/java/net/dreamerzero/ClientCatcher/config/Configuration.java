package net.dreamerzero.clientcatcher.config;

import net.dreamerzero.clientcatcher.Catcher;

import java.util.Arrays;
import java.util.List;

import de.leonhard.storage.internal.settings.ConfigSettings;

public class Configuration {
    public static void setDefaultConfig(){
        Catcher.getConfig().setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
        Catcher.getConfig().setPathPrefix("messages");
        Catcher.getConfig().setHeader(
            Arrays.asList(
                " ClientCatcher | v2.0.0",
                " ",
                " The plugin uses MiniMessage to colorize the messages.", 
                " Check out their guide at:",
                " https://docs.adventure.kyori.net/minimessage.html#format",
                " Spanish Guide:",
                " https://gist.github.com/4drian3d/9ccce0ca1774285e38becb09b73728f3"));
        Catcher.getConfig().setDefault(
            "usage", 
            "<gradient:red:white>ClientCatcher <gray>| <red>Usage: <white>/client <aqua>[user]");
        Catcher.getConfig().setDefault(
            "unknown-player", 
            "<red><name> is not a player or is not online");
        Catcher.getConfig().setDefault(
            "client-command", 
            "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client>");
        Catcher.getConfig().setDefault(
            "console-message", 
            "<aqua><player> <red>has joined with client <aqua><client>");
        Catcher.getConfig().setDefault(
            "null-client", 
            "<color:#AB8138>The client of <white><player></white> has returned a null value");
        Catcher.getConfig().setDefault(
            "disconnect-message", 
            "<red>You have been disconnected for the use of forbidden clients.</red> <newline><color:#E96E6E>Please change to an allowed client to log in again.</color>");
        Catcher.getConfig().setPathPrefix("settings");
        Catcher.getConfig().setDefault(
            "delay", 
            "7");
        Catcher.getConfig().setDefault(
            "blocked-clients", 
            List.of("wurst", "huzuni"));
    }
}
