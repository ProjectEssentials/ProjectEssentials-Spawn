@file:Suppress("unused")

package com.mairwunnx.projectessentials.spawn

import com.mairwunnx.projectessentials.core.api.v1.IMCLocalizationMessage
import com.mairwunnx.projectessentials.core.api.v1.IMCProvidersMessage
import com.mairwunnx.projectessentials.core.api.v1.configuration.ConfigurationAPI.getConfigurationByName
import com.mairwunnx.projectessentials.core.api.v1.events.ModuleEventAPI.subscribeOn
import com.mairwunnx.projectessentials.core.api.v1.events.forge.ForgeEventType
import com.mairwunnx.projectessentials.core.api.v1.events.forge.InterModEnqueueEventData
import com.mairwunnx.projectessentials.core.api.v1.module.IModule
import com.mairwunnx.projectessentials.spawn.commands.SetSpawnCommand
import com.mairwunnx.projectessentials.spawn.commands.SpawnCommand
import com.mairwunnx.projectessentials.spawn.configurations.SpawnConfiguration
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.InterModComms
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent

var firstLaunch = false
val spawnConfiguration by lazy {
    getConfigurationByName<SpawnConfiguration>("spawn")
}

fun forceTeleportToSpawn(player: ServerPlayerEntity) {
    val targetWorld = player.server.getWorld(
        DimensionType.getById(spawnConfiguration.take().dimensionId) ?: DimensionType.OVERWORLD
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
        subscribeOn<InterModEnqueueEventData>(
            ForgeEventType.EnqueueIMCEvent
        ) {
            sendLocalizationRequest()
            sendProvidersRequest()
        }
    }

    @SubscribeEvent
    fun onServerStarting(event: FMLServerStartingEvent) {
        firstSessionSpawnPoint(event)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        val player = event.player as ServerPlayerEntity
        if (player.bedPosition.isPresent) {
            player.server.worlds.forEach {
                val pos = player.getBedLocation(it.dimension.type) as BlockPos?
                if (pos != null) {
                    player.teleport(
                        it,
                        pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5,
                        player.rotationYaw, player.rotationPitch
                    )
                }
            }
        } else {
            forceTeleportToSpawn(player)
        }
    }

    private fun firstSessionSpawnPoint(event: FMLServerStartingEvent) {
        val world = event.server.getWorld(DimensionType.OVERWORLD)
        if (firstLaunch) {
            spawnConfiguration.take().xPos = world.spawnPoint.x
            spawnConfiguration.take().yPos = world.spawnPoint.y
            spawnConfiguration.take().zPos = world.spawnPoint.z
        }
    }

    private fun sendLocalizationRequest() {
        InterModComms.sendTo(
            "project_essentials_core",
            IMCLocalizationMessage
        ) {
            fun() = mutableListOf(
                "/assets/projectessentialsspawn/lang/en_us.json",
                "/assets/projectessentialsspawn/lang/ru_ru.json",
                "/assets/projectessentialsspawn/lang/zh_cn.json",
                "/assets/projectessentialsspawn/lang/de_de.json"
            )
        }
    }

    private fun sendProvidersRequest() {
        InterModComms.sendTo(
            "project_essentials_core",
            IMCProvidersMessage
        ) {
            fun() = listOf(
                SpawnConfiguration::class.java,
                ModuleObject::class.java,
                SetSpawnCommand::class.java,
                SpawnCommand::class.java
            )
        }
    }
}
