package me.adrianed.clientcatcher.objects

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.permission.Tristate

object CatcherCommandSource : CommandSource {
    override fun getPermissionValue(permission: String?) = Tristate.TRUE
}