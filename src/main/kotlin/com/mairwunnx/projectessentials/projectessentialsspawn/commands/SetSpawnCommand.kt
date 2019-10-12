package com.mairwunnx.projectessentials.projectessentialsspawn.commands

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
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.LogManager

object SetSpawnCommand {
    private val aliases = arrayOf(
        "setspawn", "esetspawn", "spawnpoint", "setworldspawn"
    )
    private val logger = LogManager.getLogger()

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        logger.info("    - register \"/setspawn\" command ...")
        aliases.forEach { command ->
            dispatcher.register(
                literal<CommandSource>(command).executes {
                    return@executes execute(it)
                }
            )
        }
        applyCommandAliases()
    }

    private fun applyCommandAliases() {
        try {
            Class.forName(
                "com.mairwunnx.projectessentialscooldown.essentials.CommandsAliases"
            )
            CommandsAliases.aliases["setspawn"] = aliases.toMutableList()
            logger.info("        - applying aliases: $aliases")
        } catch (_: ClassNotFoundException) {
            // ignored
        }
    }

    private fun execute(c: CommandContext<CommandSource>): Int {
        if (c.isPlayerSender()) {
            val player = c.source.asPlayer()
            if (PermissionsAPI.hasPermission(player.name.string, "ess.spawn.set")) {
                SpawnModelBase.spawnModel.xPos = player.posX
                SpawnModelBase.spawnModel.yPos = player.posY
                SpawnModelBase.spawnModel.zPos = player.posZ
                SpawnModelBase.spawnModel.yaw = player.rotationYaw
                SpawnModelBase.spawnModel.pitch = player.rotationPitch
                SpawnModelBase.spawnModel.worldId = player.serverWorld.worldType.id
                player.world.spawnPoint = BlockPos(player.posX, player.posY, player.posZ)
                sendMsg("spawn", c.source, "spawn.set.success")
                logger.info("New spawn point installed by ${player.name.string} with data: ")
                logger.info("    - xpos: ${player.posX}")
                logger.info("    - ypos: ${player.posY}")
                logger.info("    - zpos: ${player.posZ}")
                logger.info("    - yaw: ${player.rotationYaw}")
                logger.info("    - pitch: ${player.rotationPitch}")
                logger.info("Executed command \"/${c.input}\" from ${player.name.string}")
            } else {
                sendMsg("spawn", c.source, "spawn.set.restricted")
                logger.info(
                    PERMISSION_LEVEL
                        .replace("%0", player.name.string)
                        .replace("%1", "setspawn")
                )
            }
        } else {
            logger.info(ONLY_PLAYER_CAN.replace("%0", "setspawn"))
        }
        return 0
    }
}
