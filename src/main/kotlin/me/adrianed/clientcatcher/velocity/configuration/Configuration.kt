package me.adrianed.clientcatcher.velocity.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
class Configuration {
    @Comment(
        ""
    )
    var blocked = Blocked()
    @ConfigSerializable
    class Blocked {
        @Comment(
            ""
        )
        var mods: Map<String, List<String>> = mapOf(
            "worlddownloader" to listOf(
                "libertybans ban <player> 7d You cannot use <mod> in this server"
            ),
            "forge-wurst" to listOf(
                "libertybans ban <player> Please don´t use forge... wurst, both"
            ),
            "xray" to listOf(
                "libertybans ban <player> Haha, on my server those antiques don't work, you will be banned anyway :)"
            ),
        )

        @Comment(
            ""
        )
        var clients: Map<String, List<String>> = mapOf(
            "huzuni" to listOf(
                "libertybans ban 5d <player> No"
            ),
            "wurst" to listOf(
                "libertybans ban 5d <player> Please, no"
            )
        )
    }
}