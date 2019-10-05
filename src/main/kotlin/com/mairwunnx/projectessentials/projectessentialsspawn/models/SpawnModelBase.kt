package com.mairwunnx.projectessentials.projectessentialsspawn.models

import com.mairwunnx.projectessentials.projectessentialsspawn.helpers.MOD_CONFIG_FOLDER
import com.mairwunnx.projectessentials.projectessentialsspawn.helpers.SPAWN_CONFIG
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.minecraft.util.math.BlockPos
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager
import java.io.File

@UseExperimental(UnstableDefault::class)
object SpawnModelBase {
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
        if (!File(SPAWN_CONFIG).exists()) {
            logger.warn("        - spawn config not exist! creating it now!")
            createConfigDirs(MOD_CONFIG_FOLDER)
            val defaultConfig = json.stringify(
                SpawnModel.serializer(),
                spawnModel
            )
            File(SPAWN_CONFIG).writeText(defaultConfig)
        }
        val spawnConfigRaw = File(SPAWN_CONFIG).readText()
        spawnModel = Json.parse(SpawnModel.serializer(), spawnConfigRaw)
    }

    fun assignSpawn(event: FMLServerStartingEvent) {
        event.server.worlds.forEach {
            val dim = it.dimension.type.id
            event.server.getWorld()
            if (dim == 0) {
                val pos = spawnModel
                it.spawnPoint = BlockPos(pos.x, pos.y, pos.z)
            }
        }
    }

    fun saveData() {
        logger.info("    - saving world spawn data ...")
        createConfigDirs(MOD_CONFIG_FOLDER)
        val spawnConfig = json.stringify(
            SpawnModel.serializer(),
            spawnModel
        )
        File(SPAWN_CONFIG).writeText(spawnConfig)
    }

    private fun createConfigDirs(path: String) {
        logger.info("        - creating config directory for world spawn data ($path)")
        val configDirectory = File(path)
        if (!configDirectory.exists()) configDirectory.mkdirs()
    }
}
