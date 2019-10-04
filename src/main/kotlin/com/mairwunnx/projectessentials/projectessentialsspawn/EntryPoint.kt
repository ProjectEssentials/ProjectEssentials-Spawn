package com.mairwunnx.projectessentials.projectessentialsspawn

import com.mairwunnx.projectessentials.projectessentialsspawn.commands.SetSpawnCommand
import com.mairwunnx.projectessentials.projectessentialsspawn.commands.SpawnCommand
import com.mairwunnx.projectessentials.projectessentialsspawn.helpers.validateForgeVersion
import com.mairwunnx.projectessentials.projectessentialsspawn.models.SpawnModelBase
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.command.CommandSource
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent
import org.apache.logging.log4j.LogManager

const val MOD_ID = "project_essentials_spawn"
const val MOD_NAME = "Project Essentials Spawn"
const val PART_OF_MOD = "Project Essentials"
const val MOD_VERSION = "1.14.4-1.0.0.0"
const val MOD_MAINTAINER = "MairwunNx (Pavel Erokhin)"
const val MOD_TARGET_FORGE = "28.0.X"
const val MOD_TARGET_FORGE_REGEX = "^28\\.0\\..\\d{1,}|28\\.0\\.[\\d]\$"
const val MOD_TARGET_MC = "1.14.4"
const val MOD_SOURCES_LINK = "https://github.com/MairwunNx/ProjectEssentials-Spawn/"
const val MOD_TELEGRAM_LINK = "https://t.me/minecraftforge"

@Suppress("unused")
@Mod(MOD_ID)
class EntryPoint {
    private val logger = LogManager.getLogger()

    init {
        logBaseInfo()
        validateForgeVersion()
        logger.debug("Register event bus for $MOD_NAME mod ...")
        MinecraftForge.EVENT_BUS.register(this)
        logger.info("Loading $MOD_NAME world spawn data ...")
        SpawnModelBase.loadData()
    }

    private fun logBaseInfo() {
        logger.info("$MOD_NAME starting initializing ...")
        logger.info("    - Mod Id: $MOD_ID")
        logger.info("    - Version: $MOD_VERSION")
        logger.info("    - Maintainer: $MOD_MAINTAINER")
        logger.info("    - Target Forge version: $MOD_TARGET_FORGE")
        logger.info("    - Target Minecraft version: $MOD_TARGET_MC")
        logger.info("    - Source code: $MOD_SOURCES_LINK")
        logger.info("    - Telegram chat: $MOD_TELEGRAM_LINK")
    }

    @SubscribeEvent
    internal fun onServerStarting(it: FMLServerStartingEvent) {
        logger.info("$MOD_NAME starting mod loading ...")
        registerCommands(it.server.commandManager.dispatcher)
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
    internal fun onServerStopping(it: FMLServerStoppingEvent) {
        logger.info("Shutting down $MOD_NAME mod ...")
        logger.info("    - Saving world spawn data ...")
        SpawnModelBase.saveData()
    }
}
