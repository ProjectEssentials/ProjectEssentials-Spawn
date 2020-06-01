package com.mairwunnx.projectessentials.spawn.configurations

import com.mairwunnx.projectessentials.core.api.v1.configuration.IConfiguration
import com.mairwunnx.projectessentials.core.api.v1.helpers.jsonInstance
import com.mairwunnx.projectessentials.core.api.v1.helpers.projectConfigDirectory
import com.mairwunnx.projectessentials.spawn.firstLaunch
import net.minecraftforge.fml.server.ServerLifecycleHooks
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileNotFoundException

object SpawnConfiguration : IConfiguration<SpawnConfigurationModel> {
    private val logger = LogManager.getLogger()
    private var configurationData = SpawnConfigurationModel()

    override val name = "spawn"
    override val version = 1
    override val configuration = take()
    override val path by lazy {
        projectConfigDirectory + File.separator + ServerLifecycleHooks.getCurrentServer().folderName + File.separator + "spawn.json"
    }

    override fun load() {
        try {
            val configRaw = File(path).readText()
            configurationData = jsonInstance.parse(
                SpawnConfigurationModel.serializer(), configRaw
            )
        } catch (ex: FileNotFoundException) {
            logger.error("Configuration file ($path) not found!")
            logger.warn("The default configuration will be used")
            firstLaunch = true
        }
    }

    override fun save() {
        File(path).parentFile.mkdirs()

        logger.info("Saving configuration `${name}`")
        val raw = jsonInstance.stringify(
            SpawnConfigurationModel.serializer(), configuration
        )
        try {
            File(path).writeText(raw)
        } catch (ex: SecurityException) {
            logger.error("An error occurred while saving $name configuration", ex)
        }
    }

    override fun take() = configurationData
}
