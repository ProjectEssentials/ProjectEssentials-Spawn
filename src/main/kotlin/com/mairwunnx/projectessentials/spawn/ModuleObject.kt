@file:Suppress("unused", "SENSELESS_COMPARISON", "UNNECESSARY_SAFE_CALL")

package com.mairwunnx.projectessentials.spawn

import com.mairwunnx.projectessentials.core.api.v1.configuration.ConfigurationAPI.getConfigurationByName
import com.mairwunnx.projectessentials.core.api.v1.extensions.asPlayerEntity
import com.mairwunnx.projectessentials.core.api.v1.localization.LocalizationAPI
import com.mairwunnx.projectessentials.core.api.v1.module.IModule
import com.mairwunnx.projectessentials.core.api.v1.providers.ProviderAPI
import com.mairwunnx.projectessentials.spawn.commands.SetSpawnCommand
import com.mairwunnx.projectessentials.spawn.commands.SpawnCommand
import com.mairwunnx.projectessentials.spawn.configurations.SpawnConfiguration
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.dimension.DimensionType.getById
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent

var firstLaunch = false
val spawnConfiguration by lazy {
    getConfigurationByName<SpawnConfiguration>("spawn")
}

fun forceTeleportToSpawn(player: ServerPlayerEntity) {
    val targetWorld = player.server.getWorld(
        getById(spawnConfiguration.take().dimensionId) ?: DimensionType.OVERWORLD
    )
    with(spawnConfiguration.take()) {
        player.teleport(targetWorld, xPos + 0.5, yPos + 0.5, zPos + 0.5, yaw, pitch)
    }
}

@Mod("project_essentials_spawn")
class ModuleObject : IModule {
    override val name = this::class.java.`package`.implementationTitle.split(" ").last()
    override val version = this::class.java.`package`.implementationVersion!!
    override val loadIndex = 3
    override fun init() = Unit

    init {
        EVENT_BUS.register(this)
        initProviders()
        initLocalization()
    }

    private fun initProviders() {
        listOf(
            SpawnConfiguration::class.java,
            ModuleObject::class.java,
            SetSpawnCommand::class.java,
            SpawnCommand::class.java
        ).forEach(ProviderAPI::addProvider)
    }

    private fun initLocalization() {
        LocalizationAPI.apply(this.javaClass) {
            mutableListOf(
                "/assets/projectessentialsspawn/lang/en_us.json",
                "/assets/projectessentialsspawn/lang/ru_ru.json",
                "/assets/projectessentialsspawn/lang/zh_cn.json",
                "/assets/projectessentialsspawn/lang/de_de.json"
            )
        }
    }

    @SubscribeEvent
    fun onServerStarting(event: FMLServerStartingEvent) {
        if (firstLaunch) {
            val world = event.server.getWorld(DimensionType.OVERWORLD)
            spawnConfiguration.take().xPos = world.spawnPoint.x
            spawnConfiguration.take().yPos = world.spawnPoint.y
            spawnConfiguration.take().zPos = world.spawnPoint.z
        }
    }

    private val handledForSpawn = mutableSetOf<String>()

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onPlayerRespawn(event: PlayerEvent.Clone) {
        if (!event.isWasDeath) return
        val player = event.original as ServerPlayerEntity
        player.server.worlds.forEach {
            player.getBedLocation(it.dimension.type)?.let { pos ->
                if (
                    PlayerEntity.checkBedValidRespawnPosition(
                        player.server.getWorld(player.dimension), pos, false
                    ).isPresent
                ) {
                    player.teleport(
                        it, pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5,
                        player.rotationYaw, player.rotationPitch
                    ).let { return }
                } else handledForSpawn.add(player.name.string)
            }
        }
        handledForSpawn.add(player.name.string)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onPlayerRespawnPost(event: PlayerEvent.PlayerRespawnEvent) {
        if (event.player.name.string !in handledForSpawn) return
        handledForSpawn.remove(event.player.name.string)
        forceTeleportToSpawn(event.player.asPlayerEntity)
    }
}
