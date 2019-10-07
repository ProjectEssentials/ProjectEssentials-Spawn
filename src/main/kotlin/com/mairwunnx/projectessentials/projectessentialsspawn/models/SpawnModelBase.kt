package com.mairwunnx.projectessentials.projectessentialsspawn.models

import com.mairwunnx.projectessentialscore.helpers.MOD_CONFIG_FOLDER
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.dimension.DimensionType
import org.apache.logging.log4j.LogManager
import java.io.File

@UseExperimental(UnstableDefault::class)
object SpawnModelBase {
    private val spawnConfig = MOD_CONFIG_FOLDER + File.separator + "spawn.json"
    private val logger = LogManager.getLogger()
    var spawnModel = SpawnModel()
    private val json = Json(
        JsonConfiguration(
            encodeDefaults = true,
            strictMode = true,
            unquoted = false,
            allowStructuredMapKeys = true,
            prettyPrint = true,
            useArrayPolymorphism = false
        )
    )

    fun loadData() {
        logger.info("    - loading world spawn data ...")
        logger.debug("        - setup json configuration for parsing ...")
        if (!File(spawnConfig).exists()) {
            logger.warn("        - spawn config not exist! creating it now!")
            createConfigDirs(MOD_CONFIG_FOLDER)
            val defaultConfig = json.stringify(
                SpawnModel.serializer(),
                spawnModel
            )
            File(spawnConfig).writeText(defaultConfig)
        }
        val spawnConfigRaw = File(spawnConfig).readText()
        spawnModel = Json.parse(SpawnModel.serializer(), spawnConfigRaw)
    }

    fun assignSpawn(server: MinecraftServer) {
        val xPos = spawnModel.xPos
        val yPos = spawnModel.yPos
        val zPos = spawnModel.zPos
        server.getWorld(DimensionType.OVERWORLD).spawnPoint = BlockPos(
            xPos, yPos, zPos
        )
    }

    fun saveData() {
        logger.info("    - saving world spawn data ...")
        createConfigDirs(MOD_CONFIG_FOLDER)
        val spawnConfig = json.stringify(
            SpawnModel.serializer(),
            spawnModel
        )
        File(this.spawnConfig).writeText(spawnConfig)
    }

    @Suppress("SameParameterValue")
    private fun createConfigDirs(path: String) {
        logger.info("        - creating config directory for world spawn data ($path)")
        val configDirectory = File(path)
        if (!configDirectory.exists()) configDirectory.mkdirs()
    }
}
