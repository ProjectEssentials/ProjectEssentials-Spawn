package com.mairwunnx.projectessentials.spawn.commands

import com.mairwunnx.projectessentials.core.api.v1.MESSAGE_MODULE_PREFIX
import com.mairwunnx.projectessentials.core.api.v1.commands.CommandBase
import com.mairwunnx.projectessentials.core.api.v1.commands.back.BackLocationAPI
import com.mairwunnx.projectessentials.core.api.v1.extensions.getPlayer
import com.mairwunnx.projectessentials.core.api.v1.messaging.MessagingAPI
import com.mairwunnx.projectessentials.core.api.v1.messaging.ServerMessagingAPI
import com.mairwunnx.projectessentials.spawn.forceTeleportToSpawn
import com.mairwunnx.projectessentials.spawn.helpers.validateAndExecute
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource

object SpawnCommand : CommandBase(spawnLiteral) {
    override val name = "spawn"
    override fun process(context: CommandContext<CommandSource>) = 0.also {
        validateAndExecute(context, "ess.spawn.teleport", 0) { isServer ->
            if (isServer) ServerMessagingAPI.throwOnlyPlayerCan() else {
                MessagingAPI.sendMessage(
                    context.getPlayer()!!, "${MESSAGE_MODULE_PREFIX}spawn.spawn.success"
                ).also { BackLocationAPI.commit(context.getPlayer()!!) }
                forceTeleportToSpawn(context.getPlayer()!!).also { super.process(context) }
            }
        }
    }
}
