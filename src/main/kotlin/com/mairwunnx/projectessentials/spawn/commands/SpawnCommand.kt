package com.mairwunnx.projectessentials.spawn.commands

import com.mairwunnx.projectessentials.cooldown.essentials.CommandsAliases
import com.mairwunnx.projectessentials.core.backlocation.BackLocationProvider
import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.extensions.sendMsg
import com.mairwunnx.projectessentials.core.helpers.ONLY_PLAYER_CAN
import com.mairwunnx.projectessentials.core.helpers.PERMISSION_LEVEL
import com.mairwunnx.projectessentials.spawn.EntryPoint
import com.mairwunnx.projectessentials.spawn.EntryPoint.Companion.hasPermission
import com.mairwunnx.projectessentials.spawn.EntryPoint.Companion.modInstance
import com.mairwunnx.projectessentials.spawn.models.SpawnModelBase
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.dimension.DimensionType
import org.apache.logging.log4j.LogManager

object SpawnCommand {
    private val aliases = arrayOf("spawn", "espawn")
    private val logger = LogManager.getLogger()

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("Register \"/spawn\" command")
        applyCommandAliases()

        aliases.forEach { command ->
            dispatcher.register(
                literal<CommandSource>(command).executes(::execute).then(
                    Commands.literal("reload").executes(::reload)
                ).then(
                    Commands.literal("save").executes(::save)
                ).then(
                    Commands.literal("version").executes(::version)
                )
            )
        }
    }

    private fun applyCommandAliases() {
        if (!EntryPoint.cooldownsInstalled) return
        CommandsAliases.aliases["spawn"] = aliases.toMutableList()
    }

    private fun execute(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            if (hasPermission(c.source.asPlayer(), "ess.spawn", 0)) {
                moveToSpawn(c.source.asPlayer())
                logger.info("Executed command \"${c.input}\" from $playerName")
                sendMsg("spawn", c.source, "spawn.success")
            } else {
                sendMsg("spawn", c.source, "spawn.restricted")
                logger.info(
                    PERMISSION_LEVEL
                        .replace("%0", playerName)
                        .replace("%1", "spawn")
                )
            }
        } else {
            logger.info(ONLY_PLAYER_CAN.replace("%0", "spawn"))
        }
        return 0
    }

    fun moveToSpawn(player: ServerPlayerEntity) {
        BackLocationProvider.commit(player)

        val xPos = SpawnModelBase.spawnModel.xPos
        val yPos = SpawnModelBase.spawnModel.yPos
        val zPos = SpawnModelBase.spawnModel.zPos
        val yaw = SpawnModelBase.spawnModel.yaw
        val pitch = SpawnModelBase.spawnModel.pitch
        val dimId = SpawnModelBase.spawnModel.worldId
        val targetWorld = player.server.getWorld(
            DimensionType.getById(dimId) ?: DimensionType.OVERWORLD
        )
        player.teleport(targetWorld, xPos, yPos, zPos, yaw, pitch)
    }

    private fun reload(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            if (!hasPermission(c.source.asPlayer(), "ess.spawn.reload")) {
                sendMsg("spawn", c.source, "spawn.reload.restricted")
                logger.info(
                    PERMISSION_LEVEL
                        .replace("%0", playerName)
                        .replace("%1", "spawn reload")
                )
                return 0
            }
        }
        SpawnModelBase.loadData()
        SpawnModelBase.assignSpawn(c.source.server)
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            logger.info("Executed command \"${c.input}\" from $playerName")
            sendMsg("spawn", c.source, "spawn.reload.success")
        } else {
            logger.info("World spawn configuration reloaded.")
        }
        return 0
    }

    private fun save(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            if (!hasPermission(c.source.asPlayer(), "ess.spawn.save")) {
                sendMsg("spawn", c.source, "spawn.save.restricted")
                logger.info(
                    PERMISSION_LEVEL
                        .replace("%0", playerName)
                        .replace("%1", "spawn save")
                )
                return 0
            }
        }
        SpawnModelBase.saveData()
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            logger.info("Executed command \"${c.input}\" from $playerName")
            sendMsg("spawn", c.source, "spawn.save.success")
        } else {
            logger.info("World spawn configuration saved.")
        }
        return 0
    }

    private fun version(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            if (!hasPermission(c.source.asPlayer(), "ess.spawn.version", 3)) {
                sendMsg("spawn", c.source, "spawn.version.restricted")
                logger.info(
                    PERMISSION_LEVEL
                        .replace("%0", playerName)
                        .replace("%1", "spawn version")
                )
                return 0
            }
        }
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            sendMsg(
                "spawn",
                c.source,
                "spawn.version.success",
                modInstance.modName,
                modInstance.modVersion,
                modInstance.modMaintainer,
                modInstance.modTargetForge,
                modInstance.modTargetMC,
                modInstance.modSources,
                modInstance.modTelegram
            )
            logger.info("Executed command \"${c.input}\" from $playerName")
        } else {
            logger.info("        ${modInstance.modName}")
            logger.info("Version: ${modInstance.modVersion}")
            logger.info("Maintainer: ${modInstance.modMaintainer}")
            logger.info("Target Forge version: ${modInstance.modTargetForge}")
            logger.info("Target Minecraft version: ${modInstance.modTargetMC}")
            logger.info("Source code: ${modInstance.modSources}")
            logger.info("Telegram chat: ${modInstance.modTelegram}")
        }
        return 0
    }
}
