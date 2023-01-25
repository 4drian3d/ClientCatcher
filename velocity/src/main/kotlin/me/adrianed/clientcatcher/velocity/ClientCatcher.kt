package me.adrianed.clientcatcher.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.plugin.Plugin

@Plugin(
    id = "clientcatcher"
)
class ClientCatcher @Inject constructor() {

    @Subscribe
    fun onProxyInitialization() {

    }
}