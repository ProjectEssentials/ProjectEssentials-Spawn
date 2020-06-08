package com.mairwunnx.projectessentials.spawn.commands

import com.mairwunnx.projectessentials.core.api.v1.MESSAGE_MODULE_PREFIX
import com.mairwunnx.projectessentials.core.api.v1.commands.CommandBase
import com.mairwunnx.projectessentials.core.api.v1.extensions.currentDimensionId
import com.mairwunnx.projectessentials.core.api.v1.extensions.getPlayer
import com.mairwunnx.projectessentials.core.api.v1.messaging.MessagingAPI
import com.mairwunnx.projectessentials.core.api.v1.messaging.ServerMessagingAPI
import com.mairwunnx.projectessentials.spawn.helpers.validateAndExecute
import com.mairwunnx.projectessentials.spawn.spawnConfiguration
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.util.math.BlockPos

object SetSpawnCommand : CommandBase(setSpawnLiteral) {
    override val name = "set-spawn"
    override fun process(context: CommandContext<CommandSource>) = 0.also {
        validateAndExecute(context, "ess.spawn.set", 4) { isServer ->
            if (isServer) ServerMessagingAPI.throwOnlyPlayerCan() else {
                with(spawnConfiguration.take()) {
                    xPos = context.getPlayer()!!.position.x
                    yPos = context.getPlayer()!!.position.y
                    zPos = context.getPlayer()!!.position.z
                    yaw = context.getPlayer()!!.rotationYaw
                    pitch = context.getPlayer()!!.rotationPitch
                    dimensionId = context.getPlayer()!!.currentDimensionId
                }.also {
                    context.getPlayer()!!.serverWorld.spawnPoint = BlockPos(
                        context.getPlayer()!!.position.x,
                        context.getPlayer()!!.position.y,
                        context.getPlayer()!!.position.z
                    )
                    MessagingAPI.sendMessage(
                        context.getPlayer()!!, "${MESSAGE_MODULE_PREFIX}spawn.setspawn.success"
                    ).also { super.process(context) }
                }
            }
        }
    }
}
