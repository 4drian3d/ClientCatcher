package io.github._4drian3d.clientcatcher

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

fun String.asMiniMessage() = MiniMessage.miniMessage().deserialize(this)
fun String.asMiniMessage(vararg resolvers: TagResolver) = MiniMessage.miniMessage().deserialize(this, *resolvers)
fun String.asMiniMessage(resolver: TagResolver) = MiniMessage.miniMessage().deserialize(this, resolver)

fun Audience.sendMini(message: String, resolver: TagResolver) {
    if (message.isNotBlank()) {
        this.sendMessage(message.asMiniMessage(resolver))
    }
}
