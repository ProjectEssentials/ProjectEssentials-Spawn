@file:Suppress("unused")

package com.mairwunnx.projectessentials.spawn

import com.mairwunnx.projectessentials.core.api.v1.IMCLocalizationMessage
import com.mairwunnx.projectessentials.core.api.v1.IMCProvidersMessage
import com.mairwunnx.projectessentials.core.api.v1.events.ModuleEventAPI.subscribeOn
import com.mairwunnx.projectessentials.core.api.v1.events.forge.ForgeEventType
import com.mairwunnx.projectessentials.core.api.v1.events.forge.InterModEnqueueEventData
import com.mairwunnx.projectessentials.core.api.v1.module.IModule
import com.mairwunnx.projectessentials.spawn.commands.SetSpawnCommand
import com.mairwunnx.projectessentials.spawn.commands.SpawnCommand
import com.mairwunnx.projectessentials.spawn.configurations.SpawnConfiguration
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.fml.InterModComms
import net.minecraftforge.fml.common.Mod

@Mod("project_essentials_spawn")
class ModuleObject : IModule {
    override val name = this::class.java.`package`.implementationTitle.split(" ").last()
    override val version = this::class.java.`package`.implementationVersion!!
    override val loadIndex = 3
    override fun init() = Unit

    init {
        EVENT_BUS.register(this)
        subscribeOn<InterModEnqueueEventData>(
            ForgeEventType.EnqueueIMCEvent
        ) {
            sendLocalizationRequest()
            sendProvidersRequest()
        }
    }

    private fun sendLocalizationRequest() {
        InterModComms.sendTo(
            "project_essentials_core",
            IMCLocalizationMessage
        ) {
            fun() = mutableListOf(
                "/assets/projectessentialsspawn/lang/en_us.json",
                "/assets/projectessentialsspawn/lang/ru_ru.json",
                "/assets/projectessentialsspawn/lang/zh_cn.json",
                "/assets/projectessentialsspawn/lang/de_de.json"
            )
        }
    }

    private fun sendProvidersRequest() {
        InterModComms.sendTo(
            "project_essentials_core",
            IMCProvidersMessage
        ) {
            fun() = listOf(
                SpawnConfiguration::class.java,
                ModuleObject::class.java,
                SetSpawnCommand::class.java,
                SpawnCommand::class.java
            )
        }
    }
}
