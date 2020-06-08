package com.mairwunnx.projectessentials.spawn.configurations

import kotlinx.serialization.Serializable

@Serializable
data class SpawnConfigurationModel(
    var dimensionId: Int = 0,
    var xPos: Int = 0,
    var yPos: Int = 100,
    var zPos: Int = 0,
    var yaw: Float = 0f,
    var pitch: Float = 0f
)
