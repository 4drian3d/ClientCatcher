package me.adrianed.clientcatcher.velocity.configuration

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
class Configuration {
    var blocked = Blocked()
    @ConfigSerializable
    class Blocked {
        var mods: Map<String, List<String>> = mapOf(
            "worlddownloader" to listOf(),
            "forge-wurst" to listOf(),
            "xray" to listOf(),
        )
        var clients: Map<String, List<String>> = mapOf(
            "huzuni" to listOf(),
            "wurst" to listOf()
        )
    }
}