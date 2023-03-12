package io.github._4drian3d.clientcatcher.event

import com.velocitypowered.api.proxy.Player

data class BlockedClientEvent(val client: String, val player: Player)
