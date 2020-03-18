package com.mairwunnx.projectessentials.spawn

import com.mairwunnx.projectessentials.core.EssBase
import com.mairwunnx.projectessentials.home.HomeAPI
import com.mairwunnx.projectessentials.permissions.permissions.PermissionsAPI
import com.mairwunnx.projectessentials.spawn.commands.SetSpawnCommand
import com.mairwunnx.projectessentials.spawn.commands.SpawnCommand
import com.mairwunnx.projectessentials.spawn.models.SpawnModelBase
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.command.CommandSource
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent
import org.apache.logging.log4j.LogManager

@Suppress("unused")
@Mod("project_essentials_spawn")
class EntryPoint : EssBase() {
    private val logger = LogManager.getLogger()

    init {
        modInstance = this
        modVersion = "1.14.4-1.2.0"
        logBaseInfo()
        validateForgeVersion()
        MinecraftForge.EVENT_BUS.register(this)
        SpawnModelBase.loadData()
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onServerStarting(event: FMLServerStartingEvent) {
        registerCommands(event.commandDispatcher)
        processFirstSession(event)
        SpawnModelBase.assignSpawn(event.server)
    }

    private fun processFirstSession(event: FMLServerStartingEvent) {
        logger.info("Processing first session for loaded world")
        var equals = true
        // todo: previous todo apply for this
        val world = event.server.getWorld(DimensionType.OVERWORLD)

        if (SpawnModelBase.spawnModel.xPos.toInt() != world.spawnPoint.x) {
            equals = false
        }
        if (SpawnModelBase.spawnModel.yPos.toInt() != world.spawnPoint.y) {
            equals = false
        }
        if (SpawnModelBase.spawnModel.zPos.toInt() != world.spawnPoint.z) {
            equals = false
        }

        if (!equals) {
            SpawnModelBase.spawnModel.xPos = world.spawnPoint.x.toDouble()
            SpawnModelBase.spawnModel.yPos = world.spawnPoint.y.toDouble()
            SpawnModelBase.spawnModel.zPos = world.spawnPoint.z.toDouble()
        }
    }

    private fun registerCommands(
        cmdDispatcher: CommandDispatcher<CommandSource>
    ) {
        loadAdditionalModules()
        SpawnCommand.register(cmdDispatcher)
        SetSpawnCommand.register(cmdDispatcher)
    }

    @Suppress("UNUSED_PARAMETER")
    @SubscribeEvent
    fun onServerStopping(it: FMLServerStoppingEvent) {
        SpawnModelBase.saveData()
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        val player = event.player as ServerPlayerEntity

        fun teleportToSpawnOrBed() {
            if (player.bedPosition.isPresent) {
                val bedPos = player.bedPosition.get()
                val targetWorld = player.server.getWorld(DimensionType.OVERWORLD)
                player.teleport(
                    targetWorld,
                    bedPos.x.toDouble() + 0.5,
                    bedPos.y.toDouble() + 0.5,
                    bedPos.z.toDouble() + 0.5,
                    player.rotationYaw, player.rotationPitch
                )
            } else {
                SpawnCommand.moveToSpawn(player)
            }
        }

        if (homeInstalled) {
            val homes = HomeAPI.takeAll(player)

            if (homes.isNotEmpty()) {
                val home =
                    homes.first() // todo configure it with core module (new api with common project settings)
                val targetWorld = player.server.getWorld(
                    DimensionType.getById(home.worldId) ?: DimensionType.OVERWORLD
                )
                player.teleport(
                    targetWorld,
                    home.xPos.toDouble() + 0.5,
                    home.yPos.toDouble() + 0.5,
                    home.zPos.toDouble() + 0.5,
                    home.yaw, home.pitch
                )
            } else teleportToSpawnOrBed()
        } else teleportToSpawnOrBed()
    }

    private fun loadAdditionalModules() {
        try {
            Class.forName("com.mairwunnx.projectessentials.home.HomeAPI")
            homeInstalled = true
        } catch (_: ClassNotFoundException) {
            // ignored
        }

        try {
            Class.forName(cooldownAPIClassPath)
            cooldownsInstalled = true
        } catch (_: ClassNotFoundException) {
            // ignored
        }

        try {
            Class.forName(permissionAPIClassPath)
            permissionsInstalled = true
        } catch (_: ClassNotFoundException) {
            // ignored
        }
    }

    companion object {
        lateinit var modInstance: EntryPoint
        var cooldownsInstalled: Boolean = false
        var permissionsInstalled: Boolean = false
        var homeInstalled: Boolean = false

        fun hasPermission(player: ServerPlayerEntity, node: String, opLevel: Int = 4): Boolean =
            if (permissionsInstalled) {
                PermissionsAPI.hasPermission(player.name.string, node)
            } else {
                player.hasPermissionLevel(opLevel)
            }
    }
}
