package com.mairwunnx.projectessentials.projectessentialsspawn.extensions

import net.minecraft.command.CommandSource
import net.minecraft.util.text.TranslationTextComponent

fun sendMsg(
    commandSource: CommandSource,
    l10nString: String,
    vararg args: String
) {
    commandSource.sendFeedback(
        TranslationTextComponent(
            "project_essentials_spawn.$l10nString", *args
        ), false
    )
}
