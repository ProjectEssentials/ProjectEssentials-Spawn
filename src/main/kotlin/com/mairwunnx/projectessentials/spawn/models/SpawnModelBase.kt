package com.mairwunnx.projectessentials.spawn.models

import com.mairwunnx.projectessentials.core.helpers.MOD_CONFIG_FOLDER
import com.mairwunnx.projectessentials.core.helpers.jsonInstance
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.dimension.DimensionType
import org.apache.logging.log4j.LogManager
import java.io.File

object SpawnModelBase {
    private val spawnConfig = MOD_CONFIG_FOLDER + File.separator + "spawn.json"
    private val logger = LogManager.getLogger()
    var spawnModel = SpawnModel()

    fun loadData() {
        logger.info("Loading world spawn data")
        if (!File(spawnConfig).exists()) {
            logger.warn("Spawn config not exist! creating it now!")
            File(MOD_CONFIG_FOLDER).mkdirs()
            val defaultConfig = jsonInstance.stringify(
                SpawnModel.serializer(), spawnModel
            )
            File(spawnConfig).writeText(defaultConfig)
        }
        val spawnConfigRaw = File(spawnConfig).readText()
        spawnModel = jsonInstance.parse(SpawnModel.serializer(), spawnConfigRaw)
        logger.info("Spawn config loaded: $spawnModel")
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
        File(MOD_CONFIG_FOLDER).mkdirs()
        val spawnConfig = jsonInstance.stringify(
            SpawnModel.serializer(), spawnModel
        )
        File(this.spawnConfig).writeText(spawnConfig)
    }
}
