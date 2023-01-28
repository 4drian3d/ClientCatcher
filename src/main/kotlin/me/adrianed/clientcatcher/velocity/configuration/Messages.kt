package me.adrianed.clientcatcher.velocity.configuration

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
        var usage = "<gradient:red:white>ClientCatcher <gray>| <red>Usage: <white>/client <aqua><player>"
        var unknownPlayer = "<gray><name> <red>is not a player or is not online"
        var client = Client()
        var mods = Mods()

        @ConfigSerializable
        class Client {
            var withMods = "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client><newline><red>Mods of the client: <aqua><mods>"
            var client = "<red>Client of</red> <aqua><player></aqua><gray>: <gold><client>"
        }

        @ConfigSerializable
        class Mods {
            var modsFound = "Player <player> has the mods: <mods>"
            var notModsFound = "Player <player> does not use any mods"
        }
    }

    @ConfigSerializable
    class Alert {
        var client = "<aqua><player> <red>has joined with client <aqua><client>"
        var mods = "<aqua><player> <red>has joined with mods: <aqua><mods>"
    }

    @ConfigSerializable
    class Reload {
        var successfully = "<gradient:red:white>ClientCatcher</gradient> <green>Has been successfully reloaded"
        var error = "<gradient:red:white>ClientCatcher</gradient> An error occurred in config reloading"
    }
}