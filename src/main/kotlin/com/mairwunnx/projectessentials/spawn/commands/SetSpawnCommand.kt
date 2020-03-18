package com.mairwunnx.projectessentials.spawn.commands

import com.mairwunnx.projectessentials.cooldown.essentials.CommandsAliases
import com.mairwunnx.projectessentials.core.extensions.isPlayerSender
import com.mairwunnx.projectessentials.core.helpers.throwOnlyPlayerCan
import com.mairwunnx.projectessentials.core.helpers.throwPermissionLevel
import com.mairwunnx.projectessentials.spawn.EntryPoint
import com.mairwunnx.projectessentials.spawn.EntryPoint.Companion.hasPermission
import com.mairwunnx.projectessentials.spawn.models.SpawnModelBase
import com.mairwunnx.projectessentials.spawn.sendMessage
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.LogManager

object SetSpawnCommand {
    private val aliases = listOf(
        "setspawn", "esetspawn", "spawnpoint", "setworldspawn"
    )
    private val logger = LogManager.getLogger()

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("Register \"/setspawn\" command")
        applyCommandAliases()

        aliases.forEach { command ->
            dispatcher.register(
                literal<CommandSource>(command).executes(::execute)
            )
        }
    }

    private fun applyCommandAliases() {
        if (!EntryPoint.cooldownsInstalled) return
        CommandsAliases.aliases["setspawn"] = aliases.toMutableList()
    }

    private fun execute(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val player = c.source.asPlayer()
            if (hasPermission(player, "ess.spawn.set")) {
                SpawnModelBase.spawnModel.xPos = player.positionVector.x
                SpawnModelBase.spawnModel.yPos = player.positionVector.y
                SpawnModelBase.spawnModel.zPos = player.positionVector.z
                SpawnModelBase.spawnModel.yaw = player.rotationYaw
                SpawnModelBase.spawnModel.pitch = player.rotationPitch
                SpawnModelBase.spawnModel.worldId = player.serverWorld.worldType.id
                player.serverWorld.spawnPoint = BlockPos(
                    player.positionVector.x, player.positionVector.y, player.positionVector.z
                )
                sendMessage(c.source, "set.success")
                logger.info("New spawn point installed by ${player.name.string} with data: ")
                logger.info("    - xpos: ${player.positionVector.x}")
                logger.info("    - ypos: ${player.positionVector.y}")
                logger.info("    - zpos: ${player.positionVector.z}")
                logger.info("    - yaw: ${player.rotationYaw}")
                logger.info("    - pitch: ${player.rotationPitch}")
                logger.info("Executed command \"${c.input}\" from ${player.name.string}")
            } else {
                sendMessage(c.source, "set.restricted")
                throwPermissionLevel(player.name.string, "setspawn")
            }
        } else {
            throwOnlyPlayerCan("setspawn")
        }
        return 0
    }
}
