package com.mairwunnx.projectessentials.projectessentialsspawn.commands

import com.mairwunnx.projectessentials.projectessentialsspawn.extensions.isPlayerSender
import com.mairwunnx.projectessentials.projectessentialsspawn.extensions.sendMsg
import com.mairwunnx.projectessentials.projectessentialsspawn.models.SpawnModelBase
import com.mairwunnx.projectessentialspermissions.permissions.PermissionsAPI
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
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
                }
            )
        }
    }

    private fun execute(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val playerName = c.source.asPlayer().name.string
            if (PermissionsAPI.hasPermission(playerName, "ess.spawn")) {
                val xPos = SpawnModelBase.spawnModel.xPos
                val yPos = SpawnModelBase.spawnModel.yPos
                val zPos = SpawnModelBase.spawnModel.zPos
                val yaw = SpawnModelBase.spawnModel.yaw
                val pitch = SpawnModelBase.spawnModel.pitch
                val dimId = SpawnModelBase.spawnModel.worldId
                val targetWorld = c.source.server.getWorld(
                    DimensionType.getById(dimId) ?: DimensionType.OVERWORLD
                )
                c.source.asPlayer().teleport(targetWorld, xPos, yPos, zPos, yaw, pitch)
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
}
