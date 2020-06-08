package com.mairwunnx.projectessentials.spawn.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import net.minecraft.command.CommandSource

val spawnLiteral: LiteralArgumentBuilder<CommandSource> = literal("spawn")
val setSpawnLiteral: LiteralArgumentBuilder<CommandSource> = literal("set-spawn")
