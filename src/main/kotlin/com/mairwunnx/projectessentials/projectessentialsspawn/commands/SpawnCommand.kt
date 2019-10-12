package com.mairwunnx.projectessentials.projectessentialsspawn.commands

import com.mairwunnx.projectessentials.projectessentialsspawn.EntryPoint
import com.mairwunnx.projectessentials.projectessentialsspawn.models.SpawnModelBase
import com.mairwunnx.projectessentialscooldown.essentials.CommandsAliases
import com.mairwunnx.projectessentialscore.extensions.isPlayerSender
import com.mairwunnx.projectessentialscore.extensions.sendMsg
import com.mairwunnx.projectessentialscore.helpers.ONLY_PLAYER_CAN
import com.mairwunnx.projectessentialscore.helpers.PERMISSION_LEVEL
import com.mairwunnx.projectessentialspermissions.permissions.PermissionsAPI
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
        logger.info("    - register \"/spawn\" command ...")
        aliases.forEach { command ->
            dispatcher.register(
                literal<CommandSource>(command).executes {
                    return@executes execute(it)
                }.then(
                    Commands.literal("reload").executes {
                        return@executes reload(it)
                    }
                ).then(
                    Commands.literal("save").executes {
                        return@executes save(it)
                    }
                ).then(
                    Commands.literal("version").executes {
                        return@executes version(it)
                    }
                )
            )
        }
        applyCommandAliases()
    }

    private fun applyCommandAliases() {
        try {
            Class.forName(
                "com.mairwunnx.projectessentialscooldown.essentials.CommandsAliases"
            )
            CommandsAliases.aliases["spawn"] = aliases.toMutableList()
            logger.info("        - applying aliases: $aliases")
        } catch (_: ClassNotFoundException) {
            // ignored
        }
    }

    private fun execute(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            if (PermissionsAPI.hasPermission(playerName, "ess.spawn")) {
                moveToSpawn(c.source.asPlayer())
                logger.info("Executed command \"/${c.input}\" from $playerName")
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
            if (!PermissionsAPI.hasPermission(playerName, "ess.spawn.reload")) {
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
            logger.info("Executed command \"/${c.input}\" from $playerName")
            sendMsg("spawn", c.source, "spawn.reload.success")
        } else {
            logger.info("World spawn configuration reloaded.")
        }
        return 0
    }

    private fun save(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            if (!PermissionsAPI.hasPermission(playerName, "ess.spawn.save")) {
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
            logger.info("Executed command \"/${c.input}\" from $playerName")
            sendMsg("spawn", c.source, "spawn.save.success")
        } else {
            logger.info("World spawn configuration saved.")
        }
        return 0
    }

    private fun version(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            if (!PermissionsAPI.hasPermission(playerName, "ess.spawn.version")) {
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
                EntryPoint.modInstance.modName,
                EntryPoint.modInstance.modVersion,
                EntryPoint.modInstance.modMaintainer,
                EntryPoint.modInstance.modTargetForge,
                EntryPoint.modInstance.modTargetMC,
                EntryPoint.modInstance.modSources,
                EntryPoint.modInstance.modTelegram
            )
            logger.info("Executed command \"/${c.input}\" from $playerName")
        } else {
            logger.info("        ${EntryPoint.modInstance.modName}")
            logger.info("Version: ${EntryPoint.modInstance.modVersion}")
            logger.info("Maintainer: ${EntryPoint.modInstance.modMaintainer}")
            logger.info("Target Forge version: ${EntryPoint.modInstance.modTargetForge}")
            logger.info("Target Minecraft version: ${EntryPoint.modInstance.modTargetMC}")
            logger.info("Source code: ${EntryPoint.modInstance.modSources}")
            logger.info("Telegram chat: ${EntryPoint.modInstance.modTelegram}")
        }
        return 0
    }
}
