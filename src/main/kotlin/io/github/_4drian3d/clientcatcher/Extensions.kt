package io.github._4drian3d.clientcatcher

import io.github._4drian3d.clientcatcher.configuration.Configuration
import io.github._4drian3d.clientcatcher.webhook.Replacer
import io.github._4drian3d.jdwebhooks.Embed
import io.github._4drian3d.jdwebhooks.WebHook
import io.github.miniplaceholders.api.MiniPlaceholders
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.concurrent.CompletableFuture

val hasMiniPlaceholders by lazy {
    try {
        Class.forName("io.github.miniplaceholders.api.MiniPlaceholders")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}

private fun String.asMiniMessage(resolvers: Array<out TagResolver>) = MiniMessage.miniMessage().deserialize(this, *resolvers)
private fun String.asMiniMessage(resolver: TagResolver) = MiniMessage.miniMessage().deserialize(this, resolver)

fun Audience.sendMini(message: String, resolver: TagResolver) {
    if (message.isNotBlank()) {
        val builder = TagResolver.builder().resolver(resolver)
        if (hasMiniPlaceholders) {
            builder.resolver(MiniPlaceholders.getAudienceGlobalPlaceholders(this))
        }
        this.sendMessage(message.asMiniMessage(resolver))
    }
}

fun Audience.sendMini(message: String, vararg resolver: TagResolver) {
    if (message.isNotBlank()) {
        val builder = TagResolver.builder().resolvers(*resolver)
        if (hasMiniPlaceholders) {
            builder.resolver(MiniPlaceholders.getAudienceGlobalPlaceholders(this))
        }
        this.sendMessage(message.asMiniMessage(resolver))
    }
}

fun Audience.sendMini(message: String) {
    if (message.isNotBlank()) {
        val resolver =
            if (hasMiniPlaceholders) MiniPlaceholders.getAudienceGlobalPlaceholders(this)
            else TagResolver.empty()
        this.sendMessage(message.asMiniMessage(resolver))
    }
}

fun sendWebHook(plugin: ClientCatcher, replacer: Replacer, config: Configuration.WebHook.WebHookElement) {
    val client = plugin.webHookClient
    if (client != null && config.enabled) {
        CompletableFuture.supplyAsync {
            val builder = WebHook.builder()
                .allowedMentions(false)
                .content(replacer.replace(config.content))
                .username(replacer.replace(config.username))
                .avatarURL(config.avatarURL)
            val embedConfig = config.embed
            builder.embed(
                Embed.builder()
                    .fields(embedConfig.fields.map {
                        Embed.Field(it.inline, replacer.replace(it.name), replacer.replace(it.value))
                    })
                    .title(replacer.replace(embedConfig.title))
                    .description(embedConfig.description).build()
            )
            builder.build()
        }.thenCompose { client.sendWebHook(it) }
    }
}
