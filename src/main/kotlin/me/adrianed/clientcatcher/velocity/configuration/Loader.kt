package me.adrianed.clientcatcher.velocity.configuration

import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.nio.file.Path
import kotlin.jvm.Throws

@Throws(Exception::class)
inline fun <reified C> load(path: Path): C {
    val loader = HoconConfigurationLoader.builder()
        .defaultOptions { opts ->
            opts
                .shouldCopyDefaults(true)
                .header("ClientCatcher | by 4drian3d\n")
        }
        .path(path.resolve("${C::class.simpleName!!.lowercase()}.conf"))
        .build()

    val node = loader.load()
    val config = node.get(C::class.java)
    node.set(C::class.java, config)
    loader.save(node)

    return config!!
}