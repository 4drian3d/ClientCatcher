package io.github._4drian3d.clientcatcher.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
class Configuration {
    @Comment(
        "Configuration of blocked clients/mods"
        + "\nFor moderation commands, I recommend you to use LibertyBans"
    )
    var blocked = Blocked()

    @Comment(
        "Configuration of WebHooks"
    )
    var webHook = WebHook()

    @ConfigSerializable
    class Blocked {
        @Comment(
            "Here you can configure the mods to detect and the commands\n"
            + "to execute when a mod is detected\n"
            + "Placeholders Available in Commands:\n"
            + "- <player>\n"
            + "  | The player's name\n"
            + "- <mod>\n"
            + "  | The detected mod"
        )
        var mods = setOf(
            BlockedElement().also {
                it.name = "worlddownloader"
                it.commands = listOf(
                    "libertybans ban <player> 7d You cannot use <mod> in this server"
                )
            },
            BlockedElement().also {
                it.name = "forge-wurst"
                it.commands = listOf(
                    "libertybans ban <player> Please don´t use forge... wurst, both"
                )
            },
            BlockedElement().also {
                it.name = "xray"
                it.commands = listOf(
                    "libertybans ban <player> Haha, on my server those antiques don't work, you will be banned anyway :)"
                )
            }
        )

        @Comment(
            "Configures the clients to be detected and the commands\n"
            + "to be executed when the specific client is detected\n"
            + "Placeholders Available in Commands:\n"
            + "- <player>\n"
            + "  | The player's name\n"
            + "- <client>\n"
            + "  | The player´s client"
        )
        var clients = setOf(
            BlockedElement().also {
                it.name = "huzuni"
                it.commands = listOf("libertybans ban 5d <player> No")
            },
            BlockedElement().also {
                it.name = "wurst"
                it.commands = listOf("libertybans ban 5d <player> Please, no")
            }
        )
    }

    @ConfigSerializable
    class BlockedElement {
        var name: String = ""
        var commands: List<String> = listOf()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as BlockedElement

            return other.name == name
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + commands.hashCode()
            return result
        }
    }

    @ConfigSerializable
    class WebHook {
        @Comment("WebHook ID")
        var id = "ID"
        @Comment("WebHook TOKEN")
        var token = "TOKEN"
        @Comment(
            "Client Detected WebHook\n"
            + "Placeholders:\n"
            + "- <player>\n"
            + "  | Player Name\n"
            + "- <client>\n"
            + "  | Client Name"
        )
        var client = WebHookElement()

        @Comment(
            "Mods Detected WebHook\n"
            + "Placeholders:\n"
            + "- <player>\n"
            + "  | Player Name\n"
            + "- <mods>\n"
            + "  | Mods Detected"
        )
        var mods = WebHookElement()

        @ConfigSerializable
        class WebHookElement {
            var enabled = false
            var content = "Content"
            var username = "Username"
            var avatarURL = "https://avatars.githubusercontent.com/u/68704415"
            var embed = Embed()

            @ConfigSerializable
            class Embed {
                var title = "Title"
                var description = "Description"
                var fields = listOf(
                    Field()
                )

                @ConfigSerializable
                class Field {
                    var inline = false
                    var name = "Name"
                    var value = "Value"
                }
            }
        }
    }
}