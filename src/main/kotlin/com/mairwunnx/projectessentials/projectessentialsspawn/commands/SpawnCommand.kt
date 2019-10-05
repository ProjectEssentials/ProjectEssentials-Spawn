package com.mairwunnx.projectessentials.projectessentialsspawn.commands

import com.mairwunnx.projectessentials.projectessentialsspawn.*
import com.mairwunnx.projectessentials.projectessentialsspawn.extensions.isPlayerSender
import com.mairwunnx.projectessentials.projectessentialsspawn.extensions.sendMsg
import com.mairwunnx.projectessentials.projectessentialsspawn.models.SpawnModelBase
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
    }

    private fun execute(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            if (PermissionsAPI.hasPermission(playerName, "ess.spawn")) {
                moveToSpawn(c.source.asPlayer())
                logger.info("Executed command \"/${c.input}\" from $playerName")
                sendMsg(c.source, "spawn.success")
            } else {
                sendMsg(c.source, "spawn.restricted")
            }
        } else {
            // todo: use ModErrorHelper
            logger.info("Server failed to executing \"${c.input}\" command")
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
                sendMsg(c.source, "spawn.reload.restricted")
                return 0
            }
        }
        SpawnModelBase.loadData()
        SpawnModelBase.assignSpawn(c.source.server)
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            logger.info("Executed command \"/${c.input}\" from $playerName")
            sendMsg(c.source, "spawn.reload.success")
        } else {
            logger.info("World spawn configuration reloaded.")
        }
        return 0
    }

    private fun save(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            if (!PermissionsAPI.hasPermission(playerName, "ess.spawn.save")) {
                sendMsg(c.source, "spawn.save.restricted")
                return 0
            }
        }
        SpawnModelBase.saveData()
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            logger.info("Executed command \"/${c.input}\" from $playerName")
            sendMsg(c.source, "spawn.save.success")
        } else {
            logger.info("World spawn configuration saved.")
        }
        return 0
    }

    private fun version(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            if (!PermissionsAPI.hasPermission(playerName, "ess.spawn.version")) {
                sendMsg(c.source, "spawn.version.restricted")
                return 0
            }
        }
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            sendMsg(
                c.source,
                "spawn.version.success",
                MOD_NAME,
                MOD_VERSION,
                MOD_MAINTAINER,
                MOD_TARGET_FORGE,
                MOD_TARGET_MC,
                MOD_SOURCES_LINK,
                MOD_TELEGRAM_LINK
            )
            logger.info("Executed command \"/${c.input}\" from $playerName")
        } else {
            logger.info("        $MOD_NAME")
            logger.info("Version: $MOD_VERSION")
            logger.info("Maintainer: $MOD_MAINTAINER")
            logger.info("Target Forge version: $MOD_TARGET_FORGE")
            logger.info("Target Minecraft version: $MOD_TARGET_MC")
            logger.info("Source code: $MOD_SOURCES_LINK")
            logger.info("Telegram chat: $MOD_TELEGRAM_LINK")
        }
        return 0
    }
}
