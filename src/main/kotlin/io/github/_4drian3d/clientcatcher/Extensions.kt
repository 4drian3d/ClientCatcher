package io.github._4drian3d.clientcatcher

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.ProxyServer
import io.github._4drian3d.clientcatcher.configuration.Configuration
import io.github._4drian3d.clientcatcher.webhook.Replacer
import io.github._4drian3d.jdwebhooks.component.Component
import io.github._4drian3d.jdwebhooks.component.SeparatorComponent.Spacing
import io.github._4drian3d.jdwebhooks.webhook.WebHookExecution
import io.github.miniplaceholders.api.MiniPlaceholders
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.concurrent.CompletableFuture

val hasMiniPlaceholders by lazy {
    try {
        Class.forName("io.github.miniplaceholders.api.MiniPlaceholders")
        true
    } catch (_: ClassNotFoundException) {
        false
    }
}

fun ProxyServer.doWithFilteredAudience(permission: String, action: (CommandSource) -> Unit) {
    action.invoke(this.consoleCommandSource)
    allPlayers.forEach { if (it.hasPermission(permission)) action.invoke(it) }
}

private fun String.asMiniMessage(resolver: TagResolver) = MiniMessage.miniMessage().deserialize(this, resolver)

fun CommandSource.sendMini(message: String, logger: ComponentLogger, resolver: TagResolver) {
    if (message.isNotBlank()) {
        val builder = TagResolver.builder().resolver(resolver)
        if (hasMiniPlaceholders) {
            builder.resolver(MiniPlaceholders.audienceGlobalPlaceholders())
        }
        if (this is ConsoleCommandSource) {
            logger.info(message.asMiniMessage(builder.build()))
        } else {
            this.sendRichMessage(message, builder.build())
        }

    }
}

fun CommandSource.sendMini(message: String, logger: ComponentLogger, vararg resolver: TagResolver) {
    if (message.isNotBlank()) {
        val builder = TagResolver.builder().resolvers(*resolver)
        if (hasMiniPlaceholders) {
            builder.resolver(MiniPlaceholders.audienceGlobalPlaceholders())
        }
        if (this is ConsoleCommandSource) {
            logger.info(message.asMiniMessage(builder.build()))
        } else {
            this.sendRichMessage(message, builder.build())
        }
    }
}

fun CommandSource.sendMini(message: String, logger: ComponentLogger) {
    if (message.isNotBlank()) {
        val resolver =
            if (hasMiniPlaceholders) MiniPlaceholders.audienceGlobalPlaceholders()
            else TagResolver.empty()
        if (this is ConsoleCommandSource) {
            logger.info(message.asMiniMessage(resolver))
        } else {
            this.sendRichMessage(message, resolver)
        }
    }
}

fun sendWebHook(plugin: ClientCatcher, replacer: Replacer, config: Configuration.WebHook.WebHookElement) {
    val client = plugin.webHookClient
    if (client != null && config.enabled) {
        CompletableFuture.supplyAsync {
            val builder = WebHookExecution.builder()
                .username(replacer.replace(config.username))
                .avatarURL(config.avatarURL)
            val embedConfig = config.embed
            builder.component(Component.textDisplay(replacer.replace(config.content)))
            val containerBuilder = Component.container()
                .components(
                    Component.textDisplay("## " + replacer.replace(embedConfig.title)),
                    Component.textDisplay(replacer.replace(embedConfig.description))
                )
                .component(Component.separator().divider(true).spacing(Spacing.SMALL).build())
            embedConfig.fields.map {
                containerBuilder
                    .component(
                        Component.textDisplay("### " + replacer.replace(it.name))
                    )
                    .component(Component.textDisplay(replacer.replace(it.value)))
            }

            builder.components(containerBuilder.build()).build()
        }.thenCompose { client.executeWebHook(it) }
    }
}
