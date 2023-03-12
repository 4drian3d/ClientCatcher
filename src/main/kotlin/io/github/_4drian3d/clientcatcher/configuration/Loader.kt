package io.github._4drian3d.clientcatcher.configuration

import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.dataClassFieldDiscoverer
import org.spongepowered.configurate.objectmapping.ObjectMapper
import java.nio.file.Path
import kotlin.jvm.Throws

@Throws(Exception::class)
inline fun <reified C> load(path: Path): C {
    val loader = HoconConfigurationLoader.builder()
        .defaultOptions {
            it.serializers { s->
                s.registerAnnotatedObjects(
                    ObjectMapper.factoryBuilder()
                        .addDiscoverer(dataClassFieldDiscoverer())
                        .build(),
                )
            }.shouldCopyDefaults(true)
                .header("ClientCatcher | by 4drian3d\n")
                .implicitInitialization(true)
        }
        .path(path.resolve("${C::class.simpleName!!.lowercase()}.conf"))
        .build()

    val node = loader.load()
    val config = node.get(C::class.java)
    node.set(C::class.java, config)
    loader.save(node)

    return config!!
}