package com.mairwunnx.projectessentials.projectessentialsspawn.commands

import com.mairwunnx.projectessentials.projectessentialsspawn.models.SpawnModelBase
import com.mairwunnx.projectessentialspermissions.permissions.PermissionsAPI
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import org.apache.logging.log4j.LogManager

object SetSpawnCommand {
    private val aliases = arrayOf("setspawn", "esetspawn")
    private val logger = LogManager.getLogger()

    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        aliases.forEach { command ->
            dispatcher.register(
                LiteralArgumentBuilder.literal<CommandSource>(command).executes {
                    return@executes execute(it)
                }
            )
        }
    }

    private fun execute(c: CommandContext<CommandSource>): Int {
        // if source is server return
        val player = c.source.asPlayer()
        if (PermissionsAPI.hasPermission(player.name.string, "ess.spawn.set")) {
            SpawnModelBase.spawnModel.xPos = player.posX
            SpawnModelBase.spawnModel.yPos = player.posY
            SpawnModelBase.spawnModel.zPos = player.posZ
            SpawnModelBase.spawnModel.yaw = player.rotationYaw
            SpawnModelBase.spawnModel.pitch = player.rotationPitch
            SpawnModelBase.spawnModel.worldId = player.serverWorld.worldType.id
        }
        return 0
    }
}
