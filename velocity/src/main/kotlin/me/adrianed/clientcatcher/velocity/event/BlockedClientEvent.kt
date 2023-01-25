package me.adrianed.clientcatcher.velocity.event

import com.velocitypowered.api.proxy.Player

data class BlockedClientEvent(val client: String, val player: Player)
