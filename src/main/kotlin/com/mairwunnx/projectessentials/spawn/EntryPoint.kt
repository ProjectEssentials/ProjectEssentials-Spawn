package com.mairwunnx.projectessentials.spawn

import com.mairwunnx.projectessentials.core.EssBase
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
        modVersion = "1.14.4-1.1.2"
        logBaseInfo()
        validateForgeVersion()
        MinecraftForge.EVENT_BUS.register(this)
        SpawnModelBase.loadData()
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onServerStarting(event: FMLServerStartingEvent) {
        registerCommands(event.server.commandManager.dispatcher)
        processFirstSession(event)
        SpawnModelBase.assignSpawn(event.server)
    }

    private fun processFirstSession(event: FMLServerStartingEvent) {
        logger.info("Processing first session for loaded world")
        var equals = true
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
        if (!event.player.bedPosition.isPresent) {
            SpawnCommand.moveToSpawn(event.player as ServerPlayerEntity)
        }
    }

    private fun loadAdditionalModules() {
        try {
            Class.forName(
                "com.mairwunnx.projectessentials.cooldown.essentials.CommandsAliases"
            )
            cooldownsInstalled = true
        } catch (_: ClassNotFoundException) {
            // ignored
        }

        try {
            Class.forName(
                "com.mairwunnx.projectessentials.permissions.permissions.PermissionsAPI"
            )
            permissionsInstalled = true
        } catch (_: ClassNotFoundException) {
            // ignored
        }
    }

    companion object {
        lateinit var modInstance: EntryPoint
        var cooldownsInstalled: Boolean = false
        var permissionsInstalled: Boolean = false

        fun hasPermission(player: ServerPlayerEntity, node: String, opLevel: Int = 4): Boolean =
            if (permissionsInstalled) {
                PermissionsAPI.hasPermission(player.name.string, node)
            } else {
                player.server.opPermissionLevel >= opLevel
            }
    }
}
