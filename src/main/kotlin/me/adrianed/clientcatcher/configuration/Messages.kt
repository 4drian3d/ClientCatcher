package me.adrianed.clientcatcher.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
class Messages {
    @Comment("ClientCatcher command (/clientcatcher) messages")
    var command = Command()
    @Comment("Alert messages to be sent to administrators in case a player has a modified client or has mods")
    var alert = Alert()
    @Comment("Reload messages (/clientcatcher reload)")
    var reload = Reload()
    @ConfigSerializable
    class Command {
        @Comment("Main Command usage")
        var usage = "<gradient:red:white>ClientCatcher <gray>| <red>Usage: <white>/clientcatcher <client/mods/reload> <aqua><player>"
        @Comment(
            "Message to send if the name entered in the '/clientcatcher <client/mods> <player>' commands"
            + "\n does not correspond to an online player\n"
            + "Placeholders Available:\n"
            + "- <name>\n"
            + "  | The name provided"
        )
        var unknownPlayer = "<gray><name> <red>is not a player or is not online"
        var client = Client()
        var mods = Mods()

        @ConfigSerializable
        class Client {
            @Comment("Message to send if the player has reported his mods, so,\n"
                + "the message will contain information about his client and his installed mods.\n"
                + "Placeholders Available:\n"
                + "- <player>\n"
                + "  | The player name\n"
                + "- <client>\n"
                + "  | The player's client\n"
                + "- <mods>\n"
                + "  | The player's mods"
            )
            var withMods = "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client><newline><red>Mods of the client: <aqua><mods>"
            @Comment(
                "Message to be sent to check the player's client\n"
                + "Placeholders Available:\n"
                + "- <player>\n"
                + "  | The player name\n"
                + "- <client>\n"
                + "  | The player's client"
            )
            var client = "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client>"
        }

        @ConfigSerializable
        class Mods {
            @Comment(
                "Message to send to check a player's installed mods"+ "- <player>\n"
                + "Placeholders Available:\n"
                + "- <player>\n"
                + "  | The player's name\n"
                + "- <mods>\n"
                + "  | The player's mods"
            )
            var found = "<aqua>Player <gray><player></gray> has the mods: <mods>"
            @Comment(
                "Message to send in case the player has no mods installed"
                + "Placeholders Available\n:"
                + "- <player>\n"
                + "  | The player's name"
            )
            var notFound = "<aqua>Player <gray><player></gray> does not use any mods"
        }
    }

    @ConfigSerializable
    class Alert {
        @Comment(
            "Alert message when a player reports his client to the server\n"
            + "Placeholders Available\n:"
            + "- <player>\n"
            + "  | The player's name\n"
            + "- <client>\n"
            + "  | The player´s client"
        )
        var client = "<aqua><player> <red>has joined with client <aqua><client>"
        @Comment(
            "Alert message when a player reports his mods to the server\n"
            + "Placeholders Available\n:"
            + "- <player>\n"
            + "  | The player's name\n"
            + "- <mods>\n"
            + "  | The player´s mods"
        )
        var mods = "<aqua><player> <red>has joined with mods: <aqua><mods>"
    }

    @ConfigSerializable
    class Reload {
        var successfully = "<gradient:red:white>ClientCatcher</gradient> <green>Has been successfully reloaded"
        var error = "<gradient:red:white>ClientCatcher</gradient> An error occurred in config reloading"
    }
}