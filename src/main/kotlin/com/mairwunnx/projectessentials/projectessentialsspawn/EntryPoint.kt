package com.mairwunnx.projectessentials.projectessentialsspawn

import com.mairwunnx.projectessentials.projectessentialsspawn.commands.SetSpawnCommand
import com.mairwunnx.projectessentials.projectessentialsspawn.commands.SpawnCommand
import com.mairwunnx.projectessentials.projectessentialsspawn.models.SpawnModelBase
import com.mairwunnx.projectessentialscore.EssBase
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
        modVersion = "1.14.4-1.0.1.0"
        logBaseInfo()
        validateForgeVersion()
        logger.debug("Register event bus for $modName mod ...")
        MinecraftForge.EVENT_BUS.register(this)
        logger.info("Loading $modName world spawn data ...")
        SpawnModelBase.loadData()
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onServerStarting(event: FMLServerStartingEvent) {
        logger.info("$modName starting mod loading ...")
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
        logger.info("Command registering is starting ...")
        SpawnCommand.register(cmdDispatcher)
        SetSpawnCommand.register(cmdDispatcher)
    }

    @Suppress("UNUSED_PARAMETER")
    @SubscribeEvent
    fun onServerStopping(it: FMLServerStoppingEvent) {
        logger.info("Shutting down $modName mod ...")
        logger.info("    - Saving world spawn data ...")
        SpawnModelBase.saveData()
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        if (!event.player.bedPosition.isPresent) {
            SpawnCommand.moveToSpawn(event.player as ServerPlayerEntity)
        }
    }

    companion object {
        lateinit var modInstance: EntryPoint
    }
}
