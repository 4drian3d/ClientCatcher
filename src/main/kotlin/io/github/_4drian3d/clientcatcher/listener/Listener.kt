package io.github._4drian3d.clientcatcher.listener

import com.velocitypowered.api.event.AwaitingEventExecutor

interface Listener<E : Any> : AwaitingEventExecutor<E> {
    fun register()
}