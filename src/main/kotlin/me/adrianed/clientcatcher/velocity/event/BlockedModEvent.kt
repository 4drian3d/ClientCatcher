package me.adrianed.clientcatcher.velocity.event

import com.velocitypowered.api.util.ModInfo
import com.velocitypowered.api.proxy.Player

data class BlockedModEvent(val mod: ModInfo.Mod, val player: Player)
